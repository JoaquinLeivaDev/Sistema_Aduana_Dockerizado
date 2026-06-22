package cl.duocuc.aduana_api;

import cl.duocuc.aduana_api.repository.DocumentoRepository;
import cl.duocuc.aduana_api.repository.PasajeroRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class AduanaApiApplicationTests {

	@MockBean
	private DocumentoRepository documentoRepository;

	@MockBean
	private PasajeroRepository pasajeroRepository;

	@Test
	void contextLoads() { }
}