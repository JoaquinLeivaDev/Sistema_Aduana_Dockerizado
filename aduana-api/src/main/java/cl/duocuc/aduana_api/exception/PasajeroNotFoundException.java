package cl.duocuc.aduana_api.exception;

public class PasajeroNotFoundException extends RuntimeException {
    public PasajeroNotFoundException(String message) {
        super(message);
    }
}