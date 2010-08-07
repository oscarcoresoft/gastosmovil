package deeloco.android.gastos.Movil;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;

import android.util.Log;


/**
 * Clase franja
 * @author antonio
 *
 */

public class tarifa implements Serializable{

	
	private static final String TAG = "clase tarifa";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * identificador de la tarifa
	 */
	private int identificador;
	
	/**
	 * nombre de la tarifa
	 */
	private String nombre;
	
	/**
	 * Consumo mínimo, en euros y sin iva. Si es 0 no tiene consumo mínimo.
	 */
	private double minimo;
	
	/**
	 * Color representativo de la tarifa
	 */
	private String color;
	
	/**
	 * Si la tarifa se aplica por defecto, en cado que un número no pertenezca a una tarifa
	 */
	private boolean defecto;
	
	/**
	 * Franjas pertenecientes a la tarifa
	 */
	private ArrayList <Franja> franjas = new ArrayList <Franja>();
	
	/**
	 * numeros a los que se le asigna la tarifa
	 */
	private ArrayList <String> numeros = new ArrayList <String>();
	
	/**
	 * Constructor de la clase
	 * @param id
	 * @param nombre
	 * @param franjas
	 * @param numeros
	 */
	tarifa(int id,String nombre,ArrayList<Franja> franjas, ArrayList<String> numeros){
		this.identificador=id;
		this.nombre=nombre;
		this.franjas=franjas;
		this.numeros=numeros;
	}
	
	tarifa(int id){
		this.identificador=id;
		this.nombre="Tarifa Sin Nombre";
	}
	
	tarifa (String id){
		this.identificador=Integer.parseInt(id);
		this.nombre="Tarifa Sin Nombre";
	}
	
	
	
	
	//get y set de los atributos de la clase
	
	
	
	
	
	/**
	 * Devuelve el nombre de la tarifa
	 */
	String getNombre(){
		return this.nombre;
	}
	
	/**
	 * Devuelve el identificador de la tarifa
	 * @return
	 */
	int getIdentificador(){
		return this.identificador;
	}
	
	/**
	 * Devuelve el consumo mínimo que tiene la tarifa
	 * @return
	 */
	double getMinimo(){
		return this.minimo;
	}
	
	/**
	 * Devuelve el color representativo de la tarifa
	 * @return
	 */
	String getColor(){
		return this.color;
	}

	/**
	 * Devuelve una cadena, separada por comas, con todos los numeros que pertenece a la tarifa
	 * @return
	 */
	String getNumeros(){
		
		Log.d(TAG,"getNumeros ->"+this.numeros.size());
		if (this.numeros.size()>0)
		{
			String sNumeros="";
	    	//escribimos en el fichero
	        for (int i=0;i<this.numeros.size();i++)
	        {
	        	sNumeros=numeros.get(i)+","+sNumeros;
	        }
	        return sNumeros;
		}
		else
			return "";
	}
	
	/**
	 * Devuelve si una tarifa se asigna por defecto
	 * @return
	 */
	public boolean isDefecto() {
		return defecto;
	}

	/**
	 * Asigna el valor del parámetro al atributo defecto.
	 * @param defecto
	 */
	public void setDefecto(boolean defecto) {
		this.defecto = defecto;
	}
	
	
	/**
	 * Devuelve el numero de franjas que tiene la tarifa
	 * @return
	 */
	int getNumFranjas(){
		return this.franjas.size();
	}
	
	/**
	 * Devuelve un ArrayList con todas las franjas
	 * @return
	 */
	ArrayList <Franja> getFranjas(){
		return this.franjas;
	}
	
	
	
	
	/**
	 * Asigna el idetificador de la tarifa
	 * @param id
	 */
	void setIdentificador(int id){
		this.identificador=id;
	}
	
	/**
	 * Asigna el valor del atributo nombre
	 * @param nombre
	 */
	void setNombre(String nombre){
		this.nombre=nombre;
	}
	
	/**
	 * Asigna el valor del gasto mínimo 
	 * @param minimo
	 */
	void setMinimo(double minimo){
		this.minimo=minimo;
	}
	
	/**
	 * Asigna el color de la tarifa
	 * @param color
	 */
	void setColor(String color){
		this.color=color;
	}
	
	/**
	 * Asigna la cadena numeros al atributo numeros
	 * @param numeros
	 */
	void setNumeros(String numeros){

		String numeros2=numeros.trim();
		if (numeros2.length()>5)
        {
        	String data[] = numeros2.split(",");
        	for (int i=0; i < data.length; i++) {
        		 this.numeros.add(data[i]);
        		}
        }  	
	}
	
	/**
	 * Asigna las franjas f a las franjas de la tarifa
	 * @param f
	 */
	void setFranjas (ArrayList <Franja> f)
	{
		this.franjas=f;
	}
	
	
	
	// resto de métodos
	
	
	void addFranja(Franja f){
		this.franjas.add(f);
	}

	/**
	 * Devuelve si un numero pertenece a la tarifa
	 * @param numero
	 * @return
	 */
	boolean pertenece(String numero){
		
		if (this.numeros.indexOf(numero)==-1)
			return false;
		else
			return true;
	}
	
	
	double coste(String numero,int dia,Time hora,int duracion){
		
		System.out.println("Cantida de Numeros de esta tarifa = "+this.numeros.size());
		if (pertenece(numero)||this.numeros.size()==0)
		{
			//Calculamos el coste
			System.out.println("Numero de franjas = "+this.franjas.size());
			for (int i=0;i<this.franjas.size();i++)
			{
				System.out.println("Calculando tarifa para la franja "+this.franjas.get(i).getNombre());
				if (this.franjas.get(i).pertenece(dia, hora))
				{
					return this.franjas.get(i).coste(dia, hora, duracion);
				}
			}
		}
		return 0.0;
	}
	
	/**
	 * Devuelve el numero de franjas que forman una tarifa
	 * @return
	 */
	int numFranjas(){
		return this.franjas.size();
	}

	
	
	
	

}
