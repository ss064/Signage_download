package com.example.suzukisusumu_sist.signage_download;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class VideoActivity extends AppCompatActivity {
    public VideoView video;
    public EditText counter;
    // Movie/Signageフォルダの内のファイルを再生する。
    private File[] files;
    public int videoPoint=0;//動画カウンター

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        counter = (EditText) findViewById(R.id.Counter);
        video = (VideoView) findViewById(R.id.videoView);
        //MediaControllerを表示する設定
        video.setMediaController(new MediaController(this));

        try{
            files = new File(InitialActivity.SIGNAGE_PATH).listFiles();
        }catch (Exception e){
            Log.d("Exception",e.getMessage());
        }
        //Signageフォルダがない、または中身が空の場合、アプリを終了する。
        if(new File(InitialActivity.SIGNAGE_PATH).exists()|| files!=null) {
            //動画を再生する。
            VideoChange(files[videoPoint].getPath());
            //再生時間表示に関する処理
            //50ミリ秒に1回表示が更新される。現在時間(ミリ秒)取得→1000で割って秒数だけ表示。
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    counter.post(new Runnable() {
                        @Override
                        public void run() {
                            counter.setText(String.valueOf( video.getCurrentPosition() / 1000) + "s");
                        }
                    });
                }
            }, 0, 50);

            //動画再生終了後の処理
            //次の動画を再生する。→すべての動画を再生したらまた最初から再生する。
            video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    VideoChange(files[++videoPoint%files.length].getPath());
                    videoPoint=videoPoint%files.length;
                }
            });
        }
        else{
            Toast.makeText(this, "動画を再生できませんでした。\nアプリを終了します。", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //リモコンのボタン処理
    //再生停止両方機能が付いたリモコン、再生、停止別れているリモコン両方に対応している。
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
            if (video.isPlaying()) video.pause();
            else video.start();
        }
        if(keyCode== KeyEvent.KEYCODE_MEDIA_PLAY) video.start();
        if(keyCode== KeyEvent.KEYCODE_MEDIA_PAUSE) video.pause();
        if(keyCode== KeyEvent.KEYCODE_BACK) finish();//バックボタンを押した場合、アプリケーション終了する。
        return super.onKeyDown(keyCode, event);
    }

    //動画のパスをいれると、そのパスにある動画を再生する。
    public int VideoChange(String path) {
        video.setVideoPath(path);
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                video.start();
            }
        });
        return 0;
    }

}
