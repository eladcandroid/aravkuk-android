package com.eladcohen.aravkuk;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eladcohen.aravkuk.R;
import com.eladcohen.aravkuk.R.id;

import android.location.GpsStatus.Listener;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
//import android.support.v4.app.NavUtils;

public class ShowBook extends ActionBarActivity {
	boolean scrollListenerStatus = true;
	boolean firstTimeFlag = true;
	boolean fullscreenFlag = false;
	float lastYPos = 0;	
	final String DEFAULT_TEXT_SIZE = "15";	
	// byte[] buffer = null;
	ProgressDialog progDailog;
	byte[] buffer;
	String catFile;
	// final WebView[] myWebViews = new WebView[10];
	BookList book_list;
	String cssStyle = "<style>html,body,.entry-summary{text-align:right;direction:rtl;},.foundStr{color:red;}</style>";
	LinearLayout layout, cont_layout;
	ScrollViewExt sv;
//	CharSequence[] titles;
	List<String> titles;
	String[] notes = new String[50];
	OnSharedPreferenceChangeListener listener;
	SharedPreferences sharedPrefs;
	int textSize;
	float scrollY = 0;
	float newScrollY = 0;
	boolean screenLock,blackScreen,oldBlackScreenVal;
	LinearLayout.LayoutParams params;
	Book curBook;
	String catName,bookName;
	String searchStr;
	String catLetter;
	int  firstTextViewSearchPos=0;
	int tvMatchStrId;
	boolean fullscreenModeFlag = false;
	String activityTitle = "";
	RetreiveBookTask rbt;
	AlertDialog alert;
	AlertDialog.Builder builder;
	int currentapiVersion = android.os.Build.VERSION.SDK_INT;
	int honeycombVersion = android.os.Build.VERSION_CODES.HONEYCOMB;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(MyApplication.getAppContext());		
		blackScreen = Boolean.valueOf(sharedPrefs.getBoolean(
				"prefCheckboxBlackScreen", (MyApplication.getDefaultBlackScreenValue())));
		if (blackScreen)
			setTheme(R.style.AppTheme_Dark);
		oldBlackScreenVal = blackScreen;		
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		book_list = ((MyApplication) this.getApplication()).getBookList();

		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences prefs,
					String key) {
				textSize = Integer.parseInt((prefs.getString("prefTextSize",
						DEFAULT_TEXT_SIZE)));
				changeLayoutTextSize(layout, textSize);
				screenLock = Boolean.valueOf(sharedPrefs.getBoolean(
						"prefCheckboxScreenLock", (MyApplication.getDefaultScreenLockValue())));				
				if (screenLock)
					getWindow().addFlags(
							WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
				else
					getWindow().clearFlags(
							WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
				
//				int oldBlackScreenVal = blackScreen;
//				blackScreen = Integer.parseInt((prefs.getString(
//						"prefBlackScreen", (MyApplication.getDefaultBlackScreenValue()))));
//				if (blackScreen != oldBlackScreenVal)
//				{
//					Intent intent = getIntent();
//					finish();
//					startActivity(intent);
//				}				
			}
		};

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			textSize = Integer.parseInt(sharedPrefs.getString("prefTextSize",
					DEFAULT_TEXT_SIZE));
			screenLock = Boolean.valueOf(sharedPrefs.getBoolean(
					"prefCheckboxScreenLock", (MyApplication.getDefaultScreenLockValue())));	
			if (screenLock)
				getWindow().addFlags(
						WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			else
				getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

			catName = extras.getString("catName");
			catFile = extras.getString("catFile");
			bookName = extras.getString("bookName");
			curBook = book_list.findBookByName(bookName);
			scrollY = extras.getFloat("scrollY");
			searchStr = extras.getString("searchStr");
			
	        
			String[] str_arr = new String[2];
			str_arr[0] = catName;
			str_arr[1] = catFile;
			
			rbt = new RetreiveBookTask(this);
			rbt.execute(str_arr);
			Log.d("ELAD", "1 " + scrollY);
//			if (scrollY > 0 || tvMatchStrId>0)
//				sv.post(new Runnable() {
//					public void run() {
//						if (scrollY > 0) {
//							scrollToPercent(sv, scrollY);
//						}
//						else if (tvMatchStrId>0)
//						{
//							//firstTextViewSearchPos = tvMatchStr.requestFocus();
//							TextView tvMatchStr = (TextView) findViewById(tvMatchStrId);				
//							firstTextViewSearchPos=tvMatchStr.getTop();
//							sv.scrollTo(0,firstTextViewSearchPos);
//						}
//					}
//				});

		}
		
	}
		
			class RetreiveBookTask extends AsyncTask<String, String, String> {
				
				private ProgressBar progressSpinner;
				public ShowBook activity;
				public RetreiveBookTask(ShowBook a)
				{
					this.activity = a;
				}
				
			    @Override
			    protected void onPreExecute() {
			    	titles = new ArrayList<String>();
					cont_layout = (LinearLayout) LayoutInflater.from(activity).inflate(
							R.layout.show_book_header, null);
//			    	progressSpinner.setVisibility(View.VISIBLE);					
					sv = new ScrollViewExt(activity);
					sv.setScrollViewListener( new ScrollViewListener() {
					    public void onScrollChanged( ScrollViewExt v, int x, int y, int oldx, int oldy ) {
					    	if (scrollListenerStatus){
//							if ((y==0 && fullscreenFlag) || (y-lastYPos)<-30 && fullscreenFlag)
//							{
//								fullscreenFlag = false;
//								showHeader();								
//							}
//							else 
							if ((y-lastYPos)>80 && !fullscreenFlag)
							{
								fullscreenFlag = true;
								hideHeader();								
								if (firstTimeFlag)
								{
									Toast.makeText(ShowBook.this, "ליציאה ממסך מלא לחץ על כפתור חזור", Toast.LENGTH_SHORT).show();
									firstTimeFlag = false;
								}
							}					
					    	}
//							lastYPos = y;
					    }
					});
					sv.setFillViewport(true);
					sv.removeAllViews();
			        final GestureDetector gestureDetector = new GestureDetector(activity, new MyGestureDetector());
			        View.OnTouchListener gestureListener = new View.OnTouchListener() {
			            public boolean onTouch(View v, MotionEvent event) {
			                return gestureDetector.onTouchEvent(event);
			            }
			        };	     
			        sv.setOnTouchListener(gestureListener);	
					activity.setContentView(cont_layout);			        
			    }
			    
			protected String doInBackground(String... str_arr) {
				try {
			String catName = str_arr[0];
			String catFile = str_arr[1];
			if (catName.indexOf(" - ")>=0)
			{
			catLetter = catName.substring(0,catName.indexOf(" - "));
			activityTitle = curBook.getBookName()+" - "+catLetter;
			}
			else
			{
				activityTitle = curBook.getBookName()+" - "+catName;
			}
//			activity.setTitle(activityTitle);

			
		
			
			params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			params.gravity = Gravity.RIGHT;
			sv.setLayoutParams(params);

			layout = new LinearLayout(activity);
			layout.removeAllViews();
			params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.RIGHT;
			layout.setOrientation(1);
			sv.addView(layout, params);

			InputStream fin = getAssets().open(catFile);
			buffer = new byte[fin.available()];
			fin.read(buffer);
			fin.close();

			String content;
			Boolean firstMatchSearchStr = false;			
			String str = new String(buffer);
//			str = str.replaceAll("<sup>", "<p>")
//					.replaceAll("</sup>", "</p>")
//					.replaceAll("<p> <a", "<p><a")
//					.replaceAll("</a> </p>", "</a></p>")
//					.replaceAll("class=\"aup1\" ", "")
//					.replaceAll("<span class=\"footnotereverse\"><a href=\".*?\">↩</a></span>", "");			
//			String tofind = new String(
//					"<.*? class=\"entry-title\"><a .*?>(.*?)</a></.*?>"
//							+ "(.*?)<div class=\"entry-summary\">(.*?)</div><!-- .entry-summary -->");
			String tofind = new String(
					"<h1 class=\"entry-title\">(.*?)</h1>"
							+ "(.*?)<div class=\"entry-content\">(.*?)</div><!-- .entry-content -->");
			Pattern p = Pattern.compile(tofind, Pattern.DOTALL);
			Matcher m = p.matcher(str);
			Boolean find = false;
			find = m.find();
			int i = 1;
			while (find) {
				String title = m.group(1);
				content = m.group(3);
				
				publishProgress(new String(title),"title",Integer.toString(i));
				
				if (searchStr!=null)
				{
					String new_content = content.replaceAll(searchStr, "<b><font color='red'>"+searchStr+"</font></b>");					
					if (!content.equals(new_content))
					{
						content = new_content;		
						if (!firstMatchSearchStr) tvMatchStrId = 9000 + i;
						firstMatchSearchStr = true;										
					}
				}
				
				titles.add(title);

				i++;

				publishProgress(content,"content",Integer.toString(i)); 

				find = m.find();

			}
			
						

			// String note_tofind =
			// "<div><a id=\"note(.*?)\">(.*?)</a>(.*?)</div>";
			// Pattern note_ptrn = Pattern.compile(note_tofind, Pattern.DOTALL);
			// m = note_ptrn.matcher(str);
			// find = false;
			// find = m.find();
			// int counter = 0;
			// while (find) {
			// notes[counter] = m.group(3);
			// counter++;
			// find = m.find();
			// }



	        
			// if (scrollY>0)
			// {
			// sv.smoothScrollTo(0, scrollY);
			// }
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return "";
				}
		  @Override
		  protected void onProgressUpdate(String... values) {
			  super.onProgressUpdate(values);
			  int textNum = 0;
			  if (values[2]!=null) 
				  textNum = Integer.parseInt(values[2]);
			  if (values[1]=="button")
				{
					Button noteButton = new Button(activity);
					noteButton.setText("הערה "+values[0]);
					LinearLayout.LayoutParams noteButtonParams = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					noteButtonParams.gravity = Gravity.RIGHT;
					noteButtonParams.setMargins(10, -30, 10, 0);
					noteButton.setLayoutParams(noteButtonParams);
					layout.addView(noteButton, noteButtonParams);
					
//					noteButton.setOnClickListener(openNote(noteButton,Integer.parseInt(values[0])));
				}
			  else{
//			  WebView tv_content = new WebView(activity);
			  TextView tv_content;
			  final WebView web_content;
//				tv_content.setText(values[0]);
//				tv_content.loadData(values[0], "text/html; charset=UTF-8", null);
			  
				params = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.gravity = Gravity.RIGHT;
				params.setMargins(10, 0, 10, 0);			  
				if (values[1]=="title")
				{
					tv_content = new TextView(activity);
					tv_content.setText(values[0]);
					tv_content.setTextAppearance(activity, R.style.TitleFont);
					tv_content.setTextSize(textSize + 2);
					tv_content.setId(9000 + textNum);
					layout.addView(tv_content, params);	
				}
				else if (values[1]=="content")
				{
					web_content = new WebView(activity);
					String cssHeader;
					if (blackScreen)
						cssHeader = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style_dark.css\" />";
					else
						cssHeader = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />";
					
					
					
					final String htmlData = "<html><head><title></title>" + cssHeader + "</head><body>" + values[0] + "</body></html>";
					web_content.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "UTF-8", null);
//					web_content.loadD
//					final ProgressDialog progDailog = ProgressDialog.show( web_content.getContext(),"Process ", "Loading Data...",true,true);
//
//					new Thread ( new Runnable()
//					{
//					     public void run()
//					     {
//					    	 
//					     }
//					}).start();
//
//					 Handler progressHandler = new Handler() 
//					 {
//
//					     public void handleMessage(Message msg1) 
//					     {
//
//					         progDailog.dismiss();
//					     }
//					 };		
					 
//					web_content.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html; charset=UTF-8", null, null);
					layout.addView(web_content, params);	
//					tv_content.setTextAppearance(activity, R.style.ContentFont);
//					web_content.setTextSize(textSize);
					final WebSettings webSettings = web_content.getSettings();
					webSettings.setDefaultFontSize(textSize);
				}

				if (currentapiVersion >= honeycombVersion){				
//					tv_content.setTextIsSelectable(true);
				}				
				// tv_content.setId(i + 1);

				
			  }
		  }
		    protected void onPostExecute(String result)
		    {
				cont_layout.addView(sv);
				activity.setContentView(cont_layout);
				
				ImageButton next_chapter = (ImageButton) findViewById(R.id.next_chapter);
				ImageButton prev_chapter = (ImageButton) findViewById(R.id.prev_chapter);
				if (curBook.isFirstCat(catName))
					prev_chapter.setEnabled(false);
				if (curBook.isLastCat(catName))
					next_chapter.setEnabled(false);
				
		    	progressSpinner = (ProgressBar) findViewById(R.id.progressSpinner);				
				progressSpinner.setVisibility(View.GONE);
				
				RelativeLayout centerLayout = (RelativeLayout) findViewById(R.id.centerLayout);	
				centerLayout.setVisibility(View.GONE);
				
				if (scrollY > 0 || tvMatchStrId>0)
				sv.post(new Runnable() {
					public void run() {
						if (scrollY > 0) {
							scrollListenerStatus = false;
							scrollToPercent(sv, scrollY);
							scrollListenerStatus = true;
						}
						else if (tvMatchStrId>0)
						{
							//firstTextViewSearchPos = tvMatchStr.requestFocus();
							TextView tvMatchStr = (TextView) findViewById(tvMatchStrId);				
							firstTextViewSearchPos=tvMatchStr.getTop();
							scrollListenerStatus = false;
							sv.scrollTo(0,firstTextViewSearchPos);
							scrollListenerStatus = true;
						}						
					}
				});		
				final CharSequence[] titles_items = titles.toArray(new CharSequence[titles.size()]);			
				builder = new AlertDialog.Builder(activity);
				builder.setTitle("בחר סעיף");
				builder.setItems(titles_items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
						int i = item;
						TextView tv = (TextView) findViewById(9001 + i);
						scrollListenerStatus = false;
						sv.smoothScrollTo(0, tv.getTop());
						scrollListenerStatus = true;
						Log.d("ELAD", "2 " + tv.getTop());
				    }
				});				
				alert = builder.create();
				
				
		    }
		    
			

	}
			
	@Override
	public void onBackPressed() {
		if (fullscreenFlag)
		{
			showHeader();
			fullscreenFlag = false;
		}
		else
			super.onBackPressed();
	}	
			
	View.OnClickListener openNote(final Button button, int noteNumParam) {
		final int noteNum = noteNumParam;
		return new View.OnClickListener() {
			public void onClick(View v) {
				if (notes[noteNum] != null) {
					Dialog dialog = new Dialog(ShowBook.this);
					dialog.setContentView(R.layout.notes_dialog);
					dialog.setTitle("הערה " + noteNum);
					// TextView tv_note = new TextView(ShowBook.this);
					TextView tv_note = (TextView) dialog
							.findViewById(R.id.noteContent);
					tv_note.setTextSize(textSize);
					tv_note.setText(Html.fromHtml(notes[noteNum]));
					if (currentapiVersion >= honeycombVersion){				
						tv_note.setTextIsSelectable(true);
					}	
					dialog.show();
				}
			}
		};
	}

	// Calculate the % of scroll progress in the actual web page content
	private float calculateScrollPercent(ScrollView content) {
		float positionTopView = 0;
		float contentHeight = content.getHeight();
		float currentScrollPosition = content.getScrollY();
		float percent = Math.round((currentScrollPosition - positionTopView)
				/ contentHeight);
		return percent;
	}

	private void scrollToPercent(ScrollView content, float percent) {
		float viewsize = content.getHeight();
		Log.d("ELAD", "VIEWSIZE: " + viewsize);
		float positionInSV = viewsize * percent;
		int positionY = Math.round(0 + positionInSV);
		content.scrollTo(0, positionY);
	}

