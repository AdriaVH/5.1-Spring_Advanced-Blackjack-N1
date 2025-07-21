package cat.itacademy.s05.t01.n01.S05T01N01.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
