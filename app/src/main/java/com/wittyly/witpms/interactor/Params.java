package com.wittyly.witpms.interactor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class backed by a Map, used to pass parameters to {@link UseCase} instances.
 */
public final class Params {

    public static final Params EMPTY = Params.create();

    private final Map<String, Object> parameters = new HashMap<>();

    private Params() {}

    public static Params create() {
        return new Params();
    }

    public void putInt(String key, int value) {
        parameters.put(key, value);
    }

    public void putString(String key, String value) {
        parameters.put(key, value);
    }

    public void putObject(String key, Object object) {
        parameters.put(key, object);
    }

    public void putListInt(String key, List<Integer> list) {
        parameters.put(key, list);
    }

    public void putDate(String key, Date date) {
        parameters.put(key, date);
    }

    public void putLong(String key, long value) {
        parameters.put(key, value);
    }

    public Object getObject(String key) {
        return parameters.get(key);
    }

    public int getInt(String key, int defaultValue) {
        final Object object = parameters.get(key);
        if (object == null) {
            return defaultValue;
        }
        try {
            return (int) object;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public long getLong(String key) {
        return (long) parameters.get(key);
    }


    public int getInt(String key) {
        final Object object = parameters.get(key);
        if (object == null) {
            return 0;
        }
        try {
            return (int) object;
        } catch (ClassCastException e) {
            return 0;
        }
    }

    public String getString(String key) {
        return (String) parameters.get(key);
    }

    public List<Integer> getListInt(String key) {
        return (List<Integer>)parameters.get(key);
    }

    public Date getDate(String key) {
        return (Date) parameters.get(key);
    }

}