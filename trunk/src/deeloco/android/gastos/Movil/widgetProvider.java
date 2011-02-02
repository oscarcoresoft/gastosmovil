package deeloco.android.gastos.Movil;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

public class widgetProvider extends AppWidgetProvider {
	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy  hh:mm:ss a");
	String strWidgetText = "";
	public static String MY_WIDGET_UPDATE = "MY_OWN_WIDGET_UPDATE";

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		//super.onDeleted(context, appWidgetIds);
		Toast.makeText(context, "onDeleted()", Toast.LENGTH_LONG).show();
	}
	
	

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		 if(MY_WIDGET_UPDATE.equals(intent.getAction())){
			   Toast.makeText(context, "onReceiver()", Toast.LENGTH_LONG).show();
		 }
	}



	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		//super.onDisabled(context);
		Toast.makeText(context, "onDisabled()", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		//super.onEnabled(context);
		Toast.makeText(context, "onEnabled()", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		String currentTime = formatter.format(new Date());
		strWidgetText = strWidgetText + "\n" + currentTime;

		RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		updateViews.setTextViewText(R.id.widgettext, strWidgetText);
		appWidgetManager.updateAppWidget(appWidgetIds, updateViews);

		//super.onUpdate(context, appWidgetManager, appWidgetIds);
		Toast.makeText(context, "onUpdate()", Toast.LENGTH_LONG).show();
	}
	
	

}
