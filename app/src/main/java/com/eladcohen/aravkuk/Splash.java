package com.eladcohen.aravkuk;

import com.eladcohen.aravkuk.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;

public class Splash extends Activity {
	
	protected int _splashTime = 2000; 
	
	private Thread splashTread;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.activity_splash);
	    
	    
	    final Splash sPlashScreen = this; 
	    
	    // thread for displaying the SplashScreen
	    splashTread = new Thread() {
	        @Override
	        public void run() {
	            try {	            	
	            	synchronized(this){
	            		wait(_splashTime);
	            	}
	            	
	            } catch(InterruptedException e) {} 
	            finally {
	                finish();
	                
	                Intent i = new Intent();
	                i.setClass(sPlashScreen, MainActivity.class);
	        		startActivity(i);
	                
	                //stop();
	            }
	        }
	    };
	    
	    splashTread.start();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    	synchronized(splashTread){
	    		splashTread.notifyAll();
	    	}
	    }
	    return true;
	}
	
}