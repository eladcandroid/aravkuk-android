package com.eladcohen.aravkuk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.eladcohen.aravkuk.R;

public class Callme extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callme);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    
	public void call(View view)
	{
		Intent callIntent = new Intent(Intent.ACTION_DIAL);
		callIntent.setData(Uri.parse("tel:" + "0525710099"));
		startActivity(callIntent);
	}    
	public void email(View view)
	{
//		Intent intent = new Intent(Intent.ACTION_SENDTO);
//		intent.setType("text/plain");
//		intent.putExtra(Intent.EXTRA_EMAIL, "eladc.android@gmail.com");
//		intent.putExtra(Intent.EXTRA_SUBJECT, "בקשה לפיתוח אפליקציה");
//		intent.putExtra(Intent.EXTRA_TEXT, "");
//
//		startActivity(Intent.createChooser(intent, "Send Email"));
		
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"eladc.android@gmail.com"});
		i.putExtra(Intent.EXTRA_SUBJECT, "בקשה לפיתוח אפליקציה");
		i.putExtra(Intent.EXTRA_TEXT   , "");
		try {
		    startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(Callme.this, "אין אפליקציית אימייל מותקנת", Toast.LENGTH_SHORT).show();
		}		
	}   	
}
