package com.college.placement.config;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

class CustomDataSourceConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    void defaultProfileUsesAutoConfiguredDataSource() {
        contextRunner
                .withConfiguration(AutoConfigurations.of(DataSourceAutoConfiguration.class))
                .withPropertyValues(
                        "spring.datasource.url=jdbc:h2:mem:testdb",
                        "spring.datasource.driver-class-name=org.h2.Driver"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(DataSource.class);
                    DataSource ds = context.getBean(DataSource.class);
                    assertThat(ds).isInstanceOf(HikariDataSource.class);
                    HikariDataSource hikari = (HikariDataSource) ds;
                    assertThat(hikari.getPoolName()).isNotEqualTo("PLACEMENT-CUSTOM-POOL");
                });
    }

    @Test
    void customDataSourceProfileOverridesAutoConfiguration() {
        contextRunner
                .withUserConfiguration(CustomDataSourceConfig.class)
                .withConfiguration(AutoConfigurations.of(DataSourceAutoConfiguration.class))
                .withPropertyValues(
                        "spring.profiles.active=custom-datasource",
                        "spring.datasource.url=jdbc:h2:mem:customdb",
                        "spring.datasource.driver-class-name=org.h2.Driver"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(DataSource.class);
                    DataSource ds = context.getBean(DataSource.class);
                    assertThat(ds).isInstanceOf(HikariDataSource.class);
                    HikariDataSource hikari = (HikariDataSource) ds;
                    assertThat(hikari.getPoolName()).isEqualTo("PLACEMENT-CUSTOM-POOL");
                    assertThat(hikari.getMaximumPoolSize()).isEqualTo(5);
                });
    }
}
