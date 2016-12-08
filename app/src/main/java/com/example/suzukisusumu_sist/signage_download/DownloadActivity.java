package com.example.suzukisusumu_sist.signage_download;

import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

public class DownloadActivity extends AppCompatActivity {
    TextView tv;
    DownloadManager downloadManager;
    static int downloadNum;
    static String[] urls;
    long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        tv = (TextView)findViewById(R.id.textView2);
        tv.setText(String.valueOf(downloadNum));
        Log.d("DownLoadActivity","");
        //ダウンロードした件数が、URLsの件数を超えたら、再生Activityに遷移する。
        if(downloadNum<urls.length) {
            Log.d("NewActivity",urls[downloadNum]);
            tv.setText("Downloading:"+urls[downloadNum]);
            VideoDownload(urls[downloadNum]);
            downloadNum++;
        }
        else{
            Log.d("NewActivity","urls");
            Intent intent =new Intent();
            intent.setClassName(getPackageName(),"com.example.suzukisusumu_sist.signage_download.VideoActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    //現在のアクティビティがバックグラウンドに移動した時、アクティビティを終了する。
    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
    //s_urlの動画をダウンロード,完了後DownloadReceiverに遷移
    private void VideoDownload(String s_url){
        //URIを生成する
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(s_url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        //保存場所、形式、ファイル名を指定
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES,"/signage"+uri.getPath());
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setMimeType("video/mp4");
        id=downloadManager.enqueue(request);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor = downloadManager.query(query);
        int idStatus = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        cursor.moveToFirst();
        Log.d("DownloadManagerSample", cursor.getString(idStatus));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) downloadManager.remove(id);//バックボタンを押した場合、ダウンロードを中断する。
        return super.onKeyDown(keyCode, event);

    }
}
