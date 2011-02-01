package deeloco.android.gastos.Movil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import android.app.Activity;

public class avisoEstadoTelefono extends  BroadcastReceiver {
	
	

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
                	android.os.SystemClock.sleep(1000);
                	Cursor c; //Cursor con el que recorreremos la base de datos de registros de llamadas
                	c=context.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, CallLog.Calls.TYPE+"="+CallLog.Calls.OUTGOING_TYPE , null, CallLog.Calls.DEFAULT_SORT_ORDER);

                    int iTelefono = c.getColumnIndex(CallLog.Calls.NUMBER);
                    int iFecha = c.getColumnIndex(CallLog.Calls.DATE);
                    int iDuracion = c.getColumnIndex(CallLog.Calls.DURATION);
                    c.moveToFirst();
                    String telefono=c.getString(iTelefono);
            		long fecha=c.getLong(iFecha);
            		int duracion=c.getInt(iDuracion); //le añadimos la modificación de la duración de la llamada;
                    
                    c.close();
                    
                                
                    Log.i("avisoEstadoTelefono","Fin de la llamada, de duración: "+duracion);
                      
                    String info = "Detectada final de llamada\nnumero: "+telefono+" | Duración="+duracion;
                      
                    Toast.makeText(context, info, Toast.LENGTH_LONG).show();
                }
        }

}
