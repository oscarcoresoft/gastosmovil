package deeloco.android.gastos.Movil;

import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

import android.app.Activity;
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
    
}
