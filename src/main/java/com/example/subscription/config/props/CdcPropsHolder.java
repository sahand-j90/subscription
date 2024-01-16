package com.example.subscription.config.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Sahand Jalilvand 16.01.24
 */
@Component
@ConfigurationProperties(prefix = CdcPropsHolder.PREFIX)
@Getter
@Setter
public class CdcPropsHolder {

    public static final String PREFIX = "subscription.cdc";

    String connectorClass;

    String offsetFile;

    int flushInterval;

    String outboxTable;
}
