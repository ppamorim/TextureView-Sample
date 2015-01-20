package sample.ppamorim.com.sampletextureview;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends ActionBarActivity implements TextureView.SurfaceTextureListener {

    // Log tag.
    private static final String TAG = MainActivity.class.getName();

    // Asset video file name.
    private static final String FILE_NAME = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";

    // MediaPlayer instance to control playback of video file.
    private MediaPlayer mMediaPlayer;
    private MediaPlayer mMediaPlayer2;

    private SurfaceView mSurfaceView;
    private TextureView mTextureView;
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mTextureView = (TextureView) findViewById(R.id.textureView);
        mText = (TextView) findViewById(R.id.video_text);
        mTextureView.setSurfaceTextureListener(this);

        mMediaPlayer2 = new MediaPlayer();
        try {
            mMediaPlayer2.setDataSource(FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mSurfaceView.getHolder().addCallback(callbackVideo);
    }

    @Override
    protected void onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        super.onDestroy();
    }

    private SurfaceHolder.Callback callbackVideo = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            try {
                mMediaPlayer2.setDisplay(surfaceHolder);
                mMediaPlayer2.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    };

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {

        Surface surface = new Surface(surfaceTexture);

        try {

            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(FILE_NAME);
            mMediaPlayer.setSurface(surface);
            mMediaPlayer.prepareAsync();

            // Play video when the media source is ready for playback.
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                   mMediaPlayer.start();
                }
            });

            mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int info, int extra) {

                    switch (info) {

                        case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                            mText.setText("started render");
                            break;

                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                            mText.setText("finalize buffer");
                            break;

                        case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                            mText.setText("timeout");
                            break;

                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                            mText.setText("started buffer");
                            break;

                    }

                    return false;
                }
            });

            mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    private MediaPlayer.OnPreparedListener onPreparedVideo = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
        }
    };

}
