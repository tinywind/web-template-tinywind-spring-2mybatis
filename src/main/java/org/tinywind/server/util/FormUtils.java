package org.tinywind.server.util;

import org.tinywind.server.config.RequestMessage;
// TODO: CHECK
// import org.tinywind.server.model.tables.pojos.SysCode;
// import org.tinywind.server.repository1.CodeRepository;
import org.tinywind.server.util.spring.SpringApplicationContextAware;

import java.util.LinkedHashMap;
import java.util.Map;

public class FormUtils {
    public static <T extends Enum<?>> LinkedHashMap<String, String> options(Class<T> enumClass) {
        return options(false, enumClass);
    }

    public static <T extends Enum<?>> LinkedHashMap<String, String> options(boolean withWholeExpression, Class<T> enumClass) {
        return options(withWholeExpression, enumClass.getEnumConstants());
    }

    public static <T extends Enum<?>> LinkedHashMap<String, String> options(T... values) {
        return options(false, values);
    }

    public static <T extends Enum<?>> LinkedHashMap<String, String> options(boolean withWholeExpression, T... values) {
        final LinkedHashMap<String, String> map = new LinkedHashMap<>();
        if (withWholeExpression)
            map.put("", requestMessage().getText("text.whole"));

        for (T e : values)
            map.put(e.name(), requestMessage().getEnumText(e));

        return map;
    }

    public static Map<String, String> booleanOptions() {
        return booleanOptions(false);
    }

    public static Map<String, String> booleanOptions(boolean withWholeExpression) {
        final LinkedHashMap<String, String> map = new LinkedHashMap<>();
        if (withWholeExpression)
            map.put("", requestMessage().getText("text.whole"));

        map.put("true", requestMessage().getText("Boolean.TRUE"));
        map.put("false", requestMessage().getText("Boolean.FALSE"));

        return map;
    }

    // TODO: CHECK
    public static LinkedHashMap<String, String> codes(String group) {
        return codes(false, group);
    }

    // TODO: CHECK
    public static LinkedHashMap<String, String> codes(boolean withWholeExpression, String group) {
        final LinkedHashMap<String, String> map = new LinkedHashMap<>();
        if (withWholeExpression)
            map.put("", requestMessage().getText("text.whole"));

        /*
        final CodeRepository codeRepository = (CodeRepository) SpringApplicationContextAware.getBean("codeRepository");
        for (SysCode code : codeRepository.codes(group))
            map.put(code.getCd(), code.getName());
        */

        return map;
    }

    protected static RequestMessage requestMessage() {
        return SpringApplicationContextAware.requestMessage();
    }
}
