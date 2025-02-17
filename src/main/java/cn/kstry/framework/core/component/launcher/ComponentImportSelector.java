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
package cn.kstry.framework.core.component.launcher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import cn.kstry.framework.core.container.processor.ImmutablePostProcessor;
import cn.kstry.framework.core.container.processor.MarkIndexPostProcessor;
import cn.kstry.framework.core.container.processor.RearrangeFlowPostProcessor;
import cn.kstry.framework.core.container.processor.StartEventPostProcessor;
import cn.kstry.framework.core.container.processor.VerifyFlowPostProcessor;

/**
 * Spring 容器中注册组件
 * <p>
 * Spring 4.2.x 之前的版本无法使用 @Import 导入一个普通的 Class
 *
 * @author lyakn
 */
@Import({ConfigResourceResolver.class, KstryContextResolver.class})
public class ComponentImportSelector {

    @Bean
    public StartEventPostProcessor getImmutablePostProcessor() {
        return new ImmutablePostProcessor();
    }

    @Bean
    public StartEventPostProcessor getMarkIndexPostProcessor() {
        return new MarkIndexPostProcessor();
    }

    @Bean
    public StartEventPostProcessor getRearrangeFlowPostProcessor() {
        return new RearrangeFlowPostProcessor();
    }

    @Bean
    public StartEventPostProcessor getVerifyFlowPostProcessor() {
        return new VerifyFlowPostProcessor();
    }
}
