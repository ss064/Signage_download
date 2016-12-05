package com.example.suzukisusumu_sist.signage_download;

import android.app.DownloadManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class DownloadActivity extends AppCompatActivity {
    TextView tv;
    DownloadManager downloadManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        String[] urls = getIntent().getStringExtra("urls").split("\r\n");
        tv = (TextView)findViewById(R.id.textView2);
        tv.setText(getIntent().getStringExtra("urls"));
        VideoDownload(urls[0]);
    }

    private void VideoDownload(String s_url){
        //URIを生成する
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(s_url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        tv.setText(uri.getPath());
        //DownloadManager.Request request = new DownloadManager.Request(uriBuilder.build());
        //保存場所、形式、ファイル名を指定

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES,"test.mp4");
        request.setTitle("Test.mp4");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setMimeType("video/mp4");
        long id=downloadManager.enqueue(request);

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor = downloadManager.query(query);
        int idStatus = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        cursor.moveToFirst();
        Log.d("DownloadManagerSample", cursor.getString(idStatus));
    }
}
