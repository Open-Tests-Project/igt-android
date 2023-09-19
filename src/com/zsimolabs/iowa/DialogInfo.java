package com.zsimolabs.iowa;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;

public class DialogInfo extends Activity {
	WebView webview;
	Button buttonOk;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		se voglio farla apparire più piccola come finestra di dialogo (per i tablet)
		if (android.os.Build.VERSION.SDK_INT > 10){
			setTheme(android.R.style.Theme_Dialog);
		}
		super.onCreate(savedInstanceState);
//		se voglio non far vedere la barra del titolo
		if (android.os.Build.VERSION.SDK_INT > 10){
			requestWindowFeature (Window.FEATURE_NO_TITLE);
		}

	
		setContentView(R.layout.webview);
		
		webview = (WebView)findViewById(R.id.webview);
		buttonOk = (Button)findViewById(R.id.button_ok);
		webview.getSettings().setJavaScriptEnabled(true);		
		
		String lingua = java.util.Locale.getDefault().getDisplayName();
//		Toast.makeText(getApplicationContext(), ""+java.util.Locale.getDefault().getDisplayName(), Toast.LENGTH_LONG).show();
//        if(lingua.equals("italiano (Italia)")){
		if (lingua.startsWith("italiano")){
//        	info html in italiano
        	webview.loadUrl("file:///android_asset/css-info-ita.html");        	
        }
		else if (lingua.startsWith("portu")){
//        	info html in italiano
        	webview.loadUrl("file:///android_asset/css-info-por.html");        	
        }		
		else{
//        	info html in inglese
        	webview.loadUrl("file:///android_asset/css-info-eng.html");
        }
		
		
		buttonOk.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}

}
