package com.example.abirshukla.whatwasthat;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.InputStream;
import java.net.URL;

public class info2 extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Stage: Info 2A");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info2);
        System.out.println("Stage: Info 2");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        textView = (TextView) findViewById(R.id.textView);
        Bundle c = getIntent().getExtras();
        String code = c.getString("code");
        String show = "";
        link = "";
        link = c.getString("link");
        int index = code.indexOf("<h1 itemprop=\"name\"");
        index = code.indexOf(">",index);
        int end = code.indexOf("&nbsp;");
        if (end == -1)
            end = code.indexOf("<span",index);
        if (end == -1)
            end = code.indexOf("</h1>",index);
        show = code.substring(index+1,end);
        String s = show;
        index = code.indexOf("<div class=\"bp_heading\">");
        if (index != -1)
            index = code.indexOf(">",index);
        end = code.indexOf("</div>",index+1);
        if (index != -1) {
            if (!code.substring(index+1,end).equals("Episode Guide"))
                show = show + "\n " + code.substring(index + 1, end);

        }
        show = show.replace("<span class=\"ghost\">","");
        show = show.replace("</span>","");
        textView.setText(show);
        imageView = (ImageView) findViewById(R.id.imageView);
        getHTML(s);

        //new DownLoadImageTask(imageView).execute(imgUrl);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
    public void goToImdb (View view) {

        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(link));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            getApplicationContext().startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // Chrome browser presumably not installed so allow user to choose instead
            intent.setPackage(null);
            getApplicationContext().startActivity(intent);
        }
    }

    public void onDestroy() {

        super.onDestroy();


    }
    public void getHTML(final String word) {
        System.out.println("Begin HTML");
        String edit = word.replace("/"," ");
        edit = edit + " movie";
        edit = edit.replace(" ","%20");

        String url = "http://abirshukla.pythonanywhere.com/searchImage/"+edit+"/";
        System.out.println("Final Url: " + url);
        final String[] d = new String[1];
        Ion.with(getApplicationContext())
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        System.out.println("Result: "+result);
                        int index = result.indexOf("is:");
                        int end = result.indexOf("</p>",index);
                        String picUrl = result.substring(index+3,end);
                        picUrl = picUrl.trim();
                        System.out.println("Pic Url: "+picUrl);
                        new DownLoadImageTask(imageView).execute(picUrl);
                    }
                });
    }


    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}
