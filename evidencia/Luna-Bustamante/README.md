\# Evidencias cambio Mockito - Luna Bustamante



Profesor, junto con saludar, dejo en esta carpeta las evidencias del cambio solicitado para mi rama personal Luna-Bustamante.



El cambio lo realicé en la clase UsuarioServiceTest.java, donde ajusté una prueba existente para que utilice Mockito y no dependa de @SpringBootTest.



En la prueba configuré el mock de UsuarioRepository con when(...).thenReturn(...), luego ejecuté el método buscarPorId() del service y validé el resultado usando assertNotNull y assertEquals. También agregué verify() para comprobar que el repository mockeado fue utilizado correctamente.



La prueba se ejecutó correctamente desde Maven y obtuvo resultado BUILD SUCCESS, sin levantar todo el contexto de Spring.



También realicé el commit y push correspondiente a mi rama Luna-Bustamante.



En esta carpeta adjunto las evidencias:



1\. 01\_build\_success.png: muestra que la prueba se ejecutó correctamente.

2\. 02\_push\_luna\_bustamante.png: muestra el push realizado a la rama Luna-Bustamante.

3\. 03\_git\_status\_clean.png: muestra que no quedaron cambios pendientes.



Saludos cordiales,  

Luna Bustamante

