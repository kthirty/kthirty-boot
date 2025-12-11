package top.kthirty.core.boot.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

public class ImportIgnorePostProcessor implements BeanDefinitionRegistryPostProcessor {

    private final String currentApplicationClassName;

    public ImportIgnorePostProcessor(Class<?> currentApplication) {
        this.currentApplicationClassName = currentApplication.getName();
        System.out.println("currentApplicationClassName:"+this.currentApplicationClassName);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        for (String name : registry.getBeanDefinitionNames()) {
            BeanDefinition bd = registry.getBeanDefinition(name);
            String beanClassName = bd.getBeanClassName();
            String source = bd.getResourceDescription();
            System.out.println("source:"+source+"beanClassName:"+beanClassName);
            if (beanClassName != null
                    && beanClassName.endsWith("Application") == false // 排除 Application 本身
                    && source != null
                    && source.contains("Application")
                    && !source.contains(currentApplicationClassName)) {
                // 屏蔽其它 Application 的 Import
                registry.removeBeanDefinition(name);
            }
        }
    }

    @Override
    public void postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 不做任何处理
    }
}
