package com.appril.postprocessor;

import com.appril.datasource.utils.SpringContextHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 提前实例化既作为工具类又做为Bean的BeanDefinition
 * </p>
 *
 * @author chimeng
 * @since 2023/7/28
 */
@Component
public class EarlyInstanceBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory)
                                                                                                       throws BeansException {
        configurableListableBeanFactory.getBean(SpringContextHolder.class);
    }
}
