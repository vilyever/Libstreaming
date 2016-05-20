package net.majorkernelpanic.streaming.rtsp;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.vilyever.contextholder.ContextHolder;
import com.vilyever.logger.Logger;
import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketResponsePacket;
import com.vilyever.socketclient.server.SocketServer;
import com.vilyever.socketclient.server.SocketServerClient;

import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * RtspSocketServer
 * Created by vilyever on 2016/5/18.
 * Feature:
 */
public class RtspSocketServer extends SocketServer {
    final RtspSocketServer self = this;

    public final static String TAG = RtspSocketServer.class.getSimpleName();

    /** Port used by default. */
    public static int DefaultPort = 8086;
    
    /* Constructors */
    private static RtspSocketServer instance;
    public synchronized static RtspSocketServer getInstance() {
        if (instance == null) {
            instance = new RtspSocketServer();
            instance.getSocketPacketHelper().setReceiveTailString("\r\n\r\n");
        }

        return instance;
    }
    
    /* Public Methods */
    public int beginListen() {
        int port = beginListenFromPort(DefaultPort);
        Log.i(TAG, "RTSP server begin listen on port " + port);
        Toast.makeText(ContextHolder.getContext(), "RTSP server begin listen on port " + port, Toast.LENGTH_LONG).show();
        getSession();
        return port;
    }

    public void updateSession() {
        setSession(null);
        setSession(SessionBuilder.getInstance().build());
    }

    /** Returns whether or not the RTSP server is streaming to some client(s). */
    public boolean isStreaming() {
        return getSession().isStreaming();
    }

    public void syncStartStreaming(int trackID) throws IOException {
        Logger.log("syncStartStreaming 1");
        boolean streaming = isStreaming();
        Logger.log("syncStartStreaming 2");
        getSession().syncStart(trackID);
        Logger.log("syncStartStreaming 3");
        if (!streaming && RtspSocketServer.getInstance().isStreaming()) {
        Logger.log("syncStartStreaming 4");
            internalNotifyStreamingStart();
        Logger.log("syncStartStreaming 5");
        }
        Logger.log("syncStartStreaming 6");
    }

    public void syncStopStreaming() {
        boolean streaming = isStreaming();
        getSession().syncStop();
        if (streaming && !RtspSocketServer.getInstance().isStreaming()) {
            internalNotifyStreamingStart();
        }
    }

    /** Returns the bandwidth consumed by the RTSP server in bits per second. */
    public long getBitrate() {
        long bitrate = 0;
        if (getSession().isStreaming()) {
            bitrate = getSession().getBitrate();
        }
        return bitrate;
    }

    /**
     * 注册监听回调
     * @param delegate 回调接收者
     */
    public RtspSocketServer registerRtspDelegate(RtspDelegate delegate) {
        if (!getRtspDelegateList().contains(delegate)) {
            getRtspDelegateList().add(delegate);
        }
        return this;
    }

    /**
     * 取消注册监听回调
     * @param delegate 回调接收者
     */
    public RtspSocketServer removeRtspDelegate(RtspDelegate delegate) {
        getRtspDelegateList().remove(delegate);
        return this;
    }

    
    /* Properties */
    private String userName;
    public RtspSocketServer setUserName(String userName) {
        this.userName = userName;
        return this;
    }
    public String getUserName() {
        return this.userName;
    }

    private String password;
    public RtspSocketServer setPassword(String password) {
        this.password = password;
        return this;
    }
    public String getPassword() {
        return this.password;
    }

    private Session session;
    public RtspSocketServer setSession(Session session) {
        if (this.session != null) {
            this.session.stop();
            this.session.release();
        }
        this.session = session;
        return this;
    }
    public Session getSession() {
        if (this.session == null) {
            this.session = SessionBuilder.getInstance().build();
        }
        return this.session;
    }

    private ArrayList<RtspDelegate> rtspDelegateList;
    protected ArrayList<RtspDelegate> getRtspDelegateList() {
        if (this.rtspDelegateList == null) {
            this.rtspDelegateList = new ArrayList<RtspDelegate>();
        }
        return this.rtspDelegateList;
    }
    public interface RtspDelegate {
        void onStreamingStart(RtspSocketServer server);
        void onStreamingStop(RtspSocketServer server);
        void onError(RtspSocketServer server);
        class SimpleRtspDelegate implements RtspDelegate {
            @Override
            public void onStreamingStart(RtspSocketServer server) {
        
            }
    
            @Override
            public void onStreamingStop(RtspSocketServer server) {
        
            }

            @Override
            public void onError(RtspSocketServer server) {

            }
        }
    }

    /* Overrides */
    @Override
    protected SocketServerClient internalGetSocketServerClient(Socket socket) {
        RtspSocketServerClient client = new RtspSocketServerClient(socket);
        client.setSessionDelegate(new RtspSocketServerClient.SessionDelegate() {
            @Override
            public void onError(RtspSocketServerClient client) {
                self.internalNotifyError();
            }
        });
        return client;
    }

    @Override
    protected void internalOnSocketServerStopListen() {
        super.internalOnSocketServerStopListen();

        syncStopStreaming();
    }

    @Override
    public void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket) {
        super.onResponse(client, responsePacket);
    }

    /* Delegates */
    
    
    /* Private Methods */
    private void internalNotifyStreamingStart() {
        ArrayList<RtspDelegate> copyList =
                (ArrayList<RtspDelegate>) getRtspDelegateList().clone();
        int count = copyList.size();
        for (int i = 0; i < count; ++i) {
            copyList.get(i).onStreamingStart(this);
        }
    }

    private void internalNotifyStreamingStop() {
        ArrayList<RtspDelegate> copyList =
                (ArrayList<RtspDelegate>) getRtspDelegateList().clone();
        int count = copyList.size();
        for (int i = 0; i < count; ++i) {
            copyList.get(i).onStreamingStop(this);
        }
    }

    private void internalNotifyError() {
        ArrayList<RtspDelegate> copyList =
                (ArrayList<RtspDelegate>) getRtspDelegateList().clone();
        int count = copyList.size();
        for (int i = 0; i < count; ++i) {
            copyList.get(i).onError(this);
        }
    }
}