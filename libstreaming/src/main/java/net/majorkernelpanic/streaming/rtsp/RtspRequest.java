package net.majorkernelpanic.streaming.rtsp;

import android.util.Base64;

import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RtspRequest
 * Created by vilyever on 2016/5/18.
 * Feature:
 */
public class RtspRequest {
    final RtspRequest self = this;

    public final static String TAG = RtspRequest.class.getSimpleName();

    public final static String HeaderCSeqKey = "cseq";
    public final static String HeaderAuthorization = "authorization";
    public final static String HeaderTransport = "transport";

    public final static int NoneCSeqID = -1;

    // Parse method & uri
    public static final Pattern RegexMethod = Pattern.compile("(\\w+) (\\S+) RTSP", Pattern.CASE_INSENSITIVE);
    // Parse a request header
    public static final Pattern RegexHeader = Pattern.compile("(\\S+): (.+)", Pattern.CASE_INSENSITIVE);


    /* Constructors */
    /** Parse the method, uri & headers of a RTSP request */
    public static RtspRequest parseRequest(String message) {
        RtspRequest request = new RtspRequest();
        String line;
        Matcher matcher;

        String[] lines = message.split("\r\n");
        matcher = RegexMethod.matcher(lines[0]);
        if (matcher.find()) {
            request.setMethod(matcher.group(1));
            request.setUri(matcher.group(2));
        }
        else {
            return null;
        }

        // Parsing headers of the request
        for (int i = 1; i < lines.length; i++) {
            matcher = RegexHeader.matcher(lines[i]);
            if (matcher.find()) {
                request.getHeaders().put(matcher.group(1).toLowerCase(Locale.US), matcher.group(2));
            }
        }

        return request;
    }
    
    /* Public Methods */
    public int getCSeqID() {
        if (getHeaders() != null && getHeaders().containsKey(HeaderCSeqKey)) {
            return Integer.valueOf(getHeaders().get(HeaderCSeqKey));
        }

        return NoneCSeqID;
    }

    public boolean isAuthorized(String userName, String password) {
        if(userName == null || password == null || userName.isEmpty()) {
            return true;
        }

        String authorization = getHeaders().get(HeaderAuthorization);

        if(authorization != null && !authorization.isEmpty()) {
            String local = userName+":"+password;
            String localEncoded = Base64.encodeToString(local.getBytes(), Base64.NO_WRAP);
            if(localEncoded.equals(authorization)) {
                return true;
            }
        }

        return false;
    }

    public String getTransport() {
        if (getHeaders() != null && getHeaders().containsKey(HeaderTransport)) {
            return getHeaders().get(HeaderTransport);
        }

        return "";
    }

    /* Properties */
    private String method;
    protected RtspRequest setMethod(String method) {
        this.method = method;
        return this;
    }
    public String getMethod() {
        return this.method;
    }

    private String uri;
    protected RtspRequest setUri(String uri) {
        this.uri = uri;
        return this;
    }
    public String getUri() {
        return this.uri;
    }

    private HashMap<String, String> headers;
    protected HashMap<String, String> getHeaders() {
        if (this.headers == null) {
            this.headers = new HashMap<String, String>();
        }
        return this.headers;
    }
    
    /* Overrides */
    
    
    /* Delegates */
    
    
    /* Private Methods */
    
}