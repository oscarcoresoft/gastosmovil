package deeloco.android.gastos.Movil.plus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class avisoLlamadaSaliente extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                
                if(null == bundle)
                        return;
                
                String phonenumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

                Log.i("avisoLlamadaSaliente",phonenumber);
                Log.i("avisoLlamadaSaliente",bundle.toString());
                
                //String info = "Detect Calls sample application\nOutgoing number: " + phonenumber;
                
                //Toast.makeText(context, info, Toast.LENGTH_LONG).show();
        }
}
