package cl.duocuc.aduana_api.exception;

public class VehiculoNotFoundException extends RuntimeException {
    public VehiculoNotFoundException(String message) {
        super(message);
    }
}