package com.eladcohen.aravkuk;

import java.util.ArrayList;

import com.eladcohen.aravkuk.R;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LauncherActivity.ListItem;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.ClipData.Item;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.drm.DrmManagerClient.OnEventListener;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	SharedPreferences sharedPrefs;		
	String savedBookName,savedCatName,savedCatFile;
	float savedScrollY;
	Button lastplace_but;
	private ExpandableListView mExpandableList;
	boolean oldBlackScreenVal,blackScreen;
	OnSharedPreferenceChangeListener listener;
    @Override
    public void onCreate(Bundle savedInstanceState) {

		sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(MyApplication.getAppContext());	
    	
//		sharedPrefs.edit().clear().commit();
		blackScreen = Boolean.valueOf(sharedPrefs.getBoolean(
				"prefCheckboxBlackScreen", (MyApplication.getDefaultBlackScreenValue())));
		
		if (blackScreen)
			setTheme(R.style.AppTheme_Dark);
		oldBlackScreenVal = blackScreen;
        super.onCreate(savedInstanceState);      
        
		
        setTitle("ראשי");			
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.action_bar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        setContentView(R.layout.activity_main);
    	sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
    	savedBookName = sharedPrefs.getString("saved_bookName", null);
    	savedCatName = sharedPrefs.getString("saved_catName", null);
    	savedCatFile = sharedPrefs.getString("saved_catFile", null);    	
    	try
    	{
    	savedScrollY = sharedPrefs.getFloat("saved_scrollY",(float)0.0);
    	}catch(ClassCastException e){
    		savedScrollY = sharedPrefs.getInt("saved_scrollY",0);
    	}
//    	lastplace_but = (Button) findViewById(R.id.lastplace);
//		if (savedBookName!=null && savedCatName!=null && savedCatFile!=null)
//		{			
//			lastplace_but.setEnabled(true);
//		}
//		else
//		{
//			lastplace_but.setEnabled(false);
//		}
        new WhatsNewScreen(this).show();
//        ListView listView = (ListView) findViewById(R.id.mylist);
        MyApplication myApplication = (MyApplication) getApplication();
        
//		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
//			public void onSharedPreferenceChanged(SharedPreferences prefs,
//					String key) {
//				
//				blackScreen = Integer.parseInt((prefs.getString(
//						"prefBlackScreen", (MyApplication.getDefaultBlackScreenValue()))));
//				if (blackScreen != oldBlackScreenVal)
//				{
//					Intent intent = getIntent();
//					finish();
//					startActivity(intent);
//				}
//			}
//		};
		
        final BookList bookList = myApplication.getBookList();        
        //System.out.println(books_array);
        // First paramenter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        
        mExpandableList = (ExpandableListView)findViewById(R.id.mylist);
        
//        ArrayList<Book> arrayParents = new ArrayList<Book>();
//        ArrayList<String> arrayChildren = new ArrayList<String>();
// 
//        String[] bookListArr = bookList.getBookNamesArr();
 
        //sets the adapter that provides data to the list.
        //mExpandableList.setAdapter(new MyExpandableListAdapter(MainActivity.this,arrayParents));
         
        mExpandableList.setAdapter(new MyExpandableListAdapter(this,bookList));
        
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//          R.layout.item, R.id.item1, bookList.getBookNamesArr());

        // Assign adapter to ListView
//        listView.setAdapter(adapter);
//        
        mExpandableList.setOnChildClickListener(new OnChildClickListener() {        	
//			@Override
//			public void onChildClick(AdapterView<?> arg0, View arg1, int arg2,
//					long id) {
//			
//			}

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Intent intent = new Intent(MainActivity.this, com.eladcohen.aravkuk.ShowBook.class);
				TextView lvItem = (TextView) v; 				
			    String catName = lvItem.getText().toString();  
//			    Log.d("ELAD", "ExpandableList " + catName);
			    Book curBook =bookList.getBookByIndex(groupPosition);
		    	String catFile = curBook.getCatFile(catName);
				intent.putExtra("catName", catName);
				intent.putExtra("catFile", catFile);
				intent.putExtra("bookName", curBook.getBookName());
				startActivity(intent);	
				return false;
			}
        	});        
    }

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
//		if (savedInstanceState!=null)
//		{
//		String savedBookName = savedInstanceState.getString("saved_bookName");
//		String savedCatName = savedInstanceState.getString("saved_catName");
//		String savedCatFile = savedInstanceState.getString("saved_catFile");
////		if (savedBookName!=null)
////		{
//			Intent intent = new Intent(MainActivity.this, com.eladcohen.aravkuk.ShowBook.class);
//			intent.putExtra("savedCatName", savedCatName);
//			intent.putExtra("savedCatFile", savedCatFile);
//			intent.putExtra("savedBookName", savedBookName);			
//			startActivity(intent);
//		}
//		}
	}
	public void search(View view)
	{
		onSearchRequested();
	}	