//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		menu.setHeaderTitle("בחר סעיף");
//		super.onCreateContextMenu(menu, v, menuInfo);
//		for (int i = 0; i < titles.length; i++) {
//			if (titles[i] != null) {
//				menu.add(0, i, i, titles[i]);
//			}
//		}
//	}
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//	    //user has long pressed your TextView
//	    menu.add(0, v.getId(), 0, "text that you want to show in the context menu - I use simply Copy");
//
//	    //cast the received View to TextView so that you can get its text
//	    TextView yourTextView = (TextView) v;
//
//	    //place your TextView's text in clipboard
//	    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
//	    clipboard.setText(yourTextView.getText());
//	}
//	@Override
//	public boolean onContextItemSelected(android.view.MenuItem item) {
//		// find out which menu item was pressed
//		int i = item.getItemId();
//		TextView tv = (TextView) findViewById(9000 + i);
//		sv.smoothScrollTo(0, tv.getTop());
//		Log.d("ELAD", "2 " + tv.getTop());
//		return true;
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main,menu);
		MenuItem item = menu.findItem(R.id.menu_lastplace);
		item.setVisible(false);
		item = menu.findItem(R.id.menu_lastplace_hide);
		item.setVisible(false);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);            
	         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	         startActivity(intent); 
			return true;
		case R.id.menu_about:
			startActivity(new Intent(this, About.class));
			return true;
		case R.id.menu_feedback:
			startActivity(new Intent(this, FormActivity.class));
			return true;
        case R.id.menu_callme:
        startActivity(new Intent(this, Callme.class));
        return true;	  			
			// case R.id.menu_search:
			// startActivity(new Intent(this, Search.class));
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
		default:
		}
		return super.onOptionsItemSelected(item);

	}

	public boolean changeLayoutTextSize(LinearLayout layout, int textSize) {
		for (int i = 0; i < layout.getChildCount(); i++) {
			View v = layout.getChildAt(i);
			Class c = v.getClass();
			if (c == TextView.class) {
				TextView tv = (TextView) v;
				if (tv.getId() > 9000)
					tv.setTextSize(textSize + 2);
				else
					tv.setTextSize(textSize);
				// validate your EditText here
			}
			else if (c == WebView.class) {
				WebView wv = (WebView) v;
				final WebSettings webSettings = wv.getSettings();
				webSettings.setDefaultFontSize(textSize);
			}			
		}
		return true;
	}

	public void openParagraphsList(View v) {
		alert.show();
	}
