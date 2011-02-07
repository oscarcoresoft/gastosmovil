package deeloco.android.gastos.Movil;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;
import android.appwidget.AppWidgetManager;
import android.widget.RemoteViews;

public class avisoEstadoTelefono extends  BroadcastReceiver {
	
		private tarifas ts=null;
		private tarifa t=null;
		private Franja f=null;

        @Override
        public void onReceive(Context context, Intent intent) {
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
                	
                	//Hacemos una pausa de 1sg. para dar tiempo a actualizar el registro de llamadas
                	android.os.SystemClock.sleep(1000);
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
                    
                    //Hay que ver que tarifa y franja conrresponde a la llamada realizada
                    ts=new tarifas();
                    ts.cargarFranjas();
                    t=ts.getTarifa(telefono,fechaHora);
                    if (t!=null)
                    {
                    	f=t.getFranja(fechaHora);
                    }
                    
                    //Montamos la cadena donde se van a presentar los datos
                    String info = telefono+" | "+FunGlobales.segundosAHoraMinutoSegundo(duracion);
                    if (f!=null)
                    {
                    	info +=" | "+FunGlobales.redondear((f.coste(t, duracion)*((vp.getcosteConIVA())?vp.getPreferenciasImpuestos():1)),vp.getPreferenciasDecimales())+FunGlobales.monedaLocal();
                    }

            		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
                    ComponentName thisWidget = new ComponentName(context, widgetProvider.class);
                    
                    remoteViews.setTextViewText(R.id.widgettext, info);
                    appWidgetManager.updateAppWidget(thisWidget, remoteViews);
                }
        }

}
