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

import deeloco.android.gastos.Movil.R;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Preferencias extends PreferenceActivity  implements OnSharedPreferenceChangeListener {
	
	//private PreferenceScreen psRaiz=getPreferenceScreen();
	
	ValoresPreferencias vp=new ValoresPreferencias(this);
	private PreferenceScreen ps;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		final PreferenceScreen psRaiz = getPreferenceScreen();
		ps = (PreferenceScreen) psRaiz.findPreference("psTarifaEsp1");
		ps.setSummary(vp.getPrefEsp1Nombre());
		ps = (PreferenceScreen) psRaiz.findPreference("psTarifaEsp2");
		ps.setSummary(vp.getPrefEsp2Nombre());
		getPreferenceScreen().findPreference("defecto").setSummary(PreferenceManager.getDefaultSharedPreferences(this).getString("defecto", "--"));

		
	}
	
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        //mensaje();
        //Cuando entras en ajustes, se ejecuta este método
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        //Controlamos que todos los parámetros se han metido correctamente
        String valorTarifa=PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("txtTarifa", "8");
        String valorLimite=PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("txtLimite", "0.5");
        String valorDuracion=PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("txtDuracion", "0");

        try
        {
        	double tarifa=Double.parseDouble(valorTarifa);
        	double limite=Double.parseDouble(valorLimite);
        	int duracion=Integer.parseInt(valorDuracion);
        	mensaje();
        	
        }
        catch (Exception e)
        {
        	Toast.makeText(getBaseContext(),R.string.error_ajustes,Toast.LENGTH_LONG).show();
        }
    }
    
    private void mensaje(){
    	//Cuando sales de ajustes, se ejecuta este método
    	//Toast.makeText(getBaseContext(),R.string.actualizar,Toast.LENGTH_LONG).show();
    }
    
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Let's do something a preference value changes
    	final PreferenceScreen psRaiz = getPreferenceScreen();
    	
    	if (key.equals("txtTarifaLlamadas")||key.equals("txtTarifaSMS")||key.equals("txtTarifaEstLlamada")
    			||key.equals("listColores")||key.equals("txtTarifaEsp1Llamadas")||key.equals("txtTarifaEsp1SMS")
    			||key.equals("txtTarifaEsp1EstLlamada")||key.equals("cboxTarifaEsp1")||key.equals("listColoresEsp1")
    			||key.equals("txtTarifaEsp2Llamadas")||key.equals("txtTarifaEsp2SMS")||key.equals("txtTarifaEsp2EstLlamada")
    			||key.equals("cboxTarifaEsp2")||key.equals("listColoresEsp2")||key.equals("listMes")
    			||key.equals("txtDuracion")||key.equals("chbox_establecimiento")||key.equals("txtInicioMes"))
    	{
    		setResult(RESULT_OK);
    	}
    	else setResult(RESULT_CANCELED);
    			
    	
    	
        if (key.equals("txtTarifaEsp1Nombre")) {
    		ps = (PreferenceScreen) psRaiz.findPreference("psTarifaEsp1");
    		ps.setSummary(vp.getPrefEsp1Nombre());
        }
        if (key.equals("txtTarifaEsp2Nombre")) {

    		ps = (PreferenceScreen) psRaiz.findPreference("psTarifaEsp2");
    		ps.setSummary(vp.getPrefEsp2Nombre());
        }
        if (key.equals("defecto")) {
        	getPreferenceScreen().findPreference("defecto").setSummary(PreferenceManager.getDefaultSharedPreferences(this).getString("defecto", "--"));
    
        }
    }

}
