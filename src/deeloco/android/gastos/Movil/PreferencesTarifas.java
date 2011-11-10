package deeloco.android.gastos.Movil;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import deeloco.android.gastos.Movil.TextBox.TextBoxListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

public class PreferencesTarifas extends ListActivity{
	
	private static final int NUEVA_TARIFA = Menu.FIRST;
	private static final int NUEVA_TARIFA_PREDEFINIDA = Menu.FIRST+1;
	private static final int COMPARTIR= Menu.FIRST+2;
	private static final int RECUPERAR= Menu.FIRST+3;
	private static final int RETURN_PREFERENCES_TARIFA=1;
	private static final String TARIFA_RETORNO = "tarifa_retorno";
	private static final String TAG = "PreferencesTarifas";
	private static final String TARIFAS_RETORNO = "tarifas_retorno";
	ValoresPreferencias vp=new ValoresPreferencias(this);
	String path="/sdcard/gastosmovil/datosTarifas.xml";

	private tarifas ts;
	
	private List<IconoYTexto2> listaIYT = new ArrayList<IconoYTexto2>();
	
    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(Menu.NONE, NUEVA_TARIFA, 0, R.string.mn_nueva_tarifa).setIcon(android.R.drawable.ic_menu_add);
    	menu.add(Menu.NONE, NUEVA_TARIFA_PREDEFINIDA, 0, R.string.mn_nueva_tarifa_predefinida).setIcon(android.R.drawable.ic_menu_add);
    	menu.add(Menu.NONE, COMPARTIR, 0, R.string.mn_compartir).setIcon(android.R.drawable.ic_menu_share);
    	menu.add(Menu.NONE, RECUPERAR, 0, R.string.mn_recuperar).setIcon(android.R.drawable.ic_menu_set_as);
    	return true;
    }
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tarifas);
        ts = (tarifas) getIntent().getExtras().get("tarifas");

    }
    
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		listaIYT.clear();
        for (int a=0;a<ts.numTarifas();a++){
        	
        	if (ts.getTarifas().get(a).getDefecto())
        	{
        		listaIYT.add(new IconoYTexto2(getResources().getDrawable(android.R.drawable.ic_menu_more), ts.getTarifas().get(a).getNombre(),getString(R.string.tarifaDefecto)));
        	}
        	else
        	{
        		listaIYT.add(new IconoYTexto2(getResources().getDrawable(android.R.drawable.ic_menu_more), ts.getTarifas().get(a).getNombre(), " "));
        	}
        }
        
        adaptadorTarifas ad = new adaptadorTarifas(this,listaIYT);
        setListAdapter(ad);
        
      //Comprobar que las tarifas que se aplican por defecto son compatibles
		if(!ts.compatibilidadHorarioTarifasDefecto())
			Toast.makeText(getBaseContext(),getText(R.string.mensaje_tarifas_defecto_incompatibles),Toast.LENGTH_LONG).show();
	}

	public boolean onOptionsItemSelected (MenuItem item) {
		
        switch(item.getItemId()) {
        
        case NUEVA_TARIFA:
        	//Creamos un objeto tarifa con id=0 y se lo pasamos a la activity PreferencesTarifa
        	
        	tarifa t=new tarifa(0);
        	t.setNombre("Tarifa Nueva");
        	t.setColor("Blanco");
        	t.setNumeros("");
        	Intent settingsActivity2 = new Intent(getBaseContext(), PreferencesTarifa.class );
        	Bundle extras = new Bundle();
        	extras.putInt("idTarifa", 0);
        	extras.putSerializable("tarifa", t);
        	settingsActivity2.putExtras(extras);
        	startActivityForResult(settingsActivity2,RETURN_PREFERENCES_TARIFA);
            break;
            
        case NUEVA_TARIFA_PREDEFINIDA:
        	//Creamos un objeto tarifa con id=0 y se lo pasamos a la activity PreferencesTarifa

        	TarifasPreDefinidas tsPre=new TarifasPreDefinidas(getBaseContext());
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.mn_nueva_tarifa_predefinida);
			
			builder.setSingleChoiceItems(tsPre.nombresTarifas(),-1, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			        //Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
					//Retorno
			    	TarifasPreDefinidas tsPre=new TarifasPreDefinidas(getBaseContext());
			    	//Log.d(TAG,"Vamos a añadir la tarifa con indice="+item);
			    	tarifa t=tsPre.getTarifa(item);
			    	if (t!=null) //Se ha retornado una tarifa predefinida
			    	{
				    	ts.addTarifa(t);
				    	Intent resultIntent=new Intent();
				    	resultIntent.putExtra(TARIFAS_RETORNO, ts);
				    	setResult(Activity.RESULT_OK, resultIntent);
			    	}
			    	else
			    	{
			    		Toast.makeText(getBaseContext(),getString(R.string.mensaje_problemas_cargar_tarifa_predefinida),Toast.LENGTH_LONG).show();
			    	}
			    	onStart();
			    }
			});
			AlertDialog alert = builder.create();
			alert.show();
            break;
            
        case COMPARTIR:
        	//Compartir el fichero de configuración de tarifas
        	Intent xmlMessageIntent = new Intent(android.content.Intent.ACTION_SEND);  
        	xmlMessageIntent.setType("text/plain");  
        	File f=new File(path);

        	//Copiamos el fichero con extensión jpg, para que gmail pueda descargarlo
        	
            try{
            	FileReader fReader=new FileReader(f);;
            	FileWriter fWriter= new FileWriter(path+".jpg");;
            	char[] xml=new char[(int)f.length()];
            	fReader.read(xml,0,(int)f.length());
            	fReader.close();
                fWriter.write(xml);
                fWriter.flush();
                fWriter.close();
             }
            catch(Exception e)
            {

            	Log.d(TAG,"No se puede copiar el fichero.");
             }
        	
         	File f2=new File(path+".jpg");
        	if (f2.exists() && f2.canRead())
        	{
	        	xmlMessageIntent.putExtra(Intent.EXTRA_SUBJECT, "Gastos Móvil: Fichero configuración tarifa.");
	        	xmlMessageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/gastosmovil/datosTarifas.xml.jpg"));
	        	startActivity(xmlMessageIntent);
        	}
        	else
        	{
        		Log.d(TAG,"No se puede leer el fichero a adjuntar");
        	}
        	//Volver a poner la extensión xml.
        	break;

        case RECUPERAR:
        	//Compartir el fichero de configuración de tarifas
        	String url="http://www.simahuelva.es/deeloco/tarifas/";
    		String path="/sdcard/gastosmovil/";
    		String nombre="datosTarifasPre.xml";
        	descargar_fichero download=new descargar_fichero(getApplicationContext(), url, nombre, path);
        	if (!download.download())
    		{
    			Toast.makeText(getApplicationContext(),"No se ha podido descargar las tarifas. Intentelo más tarde.",Toast.LENGTH_LONG).show();
    		}
        	/*
        	final File fRecuperacion=new File("/sdcard/download/datosTarifas.xml.jpg");
        	if (fRecuperacion.exists() && fRecuperacion.canRead())
        	{
            	AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            	builder2.setMessage("Se perderá la configuración actual. ¿Quieres continuar?")
            	       .setCancelable(false)
            	       .setPositiveButton("Si", new DialogInterface.OnClickListener() {
            	           public void onClick(DialogInterface dialog, int id) {

            	        	 //Existe fichero de tarifa compartida. Copiarla a /gastosmovil
            	        		try{
            	                	FileReader fReader=new FileReader(fRecuperacion);
            	                	FileWriter fWriter= new FileWriter(path);
            	                	char[] xml=new char[(int)fRecuperacion.length()];
            	                	fReader.read(xml,0,(int)fRecuperacion.length());
            	                	fReader.close();
            	                    fWriter.write(xml);
            	                    fWriter.flush();
            	                    fWriter.close();
            	                    // Eliminamos el fichero de intercambio datosTarifas.xml.jpg
            	                     try
            	                     {
            	                    	 fRecuperacion.delete();
            	                     }
            	                     catch(Exception e)
            	                     {
            	                    	 Log.e("PreferenciasTarifas.java", "Error al eliminar el fichero datosTarifas.xml.jpg"+e.toString()+" ("+e.hashCode()+")");
            	                     }
            	                    
            	                    // Cargamos los valores de las tarifas 
            	                    try
            	                    {            	                    	
            	            	        SAXParserFactory spf = SAXParserFactory.newInstance();
            	            	        SAXParser sp = spf.newSAXParser();
            	            	        // Get the XMLReader of the SAXParser we created. 
            	            	        XMLReader xr = sp.getXMLReader();
            	            	        // Create a new ContentHandler and apply it to the XML-Reader
            	            	        TarifasParserXML tarifasXML = new TarifasParserXML();
            	            	        ts=null;
            	            	        ts=new tarifas();
            	            	        tarifasXML.setTarifas(ts);
            	            	        xr.setContentHandler(tarifasXML);
            	            	        xr.parse(new InputSource (new FileReader(path)));
            	            	        // Parsing has finished. 
            	            	        Log.d(TAG,"NOmbre tarifa importada->"+ts.getTarifas().get(0).getNombre());
            	            	        Intent resultIntent=new Intent();
            	    			    	resultIntent.putExtra(TARIFAS_RETORNO, ts);
            	    			    	setResult(Activity.RESULT_OK, resultIntent);
            	            	        onStart();
            	                    }
            	                    catch (Exception e)
            	                    {
            	                    	//Si el error se produce porque no existe el fichero xml, hay que crearlo.
            	                    	//Tambien hay que crear el directorio
            	                    	
            	                    	System.out.println("ERROR:"+e.toString()+" ("+e.hashCode()+")");
            	                    }

            	                    
            	                 }
            	                catch(Exception e)
            	                {
            	                	Log.d(TAG,"No se puede copiar el fichero.");
            	                }
 
            	           }
            	       })
            	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
            	           public void onClick(DialogInterface dialog, int id) {
            	                dialog.cancel();
            	           }
            	       });
            	AlertDialog alert2 = builder2.create();
            	alert2.show();
        		
        	}
        	else
        	{
        		Toast.makeText(getBaseContext(),getString(R.string.mensaje_noexiste_tarifa_compartida),Toast.LENGTH_LONG).show();
        	}
        	*/
        	
        	break;
        	
        	
        }
        return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		//Aqui es donde hay que pasar a la pantalla de Tarifas
		IconoYTexto2 iyt=(IconoYTexto2) l.getItemAtPosition(position);
		int idTarifa=ts.getId(iyt.titulo);
    	Intent settingsActivity2 = new Intent(getBaseContext(), PreferencesTarifa.class );
    	tarifa t = ts.getTarifa(idTarifa);
    	Bundle extras = new Bundle();
    	extras.putInt("idTarifa", idTarifa);
    	extras.putSerializable("tarifa", t);
    	settingsActivity2.putExtras(extras);
    	//startActivity(settingsActivity2);
    	startActivityForResult(settingsActivity2, RETURN_PREFERENCES_TARIFA);
    	
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RETURN_PREFERENCES_TARIFA:
			if (resultCode==Activity.RESULT_OK)
			{
				tarifa t=(tarifa) data.getSerializableExtra(TARIFA_RETORNO);
				//Añadir la tarifa al ArrayList de tarifas
				switch (t.getIdentificador()) {
				case 0:

					ts.addTarifa(t); //Tarifa nueva
					break;
					
				case -1:
					ts.deleteTarifa(t.getNombre());
					break;

				default:
					ts.modificarTarifa(t.getIdentificador(), t);
					break;
				}
			}
			//Comprobar que las tarifas que se aplican por defecto son compatibles
		/*	if (ts.getNumTarifasDefecto()==0)
				Toast.makeText(getBaseContext(),getText(R.string.mensaje_no_tarifas_defecto),Toast.LENGTH_LONG).show();
			else
				if(!ts.compatibilidadHorarioTarifasDefecto())
					Toast.makeText(getBaseContext(),getText(R.string.mensaje_tarifas_defecto_incompatibles),Toast.LENGTH_LONG).show();
		*/
			break;

		default:
			break;
		}
	}
	
	
	
	
	TextBoxListener tbListener=new TextBoxListener() {
		
		
		@Override
		public void onOkClick(String valor) {
			// TODO Auto-generated method stub
			Toast.makeText(getBaseContext(),"Retorno de : "+valor,Toast.LENGTH_LONG).show();
			
		}
		
		/*@Override
		public void onCancelClick() {
			// TODO Auto-generated method stub
			Toast.makeText(getBaseContext(),"CANCELAR ",Toast.LENGTH_LONG).show();
			
		}*/
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		//Retorno
    	Intent resultIntent=new Intent();
    	resultIntent.putExtra(TARIFAS_RETORNO, ts);
    	setResult(Activity.RESULT_OK, resultIntent);
	}
	
	


	
}
