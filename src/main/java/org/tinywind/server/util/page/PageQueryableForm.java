package org.tinywind.server.util.page;

import org.tinywind.server.util.UrlUtils;
import org.tinywind.server.util.spring.BaseForm;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.EmptyStackException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author tinywind
 * @since 2016-09-03
 */
public abstract class PageQueryableForm extends BaseForm {
    private static final Logger logger = LoggerFactory.getLogger(PageQueryableForm.class);

    protected boolean containNullValues = false;

    public final Map<String, Object> getQueryMap() {
        final Stack<Pair<String, Object>> stack = new Stack<>();
        final Map<String, Object> map = new LinkedHashMap<>();

        for (Class<?> klass = this.getClass(); !klass.getName().equals(PageQueryableForm.class.getName()); klass = klass.getSuperclass()) {
            final Field[] fields = klass.getDeclaredFields();
            for (Field field : fields)
                if (field.getAnnotation(PageQueryable.class) != null) {
                    final String fieldName = field.getName();
                    try {
                        final Object value = field.get(this);
                        stack.push(new MutablePair<>(fieldName, value == null ? null : value.getClass().isEnum() ? ((Enum) value).name() : value));
                    } catch (IllegalAccessException e) {
                        try {
                            final Method getter = klass.getDeclaredMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                            final Object value = getter.invoke(this);
                            stack.push(new MutablePair<>(fieldName, value == null ? null : value.getClass().isEnum() ? ((Enum) value).name() : value));
                        } catch (Exception e1) {
                            logger.debug(e1.getMessage(), e1);
                        }
                    }
                }
        }

        while (true) {
            final Pair<String, Object> pair;
            try {
                pair = stack.pop();
            } catch (EmptyStackException e) {
                break;
            }
            map.put(pair.getKey(), pair.getValue());
        }
        return map;
    }

    public final String getQuery() {
        return UrlUtils.encodeQueryParams(UrlUtils.convertMapToNameValuePair(getQueryMap(), containNullValues));
    }
}