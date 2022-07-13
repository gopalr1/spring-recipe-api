package com.cookbook.recipe.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
public class ErrorDetails implements Serializable {

    private static final long serialVersionUID = 1262603140793418233L;
    private final Date timestamp;
    private String message;
    private List<String> errors;

    public ErrorDetails(Date timestamp, List<String> errors) {
        super();
        this.timestamp = timestamp;
        this.errors = errors;
    }

    public ErrorDetails(Date timestamp, String message) {
        super();
        this.timestamp = timestamp;
        this.message = message;
    }
}
