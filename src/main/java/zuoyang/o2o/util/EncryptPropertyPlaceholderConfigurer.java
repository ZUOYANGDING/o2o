package zuoyang.o2o.util;


import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

@Configuration
public class EncryptPropertyPlaceholderConfigurer implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent) {
        ConfigurableEnvironment environment = applicationEnvironmentPreparedEvent.getEnvironment();
        String encryptUser = environment.getProperty("jdbc.username");
        String encryptPassword = environment.getProperty("jdbc.password");
        System.out.println("===================");
        System.out.println(encryptUser);
        System.out.println("===================");
        System.out.println(encryptPassword);
        String decryptUser = DESUtil.getDecryptString(encryptUser);
        String decryptPassword = DESUtil.getDecryptString(encryptPassword);
        System.out.println("===================");
        System.out.println(decryptUser);
        System.out.println("===================");
        System.out.println(decryptPassword);
        Properties properties = new Properties();
        properties.put("jdbc.username", decryptUser);
        properties.put("jdbc.password", decryptPassword);
        environment.getPropertySources().addFirst(
                new PropertiesPropertySource("application-dev.properties", properties));
    }
}
