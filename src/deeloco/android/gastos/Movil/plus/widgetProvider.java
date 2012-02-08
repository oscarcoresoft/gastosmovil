package deeloco.android.gastos.Movil.plus;

import java.util.Date;

import deeloco.android.gastos.Movil.plus.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CallLog;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;
import android.content.SharedPreferences;
import android.database.Cursor;

public class widgetProvider extends AppWidgetProvider {
	

	Context contexto;
	private tarifas ts=null;
	private tarifa t=null;
	private Franja f=null;
	static final String PREFERENCIAS_WIDGET="MIS_PREFERENCIAS_WIDGET";
	static final String PREF_NUMERO="NUMERO_ULTIMA";
	
	
	
	//private SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy  hh:mm:ss a");
	String strWidgetText = "";
	//public static String MY_WIDGET_UPDATE = "MY_OWN_WIDGET_UPDATE";

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		/*super.onDeleted(context, appWidgetIds);
		Log.d(TAG,"onDelete");
        try
        {
            context.stopService(new Intent(context, UpdateService.class));//unregisterReceiver(mBI);
        }
        catch(Exception e)
        {
        	Log.d(TAG,"Exception onDelete: ",e);
        	}*/
		//super.onDeleted(context, appWidgetIds);
		//Toast.makeText(context, "onDeleted()", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
        /*super.onDisabled(context);
        Log.d(TAG,"onDisable");
        try
        {
            context.stopService(new Intent(context, UpdateService.class));//unregisterReceiver(mBI);
        }
        catch(Exception e)
        {
        	Log.d("widgetProvider","Exception onDisable: ",e);
        	}*/
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
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		//String currentTime = formatter.format(new Date());
		//strWidgetText = strWidgetText + "\n" + currentTime;
		
		//context.startService(new Intent(context, UpdateService.class));
		contexto=context;
		RemoteViews updateView = buildUpdate(context);
	    appWidgetManager.updateAppWidget(appWidgetIds, updateView);
	    //super.onUpdate(context, appWidgetManager, appWidgetIds);

		//super.onUpdate(context, appWidgetManager, appWidgetIds);
		//Toast.makeText(context, "onUpdate()", Toast.LENGTH_LONG).show();
		
		//RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		//SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCIAS_WIDGET, Context.MODE_PRIVATE);
		//String telefono = sharedPreferences.getString(avisoEstadoTelefono.PREF_NUMERO, "0");
		

		
		//updateViews.getTextViewText()
		//String number = "tel:" + telefono.trim();
        //Intent launchIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(number));
		
		//Intent launchIntent = new Intent(Intent.ACTION_DIAL);
		//startActivity(intent);
		
