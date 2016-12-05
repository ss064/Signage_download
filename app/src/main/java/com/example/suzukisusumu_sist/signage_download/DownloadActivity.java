package com.example.suzukisusumu_sist.signage_download;

import android.app.DownloadManager;
import android.content.Intent;
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
    static int downloadNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        String[] urls = getIntent().getStringExtra("urls").split("\r\n");
        tv = (TextView)findViewById(R.id.textView2);
        tv.setText(getIntent().getStringExtra("urls"));
        //ダウンロードした件数が、URLsの件数を超えていたら、再生Activityに遷移する。
        if(downloadNum<urls.length) {
            VideoDownload(urls[downloadNum++]);
        }
        else{

            Intent intent =new Intent();
            intent.setClassName(getPackageName(),"com.example.suzukisusumu_sist.signage_download.VideoActivity");
            startActivity(intent);
        }
    }

    private void VideoDownload(String s_url){
        //URIを生成する
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(s_url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        //保存場所、形式、ファイル名を指定
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES,"/signage"+uri.getPath());
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
