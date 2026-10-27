package com.college.placement.demo.autoconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * TECH TALK: Auto-Configuration Evaluation Summary
 * Activated with profile 'report'. Summarizes ConditionEvaluationReport
 * (positive vs negative matches) so tech talk viewers see exact counts.
 */
@Component
@Profile("report")
public class AutoConfigReportPrinter {

    private static final Logger log = LoggerFactory.getLogger(AutoConfigReportPrinter.class);
    private final ConfigurableListableBeanFactory beanFactory;

    public AutoConfigReportPrinter(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void printReport() {
        ConditionEvaluationReport report = ConditionEvaluationReport.get(beanFactory);
        int positiveCount = 0;
        int negativeCount = 0;

        for (var entry : report.getConditionAndOutcomesBySource().entrySet()) {
            if (entry.getValue().isFullMatch()) {
                positiveCount++;
            } else {
                negativeCount++;
            }
        }

        log.info("======================================================================");
        log.info("[TECH TALK AUTOCONFIG SUMMARY]");
        log.info("Positive Auto-Config Matches : {}", positiveCount);
        log.info("Negative Auto-Config Matches : {}", negativeCount);
        log.info("Total Evaluated Configs      : {}", report.getConditionAndOutcomesBySource().size());
        log.info("======================================================================");
    }
}

