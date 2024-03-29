package deeloco.android.gastos.Movil.plus;

import java.util.ArrayList;
import java.util.List;

import deeloco.android.gastos.Movil.plus.R;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;
import android.widget.ListView;

public class GastosPorNumeroActivity extends Activity {
	
	private ArrayList <String> numeros=new ArrayList <String>();
	private ArrayList <String> gastos=new ArrayList <String>();
	private ArrayList <String> duracion=new ArrayList <String>();
	private List<IconoYTexto> lista = new ArrayList<IconoYTexto>();
	
	
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.estadisticas);
      Bundle extras=getIntent().getExtras();
      //Extraemos el valor del total de la cadena, que es del tipo x.xx €
      //Recuperamos los parámetros que se han pasado
      String sTotal=extras.getString("total");
      String totalSegundos=extras.getString("totalSegundos");
      numeros=extras.getStringArrayList("Numeros"); //numeros = Listado de números en los que se ha gastado
      gastos=extras.getStringArrayList("Gastos"); //gastos = total de gastos para números.
      
      if (gastos==null)
      {
    	  //Duración por numero
    	  int iTotal=Integer.parseInt(totalSegundos); //dTotal = total de gastos
    	  
    	  duracion=extras.getStringArrayList("Duracion"); //duracion = total de segundos para números.
    	  
    	//Construir el listview para presentar los datos de gastos por número y el porcentaje de cada uno con respecto al total
          Resources res = getResources();
          
          Drawable rIcono = null;
          rIcono=res.getDrawable(android.R.drawable.sym_action_call);
          lista.clear();
          String sNombre;
          String sNumero;
          double dPorciento;
          int iDuracion;
          //Cursor c;
          
          
          //Hay que recorrer numeros para rellenar toda la lista
          for (int i=numeros.size()-1;i>-1;i--)
          {
        	  iDuracion=Integer.parseInt(duracion.get(i)); //gasto para el número en que estamos
        	  sNumero=numeros.get(i);
        	  sNombre="XXXXXXX XXXX";
        	  
        	  dPorciento=((double) iDuracion/ (double) iTotal)*100;
        	  
        	  sNombre=getContactNumber(sNumero);
        	  if (sNombre.equals(sNumero))
        	  {
        		  sNumero="";
        	  }
        	  
        	  lista.add(new IconoYTexto(rIcono,sNombre,FunGlobales.segundosAHoraMinutoSegundo(iDuracion),FunGlobales.redondear(dPorciento,2)+"",sNumero,iDuracion));
        	  //c.close();
          }

      }
      else
      {
    	  //Gastos por numero
          double dTotal=Double.parseDouble(sTotal); //dTotal = total de gastos
          
        //Construir el listview para presentar los datos de gastos por número y el porcentaje de cada uno con respecto al total
          Resources res = getResources();
          
          Drawable rIcono = null;
          rIcono=res.getDrawable(android.R.drawable.sym_action_call);
          lista.clear();
          String sNombre;
          String sNumero;
          double dPorciento;
          double dGasto;

          //Hay que recorrer numeros para rellenar toda la lista
          for (int i=numeros.size()-1;i>-1;i--)
          {
        	  dGasto=Double.parseDouble(gastos.get(i)); //gasto para el número en que estamos
        	  sNumero=numeros.get(i);
        	  sNombre="XXXXXXX XXXX";
        	  
        	  dPorciento=(dGasto/dTotal)*100;
        	  
        	  sNombre=getContactNumber(sNumero);
        	  if (sNombre.equals(sNumero))
        	  {
        		  sNumero="";
        	  }
        	  
        	  lista.add(new IconoYTexto(rIcono,sNombre,"",FunGlobales.redondear(dPorciento,2)+"",sNumero,dGasto));
          }
    	  
      }
      
      AdaptadorListaIconos2 ad = new AdaptadorListaIconos2(this,lista);
      //setListAdapter(ad);
      ListView listaGastos=(ListView) findViewById(R.id.lv_gastos);
      listaGastos.setAdapter(ad);

   }
   
   
   //Función de busqueda de contactos por numeros
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

