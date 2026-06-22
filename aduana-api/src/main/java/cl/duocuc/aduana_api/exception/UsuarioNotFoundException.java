// UsuarioNotFoundException.java
package cl.duocuc.aduana_api.exception;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(String message) { super(message); }
}