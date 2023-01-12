package me.fevralev.socksapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class WrongColorException extends RuntimeException{
    public WrongColorException(String message) {
        super(message);
    }
}
