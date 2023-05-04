package com.jpbc.project.exceptionHandler;

public class ProjectNotFoundException extends RuntimeException {
    private String message;

    public ProjectNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public ProjectNotFoundException() {

    }
}
