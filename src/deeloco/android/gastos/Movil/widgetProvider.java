package deeloco.android.gastos.Movil;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.content.SharedPreferences;

public class widgetProvider extends AppWidgetProvider {
	

	Context contexto;
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
		
		//RemoteViews updateView = buildUpdate(context);
	    //appWidgetManager.updateAppWidget(appWidgetIds, updateView);
	    //super.onUpdate(context, appWidgetManager, appWidgetIds);

		//super.onUpdate(context, appWidgetManager, appWidgetIds);
		//Toast.makeText(context, "onUpdate()", Toast.LENGTH_LONG).show();
		
		RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCIAS_WIDGET, Context.MODE_PRIVATE);
		String telefono = sharedPreferences.getString(avisoEstadoTelefono.PREF_NUMERO, "0");
		

		
		//updateViews.getTextViewText()
		String number = "tel:" + telefono.trim();
        Intent launchIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(number));
		
		//Intent launchIntent = new Intent(Intent.ACTION_DIAL);
		//startActivity(intent);
		
		//Intent launchIntent = new Intent(context,gastoMovil.class);
    	PendingIntent pIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
    	updateViews.setOnClickPendingIntent(R.id.widget, pIntent);
    	appWidgetManager.updateAppWidget(appWidgetIds, updateViews);
	}	
}
