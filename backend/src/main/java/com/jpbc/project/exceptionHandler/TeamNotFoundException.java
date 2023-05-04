package com.jpbc.project.exceptionHandler;

public class TeamNotFoundException extends RuntimeException {
    private String message;

    public TeamNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public TeamNotFoundException() {

    }
}
