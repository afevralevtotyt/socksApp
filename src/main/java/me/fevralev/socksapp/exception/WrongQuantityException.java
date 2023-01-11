package me.fevralev.socksapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class WrongQuantityException extends RuntimeException{
    public WrongQuantityException(String message) {
        super(message);
    }
}
