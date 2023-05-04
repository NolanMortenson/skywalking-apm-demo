package com.jpbc.project.exceptionHandler;

public class TeamAlreadyExistsException extends RuntimeException {
    private String message;

    public TeamAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }

    public TeamAlreadyExistsException() {

    }
}
