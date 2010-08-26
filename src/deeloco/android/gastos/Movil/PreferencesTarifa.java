package deeloco.android.gastos.Movil;


import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import deeloco.android.gastos.Movil.TextBox.TextBoxListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.EditText;
import android.app.TimePickerDialog;


public class PreferencesTarifa extends ListActivity{
	
	private static final int NUEVA_FRANJA = Menu.FIRST;
	private static final int ELIMINAR_TARIFA = Menu.FIRST+1;
	private static final String TARIFA_RETORNO = "tarifa_retorno";
	private static final String FRANJA_RETORNO = "franja_retorno";
	private static final String TAG = "PreferencesTarifa";
	private static final int RETURN_PREFERENCES_FRANJA=1;
	static final int TIME_DIALOG_HORA_INICIO = 0;
	private tarifa t;
	private int idTarifa;
	private List<IconoYTexto2> listaIYT = new ArrayList<IconoYTexto2>();
	TextView tv;
	private int mHour;
	private int mMinute;
	private TimePickerDialog.OnTimeSetListener mTimeSetListener_horaInicio;
	
	
    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(Menu.NONE, NUEVA_FRANJA, 0, R.string.mn_nueva_franja).setIcon(android.R.drawable.ic_menu_add);
    	menu.add(Menu.NONE, ELIMINAR_TARIFA, 0, R.string.mn_eliminar_tarifa).setIcon(android.R.drawable.ic_menu_delete);
    	return true;
    }
	
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case TIME_DIALOG_HORA_INICIO:
            return new TimePickerDialog(this, mTimeSetListener_horaInicio, mHour, mMinute, true);
        }
        return null;
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tarifas);
        TextView cabecera=(TextView) findViewById(R.id.cabTarifa);
        cabecera.setText("TARIFA");
        t = (tarifa) getIntent().getExtras().get("tarifa");

    }
    
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		listaIYT.clear();
		idTarifa=(int) getIntent().getIntExtra("idTarifa", 0);
    	//Log.d(TAG, "Nombre de la tarifa:"+t.getNombre());
    	//Editar una tarifa existente
    	//Nombre de la tarifa
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Nombre", t.getNombre()));
    	//Gasto Mínimo
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Gasto Mínimo Mensual", ""+t.getMinimo()));
    	//Limite
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Límite de llamadas (minutos)", ""+t.getLimite()));
    	//Color
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Color", t.getColor()));
    	//Numeros
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Numeros Asociados a la tarifa", t.getNumeros()));
    	//Una entrada para cada franja que tiene la tarifa
    	for (int a=0;a<t.getNumFranjas();a++)
    	{
    		listaIYT.add(new IconoYTexto2(getResources().getDrawable(android.R.drawable.ic_menu_more),"Franja Horaria" ,t.getFranjas().get(a).getNombre()));
    	}

        adaptadorTarifas ad = new adaptadorTarifas(this,listaIYT);
        setListAdapter(ad);
		
		
		
		
		
	}
    
    
	public boolean onOptionsItemSelected (MenuItem item) {
		
        switch(item.getItemId()) {
        
        case NUEVA_FRANJA:
        	Franja f=new Franja(0);
        	f.setNombre("Franja Nueva");
        	f.setHoraInicio("00:00:00");
        	f.setHoraFinal("23:00:00");
        	f.setDias("[Lun,Mar,Mie,Jue,Vie,Sab,Dom]");
        	f.setCoste(0.0);
        	f.setEstablecimiento(0.0);
        	f.setLimite(false);
        	f.setCosteFueraLimite(0.0);
        	Intent settingsActivity2 = new Intent(getBaseContext(), PreferencesFranja.class );
        	Bundle extras = new Bundle();
        	//Log.d(TAG,"Pasamos la franja NUEVA : "+f.getNombre());
        	extras.putInt("idFranja", 0);
        	extras.putSerializable("franja", f);
        	settingsActivity2.putExtras(extras);
        	startActivityForResult(settingsActivity2,RETURN_PREFERENCES_FRANJA);
            break; 
            
        case ELIMINAR_TARIFA:
        	//Hay que eliminar la franja actual
	    	Intent resultIntent=new Intent();
	    	t.setIdentificador(-1);
	    	resultIntent.putExtra(TARIFA_RETORNO, t);
	    	setResult(Activity.RESULT_OK, resultIntent);
	    	finish();
        	
        	break;
        }
        return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		TextBox dialog = new TextBox(this);
		//Log.d(TAG,"Valor de Nombre de la tarifa: "+t.getNombre());
		//Toast.makeText(getBaseContext(),"Posición: "+position,Toast.LENGTH_LONG).show();
		//Aqui es donde hay que pasar a la pantalla de Tarifas
		//int idTarifa=t.getIdentificador(l.getItemAtPosition(position).toString());
		//Esta es la manera de acceder a uno de los elementos de la view selecconada en el listview
		//TextView tv=(TextView)v.findViewById(R.id.subtitulo);
		//tv.setText("Se ha modificado");
		IconoYTexto2 iyt=(IconoYTexto2) l.getItemAtPosition(position);
		//Log.d(TAG, "Campo seleccionado -> "+iyt.titulo);
		tv=(TextView)v.findViewById(R.id.subtitulo);
		//Log.d(TAG,"Valor de subtitulo ANTES= "+tv.getText());
		switch (position)
		{
		case 0: // Nombre
			//tv=(TextView)v.findViewById(R.id.subtitulo);
			tv=(TextView)v.findViewById(R.id.subtitulo);
        	dialog.setTitle(iyt.titulo);
        	dialog.setValorInicial(t.getNombre());
        	dialog.setSubtitulo(getString(R.string.aj_tarifa_nombre_des));
        	dialog.setTextBoxListener(
        			new TextBoxListener() {
        				@Override
        				public void onOkClick(String valor) {
        					// TODO Auto-generated method stub
        					//Toast.makeText(getBaseContext(),"Retorno de : "+valor,Toast.LENGTH_LONG).show();
        					tv.setText(valor);
        					t.setNombre(valor);
        					
        					//Retorno
        			    	Intent resultIntent=new Intent();
        			    	resultIntent.putExtra(TARIFA_RETORNO, t);
        			    	setResult(Activity.RESULT_OK, resultIntent);
        			    	onStart();
        				}
        			});
        	dialog.show();
        	//Log.d(TAG, "Valor Subtitulo del TextVie -> "+tv.getText());
        	
			break;
		case 1: // Gasto Mínimo
			//tv=(TextView)v.findViewById(R.id.subtitulo);
			TextBox dialog2 = new TextBox(this);
			dialog2.setTitle(iyt.titulo);
        	//Log.d(TAG,"Valor incial de Gastos Mínimo = "+tv.getText().toString());
        	dialog2.setValorInicial(""+t.getMinimo());
        	dialog2.setSubtitulo(getString(R.string.aj_tarifa_gastoMinimo_des));
        	dialog2.setTextBoxListener(
        			new TextBoxListener() {
        				@Override
        				public void onOkClick(String valor) {
        					// TODO Auto-generated method stub
        					//Toast.makeText(getBaseContext(),"Retorno de : "+valor,Toast.LENGTH_LONG).show();
        					tv.setText(valor);
        					t.setMinimo(Double.parseDouble(valor));
        					//Retorno
        			    	Intent resultIntent=new Intent();
        			    	resultIntent.putExtra(TARIFA_RETORNO, t);
        			    	setResult(Activity.RESULT_OK, resultIntent);
        			    	onStart();
        				}
        			});
        	dialog2.show();
        	//Log.d(TAG, "Valor Subtitulo del TextVie -> "+tv.getText());
			break;
		case 2: // Limite llamadas
			//tv=(TextView)v.findViewById(R.id.subtitulo);
			dialog.setTitle(iyt.titulo);
        	//Log.d(TAG,"Valor inicial de Limite llamadas = "+tv.getText().toString());
        	dialog.setValorInicial(""+t.getLimite());
        	dialog.setSubtitulo(getString(R.string.aj_tarifa_limite_des));
        	dialog.setTextBoxListener(
        			new TextBoxListener() {
        				@Override
        				public void onOkClick(String valor) {
        					// TODO Auto-generated method stub
        					//Toast.makeText(getBaseContext(),"Retorno de : "+valor,Toast.LENGTH_LONG).show();
        					tv.setText(valor);
        					t.setLimite(Integer.parseInt(valor));
        					//Log.d(TAG,"Limite = "+t.getLimite());
        					//Retorno
        			    	Intent resultIntent=new Intent();
        			    	resultIntent.putExtra(TARIFA_RETORNO, t);
        			    	setResult(Activity.RESULT_OK, resultIntent);
        			    	onStart();
        				}
        			});
        	dialog.show();
        	//Log.d(TAG, "Valor Subtitulo del TextVie -> "+tv.getText());
			break;
			
		case 3: // Color
			//tv=(TextView)v.findViewById(R.id.subtitulo);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(iyt.titulo);
			
			int indColor=indiceColor(tv.getText().toString());
			//Log.d(TAG,"Valor incial de Color = "+tv.getText().toString()+", con indice "+indColor);
			builder.setSingleChoiceItems(R.array.colores,indColor, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			        //Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
			    	String colorSeleccionado=getResources().getStringArray(R.array.colores)[item];
			        tv.setText(colorSeleccionado);
			        t.setColor(colorSeleccionado);
					//Retorno
			    	Intent resultIntent=new Intent();
			    	resultIntent.putExtra(TARIFA_RETORNO, t);
			    	setResult(Activity.RESULT_OK, resultIntent);
			    }
			});
			AlertDialog alert = builder.create();
			alert.show();
			break;
		case 4: //Numeros
			//tv=(TextView)v.findViewById(R.id.subtitulo);
        	dialog.setTitle(iyt.titulo);
        	dialog.setValorInicial(t.getNumeros());
        	dialog.setSubtitulo(getString(R.string.aj_tarifa_numeros_des));
        	dialog.setTextBoxListener(
        			new TextBoxListener() {
        				@Override
        				public void onOkClick(String valor) {
        					// TODO Auto-generated method stub
        					//Toast.makeText(getBaseContext(),"Retorno de : "+valor,Toast.LENGTH_LONG).show();
        					tv.setText(valor);
        					t.setNumeros(valor);
        					//Retorno
        			    	Intent resultIntent=new Intent();
        			    	resultIntent.putExtra(TARIFA_RETORNO, t);
        			    	setResult(Activity.RESULT_OK, resultIntent);
        			    	onStart();
        				}
        			});
        	dialog.show();
			break;
		default: // Es una franja horaria
			//Aqui es donde hay que pasar a la pantalla de Franjas
			
			iyt=(IconoYTexto2) l.getItemAtPosition(position);
			int idFranja=t.getIdentificador(iyt.subtitulo);
	    	Intent settingsActivity2 = new Intent(getBaseContext(), PreferencesFranja.class );
	    	Franja f = t.getFranja(idFranja);
	    	Bundle extras = new Bundle();
	    	extras.putInt("idFranja", idFranja);
	    	extras.putString("nombreTarifa", t.getNombre());
	    	extras.putSerializable("franja", f);
	    	settingsActivity2.putExtras(extras);
	    	//startActivity(settingsActivity2);
	    	startActivityForResult(settingsActivity2, RETURN_PREFERENCES_FRANJA);
			break;
		}
	}
	
	TextBoxListener tbListener=new TextBoxListener() {
		
		
		@Override
		public void onOkClick(String valor) {
			// TODO Auto-generated method stub
			Toast.makeText(getBaseContext(),"Retorno de : "+valor,Toast.LENGTH_LONG).show();
			
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//Log.d(TAG,"requestCode en onActivityResult = "+requestCode+" - RETURN_PREFERENCES_TARIFA = "+RETURN_PREFERENCES_FRANJA);
		switch (requestCode) {
		case RETURN_PREFERENCES_FRANJA:
			//Log.d(TAG,"resultCode en onActivityResult = "+resultCode+" - Activity.RESULT_OK = "+Activity.RESULT_OK);
			if (resultCode==Activity.RESULT_OK)
			{
				Franja f=(Franja) data.getSerializableExtra(FRANJA_RETORNO);
				//Log.d(TAG,"La franja retornada es "+f.getNombre());
				//Añadir la franja al ArrayList de tarifa
				
				switch (f.getIdentificador()) {
				case 0:
					t.addFranja(f); //Franja Nueva
					break;
					
				case -1:
					//Eliminar la franja
					t.deleteFranja(f.getNombre());
					break;

				default:
					t.modificarFranja(f.getIdentificador(), f);
					break;
				}
				
				//Retorno
		    	Intent resultIntent=new Intent();
		    	resultIntent.putExtra(TARIFA_RETORNO, t);
		    	setResult(Activity.RESULT_OK, resultIntent);
			}
			break;

		default:
			break;
		}
	}
	
	// FUNCIONES
	
	private int indiceColor(String color){

		String[] colores = getResources().getStringArray(R.array.colores);
		for (int a=0;a<colores.length;a++){
			//Log.d(TAG,"colores["+a+"] ="+colores[a]+", comparado con "+color);
			if (colores[a].compareTo(color)==0) return a;
		}
		
		return 0;
	}




	
}
