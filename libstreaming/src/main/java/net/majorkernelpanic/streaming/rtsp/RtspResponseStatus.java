package net.majorkernelpanic.streaming.rtsp;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * RtspResponseStatus
 * Created by vilyever on 2016/5/19.
 * Feature:
 */
public class RtspResponseStatus {
    public static final int OK = 0;
    public static final int BadRequest = 1;
    public static final int Unauthorized = 2;
    public static final int NotFound = 3;
    public static final int InternalServerError = 4;

    @IntDef({
                    OK,
                    BadRequest,
                    Unauthorized,
                    NotFound,
                    InternalServerError,
            })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Int {
    }
    
    public static int[] values = new int[]{
            OK,
            BadRequest,
            Unauthorized,
            NotFound,
            InternalServerError,
            };
    
    public static
    @Int
    int toType(int value) {
        return value;
    }
    
    public static String name(@Int int type) {
        switch (type) {
            case OK:
                return "OK";
            case BadRequest:
                return "BadRequest";
            case Unauthorized:
                return "Unauthorized";
            case NotFound:
                return "NotFound";
            case InternalServerError:
                return "InternalServerError";
        }
        return null;
    }

    public static int code(@Int int type) {
        switch (type) {
            case OK:
                return 200;
            case BadRequest:
                return 400;
            case Unauthorized:
                return 401;
            case NotFound:
                return 404;
            case InternalServerError:
                return 500;
        }
        return 0;
    }

    public static String info(@Int int type) {
        switch (type) {
            case OK:
                return "200 OK";
            case BadRequest:
                return "400 Bad Request";
            case Unauthorized:
                return "401 Unauthorized";
            case NotFound:
                return "404 Not Found";
            case InternalServerError:
                return "500 Internal Server Error";
        }
        return null;
    }
}