		//Intent launchIntent = new Intent(context,gastoMovil.class);
    	//PendingIntent pIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
    	//updateViews.setOnClickPendingIntent(R.id.widget, pIntent);
    	//appWidgetManager.updateAppWidget(appWidgetIds, updateViews);
	}
	
	
	
	
	
	 /**
    * Función que se encarga de actualizar los elementos del interface del widget
    * @param context
    * @return RemoteView
    * 	Vista remota con todos los elementos del widget actualizados.
    */
	   private RemoteViews buildUpdate(Context context) 
	   {
		   
	       //RemoteViews updateView = null;
	       ValoresPreferencias vp=new ValoresPreferencias(context);
	       RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget);
	       int totalRegistros=1;
	       String telefono;
	       String fechaHora;
	       int duracion;
	       String nombre="";
	       SharedPreferences preferencias = context.getSharedPreferences(PREFERENCIAS_WIDGET, Context.MODE_PRIVATE);
	       if (preferencias!=null)
	       {
	    	   Log.i("widgetProvider","Preferencias=null");
	    	 //Cursor con el que recorreremos la base de datos de registros de llamadas para obtener los valores de la última llamada
	    	   	Cursor c; 
		    	c=context.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, CallLog.Calls.DURATION+">1 and "+CallLog.Calls.TYPE+"="+CallLog.Calls.OUTGOING_TYPE , null, CallLog.Calls.DEFAULT_SORT_ORDER);
		        int iTelefono = c.getColumnIndex(CallLog.Calls.NUMBER);
		        int iFecha = c.getColumnIndex(CallLog.Calls.DATE);
		        int iDuracion = c.getColumnIndex(CallLog.Calls.DURATION);
		        int iNombre = c.getColumnIndex(CallLog.Calls.CACHED_NAME);
		        
		        c.moveToFirst();
		        totalRegistros=c.getCount();
		        if (totalRegistros>0)
		        {
			        telefono=FunGlobales.quitarPrePais(c.getString(iTelefono));
					long fecha=c.getLong(iFecha);
					duracion=c.getInt(iDuracion); //le añadimos la modificación de la duración de la llamada;
					nombre=c.getString(iNombre);
			        fechaHora=DateFormat.format("dd/MM/yyyy kk:mm:ss",new Date(fecha)).toString();
			        
		        }
		        else
		        {
		        	telefono="SIN DATOS";
					duracion=0; //le añadimos la modificación de la duración de la llamada;
			        fechaHora="SIN DATOS";
		        }
		        c.close();
		        Log.d("WidgetProvider", "Guardamos los valores del widget en Preferences");
		        guardarPreferences(avisoEstadoTelefono.PREF_NUMERO,telefono);
		        guardarPreferences(avisoEstadoTelefono.PREF_NOMBRE,telefono);
		        guardarPreferences(avisoEstadoTelefono.PREF_FECHA, fechaHora);
		        guardarPreferences(avisoEstadoTelefono.PREF_DURACION, duracion);
            
	       }
	       else
	       {
	    	   	SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCIAS_WIDGET, Context.MODE_PRIVATE);
		       	telefono = sharedPreferences.getString(avisoEstadoTelefono.PREF_NUMERO, "0");
		       	fechaHora = sharedPreferences.getString(avisoEstadoTelefono.PREF_FECHA, "0");
		       	duracion = sharedPreferences.getInt(avisoEstadoTelefono.PREF_DURACION, 0);
		       	nombre=sharedPreferences.getString(avisoEstadoTelefono.PREF_NOMBRE, "SIN NOMBRE");
	       }

	       	double iva=(vp.getcosteConIVA())?vp.getPreferenciasImpuestos():1;
	       	String coste = "SIN FRANJA";
	       	if (totalRegistros>0)
	       	{
		        //Hay que ver que tarifa y franja conrresponde a la llamada realizada
		        ts=new tarifas();
		        ts.cargarFranjas();
		        t=ts.getTarifa(telefono,fechaHora);
		        if (t!=null)
		        {
		        	f=t.getFranja(fechaHora);
		        }
		        
		        //Montamos la cadena donde se van a presentar los datos de la última llamada
		        coste = "SIN FRANJA";
		        if (f!=null&&t!=null)
		        {
		        	coste =FunGlobales.redondear((f.coste(t, duracion)*(iva)),vp.getPreferenciasDecimales())+FunGlobales.monedaLocal();
		        }
	       	}
	       	else
	       	{
	       		coste="SIN DATOS";
	       	}

	        //info+=Html.fromHtml(" @ <strong>"+DateFormat.format("kk:mm",new Date()).toString()+"</strong>");
	       	if (nombre==null)
	       		updateViews.setTextViewText(R.id.txt_numero, telefono); //No tiene nombre de contactos
	       	else
	       		//Truncamos el nombre si tiene más de 12 caracteres
		       	if (nombre.length()>12)
		       		updateViews.setTextViewText(R.id.txt_numero, nombre.subSequence(0, 12)+"...");
		       	else
		       		updateViews.setTextViewText(R.id.txt_numero, nombre);
	       	
			updateViews.setTextViewText(R.id.txt_duracion, ""+FunGlobales.segundosAHoraMinutoSegundo(duracion));
			updateViews.setTextViewText(R.id.txt_fecha, fechaHora);
			updateViews.setTextViewText(R.id.txt_coste, coste);
				
			
	       
			//Evento onClick en el widget 
			
			if (vp.getComportamientoWidget())
			{
				//Arrancamos el dialer
				String number = "tel:" + telefono.trim();
				Intent launchIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(number));
				PendingIntent pIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
				updateViews.setOnClickPendingIntent(R.id.widget, pIntent);
				
			}
			else
			{
				//Arrancamos gastos móvil
				Intent launchIntent = new Intent(context,gastoMovil.class);
		      	PendingIntent intent = PendingIntent.getActivity(context, 0, launchIntent, 0);
	  			updateViews.setOnClickPendingIntent(R.id.widget, intent);
				
			}
	      
			return updateViews;
	   }

    
    
    /**
     * Guarda una preferencia con la dupla clave valor
     * @param key
     * 		Clave de la preferencia.
     * @param value
     * 		Valor de la preferencia.
     */
	  private void guardarPreferences(String key, String value){
		    SharedPreferences sharedPreferences = contexto.getSharedPreferences(PREFERENCIAS_WIDGET, Context.MODE_PRIVATE);
		    SharedPreferences.Editor editor = sharedPreferences.edit();
		    editor.putString(key, value);
		    editor.commit();
		   }
	  
	  private void guardarPreferences(String key, int value){
		    SharedPreferences sharedPreferences = contexto.getSharedPreferences(PREFERENCIAS_WIDGET, Context.MODE_PRIVATE);
		    SharedPreferences.Editor editor = sharedPreferences.edit();
		    editor.putInt(key, value);
		    editor.commit();
		   }

	
	
}
