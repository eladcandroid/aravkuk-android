package com.eladcohen.aravkuk;

import com.eladcohen.aravkuk.R;

import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;

public class MyApplication extends Application{

	private final int MAX_RESULTS=20;
	private static final boolean DEFAULT_BLACK_SCREEN = false;
	private static final boolean DEFAULT_SCREEN_LOCK = false;
	
    private BookList bookList;
    public static String appName = "כתבי הרב קוק"; 
    
    public static boolean getDefaultBlackScreenValue(){
        return DEFAULT_BLACK_SCREEN;
    }
    
    public static boolean getDefaultScreenLockValue(){
        return DEFAULT_SCREEN_LOCK;
    }    
    
    public static void goMigzarNews(){
    	
    }
    
    public int getMaxResults(){
        return MAX_RESULTS;
      }
      
    public BookList getBookList() {
        return bookList;
    }

    public void setBookList(BookList bookList) {
        this.bookList = bookList;
    }
    
    public static String[] removeNullFromArray(String[] arr)
    {
    	
		int count=0;
		int j=0;
		for (int i=0 ; i<arr.length ; i++)
		{
			if (arr[i]!=null)
			{
				count++;
			}	
		}
		
		String[] removedArr = new String[count];
		for (int i=0 ; i<arr.length ; i++)
		{
			if (arr[i]!=null)
			{
				removedArr[j]=arr[i];
				j++;
			}	
		}    	
    	
    	return removedArr;
    }
    
    private static Context context;

