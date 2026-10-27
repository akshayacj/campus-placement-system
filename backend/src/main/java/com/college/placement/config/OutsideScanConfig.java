package com.college.placement.config;

import com.college.outside.ComponentScanProofBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * TECH TALK: Overcoming ComponentScan Boundaries
 * When profile 'scan-demo' is active, this configuration explicitly imports
 * ComponentScanProofBean from com.college.outside into the ApplicationContext.
 */
@Configuration
@Profile("scan-demo")
@Import(ComponentScanProofBean.class)
public class OutsideScanConfig {
}

