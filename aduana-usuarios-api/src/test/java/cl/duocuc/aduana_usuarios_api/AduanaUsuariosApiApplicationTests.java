package cl.duocuc.aduana_usuarios_api;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("Desactivado porque contextLoads levanta el contexto completo y puede requerir BD/Eureka")
@SpringBootTest
class AduanaUsuariosApiApplicationTests {

    @Test
    void contextLoads() {
    }
}
