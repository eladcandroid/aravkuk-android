package com.eladcohen.aravkuk;

import java.util.ArrayList;
import java.util.List;

import com.eladcohen.aravkuk.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SampleSearchActivity extends Activity { 
	int maxResults;
	String[] resultList;
	ListView list;
	BookList book_list;
	TextView labelResult;
	String finalQueryStr=null;
	   public void onCreate(Bundle savedInstanceState) { 
	      super.onCreate(savedInstanceState); 
	      setContentView(R.layout.activity_search);
//		   ProgressDialog mDialog = new ProgressDialog(getApplicationContext());
//           mDialog.setMessage("Please wait...");
//           mDialog.setCancelable(false);
	      
	      onNewIntent(getIntent()); 
	   } 

	   public void onNewIntent(Intent intent) { 
		   
		      ListView list=(ListView)findViewById(R.id.resultList);
		   new ProgressTask(SampleSearchActivity.this).execute();
	      setIntent(intent); 
	      handleIntent(intent); 
	   } 

	   public void onListItemClick(ListView l, 
	      View v, int position, long id) { 
	      // call detail activity for clicked entry 
	   } 

	   private void handleIntent(Intent intent) { 
	      if (Intent.ACTION_SEARCH.equals(intent.getAction())) { 
	         String query = 
	               intent.getStringExtra(SearchManager.QUERY); 
	         if (query!=null)
	        	 doSearch(query); 
	      } 
	   }    

	   private void doSearch(String queryStr) { 
	   // get a Cursor, prepare the ListAdapter
	   // and set it
		   finalQueryStr = queryStr;
		   labelResult=(TextView)findViewById(R.id.labelResult);
		   TextView labelResultStr=(TextView)findViewById(R.id.labelResultStr);
		   labelResultStr.setText('"'+queryStr+'"');		   
		   int k=0;
		   book_list = ((MyApplication) this.getApplication()).getBookList();
		   maxResults =  ((MyApplication) this.getApplication()).getMaxResults();
		   resultList = book_list.searchInBooks(queryStr, maxResults, this);
		   resultList=MyApplication.removeNullFromArray(resultList);
//		   resultList = result;
//		   resultList =  new String[maxResults];
//		   for (int i=0; i<result.length; i++)
//				   if (result[i]!=null)
//				   {
//					   resultList[k]=result[i];
//					   k++;
//				   }




      
		   
	   } 
	   
	   private class ProgressTask extends AsyncTask<String, Void, Boolean> {
	        private ProgressDialog dialog;
	        List<Message> titles;
	        private Activity activity;
	        //private List<Message> messages;
	        public ProgressTask(Activity activity) {
	            this.activity = activity;
	            context = activity;
	            dialog = new ProgressDialog(context);
	        }
	        private Context context;
	        
		    protected void onPreExecute() {
		        //Log.d(TAG, " pre execute async");
		    	dialog = ProgressDialog.show(SampleSearchActivity.this, "המתן", "מבצע חיפוש...", true, true);

		    }

		    protected void onProgressUpdate(Void... progress) {
		        //Log.d(TAG, " progress async");     
		    }

		    protected void onPostExecute(final Boolean success) {
		        //Log.d(TAG, " post execute async");
//				   ArrayList<String> removed = new ArrayList<String>();
//				   for (String foundStr : resultList)
//				      if (foundStr != null)
//				         removed.add(foundStr);
//				   resultList=removed.toArray(new String[0]);	
				   
				   if(resultList.length==0)
					   labelResult.setText(getResources().getString(R.string.search_result_label_none));
				   else
					   labelResult.setText(getResources().getString(R.string.search_result_label));
				   
			       list=(ListView)findViewById(R.id.resultList);		    	
		           ArrayAdapter adapter=new ArrayAdapter(SampleSearchActivity.this, R.layout.result_item,resultList);
		           list.setAdapter(adapter);
		           
		           
		           list.setOnItemClickListener(new OnItemClickListener() {
		   			@Override
		   			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		   					long id) {
		   				Intent intent = new Intent(SampleSearchActivity.this, ShowBook.class);
		   				TextView lvItem = (TextView) arg1;
		   				String[] lvlItemStr = lvItem.getText().toString().split(": ");
		   			    String catName = lvlItemStr[1];  
		   			    String bookName = lvlItemStr[0];
		   			    Book curBook = ((MyApplication) SampleSearchActivity.this.getApplication()).getBookList().findBookByName(bookName);
					    String catFile = curBook.getCatFile(catName);
					     
		   				intent.putExtra("catName", catName);
		   				intent.putExtra("catFile", catFile);
		   				intent.putExtra("bookName", bookName);
		   				intent.putExtra("searchStr", finalQueryStr);
		   				startActivity(intent);
		   			}
		           	});       
		           if (dialog.isShowing())
		        	   dialog.dismiss();
		    }

			@Override
			protected Boolean doInBackground(String... params) {
				handleIntent(getIntent()); 
				// TODO Auto-generated method stub
				return null;
			}

		}	   
	}
