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
    private String videoPath= Environment.getExternalStorageDirectory().getPath() + "/" + Environment.DIRECTORY_MOVIES +"/"+"Signage";
    private File[] files = new File(videoPath).listFiles();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        if(!filesDelete()) {
            Log.d("FilesDelete","delete Failed");
        }

        String androidId=android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        AsyncTaskGetJson asyncTaskGetJson = new AsyncTaskGetJson(this,androidId);
        try{
            asyncTaskGetJson.execute();
        }catch (Exception e){

        }
        tv = (TextView) findViewById(R.id.textView);
    }

    //このActivityがBackgroundになったとき、Activityを終了する。
    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }

    private boolean filesDelete(){
        for (File file:files) {
            if(!file.delete()) {
                return false;
            }
            Toast.makeText(this,file.getPath()+"delete",Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    @Override
    public  void onPostExecute(String result){
        if(result.equals("error")){
            //JSONを取得できなかった場合、アプリを終了する。
            Toast.makeText(this, "JSONを取得できませんでした。\nネットワーク接続状況を確認してください。", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            //JSONを取得できた場合
            String[] urls = result.split("\r\n");
            tv.setText(result);
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
            else {//デバイス、動画ともに登録されていた場合,Downloadを開始する。
                DownloadActivity.downloadNum = 0;
                Intent intent = new Intent();
                intent.setClassName(getPackageName(), "com.example.suzukisusumu_sist.signage_download.DownloadActivity");
                intent.putExtra("urls", result);
                try {
                    DownloadActivity.urls = urls;
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "対象のアプリがありません:InitialActivity", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}