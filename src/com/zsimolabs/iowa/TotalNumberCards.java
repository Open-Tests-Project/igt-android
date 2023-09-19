package com.zsimolabs.iowa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.model.XYValueSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.tools.Pan;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.PngImage;
//import java.awt.Image;
//import java.awt.Toolkit;
//import java.awt.image.PixelGrabber;

public class TotalNumberCards extends Activity {

	GraphicalView view;
	XYMultipleSeriesRenderer renderer;
	XYMultipleSeriesDataset dataset;
	XYSeries xyseries;
	XYSeriesRenderer r;
	XYSeriesRenderer r2;
	Pan pan;
	BarChart bar_chart;
	boolean salvato = false;
	static boolean grafico2_salvato;
	static final int REQUEST_FOR_RESULT = 1;
	static final String FOLDER_NAME = "iowa";
	private static final String TAG = "iowa";
	SharedPreferences shared_preference;
	static boolean recordThePressure;

	private void __init__() {
		renderer = new XYMultipleSeriesRenderer();
		dataset = new XYMultipleSeriesDataset();
		xyseries = new XYValueSeries(getString(R.string.total_number_of_cards));
		r = new XYSeriesRenderer();
		r2 = new XYSeriesRenderer();
		
		shared_preference = PreferenceManager.getDefaultSharedPreferences(this);  
		recordThePressure = shared_preference.getBoolean(getString(R.string.key_pressure_checkbox), false);
		Log.d(TAG, "record the pressure: "+recordThePressure);
	}
	
	static void createFolder(String folderName) {
		
		File folder = new File(Environment.getExternalStorageDirectory() + "/" + folderName);
		boolean success = true;
		if (!folder.exists()) {
		    success = folder.mkdir();
		}
		if (success) {
		    // Do something on success
		} else {
		    // Do something else on failure 
		}
		
	}

	private void __visualizza__() {
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setMargins(new int[] { 25, 20, 5, 5 });
		renderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		renderer.addSeriesRenderer(r);
		renderer.setChartTitle("" + IowaActivity.partecipant_name + "  -  "
				+ __tempo_impiegato__());
		renderer.setChartTitleTextSize(20);
		renderer.setShowCustomTextGrid(true);
		renderer.setShowLabels(true);

		renderer.setYAxisMin(0);
		renderer.setYAxisMax(105);
		renderer.addYTextLabel(0, "0");
		renderer.addYTextLabel(10, "10");
		renderer.addYTextLabel(20, "20");
		renderer.addYTextLabel(30, "30");
		renderer.addYTextLabel(40, "40");
		renderer.addYTextLabel(50, "50");
		renderer.addYTextLabel(60, "60");
		renderer.addYTextLabel(70, "70");
		renderer.addYTextLabel(80, "80");
		renderer.addYTextLabel(90, "90");

		renderer.setXLabels(0);
		renderer.setXAxisMin(0);
		renderer.setXAxisMax(5);
		renderer.setBarSpacing(0.5);
		renderer.addXTextLabel(0, "");
		renderer.addXTextLabel(1, "A");
		renderer.addXTextLabel(2, "B");
		renderer.addXTextLabel(3, "C");
		renderer.addXTextLabel(4, "D");
		renderer.addXTextLabel(5, " ");
		renderer.setAntialiasing(true);
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setAxesColor(Color.MAGENTA);
		renderer.setLabelsColor(Color.WHITE);
		renderer.setPanEnabled(true, true);

		xyseries.add(1, Gambling.count_A);
		xyseries.add(2, Gambling.count_B);
		xyseries.add(3, Gambling.count_C);
		xyseries.add(4, Gambling.count_D);

		dataset.addSeries(xyseries);

		r.setColor(Color.GREEN);
		r.setDisplayChartValues(true);
		r.setChartValuesTextSize(16);
	}

	private String __tempo_impiegato__() {
		String tempo = null;
		long tempo_impiegato = Gambling.fine - Gambling.inizio;
		long min = (tempo_impiegato / 1000) / 60;
		long ore = ((tempo_impiegato / 1000) / 60) / 60;
		if (tempo_impiegato >= 864000000) {
			tempo = getString(R.string.pi_di_un_giorno);
		}
		if (tempo_impiegato < 864000000 && tempo_impiegato >= 3600000) {
			tempo = "" + ore + getString(R.string.ore) + " "
					+ (tempo_impiegato - (ore * 3600000)) / 60000
					+ getString(R.string.minuti);
		}
		if (tempo_impiegato < 3600000) {
			tempo = " " + min + " " + getString(R.string.minuti);
		}
		if (tempo_impiegato < 60000) {
			tempo = getString(R.string.meno_di_un_minuto);
		}
		return tempo;
	}
	
