package com.zsimolabs.iowa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Spiegazione2 extends Activity {
//	TextView text_view;
	Button button_continue;
	
//	LinearLayout linear_layout;
	static final int REQUEST_FOR_RESULT= 1;
	static boolean finish;
	static final int CHIUDI_VAI_SPIEGAZIONE = 22; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_spiegazione2);
		
		button_continue = (Button)findViewById(R.id.button_continue);
		
		button_continue.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), Gambling.class);
				startActivityForResult(intent, REQUEST_FOR_RESULT);
			}
		});
		
//		button_continue.setOnTouchListener(new View.OnTouchListener() {			
//		@Override
//			public boolean onTouch(View arg0, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_DOWN) {
//					Intent intent = new Intent(getApplicationContext(), Gambling.class);
//					startActivityForResult(intent, REQUEST_FOR_RESULT);
//				}
//				return true;
//			}
//		});
		
		
//		text_view = (TextView)findViewById(R.id.text_view_spiega2);
		
	
		
		
//		text_view.setOnTouchListener(new View.OnTouchListener() {			
//			@Override
//			public boolean onTouch(View arg0, MotionEvent event) {
//				if(event.getAction() == MotionEvent.ACTION_DOWN){
//					Intent intent = new Intent(getApplicationContext(), Gambling.class);
//					startActivityForResult(intent, REQUEST_FOR_RESULT);
//				}
//				return true;
//			}
//		});
		
//		linear_layout = (LinearLayout)findViewById(R.id.linear_layout_spiega2);
//		linear_layout.setOnTouchListener(new View.OnTouchListener() {			
//			@Override
//			public boolean onTouch(View arg0, MotionEvent event) {
//				
//				if (event.getAction() == MotionEvent.ACTION_DOWN) {
//					Intent intent = new Intent(getApplicationContext(), Gambling.class);
//					startActivityForResult(intent, REQUEST_FOR_RESULT);
//				}
//				return true;
//			}
//		});		
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
//		setResult(Activity.RESULT_OK);
//		Intent intent = new Intent(getApplicationContext(), IowaActivity.class);
//		startActivityForResult(intent, IowaActivity.REQUEST_FOR_RESULT);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != CHIUDI_VAI_SPIEGAZIONE){
			finish();
		}
//		int pid = android.os.Process.myPid();
//		android.os.Process.killProcess(pid);
	}


}
