package RatelimitingAPI.Controller;

import RatelimitingAPI.Config.RateLimitConfig;
import RatelimitingAPI.Service.RedisRateLimiterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ratelimit")
public class RateLimitController {

    private final RedisRateLimiterService rateLimiterService;

    public RateLimitController(RedisRateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @PostMapping("/set")
    public Mono<ResponseEntity<String>> setRateLimit(@RequestBody RateLimitConfig config) {
        return rateLimiterService.setRateLimit(config.getEndpoint(), config)
                .thenReturn(ResponseEntity.ok("Rate limit updated for " + config.getEndpoint()));
    }

    @PostMapping("/get")
    public Mono<ResponseEntity<RateLimitConfig>> getRateLimit(@RequestBody RateLimitConfig config) {
        return rateLimiterService.getRateLimit(config.getEndpoint())
                .map(ResponseEntity::ok);
    }
}
