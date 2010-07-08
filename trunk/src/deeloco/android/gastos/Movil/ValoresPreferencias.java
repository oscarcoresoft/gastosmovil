package deeloco.android.gastos.Movil;

import java.util.Calendar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

/**
 * ValoresPreferencias: Retorna el valor almacenado en las preferencias de la aplicación.
     */

public class ValoresPreferencias {
	
    //private final int NUMSC=1;
	double iva=1.16;
    private final int NUMESP1=2;
    private final int NUMESP2=3;
    private final int NUM=4;
    
    Context contexto;
	
    /**
     * Constructor: Constructor de la clase. Se le pasa el Contexto de la preferencias que se quiere.
         */
    public ValoresPreferencias(Context c)
    {
    	contexto=c;
    }
	   //***************************************************
	// Funciones que recogen los valores de preferencias
	//***************************************************
	
    /*
     * CONTEXT_ID_CP_PROPERTY_PAGE =
     * Basic profile name/description/auto-connect property page
     */

    public int getPreferenciasMes(){
    	//Retorno del valor de Mes, variable de ajuste.
    	int mesRetorno;
    	String mes= PreferenceManager.getDefaultSharedPreferences(contexto).getString("listMes", "0");
    	int retorno=Integer.parseInt(mes);
    	switch (retorno)
    	{
	    	case 0: //Listar todos los meses
	    		mesRetorno=0;
	    		break;
	    		
	    	case 1: //Listar el mes actual
	    		Calendar calendar = Calendar.getInstance();
	        	mesRetorno = calendar.get(Calendar.MONTH); //ENERO=0, DICIEMBRE=11
	        	mesRetorno++;
	    		break;
	    		
	    	default: //Listar el mes seleccionado
	    		mesRetorno=retorno-1;
	    		break;
    	}    	    	
    	return mesRetorno;
    }
    
    public double getPreferenciasTarifa(int listaNumero){
    	//Retorna el valor de tarifa, que es uno de los parametrios de ajustes.
    	
    	String valor="";
    	switch(listaNumero)
    	{
    		case NUM:
    			valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtTarifaLlamadas", "8");
    			break;
    		case NUMESP1:
    			valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtTarifaEsp1Llamadas", "8");
    			break;
    		case NUMESP2:
    			valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtTarifaEsp2Llamadas", "8");
    			break;
    	}
    	
    	double tarifa=8; //Valor por defecto
    	try 
    	{
    	tarifa=Double.parseDouble(valor);	
    	}
    	catch(Exception e)
    	{
    		tarifa=8; //Valor en caso de una excepción (null, puntuación, ...)    		
    	}
    	tarifa=tarifa/100; //Ya lo tengo pasado a euros
    	tarifa=tarifa*iva; //Con IVA
    	tarifa=tarifa/60; //Por segundos
    	return  tarifa;
    }
    
    public double getPreferenciasTarifaSMS(){
    	//Retorna el valor de tarifa, que es uno de los parametrios de ajustes.
    	
    	String valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtTarifaSMS", "8");
    	double tarifa=9; //Valor por defecto
    	try 
    	{
    		tarifa=Double.parseDouble(valor);	
    	}
    	catch(Exception e)
    	{
    		tarifa=9; //Valor en caso de una excepción (null, puntuación, ...)    		
    	}
    	//System.out.println("tarifa:***************** "+tarifa);
    	tarifa=tarifa/100; //Ya lo tengo pasado a euros
    	//System.out.println("tarifa/100:***************** "+tarifa);
    	tarifa=tarifa*iva; //Con IVA
    	//System.out.println("tarifa*1.16:***************** "+tarifa);
    	return  tarifa;
    }
    
