package org.xi.maple.rest.aspect;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.xi.maple.common.annotation.MapleAppAuthentication;
import org.xi.maple.common.exception.MapleAuthenticationException;
import org.xi.maple.rest.service.SecurityService;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Component
@Aspect
public class ControllerAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    final HttpServletRequest request;
    final SecurityService securityService;

    final ExpressionParser parser;

    public ControllerAspect(HttpServletRequest request, SecurityService securityService) {
        this.request = request;
        this.securityService = securityService;
        parser = new SpelExpressionParser();
    }

    /**
     * 设置标识
     */
    @Pointcut("within(org.xi.maple.rest.controller..*) && @annotation(org.xi.maple.common.annotation.MapleAppAuthentication)")
    public void invoke() {
    }

    /**
     * 环绕方法执行，proceedingJoinPoint.proceed()是执行环绕的方法
     */
    @Around("invoke()")
    public Object PlayAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Signature signature = proceedingJoinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            return proceedingJoinPoint.proceed();
        }

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Parameter[] parameters = method.getParameters();
        if (parameters.length == 0) {
            return proceedingJoinPoint.proceed();
        }

        MapleAppAuthentication annotation = method.getAnnotation(MapleAppAuthentication.class);

        Object[] args = proceedingJoinPoint.getArgs();
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        for (int i = 0; i < parameters.length; i++) {
            evaluationContext.setVariable(parameters[i].getName(), args[i]);
        }

        String app = StringUtils.isBlank(annotation.app())
                ? request.getParameter("app")
                : parser.parseExpression(annotation.app()).getValue(evaluationContext, String.class);
        String el = parser.parseExpression(annotation.value()).getValue(evaluationContext, String.class);

        String timestamp = request.getParameter("timestamp");   // 时间戳
        String secret = request.getParameter("secret");         // 加密字符串

        boolean result = securityService.authenticate(app, secret, Long.parseLong(timestamp), timestamp + ";#" + el);
        if (!result) {
            throw new MapleAuthenticationException("认证失败");
        }

        return proceedingJoinPoint.proceed();
    }

}