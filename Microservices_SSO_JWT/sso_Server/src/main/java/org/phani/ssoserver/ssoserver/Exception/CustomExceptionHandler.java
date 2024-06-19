package org.phani.ssoserver.ssoserver.Exception;

import org.phani.ssoserver.ssoserver.Entity.ErrorMessage;
import org.phani.ssoserver.ssoserver.Entity.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorMessage> CustomException(CustomException customException, WebRequest webRequest) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .ErrorCode("BAD CREDENTIALS")
                .ErrorMessage(customException.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(errorMessage);

    }

}
