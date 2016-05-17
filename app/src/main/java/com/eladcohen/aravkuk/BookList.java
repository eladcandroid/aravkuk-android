package com.eladcohen.aravkuk;

import android.app.Activity;

public class BookList {
private Book[] bookList=null;	
public BookList(Book[] bookList)
{
	this.bookList = bookList;
}
public Book getBookByIndex(int i)
{
	if (i<bookList.length)
		return this.bookList[i];
	return null;		
}
public String[] getBookNamesArr()
{
	String[] bookNamesArr = new String[this.bookList.length];
	for (int i=0 ; i<this.bookList.length ; i++)
	{
		bookNamesArr[i] = this.bookList[i].getBookName();
	}
	return bookNamesArr;
}
public Book findBookByName(String bookName)
{
	String curBookName;
	for (int i=0 ; i<this.bookList.length ; i++)
	{
		curBookName = this.bookList[i].getBookName();
		if (curBookName.equals(bookName))
			return this.bookList[i];
	}
	return null;
}
public String[] searchInBooks(String str, int resultsNum, Activity curAct)
{
	Book curBook;
	//String foundArr[][] = new String[this.bookList.length][resultsNum];
	String foundArr[] = new String[resultsNum];
	int k=0;
	for (int i=0 ; i<this.bookList.length && k<resultsNum ; i++)
	{
		curBook = this.bookList[i];
		String[] foundStr = curBook.searchInBook(str,resultsNum,curAct);
		int j=0;
		while (j<foundStr.length && j<resultsNum && k<resultsNum)
		{
			foundArr[k]=foundStr[j];
			j++;
			k++;
		}
	}
	return foundArr;
}
}