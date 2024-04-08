package ultimatum.project.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        // ErrorCode에 따라 적절한 HTTP 상태 코드와 ErrorResponse 생성
        HttpStatus httpStatus = HttpStatus.valueOf(errorCode.getStatus());
        ErrorResponse response = new ErrorResponse(errorCode);

        return ResponseEntity.status(httpStatus).body(response);
    }
}