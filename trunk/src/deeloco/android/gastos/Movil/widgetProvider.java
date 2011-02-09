package deeloco.android.gastos.Movil;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class widgetProvider extends AppWidgetProvider {
	

	Context contexto;
	private static final String PREFERENCIAS_WIDGET="MIS_PREFERENCIAS_WIDGET";
	
	
	
	//private SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy  hh:mm:ss a");
	String strWidgetText = "";
	//public static String MY_WIDGET_UPDATE = "MY_OWN_WIDGET_UPDATE";

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
        try
        {
            context.stopService(new Intent(context, UpdateService.class));//unregisterReceiver(mBI);
        }
        catch(Exception e)
        {
        	Log.d("widgetProvider","Exception onDelete: ",e);
        	}
		//super.onDeleted(context, appWidgetIds);
		//Toast.makeText(context, "onDeleted()", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
        super.onDisabled(context);
        try
        {
            context.stopService(new Intent(context, UpdateService.class));//unregisterReceiver(mBI);
        }
        catch(Exception e)
        {
        	Log.d("widgetProvider","Exception onDisable: ",e);
        	}
		//super.onDisabled(context);
		//Toast.makeText(context, "onDisabled()", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		//super.onEnabled(context);
		//Toast.makeText(context, "onEnabled()", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		//String currentTime = formatter.format(new Date());
		//strWidgetText = strWidgetText + "\n" + currentTime;
		
		context.startService(new Intent(context, UpdateService.class));
		
		//RemoteViews updateView = buildUpdate(context);
	    //appWidgetManager.updateAppWidget(appWidgetIds, updateView);
	    //super.onUpdate(context, appWidgetManager, appWidgetIds);

		//super.onUpdate(context, appWidgetManager, appWidgetIds);
		//Toast.makeText(context, "onUpdate()", Toast.LENGTH_LONG).show();
	}
	
	
	
	
	public static class UpdateService extends Service {
		private tarifas ts=null;
		private tarifa t=null;
		private Franja f=null;
		private static final String PREFERENCIAS_WIDGET="MIS_PREFERENCIAS_WIDGET";
		private static final String PREF_NUMERO="NUMERO_ULTIMA";
		private static final String PREF_DURACION="DURACION_ULTIMA";
		private static final String PREF_FECHA="FECHA_ULTIMA";
		
		@Override
		public IBinder onBind(Intent arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void onCreate() {
			// TODO Auto-generated method stub
			super.onCreate();
		}

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
		}

		@Override
		public void onStart(Intent intent, int startId) {
			
			RemoteViews updateViews = buildUpdate(this);
			AppWidgetManager manager = AppWidgetManager.getInstance(this);
			ComponentName thisWidget = new ComponentName(this, widgetProvider.class);
            manager.updateAppWidget(thisWidget, updateViews);
            
			// TODO Auto-generated method stub
			//super.onStart(intent, startId);
		}
		
		
		   private RemoteViews buildUpdate(Context context) {
			   
		       //RemoteViews updateView = null;
		       ValoresPreferencias vp=new ValoresPreferencias(context);
		       RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget);
				
		       SharedPreferences preferencias = getSharedPreferences(PREFERENCIAS_WIDGET, Context.MODE_PRIVATE);
		       if (preferencias!=null)
		       {
		    	   Log.i("widgetProvider","Preferencias=null");
		    	 //Cursor con el que recorreremos la base de datos de registros de llamadas
		    	   	Cursor c; 
			    	c=context.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, CallLog.Calls.DURATION+">1 and "+CallLog.Calls.TYPE+"="+CallLog.Calls.OUTGOING_TYPE , null, CallLog.Calls.DEFAULT_SORT_ORDER);
			        int iTelefono = c.getColumnIndex(CallLog.Calls.NUMBER);
			        int iFecha = c.getColumnIndex(CallLog.Calls.DATE);
			        int iDuracion = c.getColumnIndex(CallLog.Calls.DURATION);
			        c.moveToFirst();
			        String telefono=c.getString(iTelefono);
					long fecha=c.getLong(iFecha);
					int duracion=c.getInt(iDuracion); //le añadimos la modificación de la duración de la llamada;
			        c.close();
			        String fechaHora=DateFormat.format("dd/MM/yyyy kk:mm:ss",new Date(fecha)).toString();
			        
                    guardarPreferences(PREF_NUMERO,telefono);
                    guardarPreferences(PREF_FECHA, fechaHora);
                    guardarPreferences(PREF_DURACION, duracion);
		    	   
		       }
				
		       	SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCIAS_WIDGET, Context.MODE_PRIVATE);
		       	String telefono = sharedPreferences.getString(PREF_NUMERO, "0");
		       	String fechaHora = sharedPreferences.getString(PREF_FECHA, "0");
		       	int duracion = sharedPreferences.getInt(PREF_DURACION, 0);
		       	int consumoDia = sharedPreferences.getInt("consumoDia", 1);
		        
		        //Hay que ver que tarifa y franja conrresponde a la llamada realizada
		        ts=new tarifas();
		        ts.cargarFranjas();
		        t=ts.getTarifa(telefono,fechaHora);
		        if (t!=null)
		        {
		        	f=t.getFranja(fechaHora);
		        }
		        
		        //Montamos la cadena donde se van a presentar los datos
		        String info = telefono+"|"+FunGlobales.segundosAHoraMinutoSegundo(duracion);
		        if (f!=null)
		        {
		        	info +="|"+FunGlobales.redondear((f.coste(t, duracion)*((vp.getcosteConIVA())?vp.getPreferenciasImpuestos():1)),vp.getPreferenciasDecimales())+FunGlobales.monedaLocal();
		        }
		        //info+=Html.fromHtml(" @ <strong>"+DateFormat.format("kk:mm",new Date()).toString()+"</strong>");
				updateViews.setTextViewText(R.id.widgettext, info);
				
				
				//SEMAFORO
				fechaHora=DateFormat.format("dd/MM/yyyy kk:mm:ss",new Date()).toString();
		        t=ts.getTarifa("00000000",fechaHora);
		        if (t!=null)
		        {
		        	f=t.getFranja(fechaHora);
		        }
		        if ((t.getLimite()>0||t.getLimiteDia()>0))
		        {
		        	if (f.getLimite())
		        	{
		        		updateViews.setTextViewText(R.id.txt_semaforo,Html.fromHtml("<font color='green'>@</font>"));
		        	}
		        	else
		        	{
		        		updateViews.setTextViewText(R.id.txt_semaforo,Html.fromHtml("<font color='red'>@</font>"));
		        	}
		        }
		        else
		        	updateViews.setTextViewText(R.id.txt_semaforo,Html.fromHtml("<font color='white'>@</font>"));

		        
		        updateViews.setTextViewText(R.id.txt_tiempoLimite, "D:"+consumoDia+" @ "+DateFormat.format("kk:mm",new Date()).toString());
		        
		      //Evento onClick en el widget  
		      Intent launchIntent = new Intent(context,gastoMovil.class);
		      PendingIntent intent = PendingIntent.getActivity(context, 0, launchIntent, 0);
		      updateViews.setOnClickPendingIntent(R.id.widget, intent);
		      return updateViews;
		   }
		   
		   
	    	  private void guardarPreferences(String key, String value){
	    		    SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCIAS_WIDGET, Context.MODE_PRIVATE);
	    		    SharedPreferences.Editor editor = sharedPreferences.edit();
	    		    editor.putString(key, value);
	    		    editor.commit();
	    		   }
	    	  
	    	  private void guardarPreferences(String key, int value){
	  		    SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCIAS_WIDGET, Context.MODE_PRIVATE);
	  		    SharedPreferences.Editor editor = sharedPreferences.edit();
	  		    editor.putInt(key, value);
	  		    editor.commit();
	  		   }
	    	  
	    	  private void guardarPreferences(String key, double value){
	  		    SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCIAS_WIDGET, Context.MODE_PRIVATE);
	  		    SharedPreferences.Editor editor = sharedPreferences.edit();
	  		    editor.putFloat(key, new Float(value));
	  		    editor.commit();
	  		   }

		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
