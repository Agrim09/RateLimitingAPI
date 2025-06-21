package RatelimitingAPI.Config;

import RatelimitingAPI.Repo.GatewayRouteRepository;
import RatelimitingAPI.Entity.GatewayRoute;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import org.springframework.core.env.Environment;
import java.util.Arrays;
import java.util.*;
import java.util.Random;

@Configuration
public class ApiGatewayConfig {
    private final GatewayRouteRepository routeRepository;

    public ApiGatewayConfig(GatewayRouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder, Environment env) {
        List<GatewayRoute> routes = routeRepository.findAll();

        RouteLocatorBuilder.Builder routeBuilder = builder.routes();

        for (GatewayRoute route : routes) {
            List<String> instances = Arrays.asList(route.getUri().split(","));

            routeBuilder.route(route.getRouteId(), r -> r
                    .path(route.getPath())
                    .filters(f -> f.rewritePath(route.getRewritePath(), "/ticketing-app/${segment}"))
                    .uri(getRandomInstance(instances))
            );
        }

        return routeBuilder.build();
    }

    private String getRandomInstance(List<String> instances) {
        Random random = new Random();
        return instances.get(random.nextInt(instances.size()));
    }

    @Bean
    public KeyResolver pathKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().toString());
    }
}
