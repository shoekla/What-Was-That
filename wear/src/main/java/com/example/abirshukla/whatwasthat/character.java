package com.example.abirshukla.whatwasthat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class character extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);
        Bundle c = getIntent().getExtras();
        String code = c.getString("code");
        int index = code.indexOf("<a href=\"/title/");
        index = code.indexOf("/",index+1);
        int end = code.indexOf("\">",index);
        TextView t =(TextView) findViewById(R.id.textView3);
        String link = "http://www.imdb.com"+code.substring(index,end);
        t.setText("Loading (Character Input)");
        getHTML(link);
    }

    public void getHTML(final String url) {

        if (url.contains("title")) {
            System.out.println("Stage: Info 1.6");
            System.out.println("Begin HTML");
            System.out.println("Final Url: " + url);
            final String[] d = new String[1];
            Ion.with(getApplicationContext())
                    .load(url)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            System.out.println("Result: " + result);
                            Intent i = new Intent(character.this, info2.class);
                            i.putExtra("link", url);
                            i.putExtra("code", result);
                            startActivity(i);
                        }
                    });
        }  else {
            Intent b = new Intent(this, MainActivity.class);
            Toast.makeText(getApplicationContext(), "Not Recognized Please Enter More Info",
                    Toast.LENGTH_LONG).show();
            startActivity(b);
            return;
        }
    }
}

