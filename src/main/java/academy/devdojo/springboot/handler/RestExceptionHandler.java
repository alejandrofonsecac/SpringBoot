package academy.devdojo.springboot.handler;

import academy.devdojo.springboot.exeption.BadRequestExceptionDetails;
import academy.devdojo.springboot.exeption.BadRequestExeption;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(BadRequestExeption.class)
    public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestExeption(BadRequestExeption bre){
        return new ResponseEntity<>(
                    BadRequestExceptionDetails.builder()
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.BAD_REQUEST.value())
                            .title("Bad Request Exception, Check the documentation")
                            .details(bre.getMessage())
                            .developerMessage(bre.getClass().getName())
                            .build(), HttpStatus.BAD_REQUEST
                );
    }
}
