package cl.duocuc.aduana_api.exception;

public class DocumentoNotFoundException extends RuntimeException {
    public DocumentoNotFoundException(String message) {
        super(message);
    }
}