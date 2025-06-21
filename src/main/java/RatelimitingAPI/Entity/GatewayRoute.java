package RatelimitingAPI.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "GatewayRoute")
public class GatewayRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "route_id")
    private String routeId;

    @Column(name = "path")
    private String path;

    @Column(name = "uri")
    private String uri;

    @Column(name = "rewrite_path")
    private String rewritePath;


}
