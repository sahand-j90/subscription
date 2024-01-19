package com.example.subscription.config.cdc;

import com.example.subscription.config.props.CdcPropsHolder;
import com.example.subscription.config.props.RdbmsPropsHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sahand Jalilvand 16.01.24
 */
@Configuration
@RequiredArgsConstructor
public class CdcConfig {

    private final RdbmsPropsHolder rdbmsPropsHolder;
    private final CdcPropsHolder cdcPropsHolder;

    @Bean
    public io.debezium.config.Configuration cdcConnector() {
        return io.debezium.config.Configuration.create()
                .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
                .with("offset.storage",  "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", cdcPropsHolder.getOffsetFile())
                .with("offset.flush.interval.ms", cdcPropsHolder.getFlushInterval())
                .with("name", "postgres-connector")
                .with("topic.prefix", "test")
                .with("database.hostname", rdbmsPropsHolder.getServer())
                .with("database.port", rdbmsPropsHolder.getPort())
                .with("database.user", rdbmsPropsHolder.getUsername())
                .with("database.password", rdbmsPropsHolder.getPass())
                .with("database.dbname", rdbmsPropsHolder.getSchema())
                .with("table.include.list", cdcPropsHolder.getOutboxTable())
                .with("tombstones.on.delete", "false")
                .build();
    }

}
