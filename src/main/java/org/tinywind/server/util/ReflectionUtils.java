package org.tinywind.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Stack;

public class ReflectionUtils {
    protected static final Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    public static void copy(Object target, Object source, Class<?> targetClass) {
        for (final Field field : targetClass.getDeclaredFields()) {
            try {
                final int modifiers = field.getModifiers();
                if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) continue;

                final String capName = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                final Method setter = targetClass.getDeclaredMethod("set" + capName, field.getType());
                final Object invoked = source.getClass().getMethod("get" + capName).invoke(source);
                setter.invoke(target, invoked);
            } catch (Exception e) {
                logger.trace(e.getMessage());
            }
        }
    }

    public static void copy(Object target, Object source) {
        for (Class<?> aClass = target.getClass(); !aClass.equals(Object.class); aClass = aClass.getSuperclass()) {
            copy(target, source, aClass);
        }
    }

    public static void checkMemberEmptyValue(Object o) throws InvocationTargetException, IllegalAccessException {
        final Stack<String> stack = new Stack<>();
        stack.push("object");
        checkMemberEmptyValue(o, stack);
    }

    private static void checkMemberEmptyValue(Object o, Stack<String> stack) throws InvocationTargetException, IllegalAccessException {
        final Class<?> aClass = o.getClass();

        for (Method method : aClass.getDeclaredMethods()) {
            final String methodName = method.getName();

            if (methodName.startsWith("get")) {

                String objectName = methodName.substring(3);
                objectName = objectName.substring(0, 1).toLowerCase() + objectName.substring(1);
                try {
                    aClass.getDeclaredField(objectName);
                } catch (Exception ignored) {
                    continue;
                }

                final Object invoked = method.invoke(o);

                if (invoked == null) {
                    final StringBuilder builder = new StringBuilder();
                    builder.append("empty : ");
                    for (String aStack : stack) builder.append(aStack).append(".");
                    builder.append(objectName);

                    throw new IllegalStateException(builder.toString());
                }

                stack.push(objectName);
                checkMemberEmptyValue(invoked, stack);
                stack.pop();
            }
        }
    }
}
