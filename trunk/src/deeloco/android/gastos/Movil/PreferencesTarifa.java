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
import android.app.TimePickerDialog;



public class PreferencesTarifa extends ListActivity{
	
	private static final int NUEVA_FRANJA = Menu.FIRST;
	private static final int ELIMINAR_TARIFA = Menu.FIRST+1;
	private static final String TARIFA_RETORNO = "tarifa_retorno";
	private static final String TAG = "PreferencesFranja";
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
        t = (tarifa) getIntent().getExtras().get("tarifa");
        idTarifa=(int) getIntent().getIntExtra("idTarifa", 0);
        
    	Log.d(TAG, "Nombre de la tarifa:"+t.getNombre());
    	//Editar una tarifa existente
    	//Nombre de la tarifa
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Nombre", t.getNombre()));
    	//Gasto Mínimo
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Gasto Mínimo Mensual", ""+t.getMinimo()));
    	//Color
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Color", t.getColor()));
    	//Numeros
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Numeros Asociados a la tarifa", t.getNumeros()));
    	//Data Picker
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Hora de Inicio", ""));
    	//Data Picker
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Hora de Final", ""));
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
      		//Intent settingsActivity = new Intent(getBaseContext(), Preferencias.class );
      		//startActivity(settingsActivity);
        	TextBox dialog = new TextBox(this);
        	dialog.setTitle(this.getText(R.string.tituloInputBox));
        	dialog.setValorInicial("Valor Inicial");
        	dialog.setTextBoxListener(tbListener);
        	dialog.show();
        	//Toast.makeText(getBaseContext(),"Valor: "+dialog.getValor(),Toast.LENGTH_LONG).show();
            break;
            
        case ELIMINAR_TARIFA:
        	//Hay que eliminar la franja actual
        	break;
        }
        return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		TextBox dialog = new TextBox(this);
		//Toast.makeText(getBaseContext(),"Posición: "+position,Toast.LENGTH_LONG).show();
		//Aqui es donde hay que pasar a la pantalla de Tarifas
		//int idTarifa=t.getIdentificador(l.getItemAtPosition(position).toString());
		//Esta es la manera de acceder a uno de los elementos de la view selecconada en el listview
		//TextView tv=(TextView)v.findViewById(R.id.subtitulo);
		//tv.setText("Se ha modificado");
		IconoYTexto2 iyt=(IconoYTexto2) l.getItemAtPosition(position);
		Log.d(TAG, "Campo seleccionado -> "+iyt.titulo);
		tv=(TextView)v.findViewById(R.id.subtitulo);
		switch (position)
		{
		case 0: // Nombre
			//tv=(TextView)v.findViewById(R.id.subtitulo);
        	dialog.setTitle(iyt.titulo);
        	Log.d(TAG,"Valor inicial de Nombre = "+tv.getText().toString());
        	dialog.setValorInicial(tv.getText().toString());
        	Log.d(TAG,"Valor inicial de Nombre = "+dialog.getValor());
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
        				}
        			});
        	dialog.show();
        	Log.d(TAG, "Valor Subtitulo del TextVie -> "+tv.getText());
			break;
		case 1: // Gasto Mínimo
			//tv=(TextView)v.findViewById(R.id.subtitulo);
        	dialog.setTitle(iyt.titulo);
        	Log.d(TAG,"Valor incial de Gastos Mínimo = "+tv.getText().toString());
        	dialog.setValorInicial(iyt.subtitulo);
        	dialog.setTextBoxListener(
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
        				}
        			});
        	dialog.show();
        	Log.d(TAG, "Valor Subtitulo del TextVie -> "+tv.getText());
			break;
		case 2: // Color
			//tv=(TextView)v.findViewById(R.id.subtitulo);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Selecciona un color");
			
			int indColor=indiceColor(tv.getText().toString());
			Log.d(TAG,"Valor incial de Color = "+tv.getText().toString()+", con indice "+indColor);
			builder.setSingleChoiceItems(R.array.colores,indColor, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			        //Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
			    	String colorSeleccionado=getResources().getStringArray(R.array.colores)[item];
			        Log.d(TAG,"Color seleccionado = "+colorSeleccionado);
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
		case 3: //Numeros
			//tv=(TextView)v.findViewById(R.id.subtitulo);
        	dialog.setTitle(iyt.titulo);
        	dialog.setValorInicial(iyt.subtitulo);
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
        				}
        			});
        	dialog.show();
        	Log.d(TAG, "Valor Subtitulo del TextVie -> "+tv.getText());
			break;
		case 4:
			//Data Picker
			mTimeSetListener_horaInicio =
		        new TimePickerDialog.OnTimeSetListener() {
		            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		                mHour = hourOfDay;
		                mMinute = minute;
    					tv.setText(mHour+":"+mMinute);
    					//t.setNumeros(valor);
		            }
		        };
			
			
			showDialog(TIME_DIALOG_HORA_INICIO);
			break;
		case 5:
			//Data Picker
			mTimeSetListener_horaInicio =
		        new TimePickerDialog.OnTimeSetListener() {
		            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		                mHour = hourOfDay;
		                mMinute = minute;
    					tv.setText(Time.valueOf(mHour+":"+mMinute).toString());
    					//t.setNumeros(valor);
		            }
		        };
			showDialog(TIME_DIALOG_HORA_INICIO);
			break;
		default: // Es una franja horaria
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
	
	// FUNCIONES
	
	private int indiceColor(String color){
		
		
		
		String[] colores = getResources().getStringArray(R.array.colores);
		for (int a=0;a<colores.length;a++){
			Log.d(TAG,"colores["+a+"] ="+colores[a]+", comparado con "+color);
			if (colores[a].compareTo(color)==0) return a;
		}
		
		return 0;
	}
	
}
