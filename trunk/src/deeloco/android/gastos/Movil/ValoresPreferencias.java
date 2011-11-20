package deeloco.android.gastos.Movil;

import java.util.Calendar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
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
    	//if (getcosteConIVA())
    	//	tarifa=tarifa*iva; //Con IVA
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
    
    /**
     * Retorna si hay que mostrar el establecimiento de llamada en el listado de llamadas o no
     * @return boolean
     */
    public boolean getEstablecimiento(){
    	boolean valor=PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("chbox_establecimiento", false);
    	return valor;
    }
    
    /**
     * Retorna si hay que mostrar el IVA en el coste de las llamadas
     * @return
     */
    public boolean getcosteConIVA(){
    	boolean valor=PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("chbox_presentarIVA", true);
    	return valor;
    }
    
    
    
    /**
     * Retorna si hay que mostrar los nombres de contacto conrrespondiente a cada número
     * @return
     */
    public boolean getNombreAgenda(){
    	boolean valor=PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("chbox_nombreAgenda", true);
    	return valor;
    }
    
    /**
     * Retorna si hay que mostrar el resumen del total de tiempo hablado por cada día
     * @return
     */
    public boolean getResumenDia(){
    	boolean valor=PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("chbox_resumenDia", true);
    	return valor;
    }
    
    
    
    /**
     * Retorna el valor de Duración modificada, un parámetro de ajuste.
     * @return
     */
    public int getPreferenciasDuracion(){
    	   	
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
    	//String valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("defecto", "");
    	String valor="";
    	return valor;
    }
    
    /**
     * Retorna el valor del impuesto /100 y +1. Esta es la forma adecuada para calcular el valor de un importa, incluido el iva. 
     * @return
     */
    public double getPreferenciasImpuestos(){
    	String valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtImpuestos", "18");
  		double retorno=18.0;
    	try
    	{
    		retorno=Double.parseDouble(valor);
    	}

  	    catch (Exception e) {
  		// TODO: handle exception
  		Log.d(TAG,"getPreferenciasImpuestos: Error al parsear un double");
  	    }
    	retorno=(retorno/100)+1;    	
    	return retorno;
    }
    
    /**
     * Devuelve el valor del impuesto en %
     * @return
     */
    public int getPreferenciasImpuestosPorCiento(){
    	String valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtImpuestos", "18");
    	int retorno=Integer.parseInt(valor);	
    	return retorno;
    } 
    
  public Drawable getColor(String color){

	  	Resources r=contexto.getResources();
	  	Drawable c=null;
	  	Bitmap linea=null;

	  	if (color.equals("Blanco"))  c=r.getDrawable(R.drawable.line7);
	  	if (color.equals("Amarillo"))  c=r.getDrawable(R.drawable.line1);
	  	if (color.equals("Azul"))  c=r.getDrawable(R.drawable.line2);
	  	if (color.equals("Naranja"))  c=r.getDrawable(R.drawable.line3);
	  	if (color.equals("Rojo"))  c=r.getDrawable(R.drawable.line4);
	  	if (color.equals("Verde"))  c=r.getDrawable(R.drawable.line5);
	  	if (color.equals("Violeta"))  c=r.getDrawable(R.drawable.line6);
	  	if (color.equals("Transparente"))  c=r.getDrawable(R.drawable.line0);
	  		  	
	  	if (color.equals("Blanco"))  linea=BitmapFactory.decodeResource(r, R.drawable.line7).copy(Config.ARGB_8888, true);
	  	if (color.equals("Amarillo"))  linea=BitmapFactory.decodeResource(r, R.drawable.line1).copy(Config.ARGB_8888, true);
	  	if (color.equals("Azul"))  linea=BitmapFactory.decodeResource(r, R.drawable.line2).copy(Config.ARGB_8888, true);
	  	if (color.equals("Naranja"))  linea=BitmapFactory.decodeResource(r, R.drawable.line3).copy(Config.ARGB_8888, true);
	  	if (color.equals("Rojo"))  linea=BitmapFactory.decodeResource(r, R.drawable.line4).copy(Config.ARGB_8888, true);
	  	if (color.equals("Verde"))  linea=BitmapFactory.decodeResource(r, R.drawable.line5).copy(Config.ARGB_8888, true);
	  	if (color.equals("Violeta"))  linea=BitmapFactory.decodeResource(r, R.drawable.line6).copy(Config.ARGB_8888, true);
	  	if (color.equals("Transparente"))  linea=BitmapFactory.decodeResource(r, R.drawable.line0).copy(Config.ARGB_8888, true);
	  	
	  	BitmapDrawable bpd=null;
	  	try
	  	{
	  		bpd=new BitmapDrawable(linea);
	  	
	  	}
	  	catch (Exception e) {
			// TODO: handle exception
	  		Log.d(TAG,"Excepcion en getColorDanger - "+e.getMessage());
		}
	  	c=bpd.mutate().getCurrent();
    	return c;
    }
  
  public Drawable getColorIcon(String color,String icon,int altoPantalla){

	  	Resources r=contexto.getResources();
	  	Drawable c=null;
	  	Bitmap linea=null;
	  	Bitmap icono=null;
	  	//Log.d(TAG,"COLOR= "+color);
	  	
	  	if (color.equals("Blanco"))  linea=BitmapFactory.decodeResource(r, R.drawable.line7).copy(Config.ARGB_8888, true);
	  	if (color.equals("Amarillo"))  linea=BitmapFactory.decodeResource(r, R.drawable.line1).copy(Config.ARGB_8888, true);
	  	if (color.equals("Azul"))  linea=BitmapFactory.decodeResource(r, R.drawable.line2).copy(Config.ARGB_8888, true);
	  	if (color.equals("Naranja"))  linea=BitmapFactory.decodeResource(r, R.drawable.line3).copy(Config.ARGB_8888, true);
	  	if (color.equals("Rojo"))  linea=BitmapFactory.decodeResource(r, R.drawable.line4).copy(Config.ARGB_8888, true);
	  	if (color.equals("Verde"))  linea=BitmapFactory.decodeResource(r, R.drawable.line5).copy(Config.ARGB_8888, true);
	  	if (color.equals("Violeta"))  linea=BitmapFactory.decodeResource(r, R.drawable.line6).copy(Config.ARGB_8888, true);
	  	if (color.equals("Transparente"))  linea=BitmapFactory.decodeResource(r, R.drawable.line0).copy(Config.ARGB_8888, true);	  	
	  	
	  	if (icon.equals("relog_mas")) icono=BitmapFactory.decodeResource(r, R.drawable.relog_mas).copy(Config.ARGB_8888, true);
	  	if (icon.equals("relog_peligro")) icono=BitmapFactory.decodeResource(r, R.drawable.relog_peligro).copy(Config.ARGB_8888, true);
	  	
	  	Canvas canvas=new Canvas(linea);
	  	canvas.drawBitmap(icono, 0f, 0f, null);
	  	canvas.scale(2.0f, 2.0f);
	  	
	  	BitmapDrawable bpd=null;
	  	try
	  	{
	  		bpd=new BitmapDrawable(linea);
	  	
	  	}
	  	catch (Exception e) {
			// TODO: handle exception
	  		Log.d(TAG,"Excepcion en getColorDanger - "+e.getMessage());
		}
	c=bpd.mutate().getCurrent();
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
  
  /**
   * Devuelve el valor de la cuota mensual del ajuste
   * @return
   */
  public double getCuotaMensual(){
	 String valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtCuota","0");
	 Double retorno=0.0;
	 try
	 {
		 retorno=Double.parseDouble(valor);
	 }
	 catch (Exception e)
	 {
		 retorno=0.0;
		 Log.d(TAG,"getCuotaMensual: Error al parsear un double");
	 }
	 return retorno;
  }
  
  /**
   * Devuelve el valor del coste de la tarifa plana
   * @return
   */
  public double getTarifaPlana(){
	 String valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtTarifaPlana","0");
	 Double retorno=0.0;
	 try
	 {
		 retorno=Double.parseDouble(valor);
	 }
	 catch (Exception e)
	 {
		 retorno=0.0;
		 Log.d(TAG,"getTarifaPlana: Error al parsear un double");
	 }
	 return retorno;
  } 
  
  /**
   * Devuelva el numero de decimales que se deben mostrar en los cálculos introducido en ajustes
   * @return
   */
  public int getPreferenciasDecimales(){
	  String valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtDecimales", "2");
	  int retorno=2;
	  try
	  {
	  retorno=Integer.parseInt(valor);
	  if (retorno>4) retorno=4;
	  if (retorno<2) retorno=2;
	  }
	  catch (Exception e) {
		// TODO: handle exception
		  retorno=2;
		  Log.d(TAG,"getPreferenciasDecimales: Error al parsear un entero");
	  }
	  return retorno;
  }
  
  
  
  /**
   * Devuelve el gasto minimo que se ha editado en los ajustes.
   * @return
   */
  public double getGastoMinimo(){
	  String valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("txtGastoMinimo", "0");
	  Double retorno=0.0;
		 try
		 {
			 retorno=Double.parseDouble(valor);
		 }
		 catch (Exception e)
		 {
			 retorno=0.0;
			 Log.d(TAG,"getGastoMinimo: Error al parsear un double");
		 }
		 return retorno;
  }
  
  public String getOperadora()
  {
	  String valor=PreferenceManager.getDefaultSharedPreferences(contexto).getString("listOperadora", "Todas");
	  return valor;
  }

}
