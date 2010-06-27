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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import deeloco.android.gastos.Movil.R;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.format.DateFormat;
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
		
	private static final int ACTUALIZAR = Menu.FIRST;
	private static final int ESTADISTICAS = Menu.FIRST+1;
    private static final int AJUSTES = Menu.FIRST+2;
    private static final int ACERCADE=Menu.FIRST+3;
    private static final int SALIR = Menu.FIRST+4;
    
    private static final int ADD_NS=Menu.FIRST;
    private static final int DELETE_NS=Menu.FIRST+1;
    private static final int ADD_ESP1=Menu.FIRST+2;
    private static final int DELETE_ESP1=Menu.FIRST+3;
    private static final int ADD_ESP2=Menu.FIRST+4;
    private static final int DELETE_ESP2=Menu.FIRST+5;
    
    private final int NUMSC=1;
    private final int NUMESP1=2;
    private final int NUMESP2=3;
    private final int NUM=4;
    
    private static final int SHOW_SUBACTIVITY = 1;
    
    private List<IconoYTexto> lista = new ArrayList<IconoYTexto>();
    GastosPorNumero gpn=new GastosPorNumero();
    GastosPorHora gph=new GastosPorHora();
    ValoresPreferencias vp=new ValoresPreferencias(this);
    
    
    //******************** AQUI ***************************
    private NumCoste numEsp=new NumCoste();

    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(Menu.NONE, ACTUALIZAR, 0, R.string.mn_actualizar).setIcon(android.R.drawable.ic_menu_rotate);
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

        listado(vp.getPreferenciasMes());
        //apagar_led();

    }
    
	public boolean onOptionsItemSelected (MenuItem item) {
		
        switch(item.getItemId()) {
        
        case AJUSTES:
      		Intent settingsActivity = new Intent(getBaseContext(), Preferencias.class );
      		//startActivity(settingsActivity);
      		startActivityForResult(settingsActivity, SHOW_SUBACTIVITY);
            break;
            
        case ACTUALIZAR:
        	listado(vp.getPreferenciasMes());
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
    	gpn.clear(); //limpiamos los gastos por numero (gpn)
    	gph.clear();
        Resources res = getResources();

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
           c=this.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, CallLog.Calls.DATE+"<"+(d2.getTime())+" and "+CallLog.Calls.DATE+">"+(d1.getTime())+" and "+CallLog.Calls.TYPE+"="+CallLog.Calls.OUTGOING_TYPE , null, CallLog.Calls.DEFAULT_SORT_ORDER);      
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
        double tarifa;
        double estLlamada=0;
        double totalEstLlamadas=0;
        
        //Si hay algún elemento
        c.moveToFirst();

        if (c.isFirst())
        {
        	//Recorrer todos los elementos de la consulta del registro de llamadas.
        	do{
        		Drawable rIcono = null;
        		rIcono=vp.getPreferenciasColor(NUM);
        		//rIcono=res.getDrawable(R.drawable.line5);
        		
        		String telefono=c.getString(iTelefono);
        		//String telefono="xxxyyyyyy";
        		long fecha=c.getLong(iFecha);
        		int duracion=c.getInt(iDuracion)+modifDuracion; //le añadimos la modificación de la duración de la llamada;
        		String sDuracion;
        		
        		String fechaHora=DateFormat.format("dd/MM/yyyy kk:mm",new Date(fecha)).toString();
        		
        		tarifa=vp.getPreferenciasTarifa(NUM);
        		//******************** AQUI ***************************
        		if ((vp.getPrefEsp1Activada()) && (numEsp.isNumEsp1(telefono)))
        		{
        			//Número especial 1
        			rIcono=vp.getPreferenciasColor(NUMESP1);
        			tarifa=vp.getPreferenciasTarifa(NUMESP1);
        			coste=(duracion*tarifa)+vp.getPreferenciasEstLlamadas(NUMESP1);
        			estLlamada=(vp.getPreferenciasEstLlamadas(NUMESP1)/coste)*100;
        		}
        		else
        		{
            		if ((vp.getPrefEsp2Activada()) && (numEsp.isNumEsp2(telefono)))
            		{
            			//Número especial 2
            			rIcono=vp.getPreferenciasColor(NUMESP2);
            			tarifa=vp.getPreferenciasTarifa(NUMESP2);
            			coste=(duracion*tarifa)+vp.getPreferenciasEstLlamadas(NUMESP2);
            			estLlamada=(vp.getPreferenciasEstLlamadas(NUMESP2)/coste)*100;
            		}
            		else
            		{
            			if (numEsp.isNumCosteCero(telefono))
                		//if (false)
                		{
            				//Número Sin Coste
                			coste=0;
                			rIcono=res.getDrawable(R.drawable.line7);//línea blanca
                		}
            			else
            			{
            				//Número 'normal'
                			rIcono=vp.getPreferenciasColor(NUM);
                			tarifa=vp.getPreferenciasTarifa(NUM);
                			coste=(duracion*tarifa)+vp.getPreferenciasEstLlamadas(NUM);
                			estLlamada=(vp.getPreferenciasEstLlamadas(NUM)/coste)*100;
            			}
            		
            		}
        		}

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
        TextView tv_Mes=(TextView) findViewById(R.id.txtMes);
        TextView tv_Numllamadas= (TextView) findViewById(R.id.txtNumLlamadas);
        TextView tv_CosteLlamadas=(TextView) findViewById(R.id.txtCosteLlamadas);
        
        TextView tv_total= (TextView) findViewById(R.id.txtTotal);
        TextView tv_NumSMS= (TextView) findViewById(R.id.txtNumSMS);
        TextView tv_CosteSMS= (TextView) findViewById(R.id.txtCosteSMS);
        
        tv_Mes.setText(textoMes);
        tv_Numllamadas.setText(""+numLlamadas);
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
        
        AdaptadorListaIconos ad = new AdaptadorListaIconos(this,lista);
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
    	  	menu.setHeaderTitle(R.string.mnctx_titulo); 
    		menu.add(0, ADD_NS, 0, R.string.mnctx_opc1);
    		menu.add(0, DELETE_NS, 0, R.string.mnctx_opc2);
    		if (vp.getPrefEsp1Activada())
    		{
        		menu.add(0, ADD_ESP1, 0, "Añadir a " +vp.getPrefEsp1Nombre());
        		menu.add(0, DELETE_ESP1, 0, "Eliminar de " +vp.getPrefEsp1Nombre());
    		}
    		
    		if (vp.getPrefEsp2Activada())
    		{
        		menu.add(0, ADD_ESP2, 0, "Añadir a " +vp.getPrefEsp2Nombre());
        		menu.add(0, DELETE_ESP2, 0, "Eliminar de " +vp.getPrefEsp2Nombre());
    		}
    		

    }
    
    //--- Eventos del menu contextual
    public boolean onContextItemSelected(MenuItem item) {
    	  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	  switch (item.getItemId()) {
    	  case ADD_NS:
    		  //******************** AQUI ***************************
    		  if (numEsp.isNumCosteCero(lista.get(info.position).telefono))
    		  {
    			  Toast.makeText(getBaseContext(),R.string.mensaje_existe,Toast.LENGTH_LONG).show();
    		  }
    		  else
    		  {
    			  if (numEsp.add(lista.get(info.position).telefono,NUMSC))
    			  {
    				  listado(vp.getPreferenciasMes());  
    			  }
    			  else
    			  {
    				  Toast.makeText(getBaseContext(),R.string.mensaje_existeoerror,Toast.LENGTH_LONG).show();
    			  }

    		  }
    		  
    	    return true;
    	    
    	  case DELETE_NS:
    		  
    		  if (numEsp.isNumCosteCero(lista.get(info.position).telefono))
    		  {
    			  numEsp.delete(lista.get(info.position).telefono,NUMSC);
    			  listado(vp.getPreferenciasMes());
    		  }
    		  else
    		  {
    			  Toast.makeText(getBaseContext(),R.string.mensaje_noexiste,Toast.LENGTH_LONG).show();
    		  }
    	    return true;
    	    
    	  case ADD_ESP1:

    		  if (numEsp.isNumEsp1(lista.get(info.position).telefono))
    		  {
    			  Toast.makeText(getBaseContext(),R.string.mensaje_existe,Toast.LENGTH_LONG).show();
    		  }
    		  else
    		  {
    			  if (numEsp.add(lista.get(info.position).telefono,NUMESP1))
    			  {
    				  listado(vp.getPreferenciasMes());  
    			  }
    			  else
    			  {
    				  Toast.makeText(getBaseContext(),R.string.mensaje_existeoerror,Toast.LENGTH_LONG).show();
    			  }
    		  }
    		  
    	    return true;
    	    
    	  case DELETE_ESP1:
    		  
    		  if (numEsp.isNumEsp1(lista.get(info.position).telefono))
    		  {
    			  numEsp.delete(lista.get(info.position).telefono,NUMESP1);
    			  listado(vp.getPreferenciasMes());
    		  }
    		  else
    		  {
    			  Toast.makeText(getBaseContext(),R.string.mensaje_noexiste,Toast.LENGTH_LONG).show();
    		  }
    	    return true;  	
    	    
    	  case ADD_ESP2:

    		  if (numEsp.isNumEsp2(lista.get(info.position).telefono))
    		  {
    			  Toast.makeText(getBaseContext(),R.string.mensaje_existe,Toast.LENGTH_LONG).show();
    		  }
    		  else
    		  {
    			  if (numEsp.add(lista.get(info.position).telefono,NUMESP2))
    			  {
    				  listado(vp.getPreferenciasMes());  
    			  }
    			  else
    			  {
    				  Toast.makeText(getBaseContext(),R.string.mensaje_existeoerror,Toast.LENGTH_LONG).show();
    			  }
    		  }
    		  
    	    return true;
    	    
    	  case DELETE_ESP2:
    		  
    		  if (numEsp.isNumEsp2(lista.get(info.position).telefono))
    		  {
    			  numEsp.delete(lista.get(info.position).telefono,NUMESP2);
    			  listado(vp.getPreferenciasMes());
    		  }
    		  else
    		  {
    			  Toast.makeText(getBaseContext(),R.string.mensaje_noexiste,Toast.LENGTH_LONG).show();
    		  }
    	    return true; 
    	    
    	  default:
    	    return super.onContextItemSelected(item);
    	  }
    	}
    
  //--- Eventos de devolución de parámetros de preferencias.class
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
			if (requestCode == SHOW_SUBACTIVITY) 
			{
					if (resultCode == RESULT_OK) 
					{
					     listado(vp.getPreferenciasMes());
					}
			}
    }

    
}