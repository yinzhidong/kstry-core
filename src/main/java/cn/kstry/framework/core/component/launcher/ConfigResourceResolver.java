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

import cn.kstry.framework.core.annotation.EnableKstry;
import cn.kstry.framework.core.exception.ExceptionEnum;
import cn.kstry.framework.core.resource.config.BpmnClassPathConfigSource;
import cn.kstry.framework.core.resource.config.ConfigSource;
import cn.kstry.framework.core.resource.config.PropertiesClassPathConfigSource;
import cn.kstry.framework.core.resource.factory.KValueFactory;
import cn.kstry.framework.core.resource.factory.StartEventFactory;
import cn.kstry.framework.core.util.AssertUtil;
import cn.kstry.framework.core.util.GlobalUtil;
import cn.kstry.framework.core.util.ProxyUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * 配置资源加载
 *
 * @author lykan
 */
public class ConfigResourceResolver implements ApplicationContextAware {

    private ConfigurableApplicationContext applicationContext;

    @Bean
    @Conditional(ConfigResourceResolver.ClassPathSourceCondition.class)
    public ConfigSource getClassPathStartEventResource() {
        return new BpmnClassPathConfigSource(getBpmnPath(this.applicationContext));
    }

    @Bean
    @Conditional(ConfigResourceResolver.PropertiesSourceCondition.class)
    public ConfigSource PropertiesClassPathConfigSource() {
        return new PropertiesClassPathConfigSource(getPropertiesPath(this.applicationContext));
    }

    @Bean
    public StartEventFactory getStartEventFactory() {
        return new StartEventFactory(applicationContext);
    }

    @Bean
    public KValueFactory getKValueFactory() {
        return new KValueFactory(applicationContext);
    }

    @Bean
    @Conditional(ConfigResourceResolver.MissingPropertySourcesPlaceholderConfigurer.class)
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = GlobalUtil.transferNotEmpty(applicationContext, ConfigurableApplicationContext.class);
    }

    private static String getBpmnPath(ListableBeanFactory beanFactory) {
        EnableKstry enableKstryAnn = getEnableKstryAnn(beanFactory);
        return enableKstryAnn.bpmnPath();
    }

    private static String getPropertiesPath(ListableBeanFactory beanFactory) {
        EnableKstry enableKstryAnn = getEnableKstryAnn(beanFactory);
        return enableKstryAnn.propertiesPath();
    }

    private static EnableKstry getEnableKstryAnn(ListableBeanFactory beanFactory) {
        Map<String, Object> enableKstryMap = beanFactory.getBeansWithAnnotation(EnableKstry.class);
        AssertUtil.oneSize(enableKstryMap.values(), ExceptionEnum.ENABLE_KSTRY_NUMBER_ERROR);

        Object target = enableKstryMap.values().iterator().next();
        Class<?> targetClass = ProxyUtil.noneProxyClass(target);
        return AnnotationUtils.findAnnotation(targetClass, EnableKstry.class);
    }

    private static class ClassPathSourceCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            String bpmnPath = getBpmnPath(context.getBeanFactory());
            return StringUtils.isNotBlank(bpmnPath);
        }
    }

    private static class PropertiesSourceCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            String propertiesPath = getPropertiesPath(context.getBeanFactory());
            return StringUtils.isNotBlank(propertiesPath);
        }
    }

    private static class MissingPropertySourcesPlaceholderConfigurer implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Map<String, PropertySourcesPlaceholderConfigurer> beansOfType = context.getBeanFactory().getBeansOfType(PropertySourcesPlaceholderConfigurer.class);
            return MapUtils.isEmpty(beansOfType);
        }
    }
}
