package com.example.abirshukla.whatwasthat;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class info2 extends Activity {
    TextView textView;
    String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info2);
        System.out.println("Stage: Info 2");
        textView = (TextView) findViewById(R.id.textView3);
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


}
