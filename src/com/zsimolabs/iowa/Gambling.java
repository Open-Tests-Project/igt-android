package com.zsimolabs.iowa;

import java.util.ArrayList;
import java.util.Calendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Gambling extends Activity {
	
	// adb logcat -s iowa
	
	private static final String TAG = "iowa";
	static final int DIALOG_SICURO_USCIRE = 27;
	static final float VOL_ERROR =  AudioManager.STREAM_MUSIC;
	static final float VOL_CORRECT = (float) (AudioManager.STREAM_MUSIC-0.2);
	static final String KEY_PREFERENCE = "KEY_PREFERENCE";
	static final String KEY_COUNT = "KEY_COUNT";
	static final String KEY_COUNT_A = "KEY_COUNT_A";
	static final String KEY_COUNT_B = "KEY_COUNT_B";
	static final String KEY_COUNT_C = "KEY_COUNT_C";
	static final String KEY_COUNT_D = "KEY_COUNT_D";
	static final String KEY_MONEY = "KEY_MONEY";
	static final String KEY_CHOICE_SAVE = "KEY_CHOICE_SAVE";
	static final String KEY_PENALTY_SAVE = "KEY_PENALTY_SAVE";
	static final String KEY_REWARD_SAVE = "KEY_REWARD_SAVE";
	static final String KEY_GAIN_SAVE = "KEY_GAIN_SAVE";
	static final String KEY_COLOR = "KEY_COLOR";
	static final int REQUEST_FOR_RESULT = 1;
	final static int REWARD_AB = 100;
	final static int REWARD_CD = 50;
	final static int MAX_LEVEL = 10000;
	final static int CONSTANT = 1000;
	
	final static int NUMBER_OF_CARDS = 100; // 100 default
	
	static Calendar calendar;
	static String canRun = "OK";
	// +++
//	static boolean getPressure = true;
	
	RelativeLayout risultati;
	View barra;
	Drawable d;
	ClipDrawable clipDrawable;
	TextView view1, view2, view3, view4, choice, reward, penalty, gain, total,
			tv_choice, tv_reward, tv_penalty, tv_gain, numero_carta_selezionata, n_carta_sel_dicitura;
	static String choice_save, reward_save, penalty_save, gain_save;
	static int money, count, count_A, count_B, count_C, count_D, money_finish,
			color;
		
	static ArrayList<Integer> memoria;
	static ArrayList<Integer> rewardsArray;
	static ArrayList<String> penaltiesArray;
	static ArrayList<String> gainsArray;
	static ArrayList<String> totalsArray;
	static ArrayList<String> deckChoices;
	static ArrayList<String> times;
	static ArrayList<Long> responsesTime;
	static ArrayList<Float> pressures;
	static ArrayList<Long> speedTouch;
	static ArrayList<String> pressureStyle;
	
	static long inizio;
	static long fine;
	static long startTrial, 
						startTimeDown,
						stopTimeUp; 
	
	static float pressureDown, 
						pressureUp;
	// static String tempo;
	static boolean finish, vibrazione, suono_errore, suono_corretto, visualizza_contatore;
	SharedPreferences shared_preference;
	SoundPool error, correct;
	int error_sound, correct_sound;
	Vibrator vibrator;
	static final int CHIUDI_VAI_SPIEGAZIONE = 22; 

	final static int[] PENALTY_MAZZO_A = {
		   //  1   2   3   4   5   6   7   8   9  10
			0, 0, 150, 0, 300, 0, 200, 0, 250, 350, 
			0, 350, 0, 250, 200, 0, 300, 150, 0, 0, 
			0, 300, 0, 350, 0, 200, 250, 150, 0, 0,
			350, 200, 250, 0, 0, 0, 150, 300, 0, 0,
			
			0, 0, 150, 0, 300, 0, 200, 0, 250, 350,
			0, 350, 0, 250, 200, 0, 300, 150, 0, 0,
			0, 300, 0, 350, 0, 200, 250, 150, 0, 0,
			350, 200, 250, 0, 0, 0, 150, 300, 0, 0, 
			
			0, 0, 150, 0, 300, 0, 200, 0, 250, 350,
			0, 350, 0, 250, 200, 0, 300, 150, 0, 0 };

	final static int[] PENALTY_MAZZO_B = { 0, 0, 0, 0, 0, 0, 0, 0, 1250, 0, 0,
			0, 0, 1250, 0, 0, 0, 0, 0, 0, 1250, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			1250, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1250, 0, 0,
			0, 0, 1250, 0, 0, 0, 0, 0, 0, 1250, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			1250, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1250, 0, 0,
			0, 0, 1250, 0, 0, 0, 0, 0, 0 };

//	final static int[] PENALTY_MAZZO_C = { 0, 0, 50, 0, 50, 0, 50, 0, 50, 50,
//			0, 25, 75, 0, 0, 0, 25, 75, 50, 0, 0, 0, 0, 50, 25, 50, 0, 0, 75,
//			50, 0, 0, 0, 25, 25, 75, 0, 50, 75, 0, 0, 0, 50, 0, 50, 0, 50, 0,
//			50, 50, 0, 25, 75, 0, 0, 0, 25, 75, 50, 0, 0, 0, 0, 50, 25, 50, 0,
//			0, 75, 50, 0, 0, 0, 25, 25, 75, 0, 50, 75, 0, 0, 0, 50, 0, 50, 0,
//			50, 0, 50, 50, 0, 25, 75, 0, 0, 0, 25, 75, 50, 0 };
	
	final static int[] PENALTY_MAZZO_C = {
        //  1   2   3   4   5   6   7   8   9  10
            0,  0, 50,  0, 50,  0, 50,  0, 50, 50,
			0, 25, 75,  0,  0,  0, 25, 75,  0, 50,
            0,  0,  0, 50, 25, 50,  0,  0, 75, 50,
            0,  0,  0, 25, 25,  0, 75,  0, 50, 75,
    
            0,  0, 50,  0, 50,  0, 50,  0, 50, 50,
            0, 25, 75,  0,  0,  0, 25, 75,  0, 50,
            0,  0,  0, 50, 25, 50,  0,  0, 75, 50,
            0,  0,  0, 25, 25,  0, 75,  0, 50, 75,
    
            0,  0, 50,  0, 50,  0, 50,  0, 50, 50,
            0, 25, 75,  0,  0,  0, 25, 75,  0, 50};	

	final static int[] PENALTY_MAZZO_D = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 250, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 250, 0, 0, 0, 0, 0, 0, 0, 0, 250, 0, 0, 0,
			0, 0, 250, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 250, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 250, 0, 0, 0, 0, 0, 0, 0, 0, 250, 0, 0, 0, 0, 0,
			250, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 250, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 250 };
	

	private void __init__() {
		SharedPreferences prefs = getSharedPreferences(KEY_PREFERENCE,
				Context.MODE_PRIVATE);
		
		count = prefs.getInt(KEY_COUNT, 0);
		count_A = prefs.getInt(KEY_COUNT_A, 0);
		count_B = prefs.getInt(KEY_COUNT_B, 0);
		count_C = prefs.getInt(KEY_COUNT_C, 0);
		count_D = prefs.getInt(KEY_COUNT_D, 0);
		money = prefs.getInt(KEY_MONEY, 2000);
		choice_save = prefs.getString(KEY_CHOICE_SAVE, "");
		reward_save = prefs.getString(KEY_REWARD_SAVE, "");
		penalty_save = prefs.getString(KEY_PENALTY_SAVE, "");
		gain_save = prefs.getString(KEY_GAIN_SAVE, "");
		color = prefs.getInt(KEY_COLOR, Color.LTGRAY);
		risultati = (RelativeLayout) findViewById(R.id.relative_layout_risultati);
		view1 = (TextView) findViewById(R.id.view1);
		view2 = (TextView) findViewById(R.id.view2);
		view3 = (TextView) findViewById(R.id.view3);
		view4 = (TextView) findViewById(R.id.view4);
		total = (TextView) findViewById(R.id.text_view_total);
		numero_carta_selezionata = (TextView)findViewById(R.id.numero_carta_selezionata);
		n_carta_sel_dicitura = (TextView)findViewById(R.id.textView5);
		barra = findViewById(R.id.barra);
		clipDrawable = (ClipDrawable) getResources().getDrawable(
				R.drawable.clips);
		barra.setBackgroundDrawable(clipDrawable);
		clipDrawable.setLevel(__setbarlevel__(money));
		choice = (TextView) findViewById(R.id.tv_choice);
		reward = (TextView) findViewById(R.id.tv_reward);
		penalty = (TextView) findViewById(R.id.tv_penalty);
		gain = (TextView) findViewById(R.id.tv_net_gain);
		tv_choice = (TextView) findViewById(R.id.text_view_choice);
		tv_reward = (TextView) findViewById(R.id.text_view_reward);
		tv_penalty = (TextView) findViewById(R.id.text_view_penalty);
		tv_gain = (TextView) findViewById(R.id.text_view_net_gain);
		vibrazione = shared_preference.getBoolean(getString(R.string.key_vibration), false);
		suono_errore = shared_preference.getBoolean(getString(R.string.key_suono_errore), false);
		suono_corretto = shared_preference.getBoolean(getString(R.string.key_suono_corretto), false);
		visualizza_contatore = shared_preference.getBoolean(getString(R.string.key_contatore_carte), false);
		error = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	    error_sound = error.load(this, R.raw.errore, 1);
	    correct = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	    correct_sound = correct.load(this, R.raw.correct, 1);  
	    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    
		memoria = new ArrayList<Integer>();
		rewardsArray = new ArrayList<Integer>();
		penaltiesArray = new ArrayList<String>();
		gainsArray = new ArrayList<String>();
		totalsArray = new ArrayList<String>();
		deckChoices = new ArrayList<String>();
		times = new ArrayList<String>();
		responsesTime = new ArrayList<Long>();
		pressures = new ArrayList<Float>();
		speedTouch = new ArrayList<Long>();
		pressureStyle = new ArrayList<String>();
		
	}

	private void __visualizza__() {
		total.setText(" " + money);
		clipDrawable.setLevel(__setbarlevel__(money));
		__setcolor__(color);
		choice.setText(choice_save);
		reward.setText(reward_save);
		penalty.setText(penalty_save);
		gain.setText(gain_save);
	}

	private void __visualizza_reset__() {
		total.setText(" " + money);
		clipDrawable.setLevel(__setbarlevel__(money));
		__setcolor__(color);
		choice.setText("");
		reward.setText("");
		penalty.setText("");
		gain.setText("");
		numero_carta_selezionata.setText(""+count);
	}

	private void __reset__() {
		inizio = 0;
		fine = 0;
		money = 2000;
		count = 0;
		count_A = 0;
		count_B = 0;
		count_C = 0;
		count_D = 0;
		color = Color.LTGRAY;
		
		__visualizza_reset__();
		ProfiloCarteSelezionate.grafico1_salvato = false;
		TotalNumberCards.grafico2_salvato = false;
		view1.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.view_drawable2));
		view2.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.view_drawable2));
		view3.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.view_drawable2));
		view4.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.view_drawable2));
		
		memoria.clear();
		rewardsArray.clear();
		penaltiesArray.clear();
		gainsArray.clear();
		totalsArray.clear();
		deckChoices.clear();
		times.clear();
		responsesTime.clear();
		pressures.clear();
		speedTouch.clear();
		pressureStyle.clear();
		
		canRun = "OK";
		startTrial = System.currentTimeMillis();
	}
	
	private void debug() {		
		Log.d(TAG, " ");
		Log.d(TAG, " FINISH DEBUG:");
		Log.d(TAG, "memoria size: " + memoria.size());
		Log.d(TAG, "rewardsArray size: " + rewardsArray.size());
		Log.d(TAG, "penaltiesArray size: " + penaltiesArray.size());
		Log.d(TAG, "gainsArray size: " + gainsArray.size());
		Log.d(TAG, "totalsArray size: " + totalsArray.size());
		Log.d(TAG, "deckChoices size: " + deckChoices.size());
		Log.d(TAG, "times size: " + times.size());
		Log.d(TAG, "responsesTime size: " + responsesTime.size());
		Log.d(TAG, "pressures size: " + pressures.size());
	}

	private void __finish__() {
		// +++
		debug();
			
		money_finish = money;
		fine = System.currentTimeMillis();
		Intent intent = new Intent(getApplicationContext(), Finish.class);
		startActivityForResult(intent, REQUEST_FOR_RESULT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		finish();
	}

	private void __setcolor__(int c) {
		choice.setTextColor(c);
		reward.setTextColor(c);
		penalty.setTextColor(c);
		gain.setTextColor(c);
		tv_choice.setTextColor(c);
		tv_reward.setTextColor(c);
		tv_penalty.setTextColor(c);
		tv_gain.setTextColor(c);
	}

	private void __setcolor__(int[] mazzo, int c, int r) {
		if (mazzo[c] > r) {
			choice.setTextColor(Color.RED);
			reward.setTextColor(Color.RED);
			penalty.setTextColor(Color.RED);
			gain.setTextColor(Color.RED);
			tv_choice.setTextColor(Color.RED);
			tv_reward.setTextColor(Color.RED);
			tv_penalty.setTextColor(Color.RED);
			tv_gain.setTextColor(Color.RED);
		}
		else if (mazzo[c] < r) {
			choice.setTextColor(Color.GREEN);
			reward.setTextColor(Color.GREEN);
			penalty.setTextColor(Color.GREEN);
			gain.setTextColor(Color.GREEN);
			tv_choice.setTextColor(Color.GREEN);
			tv_reward.setTextColor(Color.GREEN);
			tv_penalty.setTextColor(Color.GREEN);
			tv_gain.setTextColor(Color.GREEN);
		}
//		if (mazzo[c] == r) {
		else {
			choice.setTextColor(Color.LTGRAY);
			reward.setTextColor(Color.LTGRAY);
			penalty.setTextColor(Color.LTGRAY);
			gain.setTextColor(Color.LTGRAY);
			tv_choice.setTextColor(Color.LTGRAY);
			tv_reward.setTextColor(Color.LTGRAY);
			tv_penalty.setTextColor(Color.LTGRAY);
			tv_gain.setTextColor(Color.LTGRAY);
		}
	}

	private void __set_cards_color(TextView view, int reward, int[] penalty, int count) {
		if (count < penalty.length) {
			if (penalty[count] < reward) {		
				view.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.view_drawable2));
			}
			else if (penalty[count] == reward) {
				view.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.view_drawable3));
			}
