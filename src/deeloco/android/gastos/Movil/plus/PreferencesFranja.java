package deeloco.android.gastos.Movil.plus;


import java.util.ArrayList;
import java.util.List;

import deeloco.android.gastos.Movil.plus.R;
import deeloco.android.gastos.Movil.plus.TextBox.TextBoxListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class PreferencesFranja extends ListActivity{
	
	private static final int ELIMINAR_FRANJA = Menu.FIRST;
	private static final String FRANJA_RETORNO = "franja_retorno";
	//private static final String TAG = "PreferencesFranja";
	//private static final int RETURN_PREFERENCES_FRANJA=1;
	static final int TIME_DIALOG_HORA_INICIO = 0;
	static final int TIME_DIALOG_HORA_FINAL = 1;
	private Franja f;
	int idFranja;
	String nombreTarifa;
	private List<IconoYTexto2> listaIYT = new ArrayList<IconoYTexto2>();
	TextView tv;
	private TimePickerDialog.OnTimeSetListener mTimeSetListener_horaInicio;
	private TimePickerDialog.OnTimeSetListener mTimeSetListener_horaFinal;
	
	
    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(Menu.NONE, ELIMINAR_FRANJA, 0, R.string.mn_eliminar_franja).setIcon(android.R.drawable.ic_menu_delete);
    	return true;
    }
	
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case TIME_DIALOG_HORA_INICIO:
            return new TimePickerDialog(this, mTimeSetListener_horaInicio, 0, 0, true);
            
        case TIME_DIALOG_HORA_FINAL:
            return new TimePickerDialog(this, mTimeSetListener_horaFinal, 0, 0, true);
        }
        return null;
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        f = (Franja) getIntent().getExtras().get("franja");
        idFranja=(int) getIntent().getIntExtra("idFranja", 0);
        nombreTarifa=(String) getIntent().getStringExtra("nombreTarifa");
        setContentView(R.layout.tarifas);
        TextView cabecera=(TextView) findViewById(R.id.cabTarifa);
        cabecera.setText(getString(R.string.cabFranja)+" "+nombreTarifa);

    }

    

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		listaIYT.clear();
    	//Editar una franja existente
    	//Nombre de la franja
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Nombre", f.getNombre()));
    	//Hora Inicio
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Hora de Inicio", ""+f.getHoraInicio()));
    	//Hora Final
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Hora de Final", ""+f.getHoraFinal()));
    	//Dias
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Días de la semana", f.getDias().toString()));
    	//Coste de la llamada
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Coste de la llamada", ""+f.getCoste()));
    	//Establecimiento de la llamada
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Establecimiento de la llamada", ""+f.getEstablecimiento()));
    	//Limite
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Contabilizar las llamadas para el limite", ""+f.getLimiteSiNo()));
    	//Coste fuera del limite
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Coste pasado los limite", ""+f.getCosteFueraLimite()));
    	//Coste fuera del limite
    	listaIYT.add(new IconoYTexto2(getResources().getDrawable(R.drawable.vacio), "Establecimiento pasado los limite", ""+f.getEstablecimientoFueraLimite()));

        adaptadorTarifas ad = new adaptadorTarifas(this,listaIYT);
        setListAdapter(ad);
		
		
	}
    
    
	public boolean onOptionsItemSelected (MenuItem item) {
		
        switch(item.getItemId()) {
        
        case ELIMINAR_FRANJA:
	    	Intent resultIntent=new Intent();
	    	f.setIdentificador(-1);
	    	resultIntent.putExtra(FRANJA_RETORNO, f);
	    	setResult(Activity.RESULT_OK, resultIntent);
	    	finish();
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
		tv=(TextView)v.findViewById(R.id.subtitulo);
		switch (position)
		{
		case 0: // Nombre
			//tv=(TextView)v.findViewById(R.id.subtitulo);
        	dialog.setTitle(iyt.titulo);
        	dialog.setValorInicial(f.getNombre());
        	dialog.setSubtitulo(getString(R.string.aj_franja_nombre_des));
        	dialog.setTextBoxListener(
        			new TextBoxListener() {
        				//@Override
        				public void onOkClick(String valor) {
        					// TODO Auto-generated method stub
        					//Toast.makeText(getBaseContext(),"Retorno de : "+valor,Toast.LENGTH_LONG).show();
        					tv.setText(valor);
        					f.setNombre(valor);
        					//Retorno
        			    	Intent resultIntent=new Intent();
        			    	resultIntent.putExtra(FRANJA_RETORNO, f);
        			    	setResult(Activity.RESULT_OK, resultIntent);
        			    	onStart();
        				}
        			});
        	dialog.show();
			break;
		case 1: // Hora de Inicio
			//tv=(TextView)v.findViewById(R.id.subtitulo);
			//Data Picker
			mTimeSetListener_horaInicio =
		        new TimePickerDialog.OnTimeSetListener() {
		            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    					tv.setText(hourOfDay+":"+minute+":00");
    					f.setHoraInicio(hourOfDay+":"+minute+":00");
    					//Retorno
    			    	Intent resultIntent=new Intent();
    			    	resultIntent.putExtra(FRANJA_RETORNO, f);
    			    	setResult(Activity.RESULT_OK, resultIntent);
		            }
		        };
		        showDialog(TIME_DIALOG_HORA_INICIO);
			break;
		case 2: // Hora Final
			//tv=(TextView)v.findViewById(R.id.subtitulo);
			//Data Picker
			mTimeSetListener_horaFinal =
		        new TimePickerDialog.OnTimeSetListener() {
		            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    					tv.setText(hourOfDay+":"+minute+":00");
    					f.setHoraFinal(hourOfDay+":"+minute+":00");
    					//Retorno
    			    	Intent resultIntent=new Intent();
    			    	resultIntent.putExtra(FRANJA_RETORNO, f);
    			    	setResult(Activity.RESULT_OK, resultIntent);
		            }
		        };
		        showDialog(TIME_DIALOG_HORA_FINAL);
			break;
		case 3: //Dias
			//tv=(TextView)v.findViewById(R.id.subtitulo);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.aj_franja_dias);
			
			//int indColor=indiceColor(tv.getText().toString());
			//Log.d(TAG,"Valor incial de Color = "+tv.getText().toString()+", con indice "+indColor);
			builder.setMultiChoiceItems(R.array.semana,f.diasSeleccionados(), new DialogInterface.OnMultiChoiceClickListener() {
				
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					// TODO Auto-generated method stub
			        //Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
			    	String diaSeleccionado=getResources().getStringArray(R.array.semana)[which];
			        
			        if (isChecked) f.addDia(diaSeleccionado);
			        else f.deleteDia(diaSeleccionado);
			        	
			        tv.setText(f.getDias().toString());
			        
					//Retorno
			    	Intent resultIntent=new Intent();
			    	resultIntent.putExtra(FRANJA_RETORNO, f);
			    	setResult(Activity.RESULT_OK, resultIntent);
			}
			});
			AlertDialog alert = builder.create();
			alert.show();

			break;
		case 4://Coste de la llamada
			//Data Picker
        	dialog.setTitle(iyt.titulo);
        	dialog.setValorInicial(""+f.getCoste());
        	dialog.setSubtitulo(getString(R.string.aj_franja_coste_des));
        	dialog.setTextBoxListener(
        			new TextBoxListener() {
        				public void onOkClick(String valor) {
        					// TODO Auto-generated method stub
        					//Toast.makeText(getBaseContext(),"Retorno de : "+valor,Toast.LENGTH_LONG).show();
        					tv.setText(valor);
        					f.setCoste(valor);
        					//Retorno
        			    	Intent resultIntent=new Intent();
        			    	resultIntent.putExtra(FRANJA_RETORNO, f);
        			    	setResult(Activity.RESULT_OK, resultIntent);
        			    	onStart();
        				}
        			});
        	dialog.show();
			break;
		case 5: //Establecimento de llamada
			//Data Picker
        	dialog.setTitle(iyt.titulo);
        	dialog.setValorInicial(""+f.getEstablecimiento());
        	dialog.setSubtitulo(getString(R.string.aj_franja_establecimiento_des));
        	dialog.setTextBoxListener(
        			new TextBoxListener() {
        				public void onOkClick(String valor) {
        					// TODO Auto-generated method stub
        					//Toast.makeText(getBaseContext(),"Retorno de : "+valor,Toast.LENGTH_LONG).show();
        					tv.setText(valor);
        					f.setEstablecimiento(valor);
        					//Retorno
        			    	Intent resultIntent=new Intent();
        			    	resultIntent.putExtra(FRANJA_RETORNO, f);
        			    	setResult(Activity.RESULT_OK, resultIntent);
        			    	onStart();
        				}
        			});
        	dialog.show();
			break;
			
		case 6: //Limite
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			builder2.setTitle(iyt.titulo);
			int defecto=1;
			if (f.getLimite()) defecto=0;
			//Log.d(TAG,"Valor inicial de Color = "+tv.getText().toString()+", con indice "+0);
			builder2.setSingleChoiceItems(R.array.sino,defecto, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			        //Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
			    	String colorSeleccionado=getResources().getStringArray(R.array.sino)[item];
			        tv.setText(colorSeleccionado);
			        f.setLimite(colorSeleccionado);
					//Retorno
			    	Intent resultIntent=new Intent();
			    	resultIntent.putExtra(FRANJA_RETORNO, f);
			    	setResult(Activity.RESULT_OK, resultIntent);
			        
			    }
			});
			AlertDialog alert2 = builder2.create();
			alert2.show();

			break;
			
		case 7: //Coste fuera del limite
			//Data Picker
        	dialog.setTitle(iyt.titulo);
        	dialog.setValorInicial(""+f.getCosteFueraLimite());
        	dialog.setSubtitulo(getString(R.string.aj_franja_coste_limite_des));
        	dialog.setTextBoxListener(
        			new TextBoxListener() {
        				public void onOkClick(String valor) {
        					// TODO Auto-generated method stub
        					//Toast.makeText(getBaseContext(),"Retorno de : "+valor,Toast.LENGTH_LONG).show();
        					tv.setText(valor);
        					f.setCosteFueraLimite(valor);
        					//Retorno
        			    	Intent resultIntent=new Intent();
        			    	resultIntent.putExtra(FRANJA_RETORNO, f);
        			    	setResult(Activity.RESULT_OK, resultIntent);
        			    	onStart();
        				}
        			});
        	dialog.show();
			break;
			
		case 8: //Establecimiento de llamada fuera del limite
			//Data Picker
        	dialog.setTitle(iyt.titulo);
        	dialog.setValorInicial(""+f.getEstablecimientoFueraLimite());
        	dialog.setSubtitulo(getString(R.string.aj_franja_establecimiento_limite_des));
        	dialog.setTextBoxListener(
        			new TextBoxListener() {
        				public void onOkClick(String valor) {
        					// TODO Auto-generated method stub
        					//Toast.makeText(getBaseContext(),"Retorno de : "+valor,Toast.LENGTH_LONG).show();
        					tv.setText(valor);
        					f.setEstablecimientoFueraLimite(valor);
        					//Retorno
        			    	Intent resultIntent=new Intent();
        			    	resultIntent.putExtra(FRANJA_RETORNO, f);
        			    	setResult(Activity.RESULT_OK, resultIntent);
        			    	onStart();
        				}
        			});
        	dialog.show();
			break;	
			
			
		default: // Es una franja horaria
			break;
		}
		
		
		

	}
	
	TextBoxListener tbListener=new TextBoxListener() {
		
		
		public void onOkClick(String valor) {
			// TODO Auto-generated method stub
			Toast.makeText(getBaseContext(),"Retorno de : "+valor,Toast.LENGTH_LONG).show();
		}
	};


	
	// FUNCIONES
	
	

}
