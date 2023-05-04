package com.jpbc.project.exceptionHandler;

public class ProjectAlreadyLinkedToTeamException extends RuntimeException {
    private String message;

    public ProjectAlreadyLinkedToTeamException(String message) {
        super(message);
        this.message = message;
    }

    public ProjectAlreadyLinkedToTeamException() {

    }
}
