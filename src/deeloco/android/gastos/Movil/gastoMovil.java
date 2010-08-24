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
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class gastoMovil extends ListActivity {
    /** Called when the activity is first created. */
		
	private static final int TARIFAS = Menu.FIRST;
	private static final int ESTADISTICAS = Menu.FIRST+1;
    private static final int AJUSTES = Menu.FIRST+2;
    private static final int ACERCADE=Menu.FIRST+3;
    private static final int SALIR = Menu.FIRST+4;
    
    private static final int RETURN_PREFERENCES_AJUSTES = 1;
    private static final int RETURN_PREFERENCES_TARIFAS=2;
    private static final String TARIFAS_RETORNO = "tarifas_retorno";
    private static final String TAG = "GastosMóvil";
	private static final int COSTE=0;
	private static final int ESTABLECIMIENTO=1;
	private static final int GASTOMINIMO=2;
	private static final int LIMITE=3;
	private static final int COSTE_FUERA_LIMITE=4;
    private double iva=1.18;
    String path="\\sdcard\\gastosmovil\\datosTarifas.xml";
    int totalRegistros=0;
    
    private List<IconoYTexto> lista = new ArrayList<IconoYTexto>();
    private List<IconoYTexto> listaInvertida = new ArrayList<IconoYTexto>();
    GastosPorNumero gpn=new GastosPorNumero();
    GastosPorHora gph=new GastosPorHora();
    ValoresPreferencias vp=new ValoresPreferencias(this);
    private tarifas ts=new tarifas();
    ProgressDialog dialog;

    //******************** AQUI ***************************

    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(Menu.NONE, TARIFAS, 0, R.string.mn_tarifas).setIcon(android.R.drawable.ic_menu_recent_history);
    	menu.add(Menu.NONE, ESTADISTICAS, 0, R.string.mn_estadisticas).setIcon(android.R.drawable.ic_menu_agenda);
    	menu.add(Menu.NONE, AJUSTES, 0, R.string.mn_ajustes).setIcon(android.R.drawable.ic_menu_preferences);
    	menu.add(Menu.NONE, ACERCADE, 0, R.string.mn_acercade).setIcon(android.R.drawable.ic_menu_info_details);
    	menu.add(Menu.NONE, SALIR, 0, R.string.mn_salir).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
    	
    	return true;
    }
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        
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
        	
        case ESTADISTICAS:
        	TextView tv_Numllamadas= (TextView) findViewById(R.id.txtNumLlamadas);
        	String numLlamadas=tv_Numllamadas.getText().toString();
        	if (numLlamadas.compareTo("0")==0)
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
        	String total= ((TextView) findViewById(R.id.txtTotal)).getText().toString();
        	
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
    	   Date hoy=new Date();
    	   int anyo=hoy.getYear()+1900;
    	   int mesActual=hoy.getMonth()+1;
    	   //System.out.println("****** Mes seleccionado: "+mes+" -- Mes actual: "+mesActual);
    	   if (mesActual<mes)
    	   {
    		   anyo--;
    	   }
    	   //System.out.println("****** Año: "+anyo);
    	   Calendar c1=new GregorianCalendar(anyo,mes-1,1,0,0);
           Calendar c2=new GregorianCalendar(anyo,mes,1,0,0);
           Date d1=new Date();
           Date d2=new Date();
           d1=c1.getTime();
           d2=c2.getTime(); 

           //c=this.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, CallLog.Calls.DATE+"<"+(d2.getTime())+" and "+CallLog.Calls.DATE+">"+(d1.getTime())+" and "+CallLog.Calls.TYPE+"="+CallLog.Calls.OUTGOING_TYPE , null, CallLog.Calls.DEFAULT_SORT_ORDER);
           //Uri.parse("content://sms/sent/date")+"<"+(d2.getTime())+" and "+Uri.parse("content://sms/sent/date")+">"+(d1.getTime())
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
           Calendar c2=new GregorianCalendar(anyo,mes,valorIncioMes-1,0,0);
           Date d1=new Date();
           Date d2=new Date();
           d1=c1.getTime();
           d2=c2.getTime();
           
           //Hacemos la consulta
           c=this.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, CallLog.Calls.DATE+"<"+(d2.getTime())+" and "+CallLog.Calls.DATE+">"+(d1.getTime())+" and "+CallLog.Calls.TYPE+"="+CallLog.Calls.OUTGOING_TYPE , null, "date ASC");      
       }

        this.startManagingCursor(c);
        
        int iTelefono = c.getColumnIndex(CallLog.Calls.NUMBER);
        int iFecha = c.getColumnIndex(CallLog.Calls.DATE);
        int iDuracion = c.getColumnIndex(CallLog.Calls.DURATION);
        int numLlamadas=0;
        int numSMS=0;
        //int modifDuracion=getPreferenciasDuracion(); //modificación de la duración de la llamada
        int modifDuracion=vp.getPreferenciasDuracion(); //modificación de la duración de la llamada
        double costeLlamadas=0;
        double costeSMS=0;
        double coste;
        double estLlamada=0;
        double totalEstLlamadas=0;
        int totalSegundos=0;
        double[] retorno={0.0,0.0,0.0,0.0,0.0};
        
        //Si hay algún elemento
        AdaptadorListaIconos ad2 = new AdaptadorListaIconos(this,listaInvertida);
        setListAdapter(ad2);
        c.moveToFirst();
        totalRegistros=c.getCount();
        
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
        		
        		//t=tarifa a la que pertenece el número
        		tarifa t=ts.getTarifa(telefono,vp.getPreferenciasDefecto());
        		retorno=ts.costeLlamada(telefono,fechaHora, duracion,vp.getPreferenciasDefecto());
        		
        		rIcono=vp.getColor(t.getColor());
        		
        		//Solo se acumula el limite de tiempo cuando el limite retornado sea > 0.0, es decir tiene limite
        		if (retorno[LIMITE]!=0.0)
        			totalSegundos=totalSegundos+duracion;
        		
        		//El coste ha mostrar dependera del limite
        		if ((retorno[LIMITE]*60)>=totalSegundos || retorno[LIMITE]==0.0)
        			coste=retorno[COSTE]; //limite < tiempo hablado
        		else
        		{
        			rIcono=getResources().getDrawable(android.R.drawable.presence_busy);
        			coste=retorno[COSTE_FUERA_LIMITE]; //limite > tiempo hablado
        		}
        		
        		

        		if (retorno[ESTABLECIMIENTO]!=0.0)
        			estLlamada=(((retorno[ESTABLECIMIENTO]/100)*iva)/coste)*100;
        		//estLlamada=f.getEstablecimiento();

        		if (duracion>modifDuracion)
        		{  
        			//Ya tenemos el coste y el numero y es una llamada > 0, lo metemos en GastosPorNumero
            		gpn.add(telefono, coste);
            		gph.add(new Date(fechaHora), coste);
            		
        			if (coste>0)
        			{
        				//estLlamada=(vp.getPreferenciasEstLlamadas()/coste)*100;
        				totalEstLlamadas=totalEstLlamadas+estLlamada;
        			}
        			sDuracion=(duracion/60)+" min "+(duracion%60)+" seg";
        			//DEPURAR .. Se puede definir una variable string donde se mete el % del establecimiento ...
        			if (vp.getEstablecimiento())
        				lista.add(new IconoYTexto(rIcono, telefono, fechaHora+" ("+FunGlobales.redondear(estLlamada,0)+"%)",sDuracion,FunGlobales.redondear(coste,2)));
        			else
        				lista.add(new IconoYTexto(rIcono, telefono, fechaHora,sDuracion,FunGlobales.redondear(coste,2)));
        			costeLlamadas=costeLlamadas+FunGlobales.redondear(coste,2);
        			numLlamadas++;

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
        }
        
        TextView tv_cabRegistro=(TextView) findViewById(R.id.cabRegistros);
        TextView tv_Mes=(TextView) findViewById(R.id.txtMes);
        TextView tv_Numllamadas= (TextView) findViewById(R.id.txtNumLlamadas);
        TextView tv_CosteLlamadas=(TextView) findViewById(R.id.txtCosteLlamadas);
        
        TextView tv_total= (TextView) findViewById(R.id.txtTotal);
        TextView tv_NumSMS= (TextView) findViewById(R.id.txtNumSMS);
        TextView tv_CosteSMS= (TextView) findViewById(R.id.txtCosteSMS);
        
        tv_Mes.setText(textoMes);
        tv_Numllamadas.setText(""+numLlamadas);
        if (retorno[LIMITE]>0)
        	tv_cabRegistro.setText("Gastado "+(totalSegundos/60)+" m. "+(totalSegundos%60)+" s. Límite "+retorno[LIMITE]+" m.");//TEXTO
        else
        	tv_cabRegistro.setText("Registro de llamadas"); //TEXTO
        //-- Porcentaje del establecimiento de llamadas
        totalEstLlamadas=totalEstLlamadas/numLlamadas;
        
        if (vp.getEstablecimiento())
        	tv_CosteLlamadas.setText(FunGlobales.redondear(costeLlamadas,2)+" € ("+FunGlobales.redondear(totalEstLlamadas,0)+"%)");
        else
        	tv_CosteLlamadas.setText(FunGlobales.redondear(costeLlamadas,2)+" €");
        
        numSMS=getNumSMS_send();
        tv_NumSMS.setText(""+numSMS);
        costeSMS=FunGlobales.redondear(vp.getPreferenciasTarifaSMS(),2)*numSMS;
        tv_CosteSMS.setText(FunGlobales.redondear(costeSMS,2)+" €");
        
        tv_total.setText(FunGlobales.redondear(costeLlamadas+costeSMS,2)+" €");
        
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
    	  			Log.d(TAG,"Pertenece "+listaInvertida.get(info.position).telefono+" a "+tarifas.get(a).getNombre()+" -- "+tarifas.get(a).pertenece(listaInvertida.get(info.position).telefono));
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
    	  String tlf=lista.get(info.position).telefono;
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
    
    
    
    
    
    
    
    
    
}
