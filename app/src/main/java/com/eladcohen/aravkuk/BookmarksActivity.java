package com.eladcohen.aravkuk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BookmarksActivity extends ActionBarActivity implements OnItemClickListener {

	ArrayList<Bookmark> bookmarks_list;
	SharedPreferences sharedPrefs;
	boolean screenLock,blackScreen,oldBlackScreenVal;
	BookmarksAdapter bookmarksAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(MyApplication.getAppContext());		
		blackScreen = Boolean.valueOf(sharedPrefs.getBoolean(
				"prefCheckboxBlackScreen", (MyApplication.getDefaultBlackScreenValue())));
		if (blackScreen)
			setTheme(R.style.AppTheme_Dark);		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmarks);
		getAllKeys();
        ListView listview = (ListView) findViewById(R.id.bookmarksList);
        
        // Here is the problem
        bookmarksAdapter = new BookmarksAdapter(this,R.layout.bookmark_item,bookmarks_list);

        listview.setAdapter(bookmarksAdapter);			
		listview.setOnItemClickListener(this);
		
	    Log.v("Example", "onCreate");
	    getIntent().setAction("Already created");	
	    
	    listview.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int pos, long id) {                
                removeKey(bookmarks_list.get(pos).getTitle());
                bookmarks_list.remove(pos);
                bookmarksAdapter.notifyDataSetChanged();
                return true;
            }
        }); 
	    Toast.makeText(BookmarksActivity.this, "על מנת למחוק סימניה לחץ עליה לחיצה ארוכה", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_bookmarks, menu);
		return true;
	}    
    private void getAllKeys()
    {
    	bookmarks_list = new ArrayList<Bookmark>();
    	SharedPreferences sp = this.getSharedPreferences("bookmarks", MODE_PRIVATE);
//    	sp.edit().clear().commit();
    	Map<String,?> keys = sp.getAll();

    	int count = 0;
    	for(Map.Entry<String,?> entry : keys.entrySet())
    	{
    		String value = entry.getValue().toString();
    		String delimiter = ",";
    		String[] values_array = value.split(delimiter);
    		bookmarks_list.add(new Bookmark(entry.getKey(),values_array[0],values_array[1],values_array[2],Float.parseFloat(values_array[3])));
//    		addBookmark(values_array);
    		count++; //keep track of the number of bookmarks
    	}


        
                
    }
    private void removeKey(String key)
    {    
    	SharedPreferences sp = this.getSharedPreferences("bookmarks", MODE_PRIVATE);
    	sp.edit().remove(key).commit();
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast toast = Toast.makeText(getApplicationContext(),
//            "Item " + (position + 1) + ": " + bookmarks_list.get(position).,
//            Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
//        toast.show();
    	Bookmark b = bookmarks_list.get(position);
    	String catName = b.getCatName();
    	String catFile = b.getCatFile();
    	String bookName = b.getBookName();
    	float scrollY = b.getScrollY();
		if (catName!=null && catFile!=null)
		{
			Intent intent = new Intent(BookmarksActivity.this, ShowBook.class);
			intent.putExtra("catName", catName);
			intent.putExtra("catFile", catFile);
			intent.putExtra("bookName", bookName);
			intent.putExtra("scrollY", scrollY);	
			startActivity(intent);
		}    	
		else
		{
	        Toast toast = Toast.makeText(getApplicationContext(),"ארעה שגיאה",Toast.LENGTH_SHORT);
		}
    }    
//    private void addBookmark(String[] values_array)
//    {    	
//    	ArrayList<RaidWpis> raid_list = new ArrayList<RaidWpis>();
//		
//    }    
    
    public class BookmarksAdapter extends ArrayAdapter<Bookmark> {

//    	public BookmarksAdapter(Context context, int textViewResourceId) {
//    	    super(context, textViewResourceId);
//    	}

    	public BookmarksAdapter(Context context, int resource, ArrayList<Bookmark> items) {
    	    super(context, resource, items);
    	}

    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {

    	    View v = convertView;

    	    if (v == null) {

    	        LayoutInflater vi;
    	        vi = LayoutInflater.from(getContext());
    	        v = vi.inflate(R.layout.bookmark_item, null);

    	    }
    	    
    	    Bookmark b = getItem(position);

    	    if (b != null) {

    	        TextView tt = (TextView) v.findViewById(R.id.txtBookmark);

    	        if (tt != null) {
    	            tt.setText(b.getTitle());
    	        }
    	    }

    	    return v;

    	}    
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
	@Override
	protected void onResume() {
	    Log.v("Example", "onResume");

	    String action = getIntent().getAction();
	    // Prevent endless loop by adding a unique action, don't restart if action is present
	    if(action == null || !action.equals("Already created")) {
	        Log.v("Example", "Force restart");
	        Intent intent = new Intent(this, BookmarksActivity.class);
	        startActivity(intent);
	        finish();
	    }
	    // Remove the unique action so the next time onResume is called it will restart
	    else
	        getIntent().setAction(null);

	    super.onResume();
	}    
}
