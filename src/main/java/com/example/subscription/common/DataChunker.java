package com.example.subscription.common;

import lombok.Builder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Sahand Jalilvand 21.01.24
 */
@Builder
public class DataChunker<T> {

    private Function<Pageable, List<T>> dataSupplier;
    private Consumer<T> dataConsumer;
    private Integer chunkSize;


    public void chunking() {
        validate();

        int page = 0;
        Collection<T> collection;
        do {
            collection = dataSupplier.apply(PageRequest.of(page++, chunkSize));
            collection.forEach(dataConsumer);
        } while (!collection.isEmpty());
    }

    private void validate() {
        if (dataConsumer == null || dataSupplier == null || chunkSize == null || chunkSize.equals(0)) {
            throw new IllegalStateException();
        }
    }

}
