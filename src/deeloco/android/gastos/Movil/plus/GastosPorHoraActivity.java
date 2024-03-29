package deeloco.android.gastos.Movil.plus;

import java.util.ArrayList;
import java.util.List;

import deeloco.android.gastos.Movil.plus.R;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
//import android.net.Uri;
import android.os.Bundle;
//import android.provider.CallLog;
//import android.widget.Toast;
import android.widget.ListView;
//import android.widget.ArrayAdapter;
import android.widget.TextView;

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
      //System.out.println("["+sTotal+"]");
      //sTotal="5.1";
      //sTotal=sTotal.substring(0, sTotal.length()-2); 
      double dTotal=Double.parseDouble(sTotal); //dTotal = total de gastos
      horas=extras.getStringArrayList("Horas"); //horas = Listado de números en los que se ha gastado
      gastos=extras.getStringArrayList("Gastos"); //gastos = total de gastos para números.
      
      //Construir el listview para presentar los datos de gastos por número y el porcentaje de cada uno con respecto al total
      Resources res = getResources();
      Drawable rIcono = null;
      //rIcono=res.getDrawable(android.R.drawable.sym_action_call);
      rIcono=res.getDrawable(android.R.drawable.sym_action_call);
      lista.clear();
      //String sNombre;
      int sHoras;
      double dPorciento;
      double dGasto;
      //Cursor c;
      
      
      //Hay que recorrer horas para rellenar toda la lista
      for (int i=horas.size()-1;i>-1;i--)
      {
    	  dGasto=Double.parseDouble(gastos.get(i)); //gasto para el número en que estamos
    	  sHoras=Integer.parseInt(horas.get(i));
    	  //sNombre="XXXXXXX XXXX";
    	  dPorciento=(dGasto/dTotal)*100;
    	  
    	  lista.add(new IconoYTexto(rIcono,""+sHoras,"",FunGlobales.redondear(dPorciento,2)+"","de las "+sHoras+":00 a las "+(sHoras+1)+":00",dGasto));
    	  //c.close();
      }
      
      
      
      AdaptadorListaIconos2 ad = new AdaptadorListaIconos2(this,lista);
      //setListAdapter(ad);
      ListView listaGastos=(ListView) findViewById(R.id.lv_gastos);
      listaGastos.setAdapter(ad);
      TextView titulo=(TextView) findViewById(R.id.estadistica_titulo);
      titulo.setText(R.string.gph_titulo);

   }
}

