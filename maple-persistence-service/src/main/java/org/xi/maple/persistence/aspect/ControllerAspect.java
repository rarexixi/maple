package org.xi.maple.persistence.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Component
@Aspect
public class ControllerAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    @Autowired
    HttpServletRequest request;

    /**
     * 设置标识
     */
    @Pointcut("execution(public org.springframework.http.ResponseEntity org.xi.maple.persistence.controller.*.*(..))")
    public void invoke() {
    }

    /**
     * 环绕方法执行，proceedingJoinPoint.proceed()是执行环绕的方法
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("invoke()")
    public Object PlayAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Signature signature = proceedingJoinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            return proceedingJoinPoint.proceed();
        }

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();

        String methodName = method.getDeclaringClass().getName() + "." + method.getName();

        String path = request.getServletPath();
        Object[] args = proceedingJoinPoint.getArgs();

    //        IxUser user = (IxUser) SecurityUtils.getSubject().getPrincipal();
    //        if (user != null) {
    //            logger.info("{}({});#path:{};#method:{};#args:{}", user.getName(), user.getUsername(), path, methodName, args);
    //            setUsers(method, args, user);
    //        }

        return proceedingJoinPoint.proceed();
    }
}