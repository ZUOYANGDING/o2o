package zuoyang.o2o.config.dao;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class SessionFactoryConfiguration {
    private static String mybatisConfigPath;
    private static String mapperPath;
    private final DataSource dataSource;

    public SessionFactoryConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // set path of mybatis-config file
    @Value("${mybatis_config_file}")
    public void setMybatisConfigPath(String mybatisConfigPath) {
        SessionFactoryConfiguration.mybatisConfigPath = mybatisConfigPath;
    }

    // set path of mybatis mapper file
    @Value("${mapper_path}")
    public void setMapperPath(String mapperPath) {
        SessionFactoryConfiguration.mapperPath = mapperPath;
    }

    // path of entities
    @Value("${type_alias_package}")
    private String typeAliasPackage;

    /**
     * 创建sqlSessionFactoryBean 实例 并且设置configtion 设置mapper 映射路径 设置datasource数据源
     *
     * @return
     * @throws IOException
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean createSqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

        // set mybatis configuration scanner path
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(mybatisConfigPath));

        // add mapper scanner path
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver =
                new PathMatchingResourcePatternResolver();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + mapperPath;
        sqlSessionFactoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(packageSearchPath));

        // set data resource
        sqlSessionFactoryBean.setDataSource(dataSource);

        // set type alias resource
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasPackage);
        return sqlSessionFactoryBean;
    }


}
