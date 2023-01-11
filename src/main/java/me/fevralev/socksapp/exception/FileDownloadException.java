package me.fevralev.socksapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class FileDownloadException extends RuntimeException{
    public FileDownloadException(String message) {
        super(message);
    }
}
