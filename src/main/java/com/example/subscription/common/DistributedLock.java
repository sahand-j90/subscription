package com.example.subscription.common;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

/**
 * @author Sahand Jalilvand 21.01.24
 */
@Service
@RequiredArgsConstructor
public class DistributedLock {

    private final RedissonClient redissonClient;

    public boolean rateLimit(String key, int permits, int interval, RateIntervalUnit unit) {

        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);

        if (!rateLimiter.isExists()) {
            rateLimiter.setRate(RateType.OVERALL, permits, interval, unit);
        }

        return rateLimiter.tryAcquire();
    }

    public boolean tryLock(String name) {
        return redissonClient.getLock(name).tryLock();
    }

    public void lock(String name) {
        redissonClient.getLock(name).lock();
    }

    public boolean unlock(String name) {
        return redissonClient.getLock(name).forceUnlock();
    }
}
