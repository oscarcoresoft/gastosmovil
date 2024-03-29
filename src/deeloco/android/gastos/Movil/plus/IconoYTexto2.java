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

import android.graphics.drawable.Drawable;

public class IconoYTexto2 {
	public Drawable icono;
	/* A Drawable is a general abstraction for "something that can be drawn."*/
	public String titulo;
	public String subtitulo;

	
	public IconoYTexto2(Drawable icono, String titulo, String subtitulo) {
		this.icono = icono;
		this.titulo = titulo;
		this.subtitulo=subtitulo;
    }
}
