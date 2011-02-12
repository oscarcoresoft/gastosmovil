package deeloco.android.gastos.Movil;

import java.io.ObjectInputStream.GetField;
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
import deeloco.android.gastos.Movil.gastoMovil;

public class widgetProvider extends AppWidgetProvider {
	

	Context contexto;
	private static final String PREFERENCIAS_WIDGET="MIS_PREFERENCIAS_WIDGET";
	private static final String TAG="GastosMovil (widgetProvider)";
	
	
	
	//private SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy  hh:mm:ss a");
	String strWidgetText = "";
	//public static String MY_WIDGET_UPDATE = "MY_OWN_WIDGET_UPDATE";

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
		Log.d(TAG,"onDelete");
        try
        {
            context.stopService(new Intent(context, UpdateService.class));//unregisterReceiver(mBI);
        }
        catch(Exception e)
        {
        	Log.d(TAG,"Exception onDelete: ",e);
        	}
		//super.onDeleted(context, appWidgetIds);
		//Toast.makeText(context, "onDeleted()", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
        super.onDisabled(context);
        Log.d(TAG,"onDisable");
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

			SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCIAS_WIDGET, Context.MODE_PRIVATE);
			if (sharedPreferences.getInt(avisoEstadoTelefono.PREF_LLAMADA, 0)==1)
			{
				//Se ha llamado al servicio despues de terminar una llamada saliente
				//Hay que sumar los segundos de la llamada y el coste de la misma donde corresponda
				Log.d(TAG, "Actualzando valores...");
				actualizarDatos(this);
				
			}
			RemoteViews updateViews = buildUpdate(this);
			AppWidgetManager manager = AppWidgetManager.getInstance(this);
			ComponentName thisWidget = new ComponentName(this, widgetProvider.class);
			

			
            manager.updateAppWidget(thisWidget, updateViews);
            Log.d(TAG,"UpdateService.onDisable");
            
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
		    	 //Cursor con el que recorreremos la base de datos de registros de llamadas para obtener los valores de la última llamada
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
			        
                    guardarPreferences(avisoEstadoTelefono.PREF_NUMERO,telefono);
                    guardarPreferences(avisoEstadoTelefono.PREF_FECHA, fechaHora);
                    guardarPreferences(avisoEstadoTelefono.PREF_DURACION, duracion);
		    	   
		       }
		       
		       
				
		       	SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCIAS_WIDGET, Context.MODE_PRIVATE);
		       	String telefono = sharedPreferences.getString(avisoEstadoTelefono.PREF_NUMERO, "0");
		       	String fechaHora = sharedPreferences.getString(avisoEstadoTelefono.PREF_FECHA, "0");
		       	int duracion = sharedPreferences.getInt(avisoEstadoTelefono.PREF_DURACION, 0);
		       	int consumoDia = sharedPreferences.getInt("consumoDia", 1);
		       	//Datos resumen del me
		       	double costeLlamadas=Double.parseDouble(sharedPreferences.getString(gastoMovil.PREF_COSTE, "0"));
		       	int segConsumidosMes=sharedPreferences.getInt(gastoMovil.PREF_SEGUNDOS, 0);

		       	
		        
		       	double iva=(vp.getcosteConIVA())?vp.getPreferenciasImpuestos():1;
		       	
		        //Hay que ver que tarifa y franja conrresponde a la llamada realizada
		        ts=new tarifas();
		        ts.cargarFranjas();
		        t=ts.getTarifa(telefono,fechaHora);
		        if (t!=null)
		        {
		        	f=t.getFranja(fechaHora);
		        }
		        
		        //Montamos la cadena donde se van a presentar los datos de la última llamada
		        String info = telefono+"|"+FunGlobales.segundosAHoraMinutoSegundo(duracion);
		        if (f!=null)
		        {
		        	info +="|"+FunGlobales.redondear((f.coste(t, duracion)*(iva)),vp.getPreferenciasDecimales())+FunGlobales.monedaLocal();
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

		        //Datos resumen del mes
		        updateViews.setTextViewText(R.id.txt_costeLlamadas,"M-"+ FunGlobales.redondear(costeLlamadas*iva,vp.getPreferenciasDecimales())+FunGlobales.monedaLocal());
		        updateViews.setTextViewText(R.id.txt_tiempo, "M-"+FunGlobales.segundosAHoraMinutoSegundo(segConsumidosMes)+"D-"+FunGlobales.segundosAHoraMinutoSegundo(consumoDia));
		        
		        //Datos de las tarifas
		        String datosTarifas="";
		        for (int i=1;i<ts.ultimoId();i++)
		        {
		        	if (sharedPreferences.getInt(gastoMovil.PREF_TS_SEG_LIMITE_MES+i, -1)!=-1)
		        	{
		        	//Hay valor en preferencias
		        		int seg_consumidos_mes=sharedPreferences.getInt(gastoMovil.PREF_TS_SEG_CONSUMIDOS_MES+i,0);
		        		int seg_consumidos_dia=sharedPreferences.getInt(gastoMovil.PREF_TS_SEG_CONSUMIDOS_DIA+i,0);
		        		int seg_consumidos_limite_mes=sharedPreferences.getInt(gastoMovil.PREF_TS_SEG_CONSUMIDOS_LIMITE_MES+i,0);
		        		int seg_consumidos_limite_dia=sharedPreferences.getInt(gastoMovil.PREF_TS_SEG_CONSUMIDOS_LIMITE_DIA+i,0);
		        		int seg_limite_mes=sharedPreferences.getInt(gastoMovil.PREF_TS_SEG_LIMITE_MES+i,0);
		        		int seg_limite_dia=sharedPreferences.getInt(gastoMovil.PREF_TS_SEG_LIMITE_DIA+i,0);
		        		
		        		if (seg_limite_mes>0)
		        		{
		        			//Tiene limite mensual
		        			datosTarifas+="{T"+i+"}"+FunGlobales.segundosAMinutoSegundo(seg_consumidos_limite_mes)+"(L."+seg_limite_mes+") @";
		        		}
		        		else
		        		{
		        			datosTarifas+="{T"+i+"}"+FunGlobales.segundosAMinutoSegundo(seg_consumidos_mes)+" @";
		        		}
		        		
		        	}
		        }
		        
		        updateViews.setTextViewText(R.id.txt_tiempoLimite, datosTarifas+DateFormat.format("kk:mm",new Date()).toString());
		        
		      //Evento onClick en el widget  
		      Intent launchIntent = new Intent(context,gastoMovil.class);
		      PendingIntent intent = PendingIntent.getActivity(context, 0, launchIntent, 0);
		      updateViews.setOnClickPendingIntent(R.id.widget, intent);
		      return updateViews;
		   }
		   
		   
		   private void actualizarDatos (Context context)
		   {
			   SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCIAS_WIDGET, Context.MODE_PRIVATE);
			   ValoresPreferencias vp=new ValoresPreferencias(context);
			   //Valores de la última llamada
			   String telefono = sharedPreferences.getString(avisoEstadoTelefono.PREF_NUMERO, "0");
			   String fechaHora = sharedPreferences.getString(avisoEstadoTelefono.PREF_FECHA, "0");
			   int duracion = sharedPreferences.getInt(avisoEstadoTelefono.PREF_DURACION, 0);
			 //Hay que ver que tarifa y franja conrresponde a la llamada realizada
		        ts=new tarifas();
		        ts.cargarFranjas();
		        t=ts.getTarifa(telefono,fechaHora);
		        if (t!=null)
		        {
		        	f=t.getFranja(fechaHora);
		        }

		        double coste=f.coste(t, duracion)*((vp.getcosteConIVA())?vp.getPreferenciasImpuestos():1);
		        
		       	Log.d(TAG,"Actualizar datos de la tarifa "+t.getIdentificador());
		       //Valores que hay que actualizar	de tiempo
		        int consumoDia = sharedPreferences.getInt("consumoDia", 1)+duracion;
			   	int segConsumidosMes=sharedPreferences.getInt(gastoMovil.PREF_SEGUNDOS, 0)+duracion;
			   	Log.d(TAG,"seg_consumidos_mes "+sharedPreferences.getInt(gastoMovil.PREF_TS_SEG_CONSUMIDOS_MES+t.getIdentificador(),0));
   				int seg_consumidos_mes=sharedPreferences.getInt(gastoMovil.PREF_TS_SEG_CONSUMIDOS_MES+t.getIdentificador(),0)+duracion;
				int seg_consumidos_dia=sharedPreferences.getInt(gastoMovil.PREF_TS_SEG_CONSUMIDOS_DIA+t.getIdentificador(),0)+duracion;
				int seg_consumidos_limite_mes=sharedPreferences.getInt(gastoMovil.PREF_TS_SEG_CONSUMIDOS_LIMITE_MES+t.getIdentificador(),0);
				int seg_consumidos_limite_dia=sharedPreferences.getInt(gastoMovil.PREF_TS_SEG_CONSUMIDOS_LIMITE_DIA+t.getIdentificador(),0);
				Log.d(TAG,"seg_consumidos_mes actualizado "+seg_consumidos_mes);
				//seg_consumidos_dia=+duracion;
				//seg_consumidos_mes=+duracion;
				

			   if (t.getLimite()>0)
			   {
				   //Tiene límite mensual
				   if (f.getLimiteSiNo().equals("Si"))
				   {
					   seg_consumidos_limite_mes=+duracion;
				   }
			   }
			   if (t.getLimiteDia()>0)
			   {
				   //Tiene límite diario
				   if (f.getLimiteSiNo().equals("Si"))
				   {
					   seg_consumidos_limite_dia=+duracion;
				   }
			   }
			   
			   //Valores que hay que actualizar de coste
			   double costeLlamadas=Double.parseDouble(sharedPreferences.getString(gastoMovil.PREF_COSTE, "0"))+coste;
			   
			   //Ya estan los valores actualizados, guardarlo en preferencias.
			   guardarPreferences("consumoDia", consumoDia);
			   guardarPreferences(gastoMovil.PREF_SEGUNDOS, segConsumidosMes);
			   guardarPreferences(gastoMovil.PREF_TS_SEG_CONSUMIDOS_MES+t.getIdentificador(), seg_consumidos_mes);
			   guardarPreferences(gastoMovil.PREF_TS_SEG_CONSUMIDOS_DIA+t.getIdentificador(), seg_consumidos_dia);
			   guardarPreferences(gastoMovil.PREF_TS_SEG_CONSUMIDOS_LIMITE_MES+t.getIdentificador(), seg_consumidos_limite_mes);
			   guardarPreferences(gastoMovil.PREF_TS_SEG_CONSUMIDOS_LIMITE_DIA+t.getIdentificador(), seg_consumidos_limite_dia);
			   guardarPreferences(gastoMovil.PREF_COSTE, ""+FunGlobales.redondear(costeLlamadas,2));
			   guardarPreferences(avisoEstadoTelefono.PREF_LLAMADA, 0);
			   
			   
		   }
		   
		  /**
		   * Guarda un valor  
		   * @param key
		   * @param value
		   */
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

		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