//			if (penalty[count] > reward) {
			else {
				view.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.view_drawable));
			}
		}
	}
	
	private void __set_effects__(TextView view, int reward, int[] penalty, int count){
		if (count < penalty.length) {
		if (penalty[count] < reward) {
			if(suono_corretto){
				correct.play(correct_sound, VOL_CORRECT, VOL_CORRECT, 0, 0, 1);
			}	
		}
		if (penalty[count] > reward) {
			if (vibrazione){
				vibrator.vibrate(50);
			}
			if (suono_errore){
				error.play(error_sound, VOL_ERROR, VOL_ERROR, 0, 0, 1);	
			}
		}
	}
		
	}

	private String __setpenalty__(int[] mazzo, int count) {
		String penalty = null;
		if (count < mazzo.length) {
			penalty = "-" + mazzo[count];
			//Log.d(TAG, "--- penalty: "+mazzo[count]);
		} else {
			// Toast.makeText(getApplicationContext(),
			// "Errore. Indice più grande delle dimensioni dell'array\nmetodo __setpenalty__",
			// Toast.LENGTH_LONG).show();
		}
		return penalty;
	}

	private String __setgain__(int[] mazzo, int count, int reward) {
		String gain = null;
		if (count < mazzo.length) {
			gain = "" + (reward - mazzo[count]);
		} else {
			// Toast.makeText(getApplicationContext(),
			// "Errore. Indice più grande delle dimensioni dell'array\nmetodo __setgain__",
			// Toast.LENGTH_LONG).show();
		}
		return gain;
	}
	
	private void saveTimeAndPressure( MotionEvent event) {
		// TODO 
		pressureUp = event.getPressure();		
		calendar = Calendar.getInstance();																								
//		times.add(""+calendar.get(Calendar.HOUR_OF_DAY)+"_"+calendar.get(Calendar.MINUTE)+"_"
//							+calendar.get(Calendar.SECOND) + "_" +calendar.get(Calendar.MILLISECOND));
		times.add(""+calendar.get(Calendar.HOUR_OF_DAY)+"_"+calendar.get(Calendar.MINUTE)+"_"
				+calendar.get(Calendar.SECOND));		
		stopTimeUp = System.currentTimeMillis();
		responsesTime.add(stopTimeUp - startTrial);
		Log.d(TAG, "respone time: "+(stopTimeUp - startTrial));
					
		pressures.add((pressureDown+pressureUp)/2);
		Log.d(TAG, "pressure: "+(pressureDown+event.getPressure())/2);		
		
		String pressureStyleTemp = getPressureSyle(pressureDown, pressureUp);
		pressureStyle.add(pressureStyleTemp);
		Log.d(TAG, "pressureStyle "+pressureStyleTemp);		
		
		speedTouch.add(stopTimeUp - startTimeDown);
		Log.d(TAG, "speed touch "+(stopTimeUp - startTimeDown));		
		
		startTrial = System.currentTimeMillis();	
		
//		Log.d(TAG, "memoria "+ memoria);
		
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

	// i parametri sono rispettivamente l'array che rappresenta le specifiche
	// penalità di ognuno dei 4 mazzi, l'indice che
	// rappresenta quante carte sono state girate per ogni mazzo, la ricompensa
	// fissa per ogni mazzo
	private String __settotal__(int[] mazzo, int count, int reward) {
		String total = null;
		if (count < mazzo.length) {
			money = money + reward - mazzo[count];
		} else {
			// Toast.makeText(getApplicationContext(),
			// "Errore. Indice più grande delle dimensioni dell'array\nmetodo __settotal__",
			// Toast.LENGTH_LONG).show();
		}
		total = "" + money;
		return total;
	}

	// il parametro m è l'attuale valore della variabile money
	private int __setbarlevel__(int m) {
		int level = 0;
		level = ((m + CONSTANT) * MAX_LEVEL) / 6000;
		return level;
	}

	private void __add_memoria(int count, int carta) {
		if (count <= (memoria.size() - 1)) {
			// Toast.makeText(getApplicationContext(),
			// "Errore. carta selezionata già inserita in memoria\nmetodo  __ad_memoria",
			// Toast.LENGTH_LONG).show();
		}
		memoria.add(/* count, */carta);
	}
	
	@Override
	public void onBackPressed() {
//		super.onBackPressed();
//		Intent intent = new Intent(getApplicationContext(), Spiegazione2.class);
//		startActivityForResult(intent, IowaActivity.REQUEST_FOR_RESULT);
		
		showDialog(DIALOG_SICURO_USCIRE);
	}
	
	@Override
	protected void onStart() {

		super.onStart();
		
		Log.d(TAG, "start");
		canRun = "OK";
		startTrial = System.currentTimeMillis();
	}
	
	
	@Override
    protected Dialog onCreateDialog(int id){
    	super.onCreateDialog(id);
    	Dialog dialog = null;
    	switch (id) {	
		case DIALOG_SICURO_USCIRE:
			AlertDialog.Builder aboutBuilder_dialog = new AlertDialog.Builder(this);
			aboutBuilder_dialog.setTitle(R.string.vuoi_terminare_test);	
			aboutBuilder_dialog.setIcon(R.drawable.ic_test);
//			aboutBuilder_dialog.setMessage("");
			aboutBuilder_dialog.setCancelable(false);
			aboutBuilder_dialog.setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
//					if (haptic_feedback){
//						vibrator.vibrate(30);
//					}
//					int pid = android.os.Process.myPid();
//					android.os.Process.killProcess(pid);
					setResult(Activity.RESULT_OK);
					finish();
//					Intent intent = new Intent(getApplicationContext(), Spiegazione2.class);
//					startActivityForResult(intent, IowaActivity.REQUEST_FOR_RESULT);
					
				}
			});
			aboutBuilder_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
