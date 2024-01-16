package com.example.subscription.config.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Sahand Jalilvand 16.01.24
 */
@Component
@ConfigurationProperties(prefix = RdbmsPropsHolder.PREFIX)
@Getter
@Setter
public class RdbmsPropsHolder {

    public static final String PREFIX = "subscription.rdbms";

    String server;

    String port;

    String schema;

    String username;

    String pass;
}
