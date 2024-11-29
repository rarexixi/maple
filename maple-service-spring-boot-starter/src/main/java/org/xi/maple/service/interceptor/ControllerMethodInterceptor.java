package org.xi.maple.service.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.xi.maple.common.annotation.SetField;
import org.xi.maple.common.annotation.SetFieldTypes;
import org.xi.maple.common.constant.SetFieldType;
import org.xi.maple.common.model.MapleUser;
import org.xi.maple.service.configuration.properties.MapleAspectProperties;
import org.xi.maple.service.configuration.properties.MapleProperties;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class ControllerMethodInterceptor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ControllerMethodInterceptor.class);

    private final Map<String, Method> userPropertyMap;
    private final MapleAspectProperties aspectProperties;

    public ControllerMethodInterceptor(MapleProperties mapleProperties) {
        this.userPropertyMap = getUserPropertyMap();
        this.aspectProperties = mapleProperties.getAspect();
    }


    /**
     * 拦截器，先于 Aspect 执行
     * @param invocation 方法调用
     * @return 方法调用结果
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Method method = invocation.getMethod();
        String methodName = method.getDeclaringClass().getName() + "." + method.getName();

        String path = request.getServletPath();
        Object[] args = invocation.getArguments();

        // MapleUser user = (MapleUser) SecurityUtils.getSubject().getPrincipal();
        MapleUser user = new MapleUser();
        user.setId(1);
        user.setUsername("xishihao");
        user.setName("郗世豪");
        user.setEmail("xishihao@sina.com");
        user.setMobile("13269998968");
        if (user != null) {
            logger.info("{}({});#path:{};#method:{};#args:{}", user.getName(), user.getUsername(), path, methodName, args);
            setUsers(method, args, user);
        }

        return invocation.proceed();
    }

    public Map<String, Method> getUserPropertyMap() {
        Class<MapleUser> userClass = MapleUser.class;
        // 获取所有 public 方法
        Method[] methods = userClass.getMethods();
        Map<String, Method> result = new HashMap<>(methods.length);
        for (Method m : methods) {
            if (m.getName().startsWith("get")) {
                String propertyName = m.getName().substring(3);
                String fieldName = StringUtils.uncapitalize(propertyName);
                result.put(fieldName, m);
            } else if (m.getName().startsWith("is")) {
                String propertyName = m.getName().substring(2);
                String fieldName = StringUtils.uncapitalize(propertyName);
                result.put(fieldName, m);
            }
        }
        return result;
    }


    /**
     * 设置用户相关字段
     *
     * @param method 方法
     * @param args   实际参数
     * @param user   操作用户
     */
    private void setUsers(final Method method, final Object[] args, final MapleUser user) {
        Parameter[] parameters = method.getParameters();
        // 用户相关字段的值
        final Map<String, Object> fieldValueMap = new HashMap<>(userPropertyMap.size());
        userPropertyMap.forEach((key, m) -> fieldValueMap.put(key, getFieldValue(m, user)));

        for (int i = 0; i < parameters.length; i++) {
            SetFieldTypes setFieldTypes = parameters[i].getAnnotation(SetFieldTypes.class);
            if (setFieldTypes == null) {
                continue;
            }
            setUsers(setFieldTypes.types(), args[i], fieldValueMap);
        }
    }

    /**
     * 设置用户相关字段
     *
     * @param types         更新的类型列表
     * @param obj           更新的对象
     * @param fieldValueMap 要设置的字段和值的 map
     */
    private void setUsers(final SetFieldType[] types, final Object obj, Map<String, Object> fieldValueMap) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Collection) {
            for (Object o : (Collection<?>) obj) {
                setUsers(types, o, fieldValueMap);
            }
            return;
        } else if (obj instanceof Map) {
            for (Object o : ((Map<?, ?>) obj).values()) {
                setUsers(types, o, fieldValueMap);
            }
            return;
        }
        Class<?> clazz = obj.getClass();
        while (!clazz.equals(Object.class)) {
            // 获取所有字段，包括 private 和 protected 字段
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                SetField setField;
                SetFieldTypes setFieldTypes;

                if ((setField = field.getAnnotation(SetField.class)) != null) {
                    if (getFieldValue(field, obj) != null && !setField.force()) {
                        continue;
                    }
                    // 要设置的值
                    final Object fieldValue = fieldValueMap.getOrDefault(setField.field(), getDefaultValue(setField.defaultValue()));
                    if (fieldValue == null) {
                        continue;
                    }
                    setFieldFor:
                    for (SetFieldType type : types) {
                        for (SetFieldType fieldType : setField.types()) {
                            if (type != fieldType) {
                                continue;
                            }
                            setFieldValue(field, obj, fieldValue);
                            break setFieldFor;
                        }
                    }
                } else if ((setFieldTypes = field.getAnnotation(SetFieldTypes.class)) != null) {
                    // 查找要设置的对象字段中包含 SetFieldTypes 注解的字段，进行递归设置

                    Object fieldValue = getFieldValue(field, obj);
                    if (fieldValue == null) {
                        continue;
                    }
                    setUsers(setFieldTypes.types(), fieldValue, fieldValueMap);
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    private static Object getDefaultValue(String springEL) {
        ExpressionParser parser = new SpelExpressionParser();
        ParserContext context = ParserContext.TEMPLATE_EXPRESSION;
        Expression expression = parser.parseExpression(springEL, context);
        return expression.getValue();
    }

    private static void setFieldValue(Field field, Object obj, Object val) {
        try {
            Method fieldSetMethod = obj.getClass().getMethod("set" + StringUtils.capitalize(field.getName()), field.getType());
            fieldSetMethod.invoke(obj, val);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object getFieldValue(Field field, Object obj) {
        try {
            Method fieldGetMethod = obj.getClass().getMethod("get" + StringUtils.capitalize(field.getName()));
            return fieldGetMethod.invoke(obj);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object getFieldValue(Method fieldGetMethod, Object obj) {
        try {
            return fieldGetMethod.invoke(obj);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
