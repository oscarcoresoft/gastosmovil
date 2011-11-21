package deeloco.android.gastos.Movil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import deeloco.android.gastos.Movil.TextBox.TextBoxListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class PreferencesTarifas extends ListActivity{
	
	private static final int NUEVA_TARIFA = Menu.FIRST;
	private static final int NUEVA_TARIFA_PREDEFINIDA = Menu.FIRST+1;
	private static final int COMPARTIR= Menu.FIRST+2;
	private static final int RECUPERAR= Menu.FIRST+3;
	private static final int SOLICITAR= Menu.FIRST+4;
	private static final int RETURN_PREFERENCES_TARIFA=1;
	private static final String TARIFA_RETORNO = "tarifa_retorno";
	private static final String TAG = "PreferencesTarifas";
	private static final String TARIFAS_RETORNO = "tarifas_retorno";
	ValoresPreferencias vp=new ValoresPreferencias(this);
	String path="/sdcard/gastosmovil/datosTarifas.xml";
	
	private descargar_fichero ficheroServidor;
	private tarifas ts;
	private List<IconoYTexto2> listaIYT = new ArrayList<IconoYTexto2>();
	private ProgressDialog pd;
	
    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(Menu.NONE, NUEVA_TARIFA, 0, R.string.mn_nueva_tarifa).setIcon(android.R.drawable.ic_menu_add);
    	menu.add(Menu.NONE, NUEVA_TARIFA_PREDEFINIDA, 0, R.string.mn_nueva_tarifa_predefinida).setIcon(android.R.drawable.ic_menu_add);
    	menu.add(Menu.NONE, COMPARTIR, 0, R.string.mn_compartir).setIcon(android.R.drawable.ic_menu_share);
    	menu.add(Menu.NONE, RECUPERAR, 0, R.string.mn_recuperar).setIcon(android.R.drawable.ic_menu_set_as);
    	menu.add(Menu.NONE, SOLICITAR, 0, "Solicitar Nueva Tarifa").setIcon(android.R.drawable.ic_menu_edit);
    	return true;
    }
	
    
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
    	MenuItem itemTarifasPre = menu.findItem(NUEVA_TARIFA_PREDEFINIDA);
    	MenuItem itemDescargar = menu.findItem(RECUPERAR);
    	this.ficheroServidor=new descargar_fichero(getApplicationContext(), getString(R.string.tarifasPre_url), getString(R.string.tarifasPre_pre)+vp.getOperadora()+getString(R.string.tarifasPre_ext), getString(R.string.tarifasPre_path));
    	//Comprobamos si hemos descargado el fichero de tarifas predefinidas
    	File f=new File(getString(R.string.tarifasPre_path)+getString(R.string.tarifasPre_pre)+vp.getOperadora()+getString(R.string.tarifasPre_ext));
    	itemTarifasPre.setEnabled(f.exists());
    	//Comprobar si hay nueva versión en el servidor
    	itemDescargar.setEnabled(ficheroServidor.nuevaVersionServidor());

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
			builder.setTitle(getString(R.string.cabTarifas)+" "+vp.getOperadora());
			
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
        	
         	File f2=new File(this.path);
        	if (f2.exists() && f2.canRead())
        	{
	        	xmlMessageIntent.putExtra(Intent.EXTRA_SUBJECT, "Gastos Móvil: Fichero configuración tarifa.");
	        	xmlMessageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/gastosmovil/datosTarifas.xml"));
	        	startActivity(xmlMessageIntent);
        	}
        	else
        	{
        		Log.d(TAG,"No se puede leer el fichero a adjuntar");
        	}
        	break;

        case RECUPERAR:
        	//Descargar fichero de tarifas predefinidas
        	pd = ProgressDialog.show(this, "", "Descargando datos ...", true,false);
    		new segundoPlano().start();
        	
        	break;
        	
        case SOLICITAR:
        	//Descargar fichero de tarifas predefinidas
        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/spreadsheet/viewform?hl=es&formkey=dGwwb1ZYTkd6d3pUTHE0TFFIYmJ5WUE6MQ#gid=0"));
        	startActivity(browserIntent);
        	
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
	
	
	//Clase runable
	private class segundoPlano extends Thread
	{
    	public void run() {
           try {
	        	   	Log.d("SegundoPlano","URL="+ficheroServidor.getURL()+ficheroServidor.getNombreFichero());
	        	   	switch (ficheroServidor.download())
	           		{
	           		case 1:
	           			Toast.makeText(getApplicationContext(),"No se ha podido descargar las tarifas. Intentelo más tarde.",Toast.LENGTH_LONG).show();
	       				break;
	           		case 2:
	           			Toast.makeText(getApplicationContext(),"No se ha podido descargar las tarifas. Sin conexión a Internet.",Toast.LENGTH_LONG).show();
	           			break;
	           		}
	        	   	pd.dismiss();
               }
           	catch (Exception e) {
               // if something fails do something smart
           	 pd.dismiss();
           	}
       }
	}

	
}