    public void onCreate(){
        super.onCreate();
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        MyApplication.context = getApplicationContext();
        Book[] books_array = new Book[] { 


        		new Book("orot","אורות"
        		,new String[] {
        		"א - ארץ ישראל",
        		"ב - המלחמה",
        		"ג - ישראל ותחיתו",
        		"ד - אורות התחיה",
        		"ה - למהלך האידאות בישראל",
        		"ו - זרעונים",
        		"ז - אורות ישראל",
        		}),


        		new Book("orot_hatora","אורות התורה"
        		,new String[] {
        		"הקדמה",
        		"פרק א - תורה שבכתב ותורה שבעל פה",
        		"פרק ב - תורה לשמה",
        		"פרק ג - פרטי תורה וכללותיה",
        		"פרק ד - כל התורה כולה",
        		"פרק ה - אותיות התורה",
        		"פרק ו - למוד התורה",
        		"פרק ז - צער בטול תורה",
        		"פרק ח - הלמוד והמעשה",
        		"פרק ט - דרכי תורה ונתיבותיה",
        		"פרק י - לסתרי תורה ורזיה",
        		"פרק יא - פעולת התורה והדרכתה",
        		"פרק יב - התורה לישראל ולעולם",
        		"פרק יג - תורת חוץ לארץ ותורת ארץ ישראל",
        		}), 



        		new Book("orot_hatshuva","אורות התשובה"
        		,new String[] {
        		"הקדמה למהדורה הראשונה",
        		"הקדמה למהדורה השניה",
        		"פתיחה",
        		"פרק א - תשובה טבעית, אמונית, שכלית",
        		"פרק ב - תשובה פתאומית ותשובה הדרגית",
        		"פרק ג - תשובה פרטית מפורשת ותשובה סתמית כללית",
        		"פרק ד - התשובה הפרטית היחידית והתשובה הכללית הצבורית העולמית, בעולם ובכנסת ישראל",
        		"פרק ה - הכרחיות מציאות התשובה ופעולתה באדם בעולם ובכנסת ישראל",
        		"פרק ו - מציאותה ופעולתה הפנימית של התשובה בעמקי גנזי האדם והעולם וכנסת ישראל",
        		"פרק ז - ערכם של הרהורי תשובה, ציורה ומחשבתה",
        		"פרק ח - מכאובי החטא ויסורי התשובה ומרפא הארתה",
        		"פרק ט - ערך הרצון המתגלה על ידי התשובה",
        		"פרק י - הכרחיות התשובה והתורה זה לזה בכללותן ובמעלותיהן העליונות",
        		"פרק יא - מקורות התשובה בכללות ההויה וברוחניותה העליונה",
        		"פרק יב - השפעת התשובה על מהלכי הרוח החיים והמעשה בכלל",
        		"פרק יג - ארחות תשובה רוחניות ומעשיות",
        		"פרק יד - נתיבות תשובה פרטיות",
        		"פרק טו - יסודות התשובה לפרט ולכלל",
        		"פרק טז - שרשי התשובה ופנימיותה",
        		"פרק יז - ההתגלות הגדולה של התשובה בחיי ישראל ובתחיתו בארצנו",
        		}), 



        		new Book("musar_avicha","מוסר אביך"
        		,new String[] {
        		"מעין הקדמה - צורך לכתיבה רשימות ענינים לעיון וללימוד",
        		"פרק א - מצות יראת-ד' ולמודה",
        		"פרק ב - סדור כחות הנפש בדרכי עבודת ד'",
        		"פרק ג - בירורי מדות הנפש",
        		"פרק ד - חשבון הנפש בעבודת ה' והערכת מדרגותיה",
        		}), 



        		new Book("midot_hareaya","מידות הראי\"ה"
        		,new String[] {
        		"אל המידות",
        		"אהבה",
        		"אמונה",
        		"ברית",
        		"גאוה",
        		"דבקות",
        		"העלאת ניצוצות",
        		"חופש",
        		"יראה",
        		"כבוד",
        		"כעס",
        		"סבלנות",
        		"ענוה",
        		"פחדנות",
        		"צדקות",
        		"צניעות",
        		"רצון",
        		"תוכחה",
        		"תיקון",
        		}), 



        		new Book("reish_milin","ראש מילין"
        		,new String[] {
        		"פתיחה",
        		"א - האותיות",
        		"ב - התגין",
        		"ג - הנקודות",
        		"ד -  הטעמים",
        		"ה -  הברקה",
        		"ו - התגין",
        		"הערות",
        		"צפיה",

        		}), 



        		new Book("8_kvatsim","שמונה קבצים"
        		,new String[] {
        		"קובץ א",
        		"קובץ ב",
        		"קובץ ג",
        		"קובץ ד",
        		"קובץ ה",
        		"קובץ ו",
        		"קובץ ז",
        		"קובץ ח",

        		}), 



        		new Book("orot_hakodesh_a","אורות הקודש א"
        		,new String[] {
        		"שער ראשון: חכמת הקודש",
        		"שער שני: הגיון הקודש",

        		}), 

        		new Book("orot_hakodesh_b","אורות הקודש ב"
        		,new String[] {
        		"מאמר רביעי: הטוב הכללי",
        		"מאמר חמישי: התעלות העולם",
        		}), 


				new Book("orot_hakodesh_c","אורות הקודש ג"
				,new String[] {
				"ראש דבר- מוסר ויראת אלהים",
				"שער ראשון: מוסר הקודש",
				"שער שני: דרך הקודש",

				}), 


				new Book("orot_hakodesh_d","אורות הקודש ד"
				,new String[] {
				"סדר ראשון: האהבה הכוללת",
        		"סדר שני: הדבקות הכללית",
        		"סדר שלישי: הענוה האצילית",
        		"סדר רביעי: השלום",
				"סדר חמישי: העלאת העולם",
        		}), 


        		new Book("hazon","חזון הצמחונות והשלום"
        		,new String[] {
        		"א",
        		"ב",
        		"ג",
        		"ד",
        		"ה",
        		"ו",
        		"ז",
        		"ח",
        		"ט",
        		"י",
        		"יא",
        		"יב",
        		"יג",
        		"יד",
        		"טו",
        		}),


        		new Book("eder_hayakar","אדר היקר"
        		,new String[] {
        		"מעט צרי",
        		"א",
        		"ב",
        		"ג",
        		"ד",
        		"ה",
        		"אגרות האדר\"ת",
        		}),
				
				new Book("ikvey_hatzon","עקבי הצאן"
        		,new String[] {
        		"הדור",
        		"הענג והשמחה",
        		"הפחד",
        		"המחשבות",
        		"דרישת ה'",
        		"דעת אלהים",
        		"עבודת אלהים",
        		}),



        		};
        this.bookList = new BookList(books_array);
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
    
}