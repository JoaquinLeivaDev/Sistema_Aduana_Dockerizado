package cl.duoc.api_gateway.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayCheckController {

    // Cantidad de rutas/servicios definidos en application.yaml
    // (aduana-api, usuarios-api, reportes-api, documentos-api)
    private static final int SERVICIOS_CONFIGURADOS = 4;

    // Endpoint para verificar que el Gateway está arriba
    @GetMapping("/gateway/check")
    public Map<String, Object> check() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("mensaje", "Gateway esta corriendo :D!");
        response.put("serviciosConfigurados", SERVICIOS_CONFIGURADOS);
        return response;
    }
}