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

public class avisoEstadoTelefono extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                
                if(null == bundle)
                        return;
                
                Log.i("avisoEstadoTelefono",bundle.toString());
                
                String state = bundle.getString(TelephonyManager.EXTRA_STATE);
                                
                Log.i("avisoEstadoTelefono","State: "+ state);
                
                if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE))
                {
                	Cursor c; //Cursor con el que recorreremos la base de datos de registros de llamadas
                 	   c=context.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, CallLog.Calls.TYPE+"="+CallLog.Calls.OUTGOING_TYPE , null, CallLog.Calls.DEFAULT_SORT_ORDER);
                                
                        Log.i("avisoEstadoTelefono","Fin de la llamada: ");
                        
                        String info = "Detectada final de llamada\nnumber: ";
                        
                        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
                }
        }

}