//	public void house(View view)
//	{
//		startActivity(new Intent(this, House.class));
//	}
	public void jumpLastPlace()
	{
//    	sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
//    	String savedBookName = sharedPrefs.getString("saved_bookName", null);
//    	String savedCatName = sharedPrefs.getString("saved_catName", null);
//    	String savedCatFile = sharedPrefs.getString("saved_catFile", null);
//    	int savedScrollY = sharedPrefs.getInt("saved_scrollY",0);
    	savedBookName = sharedPrefs.getString("saved_bookName", null);
    	savedCatName = sharedPrefs.getString("saved_catName", null);
    	savedCatFile = sharedPrefs.getString("saved_catFile", null);
    	try
    	{
    	savedScrollY = sharedPrefs.getFloat("saved_scrollY",(float)0.0);
    	}catch(ClassCastException e){
    		savedScrollY = sharedPrefs.getInt("saved_scrollY",0);
    	}
		if (savedBookName!=null && savedCatName!=null && savedCatFile!=null)
		{
			Intent intent = new Intent(MainActivity.this, com.eladcohen.aravkuk.ShowBook.class);
			intent.putExtra("catName", savedCatName);
			intent.putExtra("catFile", savedCatFile);
			intent.putExtra("bookName", savedBookName);			
			intent.putExtra("scrollY", savedScrollY);	
			startActivity(intent);
		}
		else
		{
			Toast.makeText(MainActivity.this, "טרם נקבע מקום לשחזור", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
  protected void onResume(){
	 super.onResume();
	sharedPrefs = PreferenceManager
			.getDefaultSharedPreferences(MyApplication.getAppContext());		 
	blackScreen = Boolean.valueOf(sharedPrefs.getBoolean(
			"prefCheckboxBlackScreen", (MyApplication.getDefaultBlackScreenValue())));

	savedBookName = sharedPrefs.getString("saved_bookName", null);
	savedCatName = sharedPrefs.getString("saved_catName", null);
	savedCatFile = sharedPrefs.getString("saved_catFile", null);
	try
	{
	savedScrollY = sharedPrefs.getFloat("saved_scrollY",(float)0.0);
	}catch(ClassCastException e){
		savedScrollY = sharedPrefs.getInt("saved_scrollY",0);
	}	
	if (blackScreen != oldBlackScreenVal)
	{
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}		
//	if (savedBookName!=null && savedCatName!=null && savedCatFile!=null)
//	{			
//		lastplace_but.setEnabled(true);
//	}
//	else
//	{
//		lastplace_but.setEnabled(false);
//	}	
	}	

	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.activity_main, menu);
//        SubMenu subMenu1 = menu.addSubMenu("Action Item");
//        subMenu1.add("שלח משוב");
//        subMenu1.add("הגדרות");
//        subMenu1.add("אודות");
//
//        MenuItem subMenu1Item = subMenu1.getItem();
//        subMenu1Item.setIcon(R.drawable.ic_menu_moreoverflow_normal_holo_light);
//        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);    	
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
        case R.id.menu_about:
        startActivity(new Intent(this, About.class));
        return true;	         
        case R.id.menu_feedback:
        startActivity(new Intent(this, FormActivity.class));
        return true;        
        case R.id.menu_callme:
        startActivity(new Intent(this, Callme.class));
        return true;	        
        case R.id.menu_search:
            onSearchRequested();
            return true;		           
		case R.id.menu_settings:
			Intent i = new Intent(this, Preferences.class);
			startActivity(i);
			sharedPrefs.registerOnSharedPreferenceChangeListener(listener);
			return true;
			// case R.id.menu_exit:
			// finish();
			// System.exit(0);
			// return true;	
		case R.id.menu_lastplace_hide:	
        case R.id.menu_lastplace:
			jumpLastPlace();
			return true;
        case R.id.menu_bookmarks:
        startActivity(new Intent(this, BookmarksActivity.class));
        return true;	 
        case R.id.menu_migzarnews:
            try
            {
            	Intent LaunchIntent = getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.eladcohen.migzarnews");
            	startActivity( LaunchIntent );
            }
            catch ( Exception ex  )
            {
              Intent newIntent = new Intent( Intent.ACTION_VIEW, Uri.parse(
            "market://details?id=com.eladcohen.migzarnews" ) );
              startActivity(newIntent);
            }    
            return true;        
//        case R.id.menu_lastplace:
////    		if (savedInstanceStateGlobal!=null)
////    		{        	
////    		String savedBookName = savedInstanceStateGlobal.getString("saved_bookName");
////    		String savedCatName = savedInstanceStateGlobal.getString("saved_catName");
////    		String savedCatFile = savedInstanceStateGlobal.getString("saved_catFile");
////    		if (savedBookName!=null)
////    		{
//////    			Intent intent = new Intent(MainActivity.this, com.eladcohen.aravkuk.ShowBook.class);
//////    			intent.putExtra("savedCatName", savedCatName);
//////    			intent.putExtra("savedCatFile", savedCatFile);
//////    			intent.putExtra("savedBookName", savedBookName);			
//////    			startActivity(intent);
////    		}
////    		}	
//        	sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
//        	String savedBookName = sharedPrefs.getString("saved_bookName", null);
//        	String savedCatName = sharedPrefs.getString("saved_catName", null);
//        	String savedCatFile = sharedPrefs.getString("saved_catFile", null);
//        	int savedScrollY = sharedPrefs.getInt("saved_scrollY",0);
//    		if (savedBookName!=null)
//    		{
//    			Intent intent = new Intent(MainActivity.this, com.eladcohen.aravkuk.ShowBook.class);
//    			intent.putExtra("catName", savedCatName);
//    			intent.putExtra("catFile", savedCatFile);
//    			intent.putExtra("bookName", savedBookName);			
//    			intent.putExtra("scrollY", savedScrollY);	
//    			startActivity(intent);
//    		}        	
//        return true;        
//        case R.id.menu_exit:
//            finish();
//            System.exit(0);   
//        return true;             
        default:
    }
        return super.onOptionsItemSelected(item);
        
    }   
}
