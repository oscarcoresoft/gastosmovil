package deeloco.android.gastos.Movil.plus;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.Log;

/**
 * GastosPorNumero: Acumula el gasto de teléfono por número. Funcionalidad primera
 * En la actualidad su funcionalidad a aumentado y almacena toda la información acumulada relacionada con un número de telefono
 * Datos acumulados:
 * - Gastos por numero
 * - Segundos hablados por numero
     */


public class GastosPorNumero {
	
	private ArrayList <String> numeros=new ArrayList <String>();
	private ArrayList <Double> gastos=new ArrayList <Double>();
	private ArrayList <Integer> sumaDuracion=new ArrayList <Integer>();
	//private boolean listaOrdenada=false;
	
	public void add (String numero, double gasto,int duracion){
		int posicion=numeros.indexOf(numero);
		if (posicion==-1) {//El número no existe
			numeros.add(numero);
			gastos.add(gasto);
			sumaDuracion.add(duracion);
		}
		else { //El número existe, se le suma gastos
			double suma;
			int sumaD;
			suma=gastos.get(posicion)+gasto;
			sumaD=sumaDuracion.get(posicion)+duracion;
			gastos.set(posicion, suma);
			sumaDuracion.set(posicion,sumaD);
			
		}
		//this.listaOrdenada=false; //se puede mejorar. Solo cuando cambie el orden y  no siempre como esta ahora
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
	
	public double getDuracion(int posicion){
		if (posicion>this.gastos.size()-1)
			return -1;
		else
			return this.sumaDuracion.get(posicion);
	}
	
	/**
	 * Retorna la lista de numeros que han tenido gastos
	 * @return
	 */
    public List<String> getNumeros()
    {
    	return numeros;
    }
    
    /**
     * Retorna la lista de gastos por numero
     * @return
     */
    public List<Double> getGastos()
    {
    	return gastos;
    }
    
    /**
     * Retorna la lista de suma de tiempo hablado por numero
     * @return
     * List <Integer>-> Lista de segundos hablados por numero 
     */
    public List<Integer> getDuracion()
    {
    	return sumaDuracion;
    }
	
    /**
     * Limpia todas las listas
     */
	public void clear(){
		numeros.clear();
		gastos.clear();
		sumaDuracion.clear();
	}
	
	
	public void ordenaGastos(){
		ArrayList <String> GastosYNumeros=new ArrayList <String>();
		//if (!this.listaOrdenada){
			//System.out.println("TOTAL DE NÚMEROS ***************** "+numeros.size());
	        for (int i=0;i<numeros.size();i++)
	        {
	        	//sNumeros=numeros.get(i)+","+sNumeros;
	        	GastosYNumeros.add(ponerCeros(FunGlobales.redondear(gastos.get(i),2))+";"+numeros.get(i)+";"+sumaDuracion.get(i));
	        	Log.d("OrdenarGasto",GastosYNumeros.get(i));
	        }
	        
	        Collections.sort(GastosYNumeros);
	        numeros.clear();
	        gastos.clear();
	        sumaDuracion.clear();
	        
	        for (int i=0;i<GastosYNumeros.size();i++)
	        {
	        	//sNumeros=numeros.get(i)+","+sNumeros;
	        	//System.out.println("***************** "+GastosYNumeros.get(i));
	        	String data[]=GastosYNumeros.get(i).split(";");
	        	
	        	gastos.add(Double.parseDouble(data[0]));
	        	numeros.add(data[1]);
	        	sumaDuracion.add(Integer.parseInt(data[2]));
	        	
	        	
	        }
	        //this.listaOrdenada=true;
		//}
	}
	
	
	public void ordenaDuracion(){
		ArrayList <String> DuracionYNumeros=new ArrayList <String>();
		//if (!this.listaOrdenada){
			//System.out.println("TOTAL DE NÚMEROS ***************** "+numeros.size());
	        for (int i=0;i<numeros.size();i++)
	        {
	        	//sNumeros=numeros.get(i)+","+sNumeros;
	        	DuracionYNumeros.add(ponerCeros(sumaDuracion.get(i))+";"+numeros.get(i)+";"+gastos.get(i));
	        	Log.d("OrdenarDuración",DuracionYNumeros.get(i));
	        }
	        
	        Collections.sort(DuracionYNumeros);
	        numeros.clear();
	        sumaDuracion.clear();
	        gastos.clear();
	        
	        for (int i=0;i<DuracionYNumeros.size();i++)
	        {
	        	//sNumeros=numeros.get(i)+","+sNumeros;
	        	//System.out.println("***************** "+GastosYNumeros.get(i));
	        	String data[]=DuracionYNumeros.get(i).split(";");
	        	
	        	sumaDuracion.add(Integer.parseInt(data[0]));
	        	numeros.add(data[1]);
	        	gastos.add(Double.parseDouble(data[2]));
	        }
	     //   this.listaOrdenada=true;
		//}
	}
    
    private String ponerCeros(double n){
    	DecimalFormatSymbols elpunto=new DecimalFormatSymbols();
    	elpunto.setDecimalSeparator('.');
    	
    	DecimalFormat cuatroydos=new DecimalFormat("0000.00",elpunto);
    	//System.out.println(elpunto.getDecimalSeparator()+"::***************** "+cuatroydos.format(n));
    	return cuatroydos.format(n);
    }
    
    private String ponerCeros(int n){
    	String num=n+"";
    	String ceros="";
    	for (int i=1;i<(6-num.length());i++) ceros=ceros+"0";

    	return ceros+num;
    }
}
