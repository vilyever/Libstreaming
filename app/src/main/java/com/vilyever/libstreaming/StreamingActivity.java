package com.vilyever.libstreaming;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.vilyever.logger.Logger;
import com.vilyever.logger.LoggerDisplay;

import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.rtsp.RtspSocketServer;

public class StreamingActivity extends AppCompatActivity implements RtspSocketServer.RtspDelegate {
    final StreamingActivity self = this;

    /* Properties */
    private SurfaceView surfaceView;
    protected SurfaceView getSurfaceView() { if (this.surfaceView == null) {this.surfaceView = (SurfaceView) findViewById(R.id.surfaceView); } return this.surfaceView; }

//    private Session session;
//    protected StreamingActivity setSession(Session session) {
//        this.session = session;
//        return this;
//    }
//    public Session getSession() {
//        return this.session;
//    }

    /* Overrides */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.streaming_activity);

        LoggerDisplay.initialize(getApplication());
        LoggerDisplay.setDisplayLogTag(Logger.DefaultTag);

        // Configures the SessionBuilder
        SessionBuilder.getInstance()
                    .setSurfaceView(getSurfaceView())
                    .setContext(getApplicationContext())
                    .setAudioEncoder(SessionBuilder.AUDIO_AAC)
                    .setVideoEncoder(SessionBuilder.VIDEO_H264);

        RtspSocketServer.getInstance().updateSession();
    }

    @Override
    protected void onResume() {
        super.onResume();

        RtspSocketServer.getInstance().registerRtspDelegate(this);
        RtspSocketServer.getInstance().beginListen();
    }

    @Override
    protected void onPause() {
        super.onPause();

        RtspSocketServer.getInstance().stopListen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        RtspSocketServer.getInstance().removeRtspDelegate(this);
    }

    /* Delegates */
    @Override
    public void onStreamingStart(RtspSocketServer server) {

    }

    @Override
    public void onStreamingStop(RtspSocketServer server) {

    }

    @Override
    public void onError(RtspSocketServer server) {
        Toast.makeText(this, "不支持设置的视频质量，请修改设置后重试", Toast.LENGTH_LONG).show();
        finish();
    }
}