    public double getPreferenciasEstLlamadas(int listaNumero){
    	//Retorna el valor de la tarifa del establecimento de la llamada, en los parámetros de ajustes
    	
    	String valor="";
    	switch(listaNumero)
    	{
    		case NUM:
    			valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtTarifaEstLlamada", "15");
    			break;
    		case NUMESP1:
    			valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtTarifaEsp1EstLlamada", "15");
    			break;
    		case NUMESP2:
    			valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtTarifaEsp2EstLlamada", "15");
    			break;
    	}
    	
    	double tarifa=15; //Valor por defecto
    	try 
    	{
    		tarifa=Double.parseDouble(valor);	
    	}
    	catch(Exception e)
    	{
    		tarifa=15; //Valor en caso de una excepción (null, puntuación, ...)    		
    	}
    	tarifa=tarifa/100; //Pasado a euros
    	tarifa=tarifa*iva; //Con IVA
    	return tarifa;
    }
    
    public boolean getEstablecimiento(){
    	boolean valor=PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("chbox_establecimiento", false);
    	return valor;
    }
    
    public int getPreferenciasDuracion(){
    	//Retorna el valor de Duración, un parámetro de ajuste.
    	
    	String valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtDuracion", "0");
    	int duracion=0; //Valor por defecto
    	try
    	{
    		duracion=Integer.parseInt(valor);
    	}
    	catch(Exception e)
    	{
    		duracion=0; //Valor en caso de una excepción.
    	}
    	return duracion; 
    }
    
    
    public boolean getPrefEsp1Activada(){
    	boolean valor=PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("cboxTarifaEsp1", true);
    	return valor;
    }
    
    public boolean getPrefEsp2Activada(){
    	boolean valor=PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("cboxTarifaEsp2", true);
    	return valor;
    }
    
    public String getPrefEsp1Nombre(){
    	String valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtTarifaEsp1Nombre", "0");
    	return valor; 
    }
    
    public String getPrefEsp2Nombre(){
    	String valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtTarifaEsp2Nombre", "0");
    	return valor; 
    }
    
    public Drawable getPreferenciasColor(int listaNumero){
    	//Retorno del color de la tarifa correspondiente
    	// listaNumero=NUM -> Numeros Normales
    	// listaNumero=NUMESP1 -> Numeros Especiales 1
    	// listaNumero=NUMESP2 -> Numeros Especiales 2
    	
    	int color=0;
    	String col="";
    	Resources r=contexto.getResources();
    	Drawable c=r.getDrawable(R.drawable.line0);
    	switch(listaNumero)
    	{
    		case NUM:
    			col= PreferenceManager.getDefaultSharedPreferences(contexto).getString("listColores", "4");
    			color=Integer.parseInt(col);
    			break;
    		case NUMESP1:
    			col= PreferenceManager.getDefaultSharedPreferences(contexto).getString("listColoresEsp1", "4");
    			color=Integer.parseInt(col);
    			break;
    		case NUMESP2:
    			col= PreferenceManager.getDefaultSharedPreferences(contexto).getString("listColoresEsp2", "4");
    			color=Integer.parseInt(col);
    			break;
    	}
    	//System.out.println("El color de ("+listaNumero+") es: "+col);
    	switch (color)
    	{
    		case 0: c=r.getDrawable(R.drawable.line0);break;
    		case 1: c=r.getDrawable(R.drawable.line1);break;
    		case 2: c=r.getDrawable(R.drawable.line2);break;
    		case 3: c=r.getDrawable(R.drawable.line3);break;
    		case 4: c=r.getDrawable(R.drawable.line4);break;
    		case 5: c=r.getDrawable(R.drawable.line5);break;
    		case 6: c=r.getDrawable(R.drawable.line6);break;
    		case 7: c=r.getDrawable(R.drawable.line7);break;
    	}
    	return c;
    }
    
    
    public int getPreferenciasInicioMes(){
    	//Retorna el valor de Duración, un parámetro de ajuste.
    	
    	String valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtInicioMes", "1");
    	int duracion=1; //Valor por defecto
    	try
    	{
    		duracion=Integer.parseInt(valor);
    	}
    	catch(Exception e)
    	{
    		duracion=1; //Valor en caso de una excepción.
    	}
    	return duracion; 
    }

}
