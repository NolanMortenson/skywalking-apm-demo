package com.jpbc.project.exceptionHandler;

public class ProjectAccessLockedException extends RuntimeException {
    private String message;

    public ProjectAccessLockedException(String message) {
        super(message);
        this.message = message;
    }

    public ProjectAccessLockedException() {

    }
}
