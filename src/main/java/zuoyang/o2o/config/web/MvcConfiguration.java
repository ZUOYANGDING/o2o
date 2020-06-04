package zuoyang.o2o.config.web;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import zuoyang.o2o.interceptor.TestInterceptor;

import javax.annotation.Resource;

@Configuration
@EnableWebMvc
public class MvcConfiguration implements WebMvcConfigurer {
//    private ApplicationContext applicationContext;
//
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//    }
//
    // setup static resources
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**", "/css/**", "/js/**")
                .addResourceLocations("classpath:/static/img/", "classpath:/static/css/", "classpath:/static/js/");

    }

    // setup default request handler
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    // create view Resolver
//    @Bean(name="viewResolver")
//    public ViewResolver createViewResolver() {
//        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
//        // setup spring container
//        internalResourceViewResolver.setApplicationContext(this.applicationContext);
//        // disable cache
//        internalResourceViewResolver.setCache(false);
//        // setup resolver prefix
//        internalResourceViewResolver.setPrefix("templates/");
//        // setup resolver suffix
//        internalResourceViewResolver.setSuffix(".html");
//        return internalResourceViewResolver;
//    }
}
