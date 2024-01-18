package com.example.subscription.config.cdc;

import com.example.subscription.outbox.core.ChangedDataCollector;
import io.debezium.embedded.EmbeddedEngine;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Sahand Jalilvand 16.01.24
 */
@Component
@Slf4j
public class CdcEngine {

    private final Executor executor = Executors.newSingleThreadExecutor();

    private final EmbeddedEngine engine;

    public CdcEngine(Properties cdcConnector, ChangedDataCollector changedDataCollector) {
        this.engine = (EmbeddedEngine) new EmbeddedEngine.EngineBuilder()
                .notifying(changedDataCollector::handleEvent)
                .using(cdcConnector)
                .build();
    }

    @PostConstruct
    private void start() {
        this.executor.execute(engine);
    }

    @PreDestroy
    private void stop() {
        if (this.engine != null) {
            this.engine.stop();
        }
    }


}
