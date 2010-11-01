package deeloco.android.gastos.Movil;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class simulacionFactura extends Activity {
   
	private tarifas ts;
	private Double costeLLamadas;
	private Double costeSMS;
	ValoresPreferencias vp=new ValoresPreferencias(this);
	
	@Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.factura);
      
      ts = (tarifas) getIntent().getExtras().get("tarifas");
      costeLLamadas=getIntent().getExtras().getDouble("costeLlamadas");
      costeSMS=getIntent().getExtras().getDouble("costeSMS");
      //Valores de las diferentes tarifas
      LinearLayout linear=(LinearLayout) findViewById(R.id.layout_factura);
      /*
      for (int i=0;i<ts.numTarifas();i++)
      {
    	  TextView txtNombre = new TextView(this,null,android.R.attr.textAppearanceSmall);
    	  txtNombre.setTextSize(12);
    	  //txtNombre.setTextColor(ColorStateList.valueOf(255));
    	  linear.addView(txtNombre, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,2));
    	  txtNombre.setText("Tarifa .."+ts.getTarifas().get(i).getNombre());
    	  TextView txtMinutos = new TextView(this,null,android.R.attr.textAppearanceSmall);
    	  txtMinutos.setTextSize(12);
    	  linear.addView(txtMinutos, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,2));
    	  txtMinutos.setText("    Tiempo hablado .."+FunGlobales.segundosAHoraMinutoSegundo(ts.getTarifas().get(i).getSegConsumidosMes()));
      }*/
      //Separador
	  ImageView img = new ImageView(this);
	  img.setImageDrawable(getResources().getDrawable(android.R.drawable.divider_horizontal_dim_dark));
	  img.setPadding(0, 5, 0, 5);
	  linear.addView(img,  new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

      //llamadas
      TextView txtCosteLlamadas = new TextView(this,null,android.R.attr.textAppearanceSmall);
      txtCosteLlamadas.setTextSize(12);
	  //txtNombre.setTextColor(ColorStateList.valueOf(255));
	  linear.addView(txtCosteLlamadas, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,2));      
	  txtCosteLlamadas.setText("Llamadas  "+FunGlobales.redondear(costeLLamadas,2)+FunGlobales.monedaLocal());
	  //SMS
      TextView txtCosteSMS = new TextView(this,null,android.R.attr.textAppearanceSmall);
      txtCosteSMS.setTextSize(12);
	  linear.addView(txtCosteSMS, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,2));      
	  txtCosteSMS.setText("Mensajes  "+FunGlobales.redondear(costeSMS,2)+FunGlobales.monedaLocal());
	  //Cuota Mensual
	  TextView txtCuotaMensual = new TextView(this,null,android.R.attr.textAppearanceSmall);
	  txtCuotaMensual.setTextSize(12);
	  linear.addView(txtCuotaMensual, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,2));      
	  txtCuotaMensual.setText("Cuota     "+FunGlobales.redondear(vp.getCuotaMensual(),2)+FunGlobales.monedaLocal());
	  //Tarifa plana
	  TextView txtTarifaPlana = new TextView(this,null,android.R.attr.textAppearanceSmall);
	  txtTarifaPlana.setTextSize(12);
	  linear.addView(txtTarifaPlana, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,2));      
	  txtTarifaPlana.setText("Datos     "+FunGlobales.redondear(vp.getTarifaPlana(),2)+FunGlobales.monedaLocal());
      
   }
}
