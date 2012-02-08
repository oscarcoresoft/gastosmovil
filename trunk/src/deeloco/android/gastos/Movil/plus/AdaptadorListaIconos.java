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
    
    Autor: Antonio CristÃ³bal Ã�lvarez AbellÃ¡n -> acabellan@gmail.com
    
    */


package deeloco.android.gastos.Movil.plus;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class AdaptadorListaIconos extends BaseAdapter {

	private Context mContext;
	private List<IconoYTexto> elementos;
	private int longCadena=12;
	
	
	public AdaptadorListaIconos(Context mContext, List<IconoYTexto> elementos)
	{
		this.mContext = mContext;
		this.elementos = elementos;
	}	
	
	public int getCount() {
		// TODO Auto-generated method stub
		// Devolvemos el total de elementos
		return elementos.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		// Devolvemos el objeto en la posiciÃ³n position
		return elementos.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		// Haremos que el identificador de cada elemento sea su posiciÃ³n
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		IconoYTexto ti = elementos.get(position);
		return generarFila(ti.icono, ti.telefono, ti.nombre,ti.fecha, ti.duracion, ti.coste);
		
	}
	
	public View generarFila(Drawable icono, String telefono,String nombre, String fecha, String duracion, double coste)
	{
		// Generamos un LinearLayout
		LinearLayout vista = new LinearLayout(mContext);
		
		// ContendrÃ¡ el nombre en una lÃ­nea y debajo la fecha
        LinearLayout datos = new LinearLayout(mContext);
        datos.setOrientation(LinearLayout.VERTICAL);

        
		//vista.setBackgroundColor(Color.parseColor("#222222"));
		vista.setBackgroundColor(Color.parseColor("#22ffffff"));
		//datos.setBackgroundColor(Color.parseColor("#222222"));
        
		// Generamos el ImageView
        ImageView img = new ImageView(mContext);
        // Le establecemos la imagen a mostrar
        img.setImageDrawable(icono);
        // Lo ponemos un "margen" (Hacia adentro)
        img.setPadding(5, 5, 10, 5);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // Lo aÃ±adimos al LinearLayout creado
        vista.addView(img,  new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));

		if (!(telefono.equals(" ")&&nombre.equals(" "))) //Si no tiene telefono y nombre = Una sola lÃ­nea
		{
			 
	        //Contendra dos lineas de texto en horozantal, una al lado de la otra
	        LinearLayout linea1=new LinearLayout(mContext);
	        linea1.setOrientation(LinearLayout.HORIZONTAL);
	        
	        
	        // Inicio
	        TextView txtLinea1Izq = new TextView(mContext,null,android.R.attr.textAppearanceMedium);
	        if (nombre.equals("")||nombre.equals(telefono))
	        {
	        	txtLinea1Izq.setText(telefono);
	        	txtLinea1Izq.setMaxWidth(250);
	        }
	        else
	        {
	        	txtLinea1Izq.setText(telefono+" :: "+nombre);
	        	if (nombre.length()>this.longCadena)
	        		txtLinea1Izq.setText(nombre.subSequence(0, this.longCadena)+" | "+telefono);
	        	else
	        		txtLinea1Izq.setText(nombre+" | "+telefono);
	        	txtLinea1Izq.setMaxWidth(250);
	        }
	        
	        //txtLinea1Izq.setMaxWidth(250);
	        txtLinea1Izq.setSingleLine(true);
	        txtLinea1Izq.setTextSize(17);
	        //txtLinea1Izq.setTextAppearance(mContext,android.R.attr.textAppearanceLarge);
	        linea1.addView(txtLinea1Izq, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,2));
	        // Final
	        
	        // Inicio
	        TextView txtLinea1Der = new TextView(mContext,null,android.R.attr.textAppearanceMedium);
	        if (coste==-1.0)
	        {
	        	txtLinea1Der.setText("--.--");
	        	
	        }
	        else
	        {
	        	if (duracion.equals(" ")&&fecha.equals(" ")&&coste==0.0)
	        		txtLinea1Der.setText(" ");
	        	else
	        		txtLinea1Der.setText(coste+FunGlobales.monedaLocal());
	        }
	        
	        txtLinea1Der.setTextSize(17);
	        //txtLinea1Der.setTextAppearance(mContext,android.R.attr.textAppearanceLarge);
	        txtLinea1Der.setGravity(Gravity.RIGHT);
	        txtLinea1Der.setPadding(0,0,15,0);
	        linea1.addView(txtLinea1Der, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1));
	        //Final
	        datos.addView(linea1, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	        
	     // Generamos el TextView para la fecha y hora
	        TextView vFecha = new TextView(mContext,null,android.R.attr.textAppearanceSmall);
	        // Le establecemos el texto a mostrar
	        

	        if (duracion.equals(" ")&&fecha.equals(" "))
	        	vFecha.setText(" ");
	        else
	        	vFecha.setText(duracion+ "|" +fecha);
	        vFecha.setTextAppearance(mContext,android.R.attr.textAppearanceSmall);
	        //vFecha.setTextSize(15);
	        if ((telefono.equals(" ")&&nombre.equals(" "))) //Si no tiene telefono y nombre = Una sola lÃ­nea
			{
	        	vFecha.setTextColor(Color.parseColor("#FF8000"));
			}
	        
	        
	        
	        // Lo aÃ±adimos al LinearLayout "datos"
	        
	        datos.addView(vFecha, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
		else
		{
			//No incluimos la lÃ­nea de arriba y colocamos un fondo de escritorio diferente
			//vista.setBackgroundColor(Color.BLACK);
			//datos.setBackgroundColor(Color.BLACK);
			vista.setBackgroundColor(Color.parseColor("#88000000"));
			
			LinearLayout linea2=new LinearLayout(mContext);
		    linea2.setOrientation(LinearLayout.HORIZONTAL);
		    
		    TextView txtLinea2Izq = new TextView(mContext,null,android.R.attr.textAppearanceSmall);
			txtLinea2Izq.setText(duracion+ "|" +fecha);
			txtLinea2Izq.setTextAppearance(mContext,android.R.attr.textAppearanceSmall);
			txtLinea2Izq.setSingleLine(true);
			txtLinea2Izq.setTextColor(Color.parseColor("#FF8000"));
			txtLinea2Izq.setMaxWidth(250);
			txtLinea2Izq.setSingleLine(true);
			txtLinea2Izq.setGravity(Gravity.LEFT);
	        
	        linea2.addView(txtLinea2Izq, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,2));
		    
			TextView txtLinea2Der = new TextView(mContext,null,android.R.attr.textAppearanceSmall);
			txtLinea2Der.setText(coste+FunGlobales.monedaLocal());
			txtLinea2Der.setTextAppearance(mContext,android.R.attr.textAppearanceSmall);
			txtLinea2Der.setGravity(Gravity.RIGHT);
			txtLinea2Der.setPadding(0,0,15,0);
			txtLinea2Der.setTextColor(Color.parseColor("#FF8000"));
			linea2.addView(txtLinea2Der, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1));
			
	        datos.addView(linea2, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			
		}
		
        

        
        if (fecha.equals(" "))
        {
        	Log.d("", "Ampliar la lÃ­nea");
        	datos.setPadding(0, 20, 0, 20);
        }
    
          // Lo aÃ±adimos al LinearLayout creado
          vista.addView(datos, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT)); 
        
        
	    
	    // Devolvemos la vista que representa a una fila
		return vista;
	}

}
