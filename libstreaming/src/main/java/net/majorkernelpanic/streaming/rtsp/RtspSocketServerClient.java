package net.majorkernelpanic.streaming.rtsp;

import android.support.annotation.NonNull;

import com.vilyever.logger.Logger;
import com.vilyever.socketclient.helper.SocketResponsePacket;
import com.vilyever.socketclient.server.SocketServerClient;

import net.majorkernelpanic.streaming.Session;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RtspSocketServerClient
 * Created by vilyever on 2016/5/18.
 * Feature:
 */
public class RtspSocketServerClient extends SocketServerClient {
    final RtspSocketServerClient self = this;


    /* Constructors */
    public RtspSocketServerClient(@NonNull Socket socket) {
        super(socket);
    }

    /* Public Methods */
    public Session getSession() {
        return RtspSocketServer.getInstance().getSession();
    }

    /* Properties */
    private SessionDelegate sessionDelegate;
    public RtspSocketServerClient setSessionDelegate(SessionDelegate sessionDelegate) {
        this.sessionDelegate = sessionDelegate;
        return this;
    }
    public SessionDelegate getSessionDelegate() {
        if (this.sessionDelegate == null) {
            this.sessionDelegate = new SessionDelegate.SimpleSessionDelegate();
        }
        return this.sessionDelegate;
    }
    public interface SessionDelegate {
        void onError(RtspSocketServerClient client);

        class SimpleSessionDelegate implements SessionDelegate {
            @Override
            public void onError(RtspSocketServerClient client) {

            }
        }
    }

    /* Overrides */
    @Override
    protected void internalOnReceiveResponse(@NonNull SocketResponsePacket responsePacket) {
        super.internalOnReceiveResponse(responsePacket);

        RtspRequest request = RtspRequest.parseRequest(responsePacket.getMessage());

        RtspResponse response = internalGenerateResponse(request);

        sendString(response.getSendMessage());
    }

    /* Delegates */
    
