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
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Contacts;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.LinearLayout.LayoutParams;

import java.util.Currency;
import java.util.Locale;

public class gastoMovil extends ListActivity {
    /** Called when the activity is first created. */
		
	private static final int TARIFAS = Menu.FIRST;
	private static final int AJUSTES = Menu.FIRST+1;
	private static final int FACTURA = Menu.FIRST+2;
	private static final int ESTADISTICAS = Menu.FIRST+3;
    private static final int ACERCADE=Menu.FIRST+4;
    private static final int SALIR = Menu.FIRST+5;
    
    private static final int RETURN_PREFERENCES_AJUSTES = 1;
    private static final int RETURN_PREFERENCES_TARIFAS=2;
    private static final String TARIFAS_RETORNO = "tarifas_retorno";
    private static final String TAG = "GastosMóvil";
    private double iva=1.18;
    String path="\\sdcard\\gastosmovil\\datosTarifas.xml";
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
    	menu.add(Menu.NONE, SALIR, 0, R.string.mn_salir).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
    	
    	
    	
    	return true;
    }
    
    @Override
    public void onCreate(Bundle icicle) {
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
        			e.printStackTrace();
        		}
        	}
        	
	        SAXParserFactory spf = SAXParserFactory.newInstance();
	        SAXParser sp = spf.newSAXParser();
	        /* Get the XMLReader of the SAXParser we created. */
	        XMLReader xr = sp.getXMLReader();
	        /* Create a new ContentHandler and apply it to the XML-Reader*/
	        TarifasParserXML tarifasXML = new TarifasParserXML();
	        tarifasXML.setTarifas(ts);
	        tarifasXML.setIva(iva);
	        xr.setContentHandler(tarifasXML);
	        xr.parse(new InputSource (new FileReader(path)));
	        /* Parsing has finished. */
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
        	extras.putString("tarifaDefecto", vp.getPreferenciasDefecto());
        	extras.putSerializable("tarifas", ts);
        	settingsActivity2.putExtras(extras);
        	//startActivity(settingsActivity2);
        	startActivityForResult(settingsActivity2, RETURN_PREFERENCES_TARIFAS);
        	break;
        	
        case SALIR:
        	finish();
            break;
            
        case ACERCADE:
        	//showDialog(DIALOG_ACERCADE);
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
        	
        	String total= FunGlobales.redondear(costeLlamadas+costeSMS,2)+"";
        	
        	numeros=(ArrayList<String>) gpn.getNumeros();
        	Object ObjGastos[]=gpn.getGastos().toArray();
        	for (Object x:ObjGastos){
        		gastos.add(""+x);
        	}
        	
        	horas=(ArrayList<String>) gph.gethoras();
        	Object ObjGastos2[]=gph.getGastos().toArray();
        	for (Object x:ObjGastos2){
        		gastos2.add(""+x);
        	}
        	ii.putExtra("total",total);
        	ii.putExtra("Numeros", numeros);
        	ii.putExtra("Gastos",gastos);
        	ii.putExtra("Horas", horas);
        	ii.putExtra("Gastos2",gastos2);
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
    	   c=this.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, CallLog.Calls.TYPE+"="+CallLog.Calls.OUTGOING_TYPE , null, CallLog.Calls.DEFAULT_SORT_ORDER);
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
        int numSMS=0;
        int numSMSGratis=0;
        //int modifDuracion=getPreferenciasDuracion(); //modificación de la duración de la llamada
        int modifDuracion=vp.getPreferenciasDuracion(); //modificación de la duración de la llamada
        costeLlamadas=0;
        costeSMS=0;
        double coste;
        double estLlamada=0;
        numLlamadas=0;
        double totalEstLlamadas=0;
        String fechaControl="01/01/1000";
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
        	do{
        		Drawable rIcono = null;
        		String telefono=c.getString(iTelefono);
        		//String telefono="xxxyyyyyy";
        		long fecha=c.getLong(iFecha);
        		int duracion=c.getInt(iDuracion)+modifDuracion; //le añadimos la modificación de la duración de la llamada;
        		String sDuracion;
        		
        		String fechaHora=DateFormat.format("dd/MM/yyyy kk:mm:ss",new Date(fecha)).toString();
        		String fechaHoy=fechaHora.substring(0, 10).trim();
        		
        		//t=tarifa a la que pertenece el número
        		tarifa t=ts.getTarifa(telefono,vp.getPreferenciasDefecto());
        		Franja f=t.getFranja(fechaHora);
        		
        		if (f!=null)
        		{
        			//El día y la hora pertenece a una franja
	        		//Log.d(TAG,"Nombre de la franja="+f.getNombre()+" -> Fecha y hora"+fechaHora);
	        		//Añadimos los acumulados de tiempo que se pueden añadir en este punto
	
	        		t.addSegConsumidosMes(duracion);
	        		//Comprobamos si estamos en el mismo día
	        		if (fechaControl.compareTo(fechaHoy)!=0)
	        		{
	        			//No estamos en el mismo día
	        			//Log.d(TAG,"Cambio de día, hoy es "+fechaHoy+". Y en el día de ayer se consumio "+t.getSegConsumidosDia()+"min., de los cuales del límite eran "+t.getSegConsumidosLimiteDia()+" min.");
	        			t.setSegConsumidosDia(duracion); //Segundos consumidos solo los de hoy
	        			t.setSegConsumidosLimiteDia(0); //Se resetea
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
	        		estLlamada=f.establecimiento(t);
	        		
	        		if ((t.getSegConsumidosLimiteDia()>t.getLimiteDia()*60)||(t.getSegConsumidosLimiteMes()>t.getLimite()*60))
	        		{
	        			rIcono=vp.getColorIcon(t.getColor(),"relog_peligro",display.getHeight());
	        		}
	        		else
	        		{
	        			if (f.getLimite()) rIcono=vp.getColorIcon(t.getColor(),"relog_mas",display.getHeight()); 
	        			else rIcono=vp.getColor(t.getColor());
	        		}
	        		
	        		
	        		
	        		
	        		/***************************************************************
	        		//Array de retorno con
	        		//COSTE=0;ESTABLECIMIENTO=1;GASTOMINIMO=2;LIMITE=3;COSTE_FUERA_LIMITE=4;ESTABLECIMIENTO_FUERA_LIMITE=5;
	        		retorno=ts.costeLlamada(telefono,fechaHora, duracion,vp.getPreferenciasDefecto());
	        		rIcono=vp.getColor(t.getColor());
	        		
	        		//Solo se acumula el limite de tiempo cuando el limite retornado sea > 0.0, es decir cuenta para el limite
	
	        		totalSegundos=totalSegundos+duracion;
	        		t.addSegConsumidosMes(duracion);
	        		if (retorno[LIMITE]!=0.0)
	        		{
	        			
	        			//t.addSegConsumidosDia(duracion);
	        			t.addSegConsumidosLimiteMes(duracion);
	        			totalSegundosLimite=totalSegundosLimite+duracion;
	        			limite=retorno[LIMITE];
	        		}
	        		
	        		//El coste ha mostrar dependera del limite
	        		if ((retorno[LIMITE]*60)>=totalSegundosLimite || retorno[LIMITE]==0.0)
	        		{
	        			coste=retorno[COSTE]; //limite < tiempo hablado
	        			estLlamada=(((retorno[ESTABLECIMIENTO]/100)*iva)/coste)*100;
	        		}
	        		else
	        		{
	        			rIcono=getResources().getDrawable(android.R.drawable.presence_busy);
	        			coste=retorno[COSTE_FUERA_LIMITE]; //limite > tiempo hablado
	        			estLlamada=(((retorno[ESTABLECIMIENTO_FUERA_LIMITE]/100)*iva)/coste)*100;
	        		} 
	
	        		//estLlamada=f.getEstablecimiento();
	********************************************************************/
					
	        		//Esta condición habrá que ponerla antes, para no tener que hacer los calculos, sino es necesario.
	        		if (duracion>modifDuracion)
	        		{  
	        			//Ya tenemos el coste y el numero y es una llamada > 0, lo metemos en GastosPorNumero
	            		gpn.add(telefono, coste);
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
	        			if (vp.getNombreAgenda())
	        				nombre=getContactNumber(telefono);
	        			
	        			lista.add(new IconoYTexto(rIcono, telefono,nombre, fechaHora,sDuracion,FunGlobales.redondear((coste*iva),vp.getPreferenciasDecimales())));
	        			//valores acumulados
	        			costeLlamadas=costeLlamadas+coste;
	        			numLlamadas++;
	        		}
        		}
	    		else
	    		{
	    			//El día y la hora no pertenecen a ninguna franja
	    			lista.add(new IconoYTexto(rIcono, telefono,"Sin Franja", fechaHora,(duracion/60)+"m."+(duracion%60)+"s.",-1.0));
	    			
	    		}
        			
        		
        	} while (c.moveToNext());
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
        
        LinearLayout lyt_cabRegistro=(LinearLayout) findViewById(R.id.lytCabResumen);
        TextView tv_cabResumen=(TextView) findViewById(R.id.cabResumen);
        
        //Mostrando los datos en la cabecera de resumen de datos
        
        tv_cabResumen.setText(R.string.cabDatos);
        
        
        lyt_cabRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Log.d(TAG, "Se ha pulsado texto registro de llamadas");
            	LinearLayout linear2=(LinearLayout) findViewById(R.id.lytAreaResumen);
            	ImageView preImagen=(ImageView) findViewById(R.id.preImagen);
            	ImageView postImagen=(ImageView) findViewById(R.id.postImagen);
            	 
            	int altoPantalla = display.getHeight();
            	if (linear2.getHeight()==0)
            	{
            		linear2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,(altoPantalla/2)-60));
            		preImagen.setImageResource(android.R.drawable.arrow_up_float);
            		postImagen.setImageResource(android.R.drawable.arrow_up_float);
            	}
            	else
            	{
            		linear2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,0));
            		preImagen.setImageResource(android.R.drawable.arrow_down_float);
            		postImagen.setImageResource(android.R.drawable.arrow_down_float);
            	}
            }
        });
        
        
        
        //Mostrando los datos de minutos llamadas en la cabecera de registro de llamadas
        
        LinearLayout linear=(LinearLayout) findViewById(R.id.lytResumen);
        linear.removeAllViewsInLayout();
        linear.setBackgroundResource(android.R.drawable.dialog_frame);
        //Mostrar resumen de datos
        //MES
        TextView txtMes = new TextView(this,null,android.R.attr.textAppearanceSmall);
  	  	txtMes.setTextSize(15);
  	  	txtMes.setTypeface(Typeface.MONOSPACE);
  	  	txtMes.setGravity(android.view.Gravity.CENTER);
  	  	linear.addView(txtMes, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,2));
  	  	//txtMes.setText(textoMes+"..."+FunGlobales.redondear(costeLlamadas+costeSMS,2)+FunGlobales.monedaLocal());
  	  	txtMes.setText(textoMes);
  	  	//Separador
  	  	ImageView separador = new ImageView(this);
  	  	separador.setImageDrawable(getResources().getDrawable(android.R.drawable.divider_horizontal_dim_dark));
  	  	separador.setPadding(0, 5, 0, 5);
  	  	linear.addView(separador,  new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
  	  	//LLAMADAS
  	  	TextView txtLlamadas = new TextView(this,null,android.R.attr.textAppearanceSmall);
  	  	txtLlamadas.setTextSize(15);
  	  	separador.setPadding(10, 0, 10, 0);
  	  	txtLlamadas.setTypeface(Typeface.MONOSPACE);
	  	linear.addView(txtLlamadas, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,2));
	  	//IMAGEN and TEXTO
	  	/*LinearLayout lytSMS = new LinearLayout(this);
	  	lytSMS.setOrientation(LinearLayout.HORIZONTAL);
	  	//IMAGENES SMS
        ImageView imgSMS = new ImageView(this);
        imgSMS.setImageDrawable(this.getResources().getDrawable(android.R.drawable.sym_action_email));
        imgSMS.setPadding(0, 0, 0, 0);
        lytSMS.addView(imgSMS,  new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));*/
	  	//SMS
  	  	TextView txtSMS = new TextView(this,null,android.R.attr.textAppearanceSmall);
  	  	txtSMS.setTextSize(15);
  	  	txtSMS.setTypeface(Typeface.MONOSPACE);
  	  	linear.addView(txtSMS, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
  	  	
  	  	//Separador
  	  	ImageView separador1 = new ImageView(this);
  	  	separador1.setImageDrawable(getResources().getDrawable(android.R.drawable.divider_horizontal_dim_dark));
  	  	separador1.setPadding(0, 5, 0, 5);
  	  	linear.addView(separador1,  new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

	  	//TARIFAS
	  	TextView txtTarifas = new TextView(this,null,android.R.attr.textAppearanceSmall);
	  	txtTarifas.setTextSize(15);
	  	separador.setPadding(0, 5, 0, 5);
	  	txtTarifas.setTypeface(Typeface.MONOSPACE);
	  	//txtTarifas.setBackgroundResource(android.R.drawable.dialog_frame);
	  	linear.addView(txtTarifas, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,2));
    	//tv_cabRegistro.setText(getString(R.string.Gastado)+" "+(totalSegundosLimite/60)+" m. "+(totalSegundosLimite%60)+" s. "+ getString(R.string.Limite)+" "+limite+" m.");//TEXTO
    	String datos="";
    	int numLineas=0;
    	for (int i=0;i<ts.numTarifas();i++)
    	{
    		String nombreTarifa=ts.getTarifas().get(i).getNombre();
    		if (nombreTarifa.length()>28)
    			datos+=" "+nombreTarifa.substring(0, 28)+".\n";
    		else
    			datos+=" "+nombreTarifa+"\n";
    		
    		if (ts.getTarifas().get(i).getLimite()>0)
    		{
    			
        		datos+="  * "+(ts.getTarifas().get(i).getSegConsumidosLimiteMes()/60)+"m. "+(ts.getTarifas().get(i).getSegConsumidosLimiteMes()%60)+"s.|Límite de "+ts.getTarifas().get(i).getLimite()+"m.\n";
        		datos+="  * "+(ts.getTarifas().get(i).getSegConsumidosMes()-ts.getTarifas().get(i).getSegConsumidosLimiteMes())/60+"m. "+(ts.getTarifas().get(i).getSegConsumidosMes()-ts.getTarifas().get(i).getSegConsumidosLimiteMes())%60+"s.|Fuera de límite.\n";
        		numLineas+=3;
        		//tv_cabRegistro.setText(ts.getTarifas().get(i).getNombre()+":"+getString(R.string.Gastado)+" "+(totalSegundosLimite/60)+" m. "+(totalSegundosLimite%60)+" s. "+ getString(R.string.Limite)+" "+limite+" m.");//TEXTO
        		//tv_cabRegistro.setText(ts.getTarifas().get(i).getNombre().subSequence(0, 10)+":"+getString(R.string.Gastado)+" "+(ts.getTarifas().get(i).getSegConsumidosMes()/60)+" m. "+(ts.getTarifas().get(i).getSegConsumidosMes()%60)+" s. "+ getString(R.string.Limite)+" "+ts.getTarifas().get(i).getLimite()+" m.");//TEXTO
    		}
    		else
    		{
    			datos+="  * "+(ts.getTarifas().get(i).getSegConsumidosMes()/60)+"m. "+(ts.getTarifas().get(i).getSegConsumidosMes()%60)+"s.|Sin Límites\n";
    			numLineas+=2;
    		}
    	}
    	txtTarifas.setLines(numLineas);
    	txtTarifas.setText(datos);
       
        //-- Porcentaje del establecimiento de llamadas
        totalEstLlamadas=totalEstLlamadas/numLlamadas;
        
        if (vp.getEstablecimiento())
        	txtLlamadas.setText(" LLamadas ("+numLlamadas+")..."+FunGlobales.redondear(costeLlamadas*iva,2)+FunGlobales.monedaLocal()+" ("+FunGlobales.redondear(totalEstLlamadas,0)+"%)");
        else
        	txtLlamadas.setText(" LLamadas ("+numLlamadas+")..."+FunGlobales.redondear(costeLlamadas*iva,2)+FunGlobales.monedaLocal());

        //Calculamos el coste de los SMS
        numSMS=getNumSMS_send();
        numSMSGratis=vp.getPreferenciasSMSGratuitos();
        if (numSMS>numSMSGratis)
        	costeSMS=FunGlobales.redondear(vp.getPreferenciasTarifaSMS(),2)*(numSMS-numSMSGratis); //Mas SMS enviados que los gratuitos
        else
        	costeSMS=0; //Se han enviados menos SMS que los que hay gratuitos
        
        txtSMS.setText(" Mensajes ("+numSMS+")..."+FunGlobales.redondear(costeSMS,2)+FunGlobales.monedaLocal());
        //Hay que invertir la lista de llamadas, para presentarlo en pantalla y que apareccan
        //listaInvertida=lista;
        for (int a=lista.size()-1;a>=0;a--)
        	listaInvertida.add(lista.get(a));
        //dialog.dismiss();
        AdaptadorListaIconos ad = new AdaptadorListaIconos(this,listaInvertida);
        setListAdapter(ad);
        
        //Controlar si vp.getPreferenciasDefecto es vacio o nulo
    	if (ts.getId(vp.getPreferenciasDefecto())==-1)
    	{
    		//No hay tarifa por defecto  o no corresponde a una tarifa definida
    		Toast.makeText(this,R.string.mensaje_tarifa_defecto_no_definida,Toast.LENGTH_LONG).show();
    	}

        /*Añadir menu contextual */
        
        ListView listallamadas=(ListView) this.findViewById(android.R.id.list);
        registerForContextMenu(listallamadas); //--- Registrar el menu contextual sobre el listview listallamadas
    } //-- Fin de la función principal
    
    
