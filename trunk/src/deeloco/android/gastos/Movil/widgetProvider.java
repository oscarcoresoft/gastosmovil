package deeloco.android.gastos.Movil;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.text.Html;
import android.text.format.DateFormat;
import android.widget.RemoteViews;
import android.widget.Toast;

public class widgetProvider extends AppWidgetProvider {
	
	private tarifas ts=null;
	private tarifa t=null;
	private Franja f=null;
	Context contexto;
	
	
	
	//private SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy  hh:mm:ss a");
	String strWidgetText = "";
	//public static String MY_WIDGET_UPDATE = "MY_OWN_WIDGET_UPDATE";

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		//super.onDeleted(context, appWidgetIds);
		//Toast.makeText(context, "onDeleted()", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
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
		
		ValoresPreferencias vp=new ValoresPreferencias(context);
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
        String info = telefono+"|"+FunGlobales.segundosAHoraMinutoSegundo(duracion);
        if (f!=null)
        {
        	info +="|"+FunGlobales.redondear((f.coste(t, duracion)*((vp.getcosteConIVA())?vp.getPreferenciasImpuestos():1)),vp.getPreferenciasDecimales())+FunGlobales.monedaLocal();
        }
        //info+=Html.fromHtml(" @ <strong>"+DateFormat.format("kk:mm",new Date()).toString()+"</strong>");
		RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		updateViews.setTextViewText(R.id.widgettext, info);
		int consumoDia= Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("consumoDia", "0"));
		updateViews.setTextViewText(R.id.txt_tiempoLimite,"ConsumoDia="+consumoDia);
		
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
		
		
		appWidgetManager.updateAppWidget(appWidgetIds, updateViews);
		
		
		
		

		//super.onUpdate(context, appWidgetManager, appWidgetIds);
		//Toast.makeText(context, "onUpdate()", Toast.LENGTH_LONG).show();
	}
	
	

}
