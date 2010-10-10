package deeloco.android.gastos.Movil;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.*;

import android.util.Log;


/**
 * Clase franja
 * @author antonio
 *
 */

public class tarifa implements Serializable{

	
	private static final String TAG = "clase tarifa";
	private static final int COSTE=0;
	private static final int ESTABLECIMIENTO=1;
	private static final int GASTOMINIMO=2;
	private static final int LIMITE=3;
	private static final int COSTE_FUERA_LIMITE=4;
	private static final int ESTABLECIMIENTO_FUERA_LIMITE=5;
	
	/**
	 * Atributos relacionados con el total de segundos consumidos para una tarifa
	 */
	private int segConsumidosMes=0;
	private int segConsumidosDia=0;
	private int segConsumidosLimiteMes=0; //Solo debe contar las franjas que contabilizan para el limite
	
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
	 * Limite, en minutos, para el que se aplica uno y otra tarifa de una franja
	 */
	private int limite;
	
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
	 * Retorna el total de segundos consumidos de lo que vá de mes
	 * @return
	 */
	public int getSegConsumidosMes(){
		return this.segConsumidosMes;
	}
	
	
	/**
	 * Retorna el total de segundos consumidos en un día
	 * @return
	 */
	public int getSegConsumidosDia(){
		return this.segConsumidosDia;
	}
	
	
	/**
	 * Retorna el total de segundos consumidos del limite mensual
	 * @return
	 */
	public int getSegConsumidosLimiteMes(){
		return this.segConsumidosLimiteMes;
	}
	
