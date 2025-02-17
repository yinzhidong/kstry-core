/*
 *
 *  * Copyright (c) 2020-2021, Lykan (jiashuomeng@gmail.com).
 *  * <p>
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  * <p>
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  * <p>
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package cn.kstry.framework.core.resource.config;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.Resource;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.kstry.framework.core.kv.BasicKValue;
import cn.kstry.framework.core.util.GlobalUtil;

/**
 * @author lykan
 */
public class BasicPropertiesConfigResource extends AbstractConfigResource implements PropertiesConfigResource {

    /**
     * 资源
     */
    private final Resource resource;

    public BasicPropertiesConfigResource(Resource resource) {
        super(resource);
        this.resource = resource;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BasicKValue> getKValueList() {
        YamlMapFactoryBean yamlMapFactoryBean = new YamlMapFactoryBean();
        yamlMapFactoryBean.setResources(resource);
        yamlMapFactoryBean.afterPropertiesSet();
        Map<String, BasicKValue> kValueMap = Maps.newHashMap();
        yamlMapFactoryBean.getObject().forEach((k, v) -> {
            if (!(v instanceof Map)) {
                return;
            }
            BasicKValue kValue = kValueMap.computeIfAbsent(k, BasicKValue::new);
            ((Map<Object, Object>) v).forEach((inK, inV) -> {
                String inKey = GlobalUtil.transferNotEmpty(inK, String.class);
                kValue.addValue(inKey, inV);
            });
        });
        return Lists.newArrayList(kValueMap.values());
    }
}
