package academy.devdojo.springboot.handler;

import academy.devdojo.springboot.exception.BadRequestExceptionDetails;
import academy.devdojo.springboot.exception.BadRequestExeption;
import academy.devdojo.springboot.exception.ValidationExceptionDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;

//Anotações:
//{1} --- @ExceptionHandler(BadRequestExeption.class) -> Captura exceções do tipo BadRequestExeption

//{2} --- ResponseEntity representa a resposta HTTP completa da API.

@ControllerAdvice // Intercepta exceções globalmente na aplicação
@Log4j2
public class RestExceptionHandler {

    @ExceptionHandler(BadRequestExeption.class) //{1}
    public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestExeption(BadRequestExeption bre){ //{2}
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionDetails> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        log.info("Fielods {}", exception.getBindingResult().getFieldError().getField());
        return null;
//        return new ResponseEntity<>(
//                BadRequestExceptionDetails.builder()
//                        .timestamp(LocalDateTime.now())
//                        .status(HttpStatus.BAD_REQUEST.value())
//                        .title("Bad Request Exception, Check the documentation")
//                        .details(exception.getMessage())
//                        .developerMessage(exception.getClass().getName())
//                        .build(), HttpStatus.BAD_REQUEST
//        );
    }
}
