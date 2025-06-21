package RatelimitingAPI.Service;

import RatelimitingAPI.Config.RateLimitConfig;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RedisRateLimiterService {
    private static final String RATE_LIMIT_PREFIX = "rate_limit:";

    private final ReactiveStringRedisTemplate redisOperations;

    public RedisRateLimiterService(ReactiveStringRedisTemplate redisOperations) {
        this.redisOperations = redisOperations;
    }

    public Mono<Void> setRateLimit(String endpoint, RateLimitConfig config) {
        String key = RATE_LIMIT_PREFIX + endpoint;
        String value = config.getReplenishRate() + "," + config.getBurstCapacity();
        return redisOperations.opsForValue().set(key, value).then();
    }

    public Mono<RateLimitConfig> getRateLimit(String endpoint) {
        String key = RATE_LIMIT_PREFIX + endpoint;
        return redisOperations.opsForValue().get(key)
                .map(value -> {
                    String[] parts = value.split(",");
                    return new RateLimitConfig(endpoint, Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                })
                .defaultIfEmpty(new RateLimitConfig(endpoint, 2, 5));
    }
}
