package com.zsimolabs.iowa;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.util.Log;

public class Settings extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	
	private static final String TAG = "iowa";
	
	private static final int DIALOG_ID_ABOUT = 0;
	private static final int DIALOG_ID_INFO_PRESSURE = 1;
//	private static final int DIALOG_ID_CSV_INFO = 2;
	
	ListPreference orientation, risolution;
	SharedPreferences shared_preference; 
	CheckBoxPreference cb_preference_vibrazione;
	PreferenceCategory preference_category_effett;
//	Preference donazione; 
	Preference about;
	Preference pressure_info;
	Preference cssInfoPreference;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);	
		
		cb_preference_vibrazione = (CheckBoxPreference)findPreference(getString(R.string.key_vibration));
		preference_category_effett = (PreferenceCategory)findPreference(getString(R.string.key_preference_category_effetti));
//		donazione = (Preference)findPreference(getString(R.string.key_donazione_preference));
		about = (Preference)findPreference(getString(R.string.key_about_preference));
		pressure_info = (Preference)findPreference(getString(R.string.key_info_pressure_preference));
		cssInfoPreference = (Preference)findPreference(getString(R.string.key_csv_info));

		if	(Build.MODEL.equalsIgnoreCase("MZ601"))		{
			preference_category_effett.removePreference(cb_preference_vibrazione);
		}
		
//		donazione.setOnPreferenceClickListener(new OnPreferenceClickListener() {			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				Intent intent = new Intent(getApplicationContext(), MyPayPalActivity.class);
//				startActivity(intent);
//				return true;
//			}
//		});
		about.setOnPreferenceClickListener(new OnPreferenceClickListener() {			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				showDialog(DIALOG_ID_ABOUT);
				return true;
			}
		});
		
		cssInfoPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(getApplicationContext(), DialogInfo.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
								
				return true;
			}
		});		
		
		
		pressure_info.setOnPreferenceClickListener(new OnPreferenceClickListener() {			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				showDialog(DIALOG_ID_INFO_PRESSURE);
				return true;
			}
		});
		
		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);
		Dialog dialog = null;
    	switch (id) {
		case DIALOG_ID_ABOUT:
			AlertDialog.Builder aboutBuilder_about = new AlertDialog.Builder(this);
			aboutBuilder_about.setTitle(R.string.changelog);	
			aboutBuilder_about.setMessage(R.string.changelog_testo);
			aboutBuilder_about.setCancelable(true);
			aboutBuilder_about.setPositiveButton("Ok", null);
			aboutBuilder_about.setIcon(R.drawable.ic_test);
			dialog = aboutBuilder_about.create();
			break;
			
		case DIALOG_ID_INFO_PRESSURE:
			AlertDialog.Builder infoPressureDialogBuilder = new AlertDialog.Builder(this);
			infoPressureDialogBuilder.setTitle(R.string.touch_pressure_title);	
			infoPressureDialogBuilder.setMessage(R.string.detects_the_pressure_text_info);
			infoPressureDialogBuilder.setCancelable(true);
			infoPressureDialogBuilder.setPositiveButton("Ok", null);
			infoPressureDialogBuilder.setIcon(R.drawable.ic_test);
			dialog = infoPressureDialogBuilder.create();
			break;			
			
			default:
				break;
    	}
    	return dialog;
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {		
		
		
	}

	


	
	
	
	

}
