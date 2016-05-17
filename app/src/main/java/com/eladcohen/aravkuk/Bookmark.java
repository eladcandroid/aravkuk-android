package com.eladcohen.aravkuk;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.util.Log;

public class Bookmark {
	private String mTitle=null;
	private String mCatName=null;
	private String mCatFile=null;	
	private String mBookName=null;
	private float mScrollY=0; 
	public Bookmark(String title, String catName, String catFile, String bookName, float scrollY)
	{
		setTitle(title);
		setCatName(catName);
		setCatFile(catFile);
		setBookName(bookName);
		setScrollY(scrollY);
	}
	public String getTitle() {
		return mTitle;
	}
	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	public String getCatFile() {
		return mCatFile;
	}
	public void setCatFile(String mCatFile) {
		this.mCatFile = mCatFile;
	}
	public String getBookName() {
		return mBookName;
	}
	public void setBookName(String mBookName) {
		this.mBookName = mBookName;
	}
	public float getScrollY() {
		return mScrollY;
	}
	public void setScrollY(float mScrollY) {
		this.mScrollY = mScrollY;
	}
	public String getCatName() {
		return mCatName;
	}
	public void setCatName(String mCatName) {
		this.mCatName = mCatName;
	}
}
