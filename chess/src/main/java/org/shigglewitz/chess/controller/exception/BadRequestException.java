package org.shigglewitz.chess.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Request")
public class BadRequestException extends WebException {
    private static final long serialVersionUID = -7333139696582182463L;

    public BadRequestException(String string) {
        super(string);
    }
}
