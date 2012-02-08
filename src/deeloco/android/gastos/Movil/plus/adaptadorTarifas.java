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


package deeloco.android.gastos.Movil.plus;

import java.util.List;

import deeloco.android.gastos.Movil.plus.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;

public class adaptadorTarifas extends BaseAdapter {

	private Context mContext;
	private List<IconoYTexto2> elementos;
	
	public adaptadorTarifas(Context mContext, List<IconoYTexto2> elementos)
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
		// Devolvemos el objeto en la posición position
		return elementos.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		// Haremos que el identificador de cada elemento sea su posición
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		IconoYTexto2 elemento = elementos.get(position);
		return generarFila(elemento);
		
	}
	
	public View generarFila(IconoYTexto2 elemento)
	{
        // Cargamos el XML en memoria generando la interfaz
        View vista = View.inflate(mContext,R.layout.doslineastexto, null);
        
        // Rellenamos los datos, tanto del icono, como del texto
        TextView titulo = (TextView)vista.findViewById(R.id.titulo);
        titulo.setText(elemento.titulo);
        TextView subtitulo = (TextView)vista.findViewById(R.id.subtitulo);
        subtitulo.setText(elemento.subtitulo);
        ImageView imagen= (ImageView) vista.findViewById(R.id.imagen);
        imagen.setImageDrawable(elemento.icono);
	    
	    // Devolvemos la vista que representa a una fila
		return vista;
	}

}
