package RatelimitingAPI.Controller;

import RatelimitingAPI.Authentication.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/generateToken")
    public String generateToken(@RequestParam String serviceName) {
        return jwtUtil.generateToken(serviceName);
    }
}
