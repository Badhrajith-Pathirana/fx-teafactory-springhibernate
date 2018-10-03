package lk.beempz.tf.main;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("file:${user.dir}/resources/dbproperties.properties")
@EnableTransactionManagement
public class HibernateConfig {
    @Autowired
    private Environment env;

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource){
        LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();
        lsfb.setDataSource(dataSource);
        lsfb.setPackagesToScan("lk.beempz.tf.entity");
        lsfb.setHibernateProperties(hibernateProperties());
        return lsfb;
    }

    @Bean
    public DataSource dataSource(){
        BasicDataSource ds = new BasicDataSource();
//        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(env.getRequiredProperty("hibernate.connection.driver_class"));
        ds.setUrl(env.getRequiredProperty("hibernate.connection.url"));
        ds.setUsername(env.getRequiredProperty("hibernate.connection.username"));
        ds.setPassword(env.getRequiredProperty("hibernate.connection.password"));
        ds.setMaxIdle(env.getRequiredProperty("hibernate.max_idle",Integer.class));
        ds.setMaxTotal(env.getRequiredProperty("hibernate.max_size",Integer.class));
        ds.setInitialSize(env.getRequiredProperty("hibernate.init_size",Integer.class));
        return ds;
    }

    private Properties hibernateProperties(){
        Properties prop = new Properties();
        prop.put("hibernate.dialect",env.getRequiredProperty("hibernate.dialect"));
        prop.put("hibernate.show_sql",env.getRequiredProperty("hibernate.show_sql"));
        prop.put("hibernate.hbm2ddl.auto",env.getRequiredProperty("hibernate.hbm2ddl.auto"));
        return prop;
    }

    @Bean
    public PlatformTransactionManager transactionManager(SessionFactory sf){
        return new HibernateTransactionManager(sf);
    }
}
