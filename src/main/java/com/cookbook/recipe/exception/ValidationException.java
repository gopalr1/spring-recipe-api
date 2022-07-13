package com.cookbook.recipe.exception;

public class ValidationException extends IllegalArgumentException {
    private static final long serialVersionUID = -3785646200491093847L;

    public ValidationException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }


}
