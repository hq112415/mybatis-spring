package org.mybatis.spring.config;

import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import java.lang.annotation.Annotation;

/**
 * A {#code BeanDefinitionParser} that handles the element scan of the MyBatis. namespace
 *
 * @author Lishu Luo
 * @author Eduardo Macarron
 * @see MapperFactoryBean
 * @see ClassPathMapperScanner
 * @see MapperScannerConfigurer
 * @since 1.2.0
 */

public class MapperScannerBeanDefinitionParser extends AbstractBeanDefinitionParser {

    private static final String ATTRIBUTE_BASE_PACKAGE = "base-package";
    private static final String ATTRIBUTE_ANNOTATION = "annotation";
    private static final String ATTRIBUTE_MARKER_INTERFACE = "marker-interface";
    private static final String ATTRIBUTE_NAME_GENERATOR = "name-generator";
    private static final String ATTRIBUTE_TEMPLATE_REF = "template-ref";
    private static final String ATTRIBUTE_FACTORY_REF = "factory-ref";
    private static final String ATTRIBUTE_MAPPER_FACTORY_BEAN_CLASS = "mapper-factory-bean-class";
    private static final String ATTRIBUTE_LAZY_INITIALIZATION = "lazy-initialization";

    /**
     * {@inheritDoc}
     *
     * @since 2.0.2
     */
    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);

        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();

        builder.addPropertyValue("processPropertyPlaceHolders", true);
        try {
            String annotationClassName = element.getAttribute(ATTRIBUTE_ANNOTATION);
            if (StringUtils.hasText(annotationClassName)) {
                @SuppressWarnings("unchecked")
                Class<? extends Annotation> annotationClass = (Class<? extends Annotation>) classLoader
                        .loadClass(annotationClassName);
                builder.addPropertyValue("annotationClass", annotationClass);
            }
            String markerInterfaceClassName = element.getAttribute(ATTRIBUTE_MARKER_INTERFACE);
            if (StringUtils.hasText(markerInterfaceClassName)) {
                Class<?> markerInterface = classLoader.loadClass(markerInterfaceClassName);
                builder.addPropertyValue("markerInterface", markerInterface);
            }
            String nameGeneratorClassName = element.getAttribute(ATTRIBUTE_NAME_GENERATOR);
            if (StringUtils.hasText(nameGeneratorClassName)) {
                Class<?> nameGeneratorClass = classLoader.loadClass(nameGeneratorClassName);
                BeanNameGenerator nameGenerator = BeanUtils.instantiateClass(nameGeneratorClass, BeanNameGenerator.class);
                builder.addPropertyValue("nameGenerator", nameGenerator);
            }
            String mapperFactoryBeanClassName = element.getAttribute(ATTRIBUTE_MAPPER_FACTORY_BEAN_CLASS);
            if (StringUtils.hasText(mapperFactoryBeanClassName)) {
                @SuppressWarnings("unchecked")
                Class<? extends MapperFactoryBean> mapperFactoryBeanClass = (Class<? extends MapperFactoryBean>) classLoader
                        .loadClass(mapperFactoryBeanClassName);
                builder.addPropertyValue("mapperFactoryBeanClass", mapperFactoryBeanClass);
            }
        } catch (Exception ex) {
            XmlReaderContext readerContext = parserContext.getReaderContext();
            readerContext.error(ex.getMessage(), readerContext.extractSource(element), ex.getCause());
        }

        builder.addPropertyValue("sqlSessionTemplateBeanName", element.getAttribute(ATTRIBUTE_TEMPLATE_REF));
        builder.addPropertyValue("sqlSessionFactoryBeanName", element.getAttribute(ATTRIBUTE_FACTORY_REF));
        builder.addPropertyValue("lazyInitialization", element.getAttribute(ATTRIBUTE_LAZY_INITIALIZATION));
        builder.addPropertyValue("basePackage", element.getAttribute(ATTRIBUTE_BASE_PACKAGE));

        return builder.getBeanDefinition();
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.0.2
     */
    @Override
    protected boolean shouldGenerateIdAsFallback() {
        return true;
    }

}
