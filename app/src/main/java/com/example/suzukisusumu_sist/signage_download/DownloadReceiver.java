package com.example.suzukisusumu_sist.signage_download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DownloadReceiver extends BroadcastReceiver {
    public DownloadReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // ダウンロードが完了したら結果用アクティビティを表示
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Toast.makeText(context, "Download Complete ID : " + id, Toast.LENGTH_LONG).show();

            if (id != -1) {
                Intent resultIntent = new Intent();
                resultIntent.setClassName(context.getPackageName(),"com.example.suzukisusumu_sist.signage_download.DownloadActivity");
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                resultIntent.putExtra(DownloadManager.EXTRA_DOWNLOAD_ID, id);
                try {
                    context.startActivity(resultIntent);
                } catch (Exception e) {
                    Toast.makeText(context, "対象のアプリがありません", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            // ダウンロードマネージャの通知領域をクリックした場合はメッセージ表示のみ
            Toast.makeText(context, intent.getAction(), Toast.LENGTH_LONG).show();
        }

        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
