package RatelimitingAPI.Repo;

import RatelimitingAPI.Entity.GatewayRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatewayRouteRepository extends JpaRepository<GatewayRoute, Long> {

    GatewayRoute findByRouteId(String routeId);

    boolean existsByRouteId(String routeId);

    void deleteByRouteId(String routeId);
}
