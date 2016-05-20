package net.majorkernelpanic.streaming.rtsp;

/**
 * RtspResponse
 * Created by vilyever on 2016/5/19.
 * Feature:
 */
public class RtspResponse {
    final RtspResponse self = this;
    
    /** The server name that will appear in responses. */
    public static String ServerName = "FTET RTSP Server";

    /* Constructors */
    public RtspResponse(RtspRequest request) {
        this.request = request;
    }



    /* Public Methods */

    public String getSendMessage() {

        int cSeqID = getRequest() != null ? getRequest().getCSeqID() : RtspRequest.NoneCSeqID;
        String response = "RTSP/1.0 " + RtspResponseStatus.info(getStatus()) + "\r\n" +
                          "Server: " + ServerName + "\r\n" +
                          (cSeqID != RtspRequest.NoneCSeqID ? ("CSeq: " + cSeqID + "\r\n") : "") +
                          "Content-Length: " + getContent().length() + "\r\n" +
                          getAttributes() +
                          "\r\n" +
                          getContent();

        return response;
    }
    
    /* Properties */
    private RtspRequest request;
    public RtspResponse setRequest(RtspRequest request) {
        this.request = request;
        return this;
    }
    public RtspRequest getRequest() {
        return this.request;
    }

    private @RtspResponseStatus.Int int status = RtspResponseStatus.InternalServerError;
    public RtspResponse setStatus(@RtspResponseStatus.Int int status) {
        this.status = status;
        return this;
    }
    public @RtspResponseStatus.Int int getStatus() {
        return this.status;
    }

    private String content = "";
    public RtspResponse setContent(String content) {
        this.content = content;
        return this;
    }
    public String getContent() {
        return this.content;
    }

    private String attributes = "";
    public RtspResponse setAttributes(String attributes) {
        this.attributes = attributes;
        return this;
    }
    public String getAttributes() {
        return this.attributes;
    }
    
    /* Overrides */
    
    
    /* Delegates */
    
    
    /* Private Methods */
    
}