//	public void fullscreenMode(View v) {
//		updateFullscreenStatus(true);
//	}	
	public void setBookmark(View v) {
		SharedPreferences sp = this.getSharedPreferences("bookmarks", MODE_PRIVATE);
		Editor editor = sp.edit();
		scrollY = calculateScrollPercent(sv);
		String val = catName+","+catFile+","+bookName+","+scrollY;
		editor.putString(activityTitle, catName+","+catFile+","+bookName+","+scrollY);
		editor.commit();
		Toast.makeText(ShowBook.this, activityTitle+" - הסימניה נוספה בהצלחה", Toast.LENGTH_SHORT).show();
	}		
//	private void updateFullscreenStatus(boolean bUseFullscreen)
//	{   		
//		Log.d("ELAD", "updateFullscreenStatus: " + bUseFullscreen);
//        RelativeLayout headerLayout = (RelativeLayout) findViewById(R.id.headerLayout);		
//	   if(bUseFullscreen && fullscreenModeFlag != bUseFullscreen)
//	   {
//		   getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//	       getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		   
//	        getSupportActionBar().hide();
//	        Toast.makeText(ShowBook.this, "לחץ פעמיים ברצף בכדי לצאת ממסך מלא", Toast.LENGTH_SHORT).show();
//	        
//
//	        headerLayout.setVisibility(View.GONE);
//	        
//	    }
//	    else if(fullscreenModeFlag != bUseFullscreen)
//	    {
//	        
//	    	getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//
//	        getSupportActionBar().show();
//	        headerLayout.setVisibility(View.VISIBLE);
//
//	        scrollY = calculateScrollPercent(sv);
//			String[] str_arr = new String[2];
//			str_arr[0] = catName;
//			str_arr[1] = catFile;  
//	        
//	    }
//	   
//	   fullscreenModeFlag=bUseFullscreen;
//	}	
	public void nextChapter(View v) {
		catName = curBook.getNextCatName(catName);
		catFile = curBook.getCatFile(catName);
		scrollY=0;
		tvMatchStrId=0;
		String[] str_arr = new String[2];
		str_arr[0] = catName;
		str_arr[1] = catFile;
		new RetreiveBookTask(this).execute(str_arr);
	}

	public void prevChapter(View v) {
		catName = curBook.getPrevCatName(catName);
		catFile = curBook.getCatFile(catName);
		scrollY=0;
		tvMatchStrId=0;		
		String[] str_arr = new String[2];
		str_arr[0] = catName;
		str_arr[1] = catFile;
		new RetreiveBookTask(this).execute(str_arr);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			scrollY = savedInstanceState.getFloat("scrollY");
			catName = savedInstanceState.getString("catName");
			catFile = savedInstanceState.getString("catFile");
			double orientationRatio = 0.91;
			if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
				newScrollY = (float) (scrollY / orientationRatio);
			else
				newScrollY = (float) (scrollY * orientationRatio);

			Log.d("ELAD", "Orientation: "
					+ this.getResources().getConfiguration().orientation + " "
					+ Configuration.ORIENTATION_LANDSCAPE);
			if (catName != null && catFile != null)
			{
				String[] str_arr = new String[2];
				str_arr[0] = catName;
				str_arr[1] = catFile;
//				rbt.execute(str_arr);
			}
//			sv.post(new Runnable() {
//				public void run() {
//					if (scrollY > 0) {
//						scrollToPercent(sv, newScrollY);
//					}
//				}
//			});

		}
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putString("catFile", catFile);
		savedInstanceState.putString("catName", catName);
		savedInstanceState.putFloat("scrollY", calculateScrollPercent(sv));

	}

	// @Override
	// protected void onStart(){
	// super.onStart();
	// if (scrollY>0)
	// {
	// sv.smoothScrollTo(0, scrollY);
	// }
	// }
	 @Override
	 protected void onResume(){
		 super.onResume();
			sharedPrefs = PreferenceManager
					.getDefaultSharedPreferences(MyApplication.getAppContext());		 
			blackScreen = Boolean.valueOf(sharedPrefs.getBoolean(
					"prefCheckboxBlackScreen", (MyApplication.getDefaultBlackScreenValue())));
			if (blackScreen != oldBlackScreenVal)
			{
				Intent intent = getIntent();
				finish();
				startActivity(intent);
			}	
	 }
	@Override
	protected void onStop() {
		super.onStop();

		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putString("saved_bookName", curBook.getBookName());
		editor.putString("saved_catName", catName);
		editor.putString("saved_catFile", catFile);
		editor.putFloat("saved_scrollY", calculateScrollPercent(sv));
		Log.d("ELAD", "E " + calculateScrollPercent(sv));
		// Commit the edits!
		editor.commit();
	}
	public void hideHeader() {
		
        try {
        	((View) findViewById(R.id.headerLayout))
            .setVisibility(View.GONE);
//            ((View) findViewById(android.R.id.title).getParent())
//                    .setVisibility(View.GONE);
        } catch (Exception e) {
        }
        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();	        
    }

    public void showHeader() {
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);    	
        try {
        	((View) findViewById(R.id.headerLayout))
            .setVisibility(View.VISIBLE);	        	
//            ((View) findViewById(android.R.id.title).getParent())
//                    .setVisibility(View.VISIBLE);
        } catch (Exception e) {
        }

        getSupportActionBar().show();
    }		
    
    class MyGestureDetector extends SimpleOnGestureListener {
//    	boolean firstTimeFlag = true;
//        @Override
//		public boolean onDoubleTapEvent(MotionEvent e) {
//			// TODO Auto-generated method stub
//        	updateFullscreenStatus(false);
//			return super.onDoubleTapEvent(e);
//		}
//		@Override
//		public boolean onDoubleTap(MotionEvent e) {
//			updateFullscreenStatus(false);
//			return super.onDoubleTap(e);
//		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (e1!=null && e1.getAction()==MotionEvent.ACTION_DOWN)
			{
//	            Log.d("ELAD 55", " = " + lastYPos);
//					if ((e2.getY()-lastYPos)<-80 && fullscreenFlag)
//					{
//						showHeader();
//						fullscreenFlag = false;
//					}
//					else if ((e2.getY()-lastYPos)>100 && !fullscreenFlag)
//					{
//						hideHeader();
//						fullscreenFlag = true;
//						Toast.makeText(ShowBook.this, "גלילה למעלה תציג שוב את הכפתורים", Toast.LENGTH_SHORT).show();
//					}				
//				if (firstTimeFlag)
//				{
					lastYPos = sv.getScrollY();
//					firstTimeFlag = false;
//				}
			}

			return super.onScroll(e1, e2, distanceX, distanceY);
		}   


		

    }
    public interface ScrollViewListener {
        void onScrollChanged(ScrollViewExt scrollView, 
                             int x, int y, int oldx, int oldy);
    }
    public class ScrollViewExt extends ScrollView {
        private ScrollViewListener scrollViewListener = null;
        public ScrollViewExt(Context context) {
            super(context);
        }

        public ScrollViewExt(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public ScrollViewExt(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public void setScrollViewListener(ScrollViewListener scrollViewListener) {
            this.scrollViewListener = scrollViewListener;
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);
            if (scrollViewListener != null) {
                scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
            }
        }
    }    
    
}
