/*(C) Copyright

    This file is part of GastosMovil.
    
    GastosMovil is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    GastosMovil is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with GastosMovil.  If not, see <http://www.gnu.org/licenses/>.
    
    Autor: Antonio Cristóbal Álvarez Abellán -> acabellan@gmail.com
    
    */

package deeloco.android.gastos.Movil;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import deeloco.android.gastos.Movil.R;
import deeloco.android.gastos.Movil.PreferencesTarifas;
import deeloco.android.gastos.Movil.tarifas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.LinearLayout.LayoutParams;


public class gastoMovil extends ListActivity{
    /** Called when the activity is first created. */
		
	private static final int TARIFAS = Menu.FIRST;
	private static final int AJUSTES = Menu.FIRST+1;
	private static final int FACTURA = Menu.FIRST+2;
	private static final int ESTADISTICAS = Menu.FIRST+3;
    private static final int ACERCADE=Menu.FIRST+4;
    private static final int SALIR = Menu.FIRST+5;
    
    private static final int RETURN_PREFERENCES_AJUSTES = 1;
    private static final int RETURN_PREFERENCES_TARIFAS=2;
    
    private descargar_fichero ficheroServidor;
    private static final String TARIFAS_RETORNO = "tarifas_retorno";
    //private static final String TAG = "GastosMóvil";
    private double iva=1.18;
    String path="/sdcard/gastosmovil/datosTarifas.xml";
    int totalRegistros=0;
    private double costeLlamadas=0;
    private double costeSMS=0;
    int numLlamadas=0;
    
    private List<IconoYTexto> lista = new ArrayList<IconoYTexto>();
    private List<IconoYTexto> listaInvertida = new ArrayList<IconoYTexto>();
    GastosPorNumero gpn=new GastosPorNumero();
    GastosPorHora gph=new GastosPorHora();
    ValoresPreferencias vp=new ValoresPreferencias(this);
    private tarifas ts=new tarifas();
    ProgressDialog dialog;
    Display display;

