package cl.duoc.api_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayCheckController {

    // Endpoint para verificar que el Gateway está arriba
    @GetMapping("/gateway/check")
    public String check() {
        return "Gateway is up and running!";
    }
}