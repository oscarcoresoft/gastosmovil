package deeloco.android.gastos.Movil;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.widget.RemoteViews;

public class avisoEstadoTelefono extends  BroadcastReceiver {
	
		private tarifas ts=null;
		private tarifa t=null;
		private Franja f=null;
		public static String MY_WIDGET_UPDATE = "MY_OWN_WIDGET_UPDATE";
		static final String PREFERENCIAS_WIDGET="MIS_PREFERENCIAS_WIDGET";
		static final String PREF_NUMERO="NUMERO_ULTIMA";
		static final String PREF_DURACION="DURACION_ULTIMA";
		static final String PREF_FECHA="FECHA_ULTIMA";
		static final String PREF_LLAMADA="LLAMADA_ULTIMA";
		static final String PREF_NOMBRE="NOMBRE_ULTIMA";
		Context contexto;

        @Override
        public void onReceive(Context context, Intent intent) {
        		contexto=context;
                Bundle bundle = intent.getExtras();
                ValoresPreferencias vp=new ValoresPreferencias(context);
                
                if(null == bundle)
                        return;
                
                Log.i("avisoEstadoTelefono",bundle.toString());
                
                String state = bundle.getString(TelephonyManager.EXTRA_STATE);
                                
                Log.i("avisoEstadoTelefono","State: "+ state);
                
                if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE))
                {
                	Log.i("GM:avisoEstadoTelefono","Fin de la llamada");
                	
                	try
                	{
                		//Actualizamos el widget
                		
                		RemoteViews updateView = buildUpdate(context);
                		ComponentName myComponentName = new ComponentName(context, widgetProvider.class);
                		AppWidgetManager manager = AppWidgetManager.getInstance(context);
                		manager.updateAppWidget(myComponentName, updateView);
                		
                		/*
	                    Intent intentUpdateWidget = new Intent(MY_WIDGET_UPDATE);
	                    intentUpdateWidget.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                    context.startActivity(intentUpdateWidget);
	                    */
	                    
                	}
                	catch (Exception e)
                	{
                		Log.d("GM:avisoEstadoTelefono","Error:"+e);
                	}
                }
        }
        
        
        
        
       /**
        * Función que se encarga de actualizar los elementos del interface del widget
        * @param context
        * @return RemoteView
        * 	Vista remota con todos los elementos del widget actualizados.
        */
	   private RemoteViews buildUpdate(Context context) {
		   
	       //RemoteViews updateView = null;
	       ValoresPreferencias vp=new ValoresPreferencias(context);
	       RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget);
	       int totalRegistros=1;
	       String telefono;
	       String fechaHora;
	       int duracion=0;
	       String nombre="";			
	       SharedPreferences preferencias = context.getSharedPreferences(PREFERENCIAS_WIDGET, Context.MODE_PRIVATE);
	       if (preferencias!=null)
	       {
	    	   //Log.i("widgetProvider","Preferencias=null");
	    	 //Cursor con el que recorreremos la base de datos de registros de llamadas para obtener los valores de la última llamada
	    	   android.os.SystemClock.sleep(1000);
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
			        nombre="Gastos Móvil";
		        }
		        c.close();
                guardarPreferences(avisoEstadoTelefono.PREF_NUMERO,telefono);
                guardarPreferences(avisoEstadoTelefono.PREF_FECHA, fechaHora);
                guardarPreferences(avisoEstadoTelefono.PREF_DURACION, duracion);
                guardarPreferences(avisoEstadoTelefono.PREF_NOMBRE, nombre);
                
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
		       	if (nombre.length()>10)
		       		updateViews.setTextViewText(R.id.txt_numero, nombre.subSequence(0, 10)+"...");
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
