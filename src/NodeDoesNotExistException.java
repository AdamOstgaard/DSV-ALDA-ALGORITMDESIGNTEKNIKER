public class NodeDoesNotExistException extends Exception {
    public NodeDoesNotExistException() {
        super();
    }

    public NodeDoesNotExistException(String message) {
        super(message);
    }

    public NodeDoesNotExistException(String message, Exception innerException) {
        super(message, innerException);
    }
    
    public NodeDoesNotExistException(Exception innerException) {
        super(innerException);
    }
}