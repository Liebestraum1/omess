package org.sixback.omess.common.exception;

import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
public class FieldErrorMessage {
    private final String objectName;
    private final String field;
    private final String message;
    private final Object rejectedValue;
    private final String code;

    public FieldErrorMessage(String objectName, String field, String message, Object rejectedValue, String code) {
        this.objectName = objectName;
        this.field = field;
        this.message = message;
        this.rejectedValue = rejectedValue;
        this.code = code;
    }

    public FieldErrorMessage(FieldError fieldError) {
        this(fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getDefaultMessage(),
                fieldError.getRejectedValue(),
                fieldError.getCode()
        );
    }

    @Override
    public String toString() {
        return String.format(
                "FieldErrorMessage{"
                        + "objectName='%s', "
                        + "field='%s', "
                        + "message='%s', "
                        + "rejectedValue=%s, "
                        + "code='%s'"
                        + "}",
                objectName, field, message, rejectedValue, code
        );
    }
}
