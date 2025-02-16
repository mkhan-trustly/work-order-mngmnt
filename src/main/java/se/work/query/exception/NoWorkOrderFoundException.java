package se.work.query.exception;

public class NoWorkOrderFoundException extends RuntimeException {

    public NoWorkOrderFoundException(String errorMsg) {
        super(errorMsg);
    }
}
