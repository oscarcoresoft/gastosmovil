package deeloco.android.gastos.Movil;

import java.io.FileWriter;
import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Date;
import java.util.SimpleTimeZone;

import android.widget.Toast;


import android.content.Context;
import android.util.Log;
import android.app.Activity;


public class tarifas implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TAG = "clase tarifas";
	private static String path="\\sdcard\\gastosmovil\\datosTarifas.xml";
	private static final int COSTE=0;
	private static final int ESTABLECIMIENTO=1;
	private static final int GASTOMINIMO=2;
	private static final int LIMITE=3;
	private static final int COSTE_FUERA_LIMITE=4;
	private static final int ESTABLECIMIENTO_FUERA_LIMITE=5;;
	
	/**
	 * Conjunto de franjas horarias definidas por el usuario
	 */
	private ArrayList <tarifa> tarifas=new ArrayList <tarifa>();
	private Context contexto;

	
	tarifas(){
		//cuando se instance un objeto, se debe cargar los valores de las franjas 
		cargarFranjas();
	}
	
	/**
	 * Carga las franjas horarias almacenadas en el fichero XML (franjas.xml) en el 
	 * ArrayList franjas
	 */
	boolean cargarFranjas(){
		//Leere fichero XML
		//Recorrer XML y cargar valores en franjas
		return false;
	}
	
	//get y set
	
	ArrayList <tarifa> getTarifas(){
		return this.tarifas;
	}
	
	/**
	 * Devuelve una tarifa dado un identificador
	 * @param id
	 * @return
	 */
	public tarifa getTarifa(int id){
		
        for (int i=0;i<this.tarifas.size();i++)
        {
        	if (this.tarifas.get(i).getIdentificador()==id)
        	{
        		return this.tarifas.get(i);
        	}
        }
		
        return null;
	}
	
	/**
	 * Devuelve la tarifa a la que pertence un numero de tlf.
	 * @param numero
	 * @return
	 */
	public tarifa getTarifa(String numero,String tarifaDef)
	{

		if (this.tarifas.size()==0)
			//No hay tarifas definidas
			return null;
		
		int idTarifa=indiceTarifa(numero);
		if (idTarifa==0)
		{
			//El numero no pertenece a ninguna tarifa. Aplicar la tarifa por defecto
			idTarifa=this.getId(tarifaDef);
		}
		
		if (idTarifa==-1)
		{
			return this.tarifas.get(0);
		}
		
		return this.tarifas.get(getIndice(idTarifa));
	}
	
	
	/**
	 * Devuelve un List con los nombres de todas las franjas
	 */

	public List <String> nombresTarifas(){
		ArrayList <String> nomTarifas=new ArrayList <String>();
		//Recorrer todas las tarifas y devolver tarifas

        for (int i=0;i<this.tarifas.size();i++)
        {
        	nomTarifas.add(this.tarifas.get(i).getNombre());
        	System.out.println(this.tarifas.get(i).getNombre());
        }
        Collections.sort(nomTarifas);
		return nomTarifas;
	}
	
	/**
	 * Devuelve el identificador de la tarifa a la que pertence el número de telefono
	 * Si no pertenece al ninguno, devuelve 1 = Tarifa Normal
	 * @param numero
	 * @return
	 */
	int tarifa(String numero){
		
        for (int i=0;i<this.tarifas.size();i++)
        {
        	if (this.tarifas.get(i).pertenece(numero))
        	{
        		return this.tarifas.get(i).getIdentificador();
        	}
        }
		return 1; //Identificador de la tarifa normal
	}
	
	/**
	 * Retorna el indice del array list que ocupa una tarifa con un nombre
	 * @param nombre
	 * @return
	 */
	private int getIndice(String nombre){
        for (int i=0;i<this.tarifas.size();i++)
        {
        	if (nombre.equals(this.tarifas.get(i).getNombre()))
        	{
        		return i;
        	}
        }
        return -1;
	}
	
	private int getIndice(int identificador){
        for (int i=0;i<this.tarifas.size();i++)
        {
        	if (this.tarifas.get(i).getIdentificador()==identificador)
        	{
        		return i;
        	}
        }
        return -1;
	}
	
	
	/**
	 * Devuelve el identificador de la tarifa con un nombre determinado
	 * @param nombre
	 * @return
	 */
	
	int getId(String nombre){
		
        for (int i=0;i<this.tarifas.size();i++)
        {
        	if (nombre.equals(this.tarifas.get(i).getNombre()))
        	{
        		return this.tarifas.get(i).getIdentificador();
        	}
        }
		return -1; //Identificador de la tarifa normal
	}
	
	
	public void getContexto(Context c)
	{
		this.contexto=c;
	}
	
	/**
	 * Devuelve el ultimo id que se puede asignar
	 * @return
	 */
	private int ultimoId(){
		int ultimo=0;
		for (int i=0;i<this.tarifas.size();i++)
		{
			if (ultimo<this.tarifas.get(i).getIdentificador())
			{
				ultimo=this.tarifas.get(i).getIdentificador();
			}
		}
		return ++ultimo;
	}
	
	/**
	 * Controla que no exista incompatibilidad de una franja con las existentes
	 */
	boolean incompatibilidad(int id,String nombre,Time horaIni,Time horaFinal,List <String> dias){
		return false;
	}
	
	/**
	 * Calcula el coste de una llamada
	 * @param dia (de la semana)
	 * @param hora (hora del día)
	 * @param duracion (en segundos)
	 * @return
	 */
	public double[] costeLlamada(String numero,String fechayhora, int duracion,String tarifaDef){
		
		//Conocer a que tarifa pertecene
		double[] retorno={0.0,0.0,0.0,0.0,0.0,0.0};
		//Log.d(TAG,"numero= "+numero);
		int idTarifa=indiceTarifa(numero);
		
		if (idTarifa==0)
		{
			//El numero no pertenece a ninguna tarifa. Aplicar la tarifa por defecto
			idTarifa=this.getId(tarifaDef);
			
		}
		
		if (idTarifa==-1)
		{
			
			if (this.tarifas.size()>0)
			{
				//No hay una tarifa por defecto definida
				//Colocar un toast indicando que no hay tarifa por defecto, se aplica xxx
				idTarifa=this.tarifas.get(0).getIdentificador();
			}
			else
				return retorno; //No hay tarifas
		}
		//GregorianCalendar(int year, int month, int day, int hour, int minute, int second)
		//GregorianCalendar calendario=new GregorianCalendar();
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
		
		int indice=getIndice(idTarifa);
		retorno=this.tarifas.get(indice).coste(numero, diaSemana, shora, duracion);
		return retorno;
	}
	
	/**
	 * Devuelve el identificador de la tarifa al que pertenece un numero 
	 * @param numero
	 * @return
	 */
	private int indiceTarifa (String numero){
		
        for (int i=0;i<this.tarifas.size();i++)
        {
        	if (this.tarifas.get(i).pertenece(numero))
        	{
        		return this.tarifas.get(i).getIdentificador();
        	}
        }
		return 0; //Identificador de la tarifa normal
	}
	
	/**
	 * Añadir una nueva tarifa
	 * @param t
	 */
	void addTarifa(tarifa t){
		//Cuando se añade una nueva tarifa, hay que asignarle un identificador nuevo si id=0
		if (t.getIdentificador()==0)
		{
			t.setIdentificador(ultimoId());
		}
		this.tarifas.add(t);
	}
	
	/**
	 * En la tarifa con identificador id, hay que cargarle los valores de la tarifa t
	 * @param id
	 * @param t
	 */
	void modificarTarifa(int id,tarifa t){
		Log.i(TAG,"Modificar Tarifa");
		tarifa tactual=this.getTarifa(id);
		Log.i(TAG,"Nombre de la tarifa que se va ha modificar -> "+tactual.getNombre());
		Log.i(TAG,"Nombre de la tarifa que se va a añadir -> "+t.getNombre());
		tactual.setNombre(t.getNombre());
		tactual.setMinimo(t.getMinimo());
		tactual.setLimite(t.getLimite());
		tactual.setNumeros(t.getNumeros());
		tactual.setColor(t.getColor());
		tactual.setDefecto(t.isDefecto());
		tactual.setFranjas(t.getFranjas());
	}
	
	
	/**
	 * Elimina la tarifa con nombre
	 * @param nombre
	 */
	public void deleteTarifa(String nombre)
	{
        for (int i=0;i<this.tarifas.size();i++)
        {
        	if (nombre.equals(this.tarifas.get(i).getNombre()))
        	{
        		this.tarifas.remove(i);
        	}
        }
	}
	
	
	/**
	 * Retorna el numero de tarifas que hay definidas
	 * @return
	 */
	public int numTarifas(){
		return tarifas.size();
	}
	
	
	/**
	 * Guarda en un fichero XML los datos de las tarifas y las franjas
	 * @return
	 */
	
	public boolean guardarTarifas(){
		
		ArrayList <Franja> franjas = new ArrayList <Franja>();
		String xmlFinal="<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>"; 
		xmlFinal+="<tarifas>";
		for (int t=0;t<this.tarifas.size();t++)
		{
			xmlFinal+="<tarifa id=\""+this.tarifas.get(t).getIdentificador()+"\">";
			xmlFinal+="<nombreTarifa>"+this.tarifas.get(t).getNombre()+"</nombreTarifa>";
			xmlFinal+="<gastoMinimo>"+this.tarifas.get(t).getMinimo()+"</gastoMinimo>";
			xmlFinal+="<limiteLlamadas>"+this.tarifas.get(t).getLimite()+"</limiteLlamadas>";
			xmlFinal+="<color>"+this.tarifas.get(t).getColor()+"</color>";
			xmlFinal+="<numeros>"+this.tarifas.get(t).getNumeros()+"</numeros>";
			franjas=this.tarifas.get(t).getFranjas();

			for (int f=0;f<franjas.size();f++)
			{
				xmlFinal+="<franja id=\""+franjas.get(f).getIdentificador()+"\">";
				xmlFinal+="<nombre>"+franjas.get(f).getNombre()+"</nombre>";
				xmlFinal+="<horaInicio>"+franjas.get(f).getHoraInicio()+"</horaInicio>";
				xmlFinal+="<horaFinal>"+franjas.get(f).getHoraFinal()+"</horaFinal>";
				xmlFinal+="<dias>"+franjas.get(f).getDias()+"</dias>";
				xmlFinal+="<coste>"+franjas.get(f).getCoste()+"</coste>";
				xmlFinal+="<establecimiento>"+franjas.get(f).getEstablecimiento()+"</establecimiento>";
				xmlFinal+="<limite>"+franjas.get(f).getLimite()+"</limite>";
				xmlFinal+="<costeFueraLimite>"+franjas.get(f).getCosteFueraLimite()+"</costeFueraLimite>";
				xmlFinal+="<establecimientoFueraLimite>"+franjas.get(f).getEstablecimientoFueraLimite()+"</establecimientoFueraLimite>";
				xmlFinal+="</franja>";
			}
			xmlFinal+="</tarifa>";
		}
		xmlFinal+="</tarifas>";
		
		//Guardar xml en un fichero
		
		//Escribir en la tarjeta SD
        
        FileWriter fWriter;
        boolean retorno=true;
        try{
             fWriter = new FileWriter(path);
             fWriter.write(xmlFinal);
             fWriter.flush();
             fWriter.close();

         }catch(Exception e){

                  e.printStackTrace();
                  Log.d(TAG,e.getMessage());
                  retorno=false;

         }
         return retorno;
	}
	
	/**
	 * Devuelve el color de la franja a la que pertenece numero
	 * @param numero
	 * @return
	 */
	public String getColor(String numero){
		
		for (int i=0; i<this.tarifas.size();i++)
		{
			if (this.tarifas.get(i).pertenece(numero))
				return this.tarifas.get(i).getColor();
		}
		return "Transparente";	
	}

}

