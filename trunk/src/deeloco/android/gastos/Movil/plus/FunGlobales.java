package deeloco.android.gastos.Movil.plus;

import java.util.Calendar;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.util.Log;

public class FunGlobales extends Activity{
	
	
	
    //--- Redondear un número con x decimales
    public static double redondear(double numero, int decimales ) {
        return Math.round(numero*Math.pow(10,decimales))/Math.pow(10,decimales);
    }

    /**
     * Hay que devolver el texto del mes de Inicio y el siguiente, si mesInicio es Diciembre el siguiente es Enero
     * @param meses
     * @param mesInicio
     * @param diaInicio
     * @return
     */
    public static String periodo(String meses[],int mesInicio,int diaInicio){
    	
    	
    	String retorno="";
    	if (diaInicio==1)
    	{
    		retorno =meses[mesInicio];
    	}
    	else
    	{
    		
    		Calendar calendario= Calendar.getInstance();
    	    int mDia = calendario.get(Calendar.DAY_OF_MONTH);
    		Log.d("Periodo", "Hoy es " + mDia + ". mesInicio="+mesInicio+". diaInicio="+diaInicio);
    		//calendario.
    		if (mDia<diaInicio)
    		{
    			mesInicio=(mesInicio==2)?13:mesInicio--;
    		}
    		
    		if (mesInicio<13)
    		{
    			retorno= meses[mesInicio].substring(0, 3)+"/"+meses[mesInicio+1].substring(0,3);	
    		}
    		else
    		{
    			retorno= meses[13].substring(0,3)+"/"+meses[2].substring(0, 3);
    		}
    		
    	}
    	return retorno;	
    }
    
    public static String monedaLocal(){
    	Currency currency = Currency.getInstance(Locale.getDefault());
    	String codigoMoneda=currency.getCurrencyCode();
    	//Log.d("CODIGO MONEDA",codigoMoneda);
    	if (codigoMoneda.equals("EUR"))
    	{
    		return "€";
    		
    	}
    	if (codigoMoneda.equals("USS"))
    	{
    		return "$";
    		
    	}
    	return "€";
    }
    
    /**
     * Convierte segundos en una cadena de caracteres formateados horas, min. y seg.
     * @param totalsegundos
     * Segundos a formatear
     * @return
     * String con las horas, min. y seg. que corresponden a los segundos pasados como parámetros.
     */
    public static String segundosAHoraMinutoSegundo(int totalsegundos){
    	int horas=0;
    	int minutos=0;
    	int segundos=0;
    	String retorno="";
    	
    	minutos=totalsegundos/60;
    	segundos=totalsegundos%60;
    	
    	if (minutos>60)
    	{
    		horas=minutos/60;
    		minutos=minutos%60;
    		retorno=horas+ "h. "+minutos+"m. "+segundos+"s.";
    	}
    	else
    	{
    		retorno=minutos+"m. "+segundos+"s.";
    	}
    	
    	return retorno;
    }

    /**
     * Convierte segundos en una cadena de caracteres formateados en min. y seg.
     * @param totalsegundos
     * Segundos a formatear
     * @return
     * String con los min. y seg. que corresponden a los segundos pasados como parámetros.
     */

    public static String segundosAMinutoSegundo(int totalsegundos){
    	int minutos=0;
    	int segundos=0;
    	String retorno="";
    	minutos=totalsegundos/60;
    	segundos=totalsegundos%60;
    	retorno=minutos+"m. "+segundos+"s.";
    	return retorno;
    }
    
    
    /**
     * Comprueba si un Intent está disponible en el sistema 
     * @param context
     * @param action
     * @return boolean
     */

    public static boolean estaIntentDisponible(Context context, String action) 
    {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,0);
        return list.size() > 0;
    }
    
    /**
     * Comprueba si un Intent está disponible en el sistema
     * @param context
     * @param action
     * @param uri
     * @return boolean
     */

    public static boolean estaIntentDisponible(Context context, String action,Uri uri) 
    {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action,uri);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,0);
        return list.size() > 0;
    }
    
    /**
     * Comprueba si en Market esta instalado en el dispositivo
     * @param context
     * @return boolean
     * True si está instalado y False si no lo está.
     */
    public static boolean estaMarketInstalado(Context context)
    {
    	
    	final PackageManager packageManager = context.getPackageManager();
    	String packagename = context.getPackageName();
    	String url = "market://details?id=" + packagename;

    	Intent intentMarket = new Intent(Intent.ACTION_VIEW);
    	intentMarket.setData(Uri.parse(url));

    	List<ResolveInfo> list = packageManager.queryIntentActivities(intentMarket,PackageManager.MATCH_DEFAULT_ONLY);

    	return list.size() > 0;
    }
    
    /**
     * Dado una fecha devuelve una cadena con el día de la semana correspondiente.
     * @param fecha String con la fecha
     * @return String con el día de la semana.
     */
    public static String diaSemana(String fecha)
    {
    	String diasCortos[] = {"Lun.","Mar.","Mie.","Jue.","Vie.","Sab.","Dom."};
    	
    	String sFecha =fecha.substring(0, 10).trim();
		String sHora=fecha.substring(10, fecha.length()).trim();
		String sDia=sFecha.substring(0,2);
		String sMes=sFecha.substring(3, 5);
		String sAno=sFecha.substring(6, 10);

		int ano= Integer.parseInt(sAno);
		int mes= Integer.parseInt(sMes)-1;
		int dia= Integer.parseInt(sDia);
		
		Calendar calendario=new GregorianCalendar(ano,mes,dia);
		calendario.setFirstDayOfWeek(Calendar.MONDAY);
		int diaSemana=calendario.get(Calendar.DAY_OF_WEEK);
		String shora=sHora;
		
		switch (diaSemana) {
		case Calendar.SUNDAY:
			diaSemana=6;
			break;

		default:
			diaSemana=diaSemana-2;
			break;
		}


    	return diasCortos[diaSemana];
    }
    
    /**
     * Dado un numero de teléfono, le quita el prefijo del pais, si tiene. Si no tiene prefijo, devuelve el numero. 
     * @param numero String al que hay que quitar el prefijo, si tiene.
     * @return String con el numero sin el prefijo.
     */
    public static String quitarPrePais(String numero)
    {
    	String retorno="";
    	String prePais="34"; //España
    	//Comprobamos si tiene prefijo de pais
    	if (numero.contains(prePais))
    	{
    		//Tiene prefijo
    		String inicio[]=numero.split(prePais);
    		retorno=inicio[1].trim();
    	}
    	else
    		retorno=numero; //Retornamos el numero, que no tiene prefijo de pais
    	
    	return retorno;
    }
}
