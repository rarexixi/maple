package org.xi.maple.service.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
@CrossOrigin
public class ExceptionControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    // region 参数校验异常

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseError> BindExceptionHandler(BindException e) {
        logger.error("接口参数校验异常", e);
        String error = e.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
        return getError(HttpStatus.UNPROCESSABLE_ENTITY, error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseError> ConstraintViolationExceptionHandler(ConstraintViolationException e) {
        logger.error("参数校验异常", e);
        String error = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        return getError(HttpStatus.UNPROCESSABLE_ENTITY, error);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ResponseError> ValidationExceptionHandler(ValidationException e) {
        logger.error("参数校验异常", e);
        String error = e.getMessage();
        return getError(HttpStatus.UNPROCESSABLE_ENTITY, error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseError> MethodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException e) {
        logger.error("参数类型不匹配", e);
        String error = String.format("参数类型不匹配：%s 无法转换为 %s(%s)", e.getValue(), e.getName(), e.getRequiredType());
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
        logger.error("数据唯一索引或主键冲突异常", e);
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseError> ExceptionHandler(HttpMessageNotReadableException e) {
        logger.error("拦截到异常", e);
        if (e.getCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) e.getCause();
            String error = String.format("类型不匹配：%s 无法转换为 %s(%s)", invalidFormatException.getValue(), invalidFormatException.getPathReference(), invalidFormatException.getTargetType());
            return getError(HttpStatus.UNPROCESSABLE_ENTITY, error);
        }
        String error = e.getMessage();
        return getError(HttpStatus.INTERNAL_SERVER_ERROR, error);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseError> ExceptionHandler(HttpRequestMethodNotSupportedException e) {
        logger.error("拦截到异常", e);
        String error = String.format("当前接口不支持 %s 请求，支持的请求类型包括：%s", e.getMethod(), Arrays.toString(e.getSupportedMethods()));
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
        responseError.setMsg(message);
        return ResponseEntity.status(httpStatus).body(responseError);
    }
}