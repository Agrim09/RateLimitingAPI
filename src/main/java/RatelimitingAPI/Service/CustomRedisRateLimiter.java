package RatelimitingAPI.Service;

import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import java.time.Duration;


@Component
public class CustomRedisRateLimiter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(CustomRedisRateLimiter.class);

    private static final String RATE_LIMIT_PREFIX = "rate_limit:";
    private static final String RATE_LIMIT_COUNTER = "rate_limit:counter:";

    private final ReactiveStringRedisTemplate redisTemplate;

    public CustomRedisRateLimiter(ReactiveStringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        String redisKey = RATE_LIMIT_PREFIX + path;
        String counterKey = RATE_LIMIT_COUNTER + path;

        logger.info("Processing request for path: {}", path);

        return redisTemplate.opsForValue().get(redisKey)
                .flatMap(value -> {
                    String[] parts = value.split(",");
                    int replenishRate = Integer.parseInt(parts[0]);
                    int burstCapacity = Integer.parseInt(parts[1]);

                    logger.info("Checking rate limit: Replenish Rate = {}, Burst Capacity = {}", replenishRate, burstCapacity);

                    return isAllowed(counterKey, replenishRate, burstCapacity)
                            .flatMap(allowed -> {
                                if (allowed) {
                                    logger.info("✅ Request allowed for {}", path);
                                    return chain.filter(exchange);
                                } else {
                                    logger.warn("request blocked for {}", path);
                                    return denyRequest(exchange);
                                }
                            });
                })
                .switchIfEmpty(chain.filter(exchange));
    }

    private Mono<Boolean> isAllowed(String key, int replenishRate, int burstCapacity) {
        String redisKey = "rate_limit:counter:" + key;

        return redisTemplate.opsForValue()
                .increment(redisKey)
                .flatMap(count -> {
                    System.out.println("Incremented key: " + redisKey + " -> Count: " + count);

                    return redisTemplate.opsForValue().get(redisKey)
                            .flatMap(value -> redisTemplate.getExpire(redisKey))
                            .flatMap(ttl -> {
                                if (ttl.isNegative() || ttl.isZero()) {
                                    return redisTemplate.expire(redisKey, Duration.ofSeconds(1))
                                            .thenReturn(count <= burstCapacity);
                                }
                                return Mono.just(count <= burstCapacity);
                            });
                })
                .doOnError(error -> System.err.println("Redis Error: " + error.getMessage()));
    }


    private Mono<Void> denyRequest(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}