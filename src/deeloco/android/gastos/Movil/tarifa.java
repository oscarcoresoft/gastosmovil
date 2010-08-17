package deeloco.android.gastos.Movil;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

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
	 * 
	 * @param nombre
	 * @return
	 */
	public int getIdentificador(String nombre){
		
        for (int i=0;i<this.franjas.size();i++)
        {
        	if (nombre.equals(this.franjas.get(i).getNombre()))
        	{
        		return this.franjas.get(i).getIdentificador();
        	}
        }
		return -1; //El nombre no pertenece a ninguna franja de esta tarifa
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
	 * Devuelve la franja a la que pertence un día y una hora
	 * @param dia
	 * @param hora
	 * @return
	 */
	Franja getFranja(int dia,String hora){
		
		for (int i=0;i<this.franjas.size();i++)
		{
			Log.d(TAG,"getFranja -> "+this.franjas.get(i).pertenece(dia, Time.valueOf(hora)));
			if (this.franjas.get(i).pertenece(dia, Time.valueOf(hora)))
			{
				return this.franjas.get(i);
			}
		}
		Log.d(TAG,"Día = "+dia+" Hora = "+hora+" NO TIENE FRANJA EN LA TARIFA "+this.getNombre());
		return null;
	}
	
	Franja getFranja (String fechayhora){
		Date d=new Date(fechayhora);
		int dia=d.getDay();
		String hora=d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
		return getFranja(dia,hora);
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
	void addNumero(String numero){
		if (numero.length()>5)
		 this.numeros.add(numero.trim());
	}
	
	
	public void deleteNumero(String numero){
		
		if (pertenece(numero))
		{
			this.numeros.remove(this.numeros.indexOf(numero));
		}
			
	}
	
	/**
	 * Asigna la cadena numeros al atributo numeros
	 * @param numeros
	 */
	void setNumeros(String numeros){
		
		String numeros2=numeros.trim();
		if (numeros2.length()>5)
        {
			this.numeros.clear();
        	String data[] = numeros2.split(",");
        	for (int i=0; i < data.length; i++) {
        		 this.numeros.add(data[i].trim());
        		}
        }
		else
			this.numeros.clear();
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
	
	/**
	 * Devuelve el ultimo id que se puede asignar
	 * @return
	 */
	private int ultimoId(){
		int ultimo=0;
		for (int i=0;i<this.franjas.size();i++)
		{
			if (ultimo<this.franjas.get(i).getIdentificador())
			{
				ultimo=this.franjas.get(i).getIdentificador();
			}
		}
		return ++ultimo;
	}
	
	
	/**
	 * Añade una nueva franja al arraylist de franjas
	 */
	void addFranja(Franja f){
		if (f.getIdentificador()==0)
		{
			f.setIdentificador(ultimoId());
		}
		this.franjas.add(f);
	}
	
	void modificarFranja(int id,Franja f){
		Log.i(TAG,"Modificar Franja");
		Franja factual=this.getFranja(id);
		Log.i(TAG,"Nombre de la franja que se va ha modificar -> "+factual.getNombre());
		Log.i(TAG,"Nombre de la franja que se va a añadir -> "+f.getNombre());
		factual.setNombre(f.getNombre());
		factual.setHoraInicio(f.getHoraInicio());
		factual.setHoraFinal(f.getHoraFinal());
		factual.setDias((ArrayList <String>) f.getDias());
		factual.setCoste(f.getCoste());
		factual.setEstablecimiento(f.getEstablecimiento());
		factual.setLimite(f.getLimite());
		factual.setCosteFueraLimite(f.getCosteFueraLimite());
	}

	/**
	 * Elimina la franja con nombre
	 * @param nombre
	 */
	public void deleteFranja(String nombre)
	{
		Log.d(TAG,"Intentando eliminar la franja= "+nombre);
        for (int i=0;i<this.franjas.size();i++)
        {
        	if (nombre.equals(this.franjas.get(i).getNombre()))
        	{
        		Log.d(TAG,"Eliminando la franja= "+nombre);
        		this.franjas.remove(i);
        	}
        }
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
	
	/**
	 * Calcula el coste de una llamada, para un número, día, hora y duración
	 * @param numero
	 * @param dia
	 * @param hora
	 * @param duracion
	 * @return
	 */
	public double coste(String numero,int dia,Time hora,int duracion){
		
		Log.d(TAG,"Cantida de Numeros de esta tarifa = "+this.numeros.size());
		//Calculamos el coste
		Log.d(TAG,"Numero de franjas = "+this.franjas.size());
		for (int i=0;i<this.franjas.size();i++)
		{
			Log.d(TAG,"Calculando tarifa para la franja "+this.franjas.get(i).getNombre());
			if (this.franjas.get(i).pertenece(dia, hora))
			{
				return this.franjas.get(i).coste(dia, hora, duracion);
			}
		}
		return 0.0;
	}
	
	public double coste(String numero,int dia,String hora,int duracion){
		return coste(numero,dia,Time.valueOf(hora),duracion);
		
	}
	
	/**
	 * Devuelve el numero de franjas que forman una tarifa
	 * @return
	 */
	int numFranjas(){
		return this.franjas.size();
	}

	/**
	 * Retorna una Franja cuyo identificador es igual a id. Si no existe retorna null
	 * @param id
	 * @return
	 */
	public Franja getFranja(int id){
		
        for (int i=0;i<this.franjas.size();i++)
        {
        	if (this.franjas.get(i).getIdentificador()==id)
        	{
        		return this.franjas.get(i);
        	}
        }
		
        return null;
	}

}
