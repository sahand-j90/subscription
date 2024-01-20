package com.example.subscription.common;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

/**
 * @author Sahand Jalilvand 21.01.24
 */
@Component
public class TransactionalService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void doInTransaction(Runnable func) {
        doInTransaction(() -> {
            func.run();
            return null;
        });
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public <T> T doInTransaction(Supplier<T> func) {
        return func.get();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    public void doInNewTransaction(Runnable func) {
        doInNewTransaction(() -> {
            func.run();
            return null;
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    public <T> T doInNewTransaction(Supplier<T> func) {
        return func.get();
    }

}
