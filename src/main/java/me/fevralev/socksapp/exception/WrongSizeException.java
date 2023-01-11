package me.fevralev.socksapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class WrongSizeException extends RuntimeException{
    public WrongSizeException(String message) {
        super(message);
    }
}
