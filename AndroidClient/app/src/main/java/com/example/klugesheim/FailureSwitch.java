package com.example.klugesheim;

public class FailureSwitch extends Switch{

    private String errorMessage;

    public FailureSwitch(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
