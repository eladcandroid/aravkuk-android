package com.eladcohen.aravkuk;

import com.eladcohen.aravkuk.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CatListActivity extends ActionBarActivity {
	String bookName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_list);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	bookName = extras.getString("bookName");
        	setTitle(bookName);
        	//String bookFile = extras.getString("bookFile");   
        	final Book curBook = ((MyApplication) this.getApplication()).getBookList().findBookByName(bookName);
        	ListView listView = (ListView) findViewById(R.id.catlist);
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            R.layout.item, R.id.item1, curBook.getCategoires());
        	listView.setAdapter(adapter);
            listView.setOnItemClickListener(new OnItemClickListener() {
    			@Override
    			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
    					long id) {
    				// TODO Auto-generated method stub
    				//AlertDialogManager alert = new AlertDialogManager();
    				//alert.showAlertDialog(MainActivity.this,"ELAD",""+id,false);
    				Intent intent = new Intent(CatListActivity.this, com.eladcohen.aravkuk.ShowBook.class);
    				//ListItem list_item = (ListItem) findViewById(arg0.getItemIdAtPosition((int)id));
    				//Object o = this.getListAdapter().getItem((int)id);
    				//TextView list_item = (TextView) ;
    				//String bookName = (String) list_item.getText();
    				//ListView lv = (ListView) arg0;
    				TextView lvItem = (TextView) arg1; 				
    			    String catName = lvItem.getText().toString();  
    				//TextView list_item = (TextView) findViewById((int)arg0.getItemIdAtPosition((int)id));
    				//String bookName1 = list_item.toString();
    				//String bookName = arg1.toString();
    				//System.out.println(list_item.getText());
			    	String catFile = curBook.getCatFile(catName);
    				intent.putExtra("catName", catName);
    				intent.putExtra("catFile", catFile);
    				intent.putExtra("bookName", bookName);
    				//Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
    				startActivity(intent);
    			}
            	});             	
        }
              // Assign adapter to ListView
              
              
           
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_about:
        startActivity(new Intent(this, About.class));
        return true;
        case R.id.menu_feedback:
        startActivity(new Intent(this, FormActivity.class));
        return true;        
        case R.id.menu_search:
            onSearchRequested();
            return true;		
        case R.id.menu_settings:
			Intent i = new Intent(this, Preferences.class);
			startActivity(i);   
        return true;
//        case R.id.menu_exit:
//            finish();
//            System.exit(0);   
//        return true;          
        default:
    }
        return super.onOptionsItemSelected(item);
        
    }

}
