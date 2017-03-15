package com.hzbuvi.quiz.biz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by taylor on 8/23/16.
 * twitter: @taylorwang789
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.*"
//        ,entityManagerFactoryRef = "userEntityManagerFactory"
//        ,transactionManagerRef = "userTransactionManager"
)
@ComponentScan(basePackages = {"com"})
//@PropertySource("classpath:activity-manage.properties")
public class DBConfig {
    private String scanPackage="com";
    private String dialect="org.hibernate.dialect.OracleDialect";
    private String url="jdbc:oracle:thin:@88.88.88.49:49171:xe";
    private String userName="hz";
    private String password="hz";
    private String driverClassName="oracle.jdbc.driver.OracleDriver";

//    @Value("${jdbc.dialect}")
//    private String dialect ;
//    @Value("${jdbc.url}")
//    private String url;
//    @Value("${jdbc.userName}")
//    private String userName;
//    @Value("${jdbc.password}")
//    private String password;
//    @Value("${jdbc.driver}")
//    private String driverClassName;

    @Bean(name = "dataSource")
//    @Qualifier("userDataSource")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        return dataSource;
    }

    //    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan(scanPackage);
        factoryBean.setDataSource(dataSource());
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabasePlatform(dialect);
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        return factoryBean;
    }

    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setPackagesToScan(scanPackage);
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", dialect);
        hibernateProperties.put("hibernate.show_sql", true);
//        hibernateProperties.put("hibernate.enable_lazy_load_no_trans", true );
//        hibernateProperties.put("hibernate.hbm2ddl.auto", "create");//自动建表
        sessionFactoryBean.setHibernateProperties(hibernateProperties);

        return sessionFactoryBean;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(){
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory().getObject());
        tm.setDataSource(dataSource());
        return tm;
    }
}