package deeloco.android.gastos.Movil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.util.Log;


public class tarifas implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TAG = "clase tarifas";
	private static String path="/sdcard/gastosmovil/datosTarifas.xml";
	
	/**
	 * Conjunto de franjas horarias definidas por el usuario
	 */
	private ArrayList <tarifa> tarifas=new ArrayList <tarifa>();
	private Context contexto;

	
	tarifas(){
		//cuando se instance un objeto, se debe cargar los valores de las franjas 
		//cargarFranjas();
	}
	
	/**
	 * Carga las franjas horarias almacenadas en el fichero XML (franjas.xml) en el 
	 * ArrayList franjas
	 */
	boolean cargarFranjas(){
		//Leere fichero XML
		//Recorrer XML y cargar valores en franjas
		boolean retorno;
		 try
	        {
	        	//Comprobamos si el fichero esta creado. Si es que no, se crea.
	        	File f=new File(path);
	        	if (!f.exists())
	        	{
	        		//El fichero no existe, hay que crearlo
	        		try{
	        			boolean dir = new File("/sdcard/gastosmovil").mkdir(); //Creamos el directorio
	        			if (dir) //Si se ha creado correctamente el directorio
	        			{
	        				f.createNewFile(); //Creamos el fichero 
	        			}
	        		}
	        		catch (Exception e){ //Error al crear el directorio o el fichero
	        			Log.e("GM:Tarifas.java","Error al crear el fichero o directorio: " + e.getMessage());
	        			retorno=false;
	        		}
	        	}
	        	
		        SAXParserFactory spf = SAXParserFactory.newInstance();
		        SAXParser sp = spf.newSAXParser();
		        /* Get the XMLReader of the SAXParser we created. */
		        XMLReader xr = sp.getXMLReader();
		        /* Create a new ContentHandler and apply it to the XML-Reader*/
		        TarifasParserXML tarifasXML = new TarifasParserXML();
		        tarifasXML.setTarifas(this);
		        xr.setContentHandler(tarifasXML);
		        xr.parse(new InputSource (new FileReader(path)));
		        /* Parsing has finished. */		   
		        retorno=true;
	        }
	        catch (Exception e)
	        {
	        	//Si el error se produce porque no existe el fichero xml, hay que crearlo.
	        	//Tambien hay que crear el directorio	        	
	        	Log.e("GM:Tarifas.java", "Error al parsear. "+e.toString()+" ("+e.hashCode()+")");
	        	retorno=false;
	        }
		return retorno;
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
	 * Número de teléfono
	 * @param fechayhora
	 * Fecha y hora de la llamada.
	 * @return
	 */
	public tarifa getTarifa(String numero,String fechayhora)
	{

		if (this.tarifas.size()==0)
			//No hay tarifas definidas
			return null;
		
		int idTarifa=indiceTarifa(numero);
		//Log.d(TAG,"indiceTarifa("+numero+")="+idTarifa);
		if (idTarifa==0)
		{
			//El numero no pertenece a ninguna tarifa. Aplicar la tarifa por defecto
			//Como podemos tener más de una tarifa por defecto
			for (int tt=0;tt<this.tarifas.size();tt++)
			{
				if (this.tarifas.get(tt).getDefecto())
				{
					//Es una tarifa por defecto
					//Log.d(TAG,this.tarifas.get(tt).getNombre()+" .. es una tarifa por defecto");
					Franja f=this.tarifas.get(tt).getFranja(fechayhora);
					if (f!=null)
					{
						//Log.d(TAG,"Franja distinto de NULL");
						return this.tarifas.get(tt);
					}
					else
					{
						//Log.d(TAG,"Franja NULL");
					}
				}
			}
			return null;
			
		}
		
		if (idTarifa==-1)
		{
			return this.tarifas.get(0);
		}
		
		return this.tarifas.get(getIndice(idTarifa));
	}
	
	
	/**
	 * Devuelve un List con los nombres de todas las Tarifas
	 */

	public List <String> nombresTarifas(){
		ArrayList <String> nomTarifas=new ArrayList <String>();
		//Recorrer todas las tarifas y devolver tarifas

        for (int i=0;i<this.tarifas.size();i++)
        {
        	nomTarifas.add(this.tarifas.get(i).getNombre());
        	//System.out.println(this.tarifas.get(i).getNombre());
        }
        Collections.sort(nomTarifas);
		return nomTarifas;
	}	
	
	
	/**
	 * Retorna el número de tarifas que estan definidas como por defecto.
	 * @return
	 */
	public int getNumTarifasDefecto()
	{
		int retorno=0;
		for (int i=0;i<this.tarifas.size();i++)
        {
        	if (this.tarifas.get(i).getDefecto())
        		retorno++;
        }
		return retorno;
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
	 * 
	 * @param nombre
	 * @return
	 */
	
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
	
	public int getId(String nombre){
		
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
	public int ultimoId(){
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
	
	
	
	public boolean existeTarifa(int id)
	{
		
        for (int i=0;i<this.tarifas.size();i++)
        {
        	if (this.tarifas.get(i).getIdentificador()==id)
        	{
        		return true;
        	}
        }
		
        return false;
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
		int retorno=0;
        for (int i=0;i<this.tarifas.size();i++)
        {
        	if (this.tarifas.get(i).pertenece(numero))
        	{
        		retorno= this.tarifas.get(i).getIdentificador();
        		//Si el numero está entero (no es una expresión), nos quedamos con este.
        		if (this.tarifas.get(i).getNumeros().indexOf(numero)>-1)
        			return retorno;
        	}
        }
		return retorno; //Identificador de la tarifa normal
	}
	
	/**
	 * Añadir una nueva tarifa
	 * @param t
	 */
	void addTarifa(tarifa t){
		//Cuando se añade una nueva tarifa, hay que asignarle un identificador nuevo si id=0
		if (t.getIdentificador()==0)
		{
			//Log.d(TAG,"Nueva tarifa="+t.getNombre());
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
		//Log.i(TAG,"Defecto Actual-> "+tactual.getDefecto());
		//Log.i(TAG,"Defecto modificar-> "+t.getDefecto());
		//Log.i(TAG,"Nombre de la tarifa que se va a añadir -> "+t.getNombre());
		tactual.setNombre(t.getNombre());
		tactual.setLimite(t.getLimite());
		tactual.setLimiteDia(t.getLimiteDia());
		tactual.setLimiteLlamada(t.getLimiteLlamada());
		tactual.setNumeros(t.getNumeros());
		tactual.setColor(t.getColor());
		tactual.setDefecto(t.getDefecto());
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
		String xmlFinal="<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"; 
		xmlFinal+="<tarifas>";
		for (int t=0;t<this.tarifas.size();t++)
		{
			xmlFinal+="<tarifa id=\""+this.tarifas.get(t).getIdentificador()+"\">";
			xmlFinal+="<nombreTarifa>"+this.tarifas.get(t).getNombre()+"</nombreTarifa>";
			xmlFinal+="<limiteLlamadas>"+this.tarifas.get(t).getLimite()+"</limiteLlamadas>";
			xmlFinal+="<limiteLlamadasDia>"+this.tarifas.get(t).getLimiteDia()+"</limiteLlamadasDia>";
			xmlFinal+="<limiteLlamada>"+this.tarifas.get(t).getLimiteLlamada()+"</limiteLlamada>";
			xmlFinal+="<color>"+this.tarifas.get(t).getColor()+"</color>";
			xmlFinal+="<numeros>"+this.tarifas.get(t).getNumeros()+"</numeros>";
			xmlFinal+="<defecto>"+this.tarifas.get(t).getDefectoSiNo()+"</defecto>";
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

	
	public void cambiarIva(double iva){
		ArrayList <Franja> franjas = new ArrayList <Franja>();
		
		for (int t=0; t<this.tarifas.size();t++)
		{
			franjas=this.tarifas.get(t).getFranjas();
			for (int f=0;f<franjas.size();f++)
			{
				franjas.get(f).setIva(iva);
			}
		}

	}
	
	/**
	 * La tarifa que tiene @param nombre como nombre, se pone como tarifa que se aplica por defecto. 
	 * @param nombre
	 * Nombre de la tarifa que se va a poner por defecto.
	 */
	public void setTarifaDefecto(String nombre)
	{
        for (int i=0;i<this.tarifas.size();i++)
        {
        	if (nombre.equals(this.tarifas.get(i).getNombre()))
        		this.tarifas.get(i).setDefecto(true);
        }
	}
	
	
	/**
	 * Resetea todos los contadores de segundos de todas las tarifas
	 */
	public void resetSegundos()
	{
        for (int i=0;i<this.tarifas.size();i++)
        {
        	this.tarifas.get(i).resetSegundos();
        }
	}
	
	/**
	 * Resetea todos los contadores de segundos consumidos de un día de todas las tarifas
	 */
	public void resetSegundosConsumidosDia()
	{
        for (int i=0;i<this.tarifas.size();i++)
        {
        	this.tarifas.get(i).setSegConsumidosDia(0);
        	this.tarifas.get(i).setSegConsumidosLimiteDia(0);
        }
	}
	
	
	/**
	 * Retorna si las tarifas que son predeterminadas son compatibles en los horarios
	 * @return
	 */
	public boolean compatibilidadHorarioTarifasDefecto(){
		int[][] mControl=new int[7][24];
		boolean conflicto=true;
		//Iniciando mControl
		for (int f=0;f<7;f++)
			for (int c=0;c<24;c++)
				mControl[f][c]=0;
		
		//recorremos todas las tarifas que se aplican por defecto
		tarifa t=null;
		for (int tt=0;tt<this.tarifas.size();tt++)
		{
		
			t=this.tarifas.get(tt);
			
			//recorremos todas las franjas que esten definidas como por defecto
			
	        for (int i=0;(i<t.getFranjas().size()&&t.getDefecto());i++)
	        {
	        	int fila=0;
	        	List<String> diasSemana=new ArrayList <String>();
	        	diasSemana=t.getFranjas().get(i).getDias();
	        	int hInicio=Integer.parseInt((""+t.getFranjas().get(i).getHoraInicio()).split(":")[0]);
	        	int hFinal=Integer.parseInt((""+t.getFranjas().get(i).getHoraFinal()).split(":")[0]);
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
	 * Retorna el total de segundo consumidos del mes, de todas las tarifas
	 * @return double
	 */
	public int getSegConsumidosMes()
	{
		int retorno=0;
		for (int i=0;i<this.tarifas.size();i++)
		{
			retorno+=this.tarifas.get(i).getSegConsumidosMes();
		}
		return retorno;
	}
	
	/**
	 * Retorna el total de segundo consumidos del día, de todas las tarifas
	 * @return int
	 */
	public int getSegConsumidosDia()
	{
		int retorno=0;
		for (int i=0;i<this.tarifas.size();i++)
		{
			retorno+=this.tarifas.get(i).getSegConsumidosDia();
		}
		return retorno;
	}
	
	
	
	
	/**
	 * Retorna el total de segundo consumidos del limite del mes, de todas las tarifas.
	 * @return String
	 */
	public String getTextoConsumidosMesLimite()
	{
		String retorno="";
		for (int i=0;i<this.tarifas.size();i++)
		{
			if (this.tarifas.get(i).getLimite()>0)
			{
				//tarifa que tiene limite xxx de xxxx
				retorno+=FunGlobales.segundosAHoraMinutoSegundo(this.tarifas.get(i).getSegConsumidosLimiteMes())+" de "+FunGlobales.segundosAHoraMinutoSegundo(this.tarifas.get(i).getLimite()*60)+" @ ";
			}
			else
			{
				//tarifa que no tiene limite
				retorno+="Sin Límite"+" @ ";
			}
		}
		return retorno;
	}
	
	
	
	public String[] getNombresTarifas()
	{
		String [] retorno=new String[this.tarifas.size()];
		
		for (int i=0;i<this.tarifas.size();i++)
		{
			retorno[i]=this.tarifas.get(i).getNombre();
		}
		return retorno;
	}
	
}

