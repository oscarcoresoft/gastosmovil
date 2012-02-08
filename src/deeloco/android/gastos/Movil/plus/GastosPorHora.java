package deeloco.android.gastos.Movil.plus;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * GastosPorNumero: Acumula el gasto de teléfono por número.
     */


public class GastosPorHora {
	
	private ArrayList <String> horas=new ArrayList <String>();
	private ArrayList <Double> gastos=new ArrayList <Double>();
	private boolean listaOrdenada=false;
	
	public void add (Date fechayhora, double gasto){
		String hora=""+fechayhora.getHours();
		//Comprobamos que la hora esta creada
		int posicion=horas.indexOf(hora);
		if (posicion==-1) {//La hora no existe
			horas.add(hora);
			gastos.add(gasto);
		}
		else { //El número existe, se le suma gastos
			double suma;
			suma=gastos.get(posicion)+gasto;
			gastos.set(posicion, suma);
		}
		this.listaOrdenada=false; //se puede mejorar. Solo cuando cambie el orden y  no siempre como esta ahora
	}

	public int longitud(){
		return horas.size();
	}
	
	public String getHora(int posicion){
		if (posicion>this.horas.size()-1)
			return "";
		else
			return this.horas.get(posicion);
	}
	
	public double getGasto(int posicion){
		if (posicion>this.gastos.size()-1)
			return -1;
		else
			return this.gastos.get(posicion);
	}
	
    public List<String> gethoras()
    {
    	return horas;
    }
    
    public List<Double> getGastos()
    {
    	return gastos;
    }
	
	public void clear(){
		horas.clear();
		gastos.clear();
	}
	
	
	public void ordenaGastos(){
		ArrayList <String> GastosYhoras=new ArrayList <String>();
		if (!this.listaOrdenada){
			//System.out.println("TOTAL DE NÚMEROS ***************** "+horas.size());
	        for (int i=0;i<horas.size();i++)
	        {
	        	//shoras=horas.get(i)+","+shoras;
	        	GastosYhoras.add(ponerCeros(FunGlobales.redondear(gastos.get(i),2))+";"+horas.get(i));
	        }
	        
	        Collections.sort(GastosYhoras);
	        horas.clear();
	        gastos.clear();
	        
	        for (int i=0;i<GastosYhoras.size();i++)
	        {
	        	//shoras=horas.get(i)+","+shoras;
	        	//System.out.println("***************** "+GastosYhoras.get(i));
	        	String data[]=GastosYhoras.get(i).split(";");
	        	horas.add(data[1]);
	        	gastos.add(Double.parseDouble(data[0]));
	        }
	        this.listaOrdenada=true;
		}
	}
    
    private String ponerCeros(double n){
    	DecimalFormatSymbols elpunto=new DecimalFormatSymbols();
    	elpunto.setDecimalSeparator('.');
    	
    	DecimalFormat cuatroydos=new DecimalFormat("0000.00",elpunto);
    	//System.out.println(elpunto.getDecimalSeparator()+"::***************** "+cuatroydos.format(n));
    	return cuatroydos.format(n);
    }
}
