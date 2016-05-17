package com.eladcohen.aravkuk;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.util.Log;

public class Book {
	private String bookFile=null;	
	private String bookName=null;
	private String[] categories=null;
	final private String DEF_FILE_EXTENSION = "html"; 
	public Book(String bookFile,String bookName)
	{
		this.bookFile = bookFile;
		this.bookName = bookName;
	}	
	public Book(String bookFile,String bookName, String[] categoires)
	{
		this.bookFile = bookFile;
		this.bookName = bookName;
		this.categories = categoires;
	}
	public String[] getCategoires() {
		return categories;
	}	
	public void setCategoires(String[] categories) {
		this.categories = categories;
	}	
	public String getBookFile() {
		return bookFile;
	}
	public void setBookFile(String bookFile) {
		this.bookFile = bookFile;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public boolean isLastCat(String catName)
	{
		if (getCatPosition(catName)==this.categories.length-1)
			return true;
		return false;
	}
	public boolean isFirstCat(String catName)
	{
		if (getCatPosition(catName)==0)
			return true;
		return false;
	}	
	public String getNextCatName(String catName){
		int catPos = getCatPosition(catName);
		if (catPos==-1)
			return null;
		String retCatName = getCatNameByPos(++catPos);
		if (retCatName!=null)
			return retCatName;
		return null;
	}
	public String getPrevCatName(String catName){
		int catPos = getCatPosition(catName);
		if (catPos==-1)
			return null;
		String retCatName = getCatNameByPos(--catPos);
		if (retCatName!=null)
			return retCatName;
		return null;
	}	
	public String getCatFile(String catName) {
		int pos = -1;
		for (int i=0 ; i<this.categories.length ; i++)
		{
			if (catName.equals(this.categories[i]))
				pos=i;
		}
		if (pos==-1) return null;
		//String[] bookFileElements = bookFile.split("\\.(?=[^\\.]+$)");
		return bookFile+"/"+(pos+1)+"."+DEF_FILE_EXTENSION;
	}	
	public int getCatPosition(String catName) {
		for (int i=0 ; i<this.categories.length ; i++)
		{
			if (catName.equals(this.categories[i]))
					return i;
		}
		return -1;
	}	
	public String getCatNameByPos(int pos)
	{
		if (pos>=0 && pos<categories.length)
			return categories[pos];
		return null;
	}
	public String[] searchInBook(String str, int resultsNum, Activity curAct)
	{
		String curCatFile,curCatName;
		String[] foundArr=new String[resultsNum];
		int j=0;
		for (int i=0 ; i<this.categories.length && j<resultsNum ; i++)
		{
			curCatName = this.getCatNameByPos(i);
			curCatFile = this.getCatFile(curCatName);
			InputStream fin;
			byte[] buffer = null;
			try {
				fin = curAct.getAssets().open(curCatFile);
				buffer = new byte[fin.available()];
				fin.read(buffer);
				fin.close();	
				String content;

				String catStr = new String(buffer);			
				
//				String tofind = new String(str);
//				Pattern p = Pattern.compile(tofind, Pattern.DOTALL);
//				Matcher m = p.matcher(catStr);
//				Boolean found = false;
//				found = m.find();
//				if (catStr.toLowerCase().matches("(.*)"+str.toLowerCase()+"(.*)") && j<resultsNum) {

//				}
//				
				if (catStr.contains(str) && j<resultsNum)
				{
					Log.d("ELAD2", "j: " + j+" resultsNum: "+resultsNum);
					foundArr[j]=this.getBookName()+": "+curCatName;
					j++;
				}				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



		}
		return MyApplication.removeNullFromArray(foundArr);

	}
}
