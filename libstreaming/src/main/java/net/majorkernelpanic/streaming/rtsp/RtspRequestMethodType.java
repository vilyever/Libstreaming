package net.majorkernelpanic.streaming.rtsp;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * RtspRequestMethodType
 * Created by vilyever on 2016/5/19.
 * Feature:
 */
public class RtspRequestMethodType {
    public static final int OPTIONS = 0;
    public static final int DESCRIBE = 1;
    public static final int SETUP = 2;
    public static final int PLAY = 3;
    public static final int PAUSE = 4;
    public static final int RECORD = 5;
    public static final int ANNOUNCE = 6;
    public static final int TEARDOWN = 7;
    public static final int GET_PARAMETER = 8;
    public static final int SET_PARAMETER = 9;
    public static final int REDIRECT = 10;

    @IntDef({
                    OPTIONS,
                    DESCRIBE,
                    SETUP,
                    PLAY,
                    PAUSE,
                    RECORD,
                    ANNOUNCE,
                    TEARDOWN,
                    GET_PARAMETER,
                    SET_PARAMETER,
                    REDIRECT,
            })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Int {
    }
    
    public static int[] values = new int[]{
            OPTIONS,
            DESCRIBE,
            SETUP,
            PLAY,
            PAUSE,
            RECORD,
            ANNOUNCE,
            TEARDOWN,
            GET_PARAMETER,
            SET_PARAMETER,
            REDIRECT,
            };
    
    public static
    @Int
    int toType(int value) {
        return value;
    }
    
    public static String name(@Int int type) {
        switch (type) {
            case OPTIONS:
                return "OPTIONS";
            case DESCRIBE:
                return "DESCRIBE";
            case SETUP:
                return "SETUP";
            case PLAY:
                return "PLAY";
            case PAUSE:
                return "PAUSE";
            case RECORD:
                return "RECORD";
            case ANNOUNCE:
                return "ANNOUNCE";
            case TEARDOWN:
                return "TEARDOWN";
            case GET_PARAMETER:
                return "GET_PARAMETER";
            case SET_PARAMETER:
                return "SET_PARAMETER";
            case REDIRECT:
                return "REDIRECT";
        }
        return null;
    }

    public static String method(@Int int type) {
        switch (type) {
            case OPTIONS:
                return "OPTIONS";
            case DESCRIBE:
                return "DESCRIBE";
            case SETUP:
                return "SETUP";
            case PLAY:
                return "PLAY";
            case PAUSE:
                return "PAUSE";
            case RECORD:
                return "RECORD";
            case ANNOUNCE:
                return "ANNOUNCE";
            case TEARDOWN:
                return "TEARDOWN";
            case GET_PARAMETER:
                return "GET_PARAMETER";
            case SET_PARAMETER:
                return "SET_PARAMETER";
            case REDIRECT:
                return "REDIRECT";
        }
        return null;
    }

    public static @RtspRequestMethodType.Int int methodToType(String method) {
        for (int type : values) {
            if (method(type).equalsIgnoreCase(method)) {
                return type;
            }
        }
        return OPTIONS;
    }
}
