package com.college.placement.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * TECH TALK: Custom DataSource Override (@ConditionalOnMissingBean)
 * Shows how defining a user bean backs off Spring Boot's auto-configured DataSource.
 * Hikari pool name is customized to PLACEMENT-CUSTOM-POOL with maximumPoolSize 5.
 *
 * Spring Boot's DataSourceConfiguration.Hikari declares:
 *   @ConditionalOnMissingBean(DataSource.class)
 * Activating profile 'custom-datasource' causes DataSourceAutoConfiguration to yield.
 *
 * Alternative: To completely exclude an auto-configuration class from evaluation:
 *   YAML: spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
 *   Java: @SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
 */
@Configuration
@Profile("custom-datasource")
public class CustomDataSourceConfig {

    @Value("${spring.datasource.url:jdbc:mysql://localhost:3306/placement_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC}")
    private String url;

    @Value("${spring.datasource.username:placement_user}")
    private String username;

    @Value("${spring.datasource.password:placement_pass}")
    private String password;

    @Value("${spring.datasource.driver-class-name:com.mysql.cj.jdbc.Driver}")
    private String driverClassName;

    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setPoolName("PLACEMENT-CUSTOM-POOL");
        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(driverClassName);
        ds.setMaximumPoolSize(5);
        ds.setLeakDetectionThreshold(2000);
        ds.setConnectionTimeout(30000);
        return ds;
    }
}

