package com.zsimolabs.iowa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.achartengine.GraphicalView;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.model.XYValueSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.tools.Pan;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class ProfiloCarteSelezionate extends Activity {

		static final int REQUEST_FOR_RESULT= 1;
        GraphicalView view;
        XYMultipleSeriesRenderer renderer;
        XYMultipleSeriesDataset dataset;
        XYSeries xyseries;
        XYSeriesRenderer r; 
        Pan pan;
        LineChart chart;
        static boolean grafico1_salvato;
        static boolean finish;
  
        
    	private String __tempo_impiegato__() {
    		String tempo = null;
    		long tempo_impiegato = Gambling.fine - Gambling.inizio;
    		long min = (tempo_impiegato / 1000) / 60;
    		long ore = ((tempo_impiegato / 1000) / 60) / 60;
    		if (tempo_impiegato >= 864000000) {
    			tempo = getString(R.string.pi_di_un_giorno);
    		}
    		if (tempo_impiegato < 864000000 && tempo_impiegato >=3600000) {
    			tempo = ""+ore+getString(R.string.ore)+ " "+(tempo_impiegato-(ore*3600000))/60000+ getString(R.string.minuti);
    		}
    		if (tempo_impiegato<3600000) {
    			tempo = " " + min + " " + getString(R.string.minuti);
    		}
    		if (tempo_impiegato < 60000) {
    			tempo = getString(R.string.meno_di_un_minuto);
    		}
    		return tempo;
    	}

        private void __init__(){
        	 renderer = new XYMultipleSeriesRenderer();
        	 dataset = new XYMultipleSeriesDataset();
        	 xyseries = new XYValueSeries(getString(R.string.profilo_delle_carte_selezionate));
        	 r = new XYSeriesRenderer();             
        }
        
        private void __visualizza__(){
        	  renderer.setLabelsTextSize(15);
              renderer.setLegendTextSize(15);
              renderer.setMargins(new int[] { 20, 10, 5, 5 });
              renderer.setZoomButtonsVisible(true);
              renderer.setZoomEnabled(true);          
              renderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
              renderer.setChartTitle(""+IowaActivity.partecipant_name+"  -  "+/*getString(R.string.tempo_impiegato)+*/__tempo_impiegato__());
              renderer.setChartTitleTextSize(20);
              renderer.addSeriesRenderer(r);                
              renderer.setYAxisMin(0);
              renderer.setYAxisMax(4);
              renderer.addYTextLabel(1, "D");
              renderer.addYTextLabel(2, "C");
              renderer.addYTextLabel(3, "B");             
              renderer.addYTextLabel(4, "A");
              renderer.addYTextLabel(0, "");
              renderer.addXTextLabel(10, "10");
              renderer.addXTextLabel(20, "20");
              renderer.addXTextLabel(30, "30");
              renderer.addXTextLabel(40, "40");
              renderer.addXTextLabel(60, "60");
              renderer.addXTextLabel(70, "70");
              renderer.addXTextLabel(80, "80");
              renderer.addXTextLabel(90, "90");
              renderer.setAntialiasing(true);
              renderer.setYLabelsAlign(Align.RIGHT);
              renderer.setXAxisMin(0);
              renderer.setXAxisMax(105);              
              renderer.setAxesColor(Color.MAGENTA);
              renderer.setLabelsColor(Color.WHITE);
              renderer.setPanEnabled(true, true); 
              renderer.setShowCustomTextGrid(true);
              
            
              
              __disponi_grafico(Gambling.memoria);
              dataset.addSeries(xyseries);
              
              
                            
              r.setColor( Color.GREEN);
              r.setLineWidth(2);
              r.setPointStyle( PointStyle.SQUARE);
              r.setFillPoints(true);
              r.setStroke(BasicStroke.SOLID);             
        }
        
        private void __disponi_grafico (ArrayList<Integer> memoria){
        	for (int i = 0; i <memoria.size(); i++){
        		if (i<memoria.size()){
        		xyseries.add(i, memoria.get(i));
        		}
        		else{
        			Toast.makeText(getApplicationContext(), "Errore. Indice più grande delle dimensioni dell'array\nmetodo __disponi_grafico", Toast.LENGTH_LONG).show();
        		}
        	}        
        }
        
        private void __salva_png__ (){
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
        	if (view != null) {
		          view.repaint();
		        }
        	if	(mExternalStorageAvailable&&mExternalStorageWriteable) {
        		
        		TotalNumberCards.createFolder(TotalNumberCards.FOLDER_NAME);
        		
        		Bitmap bitmap = view.toBitmap();
        		File file = new File(Environment.getExternalStorageDirectory(), TotalNumberCards.FOLDER_NAME + "/" + "grafico1" + ".png");  	        	  	         
        		FileOutputStream output = null;
			try {
				output = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}  	        
 	          bitmap.compress(CompressFormat.PNG, 100, output);
 	          grafico1_salvato = true;
        	}
        }
        
        
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
              
                __init__();
                
                __visualizza__();
                
                chart = new LineChart(dataset, renderer);                
                pan = new Pan(chart);            
                view = new GraphicalView(this,chart);              
                
                setContentView(view);         
        
                                     
        }
        
//        @Override
//		protected void onRestart() {
//			super.onRestart();
//			  if (finish){
//              	finish();
//              }
//		}

//		@Override
//		protected void onResume() {
//			super.onResume();
//			  if (finish){
//				  finish = false;
//              	finish();
//              }
//		}
        
        @Override
        public void onBackPressed() {
        super.onBackPressed();
        Finish.finish = false;
        }

		@Override
    	public boolean onCreateOptionsMenu(Menu menu) {
    		super.onCreateOptionsMenu(menu);
    		MenuInflater menu_inflater = getMenuInflater();
    		menu_inflater.inflate(R.menu.menu_primo_grafico, menu);
    		return true;
    	}
		
		
    	
    	@Override
    	public boolean onOptionsItemSelected(MenuItem item) {
    		
    		switch (item.getItemId()) {
    		case R.id.primo_grafico:			
    			Intent intent = new Intent(getApplicationContext(), TotalNumberCards.class);
    			startActivityForResult(intent,  REQUEST_FOR_RESULT);	
    			if (!grafico1_salvato){
    			__salva_png__();
    			//System.out.println(">>> salvato grafico 1");
    			}
    			return true;
    	
    		default:
    			return super.onOptionsItemSelected(item);
    		}
    	}
    	
    	@Override
    	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    		super.onActivityResult(requestCode, resultCode, data);
//    			finish();
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