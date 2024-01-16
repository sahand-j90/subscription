package com.example.subscription.config.cdc;

import com.example.subscription.config.props.CdcPropsHolder;
import com.example.subscription.config.props.RdbmsPropsHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author Sahand Jalilvand 16.01.24
 */
@Configuration
@RequiredArgsConstructor
public class CdcConfig {

    private final RdbmsPropsHolder rdbmsPropsHolder;
    private final CdcPropsHolder cdcPropsHolder;

    @Bean
    public Properties cdcConnector() {

        Properties props = new Properties();

        props.put("connector.class", cdcPropsHolder.getConnectorClass());
        props.put("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
        props.put("offset.storage.file.filename", cdcPropsHolder.getOffsetFile());
        props.put("offset.flush.interval.ms", cdcPropsHolder.getFlushInterval());
        props.put("name", "debezium-connector");
        props.put("database.server.name", "rdms-server");
        props.put("topic.prefix", "test-order");
        props.put("database.hostname", rdbmsPropsHolder.getServer());
        props.put("database.port", rdbmsPropsHolder.getPort());
        props.put("database.user", rdbmsPropsHolder.getUsername());
        props.put("database.password", rdbmsPropsHolder.getPass());
        props.put("database.dbname", rdbmsPropsHolder.getSchema());
        props.put("decimal.handling.mode", "string");
        props.put("wal_level", "logical");
        props.put("plugin.name", "pgoutput");
        props.put("table.include.list", cdcPropsHolder.getOutboxTable());
        props.put("slot.name", "test1");

        return props;
    }

}
