package com.eladcohen.aravkuk;

import com.eladcohen.aravkuk.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class About extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        try
        {
            String app_ver = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            TextView tvVer = (TextView) findViewById(R.id.about_ver);
            tvVer.setText("גירסא "+app_ver);
        }
        catch (NameNotFoundException e)
        {
            Log.v("ABOUT_ERR", e.getMessage());
        }        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_about, menu);
        return true;
    }
}
