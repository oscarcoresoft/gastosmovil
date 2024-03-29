package deeloco.android.gastos.Movil.plus;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.*;

import deeloco.android.gastos.Movil.plus.R;

import android.util.Log;


/**
 * Clase franja
 * @author antonio
 *
 */

public class tarifa implements Serializable{

	private static final int LIMITE=3;
	
	/**
	 * Atributos relacionados con el total de segundos consumidos para una tarifa
	 */
	private int segConsumidosMes=0;
	private int segConsumidosDia=0;
	private int segConsumidosLimiteMes=0; //Solo debe contar las franjas que contabilizan para el limite
	private int segConsumidosLimiteDia=0;
	
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
	 * Limite mensual, en minutos, para el que se aplica uno y otra tarifa de una franja
	 */
	private int limite;
	
	/**
	 * Limite diario, en minutos, para el que se aplica uno y otra tarifa de una franja
	 */
	private int limiteDia;
	
	/**
	 * Limite de la llamada, en minutos, para el que se aplica uno y otra tarifa de una franja
	 */
	private int limiteLlamada;
	
	/**
	 * Color representativo de la tarifa
	 */
	private String color;
	
	/**
	 * Si la tarifa se aplica por defecto, en caso que un número no pertenezca a una tarifa
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
	
	public int getSegConsumidosLimiteDia() {
		return segConsumidosLimiteDia;
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
	 * Devuelve el limite mensual de la franja
	 * @return
	 */
	public int getLimite(){
		return this.limite;
	}
	
	
	/**
	 * Devuelve el limite diario de la franja
	 * @return
	 */
	public int getLimiteDia(){
		return this.limiteDia;
	}
	
	
	/**
	 * Devuelve el limite de la llamada de la franja
	 * @return
	 */
	public int getLimiteLlamada(){
		return this.limiteLlamada;
	}
	
	
	/**
	 * Devuelve el color representativo de la tarifa
	 * @return
	 */
	String getColor(){
		return this.color;
	}

