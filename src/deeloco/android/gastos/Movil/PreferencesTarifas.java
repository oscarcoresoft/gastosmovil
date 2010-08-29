package deeloco.android.gastos.Movil;


import java.util.ArrayList;
import java.util.List;

import deeloco.android.gastos.Movil.TextBox.TextBoxListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
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
	private static final int RETURN_PREFERENCES_TARIFA=1;
	private static final String TARIFA_RETORNO = "tarifa_retorno";
	private static final String TAG = "PreferencesTarifas";
	private static final String TARIFAS_RETORNO = "tarifas_retorno";

	private tarifas ts;
	
	private List<IconoYTexto2> listaIYT = new ArrayList<IconoYTexto2>();
	
    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(Menu.NONE, NUEVA_TARIFA, 0, R.string.mn_nueva_tarifa).setIcon(android.R.drawable.ic_menu_add);
    	menu.add(Menu.NONE, NUEVA_TARIFA_PREDEFINIDA, 0, R.string.mn_nueva_tarifa_predefinida).setIcon(android.R.drawable.ic_menu_add);
    	return true;
    }
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tarifas);
        ts = (tarifas) getIntent().getExtras().get("tarifas");
        /*String tarifaDef=getIntent().getExtras().getString("tarifaDefecto");
        for (int a=0;a<ts.numTarifas();a++){
        	
        	if (ts.getTarifas().get(a).getNombre().compareTo(tarifaDef)==0)
        	{
        		
        		listaIYT.add(new IconoYTexto2(getResources().getDrawable(android.R.drawable.ic_menu_more), ts.getTarifas().get(a).getNombre(),"Defecto"));
        		Log.d(TAG,"Defecto = "+listaIYT.get(a).subtitulo);
        	}
        	else
        	{
        		listaIYT.add(new IconoYTexto2(getResources().getDrawable(android.R.drawable.ic_menu_more), ts.getTarifas().get(a).getNombre(), "LLASKJ "));
        		Log.d(TAG,"NO Defecto = "+listaIYT.get(a).subtitulo);
        	}
        }
        
        adaptadorTarifas ad = new adaptadorTarifas(this,listaIYT);
        setListAdapter(ad);*/

    }
    
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		listaIYT.clear();
        String tarifaDef=getIntent().getExtras().getString("tarifaDefecto");
        for (int a=0;a<ts.numTarifas();a++){
        	
        	if (ts.getTarifas().get(a).getNombre().compareTo(tarifaDef)==0)
        	{
        		listaIYT.add(new IconoYTexto2(getResources().getDrawable(android.R.drawable.ic_menu_more), ts.getTarifas().get(a).getNombre(),"Tarifa aplicada por defecto"));
        	}
        	else
        	{
        		listaIYT.add(new IconoYTexto2(getResources().getDrawable(android.R.drawable.ic_menu_more), ts.getTarifas().get(a).getNombre(), " "));
        	}
        }
        
        adaptadorTarifas ad = new adaptadorTarifas(this,listaIYT);
        setListAdapter(ad);
	}

	public boolean onOptionsItemSelected (MenuItem item) {
		
        switch(item.getItemId()) {
        
        case NUEVA_TARIFA:
        	//Creamos un objeto tarifa con id=0 y se lo pasamos a la activity PreferencesTarifa
        	
        	tarifa t=new tarifa(0);
        	t.setNombre("Tarifa Nueva");
        	t.setMinimo(0.0);
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
        	
        	/*
        	tarifa t=new tarifa(0);
        	t.setNombre("Tarifa Nueva");
        	t.setMinimo(0.0);
        	t.setColor("Blanco");
        	t.setNumeros("");
        	Intent settingsActivity2 = new Intent(getBaseContext(), PreferencesTarifa.class );
        	Bundle extras = new Bundle();
        	extras.putInt("idTarifa", 0);
        	extras.putSerializable("tarifa", t);
        	settingsActivity2.putExtras(extras);
        	startActivityForResult(settingsActivity2,RETURN_PREFERENCES_TARIFA);
        	*/
        	TarifasPreDefinidas tsPre=new TarifasPreDefinidas();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.mn_nueva_tarifa_predefinida);
			builder.setSingleChoiceItems(tsPre.nombresTarifas(),-1, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			        //Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
					//Retorno
			    	TarifasPreDefinidas tsPre=new TarifasPreDefinidas();
			    	Log.d(TAG,"Vamos a añadir la tarifa con indice="+item);
			    	tarifa t=tsPre.getTarifa(item);
			    	ts.addTarifa(t);
			    	onStart();
			    }
			});
			AlertDialog alert = builder.create();
			alert.show();
        	
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
				Log.d(TAG,"Valor limite = "+t.getLimite());
				
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
