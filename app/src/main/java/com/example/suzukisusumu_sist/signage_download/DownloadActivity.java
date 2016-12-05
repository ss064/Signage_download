package com.example.suzukisusumu_sist.signage_download;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DownloadActivity extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        String urls = getIntent().getStringExtra("urls");
        tv = (TextView)findViewById(R.id.textView2);
        tv.setText(urls);
    }
}
