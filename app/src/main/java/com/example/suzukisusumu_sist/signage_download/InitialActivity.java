package com.example.suzukisusumu_sist.signage_download;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class InitialActivity extends AppCompatActivity implements AsyncTaskGetJson.AsyncCallBack{
    TextView tv;
    static final String SIGNAGE_PATH = Environment.getExternalStorageDirectory().getPath() + "/" + Environment.DIRECTORY_MOVIES +"/"+"Signage";
    private File[] files;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        String androidId=android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        AsyncTaskGetJson asyncTaskGetJson = new AsyncTaskGetJson(this,androidId);
        asyncTaskGetJson.execute();
        tv = (TextView) findViewById(R.id.textView);
        files = new File(SIGNAGE_PATH).listFiles();
    }

    //このActivityがBackgroundになったとき、Activityを終了する。
    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }

    private boolean filesDelete(){
        for (File file:files) {
            try{
                if(!file.delete()) {
                    return false;
                }
            }catch (Exception e){
                Toast.makeText(this,"Delete:"+file.getPath(),Toast.LENGTH_SHORT).show();
            }

        }
        return true;
    }
    @Override
    public  void onPostExecute(String result){
        //JSONを取得できなかった場合、resultに"error"が格納される。
        //取得できた場合、URLが格納される。
        if(result.equals("error")){
            Log.d("postExecute",result);
            //JSONを取得できなかった場合、ダウンロードせず、動画再生開始する。
            Toast.makeText(this, "JSONを取得できませんでした。\nネットワーク接続状況を確認してください。\n動画再生を開始します。", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClassName(getPackageName(), "com.example.suzukisusumu_sist.signage_download.VideoActivity");
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "対象のアプリがありません:InitialActivity", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else {
            String[] urls = result.split("\r\n");
            tv.setText(result);
            //サイネージフォルダ内のファイル全削除
            //サイネージフォルダがない,中身がない場合、フォルダを作る。
            if(new File(SIGNAGE_PATH).exists() && files!=null){
                Log.d("FilesDelete","execute");
                filesDelete();
            }else{
                //サイネージフォルダを作る。
                new File(SIGNAGE_PATH).mkdir();
                Log.d("FilesDelete","SIGNAGE_PATH not exists");
            }
            //JSONの中にエラーが含まれていた場合
            //エラーコード1000→デバイス未登録、エラーコード1001→動画未登録
            if(urls[0].equals("Error")){
                if(urls[1].equals("1000")) {
                    Intent intent = new Intent();
                    intent.setClassName(getPackageName(), "com.example.suzukisusumu_sist.signage_download.postActivity");
                    try {
                        Toast.makeText(this, "デバイスを登録してください。", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(this, "PostAndroidIDを起動できませんでした。", Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(30); //30ミリ秒Sleepする
                        } catch (InterruptedException ex) {
                        }
                        finish();
                    }
                }
                if(urls[1].equals("1001")){
                    Toast.makeText(this,"動画を登録してください。",Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(30); //30ミリ秒Sleepする
                    } catch (InterruptedException e) {
                    }
                    finish();
                }
            }
            else {
                //デバイス、動画ともに登録されていた場合,Downloadを開始する。
                DownloadActivity.downloadCnt = 0;//ダウンロードカウンタを初期化
                DownloadActivity.urls = urls;//Static変数にURLを書き込む
                Intent intent = new Intent();
                intent.setClassName(getPackageName(), "com.example.suzukisusumu_sist.signage_download.DownloadActivity");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "対象のアプリがありません:InitialActivity", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}