    //******************** AQUI ***************************

    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(Menu.NONE, TARIFAS, 0, R.string.mn_tarifas).setIcon(android.R.drawable.ic_menu_recent_history);
    	menu.add(Menu.NONE, AJUSTES, 0, R.string.mn_ajustes).setIcon(android.R.drawable.ic_menu_preferences);
    	menu.add(Menu.NONE, FACTURA, 0, R.string.mn_factura).setIcon(android.R.drawable.ic_menu_edit);
    	menu.add(Menu.NONE, ESTADISTICAS, 0, R.string.mn_estadisticas).setIcon(android.R.drawable.ic_menu_agenda);
    	menu.add(Menu.NONE, ACERCADE, 0, R.string.mn_acercade).setIcon(android.R.drawable.ic_menu_info_details);
    	menu.add(Menu.NONE, SALIR, 0, R.string.mn_donacion).setIcon(android.R.drawable.ic_menu_view);
    	return true;
    }
    
    @Override
    public void onCreate(Bundle icicle) {
    	this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(icicle);
        setContentView(R.layout.main);
        if (vp.getcosteConIVA())
        	iva=vp.getPreferenciasImpuestos();
        else
        	iva=1.00; //Sin IVA
        display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        /* Cargamos los valores de las tarifas */
        try
        {
        	//Comprobamos si el fichero esta creado. Si es que no, se crea.
        	File f=new File(path);
        	if (!f.exists())
        	{
        		//El fichero no existe, hay que crearlo
        		try{
        			boolean dir = new File("/sdcard/gastosmovil").mkdir(); //Creamos el directorio
        			if (dir) //Si se ha creado correctamente el directorio
        			{
        				f.createNewFile(); //Creamos el fichero 
        			}
        		}
        		catch (Exception e){ //Error al crear el directorio o el fichero
        			Log.e("Gastos Móvil","Error al crear el fichero o directorio: " + e.getMessage());
        		}
        	}
        	
	        SAXParserFactory spf = SAXParserFactory.newInstance();
	        SAXParser sp = spf.newSAXParser();
	        /* Get the XMLReader of the SAXParser we created. */
	        XMLReader xr = sp.getXMLReader();
	        /* Create a new ContentHandler and apply it to the XML-Reader*/
	        TarifasParserXML tarifasXML = new TarifasParserXML();
	        tarifasXML.setTarifas(ts);
	        xr.setContentHandler(tarifasXML);
	        xr.parse(new InputSource (new FileReader(path)));
	        /* Parsing has finished. */
	        /*Comprobamos si tiene seleccionado operadora*/
	        if (vp.getOperadora().equals("SinOperadora"))
	        {
	        	//Toast.makeText(getBaseContext(),"Sin Operadora",Toast.LENGTH_LONG).show();
	        	//Dialogo de selección de operadora
	        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(getString(R.string.mn_operador));
				builder.setSingleChoiceItems(R.array.operadoras,-1, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				        //Meter la operadora seleccionada en preferencias
				    	vp.guardarPreferences("listOperadora", getResources().getStringArray(R.array.operadoras)[item]);
				    	dialog.cancel();
				    	//Descargamos las tarifas
				    	if (!vp.getOperadora().equals("SinOperadora"))
				    	{
	 		        	   	ficheroServidor=new descargar_fichero(getApplicationContext(), getString(R.string.tarifasPre_url), getString(R.string.tarifasPre_pre)+vp.getOperadora()+getString(R.string.tarifasPre_ext), getString(R.string.tarifasPre_path));
	 		        	   	Log.d("AlertDialog","URL="+ficheroServidor.getURL()+ficheroServidor.getNombreFichero());
	 		        	   	switch (ficheroServidor.download())
	 		           		{
	 		           		case 1:
	 		           			Toast.makeText(getApplicationContext(),getString(R.string.msg_descarga_masTarde),Toast.LENGTH_LONG).show();
	 		       				break;
	 		           		case 2:
	 		           			Toast.makeText(getApplicationContext(),getString(R.string.msg_descarga_sinConexion),Toast.LENGTH_LONG).show();
	 		           			break;
	 		           		default:
	 		           			Toast.makeText(getApplicationContext(),getString(R.string.msg_descarga_ok),Toast.LENGTH_LONG).show();
	 		           			break;
	 		           		}
				    	}				    	
				    }
				});
				AlertDialog alert = builder.create();
				alert.show();
	        }

	        listado(vp.getPreferenciasMes());
	        
        }
        catch (Exception e)
        {
        	//Si el error se produce porque no existe el fichero xml, hay que crearlo.
        	//Tambien hay que crear el directorio
        	
        	System.out.println("ERROR:"+e.toString()+" ("+e.hashCode()+")");
        }

    }
    
	public boolean onOptionsItemSelected (MenuItem item) {
		
		Bundle extras = new Bundle();
        switch(item.getItemId()) {
        
        case AJUSTES:
      		Intent settingsActivity = new Intent(getBaseContext(), Preferencias.class );
      		//startActivity(settingsActivity);
      		extras = new Bundle();
      		List <String> nom=ts.nombresTarifas();
      		String [] nomTarifas = nom.toArray(new String[nom.size()]);

        	extras.putStringArray("nombresTarifas", nomTarifas);
        	settingsActivity.putExtras(extras);
      		startActivityForResult(settingsActivity, RETURN_PREFERENCES_AJUSTES);
            break;
            
        case TARIFAS:
        	//listado(vp.getPreferenciasMes());
           	Intent settingsActivity2 = new Intent(getBaseContext(), PreferencesTarifas.class );
        	extras = new Bundle();
        	extras.putSerializable("tarifas", ts);
        	settingsActivity2.putExtras(extras);
        	//startActivity(settingsActivity2);
        	startActivityForResult(settingsActivity2, RETURN_PREFERENCES_TARIFAS);
        	break;
        	
        case SALIR:
        	//finish();
        	Intent myIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://gastosmovil.simahuelva.es/"));
        			startActivity(myIntent);
            break;
            
        case ACERCADE:
        	//showDialog(DIALOG_ACERCADE);
        	
        	gpn.ordenaDuracion();
        	gpn.ordenaGastos();
        	Intent i = new Intent(getBaseContext(), AcercaDe.class);
        	startActivity(i);
        	break;
        
        case FACTURA:
        	//showDialog(DIALOG_ACERCADE);
        	Intent iFactura = new Intent(getBaseContext(), simulacionFactura.class);
        	extras = new Bundle();
        	extras.putSerializable("tarifas", ts);
        	//Log.d(TAG,"Coste llamadas="+costeLlamadas);
        	extras.putDouble("costeLlamadas", costeLlamadas);
        	extras.putDouble("costeSMS", costeSMS);
        	iFactura.putExtras(extras);
        	startActivity(iFactura);
        	break;
        case ESTADISTICAS:
        	//TextView tv_Numllamadas= (TextView) findViewById(R.id.txtNumLlamadas);
        	//String numLlamadas=tv_Numllamadas.getText().toString();
        	if (numLlamadas==0)
        	{
        		Toast.makeText(getBaseContext(),R.string.mensaje_noexistellamadas,Toast.LENGTH_LONG).show();
        		break;
        	}
        	gpn.ordenaGastos();
        	gph.ordenaGastos();
        	//Intent ii = new Intent(getBaseContext(), GastosPorNumeroActivity.class);
        	Intent ii = new Intent(getBaseContext(), Estadisticas.class);
        	ArrayList<String> numeros=new ArrayList <String>();
        	ArrayList<String> gastos=new ArrayList <String>();
        	ArrayList<String> horas=new ArrayList <String>();
        	ArrayList<String> gastos2=new ArrayList <String>();
        	ArrayList<String> numeros2=new ArrayList <String>();
        	ArrayList<String> duracion=new ArrayList <String>();
        	
        	String total= FunGlobales.redondear(costeLlamadas+costeSMS,2)+"";
        	
        	//Preparamos los datos estadísticos de gastos por numero
        	numeros=(ArrayList<String>) gpn.getNumeros();
        	//CAMBIAR EL ROLLO ESTE, POR UNA FUNCIÓN EN LA CLASE CORRESPONDIENTE QUE ME DEVUELVA UN ARRALIST DE STRING
        	Object ObjGastos[]=gpn.getGastos().toArray();
        	for (Object x:ObjGastos){
        		gastos.add(""+x);
        	}
        	
        	//Preparamos los datos estadísticos de gastos por horas
        	horas=(ArrayList<String>) gph.gethoras();
        	//CAMBIAR EL ROLLO ESTE, POR UNA FUNCIÓN EN LA CLASE CORRESPONDIENTE QUE ME DEVUELVA UN ARRALIST DE STRING
        	Object ObjGastos2[]=gph.getGastos().toArray();
        	for (Object x:ObjGastos2){
        		gastos2.add(""+x);
        	}
        	
        	//Preparamos los datos estadísticos de duración por numero
        	gpn.ordenaDuracion();
        	numeros2=(ArrayList<String>) gpn.getNumeros();
        	//CAMBIAR EL ROLLO ESTE, POR UNA FUNCIÓN EN LA CLASE CORRESPONDIENTE QUE ME DEVUELVA UN ARRALIST DE STRING
        	Object ObjDuracion[]=gpn.getDuracion().toArray();
        	for (Object x:ObjDuracion){
        		duracion.add(""+x);
        	}
        	
        	
        	//Estadísticas gastos por numero
        	ii.putExtra("total",total);
        	ii.putExtra("Numeros", numeros);
        	ii.putExtra("Gastos",gastos);
        	//Estadísticas gastos por hora
        	ii.putExtra("Horas", horas);
        	ii.putExtra("Gastos2",gastos2);
        	//Estadísticas duración por numero
        	String totalSegundos=ts.getSegConsumidosMes()+"";
        	ii.putExtra("totalSegundos",totalSegundos);
        	ii.putExtra("Numeros2", numeros2);
        	ii.putExtra("Duracion",duracion);
        	
        	extras = new Bundle();
        	extras.putSerializable("tarifas", ts);
        	ii.putExtras(extras);
        	startActivity(ii);
        	break;
        }
        
        return true;
	}
	
   
    
    //***************************************************
	// FUNCIONES
	//***************************************************
    

    //--- Retorna el número de SMS enviados
    private int getNumSMS_send()
    {
		int retorno=0;
		int mes=vp.getPreferenciasMes();
		
       Cursor c;
       if (mes==0)
       {
           try
           {
               c=this.getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null);
               this.startManagingCursor(c);
               retorno=c.getCount();
               c.close();
           }
           catch (Exception e)
           {
               System.out.println("* Excepción: "+e.toString());
           }
       }
       else
       {
       /*Vamos a hacer una consulta de un mes concreto*/
    	/*De momento, para las pruebas, estamos utilizando 2009, pero hay que utilizar el año en curso, el mes, ... */
    	/*   Date hoy=new Date();
    	   int anyo=hoy.getYear()+1900;
    	   int mesActual=hoy.getMonth()+1;
    	   if (mesActual<mes)
    	   {
    		   anyo--;
    	   }
    	   Calendar c1=new GregorianCalendar(anyo,mes-1,1,0,0);
           Calendar c2=new GregorianCalendar(anyo,mes,1,0,0);
           Date d1=new Date();
           Date d2=new Date();
           d1=c1.getTime();
           d2=c2.getTime();*/
    	   
    	   int valorIncioMes=vp.getPreferenciasInicioMes();
    	   Date hoy=new Date();
    	   int anyo=hoy.getYear()+1900;
    	   int mesActual=hoy.getMonth()+1;
    	   if (mesActual<mes)
    	   {
    		   anyo--;
    	   }
           Calendar c1=new GregorianCalendar(anyo,mes-1,valorIncioMes,0,0);
           Calendar c2=new GregorianCalendar(anyo,mes,valorIncioMes,0,0);
           
           Date d1=new Date();
           Date d2=new Date();
           d1=c1.getTime();
           d2=c2.getTime();

           try
           {
        	   c=this.getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null);
               this.startManagingCursor(c);
               c.moveToFirst();
               int iDate=c.getColumnIndex("date");
               if (c.isFirst())
               {
            	   do{
            		   long dateCol =c.getLong(iDate);
            		   //System.out.println("SMS con fecha: "+dateCol+"<<>>"+c.getLong(iDate));
            		   if (dateCol<d2.getTime() && dateCol>d1.getTime())
            		   {
            			   retorno++;
            		   }
            		   
            	   } while (c.moveToNext());
               }
               c.close();
           }
           catch (Exception e)
           {
               System.out.println("* Excepción: "+e.toString());
           }
           
       }
		
        return retorno;
    }
    
    //***************************************************
	// FUNCIÓN PRINCIPAL
	//***************************************************
    
    public void listado (int mes){
    	//List<IconoYTexto> lista = new ArrayList<IconoYTexto>();
    	lista.clear();
    	listaInvertida.clear();
    	gpn.clear(); //limpiamos los gastos por numero (gpn)
    	gph.clear();
       Cursor c; //Cursor con el que recorreremos la base de datos de registros de llamadas
       if (mes==0) //Consulta de todo el año.
       {
    	   c=this.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, CallLog.Calls.TYPE+"="+CallLog.Calls.OUTGOING_TYPE , null, "date ASC");
       }
       else
       {    	   
    	   
       /*Vamos a hacer una consulta de un mes concreto*/
    	/*De momento, para las pruebas, estamos utilizando 2009, pero hay que utilizar el año en curso, el mes, ... */
    	   // Seleccionado un mes, tenemos que saber a que año corresponde
    	   int valorIncioMes=vp.getPreferenciasInicioMes();
    	   Date hoy=new Date();
    	   int anyo=hoy.getYear()+1900;
    	   int mesActual=hoy.getMonth()+1;
    	   if (mesActual<mes)
    	   {
    		   anyo--;
    	   }
           Calendar c1=new GregorianCalendar(anyo,mes-1,valorIncioMes,0,0);
           Calendar c2=new GregorianCalendar(anyo,mes,valorIncioMes,0,0);
           
           Date d1=new Date();
           Date d2=new Date();
           d1=c1.getTime();
           d2=c2.getTime();
           
           //Hacemos la consulta
           //Log.d(TAG,"fecha D1="+d1);
           //Log.d(TAG,"fecha D2="+d2);
           c=this.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, CallLog.Calls.DATE+"<="+(d2.getTime())+" and "+CallLog.Calls.DATE+">="+(d1.getTime())+" and "+CallLog.Calls.TYPE+"="+CallLog.Calls.OUTGOING_TYPE , null, "date ASC");      
       }

        this.startManagingCursor(c);
        
        int iTelefono = c.getColumnIndex(CallLog.Calls.NUMBER);
        int iFecha = c.getColumnIndex(CallLog.Calls.DATE);
        int iDuracion = c.getColumnIndex(CallLog.Calls.DURATION);
        int iNombre = c.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int iLabel = c.getColumnIndex(CallLog.Calls.CACHED_NUMBER_LABEL);
        //int iType = c.getColumnIndex(CallLog.Calls.CACHED_NUMBER_TYPE);
        
        
        int numSMS=0;
        int numSMSGratis=0;
        //int modifDuracion=getPreferenciasDuracion(); //modificación de la duración de la llamada
        int modifDuracion=vp.getPreferenciasDuracion(); //modificación de la duración de la llamada
        costeLlamadas=0;
        costeSMS=0;
        double coste=0.0;
        double costeDia=0.0;
        double estLlamada=0;
        numLlamadas=0;
        double totalEstLlamadas=0;
        String fechaControl="01/01/1000";
		tarifa t=null;
		Franja f=null;
		Drawable rIcono = null;
        //boolean sw_limite=false;
        
        //Si hay algún elemento
        AdaptadorListaIconos ad2 = new AdaptadorListaIconos(this,listaInvertida);
        setListAdapter(ad2);
        c.moveToFirst();
        totalRegistros=c.getCount();
        ts.resetSegundos();
        if (c.isFirst()&&ts.numTarifas()>0)
        {
        	//Recorrer todos los elementos de la consulta del registro de llamadas.
        	//Antes de recorrer todos los elementos, comprobamos que hay tarifa por defecto asignada.
        	//sino asignamos una
        	//Log.d("gastosMovil","Numero de tarifas por defecto="+ts.getNumTarifasDefecto());
        	if (ts.getNumTarifasDefecto()==0)
        	{
        		//Vamos hacer un dialogo para que el usuario pueda elegir la tarifa por defecto que prefiera
        		//Log.d("gastosMovil","Abrir Dialogo");
    			AlertDialog.Builder builder = new AlertDialog.Builder(this);
    			builder.setTitle("Selecciona Tarifa por defecto");
    			final String[] opciones= ts.getNombresTarifas();
    			builder.setItems(opciones, new DialogInterface.OnClickListener() {
    			    public void onClick(DialogInterface dialog, int item) {
    			    	
    			        //Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
    			    	ts.getTarifas().get(item).setDefecto(true);
    			    	listado(vp.getPreferenciasMes());
    			    }
    			});
    			AlertDialog alert = builder.create();
    			alert.show();
        	}
        		
        	do{
        		
        		String telefono=FunGlobales.quitarPrePais(c.getString(iTelefono));
        		String nombreLlamada=c.getString(iNombre);
        		long fecha=c.getLong(iFecha);
        		int duracion=c.getInt(iDuracion)+modifDuracion; //le añadimos la modificación de la duración de la llamada;
        		String viber=c.getString(iLabel);
        		//Log.d("gastosMovil.java", "CACHE_NUMBER_LABEL="+c.getString(iLabel));
        		
        		String sDuracion;
        		
        		String fechaHora=DateFormat.format("dd/MM/yyyy kk:mm:ss",new Date(fecha)).toString();
        		String fechaHoy=fechaHora.substring(0, 10).trim();
        		//Si la duración es mayor que la duración modificada, se hace los cálculos.
        		if (duracion>modifDuracion)
        		{
	        		//t=tarifa a la que pertenece el número
	        		t=ts.getTarifa(telefono,fechaHora);
	        		//Si t=null no hay tarifa para el teléfono,fechayhora
	        		if (t!=null)
	        		{
	        			f=t.getFranja(fechaHora);
	        			//Si no tiene franja y t no es por defecto
	        			if (f==null&&!t.getDefecto())
	            		{
	        				//Forzamos a que compruebe con la que este definida 'por defecto'
	            			t=ts.getTarifa("999999999",fechaHora); 
	            			if (t!=null)
	                			f=t.getFranja(fechaHora);
	            		}
	        		}
	        		
	        		if (f!=null&&t!=null)
	        		{
	        			//El día y la hora pertenece a una franja
		        		//Log.d(TAG,"Nombre de la franja="+f.getNombre()+" -> Fecha y hora"+fechaHora);
		        		//Añadimos los acumulados de tiempo que se pueden añadir en este punto
	        			//Me esta dando un error en la consola del market. No se porque, pongo un try
	        			try
	        			{
			        		t.addSegConsumidosMes(duracion);
			        		f.addSegundosConsumidos(telefono, duracion);
	        			}catch (Exception e){
	        				Log.e("Gastos Movil","t.addSegConsumidosMes(duracion):"+e.getMessage());
	        				duracion=0;
	        			}
		        		//Comprobamos si estamos en el mismo día
		        		if (fechaControl.compareTo(fechaHoy)!=0)
		        		{
		        			//No estamos en el mismo día
		        			//Incluimos el resumen del día si se ha seleccionado en ajustes y si seg. consumidos del día > 1
		        			//Log.d("GAstos Móvil", "Segundos consumidos día="+ts.getSegConsumidosDia());
		        			if (ts.getSegConsumidosDia()>1&&vp.getResumenDia())
		        			{
		        				//Resumen día
		        				lista.add(new IconoYTexto(getResources().getDrawable(android.R.drawable.presence_away), " "," ", fechaControl+" "+FunGlobales.diaSemana(fechaControl),(ts.getSegConsumidosDia()/60)+"m."+(ts.getSegConsumidosDia()%60)+"s. ",FunGlobales.redondear((costeDia*iva),vp.getPreferenciasDecimales())));
		        			}
		        			//final MODIFICACIÓN POR CONFIRMAR
		        			ts.resetSegundosConsumidosDia();
		        			t.setSegConsumidosLimiteDia(0); //Se resetea
		        			costeDia=0.0;
		        			t.setSegConsumidosDia(duracion); //Segundos consumidos solo los de hoy
		        			fechaControl=fechaHoy;
		        		}
		        		else
		        		{
		            		
		            		t.addSegConsumidosDia(duracion);
		        		}
		        		//Añadimos los acumulados de limite, si la llamada esta en la franja del limite
		        		if (f.getLimite())
		        		{
		        			//La franja cuenta para el límite
		        			//Comprobar si la tarifa tiene limite mensual
		        			if (t.getLimite()>0)
		        			{
		        				//Cuenta, añadirlo al contador de limite mensual
		        				t.addSegConsumidosLimiteMes(duracion);
		        				
		        			}
		        			//Comprobar si la tarifa tiene limite diario
		        			if (t.getLimiteDia()>0)
		        			{
		        				//Cuenta, añadirlo al contador de limite diario
		        				t.addSegConsumidosLimiteDia(duracion);	
		        			}
		        			
		        		}
		        		
		        		coste=f.coste(t, duracion); //Coste sin iva
		        		costeDia+=coste;
		        		estLlamada=f.establecimiento(t);
		        		
		        		if (
	        				((t.getSegConsumidosLimiteDia()>t.getLimiteDia()*60)&&t.getLimiteDia()>0)||
	        				((t.getSegConsumidosLimiteMes()>t.getLimite()*60)&&t.getLimite()>0)||
	        				((duracion>t.getLimiteLlamada()*60)&&t.getLimiteLlamada()>0))
		        		{
		        			rIcono=vp.getColorIcon(t.getColor(),"relog_peligro",display.getHeight());
		        		}
		        		else
		        		{
		        			if (f.getLimite()) rIcono=vp.getColorIcon(t.getColor(),"relog_mas",display.getHeight()); 
		        			else rIcono=vp.getColor(t.getColor());
		        		}
  
	        			//Ya tenemos el coste y el numero y es una llamada > 0, lo metemos en GastosPorNumero
	            		gpn.add(telefono, coste,duracion);
	            		gph.add(new Date(fechaHora), coste);
	            		
	        			if (coste>0)
	        			{
	        				//estLlamada=(vp.getPreferenciasEstLlamadas()/coste)*100;
	        				estLlamada=(estLlamada/coste)*100;
	        				totalEstLlamadas=totalEstLlamadas+estLlamada;
	        			}
	        			sDuracion=(duracion/60)+"m."+(duracion%60)+"s.";
	        			
	        			if (vp.getEstablecimiento())
	        				fechaHora=fechaHora+" | "+FunGlobales.redondear(estLlamada,0)+"%";
	        			String nombre="";
	        			if (vp.getNombreAgenda()&&(nombreLlamada!=null))
	        				nombre=""+nombreLlamada;
	        				//nombre=getContactNumber(telefono);
	        			lista.add(new IconoYTexto(rIcono, telefono,nombre, fechaHora,sDuracion,FunGlobales.redondear((coste*iva),vp.getPreferenciasDecimales())));
	        			//valores acumulados
	        			costeLlamadas=costeLlamadas+coste;
	        			numLlamadas++;
	        		}
		    		else
		    		{
		    			//El día y la hora no pertenecen a ninguna franja
		    			rIcono=vp.getColor("Transparente");
		    			lista.add(new IconoYTexto(rIcono, telefono,"SIN FRANJA", fechaHora,(duracion/60)+"m."+(duracion%60)+"s.",-1.0));
		    			
		    		}
	        	}
        		
        	} while (c.moveToNext());
        	
        	//Incluimos el resumen de tiempos del último día
        	if (ts.getNumTarifasDefecto()>0)  // hay tarifa por defecto definida (java.lang.NullPointerException)
        		if (ts.getSegConsumidosDia()>1&&vp.getResumenDia()) //Si es igual a 1 seg. que no salga en el resumen del día y esta activado el resumen del día en los ajustes        			
        		{
        				lista.add(new IconoYTexto(getResources().getDrawable(android.R.drawable.presence_away), " "," ", fechaControl+" "+FunGlobales.diaSemana(fechaControl),(ts.getSegConsumidosDia()/60)+"m."+(ts.getSegConsumidosDia()%60)+"s.",FunGlobales.redondear((costeDia*iva),vp.getPreferenciasDecimales())));
					//lista.add(new IconoYTexto(rIcono, " "," ", fechaControl,(t.getSegConsumidosDia()/60)+"m."+(t.getSegConsumidosDia()%60)+"s.",0.0));
        		}
        c.close();
        }
        
        //Resumen de datos del mes seleccionado g 
        String textoMes="";
        if (vp.getPreferenciasMes()>0) 
        {
        	String meses[] = getResources().getStringArray(R.array.listaMeses);
        	textoMes=meses[vp.getPreferenciasMes()+1];
        	textoMes=FunGlobales.periodo(meses, vp.getPreferenciasMes()+1, vp.getPreferenciasInicioMes());
        }
        
        //LinearLayout lyt_cabRegistro=(LinearLayout) findViewById(R.id.lytCabResumen);
        //TextView tv_cabResumen=(TextView) findViewById(R.id.cabResumen);
        
        //Mostrando los datos en la cabecera de resumen de datos
        
        //tv_cabResumen.setText(R.string.cabDatos);

        //Mostrando los datos de minutos llamadas en la cabecera de registro de llamadas
        
        LinearLayout linear=(LinearLayout) findViewById(R.id.lytResumenMesDia);
        linear.removeAllViewsInLayout();
        
        
  	  	
  	  	//LLAMADAS
  	  	TextView txtLlamadas = new TextView(this,null,android.R.attr.textAppearanceSmall);
  	  	txtLlamadas.setTextSize(15);
  	  	txtLlamadas.setTypeface(Typeface.MONOSPACE);
  	  	txtLlamadas.setTextColor(0xff000000);
	  	linear.addView(txtLlamadas, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	  	
	  	//SMS
  	  	TextView txtSMS = new TextView(this,null,android.R.attr.textAppearanceSmall);
  	  	txtSMS.setTextSize(15);
  	  	txtSMS.setTextColor(0xff000000);
  	  	txtSMS.setTypeface(Typeface.MONOSPACE);
  	  	linear.addView(txtSMS, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
  	  	
  	  	//Separador
  	  	ImageView separador1 = new ImageView(this);
  	  	separador1.setImageDrawable(getResources().getDrawable(android.R.drawable.divider_horizontal_bright));
  	  	separador1.setPadding(0, 5, 0, 5);
  	  	linear.addView(separador1,  new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

	  	//TARIFAS
	  	TextView txtTarifas = new TextView(this,null,android.R.attr.textAppearanceSmall);
	  	txtTarifas.setTextSize(15);
	  	txtTarifas.setTextColor(0xff000000);
	  	txtTarifas.setTypeface(Typeface.MONOSPACE);
	  	linear.addView(txtTarifas, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	  	
    	String datosMes="";
    	String datosDia="";
    	int numLineasMes=0;
    	int numLineasDia=0;

		datosMes+=getString(R.string.res_mes)+"\n";
		datosMes+="------------\n";
		datosDia+="\n"+getString(R.string.res_dia)+" "+fechaControl+"\n";
		datosDia+="-----------------------\n";
		numLineasMes+=2;
		numLineasDia+=3;
		
    	for (int i=0;i<ts.numTarifas();i++)
    	{
    		String nombreTarifa=ts.getTarifas().get(i).getNombre();

    		if (nombreTarifa.length()>28)
    		{
    			datosMes+=" "+nombreTarifa.substring(0, 28)+".\n";
    			datosDia+=" "+nombreTarifa.substring(0, 28)+".\n";
    		}	
    		else
    		{
    			datosMes+=" "+nombreTarifa+"\n";
    			datosDia+=" "+nombreTarifa+"\n";
    		}
    		
    		//Datos mensual
    		if (ts.getTarifas().get(i).getLimite()>0)
    		{
    			//Tiene limite mensual Html.fromHtml("<font color='red'>@</font>")
    			
   				datosMes+="  * "+(ts.getTarifas().get(i).getSegConsumidosLimiteMes()/60)+"m. "+(ts.getTarifas().get(i).getSegConsumidosLimiteMes()%60)+"s.|"+getString(R.string.res_limite_mes)+" "+ts.getTarifas().get(i).getLimite()+"m.\n";
        		datosMes+="  * "+(ts.getTarifas().get(i).getSegConsumidosMes()-ts.getTarifas().get(i).getSegConsumidosLimiteMes())/60+"m. "+(ts.getTarifas().get(i).getSegConsumidosMes()-ts.getTarifas().get(i).getSegConsumidosLimiteMes())%60+"s.|"+getString(R.string.res_fuera_limite)+"\n";
        		numLineasMes+=3;
    		}
    		else
    		{
    			datosMes+="  * "+(ts.getTarifas().get(i).getSegConsumidosMes()/60)+"m. "+(ts.getTarifas().get(i).getSegConsumidosMes()%60)+"s.|"+getString(R.string.res_sin_limite)+"\n";
    			numLineasMes+=2;
    			//Log.d("gastosmovil.java","CM:"+ts.getTarifas().get(i).getSegConsumidosMes());
    		}
    		
    		//Datos del último día
    		if (ts.getTarifas().get(i).getLimiteDia()>0)
    		{
    			//Tiene limite mensual
        		datosDia+="  * "+(ts.getTarifas().get(i).getSegConsumidosLimiteDia()/60)+"m. "+(ts.getTarifas().get(i).getSegConsumidosLimiteDia()%60)+"s.|"+getString(R.string.res_limite_dia)+" "+ts.getTarifas().get(i).getLimiteDia()+"m.\n";
        		datosDia+="  * "+(ts.getTarifas().get(i).getSegConsumidosDia()-ts.getTarifas().get(i).getSegConsumidosLimiteDia())/60+"m. "+(ts.getTarifas().get(i).getSegConsumidosDia()-ts.getTarifas().get(i).getSegConsumidosLimiteDia())%60+"s.|"+getString(R.string.res_fuera_limite)+"\n";
        		numLineasDia+=3;
        		//Log.d("gastosmovil.java","CLM:"+ts.getTarifas().get(i).getSegConsumidosLimiteMes()+" CM:"+ts.getTarifas().get(i).getSegConsumidosMes()+" LM:"+ts.getTarifas().get(i).getSegConsumidosLimiteMes());
        		//tv_cabRegistro.setText(ts.getTarifas().get(i).getNombre()+":"+getString(R.string.Gastado)+" "+(totalSegundosLimite/60)+" m. "+(totalSegundosLimite%60)+" s. "+ getString(R.string.Limite)+" "+limite+" m.");//TEXTO
        		//tv_cabRegistro.setText(ts.getTarifas().get(i).getNombre().subSequence(0, 10)+":"+getString(R.string.Gastado)+" "+(ts.getTarifas().get(i).getSegConsumidosMes()/60)+" m. "+(ts.getTarifas().get(i).getSegConsumidosMes()%60)+" s. "+ getString(R.string.Limite)+" "+ts.getTarifas().get(i).getLimite()+" m.");//TEXTO
    		}
    		else
    		{
    			datosDia+="  * "+(ts.getTarifas().get(i).getSegConsumidosDia()/60)+"m. "+(ts.getTarifas().get(i).getSegConsumidosDia()%60)+"s.|"+getString(R.string.res_sin_limite)+"\n";
    			numLineasDia+=2;
    			//Log.d("gastosmovil.java","CM:"+ts.getTarifas().get(i).getSegConsumidosMes());
    		}
    		
    		
    	}
    	txtTarifas.setLines(numLineasMes+numLineasDia);
    	txtTarifas.setText(datosMes+datosDia);
    	
    	
  	  	//Separador
  	  	ImageView separador2 = new ImageView(this);
  	  	separador2.setImageDrawable(getResources().getDrawable(android.R.drawable.divider_horizontal_bright));
  	  	separador2.setPadding(0, 5, 0, 5);
  	  	linear.addView(separador2,  new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	  	
	  	//Duración media por llamada
    	TextView txtMediaLlamadas = new TextView(this,null,android.R.attr.textAppearanceSmall);
    	txtMediaLlamadas.setTextSize(15);
    	txtMediaLlamadas.setTextColor(0xff000000);
    	txtMediaLlamadas.setTypeface(Typeface.MONOSPACE);
	  	linear.addView(txtMediaLlamadas, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	  	if (numLlamadas>0)
	  		txtMediaLlamadas.setText(getString(R.string.txtDuracionMedia)+FunGlobales.segundosAHoraMinutoSegundo(ts.getSegConsumidosMes()/numLlamadas));
	  	else
	  		txtMediaLlamadas.setText(getString(R.string.txtDuracionMedia));
	  	
	  	//Porcentaje establecimiento de llamada
    	TextView txtEstablecimiento = new TextView(this,null,android.R.attr.textAppearanceSmall);
    	txtEstablecimiento.setTextSize(15);
    	txtEstablecimiento.setTextColor(0xff000000);
    	txtEstablecimiento.setTypeface(Typeface.MONOSPACE);
	  	linear.addView(txtEstablecimiento, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        //-- Porcentaje del establecimiento de llamadas
        totalEstLlamadas=totalEstLlamadas/numLlamadas;
        txtEstablecimiento.setText(getString(R.string.res_establecimiento)+FunGlobales.redondear(totalEstLlamadas,0)+"%");
        
        //Numero de llamadas, coste de las llamadas
        txtLlamadas.setText(getString(R.string.res_llamadas) +" ("+numLlamadas+")..."+FunGlobales.redondear(costeLlamadas*iva,2)+FunGlobales.monedaLocal());
        

        //Calculamos el coste de los SMS
        numSMS=getNumSMS_send();
        numSMSGratis=vp.getPreferenciasSMSGratuitos();
        if (numSMS>numSMSGratis)
        	costeSMS=FunGlobales.redondear(vp.getPreferenciasTarifaSMS(),2)*(numSMS-numSMSGratis); //Mas SMS enviados que los gratuitos
        else
        	costeSMS=0; //Se han enviados menos SMS que los que hay gratuitos
        
        txtSMS.setText(getString(R.string.res_mensajes)+" ("+numSMS+")..."+FunGlobales.redondear(costeSMS*iva,2)+FunGlobales.monedaLocal());
        //Log.d("gastosMovil","coste+sms="+FunGlobales.redondear(costeLlamadas+costeSMS,2));
        
        //MOntamos el literal que se va ha presentar en el desplegable
        TextView txtMes=(TextView) findViewById(R.id.txtPersiana);
  	  	txtMes.setText(getString(R.string.cabDatos) +" "+textoMes+" ("+FunGlobales.redondear((costeLlamadas+costeSMS)*iva,2)+FunGlobales.monedaLocal()+")");
  	  	//Montamos la señal de estado
  	  	ImageView imgEstado=(ImageView) findViewById(R.id.imagePersianaEstado);
  	  	//Log.d("gastosMovil.java",""+ts.getEstadoLimite());
  	  	switch (ts.getEstadoLimite())
  	  	{
  	  	case 0: //Imagen vacia
  	  		imgEstado.setImageDrawable(getResources().getDrawable(R.drawable.line0));
  	  		break;
  	  	case 1: //Imagen verde
  	  		imgEstado.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_online));
  	  		break;
  	  	case 2: //Imagen roja
  	  		imgEstado.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_busy));
  	  		break; 
  	  	}

        //Hay que invertir la lista de llamadas, para presentarlo en pantalla y que apareccan
        //listaInvertida=lista;
        for (int a=lista.size()-1;a>=0;a--)
        	listaInvertida.add(lista.get(a));
        //if (lista.size()>0)
        //	listaInvertida.add(new IconoYTexto(vp.getColor("Transparente"), " "," ", " "," ",0.0));
        //dialog.dismiss();
        AdaptadorListaIconos ad = new AdaptadorListaIconos(this,listaInvertida);
        setListAdapter(ad);
        
        /*Añadir menu contextual */
        
        ListView listallamadas=(ListView) this.findViewById(android.R.id.list);

        
        registerForContextMenu(listallamadas); //--- Registrar el menu contextual sobre el listview listallamadas
    } //-- Fin de la función principal
    
    
// MANEJADORES DE EVENTOS    
    
    //--- Menú contextual
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    		AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
    	  	menu.setHeaderTitle(R.string.mnctx_titulo);
    	  	ArrayList <tarifa> tarifas=ts.getTarifas();
    	  	//Log.d("gastosMovil","Telefono=["+listaInvertida.get(info.position).telefono.trim()+"]");
    	  	if (!(listaInvertida.get(info.position).telefono.trim().equals("")))
    	  	{	
	    	  	for (int a=0;a<tarifas.size();a++)
	    	  	{
	    	  		//La tarifa por defecto no debe aparecer en el menú contextual
	    	  		if (!tarifas.get(a).getDefecto())
	    	  		{
	    	  			//Log.d(TAG,"Pertenece "+listaInvertida.get(info.position).telefono+" a "+tarifas.get(a).getNombre()+" -- "+tarifas.get(a).pertenece(listaInvertida.get(info.position).telefono));
	    	  			if (tarifas.get(a).pertenece(listaInvertida.get(info.position).telefono))
	    	  			{
	        	  			//Ya está en esa tarifa. Hay que darle opción de eliminar
	        	  			//menu.add(0, tarifas.get(a).getIdentificador(), 0, "Eliminar de "+tarifas.get(a).getNombre());
	    	  				menu.clear();
	    	  				menu.add(0, tarifas.get(a).getIdentificador(), 0, "Eliminar de "+tarifas.get(a).getNombre());
	    	  				a=tarifas.size()+1;
	    	  			}
	    	  			
	        	  		else
	        	  			menu.add(0, tarifas.get(a).getIdentificador(), 0, "Añadir a "+tarifas.get(a).getNombre());
	    	  		}
	    	  	}
	    	  	if (menu.size()==0)
	    	  	{
	    	  		Toast.makeText(getApplicationContext(), "No existen tarifas seleccionable.", Toast.LENGTH_SHORT).show();
	    	  	}
    	  	}
    }
    
    //--- Eventos del menu contextual
    public boolean onContextItemSelected(MenuItem item) {
    	  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	  
    	  //REVISAR. 
    	  tarifa t=ts.getTarifa(item.getItemId());
    	  String tlf=listaInvertida.get(info.position).telefono;
    	  
    	  if (t.pertenece(tlf))
    	  {
    		  //Eliminar
    		  t.deleteNumero(tlf);
    		  listado(vp.getPreferenciasMes());
    	  }
    	  else
    	  {
    		  //Añadir
    		  t.addNumero(tlf);
    		  listado(vp.getPreferenciasMes());
    	  }
    	  ts.guardarTarifas();
    	  return true;
    	  
    	}
    
  //--- Eventos de devolución de parámetros de preferencias.class
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	super.onActivityResult(requestCode, resultCode, data);
    	//Log.v("Prueba", "RESULTADO(reques/result/OK)="+requestCode+"/"+resultCode+"/"+Activity.RESULT_OK+"/"+Activity.RESULT_CANCELED);
    	switch (requestCode)
    	{
    	case RETURN_PREFERENCES_AJUSTES:
			if (resultCode == RESULT_OK) 
			{
			     //Ha cambiado el iva
			     if (iva!=vp.getPreferenciasImpuestos())
			     {
			    	 iva=vp.getPreferenciasImpuestos(); 
			    	 //ts.cambiarIva(iva);
			    	 //Log.d(TAG,"Cambiando el iva ="+iva);
			     }
			     
		        if (vp.getcosteConIVA())
		        	iva=vp.getPreferenciasImpuestos();
		        else
		        	iva=1.00; //Sin IVA
		        
			     listado(vp.getPreferenciasMes());
			     ts.guardarTarifas();
			}
    		break;
		case RETURN_PREFERENCES_TARIFAS:
			if (resultCode==Activity.RESULT_OK)
			{
				ts=(tarifas) data.getSerializableExtra(TARIFAS_RETORNO);
				//Log.d(TAG,"Retorno de preferencias tarifas ");
				//GUARDAR ts EN EL DOCUMENTO XML
				ts.guardarTarifas();
				listado(vp.getPreferenciasMes());
			}
			break;

		default:
			if (resultCode == Activity.RESULT_OK) 
			{
				String operadora=data.getStringExtra("operadora");

				if (operadora.equals("KPN"))
				{
					operadora="SIMYO";
				}
				Toast.makeText(getApplicationContext(), "Operadora - "+operadora, Toast.LENGTH_SHORT).show();
			}
			if (resultCode == Activity.RESULT_CANCELED) 
			{
				Toast.makeText(getApplicationContext(), "Error en el calculo de la operadora. Vuelva a intentarlo.", Toast.LENGTH_SHORT).show();
				//Log.v("Prueba", data.getStringExtra("operadora"));
			}
			break; 
    	
    	
    	}
    }
    
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		/*
		guardarPreferences(PREF_SEGUNDOS, ts.getSegConsumidosMes());
		ts.guardarTarifas();
		
		Intent forceUpIntent = new Intent(getBaseContext(), widgetProvider.UpdateService.class);
        getBaseContext().startService(forceUpIntent);
        
        */
		/*AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());
        RemoteViews remoteViews = new RemoteViews(getBaseContext().getPackageName(), R.layout.widget);
        ComponentName thisWidget = new ComponentName(getBaseContext(), widgetProvider.class);
        remoteViews.setTextViewText(R.id.txt_costeLlamadas, FunGlobales.redondear(costeLlamadas*iva,vp.getPreferenciasDecimales())+FunGlobales.monedaLocal());
        remoteViews.setTextViewText(R.id.txt_costeSMS, FunGlobales.redondear(costeSMS*iva,vp.getPreferenciasDecimales())+FunGlobales.monedaLocal());
        remoteViews.setTextViewText(R.id.txt_tiempo, Html.fromHtml("<font color='green'>"+FunGlobales.segundosAHoraMinutoSegundo(ts.getSegConsumidosMes())+"</font>"));*/
        
        /*SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCIAS_WIDGET, MODE_PRIVATE);
        String consumoDia = sharedPreferences.getString("consumoDia", "1");
        remoteViews.setTextViewText(R.id.txt_tiempoLimite, "D:"+consumoDia);*/
        //Añadir las vistas correspondientes a los consumos de las tarifas
        
        
        //appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		
	}
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//guardarPreferences(PREF_SEGUNDOS, ts.getSegConsumidosMes());
		ts.guardarTarifas();
		
		//Intent forceUpIntent = new Intent(getBaseContext(), widgetProvider.UpdateService.class);
        //getBaseContext().startService(forceUpIntent);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		
		
		//String numero=listaInvertida.get(position).telefono.trim();
		String action = "es.ieeesb.androidoperator.COMPRUEBA_NUM";
		String num=listaInvertida.get(position).telefono.trim();
		if (num.length()==0) return;
		
	   	AlertDialog.Builder dialogMarket = new AlertDialog.Builder(gastoMovil.this);
		
	   	//Comprobamos si AndroidOperator estça disponible en el dispositivo
		if (!FunGlobales.estaIntentDisponible(getApplicationContext(), action,Uri.parse("numero://telefono/")))
		{
			//No lo está. Damos opcińo a descargarlo del Market, si tiene el Market instalado
			//Comprobamos si el market esta instalado
			if (FunGlobales.estaMarketInstalado(getApplicationContext()))
			{
				//EL market esta disponible
				dialogMarket.setMessage(R.string.msg_dialog_market);
	    		dialogMarket.setPositiveButton("Si", new DialogInterface.OnClickListener() 
		       		{
	    				public void onClick(DialogInterface dialog, int id) 
	 		           	{
	 		        	   //Enlazar con el market
	 		        	   	dialog.cancel();
	 		        	   	Intent intent = new Intent(Intent.ACTION_VIEW);
	 		        	   	intent.setData(Uri.parse("market://details?id=es.ieeesb.androidoperator"));
	 		        	   	startActivity(intent);
	 		        	   
	 		           	}
		       		});
	    		dialogMarket.setNegativeButton("No", new DialogInterface.OnClickListener() 
	 		       {
	 		           public void onClick(DialogInterface dialog, int id) 
	 		           {
	 		                dialog.cancel();
	 		           }
	 		       });
			}
			else
			{
				//EL market no esta disponible
				dialogMarket.setMessage(R.string.msg_dialog_androidOperator);
				dialogMarket.setNeutralButton("OK", new DialogInterface.OnClickListener() 
		       		{
	    				public void onClick(DialogInterface dialog, int id) 
	 		           	{
	 		        	   //Enlazar con el market
	 		        	   	dialog.cancel();
	 		           	}
		       		});
				
				
			}
			
			//No existe el contexto
			
			
			dialogMarket.setCancelable(false);
    		dialogMarket.show();
    		return;
		}
		
		
		//Si no tenemos números
		
		//Si tiene código de pais, se lo quitamos
		if (num.length()==12)
		{
			//Puede ser +34
			
			num=num.substring(3);
		}
		if (num.length()==13)
		{
			//Puede ser 0034
			
			num=num.substring(4);
		}
		
		//Controlamos que sea un móvil
		if ((num.substring(0, 1).equals("9"))||(num.length()<6))
		{
			Toast.makeText(getApplicationContext(), getString(R.string.msg_dialog_soloMoviles), Toast.LENGTH_SHORT).show();
		}
		else
		{
			try
			{
				
		        String n= "numero://telefono/" + num;
		        Uri data = Uri.parse(n);
		        Intent numIntent = new Intent(action,data);
		        startActivityForResult(numIntent,0);
			}
			catch (Exception e)
			{
				Log.e("gastosMovil.java","Error:"+e.getMessage());
			}
		}
		super.onListItemClick(l, v, position, id);
	}


    
    
    
}
