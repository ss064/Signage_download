package com.example.suzukisusumu_sist.signage_download;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class postActivity extends AppCompatActivity {
    public TextView androidId;
    public TextView macAdd;
    public EditText name;
    public Button submit;
    private AsyncHttp asynchttp;
    public EditText t_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        androidId=(TextView)findViewById(R.id.androidid);
        macAdd=(TextView)findViewById(R.id.MacAddress);
        androidId.setText(android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        final String macAddress = wifiInfo.getMacAddress();
        macAdd.setText(macAddress);
        name = (EditText) findViewById(R.id.editText);
        t_name =(EditText) findViewById(R.id.t_name);

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty()==false && t_name.getText().toString().isEmpty()==false){
                    asynchttp = new AsyncHttp(android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID),name.getText().toString(),t_name.getText().toString(),macAddress);
                    asynchttp.execute();
                    Toast.makeText(postActivity.this,"送信", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else Toast.makeText(postActivity.this,"必要事項未記入エラー", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
