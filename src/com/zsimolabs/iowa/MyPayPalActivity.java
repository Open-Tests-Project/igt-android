package com.zsimolabs.iowa;

import java.math.BigDecimal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalPayment;
import com.paypal.android.MEP.PayPalResultDelegate;

public class MyPayPalActivity extends Activity implements PayPalResultDelegate{
//	TextView messaggio;
	EditText edit_text;
	CheckoutButton launchSimplePayment;
	static String money = "1";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
//		PayPal pp = PayPal.initWithAppID(this, "APP-80W284485P519543T",PayPal.ENV_SANDBOX);
		PayPal pp = PayPal.initWithAppID(this, "APP-7YV58901KW6750300", PayPal.ENV_LIVE);
        
        LinearLayout layoutSimplePayment = new LinearLayout(this);
        layoutSimplePayment.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        layoutSimplePayment.setOrientation(LinearLayout.VERTICAL);
        layoutSimplePayment.setGravity(Gravity.CENTER);
        TextView messaggio = new TextView(getApplicationContext());
		messaggio.setText(getString(R.string.testo_donazione));
		messaggio.setTextColor(Color.WHITE);
		edit_text = new EditText(getApplicationContext());		
		edit_text.setInputType(InputType.TYPE_NULL);
		
		edit_text.setHint(R.string.donazione);
        if (android.os.Build.VERSION.SDK_INT>=11 
				&& android.os.Build.VERSION.SDK_INT < 14 ){
        	launchSimplePayment = pp.getCheckoutButton(this,PayPal.BUTTON_294x45, CheckoutButton.TEXT_DONATE);
        	launchSimplePayment.setPadding(200, 150, 200, 0);
        	messaggio.setTextSize(30);
        	edit_text.setPadding(20, 20, 20, 0);
        	edit_text.setTextSize(40);
        }
        else{
        launchSimplePayment = pp.getCheckoutButton(this,PayPal.BUTTON_278x43, CheckoutButton.TEXT_DONATE);
//        edit_text.setPadding(0, 10, 0, 0);
        messaggio.setTextSize(18);
        launchSimplePayment.setPadding(10, 40, 10, 0);

        }   
		layoutSimplePayment.addView(messaggio);  
		layoutSimplePayment.addView(edit_text);            
        layoutSimplePayment.addView(launchSimplePayment);        
        setContentView(layoutSimplePayment);
        edit_text.setInputType(InputType.TYPE_CLASS_NUMBER);	
        
        
        edit_text.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				edit_text.setHint("");
//				edit_text.setInputType(InputType.TYPE_CLASS_NUMBER);			
			}
		});
			       
     
        money = "1";
        if (edit_text.getText().toString().length() >0){
			money = edit_text.getText().toString();		
        }
        
        launchSimplePayment.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				PayPalPayment payment = new PayPalPayment();
				try {
					payment.setSubtotal(new BigDecimal(edit_text.getText().toString()));
					payment.setCurrencyType("EUR");
//	                payment.setRecipient("dev_1325507582_biz@gmail.com");
					  payment.setRecipient("sacchisimone@gmail.com");
	                payment.setPaymentType(PayPal.PAYMENT_TYPE_GOODS);
	                Intent checkoutIntent = PayPal.getInstance().checkout(payment, MyPayPalActivity.this);
	                startActivityForResult(checkoutIntent, 1); 	
				} catch (NumberFormatException e) {
					Toast.makeText(getApplicationContext(), getString(R.string.paypal_input_error), Toast.LENGTH_LONG).show();
					finish();
				}
                			
			}
		});
	}
	@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			switch(resultCode) { 
			 case Activity.RESULT_OK: 
			     //Il pagamento è stato effettuato 
//			     String payKey = data.getStringExtra(PayPalActivity.EXTRA_PAY_KEY); 
			     Toast.makeText(getApplicationContext(), getString(R.string.grazie_donazione), Toast.LENGTH_LONG).show();
			     finish();
			     break; 
			 case Activity.RESULT_CANCELED: 
			    // Il pagamento è stato cancellato dall’utente
//				 String errorMessage = data.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
				 Toast.makeText(getApplicationContext(), getString(R.string.donazione_operazione_annullata), Toast.LENGTH_LONG).show();
				 finish();
			    break; 
			 case PayPalActivity.RESULT_FAILURE: 
			    // Il pagamento non è stato effettuato a causa di un errore
//			    String errorID = data.getStringExtra(PayPalActivity.EXTRA_ERROR_ID); 
//			    String errorMessage1 = data.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE); 
			    Toast.makeText(getApplicationContext(), getString(R.string.donazione_operazione_annullata), Toast.LENGTH_LONG).show();
			    finish();
			}
		}
	
	@Override
		protected void onPause() {
			super.onPause();
//			finish();
		}
	
//	questa è l'iinterfaccia per ricevere le notifiche del pagamento, ma non la uso, prefersico usare
//	l'onActicivtyResult
	
	@Override
	public void onPaymentCanceled(String arg0) {
		Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
//		finish();
	}
	@Override
	public void onPaymentFailed(String paymentStatus, String correlationID, String payKey,
			String errorID, String errorMessage) {
		Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
//		Toast.makeText(getApplicationContext(), paymentStatus+"\n"+errorMessage, Toast.LENGTH_LONG).show();
//		finish();
	}
	@Override
	public void onPaymentSucceeded(String arg0, String arg1) {
//		finish();
	}
}
