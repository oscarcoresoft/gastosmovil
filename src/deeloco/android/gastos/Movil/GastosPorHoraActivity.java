package deeloco.android.gastos.Movil;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
//import android.net.Uri;
import android.os.Bundle;
//import android.provider.CallLog;
import android.provider.Contacts.People;
//import android.widget.Toast;
import android.widget.ListView;
//import android.widget.ArrayAdapter;

public class GastosPorHoraActivity extends Activity {
	
	private ArrayList <String> horas=new ArrayList <String>();
	private ArrayList <String> gastos=new ArrayList <String>();
	private List<IconoYTexto> lista = new ArrayList<IconoYTexto>();
	
	
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.estadisticas);
      Bundle extras=getIntent().getExtras();
      //Extraemos el valor del total de la cadena, que es del tipo x.xx €
      String sTotal=extras.getString("total");
      sTotal=sTotal.substring(0, sTotal.length()-2); 
      double dTotal=Double.parseDouble(sTotal); //dTotal = total de gastos
      horas=extras.getStringArrayList("horas"); //horas = Listado de números en los que se ha gastado
      gastos=extras.getStringArrayList("Gastos"); //gastos = total de gastos para números.
      
      //Construir el listview para presentar los datos de gastos por número y el porcentaje de cada uno con respecto al total
      Resources res = getResources();
      Drawable rIcono = null;
      //rIcono=res.getDrawable(android.R.drawable.sym_action_call);
      rIcono=res.getDrawable(android.R.drawable.sym_action_call);
      lista.clear();
      //String sNombre;
      String sHoras;
      double dPorciento;
      double dGasto;
      //Cursor c;
      
      
      //Hay que recorrer horas para rellenar toda la lista
      for (int i=horas.size()-1;i>-1;i--)
      {
    	  dGasto=Double.parseDouble(gastos.get(i)); //gasto para el número en que estamos
    	  sHoras=horas.get(i);
    	  //sNombre="XXXXXXX XXXX";
    	  dPorciento=(dGasto/dTotal)*100;
    	  
    	  //Obtenemos el nombre de contacto de la agenda, dado el número
    	  /*System.out.println("****** "+People._ID+"="+'"'+i+'"');
    	  c=managedQuery(People.CONTENT_URI,null, People.NUMBER+"="+sHoras , null, null);
    	  
    	  startManagingCursor(c);
    	  c.moveToFirst();
    	  //Toast.makeText(getBaseContext(),"Posición:"+c.getPosition(),Toast.LENGTH_LONG).show();
    	  if (c.isFirst())
    	  { //Existe el número en la agenda de contactos
    		  int iNombre = c.getColumnIndex(People.NAME);
    		  sNombre=c.getString(iNombre);
    	  }
    	  else
    	  {//No existe el número
    		  sNombre=sHoras;
    		  sHoras="";
    	  }*/
    	  
    	  lista.add(new IconoYTexto(rIcono,sHoras,FunGlobales.redondear(dPorciento,2)+"",sHoras,dGasto));
    	  //c.close();
      }
      
      
      
      AdaptadorListaIconos2 ad = new AdaptadorListaIconos2(this,lista);
      //setListAdapter(ad);
      ListView listaGastos=(ListView) findViewById(R.id.lv_gastos);
      listaGastos.setAdapter(ad);

   }
}

