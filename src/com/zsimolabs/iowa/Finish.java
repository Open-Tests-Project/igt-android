package com.zsimolabs.iowa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class Finish extends Activity {
	TextView money;
	TextView congratulations;
	TextView hai_guadagnato;
	static final int REQUEST_FOR_RESULT= 1;
	static boolean finish;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finish);
		
		  if (finish){
			  finish = false;
          	finish();
          }		
		
		money = (TextView)findViewById(R.id.money);
		congratulations = (TextView)findViewById(R.id.congratulations);
		hai_guadagnato = (TextView)findViewById(R.id.hai_guadagnato);
		
		if (Gambling.money_finish>=2500){
			congratulations.setText(R.string.congratulations);
		}
		else{
			congratulations.setText("");
		}
		hai_guadagnato.setText(getText(R.string.hai_guadagnato));
		money.setText("$ "+Gambling.money_finish);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater menu_inflater = getMenuInflater();
		menu_inflater.inflate(R.menu.menu_finish, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.grafico:			
			Intent intent = new Intent(getApplicationContext(), ProfiloCarteSelezionate.class);
			startActivityForResult(intent,  REQUEST_FOR_RESULT);	
			return true;
	
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (android.os.Build.VERSION.SDK_INT >10){
			finish();
		}
	}
	
    @Override
	protected void onRestart() {
		super.onRestart();
//		 if (finish){
//			  finish = false;
//         	finish();
//         }
	}

	@Override
	protected void onResume() {
		super.onResume();
		  if (finish){
			  finish = false;
          	finish();
          }
	}	
}