	/**
	 * Devuelve el nombre de la tarifa
	 */
	public String getNombre(){
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
	 * Devuelve el limite de la franja
	 * @return
	 */
	public int getLimite(){
		return this.limite;
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


	public void resetSegundos(){
		this.segConsumidosMes=0;
		this.segConsumidosDia=0;
		this.segConsumidosLimiteMes=0;
	}
	
	/**
	 * Suma segundos al total de segundos consumidos en un mes
	 * @param segundos
	 */
	public void addSegConsumidosMes(int segundos){
		this.segConsumidosMes=this.segConsumidosMes+segundos;
	}
	
	/**
	 * Suma segundos al total de segundos consumidos en un dia
	 * @param segundos
	 */
	public void addSegConsumidosDia(int segundos){
		this.segConsumidosDia=this.segConsumidosDia+segundos;
	}
	
	
	public void addSegConsumidosLimiteMes(int segundos){
		this.segConsumidosLimiteMes+=segundos;
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
	
	void setMinimo(String minimo){
		
		try
		{
			minimo=minimo.replace(",",".");
			this.minimo=Double.parseDouble(minimo);
		}
		catch (Exception e)
		{
			this.minimo=0.0;
		}

	}
	
	/**
	 * Asigna el valor limite a limite de llamadas
	 * @param limite
	 */
	public void setLimite (int limite){
		this.limite=limite;
	}
	
	public void setLimite(String limite){
		
		try
		{
			limite=limite.replace(",",".");
			this.limite=(int) Math.floor(Double.parseDouble(limite));
			//this.limite=Integer.parseInt(limite);
		}
		catch (Exception e)
		{
			this.limite=0;
		}
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
		
		if (this.numeros.indexOf(numero)>-1)
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
		if (numeros2.length()>0)
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
		factual.setEstablecimientoFueraLimite(f.getEstablecimientoFueraLimite());
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
		
		if (this.numeros.indexOf(numero)>-1)
			return true;
		
		
		for (int i=0;i<this.numeros.size();i++)
		{
			Log.d(TAG,"Patrón="+this.numeros.get(i));
			Pattern p=Pattern.compile('^'+this.numeros.get(i));
			Matcher m=p.matcher(numero);
			if (m.find())
			{
				//El patron se cumple
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Calcula el coste de una llamada, para un número, día, hora y duración
	 * @param numero
	 * @param dia
	 * @param hora
	 * @param duracion
	 * @return
	 */
	public double[] coste(String numero,int dia,Time hora,int duracion){
		
		double[] retorno={0.0,0.0,0.0,0.0,0.0,0.0};
		//Calculamos el coste
		for (int i=0;i<this.franjas.size();i++)
		{
			//Comprueba si el día y la hora pertenece a una franja
			if (this.franjas.get(i).pertenece(dia, hora))
			{
				//Si el día y la hora pertenece a una franja, calcula el coste para esa franja
				retorno= this.franjas.get(i).coste(dia, hora, duracion);
				//Si la franja cuenta para el límite, se retorna el valor del límite, sino 0
				if (this.franjas.get(i).getLimite())
					retorno[LIMITE]=this.limite;
				else
					retorno[LIMITE]=0.0;
			}
		}
		retorno[GASTOMINIMO]=this.getMinimo();
		//El retorno del limite, sera para aquellas franjas que se contabilicen para el limite
		return retorno;
	}
	
	public double[] coste(String numero,int dia,String hora,int duracion){
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
	
	/**
	 * Retorna si las franjas que pertenecen a la tarifa son compatibles en los horarios
	 * @return
	 */
	public boolean compatibilidadHorarioFranjas(){
		int[][] mControl=new int[7][24];
		boolean conflicto=true;
		//Iniciando mControl
		for (int f=0;f<7;f++)
			for (int c=0;c<24;c++)
				mControl[f][c]=0;
		
		
		//recorremos todas las franjas
        for (int i=0;i<this.franjas.size();i++)
        {
        	int fila=0;
        	List<String> diasSemana=new ArrayList <String>();
        	diasSemana=this.franjas.get(i).getDias();
        	int hInicio=Integer.parseInt((""+this.franjas.get(i).getHoraInicio()).split(":")[0]);
        	int hFinal=Integer.parseInt((""+this.franjas.get(i).getHoraFinal()).split(":")[0]);
        	Log.d(TAG,"hora inicio:"+hInicio+" - hora final:"+hFinal);
        	Log.d(TAG,"Dia de la semana "+diasSemana+"="+diasSemana.indexOf("Lun"));
        	for (int d=0;d<diasSemana.size();d++)
        	{
        		if (diasSemana.get(d).equals("Lun")) fila=0;
        		if (diasSemana.get(d).equals("Mar")) fila=1;
        		if (diasSemana.get(d).equals("Mie")) fila=2;
        		if (diasSemana.get(d).equals("Jue")) fila=3;
        		if (diasSemana.get(d).equals("Vie")) fila=4;
        		if (diasSemana.get(d).equals("Sab")) fila=5;
        		if (diasSemana.get(d).equals("Dom")) fila=6;
        		
        		//El lunes esta entro los días
        		
        		if (hInicio<hFinal)
        		{
        			//la franja está en el mismo día
        			for (int a=hInicio;a<hFinal;a++)
        			{
        				mControl[fila][a]++;
        			}
        		}
        		else
        		{
        			//la franja está en dos días
        			for (int a=hInicio;a<24;a++)
        			{
        				mControl[fila][a]++;
        			}
        			
        			for (int a=0;a<hFinal;a++)
        			{
        				mControl[fila][a]++;
        			}
        			
        		}
        	}

        } 
        
        
		for (int f=0;f<7;f++)
			for (int c=0;c<24;c++)
			{
				if (mControl[f][c]!=1) conflicto=false;
				//Log.d(TAG,"("+f+","+c+")="+mControl[f][c]);
			}
		
		
		return conflicto;
	}
	
	
	public String getNombreFranja(int dia,Time hora){
		
		
		for (int i=0;i<this.franjas.size();i++)
		{
			//Comprueba si el día y la hora pertenece a una franja
			if (this.franjas.get(i).pertenece(dia, hora))
			{
				return this.franjas.get(i).getNombre();
			}
		}
		
		return "";
	}

}