// MANEJADORES DE EVENTOS    
    
    //--- Menú contextual
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	  	menu.setHeaderTitle(R.string.mnctx_titulo);
    	  	ArrayList <tarifa> tarifas=ts.getTarifas();
    	  	AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
    	  	
    	  	for (int a=0;a<tarifas.size();a++)
    	  	{
    	  		//La tarifa por defecto no debe aparecer en el menú contextual
    	  		if (!vp.getPreferenciasDefecto().equals(tarifas.get(a).getNombre()))
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
    	switch (requestCode){
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
			break; 
    	
    	
    	}
    }
    
    
    /**
     * Devuelve el nombre del contacto para un número determinado. Si no existe, devuelve el número
     * @param number
     * @return
     */
    private String getContactNumber(String number) {
 	   
 	   	String[] projection = new String[] {
 	   	Contacts.Phones.DISPLAY_NAME,
 	   	Contacts.Phones.NUMBER};
 	   	String retorno=number;
 	
 	   	Uri contactUri = Uri.withAppendedPath(Contacts.Phones.CONTENT_FILTER_URL, Uri.encode(number));
 	   	Cursor c =  managedQuery(contactUri, projection,null, null, null);
 	
 	   	if (c.moveToFirst()) 
 	   	{
 	   		String name = c.getString(c.getColumnIndex(Contacts.Phones.DISPLAY_NAME));
 	   		retorno= name;
 	   	}
 	   	
 	   	c.close();
 	   	return retorno;
    	}
    
    
    
    
    
    
}
