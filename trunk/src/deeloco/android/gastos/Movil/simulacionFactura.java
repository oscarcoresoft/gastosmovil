package deeloco.android.gastos.Movil;

import android.R.integer;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class simulacionFactura extends Activity {
   
	private tarifas ts;
	private Double costeLLamadas;
	private Double costeSMS;
	private Double cuota;
	private Double tarifaPlana;
	private Double total;
	private Double descuento;
	private Double iva;
	private Double totalPagar;
	ValoresPreferencias vp=new ValoresPreferencias(this);
	
	@Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.factura);
      
      ts = (tarifas) getIntent().getExtras().get("tarifas");
      costeLLamadas=getIntent().getExtras().getDouble("costeLlamadas");
      costeSMS=getIntent().getExtras().getDouble("costeSMS");
      cuota=vp.getCuotaMensual();
      tarifaPlana=vp.getTarifaPlana();
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
      
    //MES
	  TextView txtMes = new TextView(this,null,android.R.attr.textAppearanceSmall);
	  txtMes.setTextSize(15);
	  txtMes.setTypeface(Typeface.MONOSPACE);
	  linear.addView(txtMes, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,2));      
	  txtMes.setText("Simulación mes de "+FunGlobales.periodo(getResources().getStringArray(R.array.listaMeses), vp.getPreferenciasMes()+1, vp.getPreferenciasInicioMes()));
      
      //Separador
	  ImageView separador = new ImageView(this);
	  separador.setImageDrawable(getResources().getDrawable(android.R.drawable.divider_horizontal_dim_dark));
	  separador.setPadding(0, 5, 0, 5);
	  linear.addView(separador,  new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	  
      //llamadas
	  //Hay que quitarle el iva
      TextView txtCosteLlamadas = new TextView(this,null,android.R.attr.textAppearanceSmall);
      txtCosteLlamadas.setTextSize(15);
      txtCosteLlamadas.setTypeface(Typeface.MONOSPACE);
	  //txtNombre.setTextColor(ColorStateList.valueOf(255));
	  linear.addView(txtCosteLlamadas, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,2));      
	  txtCosteLlamadas.setText("Llamadas            "+FunGlobales.redondear(costeLLamadas,2)+FunGlobales.monedaLocal());
	  //SMS
      TextView txtCosteSMS = new TextView(this,null,android.R.attr.textAppearanceSmall);
      txtCosteSMS.setTextSize(15);
      txtCosteSMS.setTypeface(Typeface.MONOSPACE);
	  linear.addView(txtCosteSMS, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,2));      
	  txtCosteSMS.setText("Mensajes            "+FunGlobales.redondear(costeSMS,2)+FunGlobales.monedaLocal());
	  //Cuota Mensual
	  TextView txtCuotaMensual = new TextView(this,null,android.R.attr.textAppearanceSmall);
	  txtCuotaMensual.setTextSize(15);
	  txtCuotaMensual.setTypeface(Typeface.MONOSPACE);
	  linear.addView(txtCuotaMensual, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,2));      
	  txtCuotaMensual.setText("Cuota               "+FunGlobales.redondear(cuota,2)+FunGlobales.monedaLocal());
	  //Tarifa plana
	  TextView txtTarifaPlana = new TextView(this,null,android.R.attr.textAppearanceSmall);
	  txtTarifaPlana.setTextSize(15);
	  txtTarifaPlana.setTypeface(Typeface.MONOSPACE);
	  linear.addView(txtTarifaPlana, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,2));      
	  txtTarifaPlana.setText("Datos               "+FunGlobales.redondear(tarifaPlana,2)+FunGlobales.monedaLocal());
	  //Total
	  total=costeLLamadas+costeSMS+cuota+tarifaPlana;
	  TextView txtTotal = new TextView(this,null,android.R.attr.textAppearanceSmall);
	  txtTotal.setTextSize(15);
	  txtTotal.setTypeface(Typeface.MONOSPACE);
	  linear.addView(txtTotal, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,2));      
	  txtTotal.setText("Total imponible     "+FunGlobales.redondear(total,2)+FunGlobales.monedaLocal());
	//Separador
	  ImageView separador2 = new ImageView(this);
	  separador2.setImageDrawable(getResources().getDrawable(android.R.drawable.divider_horizontal_dim_dark));
	  separador2.setPadding(0, 5, 0, 5);
	  linear.addView(separador2,  new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

	  //Descuento
	  descuento=total*(Double.parseDouble(""+vp.getPreferenciasDescuento())/100);
  
	  TextView txtDescuento = new TextView(this,null,android.R.attr.textAppearanceSmall);
	  txtDescuento.setTextSize(15);
	  txtDescuento.setTypeface(Typeface.MONOSPACE);
	  linear.addView(txtDescuento, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,2));      
	  txtDescuento.setText("Descuento ("+vp.getPreferenciasDescuento()+"%)     "+FunGlobales.redondear(descuento,2)+FunGlobales.monedaLocal());
	  //Total - Descuento
	  total=total-descuento;
	  TextView txtTotalDescuento = new TextView(this,null,android.R.attr.textAppearanceSmall);
	  txtTotalDescuento.setTextSize(15);
	  txtTotalDescuento.setTypeface(Typeface.MONOSPACE);
	  linear.addView(txtTotalDescuento, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,2));      
	  txtTotalDescuento.setText("Total               "+FunGlobales.redondear(total,2)+FunGlobales.monedaLocal());
	  //Iva
	  iva=total*(vp.getPreferenciasImpuestos()-1);
	  TextView txtIVA = new TextView(this,null,android.R.attr.textAppearanceSmall);
	  txtIVA.setTextSize(15);
	  txtIVA.setTypeface(Typeface.MONOSPACE);
	  linear.addView(txtIVA, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,2));      
	  txtIVA.setText("Impuestos ("+vp.getPreferenciasImpuestosPorCiento()+"%)     "+FunGlobales.redondear(iva,2)+FunGlobales.monedaLocal());
	//Separador
	  ImageView separador3 = new ImageView(this);
	  separador3.setImageDrawable(getResources().getDrawable(android.R.drawable.divider_horizontal_dim_dark));
	  separador3.setPadding(0, 5, 0, 5);
	  linear.addView(separador3,  new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	  //Total a pagar
	  totalPagar=total+iva;
	  TextView txtTotalaPagar = new TextView(this,null,android.R.attr.textAppearanceSmall);
	  txtTotalaPagar.setTextSize(15);
	  txtTotalaPagar.setTypeface(Typeface.MONOSPACE);
	  linear.addView(txtTotalaPagar, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,2));      
	  txtTotalaPagar.setText("Total               "+FunGlobales.redondear(totalPagar,2)+FunGlobales.monedaLocal());
	  

      
   }
}
