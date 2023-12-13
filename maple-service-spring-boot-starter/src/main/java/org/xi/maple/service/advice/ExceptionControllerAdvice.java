package org.xi.maple.service.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.exception.MapleException;
import org.xi.maple.common.model.ResponseError;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.stream.Collectors;

@RestControllerAdvice
@CrossOrigin
public class ExceptionControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    // region 参数校验异常

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        logger.error("拦截到异常", e);
        String error = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
        return getError(HttpStatus.UNPROCESSABLE_ENTITY, error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseError> ConstraintViolationExceptionHandler(ConstraintViolationException e) {
        logger.error("拦截到异常", e);
        String error = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        return getError(HttpStatus.UNPROCESSABLE_ENTITY, error);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ResponseError> ValidationExceptionHandler(ValidationException e) {
        logger.error("拦截到异常", e);
        String error = e.getMessage();
        return getError(HttpStatus.UNPROCESSABLE_ENTITY, error);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseError> BindExceptionHandler(BindException e) {
        logger.error("拦截到异常", e);
        String error = e.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
        return getError(HttpStatus.UNPROCESSABLE_ENTITY, error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseError> MethodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException e) {
        logger.error("拦截到异常", e);
        String error = "参数转换错误";
        return getError(HttpStatus.UNPROCESSABLE_ENTITY, error);
    }

    // endregion

    // 权限异常
    // @ExceptionHandler(AuthorizationException.class)
    // public ResponseEntity<ResponseError> AuthorizationExceptionHandler(AuthorizationException e) {
    //     logger.error("拦截到异常", e);
    //     String error = "权限不足";
    //     return getError(HttpStatus.UNAUTHORIZED, error);
    // }

    // 数据唯一索引或主键冲突
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ResponseError> DuplicateKeyExceptionHandler(DuplicateKeyException e) {
        logger.error("拦截到异常", e);
        String error = "数据唯一约束校验失败";
        return getError(HttpStatus.FORBIDDEN, error);
    }

    // 查询数据不存在
    @ExceptionHandler(MapleDataNotFoundException.class)
    public ResponseEntity<ResponseError> ExceptionHandler(MapleDataNotFoundException e) {
        logger.error("拦截到异常", e);
        String error = e.getMessage();
        return getError(HttpStatus.NOT_FOUND, error);
    }

    // 自定义权限
    @ExceptionHandler(MapleException.class)
    public ResponseEntity<ResponseError> ExceptionHandler(MapleException e) {
        logger.error("拦截到异常", e);
        String error = e.getMessage();
        return getError(HttpStatus.INTERNAL_SERVER_ERROR, error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> ExceptionHandler(Exception e) {
        logger.error("拦截到异常", e);
        String error = e.getMessage();
        return getError(HttpStatus.INTERNAL_SERVER_ERROR, error);
    }

    private ResponseEntity<ResponseError> getError(HttpStatus httpStatus, String message) {
        ResponseError responseError = new ResponseError();
        responseError.setMessage(message);
        return ResponseEntity.status(httpStatus).body(responseError);
    }
}