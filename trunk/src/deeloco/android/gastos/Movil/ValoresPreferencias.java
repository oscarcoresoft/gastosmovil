package deeloco.android.gastos.Movil;

import java.util.Calendar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * ValoresPreferencias: Retorna el valor almacenado en las preferencias de la aplicación.
     */

public class ValoresPreferencias {
	
    //private final int NUMSC=1;
	private double iva=1.18;
    private final int NUMESP1=2;
    private final int NUMESP2=3;
    private final int NUM=4;
    private static final String TAG = "ValoresPreferencias";
    
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
    	String mes= PreferenceManager.getDefaultSharedPreferences(contexto).getString("listMes", "1");
    	int retorno=Integer.parseInt(mes);
    	switch (retorno)
    	{
	    	case 0: //Listar todos los meses
	    		mesRetorno=0;
	    		break;
	    		
	    	case 1: //Listar el mes actual
	    		Calendar calendar = Calendar.getInstance();
	        	mesRetorno = calendar.get(Calendar.MONTH); //ENERO=0, DICIEMBRE=11
	        	int dia=calendar.get(Calendar.DAY_OF_MONTH);
	        	mesRetorno++;
	        	if (dia<this.getPreferenciasInicioMes())
	        	{
	        		mesRetorno--;
	        		if (mesRetorno==0) mesRetorno=12;
	        	}
	    		break;
	    		
	    	default: //Listar el mes seleccionado
	    		mesRetorno=retorno-1;
	    		break;
    	}    	    	
    	return mesRetorno;
    }
    
    public double getPreferenciasTarifa(int listaNumero){
    	//Retorna el valor de tarifa, que es uno de los parametrios de ajustes.
    	iva=getPreferenciasImpuestos();
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
    	//Retorna el valor de tarifa del SMS, que es uno de los parametrios de ajustes.
    	iva=getPreferenciasImpuestos();
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
    	tarifa=tarifa/100; //Ya lo tengo pasado a euros
    	tarifa=tarifa*iva; //Con IVA
    	return  tarifa;
    }
    
    public int getPreferenciasSMSGratuitos(){
    	String valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtSMSGratis", "0");
    	int numeroSMS=0;
    	try
    	{
    		numeroSMS=Integer.parseInt(valor);
    	}
    	catch (Exception e) {
			numeroSMS=0;
		}
    	return numeroSMS;
    }
    
    public double getPreferenciasEstLlamadas(int listaNumero){
    	//Retorna el valor de la tarifa del establecimento de la llamada, en los parámetros de ajustes
    	iva=getPreferenciasImpuestos();
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
    
    
    public boolean getNombreAgenda(){
    	boolean valor=PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("chbox_nombreAgenda", false);
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
    
    /**
     * Retorna el valor de Duración, un parámetro de ajuste.
     * @return
     */
    public int getPreferenciasInicioMes(){
    	
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
    
    /**
     * Retorna el valor de la preferencia defecto = Tarifa por defecto aplicada.
     * @return
     */
    public String getPreferenciasDefecto(){
    	String valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("defecto", "");
    	return valor;
    }
    
    
    public double getPreferenciasImpuestos(){
    	String valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtImpuestos", "18");
    	double retorno=Double.parseDouble(valor);
    	retorno=(retorno/100)+1;    	
    	return retorno;
    	//return 1.36;
    }
    
  public Drawable getColor(String color){

	  	Resources r=contexto.getResources();
	  	Drawable c=null;
	  	
	  	if (color.equals("Blanco"))  c=r.getDrawable(R.drawable.line7);
	  	if (color.equals("Amarillo"))  c=r.getDrawable(R.drawable.line1);
	  	if (color.equals("Azul"))  c=r.getDrawable(R.drawable.line2);
	  	if (color.equals("Naranja"))  c=r.getDrawable(R.drawable.line3);
	  	if (color.equals("Rojo"))  c=r.getDrawable(R.drawable.line4);
	  	if (color.equals("Verde"))  c=r.getDrawable(R.drawable.line5);
	  	if (color.equals("Violeta"))  c=r.getDrawable(R.drawable.line6);
	  	if (color.equals("Transparente"))  c=r.getDrawable(R.drawable.line0);
    	return c;
    }
  
  public Drawable getColorDanger(String color){

	  	Resources r=contexto.getResources();
	  	Drawable c=null;
	  	
	  	if (color.equals("Blanco"))  c=r.getDrawable(R.drawable.line5_danger);
	  	if (color.equals("Amarillo"))  c=r.getDrawable(R.drawable.line5_danger);
	  	if (color.equals("Azul"))  c=r.getDrawable(R.drawable.line5_danger);
	  	if (color.equals("Naranja"))  c=r.getDrawable(R.drawable.line5_danger);
	  	if (color.equals("Rojo"))  c=r.getDrawable(R.drawable.line5_danger);
	  	if (color.equals("Verde"))  c=r.getDrawable(R.drawable.line5_danger);
	  	if (color.equals("Violeta"))  c=r.getDrawable(R.drawable.line5_danger);
	  	if (color.equals("Transparente"))  c=r.getDrawable(R.drawable.line5_danger);
  	return c;
  }
  
  
  
  /**
   * Devuelve el valor del ajuste de descuento.
   * @return
   */
  public int getPreferenciasDescuento(){
	  String valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtDescuento", "0");
	  int retorno=0;
	  try
	  {
	  retorno=Integer.parseInt(valor);
	  }
	  catch (Exception e) {
		// TODO: handle exception
		  retorno=0;
		  Log.d(TAG,"getPreferenciasDescuento: Error al parsear un entero");
	  }
	  return retorno;
  }
  

}
