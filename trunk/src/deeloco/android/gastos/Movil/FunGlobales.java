package deeloco.android.gastos.Movil;


public class FunGlobales {
	
	
	
    //--- Redondear un número con x decimales
    public static double redondear(double numero, int decimales ) {
        return Math.round(numero*Math.pow(10,decimales))/Math.pow(10,decimales);
    }

    
    public static String periodo(String meses[],int mesInicio,int diaInicio){
    	//Hay que devolver el texto del mes de Inicio y el siguiente, si mesInicio es Diciembre el siguiente es Enero
    	String retorno="";
    	if (diaInicio==1)
    	{
    		retorno =meses[mesInicio];
    	}
    	else
    	{
    		if (mesInicio<12)
    		{
    			retorno= meses[mesInicio].substring(0, 3)+"/"+meses[mesInicio+1].substring(0,3);	
    		}
    		else
    		{
    			retorno= meses[12].substring(0,3)+"/"+meses[1].substring(0, 3);
    		}
    		
    	}
    	return retorno;	
    }
}
