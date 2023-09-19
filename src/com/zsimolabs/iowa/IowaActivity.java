package com.zsimolabs.iowa;

import java.math.BigDecimal;
import java.util.Calendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IowaActivity extends Activity implements OnSharedPreferenceChangeListener {
	
	// adb logcat -s iowa
	
	private static final String TAG = "iowa";
	static final int DIALOG_ID_INFO = 0;
	static final int DIALOG_ID_HELP = 1;
	static final int REQUEST_FOR_RESULT= 1;
	EditText edit_text_partecipant;
	TextView touchPressureScore,
					    touchSpeedScore,
					    pressureStyleScore;
	RelativeLayout  relative_layout;
	Button start;
	public static String partecipant_name ="";
	public static String starting_date ="";
	public static String date_for_pdf_file_content ="";
	public static boolean nuovo_test;
	public static boolean finish;
	static boolean finito;
	static boolean show_test_button, visualizza_contatore;
	
	float startPressure, 
			 stopPressure;
    long startSpeed, 
             stopSpeed;
	
	LinearLayout testPressureLinearLayout;
	LinearLayout touchSpeedLinearLayout;
	Button testPressure;
	SharedPreferences shared_preference;
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, BigDecimal.ROUND_HALF_UP);
	    return bd.doubleValue();
	}
	
	private String getPressureSyle(float pressureDown, float pressureUp) {
		
		if (pressureDown < pressureUp) {
			return "LOW_HIGH";
		}
		else if (pressureDown > pressureUp) {
			return "HIGH_LOW";
		}
		else {
			return "EQUAL";
		}
		
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        shared_preference = PreferenceManager.getDefaultSharedPreferences(this);        
        shared_preference.registerOnSharedPreferenceChangeListener((OnSharedPreferenceChangeListener) this);

        
        show_test_button = shared_preference.getBoolean(getString(R.string.key_show_test_button), false);
        
//        Log.d(TAG, "show button: "+show_test_button);
        
        
		Calendar calendar_css = Calendar.getInstance(); 
//		String now_file_csv_label = IowaActivity.starting_date;
		String now_css = ""+calendar_css.get(Calendar.YEAR) + "_" 
										+ (calendar_css.get(Calendar.MONTH)+1) + "_" 
										+ calendar_css.get(Calendar.DAY_OF_MONTH);
        Log.d("iowa", "date: "+now_css);
		
		
        finito = false;
        finish = false;
        nuovo_test = false;
        start = (Button)findViewById(R.id.button_start);
        edit_text_partecipant = (EditText)findViewById(R.id.edit_text_partecipant_name);
        touchPressureScore = (TextView)findViewById(R.id.touch_pressure_score);
        touchSpeedScore = (TextView)findViewById(R.id.touch_speed_score);
        relative_layout = (RelativeLayout)findViewById(R.id.linear_layout);
        testPressure = (Button)findViewById(R.id.test_pressure);    	        
        testPressureLinearLayout = (LinearLayout)findViewById(R.id.test_pressure_layout); 
        pressureStyleScore = (TextView)findViewById(R.id.pressure_style_score);
        
        // permanently hide the touch speed linear layout
//        touchSpeedLinearLayout = (LinearLayout)findViewById(R.id.touch_speed_linear_layout); 
//        touchSpeedLinearLayout.setVisibility(LinearLayout.GONE);
        
        
		if (show_test_button) { 
	        testPressureLinearLayout.setVisibility(LinearLayout.VISIBLE);	        
		} 
		else {
			testPressureLinearLayout.setVisibility(LinearLayout.GONE);
		}
	        
	            
	     testPressure.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// https://code.google.com/p/markers-for-android/wiki/DeviceSupport
					
					// For a touch screen or touch pad, reports the approximate pressure applied 
					// to the surface by a finger or other tool. The value is normalized to a range 
					// from 0 (no pressure at all) to 1 (normal pressure), although values higher 
					// than 1 may be generated depending on the calibration of the input device. 

					switch (event.getAction()) {				
						case MotionEvent.ACTION_DOWN:
//							double pressure = round(event.getPressure(), 5);
//							Log.v("", "action_down " );
//							Log.v("", "*** pressure: " + pressure);						
//							touchPressureScore.setText("" + pressure);
	//						Log.v("", "down: " );
	//						Log.v("", "### size: " + event.getSize());	
	//						return true;
							
							startPressure = event.getPressure();
							startSpeed = System.currentTimeMillis();
//							Log.d(TAG, ""+System.currentTimeMillis());
							pressureStyleScore.setText(pressureStyleScore.getText()+"...");
	
						break;
						
					case MotionEvent.ACTION_UP:							
						stopPressure = event.getPressure();
						stopSpeed = System.currentTimeMillis();
						touchPressureScore.setText("" + round((startPressure+stopPressure)/2, 3));
						touchSpeedScore.setText((""+(stopSpeed - startSpeed)));
						
//						if  (startPressure < stopPressure) {
//							pressureStyleScore.setText("LOW_HIGH");
//						}
//						else if  (startPressure > stopPressure) {
//							pressureStyleScore.setText("HIGH_LOW");
//						}
//						else {
//							pressureStyleScore.setText("EQUAL");
//						}
						
						pressureStyleScore.setText(getPressureSyle(startPressure, stopPressure));
						
//						Log.d(TAG, ""+event.getPressure());
//						Log.d(TAG, ""+event.getPressure( event.getActionIndex()));

						break;
										
					default:
						break;
											
					}			
					
					return false;
				}
			});
        

        start.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				Calendar calendar = Calendar.getInstance(); 
				starting_date = ""+calendar.get(Calendar.YEAR) + "-" 
						+ (calendar.get(Calendar.MONTH) +1)+ "-" 
						+ calendar.get(Calendar.DAY_OF_MONTH)+	"_" 
						+ calendar.get(Calendar.HOUR_OF_DAY)+ "-" 
						+ calendar.get(Calendar.MINUTE);			
				Log.d("iowa", starting_date);
				
				date_for_pdf_file_content = calendar.getInstance().getTime().toString();
				
				
				Intent intent = new Intent(getApplicationContext(), Spiegazione2.class);
				startActivityForResult(intent, REQUEST_FOR_RESULT);
				partecipant_name = edit_text_partecipant.getText().toString();
				nuovo_test = true;
				ProfiloCarteSelezionate.finish = false;
				Finish.finish = false;
				Gambling.finish = false;
				Spiegazione2.finish = false;
				IowaActivity.finish = false;
			}
		});        
    }
    
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK || finito) {
			finish();
		}
	}
     
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater menu_inflater = getMenuInflater();
		menu_inflater.inflate(R.menu.menu_iowa_activity, menu);
		return true;
	}
    
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    	ProfiloCarteSelezionate.finish = true;
		Finish.finish = true;
		Gambling.finish = true;
		Spiegazione2.finish = true;
		IowaActivity.finish = true;
		finish();
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.iowa_activity:
			ProfiloCarteSelezionate.finish = true;
			Finish.finish = true;
			Gambling.finish = true;
			Spiegazione2.finish = true;
			IowaActivity.finish = true;
			finish();
