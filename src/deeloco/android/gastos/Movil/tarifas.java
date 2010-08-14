package deeloco.android.gastos.Movil;

import java.io.FileWriter;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.Log;

public class tarifas implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TAG = "clase tarifas";
	private static String path="\\sdcard\\gastosmovil\\datosTarifas.xml";
	/**
	 * Conjunto de franjas horarias definidas por el usuario
	 */
	private ArrayList <tarifa> tarifas=new ArrayList <tarifa>();
	
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
	
	/**
	 * Guarda las franjas alamcenadas en el ArrayList franjas en un fichero XML (franjas.xml)
	 */
	boolean guardarFranjas(){
		return false;
	}
	
	//get y set
	
	ArrayList <tarifa> getTarifas(){
		return this.tarifas;
	}
	
	tarifa getTarifa(int id){
		
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
	double costeLlamada(String numero,int dia, Time hora, int duracion){
		
		//Conocer a que tarifa pertecene
		int indice=indiceTarifa(numero);
		double coste=this.tarifas.get(indice).coste(numero, dia, hora, duracion);
		return coste;
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
		Log.d(TAG,"Añadiendo tarifa con id="+t.getIdentificador()+", con nombre: "+t.getNombre());
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
	int numTarifas(){
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
		Log.d(TAG,"Cargando XML");
		for (int t=0;t<this.tarifas.size();t++)
		{
			xmlFinal+="<tarifa id=\""+this.tarifas.get(t).getIdentificador()+"\">";
			xmlFinal+="<nombreTarifa>"+this.tarifas.get(t).getNombre()+"</nombreTarifa>";
			xmlFinal+="<gastoMinimo>"+this.tarifas.get(t).getMinimo()+"</gastoMinimo>";
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
				xmlFinal+="</franja>";
			}
			xmlFinal+="</tarifa>";
		}
		xmlFinal+="</tarifas>";
		Log.d(TAG,xmlFinal);
		
		//Guardar xml en un fichero
		
		//Escribir en la tarjeta SD
        
        FileWriter fWriter;
        boolean retorno=true;
        try{
        	Log.d(TAG,"Escribiendo XML");
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
}
