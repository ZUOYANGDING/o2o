package zuoyang.o2o.config.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyVetoException;

@Configuration
@MapperScan("zuoyang.o2o.dao")
public class DataSourceConfiguration {
    @Value("${jdbc.driver}")
    private String jdbcDriver;
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String jdbcUserName;
    @Value("${jdbc.password}")
    private String jdbcPassword;

    @Bean(name="dataSource")
    public ComboPooledDataSource createDataSource() throws PropertyVetoException {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        // set driver
        comboPooledDataSource.setDriverClass(jdbcDriver);
        // set url
        comboPooledDataSource.setJdbcUrl(jdbcUrl);
        // set username
        comboPooledDataSource.setUser(jdbcUserName);
        // set password
        comboPooledDataSource.setPassword(jdbcPassword);
        return comboPooledDataSource;
    }

}
