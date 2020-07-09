package zuoyang.o2o.config.web;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
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
    @Value("${replace_file_path}")
    private String replaceFilePath;

    // setup static resources
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**", "/css/**", "/js/**")
                .addResourceLocations("classpath:/static/img/", "classpath:/static/css/", "classpath:/static/js/");
        registry.addResourceHandler("/upload/**").
                addResourceLocations("file:" + replaceFilePath);
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


    /**
     * set up bean for kaptcha
     */
    @Value("${kaptcha.border}")
    private String border;

    @Value("${kaptcha.textproducer.font.color}")
    private String fontColor;

    @Value("${kaptcha.image.width}")
    private String imgWidth;

    @Value("${kaptcha.textproducer.char.string}")
    private String charString;

    @Value("${kaptcha.image.height}")
    private String imgHeight;

    @Value("${kaptcha.textproducer.font.size}")
    private String fontSize;

    @Value("${kaptcha.noise.color}")
    private String noiseColor;

    @Value("${kaptcha.textproducer.char.length}")
    private String charLength;

    @Value("${kaptcha.textproducer.font.names}")
    private String fontName;

    @Bean
    public ServletRegistrationBean<KaptchaServlet> servletServletRegistrationBean() throws SecurityException {
        ServletRegistrationBean<KaptchaServlet> kaptchaServlet =
                new ServletRegistrationBean<>(new KaptchaServlet(), "/Kaptcha");
        kaptchaServlet.addInitParameter("kaptcha.border", border);
        kaptchaServlet.addInitParameter("kaptcha.textproducer.font.color", fontColor);
        kaptchaServlet.addInitParameter("kaptcha.image.width", imgWidth);
        kaptchaServlet.addInitParameter("kaptcha.image.height", imgHeight);
        kaptchaServlet.addInitParameter("kaptcha.textproducer.char.string", charString);
        kaptchaServlet.addInitParameter("kaptcha.textproducer.font.size", fontSize);
        kaptchaServlet.addInitParameter("kaptcha.noise.color", noiseColor);
        kaptchaServlet.addInitParameter("kaptcha.textproducer.char.length", charLength);
        kaptchaServlet.addInitParameter("kaptcha.textproducer.font.names", fontName);
        return kaptchaServlet;
    }

    /**
     * set up the file upload resolver
     * @return
     */
    @Bean(name="multipartResolver")
    public CommonsMultipartResolver createMultipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("utf-8");
        // 1024*1024*20 = 20M
        resolver.setMaxUploadSize(20971520);
        resolver.setMaxInMemorySize(20971520);
        return resolver;
    }

}