	public int getColorDrawble(){
		int retorno=0;
		if (this.color.equals("Blanco"))  retorno=R.drawable.line7;
	  	if (this.color.equals("Amarillo"))  retorno=R.drawable.line1;
	  	if (this.color.equals("Azul"))  retorno=R.drawable.line2;
	  	if (this.color.equals("Naranja"))  retorno=R.drawable.line3;
	  	if (this.color.equals("Rojo"))  retorno=R.drawable.line4;
	  	if (this.color.equals("Verde"))  retorno=R.drawable.line5;
	  	if (this.color.equals("Violeta"))  retorno=R.drawable.line6;
	  	if (this.color.equals("Transparente"))  retorno=R.drawable.line0;
	  	return retorno;
	}
	
	
	/**
	 * Devuelve una cadena, separada por comas, con todos los numeros que pertenece a la tarifa
	 * @return
	 */
	String getNumeros(){
		
		//Log.d(TAG,"getNumeros ->"+this.numeros.size());
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
	public boolean getDefecto() {
		return defecto;
	}
	
	/**
	 * Devuelve si una tarifa se asigna por defecto. Devuelve Si o No
	 * @return
	 */
	public String getDefectoSiNo() {
		if (this.defecto)
			return "Si";
		else
			return "No";
	}
	
	
	/**
	 * Asigna el valor del parámetro al atributo defecto.
	 * @param defecto
	 */
	public void setDefecto(boolean defecto) {
		this.defecto = defecto;
	}
	
	public void setDefecto(String defecto) {
		//Log.d(TAG,"Defecto="+defecto);
		if (defecto.equals("Si"))
			this.defecto=true;
		else
			this.defecto=false;
		//Log.d(TAG,"Defecto="+this.defecto);
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
	 * Si el día y la hora no pertenece a ninguna franja, devuelve null
	 * @param dia
	 * @param hora
	 * @return Franja
	 */
	public Franja getFranja(int dia,String hora){
		
		for (int i=0;i<this.franjas.size();i++)
		{
			//Log.d(TAG,"getFranja -> "+this.franjas.get(i).pertenece(dia, Time.valueOf(hora)));
			if (this.franjas.get(i).pertenece(dia, Time.valueOf(hora)))
			{
				return this.franjas.get(i);
			}
		}
		//Log.d(TAG,"Día = "+dia+" Hora = "+hora+" NO TIENE FRANJA EN LA TARIFA "+this.getNombre());
		return null;
	}
	
	/**
	 * Retorna la franja, dentro de la tarifa, a la que pertenece una fechayhora.
	 * @param fechayhora
	 * Cadena con la fecha y la hora.
	 * @return
	 */
	
	public Franja getFranja (String fechayhora){
		
		String sFecha =fechayhora.substring(0, 10).trim();
		String sHora=fechayhora.substring(10, fechayhora.length()).trim();
		String sDia=sFecha.substring(0,2);
		String sMes=sFecha.substring(3, 5);
		String sAno=sFecha.substring(6, 10);

		int ano= Integer.parseInt(sAno);
		int mes= Integer.parseInt(sMes)-1;
		int dia= Integer.parseInt(sDia);
		
		Calendar calendario=new GregorianCalendar(ano,mes,dia);
		calendario.setFirstDayOfWeek(Calendar.MONDAY);
		int diaSemana=calendario.get(Calendar.DAY_OF_WEEK);
		String shora=sHora;
		
		switch (diaSemana) {
		case Calendar.SUNDAY:
			diaSemana=6;
			break;

		default:
			diaSemana=diaSemana-2;
			break;
		}
		//Log.d(TAG,"dia de la semana -> "+diaSemana);

		return getFranja(diaSemana,shora);
	}


	/**
	 * Resetea los parametros relacionados con los segundos consumidos al mes y al día, así como 
	 * los consumidos de los limites del mes y del día.
	 */
	public void resetSegundos(){
		this.segConsumidosMes=0;
		this.segConsumidosDia=0;
		this.segConsumidosLimiteMes=0;
		this.segConsumidosLimiteDia=0;
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
	
	/**
	 * Suma segundos al total de segundos consumidos para el límite del mes
	 * @param segundos
	 */
	public void addSegConsumidosLimiteMes(int segundos){
		this.segConsumidosLimiteMes+=segundos;
	}
	
	/**
	 * Suma segundos al total de segundos consumidos para el límite del día
	 * @param segundos
	 */
	public void addSegConsumidosLimiteDia(int segundo) {
		this.segConsumidosLimiteDia += segundo;
	}
	
	
	/*****************************************************
	 ****************** SETTER ***************************
	 *****************************************************/
	
	public void setSegConsumidosLimiteDia(int segundos){
		this.segConsumidosLimiteDia=segundos;
	}
	
	public void setSegConsumidosDia(int segundos){
		this.segConsumidosDia=segundos;
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
	 * Asigna el valor limite a limite mensual de llamadas
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
	 * Asigna el valor limite a limite diario de llamadas
	 * @param limite
	 */
	public void setLimiteDia(int limite){
		this.limiteDia=limite;
	}
	
	public void setLimiteDia(String limite){
		
		try
		{
			limite=limite.replace(",",".");
			this.limiteDia=(int) Math.floor(Double.parseDouble(limite));
			//this.limite=Integer.parseInt(limite);
		}
		catch (Exception e)
		{
			this.limiteDia=0;
		}
	}
	
	
	/**
	 * Asigna el valor limite a limite de cada llamada
	 * @param limite
	 */
	public void setLimiteLlamada(int limite){
		this.limiteLlamada=limite;
	}
	
	public void setLimiteLlamada(String limite){
		
		try
		{
			limite=limite.replace(",",".");
			this.limiteLlamada=(int) Math.floor(Double.parseDouble(limite));
			//this.limite=Integer.parseInt(limite);
		}
		catch (Exception e)
		{
			this.limiteLlamada=0;
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
		if (numero.length()>2)
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
		//Log.i(TAG,"Modificar Franja");
		Franja factual=this.getFranja(id);
		//Log.i(TAG,"Nombre de la franja que se va ha modificar -> "+factual.getNombre());
		//Log.i(TAG,"Nombre de la franja que se va a añadir -> "+f.getNombre());
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
		//Log.d(TAG,"Intentando eliminar la franja= "+nombre);
        for (int i=0;i<this.franjas.size();i++)
        {
        	if (nombre.equals(this.franjas.get(i).getNombre()))
        	{
        		//Log.d(TAG,"Eliminando la franja= "+nombre);
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
		
		//Que el número coincida entero
		if (this.numeros.indexOf(numero)>-1)
			return true;
		
		Pattern p;
		for (String num : this.numeros)
		{
			p=Pattern.compile("999999999");
			if (num.contains("empieza por"))
			{
				String patron="";
				String inicio[]=num.split("por");
				inicio[1].trim();
				if (num.contains("+"))
				{
					patron="\\"+inicio[1].trim()+"[0-9]{1,6}";
				}
				else
				{
					patron=inicio[1].trim()+"[0-9]{1,6}";
				}
				p=Pattern.compile(patron);
			}
			if (num.contains("movil"))
			{
				p=Pattern.compile("^[6|71|72|73|74][0-9]{7,8}");
			}
			if (num.contains("fijo"))
			{
				p=Pattern.compile("^[8-9][1-8][0-9]{7}");
			}
			
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
			else
			{
				//El día y la hora no pertenece a ninguna franja
				
			}
		}
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
        	//Log.d(TAG,"hora inicio:"+hInicio+" - hora final:"+hFinal);
        	//Log.d(TAG,"Dia de la semana "+diasSemana+"="+diasSemana.indexOf("Lun"));
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
	
	/**
	 * Retorna el nombre de la franja que corresponde con un dia y una hora
	 * @param dia
	 * @param hora
	 * @return
	 */
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
