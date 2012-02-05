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


package deeloco.android.gastos.Movil;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class AdaptadorListaIconos2 extends BaseAdapter {

	private Context mContext;
	private List<IconoYTexto> elementos;
	
	public AdaptadorListaIconos2(Context mContext, List<IconoYTexto> elementos)
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
	
	public View generarFila(Drawable icono, String telefono, String nombre,String fecha, String duracion, double coste)
	{
		// Generamos un LinearLayout
		LinearLayout vista = new LinearLayout(mContext);
		
		// Generamos el ImageView
        ImageView img = new ImageView(mContext);
        // Le establecemos la imagen a mostrar
        img.setImageDrawable(icono);
        // Lo ponemos un "margen" (Hacia adentro)
        img.setPadding(10, 10, 10, 10);
        // TamaÃ±o
        img.setMinimumHeight(50);
        // Lo aÃ±adimos al LinearLayout creado
        vista.addView(img,  new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
      
      
        // ContendrÃ¡ el nombre en una lÃ­nea y debajo la fecha
        LinearLayout datos = new LinearLayout(mContext);
        datos.setOrientation(LinearLayout.VERTICAL);
        
        //Contendra dos lineas de texto en horozantal, una al lado de la otra
        LinearLayout linea1=new LinearLayout(mContext);
        linea1.setOrientation(LinearLayout.HORIZONTAL);
        
        //Contendra dos lineas de texto en horozantal, una al lado de la otra
        LinearLayout linea2=new LinearLayout(mContext);
        linea2.setOrientation(LinearLayout.HORIZONTAL);
        
        	
            // Inicio
            TextView txtLinea1Izq = new TextView(mContext,null,android.R.attr.textAppearanceMedium);
            txtLinea1Izq.setText(telefono);
            txtLinea1Izq.setMaxWidth(150);
            txtLinea1Izq.setSingleLine(true);
            txtLinea1Izq.setTextAppearance(mContext,android.R.attr.textAppearanceLarge);
            linea1.addView(txtLinea1Izq, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,2));
            // Final
            
            // Inicio
            TextView txtLinea1Der = new TextView(mContext,null,android.R.attr.textAppearanceMedium);
            if (nombre.compareTo("")==0)
            {
            	//Gastos por numero
            	txtLinea1Der.setText(coste+"â‚¬");
            }
            else
            {
            	//DuraciÃ³n por numero. nombre=duraciÃ³n
            	txtLinea1Der.setText(nombre);
            }
            
            txtLinea1Der.setTextAppearance(mContext,android.R.attr.textAppearanceLarge);
            txtLinea1Der.setGravity(Gravity.RIGHT);
            txtLinea1Der.setPadding(0,0,15,0);
            linea1.addView(txtLinea1Der, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1));
            //Final
        datos.addView(linea1, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            
	     // Inicio
	        TextView txtLinea2Izq = new TextView(mContext,null,android.R.attr.textAppearanceSmall);
	        txtLinea2Izq.setText(duracion); 
	        txtLinea2Izq.setTextAppearance(mContext,android.R.attr.textAppearanceSmall);
	        linea2.addView(txtLinea2Izq, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,2));
	        // Final
	        
	        // Inicio
	        TextView txtLinea2Der = new TextView(mContext,null,android.R.attr.textAppearanceSmall);
	        txtLinea2Der.setText(fecha+"%");
	        txtLinea2Der.setTextAppearance(mContext,android.R.attr.textAppearanceSmall);
	        txtLinea2Der.setGravity(Gravity.RIGHT);
	        txtLinea2Der.setPadding(0,0,15,0);
	        linea2.addView(txtLinea2Der, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1));
	        //Final
	    datos.addView(linea2, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	        
            
            
          // Lo aÃ±adimos al LinearLayout creado
          vista.addView(datos, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT)); 
        
        
	    
	    // Devolvemos la vista que representa a una fila
		return vista;
	}

}