	private boolean externalStorageAvaible() {

		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		
		String state = Environment.getExternalStorageState();
		
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			mExternalStorageAvailable = mExternalStorageWriteable = false;
			Toast.makeText(getApplicationContext(), getString(R.string.error_no_sd), Toast.LENGTH_LONG).show();
		}
		
		return mExternalStorageAvailable && mExternalStorageWriteable;
		
	}
	

	private void __salva_png__() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			mExternalStorageAvailable = mExternalStorageWriteable = false;
			Toast.makeText(getApplicationContext(),
					getString(R.string.error_no_sd), Toast.LENGTH_LONG).show();
		}
		if (view != null) {
			view.repaint();
		}
		if (mExternalStorageAvailable && mExternalStorageWriteable) {
			
			createFolder(FOLDER_NAME);
			
			Bitmap bitmap = view.toBitmap();
			File file = new File(Environment.getExternalStorageDirectory(),
					FOLDER_NAME + "/" + "grafico2" + ".png");
			FileOutputStream output = null;
			try {
				output = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			bitmap.compress(CompressFormat.PNG, 100, output);
			grafico2_salvato = true;
		}
	}
	
	
	private void savePdf(long delay) {
		
	    new Handler().postDelayed(new Runnable() {
	        public void run() {                
	        	
	    		__salva_png__();
	    		
	    		System.out.println(">>> salvato grafico 2");
	    		
	    		try {

	    			File filepdf = null;
	    			
	    			//Calendar calendar_pdf = Calendar.getInstance(); 
//	    			String now_file_pdf_label = ""+calendar_pdf.get(Calendar.YEAR) + "-" 
//	    									+ calendar_pdf.get(Calendar.MONTH) + "-" 
//	    									+ calendar_pdf.get(Calendar.DAY_OF_MONTH)+	"_" 
//	    									+ calendar_pdf.get(Calendar.HOUR_OF_DAY)+ "-" 
//	    									+ calendar_pdf.get(Calendar.MINUTE);
	    			
	    			String now_file_pdf_label = IowaActivity.starting_date;
	    			
	    			if (android.os.Build.VERSION.SDK_INT >= 8) {

	    				if (IowaActivity.partecipant_name.length() >= 2) {
	    					filepdf = new File(
	    							Environment.getExternalStorageDirectory(),
	    							FOLDER_NAME +"/" + "iowa" + "_" 
	    									+ IowaActivity.partecipant_name.charAt(0)
	    									+ IowaActivity.partecipant_name.charAt(1) 
	    									+ "_" + now_file_pdf_label + ".pdf");// il nome del pdf con le prime 2 lettere del nome
	    				}
	    				else{
	    					
	    						filepdf = new File(
	    								Environment.getExternalStorageDirectory(),
	    								FOLDER_NAME + "/" + "iowa" + "_" + now_file_pdf_label + ".pdf");
	    				}

	    			} else {
	    				System.out.println(">>> apilevel "
	    						+ android.os.Build.VERSION.SDK_INT);
	    				Toast.makeText(getApplicationContext(),
	    						R.string.api_level_nopdf, Toast.LENGTH_LONG).show();
	    			}
	    			FileOutputStream output2 = new FileOutputStream(filepdf);
	    			Document document = new Document();
	    			PdfWriter.getInstance(document, output2);
	    			document.open();
	    			Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 24,
	    					Font.BOLD);
	    			Font catFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 20,
	    					Font.NORMAL);
	    			Paragraph titolo = new Paragraph(getString(R.string.app_name),
	    					catFont);

//	    			Phrase data = new Phrase("\n"
//	    					+ Calendar.getInstance().getTime().toString(), catFont1);
	    			
//TODO     
	    			Phrase data = new Phrase("\n"
	    					+ IowaActivity.date_for_pdf_file_content, catFont1);
	    			titolo.add(data);
	    			Phrase nome = new Phrase("\n"+getString(R.string.nome)+": "
	    					+ IowaActivity.partecipant_name, catFont1);
	    			titolo.add(nome);
	    			Phrase tempo = new Phrase("\n "+getString(R.string.tempo)+": " + __tempo_impiegato__(),
	    					catFont1);
	    			titolo.add(tempo);
	    			Phrase risultato = new Phrase("\n"+getString(R.string.score)+": "
	    					+ Gambling.money_finish, catFont1);
	    			titolo.add(risultato);
	    			Phrase solospazio = new Phrase("\n\n\n");
	    			titolo.add(solospazio);
	    			document.add(titolo);
	    			Image png1 = PngImage.getImage("mnt/sdcard/" + FOLDER_NAME + "/grafico1.png");
//	    			png1.scaleToFit(550, 550);				
	    			png1.setAlignment(Image.ALIGN_CENTER);
	    			png1.scaleAbsolute(550, 400);
//	    			png1.setDpi(1024, 1024);
	    			document.add(png1);
	    			Image png2 = PngImage.getImage("mnt/sdcard/" + FOLDER_NAME + "/grafico2.png");
//	    			png2.scaleToFit(550, 550);
	    			png2.scaleAbsolute(550, 400);
	    			png2.setAlignment(Image.ALIGN_CENTER);
	    			document.add(png2);
	    			document.close();
	    			
//	    			progress.dismiss();	
	    											
	    			salvato = true;
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}			    			        		    
	        	
	        }
	    }, delay); 
		
	}
	
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, BigDecimal.ROUND_HALF_UP);
	    return bd.doubleValue();
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		__init__();

		__visualizza__();

		bar_chart = new BarChart(dataset, renderer, BarChart.Type.STACKED);
		pan = new Pan(bar_chart);
		view = new GraphicalView(this, bar_chart);
		setContentView(view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater menu_inflater = getMenuInflater();
		menu_inflater.inflate(R.menu.menu_secondo_grafico, menu);
		return true;
	}
	
	  @Override
      public void onBackPressed() {
      super.onBackPressed();
      Finish.finish = false;
      }
	  
	  public void timerDelayRemoveDialog(long time, final Dialog d){
		    new Handler().postDelayed(new Runnable() {
		        public void run() {                
		            d.dismiss();
		            Toast.makeText(getApplicationContext(), R.string.salvato,
							Toast.LENGTH_SHORT).show();
		        }
		    }, time); 
		}
	  
	  

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		ProgressDialog progress;
		
		switch (item.getItemId()) {
		case R.id.secondo_grafico:
			Intent intent = new Intent(getApplicationContext(),
					ProfiloCarteSelezionate.class);
			startActivity(intent);
			return true;

		case R.id.salva2:
			progress = new ProgressDialog(this);
			progress.setMessage("Saving Pdf...");
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.show();
			timerDelayRemoveDialog(3000, progress);
			
			closeOptionsMenu();
			
			savePdf(4000);
			
//			__salva_png__();
//			
//			System.out.println(">>> salvato grafico 2");
//			try {
//
//				File filepdf = null;
//				
//				Calendar calendar_pdf = Calendar.getInstance(); 
//				String now_file_pdf_label = ""+calendar_pdf.get(Calendar.YEAR) + "-" 
//										+ calendar_pdf.get(Calendar.MONTH) + "-" 
//										+ calendar_pdf.get(Calendar.DAY_OF_MONTH)+	"_" 
//										+ calendar_pdf.get(Calendar.HOUR_OF_DAY)+ "-" 
//										+ calendar_pdf.get(Calendar.MINUTE);
//				
//				if (android.os.Build.VERSION.SDK_INT >= 8) {
//					
////					Calendar c1 = Calendar.getInstance(); 
////					String now1 = ""+c1.get(Calendar.YEAR) + "-" + c1.get(Calendar.MONTH) + "-" + c1.get(Calendar.DAY_OF_MONTH);
//
//					if (IowaActivity.partecipant_name.length() >= 2) {
//						filepdf = new File(
//								Environment.getExternalStorageDirectory(),
//								FOLDER_NAME +"/" + "iowa" + "_" 
//										+ IowaActivity.partecipant_name.charAt(0)
//										+ IowaActivity.partecipant_name.charAt(1) 
//										+ "_" + now_file_pdf_label + ".pdf");// il nome del pdf con le prime 2 lettere del nome
//					}
//					else{
////						Calendar c2 = Calendar.getInstance(); 
////						String now2 = ""+c2.get(Calendar.YEAR) + "-" + c2.get(Calendar.MONTH) + "-" + c2.get(Calendar.DAY_OF_MONTH);
//						
//							filepdf = new File(
//									Environment.getExternalStorageDirectory(),
//									FOLDER_NAME + "/" + "iowa" + "_" + now_file_pdf_label + ".pdf");
//					}
//
//				} else {
//					System.out.println(">>> apilevel "
//							+ android.os.Build.VERSION.SDK_INT);
//					Toast.makeText(getApplicationContext(),
//							R.string.api_level_nopdf, Toast.LENGTH_LONG).show();
//				}
//				FileOutputStream output2 = new FileOutputStream(filepdf);
//				Document document = new Document();
//				PdfWriter.getInstance(document, output2);
//				document.open();
//				Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 24,
//						Font.BOLD);
//				Font catFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 20,
//						Font.NORMAL);
//				Paragraph titolo = new Paragraph(getString(R.string.app_name),
//						catFont);
//
//				Phrase data = new Phrase("\n"
//						+ Calendar.getInstance().getTime().toString(), catFont1);
//				titolo.add(data);
//				Phrase nome = new Phrase("\n"+getString(R.string.nome)+": "
//						+ IowaActivity.partecipant_name, catFont1);
//				titolo.add(nome);
//				Phrase tempo = new Phrase("\n "+getString(R.string.tempo)+": " + __tempo_impiegato__(),
//						catFont1);
//				titolo.add(tempo);
//				Phrase risultato = new Phrase("\n"+getString(R.string.score)+": "
//						+ Gambling.money_finish, catFont1);
//				titolo.add(risultato);
//				Phrase solospazio = new Phrase("\n\n\n");
//				titolo.add(solospazio);
//				document.add(titolo);
//				Image png1 = PngImage.getImage("mnt/sdcard/" + FOLDER_NAME + "/grafico1.png");
////				png1.scaleToFit(550, 550);				
//				png1.setAlignment(Image.ALIGN_CENTER);
//				png1.scaleAbsolute(550, 400);
////				png1.setDpi(1024, 1024);
//				document.add(png1);
//				Image png2 = PngImage.getImage("mnt/sdcard/" + FOLDER_NAME + "/grafico2.png");
////				png2.scaleToFit(550, 550);
//				png2.scaleAbsolute(550, 400);
//				png2.setAlignment(Image.ALIGN_CENTER);
//				document.add(png2);
//				document.close();
//				
////				progress.dismiss();	
//												
//				salvato = true;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
			
			return true;

		case R.id.secondo_grafico_exit:
			if (salvato && android.os.Build.VERSION.SDK_INT >= 8) {
				File file1 = new File(
						Environment.getExternalStorageDirectory(),
						FOLDER_NAME + "/" + "grafico1.png");
				file1.delete(); 
				File file2 = new File(
						Environment.getExternalStorageDirectory(),
						FOLDER_NAME + "/" + "grafico2.png");
				file2.delete();
			}
			ProfiloCarteSelezionate.finish = true;
			Finish.finish = true;
			Gambling.finish = true;
			Spiegazione2.finish = true;
			IowaActivity.finish = true;
			setResult(Activity.RESULT_OK);
			IowaActivity.finito = true;
			finish();
			
			return true;
			
		case R.id.salva_css:
//			Log.v("TotalNumberCardsActivity", "memoria: "+ Gambling.memoria);
			
			progress = new ProgressDialog(this);
			progress.setMessage("Saving Csv...");
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.show();
			timerDelayRemoveDialog(2500, progress);
			
			boolean sdCardAvaible =  externalStorageAvaible();
			
			
			if (sdCardAvaible) {
//				progress = new ProgressDialog(this);
//				progress.setTitle("Save csv");
//				progress.setMessage("Saving csv...");
//				progress.show();
				
				createFolder(FOLDER_NAME);
				
				String csvFileName;
				Calendar calendar_css = Calendar.getInstance(); 
//				String now_file_csv_label = ""+calendar_css.get(Calendar.YEAR) + "-" 
//										+ calendar_css.get(Calendar.MONTH) + "-" 
//										+ calendar_css.get(Calendar.DAY_OF_MONTH)+	"_" 
//										+ calendar_css.get(Calendar.HOUR_OF_DAY)+ "-" 
//										+ calendar_css.get(Calendar.MINUTE);
				
				String now_file_csv_label = IowaActivity.starting_date;
				
				String now_css = ""+calendar_css.get(Calendar.YEAR) + "_" 
												+ (calendar_css.get(Calendar.MONTH)+1) + "_" 
												+ calendar_css.get(Calendar.DAY_OF_MONTH);
				
				if (IowaActivity.partecipant_name.length() >= 2) {
					csvFileName =  "iowa" + "_"
									+ IowaActivity.partecipant_name.charAt(0)
									+ IowaActivity.partecipant_name.charAt(1)
									+ "_" + now_file_csv_label
									+ ".csv";				
					}
				else{
					csvFileName = "iowa" + "_" 
											 + now_file_csv_label
											 + ".csv";
				}
			
//				String content = "name,trial,deck,reward,penalty,gain,total,hh:mm:ss,date\n";
				
				String pressureHeaderCsvText = "";
				String pressureStyleHeaderCsvText = "";
				if (recordThePressure) { 
					pressureHeaderCsvText = getResources().getText(R.string.css_pressure)+",";
					pressureStyleHeaderCsvText = getResources().getText(R.string.css_pressure_style)+",";
				}
				
				String content = "" + getResources().getText(R.string.css_name) + ","
						                      + getResources().getText(R.string.trial) + ","
						                      + getResources().getText(R.string.deck) + ","
						                      + getResources().getText(R.string.css_reward) + ","
						                      + getResources().getText(R.string.css_penalty) + ","
						                      + getResources().getText(R.string.css_gain) + ","
						                      + getResources().getText(R.string.css_total) + ","
						                      + getResources().getText(R.string.css_response_time) + ","
						                      + pressureHeaderCsvText
						                      + pressureStyleHeaderCsvText
						                      + getResources().getText(R.string.css_touch_speed) + ","
						                      + getResources().getText(R.string.css_hh_mm_ss) + ","
						                      + getResources().getText(R.string.css_date) + "\n"; 
				
		 
				
				// +++ questo non serve piu'
//				if (recordThePressure) { 
//					while (Gambling.pressures.size() < 100) {
//						Gambling.pressures.add((float)0.0);
//					}
//				}
			
			   	for (int i = 0; i < Gambling.rewardsArray.size(); i++) {
			   		
			   		String pressureValue = "";
			   		String pressureStyleValue = "";
			   		if (recordThePressure) { 
			   			pressureValue = "" + round(Gambling.pressures.get(i), 3) + "," ;
			   			pressureStyleValue = Gambling.pressureStyle.get(i) + ",";
			   		}
			   		
				   		content += IowaActivity.partecipant_name + "," 
		   						+ (i+1) + "," 
		   						+ Gambling.deckChoices.get(i) + "," 
		   						+ Gambling.rewardsArray.get(i) + "," 
		   						+ Gambling.penaltiesArray.get(i) + "," 
		   						+ Gambling.gainsArray.get(i) + "," 
		   						+ Gambling.totalsArray.get(i) + "," 
		   						+ Gambling.responsesTime.get(i) + "," 
		   						+ pressureValue 
		   						+ pressureStyleValue
		   						+ Gambling.speedTouch.get(i) + "," 
		   						+ Gambling.times.get(i) + ","
		   						+ now_css + "\n";

	        	}
				
				File file = new File(Environment.getExternalStorageDirectory(),  FOLDER_NAME + "/" + csvFileName);
				
				FileOutputStream outputStream;
				try {
//					  outputStream = openFileOutput(csvFile, Context.MODE_PRIVATE);
					  outputStream = new FileOutputStream(file);
					  outputStream.write(content.getBytes());
					  outputStream.close();
					  
//					  Toast.makeText(getApplicationContext(), R.string.salvato,
//								Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
					  e.printStackTrace();
					}
				
				
//				progress.dismiss();	
			}
			
			
			return true;
			
			
			
		default:
			return super.onOptionsItemSelected(item);
		}
		
	
	}

}