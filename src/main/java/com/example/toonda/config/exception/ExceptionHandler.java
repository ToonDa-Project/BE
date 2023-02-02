package com.example.toonda.config.exception;

import com.example.toonda.config.dto.ErrorResponseDto;
import com.example.toonda.config.exception.errorcode.Code;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
        e.printStackTrace();
        return handleExceptionInternal(e, Code.INTERNAL_SERVER_ERROR, request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<Object> general(RestApiException e, WebRequest request) {
        return handleExceptionInternal(e, e.getErrorCode(), request);
    }

    // MethodArgumentNotValid 에러 핸들링
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        log.warn("handleMethodArgumentNotValid", e);
        String errorFieldName = e.getBindingResult().getFieldError().getField();
        Code statusCode = Code.INVALID_PARAMETER;
        if (errorFieldName.equals("email")) {
            statusCode = Code.WRONG_EMAIL_PATTERN;
        } else if (errorFieldName.equals("password")) {
            if (e.getBindingResult().getObjectName().equals("meetingRequestDto") ||
                    e.getBindingResult().getObjectName().equals("meetingUpdateRequestDto")) {
                statusCode = Code.WRONG_SECRET_PASSWORD;
            } else {
                statusCode = Code.WRONG_PASSWORD_PATTERN;
            }
        } else if (errorFieldName.equals("username")) {
            statusCode = Code.WRONG_USERNAME_PATTERN;
        } else if (errorFieldName.equals("title")) {
            statusCode = Code.TOO_LONG_TITLE;
        }
        return handleExceptionInternal(e, statusCode, request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        e.printStackTrace();
        return handleExceptionInternal(e, Code.INTERNAL_SERVER_ERROR, request);
    }


    // 그외 에러들 핸들링
//     @ExceptionHandler({Exception.class})
//     public ResponseEntity<Object> handleAllException(Exception ex) {
//          log.warn(">>>>>>>>>handleAllException", ex);
//          ex.printStackTrace();
//          Code statusCode = Code.INTERNAL_SERVER_ERROR;
//          return handleExceptionInternal(statusCode);
//     }

    // ErrorCode 만 있는 에러 ResponseEntity 생성
//     private ResponseEntity<Object> handleExceptionInternal(Code statusCode) {
//          return ResponseEntity.status(statusCode.getStatusCode())
//               // ErrorCode 만 있는 에러 responseEntity body만들기
//               .body(makeErrorResponse(statusCode));
//     }

//     private ErrorResponseDto makeErrorResponse(Code statusCode) {
//          return ErrorResponseDto.builder()
//               .statusCode(statusCode.getStatusCode())
//               .statusMsg(statusCode.getStatusMsg())
//               .build();
//     }

    // ErrorCode + message따로 있는 에러 ResponseEntity 생성
//     private ResponseEntity<Object> handleExceptionInternal(Code statusCode, String message) {
//          return ResponseEntity.status(statusCode.getStatusCode())
//               .body(makeErrorResponse(statusCode, message));
//     }

    //     private ErrorResponseDto makeErrorResponse(Code statusCode, String message) {
//          return ErrorResponseDto.builder()
//               .statusCode(statusCode.getStatusCode())
//               .statusMsg(message)
//               .build();
//     }
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
                                                             HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, Code.valueOf(status), headers, status, request);
    }

    // no header, no status
    private ResponseEntity<Object> handleExceptionInternal(Exception e, Code errorCode,
                                                           WebRequest request) {
        return handleExceptionInternal(e, errorCode, HttpHeaders.EMPTY, errorCode.getStatusCode(),
                request);
    }

    // Allargs Constructor
    private ResponseEntity<Object> handleExceptionInternal(Exception e, Code errorCode,
                                                           HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(
                e,
//               ErrorResponseDto.of(errorCode, errorCode.getStatusMsg(e)),
                ErrorResponseDto.of(errorCode),
                headers,
                status,
                request
        );
    }

}
