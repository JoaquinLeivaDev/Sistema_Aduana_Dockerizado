package cl.duoc.api_gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class GatewayIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testGatewayCheckEndpoint() {
        webTestClient.get()
                .uri("/gateway/check") // Probamos el endpoint que agregamos
                .exchange()
                .expectStatus().isOk() // Verificamos que responde 200
                .expectBody(String.class).isEqualTo("Gateway esta corriendo :D!");
    }
}