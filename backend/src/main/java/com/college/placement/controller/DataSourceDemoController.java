package com.college.placement.controller;

import com.college.placement.dto.DataSourceInfoResponse;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

/**
 * TECH TALK: DataSource Inspection Endpoint
 * Inspects the runtime DataSource bean injected by Spring ApplicationContext.
 * Shows Hikari pool name, maximum pool size, and JDBC URL to verify auto-config vs override.
 */
@RestController
@RequestMapping("/api/v1/demo")
public class DataSourceDemoController {

    private final DataSource dataSource;

    public DataSourceDemoController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/datasource")
    public DataSourceInfoResponse getDataSourceInfo() {
        if (dataSource instanceof HikariDataSource hikari) {
            return new DataSourceInfoResponse(
                    hikari.getClass().getName(),
                    hikari.getPoolName(),
                    maskUrl(hikari.getJdbcUrl()),
                    hikari.getMaximumPoolSize()
            );
        }
        return new DataSourceInfoResponse(
                dataSource.getClass().getName(),
                "NON-HIKARI-POOL",
                "unknown",
                -1
        );
    }

    private String maskUrl(String url) {
        if (url == null) return "n/a";
        return url.replaceAll("(?i)(password=)[^&]*", "$1*****");
    }
}
