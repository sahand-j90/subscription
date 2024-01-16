package com.example.subscription.config.cdc;

import com.example.subscription.listener.outbox.CdcService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

import static io.debezium.data.Envelope.FieldName.AFTER;
import static io.debezium.data.Envelope.FieldName.OPERATION;
import static java.util.stream.Collectors.toMap;

/**
 * @author Sahand Jalilvand 16.01.24
 */
@Component
@RequiredArgsConstructor
public class CdcEventHandler {

    private final CdcService cdcService;

    public void handleEvent(SourceRecord sourceRecord) {

        Struct sourceRecordValue = (Struct) sourceRecord.value();

        if (sourceRecordValue != null) {

            Operation operation = Operation.forCode((String) sourceRecordValue.get(OPERATION));

            if (operation == Operation.CREATE) {

                Struct struct = (Struct) sourceRecordValue.get(AFTER);
                Map<String, Object> payload = struct.schema().fields()
                        .stream()
                        .map(Field::name)
                        .filter(fieldName -> struct.get(fieldName) != null)
                        .map(fieldName -> Pair.of(fieldName, struct.get(fieldName)))
                        .collect(toMap(Pair::getKey, Pair::getValue));

                cdcService.publish(payload);
            }
        }
    }

    @Getter
    enum Operation {

        READ("r"),
        CREATE("c"),
        UPDATE("u"),
        DELETE("d");

        private final String code;

        Operation(String code) {
            this.code = code;
        }

        public static Operation forCode(String code) {

            return Arrays.stream(values())
                    .filter(item -> item.getCode().equalsIgnoreCase(code))
                    .findAny()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }

}
