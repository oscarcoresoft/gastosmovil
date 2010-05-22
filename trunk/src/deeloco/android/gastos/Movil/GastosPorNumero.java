package deeloco.android.gastos.Movil;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GastosPorNumero {
	
	private ArrayList <String> numeros=new ArrayList <String>();
	private ArrayList <Double> gastos=new ArrayList <Double>();
	private boolean listaOrdenada=false;
	
	public void add (String numero, double gasto){
		int posicion=numeros.indexOf(numero);
		if (posicion==-1) {//El número no existe
			numeros.add(numero);
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
		return numeros.size();
	}
	
	public String getNumero(int posicion){
		if (posicion>this.numeros.size()-1)
			return "";
		else
			return this.numeros.get(posicion);
	}
	
	public double getGasto(int posicion){
		if (posicion>this.gastos.size()-1)
			return -1;
		else
			return this.gastos.get(posicion);
	}
	
    public List<String> getNumeros()
    {
    	return numeros;
    }
    
    public List<Double> getGastos()
    {
    	return gastos;
    }
	
	public void clear(){
		numeros.clear();
		gastos.clear();
	}
	
	public void ordenaGastos(){
		ArrayList <String> GastosYNumeros=new ArrayList <String>();
		if (!this.listaOrdenada){
			//System.out.println("TOTAL DE NÚMEROS ***************** "+numeros.size());
	        for (int i=0;i<numeros.size();i++)
	        {
	        	//sNumeros=numeros.get(i)+","+sNumeros;
	        	GastosYNumeros.add(ponerCeros(FunGlobales.redondear(gastos.get(i),2))+";"+numeros.get(i));
	        }
	        
	        Collections.sort(GastosYNumeros);
	        numeros.clear();
	        gastos.clear();
	        
	        for (int i=0;i<GastosYNumeros.size();i++)
	        {
	        	//sNumeros=numeros.get(i)+","+sNumeros;
	        	//System.out.println("***************** "+GastosYNumeros.get(i));
	        	String data[]=GastosYNumeros.get(i).split(";");
	        	numeros.add(data[1]);
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