    /* Protected Methods */
    protected RtspResponse internalGenerateResponse(RtspRequest request) {
        RtspResponse response = new RtspResponse(request);

        if (request == null) {
            response.setStatus(RtspResponseStatus.BadRequest);
        }
        else {
            @RtspRequestMethodType.Int int methodType = RtspRequestMethodType.methodToType(request.getMethod());
            Logger.log("method " + RtspRequestMethodType.name(RtspRequestMethodType.values[methodType]));
            if (methodType == RtspRequestMethodType.OPTIONS) {
                response.setStatus(RtspResponseStatus.OK);
                String methods = "";
                methods += RtspRequestMethodType.method(RtspRequestMethodType.DESCRIBE) + ",";
                methods += RtspRequestMethodType.method(RtspRequestMethodType.SETUP) + ",";
                methods += RtspRequestMethodType.method(RtspRequestMethodType.TEARDOWN) + ",";
                methods += RtspRequestMethodType.method(RtspRequestMethodType.PLAY) + ",";
                methods += RtspRequestMethodType.method(RtspRequestMethodType.PAUSE);
                response.setAttributes("Public: " + methods + "\r\n");
            }
            else {
                if (!request.isAuthorized(RtspSocketServer.getInstance().getUserName(), RtspSocketServer.getInstance().getPassword())) {
                    response.setStatus(RtspResponseStatus.Unauthorized);
                    response.setAttributes( "WWW-Authenticate: Basic realm=\"" + RtspResponse.ServerName + "\"\r\n");
                }
                else {
                    try {
                        switch (methodType) {
                            case RtspRequestMethodType.DESCRIBE: {
                                internalUpdateSession(request.getUri());
                                Logger.log("syncConfigure B");
                                getSession().syncConfigure();
                                Logger.log("syncConfigure A");

                                String requestContent = getSession().getSessionDescription();
                                String requestAttributes =
                                        "Content-Base: " + getRunningSocket().getLocalAddress().getHostAddress() + ":" + getRunningSocket().getLocalPort() + "/\r\n" +
                                        "Content-Type: application/sdp\r\n";

                                response.setContent(requestContent);
                                response.setAttributes(requestAttributes);

                                // If no exception has been thrown, we reply with OK
                                response.setStatus(RtspResponseStatus.OK);
                                break;
                            }
                            case RtspRequestMethodType.SETUP: {
                                Pattern pattern;
                                Matcher matcher;
                                int port1, port2, ssrc, trackID, src[];
                                String destination;

                                pattern = Pattern.compile("trackID=(\\w+)", Pattern.CASE_INSENSITIVE);
                                matcher = pattern.matcher(request.getUri());

                                if (!matcher.find()) {
                                    response.setStatus(RtspResponseStatus.BadRequest);
                                    return response;
                                }

                                trackID = Integer.parseInt(matcher.group(1));

                                if (!getSession().trackExists(trackID)) {
                                    response.setStatus(RtspResponseStatus.NotFound);
                                    return response;
                                }

                                pattern = Pattern.compile("client_port=(\\d+)-(\\d+)", Pattern.CASE_INSENSITIVE);
                                matcher = pattern.matcher(request.getTransport());

                                if (!matcher.find()) {
                                    int[] ports = getSession().getTrack(trackID).getDestinationPorts();
                                    port1 = ports[0];
                                    port2 = ports[1];
                                }
                                else {
                                    port1 = Integer.parseInt(matcher.group(1));
                                    port2 = Integer.parseInt(matcher.group(2));
                                }

                                ssrc = getSession().getTrack(trackID).getSSRC();
                                src = getSession().getTrack(trackID).getLocalPorts();
                                destination = getSession().getDestination();

                                getSession().getTrack(trackID).setDestinationPorts(port1, port2);

                                RtspSocketServer.getInstance().syncStartStreaming(trackID);

                                String attributes = "Transport: RTP/AVP/UDP;" + (InetAddress.getByName(destination).isMulticastAddress() ? "multicast" : "unicast") +
                                                      ";destination=" + getSession().getDestination() +
                                                      ";client_port=" + port1 + "-" + port2 +
                                                      ";server_port=" + src[0] + "-" + src[1] +
                                                      ";ssrc=" + Integer.toHexString(ssrc) +
                                                      ";mode=play\r\n" +
                                                      "Session: " + "1185d20035702ca" + "\r\n" +
                                                      "Cache-Control: no-cache\r\n";

                                response.setAttributes(attributes);

                                // If no exception has been thrown, we reply with OK
                                response.setStatus(RtspResponseStatus.OK);
                                break;
                            }
                            case RtspRequestMethodType.PLAY: {
                                String requestAttributes = "RTP-Info: ";
                                if (getSession().trackExists(0))
                                    requestAttributes += "url=rtsp://" + getRunningSocket().getLocalAddress().getHostAddress() + ":" + getRunningSocket().getLocalPort() + "/trackID=" + 0 + ";seq=0,";
                                if (getSession().trackExists(1))
                                    requestAttributes += "url=rtsp://" + getRunningSocket().getLocalAddress().getHostAddress() + ":" + getRunningSocket().getLocalPort() + "/trackID=" + 1 + ";seq=0,";
                                requestAttributes = requestAttributes.substring(0, requestAttributes.length() - 1) + "\r\nSession: 1185d20035702ca\r\n";

                                response.setAttributes(requestAttributes);

                                // If no exception has been thrown, we reply with OK
                                response.setStatus(RtspResponseStatus.OK);
                                break;
                            }
                            case RtspRequestMethodType.PAUSE:
                                response.setStatus(RtspResponseStatus.OK);
                                break;
                            case RtspRequestMethodType.TEARDOWN:
                                response.setStatus(RtspResponseStatus.OK);
                                break;
                            default:
                                response.setStatus(RtspResponseStatus.BadRequest);
                                break;
                        }
                    }
                    catch (Exception e) {
//                        RtspSocketServer.getInstance().updateSession();
                        getSessionDelegate().onError(this);
                        e.printStackTrace();
                    }
                }
            }
        }

        return response;
    }

    protected Session internalUpdateSession(String uri) throws IOException {
        Session session = getSession();

        session.setOrigin(getRunningSocket().getLocalAddress().getHostAddress());
        session.setDestination(getRunningSocket().getInetAddress().getHostAddress());
        return session;
    }

    /* Private Methods */
}