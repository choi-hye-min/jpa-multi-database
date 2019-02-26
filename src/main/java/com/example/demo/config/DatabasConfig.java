package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @EnableJpaRepositories
 * basePackages : repository의 패키지 위치
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.example.demo.domain.repository",
        entityManagerFactoryRef = "entityManager",
        transactionManagerRef = "transactionManager")
public class DatabasConfig {

    @Bean
    public DataSource jpastartDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver"); //com.mysql.jdbc.Driver
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/jpastart?autoReconnect=true&useSSL=false&verifyServerCertificate=false&characterEncoding=utf8&characterSetResults=utf8&&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");

        return dataSource;
    }

    @Bean
    public DataSource jpastartCopyDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver"); //com.mysql.jdbc.Driver
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/jpastart_copy?autoReconnect=true&useSSL=false&verifyServerCertificate=false&characterEncoding=utf8&characterSetResults=utf8&&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");

        return dataSource;
    }

    @Bean
    public DataSource routingDataSource(DataSource jpastartDataSource, DataSource jpastartCopyDataSource){
        AbstractRoutingDataSource routingDataSource = new ReplicationRoutingDataSource(); // 트랜잭션 DataSource 선별

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("write", jpastartDataSource);
        dataSourceMap.put("read", jpastartCopyDataSource);

        routingDataSource.setTargetDataSources(dataSourceMap); // 트랜잭션 DataSource Map
        routingDataSource.setDefaultTargetDataSource(jpastartDataSource); // 트랜잭션 기본 DataSource

        return routingDataSource;
    }

    @Bean
    public DataSource userDaraSource(DataSource routingDataSource){
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManager(DataSource userDaraSource){
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(userDaraSource); // LazyConnectionDataSourceProxy 에서 얻은 Connection Proxy객체 획득
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
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager);

        return transactionManager;
    }
}
