package deeloco.android.gastos.Movil;


import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class Estadisticas extends TabActivity {
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.pestanas);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, GastosPorNumeroActivity.class);
	    Bundle extras=getIntent().getExtras();
	    intent.putExtra("total",extras.getString("total"));
	    intent.putExtra("Numeros", extras.getStringArrayList("Numeros"));
	    intent.putExtra("Gastos",extras.getStringArrayList("Gastos"));

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("numeros").setIndicator(""+R.string.gpn_resumen,res.getDrawable(android.R.drawable.ic_menu_call)).setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, GastosPorHoraActivity.class);
	    intent.putExtra("total",extras.getString("total"));
	    intent.putExtra("Horas", extras.getStringArrayList("Horas"));
	    intent.putExtra("Gastos",extras.getStringArrayList("Gastos2"));	    
	    
	    spec = tabHost.newTabSpec("horas").setIndicator(""+R.string.gph_resumen,res.getDrawable(android.R.drawable.ic_menu_recent_history)).setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
	}
	
	

}

