package cl.duocuc.aduana_api;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


@Disabled("Desactivado porque contextLoads levanta el contexto completo y requiere conexión a BD Oracle")
class AduanaApiApplicationTests {
	@Test
	void contextLoads() {
		// Desactivado para evitar que la prueba intente conectar a Oracle.
	}
}
