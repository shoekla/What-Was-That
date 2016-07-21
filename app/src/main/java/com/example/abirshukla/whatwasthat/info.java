package com.example.abirshukla.whatwasthat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class info extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        textView = (TextView) findViewById(R.id.textView);
        Bundle c = getIntent().getExtras();
        String code = c.getString("code");
        int index1 = code.indexOf("<p>");
        int index2 = code.indexOf("</p>");
        System.out.println("Index 1: "+code.substring(index1+3,index2));
        getHTML(code.substring(index1+3,index2));
        textView.setText(code);
    }


    public void getHTML(final String url) {
        System.out.println("Begin HTML");
        System.out.println("Final Url: " + url);
        final String[] d = new String[1];
        Ion.with(getApplicationContext())
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        System.out.println("Result: "+result);
                        Intent i = new Intent(info.this,info2.class);
                        i.putExtra("link",url);
                        i.putExtra("code",result);
                        startActivity(i);
                    }
                });
    }
}
