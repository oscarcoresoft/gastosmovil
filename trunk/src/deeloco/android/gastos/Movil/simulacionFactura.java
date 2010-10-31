package deeloco.android.gastos.Movil;

import android.app.Activity;
import android.os.Bundle;
public class simulacionFactura extends Activity {
   
	private tarifas ts;
	private String costeLLamadas;
	
	@Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.factura);
      
      ts = (tarifas) getIntent().getExtras().get("tarifas");
      costeLLamadas=(String) getIntent().getExtras().get("costeLLamadas");
      
   }
}
