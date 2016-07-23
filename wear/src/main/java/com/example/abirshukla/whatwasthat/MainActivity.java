package com.example.abirshukla.whatwasthat;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        Toast.makeText(getApplicationContext(), "Tap Icon to Start",
                Toast.LENGTH_SHORT).show();

    }


    public void makeT(View view) {
        promptSpeechInput();
    }
    private void promptSpeechInput() {
        String speech_prompt = "What are the details of the TV Show/Movie";
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                speech_prompt);
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);

        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Speech Not Supported",
                    Toast.LENGTH_SHORT).show();
            return;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String res = "";
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    res = result.get(0);
                }
                break;
            }

        }
        //Intent speak = new Intent(MainActivity.this, speaker.class);
        //speak.putExtra("res",res);
        //startActivity(speak);
        if (res.length() < 2) {
            Toast.makeText(getApplicationContext(), "Please Say Description",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        res = res.replace(" ","%20");
        getHTML("http://abirshukla.pythonanywhere.com/searchWhat/"+res+"/");

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
                        Intent i = new Intent(MainActivity.this,info.class);
                        i.putExtra("code",result);
                        startActivity(i);
                    }
                });
    }
}