//			int pid = android.os.Process.myPid();
//			android.os.Process.killProcess(pid);
			return true;
			
		case R.id.info:
			showDialog(DIALOG_ID_INFO);
			return true;
			
		case R.id.settings:
			Intent setting_intent = new Intent(this, Settings.class);
			startActivity(setting_intent);
			return true;
			
		case R.id.help:
			showDialog(DIALOG_ID_HELP);
			return true;
	
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	

	
	@Override
    protected Dialog onCreateDialog(int id) 
    {
    	super.onCreateDialog(id);
    	Dialog dialog = null;
    	switch (id) 
    	{
		case DIALOG_ID_INFO:
			Dialog customDialog = new Dialog(this);
			customDialog.setContentView(R.layout.dialog_about);
			customDialog.setTitle(getString(R.string.app_name)+" "+ getString(R.string.ver_number));
			dialog = customDialog;
			break;
			
		case DIALOG_ID_HELP:
//			Dialog helpDialog = new Dialog(this);
//			helpDialog.setContentView(R.layout.dialog_help);
//			helpDialog.setTitle(R.string.app_name);
//			dialog = helpDialog;
			
			AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
			helpBuilder.setTitle(getString(R.string.app_name) +" "+ getString(R.string.ver_number));	
			helpBuilder.setMessage(R.string.help_message1);
			helpBuilder.setCancelable(true);
			helpBuilder.setPositiveButton("Ok", null);
			helpBuilder.setIcon(R.drawable.ic_test);
			dialog = helpBuilder.create();
			break;
		default:
			break;
		}
    	return dialog;
    }


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) { 
		// TODO Auto-generated method stub
		
		if (sharedPreferences.getBoolean(getString(R.string.key_show_test_button), false)) {
			 testPressureLinearLayout.setVisibility(LinearLayout.VISIBLE);
		}
		else {
			 testPressureLinearLayout.setVisibility(LinearLayout.GONE);
		}
		
	}
}