package com.example.suzukisusumu_sist.signage_download;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class InitialActivity extends AppCompatActivity implements AsyncTaskGetJson.AsyncCallBack{
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        String androidId=android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        AsyncTaskGetJson asyncTaskGetJson = new AsyncTaskGetJson(this,androidId);
        asyncTaskGetJson.execute();
        tv = (TextView) findViewById(R.id.textView);
    }

    @Override
    public  void onPostExecute(String result){
        tv.setText(result);
        Intent intent = new Intent();
        intent.setClassName(getPackageName(),"com.example.suzukisusumu_sist.signage_download.DownloadActivity");
        intent.putExtra("urls",result);
        try {
            DownloadActivity.downloadNum=0;
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "対象のアプリがありません", Toast.LENGTH_SHORT).show();
        }
    }
}