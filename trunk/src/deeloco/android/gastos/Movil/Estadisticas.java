package deeloco.android.gastos.Movil;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
//import android.net.Uri;
import android.os.Bundle;
//import android.provider.CallLog;
import android.provider.Contacts.People;
//import android.widget.Toast;
import android.widget.ListView;
import android.widget.TabHost;
//import android.widget.ArrayAdapter;

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

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("artists").setIndicator("Artists",res.getDrawable(R.drawable.icon)).setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, GastosPorHoraActivity.class);
	    spec = tabHost.newTabSpec("albums").setIndicator("Albums",res.getDrawable(R.drawable.icon)).setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(1);
	}
	
	

}

