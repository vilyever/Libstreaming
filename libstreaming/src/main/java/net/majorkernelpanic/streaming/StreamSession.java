//package net.majorkernelpanic.streaming;
//
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.os.Looper;
//import android.util.Log;
//
//import net.majorkernelpanic.streaming.audio.AudioQuality;
//import net.majorkernelpanic.streaming.audio.AudioStream;
//import net.majorkernelpanic.streaming.exceptions.CameraInUseException;
//import net.majorkernelpanic.streaming.exceptions.ConfNotSupportedException;
//import net.majorkernelpanic.streaming.exceptions.InvalidSurfaceException;
//import net.majorkernelpanic.streaming.exceptions.StorageUnavailableException;
//import net.majorkernelpanic.streaming.gl.SurfaceView;
//import net.majorkernelpanic.streaming.video.VideoQuality;
//import net.majorkernelpanic.streaming.video.VideoStream;
//
//import java.io.IOException;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
///**
// * StreamSession
// * Created by vilyever on 2016/5/20.
// * Feature:
// */
//public class StreamSession {
//    final StreamSession self = this;
//
//
//    /* Constructors */
//    public StreamSession() {
//        long currentTimeMillis = System.currentTimeMillis();
//        setTimeStamp((currentTimeMillis / 1000) << 32 & (((currentTimeMillis - ((currentTimeMillis / 1000) * 1000)) >> 32) / 1000));
//    }
//
//    /* Public Methods */
//    /**
//     * Sets the configuration of the stream. <br />
//     * You can call this method at any time and changes will take
//     * effect next time you call {@link #configure()}.
//     * @param videoQuality Quality of the stream
//     */
//    public StreamSession setVideoQuality(VideoQuality videoQuality) {
//        if (getVideoStream() != null) {
//            getVideoStream().setVideoQuality(videoQuality);
//        }
//        return this;
//    }
//
//    /**
//     * Sets a Surface to show a preview of recorded media (video). <br />
//     * You can call this method at any time and changes will take
//     * effect next time you call {@link #start()} or {@link #startPreview()}.
//     */
//    public StreamSession setSurfaceView(final SurfaceView surfaceView) {
//        getThreadHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                if (self.getVideoStream() != null) {
//                    self.getVideoStream().setSurfaceView(surfaceView);
//                }
//            }
//        });
//
//        return this;
//    }
//
//    /**
//     * Sets the orientation of the preview. <br />
//     * You can call this method at any time and changes will take
//     * effect next time you call {@link #configure()}.
//     * @param orientation The orientation of the preview
//     */
//    public StreamSession setPreviewOrientation(int orientation) {
//        if (getVideoStream() != null) {
//            getVideoStream().setPreviewOrientation(orientation);
//        }
//        return this;
//    }
//
//    /**
//     * Sets the configuration of the stream. <br />
//     * You can call this method at any time and changes will take
//     * effect next time you call {@link #configure()}.
//     * @param audioQuality Quality of the stream
//     */
//    public StreamSession setAudioQuality(AudioQuality audioQuality) {
//        if (getAudioStream() != null) {
//            getAudioStream().setAudioQuality(audioQuality);
//        }
//        return this;
//    }
//
//    /**
//     * Returns a Session Description that can be stored in a file or sent to a client with RTSP.
//     * @return The Session Description.
//     * @throws IllegalStateException Thrown when {@link #setDestinationAddress(String)} has never been called.
//     */
//    public String getSessionDescription() {
//        StringBuilder sessionDescription = new StringBuilder();
//        if (getDestinationAddress() == null) {
//            throw new IllegalStateException("setDestination() has not been called !");
//        }
//        sessionDescription.append("v=0\r\n");
//        // TODO: Add IPV6 support
//        sessionDescription.append("o=- " + getTimeStamp() + " " + getTimeStamp() + " IN IP4 " + getOriginAddress() + "\r\n");
//        sessionDescription.append("s=Unnamed\r\n");
//        sessionDescription.append("i=N/A\r\n");
//        sessionDescription.append("c=IN IP4 " + getDestinationAddress() + "\r\n");
//        // t=0 0 means the session is permanent (we don't know when it will stop)
//        sessionDescription.append("t=0 0\r\n");
//        sessionDescription.append("a=recvonly\r\n");
//        // Prevents two different sessions from using the same peripheral at the same time
//        if (getAudioStream() != null) {
//            sessionDescription.append(getAudioStream().getSessionDescription());
//            sessionDescription.append("a=control:trackID=" + 0 + "\r\n");
//        }
//        if (getVideoStream() != null) {
//            sessionDescription.append(getVideoStream().getSessionDescription());
//            sessionDescription.append("a=control:trackID=" + 1 + "\r\n");
//        }
//        return sessionDescription.toString();
//    }
//
//    /** Returns an approximation of the bandwidth consumed by the session in bit per second. */
//    public long getBitrate() {
//        long sum = 0;
//        if (getAudioStream() != null) sum += getAudioStream().getBitrate();
//        if (getVideoStream() != null) sum += getVideoStream().getBitrate();
//        return sum;
//    }
//
//    /** Indicates if a track is currently running. */
//    public boolean isStreaming() {
//        if ((getAudioStream() != null && getAudioStream().isStreaming()) || (getVideoStream() != null && getVideoStream().isStreaming()))
//            return true;
//        else
//            return false;
//    }
//
//    /**
//     * Configures all streams of the session.
//     **/
//    public void configure() {
//        getThreadHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    self.syncConfigure();
//                } catch (Exception e) {};
//            }
//        });
//    }
//
//    /**
//     * Does the same thing as {@link #configure()}, but in a synchronous manner. <br />
//     * Throws exceptions in addition to calling a callback
//     * {@link Callback#onSessionError(int, int, Exception)} when
//     * an error occurs.
//     **/
//    public void syncConfigure()
//            throws CameraInUseException,
//            StorageUnavailableException,
//            ConfNotSupportedException,
//            InvalidSurfaceException,
//            RuntimeException,
//            IOException {
//
//        for (int id = 0; id < 2; id++) {
//            Stream stream = id == 0 ? getAudioStream() : getVideoStream();
//            if (stream != null && !stream.isStreaming()) {
//                try {
//                    stream.configure();
//                }
//                catch (CameraInUseException e) {
//                    postError(ERROR_CAMERA_ALREADY_IN_USE, id, e);
//                    throw e;
//                }
//                catch (StorageUnavailableException e) {
//                    postError(ERROR_STORAGE_NOT_READY, id, e);
//                    throw e;
//                }
//                catch (ConfNotSupportedException e) {
//                    postError(ERROR_CONFIGURATION_NOT_SUPPORTED, id, e);
//                    throw e;
//                }
//                catch (InvalidSurfaceException e) {
//                    postError(ERROR_INVALID_SURFACE, id, e);
//                    throw e;
//                }
//                catch (IOException e) {
//                    postError(ERROR_OTHER, id, e);
//                    throw e;
//                }
//                catch (RuntimeException e) {
//                    postError(ERROR_OTHER, id, e);
//                    throw e;
//                }
//            }
//        }
//        postSessionConfigured();
//    }
//
//    /**
//     * Asynchronously starts all streams of the session.
//     **/
//    public void start() {
//        getThreadHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    self.syncStart();
//                } catch (Exception e) {}
//            }
//        });
//    }
//
//    /**
//     * Does the same thing as {@link #start()}, but in a synchronous manner. <br />
//     * Throws exceptions in addition to calling a callback.
//     **/
//    public void syncStart()
//            throws CameraInUseException,
//            StorageUnavailableException,
//            ConfNotSupportedException,
//            InvalidSurfaceException,
//            UnknownHostException,
//            IOException {
//
//        syncStart(1);
//        try {
//            syncStart(0);
//        }
//        catch (RuntimeException e) {
//            syncStop(1);
//            throw e;
//        }
//        catch (IOException e) {
//            syncStop(1);
//            throw e;
//        }
//    }
//
//    /**
//     * Starts a stream in a synchronous manner. <br />
//     * Throws exceptions in addition to calling a callback.
//     * @param id The id of the stream to start
//     **/
//    public void syncStart(int id)
//            throws CameraInUseException,
//            StorageUnavailableException,
//            ConfNotSupportedException,
//            InvalidSurfaceException,
//            UnknownHostException,
//            IOException {
//
//        Stream stream = id == 0 ? getAudioStream() : getVideoStream();
//        if (stream != null && !stream.isStreaming()) {
//            try {
//                InetAddress destination = InetAddress.getByName(getDestinationAddress());
//                stream.setTimeToLive(getTimeToLive());
//                stream.setDestinationAddress(destination);
//                stream.start();
//                if (getTrack(1 - id) == null || getTrack(1 - id).isStreaming()) {
//                    postSessionStarted();
//                }
//                if (getTrack(1 - id) == null || !getTrack(1 - id).isStreaming()) {
//                    mHandler.post(mUpdateBitrate);
//                }
//            }
//            catch (UnknownHostException e) {
//                postError(ERROR_UNKNOWN_HOST, id, e);
//                throw e;
//            }
//            catch (CameraInUseException e) {
//                postError(ERROR_CAMERA_ALREADY_IN_USE, id, e);
//                throw e;
//            }
//            catch (StorageUnavailableException e) {
//                postError(ERROR_STORAGE_NOT_READY, id, e);
//                throw e;
//            }
//            catch (ConfNotSupportedException e) {
//                postError(ERROR_CONFIGURATION_NOT_SUPPORTED, id, e);
//                throw e;
//            }
//            catch (InvalidSurfaceException e) {
//                postError(ERROR_INVALID_SURFACE, id, e);
//                throw e;
//            }
//            catch (IOException e) {
//                postError(ERROR_OTHER, id, e);
//                throw e;
//            }
//            catch (RuntimeException e) {
//                postError(ERROR_OTHER, id, e);
//                throw e;
//            }
//        }
//    }
//
//    /* Properties */
//    private VideoStream videoStream;
//    protected StreamSession setVideoStream(VideoStream videoStream) {
//        if (this.videoStream != null) {
//            this.videoStream.stopPreview();
//        }
//        this.videoStream = videoStream;
//        return this;
//    }
//    public VideoStream getVideoStream() {
//        return this.videoStream;
//    }
//
//    private AudioStream audioStream;
//    protected StreamSession setAudioStream(AudioStream audioStream) {
//        if (this.audioStream != null) {
//            this.audioStream.stop();
//        }
//        this.audioStream = audioStream;
//        return this;
//    }
//    public AudioStream getAudioStream() {
//        return this.audioStream;
//    }
//
//    /**
//     * The origin address of the session.
//     * It appears in the session description.
//     * @param origin The origin address
//     */
//    private String originAddress;
//    protected StreamSession setOriginAddress(String originAddress) {
//        this.originAddress = originAddress;
//        return this;
//    }
//    public String getOriginAddress() {
//        if (this.originAddress == null) {
//            this.originAddress = "127.0.0.1";
//        }
//        return this.originAddress;
//    }
//
//    /**
//     * The destination address for all the streams of the session. <br />
//     * Changes will be taken into account the next time you start the session.
//     * @param destination The destination address
//     */
//    private String destinationAddress;
//    protected StreamSession setDestinationAddress(String destinationAddress) {
//        this.destinationAddress = destinationAddress;
//        return this;
//    }
//    public String getDestinationAddress() {
//        return this.destinationAddress;
//    }
//
//    private long timeStamp;
//    protected StreamSession setTimeStamp(long timeStamp) {
//        this.timeStamp = timeStamp;
//        return this;
//    }
//    public long getTimeStamp() {
//        return this.timeStamp;
//    }
//
//    /**
//     * Set the TTL of all packets sent during the session. <br />
//     * Changes will be taken into account the next time you start the session.
//     */
//    private int timeToLive;
//    protected StreamSession setTimeToLive(int timeToLive) {
//        this.timeToLive = timeToLive;
//        return this;
//    }
//    public int getTimeToLive() {
//        return this.timeToLive;
//    }
//
//    private Handler threadHandler;
//    protected Handler getThreadHandler() {
//        if (this.threadHandler == null) {
//            HandlerThread thread = new HandlerThread("net.majorkernelpanic.streaming.StreamSession");
//            thread.start();
//            this.threadHandler = new Handler(thread.getLooper());
//        }
//        return this.threadHandler;
//    }
//
//    private Handler mainThreadHandler;
//    protected Handler getMainThreadHandler() {
//        if (this.mainThreadHandler == null) {
//            this.mainThreadHandler = new Handler(Looper.getMainLooper());
//        }
//        return this.mainThreadHandler;
//    }
//
//    /* Overrides */
//
//
//    /* Delegates */
//
//
//    /* Private Methods */
//
//}