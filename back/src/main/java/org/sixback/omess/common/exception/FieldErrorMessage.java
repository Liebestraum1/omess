package org.sixback.omess.common.exception;

import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
public class FieldErrorMessage {
    private final String field;
    private final String message;
    private final Object rejectedValue;

    public FieldErrorMessage(String field, String message, Object rejectedValue) {
        this.field = field;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }

    public FieldErrorMessage(FieldError fieldError) {
        this(
                fieldError.getField(),
                fieldError.getDefaultMessage(),
                fieldError.getRejectedValue()
        );
    }

    @Override
    public String toString() {
        return String.format(
                "FieldErrorMessage{"
                        + "field='%s', "
                        + "message='%s', "
                        + "rejectedValue=%s"
                        + "}\n",
                field, message, rejectedValue
        );
    }
}