//					non serve, dismette da solo
//					dismissDialog(DIALOG_SICURO_USCIRE);
//					if (haptic_feedback){
//						vibrator.vibrate(30);
//					}
				}
			});
			dialog = aboutBuilder_dialog.create();
			break;				
		default:
			break;
		}
    	return dialog;
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//+++
		if (android.os.Build.VERSION.SDK_INT < 11) {
//		if (getWindow().getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_90 ||
//			getWindow().getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_270) { 
				requestWindowFeature(Window.FEATURE_NO_TITLE);
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,	
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		setContentView(R.layout.main_gambling);
//		con il bilancere del volume regolo il volume multimediale, non più la suoeria.
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		shared_preference = PreferenceManager.getDefaultSharedPreferences(this);
		
		try {
			__init__();
		} catch (Exception e) {
			Log.d(TAG, "error: "+e);		
		}
		
		

		if (IowaActivity.nuovo_test) {
			__reset__();
		} else {
			__visualizza__();
		}
		
		if (visualizza_contatore){
			numero_carta_selezionata.setVisibility(TextView.VISIBLE);		
			n_carta_sel_dicitura.setVisibility(TextView.VISIBLE);		
			numero_carta_selezionata.setText(""+count);
		}
		else{
			numero_carta_selezionata.setVisibility(TextView.INVISIBLE);		
			n_carta_sel_dicitura.setVisibility(TextView.INVISIBLE);		
		}
		
		
		view1.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if (canRun == "A" || canRun == "OK") {
										
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							canRun = "A";
							
							startTimeDown = System.currentTimeMillis();
							
							Log.d(TAG, " ");		
							Log.d(TAG, "___DOWN___A__________");		
							
							pressureDown = event.getPressure();
	//						Log.d(TAG, "*** pressure: " + pressure);
												
						}				
						else if (event.getAction() == MotionEvent.ACTION_UP) {					
											
							if (count == NUMBER_OF_CARDS) {
								__finish__();
							} 
							else {
//								stopTimeUp = System.currentTimeMillis();
//								calendar = Calendar.getInstance();																								
//								times.add(""+calendar.get(Calendar.HOUR_OF_DAY)+"_"+calendar.get(Calendar.MINUTE)+"_"
//													+calendar.get(Calendar.SECOND) + "_" +calendar.get(Calendar.MILLISECOND));
//								responsesTime.add(stopTimeUp - startTrial);
//								pressures.add((pressureDown+event.getPressure())/2);
//								speedTouch.add(stopTimeUp - startTimeDown);
//								startTrial = System.currentTimeMillis();	
								
								saveTimeAndPressure(event);
															
								Log.d(TAG, "___UP_____A__________");						
//								Log.d(TAG, "time: "+(stopTimeUp - startTrial));																
								
								choice.setText("A");				
								deckChoices.add("A");
								
								reward.setText("" + REWARD_AB);					
								rewardsArray.add(REWARD_AB);
								
								String p = __setpenalty__(PENALTY_MAZZO_A, count_A);
								penalty.setText(p);					
								penaltiesArray.add(p);
								
								String g = __setgain__(PENALTY_MAZZO_A, count_A, REWARD_AB);
								gain.setText(g);					
								gainsArray.add(g);
								
								__setcolor__(PENALTY_MAZZO_A, count_A, REWARD_AB);
								
								String t = __settotal__(PENALTY_MAZZO_A, count_A, REWARD_AB);
								total.setText(t);					
								totalsArray.add(t);		
								
								clipDrawable.setLevel(__setbarlevel__(money));
								__add_memoria(count, 4);
								__set_effects__(view1, REWARD_AB, PENALTY_MAZZO_A, count_A);
		
								count_A++;
								count++;
//								__set_cards_color(view1, REWARD_AB, PENALTY_MAZZO_A, count_A);				
								numero_carta_selezionata.setText(""+count);
								
								if (inizio == 0) {
									inizio = System.currentTimeMillis();
								}
								IowaActivity.nuovo_test = false;
								
								canRun = "OK";
							}					
						
						
					} // END ACTION_UP
				
				} // END if (canRun == "A" || canRun == "OK")
				else {
					return true;
				}
				
				return false;
			} // END view1.onTouch
			
		});
		
		
		view2.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if (canRun == "B" || canRun == "OK") {
				
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						canRun = "B";
						
						startTimeDown = System.currentTimeMillis();
						
						Log.d(TAG, " ");		
						Log.d(TAG, "___DOWN___B___________");		
						
						pressureDown = event.getPressure();
//						Log.d(TAG, "pressure: " + pressure);

					}
				
					else if (event.getAction() == MotionEvent.ACTION_UP) {
					
						if (count == NUMBER_OF_CARDS) {
							__finish__();
						} 
						
						else {									
//							stopTimeUp = System.currentTimeMillis();
//							calendar = Calendar.getInstance(); 										
//							times.add(""+calendar.get(Calendar.HOUR_OF_DAY)+"_"+calendar.get(Calendar.MINUTE)+"_"
//									+calendar.get(Calendar.SECOND) + "_" +calendar.get(Calendar.MILLISECOND));
//							responsesTime.add(stopTimeUp - startTrial);							
//							pressures.add((pressureDown+event.getPressure())/2);
//							speedTouch.add(stopTimeUp - startTimeDown);
//							startTrial = System.currentTimeMillis();

							saveTimeAndPressure(event);
							
							Log.d(TAG, "___UP_____B__________");	
//							Log.d(TAG, "deck B time: "+(stopTimeUp - startTrial));						
							
							choice.setText("B");						
							deckChoices.add("B");
							
							reward.setText("" + REWARD_AB);						
							rewardsArray.add(REWARD_AB);
							
							String p = __setpenalty__(PENALTY_MAZZO_B, count_B);
							penalty.setText(p);						
							penaltiesArray.add(p);
							
							String g = __setgain__(PENALTY_MAZZO_B, count_B, REWARD_AB);
							gain.setText(g);						
							gainsArray.add(g);
							
							__setcolor__(PENALTY_MAZZO_B, count_B, REWARD_AB);
							
							String t = __settotal__(PENALTY_MAZZO_B, count_B, REWARD_AB);
							total.setText(t);						
							totalsArray.add(t);				
							
							clipDrawable.setLevel(__setbarlevel__(money));
							__add_memoria(count, 3);
							__set_effects__(view2, REWARD_AB, PENALTY_MAZZO_B, count_B);
		
							count_B++;
							count++;
//							__set_cards_color(view2, REWARD_AB, PENALTY_MAZZO_B, count_B);
							numero_carta_selezionata.setText(""+count);
							
							if (inizio == 0) {
								inizio = System.currentTimeMillis();
							}
							IowaActivity.nuovo_test = false;
												
							canRun = "OK";
						}					
					
					} // END ACTION_UP
				
				}  // END if (canRun == "B" || canRun == "OK")
				else {
					return true;
				}
				
				return false;
			}  // END view2.onTouch
			
		});		
		
		
		view3.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if (canRun == "C" || canRun == "OK") {
				
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						canRun = "C";
						
						startTimeDown = System.currentTimeMillis();
						
						Log.d(TAG, " ");		
						Log.d(TAG, "___DOWN_____C___________");

					}
				
					else if (event.getAction() == MotionEvent.ACTION_UP) {
	
						if (count == NUMBER_OF_CARDS) {
							__finish__();
						} 
						else {
																	
//							calendar = Calendar.getInstance();					
//							responsesTime.add(System.currentTimeMillis() - startTrial);
//							times.add(""+calendar.get(Calendar.HOUR_OF_DAY)+"_"+calendar.get(Calendar.MINUTE)+"_"
//									+calendar.get(Calendar.SECOND) + "_" +calendar.get(Calendar.MILLISECOND));

//							startTrial = System.currentTimeMillis();					
							
							saveTimeAndPressure(event);
							
							Log.d(TAG, "___UP_____C__________");	
//							Log.d(TAG, "deck C time: "+(stopTimeUp - startTrial));
							
							choice.setText("C");						
							deckChoices.add("C");
							
							reward.setText("" + REWARD_CD);						
							rewardsArray.add(REWARD_CD);
							
							String p = __setpenalty__(PENALTY_MAZZO_C, count_C);
							penalty.setText(p);						
							penaltiesArray.add(p);
							
							String g = __setgain__(PENALTY_MAZZO_C, count_C, REWARD_CD);
							gain.setText(g);						
							gainsArray.add(g);
							
							__setcolor__(PENALTY_MAZZO_C, count_C, REWARD_CD);
							
							String t = __settotal__(PENALTY_MAZZO_C, count_C, REWARD_CD);
							total.setText(t);						
							totalsArray.add(t);				
							
							clipDrawable.setLevel(__setbarlevel__(money));
							__add_memoria(count, 2);
							__set_effects__(view3, REWARD_CD, PENALTY_MAZZO_C, count_C);
	
							count_C++;
							count++;
//							__set_cards_color(view3, REWARD_CD, PENALTY_MAZZO_C, count_C);
							numero_carta_selezionata.setText(""+count);
							
							if (inizio == 0) {
								inizio = System.currentTimeMillis();
							}
							IowaActivity.nuovo_test = false;
							
							canRun = "OK";
						}					
					
					} // END ACTION_UP
			
					
				}  // END if (canRun == "C" || canRun == "OK")
				else {
					return true;
				}
					
					
				return false;
			}// END view3.onTouch
			
		});			
		
		view4.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {							
				
				if (canRun == "D" || canRun == "OK") {
				
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						canRun = "D";
						
						startTimeDown = System.currentTimeMillis();
						
						Log.d(TAG, " ");		
						Log.d(TAG, "___DOWN___D___________");		

					}				
					
					else if (event.getAction() == MotionEvent.ACTION_UP) {
						
						if (count == NUMBER_OF_CARDS) {
							__finish__();
						} 
						
						else {
												
//							calendar = Calendar.getInstance(); 
//							responsesTime.add(System.currentTimeMillis() - startTrial);
//							times.add(""+calendar.get(Calendar.HOUR_OF_DAY)+"_"+calendar.get(Calendar.MINUTE)+"_"
//									+calendar.get(Calendar.SECOND) + "_" +calendar.get(Calendar.MILLISECOND));
//							Log.d(TAG, "___ UP _____ D __________");	
//							Log.d(TAG, "deck D time: "+(System.currentTimeMillis() - startTrial));
//							startTrial = System.currentTimeMillis();									
							
							saveTimeAndPressure(event);
							
							Log.d(TAG, "___ UP _____ D __________");	
//							Log.d(TAG, "deck D time: "+(stopTimeUp - startTrial));
							
							choice.setText("D");					
							deckChoices.add("D");
							
							reward.setText("" + REWARD_CD);					
							rewardsArray.add(REWARD_CD);
							
							String p = __setpenalty__(PENALTY_MAZZO_D, count_D);
							penalty.setText(p);					
							penaltiesArray.add(p);
							
							String g = __setgain__(PENALTY_MAZZO_D, count_D, REWARD_CD);
							gain.setText(g);					
							gainsArray.add(g);					
							
							__setcolor__(PENALTY_MAZZO_D, count_D, REWARD_CD);
							String t = __settotal__(PENALTY_MAZZO_D, count_D, REWARD_CD);
							total.setText(t);					
							totalsArray.add(t);
							
							clipDrawable.setLevel(__setbarlevel__(money));
							__add_memoria(count, 1);
							__set_effects__(view4, REWARD_CD, PENALTY_MAZZO_D, count_D);
	
							count_D++;
							count++;
//							__set_cards_color(view4, REWARD_CD, PENALTY_MAZZO_D, count_D);
							numero_carta_selezionata.setText(""+count);		
							
							if (inizio == 0) {
								inizio = System.currentTimeMillis();
							}
							IowaActivity.nuovo_test = false;
							
							canRun = "OK";
						}					
						
					} // END ACTION_UP
				
				}   // END if (canRun == "D" || canRun == "OK")
				else {
					return true;
				}
				
				return false;
			}
			
		});		
		
		
		
		
		view1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// questo serve per far cambiare il colore delle carte				
				__set_cards_color(view1, REWARD_AB, PENALTY_MAZZO_A, count_A);		
			}
		});
		view2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// questo serve per far cambiare il colore delle carte
				__set_cards_color(view2, REWARD_AB, PENALTY_MAZZO_B, count_B);
			}
		});
		view3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// questo serve per far cambiare il colore delle carte
				__set_cards_color(view3, REWARD_CD, PENALTY_MAZZO_C, count_C);
			}
		});
		view4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// questo serve per far cambiare il colore delle carte
				__set_cards_color(view4, REWARD_CD, PENALTY_MAZZO_D, count_D);
			}
		});
		
		
		
		Log.d(TAG, "create");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater menu_inflater = getMenuInflater();
		menu_inflater.inflate(R.menu.menu, menu);
		// +++ return true per riattivare il menu
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.reset:
			__reset__();
			return true;

		case R.id.introduzione:
//			Intent intent = new Intent(getApplicationContext(), Spiegazione2.class);
//			startActivity(intent);
			setResult(CHIUDI_VAI_SPIEGAZIONE);
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences prefs = getSharedPreferences(KEY_PREFERENCE,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(KEY_COUNT, count);
		editor.putInt(KEY_COUNT_A, count_A);
		editor.putInt(KEY_COUNT_B, count_B);
		editor.putInt(KEY_COUNT_C, count_C);
		editor.putInt(KEY_COUNT_D, count_D);
		editor.putInt(KEY_MONEY, money);
		editor.putString(KEY_CHOICE_SAVE, choice.getText().toString());
		editor.putString(KEY_REWARD_SAVE, reward.getText().toString());
		editor.putString(KEY_PENALTY_SAVE, penalty.getText().toString());
		editor.putString(KEY_GAIN_SAVE, gain.getText().toString());
		editor.putInt(KEY_COLOR, penalty.getTextColors().getDefaultColor());

		editor.commit();
	}

	//
	// @Override
	// protected void onRestart() {
	// super.onRestart();
	// if (finish){
	// finish();
	// }
	// }

	@Override
	protected void onResume() {
		super.onResume();
//		System.out.println(">>> " + finish);
		if (finish) {
			finish = false;
			finish();
		}
	}

}
