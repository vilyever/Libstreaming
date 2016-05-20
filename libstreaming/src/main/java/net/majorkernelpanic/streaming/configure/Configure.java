package net.majorkernelpanic.streaming.configure;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringDef;

import com.vilyever.contextholder.ContextHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Configure
 * ESB <com.vilyever.base.Configure>
 * Created by vilyever on 2016/4/12.
 * Feature:
 */
public class Configure {
    final Configure self = this;

    public final static int DefaultIntValue = 0;
    public final static boolean DefaultBooleanValue = false;
    public final static long DefaultLongValue = 0L;
    public final static float DefaultFloatValue = 0.0f;
    public final static String DefaultStringValue = "";
    public final static Set<String> DefaultStringSetValue = null;

    public final static String AllKeys = "AllKeys";

    public static final String RtspSocketServerPort = "RtspSocketServerPort";

    @StringDef({
                       RtspSocketServerPort,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ConfigureKey {}
    
    /* Constructors */
    private static Configure instance;
    public synchronized static Configure getInstance() {
        if (instance == null) {
            instance = new Configure();
        }

        return instance;
    }
    
    /* Public Methods */
    public static void setValue(@ConfigureKey String key, Object value) {
        if (value instanceof Integer) {
            getSharedPreferences().edit().putInt(key, (Integer) value).apply();
        }
        else if (value instanceof Boolean) {
            getSharedPreferences().edit().putBoolean(key, (Boolean) value).apply();
        }
        else if (value instanceof Long) {
            getSharedPreferences().edit().putLong(key, (Long) value).apply();
        }
        else if (value instanceof Float) {
            getSharedPreferences().edit().putFloat(key, (Float) value).apply();
        }
        else if (value instanceof String) {
            getSharedPreferences().edit().putString(key, (String) value).apply();
        }
        else if (value instanceof Set) {
            getSharedPreferences().edit().putStringSet(key, (Set<String>) value).apply();
        }
        else {
            throw new IllegalStateException(String.format("The value type %s for key %s is not support", value.getClass(), key));
        }

        getInstance().notifyObservers(key, value);
    }

    public static Object getValue(@ConfigureKey String key) {
        return getValue(key, null);
    }

    public static Object getValue(@ConfigureKey String key, Object defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences.contains(key)) {
            return sharedPreferences.getAll().get(key);
        }
        return defaultValue;
    }

    public static void setInt(@ConfigureKey String key, int value) {
        setValue(key, value);
    }

    public static int getInt(@ConfigureKey String key) {
        return getInt(key, DefaultIntValue);
    }

    public static int getInt(@ConfigureKey String key, int defaultValue) {
        Object value = getValue(key, defaultValue);
        return value != null ? (int) value : defaultValue;
    }

    public static void setBoolean(@ConfigureKey String key, boolean value) {
        setValue(key, value);
    }

    public static boolean getBoolean(@ConfigureKey String key) {
        return getBoolean(key, DefaultBooleanValue);
    }

    public static boolean getBoolean(@ConfigureKey String key, boolean defaultValue) {
        Object value = getValue(key, defaultValue);
        return value != null ? (boolean) value : defaultValue;
    }

    public static void setLong(@ConfigureKey String key, long value) {
        setValue(key, value);
    }

    public static long getLong(@ConfigureKey String key) {
        return getLong(key, DefaultLongValue);
    }

    public static long getLong(@ConfigureKey String key, long defaultValue) {
        Object value = getValue(key, defaultValue);
        return value != null ? (long) value : defaultValue;
    }

    public static void setFloat(@ConfigureKey String key, float value) {
        setValue(key, value);
    }

    public static float getFloat(@ConfigureKey String key) {
        return getFloat(key, DefaultFloatValue);
    }

    public static float getFloat(@ConfigureKey String key, float defaultValue) {
        Object value = getValue(key, defaultValue);
        return value != null ? (float) value : defaultValue;
    }

    public static void setString(@ConfigureKey String key, String value) {
        setValue(key, value);
    }

    public static String getString(@ConfigureKey String key) {
        return getString(key, DefaultStringValue);
    }

    public static String getString(@ConfigureKey String key, String defaultValue) {
        Object value = getValue(key, defaultValue);
        return value != null ? (String) value : defaultValue;
    }

    public static void setStringSet(@ConfigureKey String key, Set<String> value) {
        setValue(key, value);
    }

    public static Set<String> getStringSet(@ConfigureKey String key) {
        return getStringSet(key, DefaultStringSetValue);
    }

    public static Set<String> getStringSet(@ConfigureKey String key, Set<String> defaultValue) {
        Object value = getValue(key, defaultValue);
        return value != null ? (Set<String>) value : defaultValue;
    }

    @SuppressWarnings("ResourceType")
    public static void registerObserverForAllKeys(Observer observer) {
        registerObserver(AllKeys, observer);
    }

    public static void registerObserver(@ConfigureKey String key, Observer observer) {
        if (!getInstance().getObserverMap().containsKey(observer)) {
            getInstance().getObserverMap().put(observer, new ArrayList<String>());
        }

        if (!getInstance().getObserverMap().get(observer).contains(key)) {
            getInstance().getObserverMap().get(observer).add(key);
        }
    }

    public static void removeObserver(Observer observer) {
        getInstance().getObserverMap().remove(observer);
    }

    public static void removeObserver(Observer observer, @ConfigureKey String key) {
        if (getInstance().getObserverMap().containsKey(observer)) {
            getInstance().getObserverMap().get(observer).remove(key);
        }
    }

    /* Properties */
    private Map<Observer, ArrayList<String>> observerMap;
    protected Map<Observer, ArrayList<String>> getObserverMap() {
        if (this.observerMap == null) {
            this.observerMap = Collections.synchronizedMap(new WeakHashMap<Observer, ArrayList<String>>());
        }
        return this.observerMap;
    }
    public interface Observer {
        void onConfigureUpdate(String key, Object value);
    }

    /* Overrides */

     
    /* Delegates */


    /* Private Methods */
    private static SharedPreferences getSharedPreferences() {
        return ContextHolder.getContext().getSharedPreferences(Configure.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    private synchronized void notifyObservers(String key, Object value) {
        for (Map.Entry<Observer, ArrayList<String>> entry : getInstance().getObserverMap().entrySet()) {
            if (entry.getKey() != null
                && (entry.getValue().contains(key) || entry.getValue().contains(AllKeys))) {
                entry.getKey().onConfigureUpdate(key, value);
            }
        }
    }
}