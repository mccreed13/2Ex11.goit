package crud.exception;

public class PlanetIdException extends RuntimeException{
    public PlanetIdException() {
    }

    public PlanetIdException(String message) {
        super(message);
    }

    public PlanetIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
