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
    
    Autor: Antonio Cristóbal Álvarez Abellán -> acabellan@gmail.com
    
    */


package deeloco.android.gastos.Movil;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
	
	public AdaptadorListaIconos(Context mContext, List<IconoYTexto> elementos)
	{
		this.mContext = mContext;
		this.elementos = elementos;
	}	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// Devolvemos el total de elementos
		return elementos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		// Devolvemos el objeto en la posición position
		return elementos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		// Haremos que el identificador de cada elemento sea su posición
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		IconoYTexto ti = elementos.get(position);
		return generarFila(ti.icono, ti.telefono, ti.fecha, ti.duracion, ti.coste);
		
	}
	
	public View generarFila(Drawable icono, String telefono, String fecha, String duracion, double coste)
	{
		// Generamos un LinearLayout
		LinearLayout vista = new LinearLayout(mContext);
		
		// Generamos el ImageView
        ImageView img = new ImageView(mContext);
        // Le establecemos la imagen a mostrar
        img.setImageDrawable(icono);
        // Lo ponemos un "margen" (Hacia adentro)
        img.setPadding(5, 5, 5, 5);
        // Lo añadimos al LinearLayout creado
        vista.addView(img,  new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
      
      
        // Contendrá el nombre en una línea y debajo la fecha
        LinearLayout datos = new LinearLayout(mContext);
        datos.setOrientation(LinearLayout.VERTICAL);
           
            // Generamos el TextView para el nombre en negrita
            TextView txt = new TextView(mContext,null,android.R.attr.textAppearanceMedium);
            
            // Le establecemos el texto a mostrar
            txt.setText(telefono + " : " + coste + FunGlobales.monedaLocal());
            // Lo ponemos en negrita
            //txt.setTypeface(Typeface.DEFAULT_BOLD);
            txt.setTextAppearance(mContext,android.R.attr.textAppearanceLarge);
            //txt.setTextSize(30);

            // Lo añadimos al LinearLayout "datos"
            datos.addView(txt, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
           
            // Generamos el TextView para la fecha y hora
            TextView vFecha = new TextView(mContext,null,android.R.attr.textAppearanceSmall);
            // Le establecemos el texto a mostrar
            vFecha.setText(duracion+ "   " +fecha);
            vFecha.setTextAppearance(mContext,android.R.attr.textAppearanceSmall);
            //vFecha.setTextSize(15);
            // Lo añadimos al LinearLayout "datos"
            datos.addView(vFecha, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
          // Lo añadimos al LinearLayout creado
          vista.addView(datos, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)); 
        
        
	    
	    // Devolvemos la vista que representa a una fila
		return vista;
	}

}
