package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * @EnableJpaRepositories
 * basePackages : repository의 패키지 위치
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.demo.domain.repository",
        entityManagerFactoryRef = "userEntityManager",
        transactionManagerRef = "userTransactionManager")
public class DatabasConfig {

    @Autowired
    private Environment env;

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean userEntityManager(){
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(userDataSource());
        em.setPackagesToScan(new String[] {"com.example.demo.domain"}); // @entity annotation이 붙어있는 위치

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();

        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.use_sql_comments", "true");

        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean
    public DataSource userDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver"); //com.mysql.jdbc.Driver
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/jpastart?autoReconnect=true&useSSL=false&verifyServerCertificate=false&characterEncoding=utf8&characterSetResults=utf8&&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");

        return dataSource;
    }

    @Primary
    @Bean
    public PlatformTransactionManager userTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(userEntityManager().getObject());

        return transactionManager;
    }
}
