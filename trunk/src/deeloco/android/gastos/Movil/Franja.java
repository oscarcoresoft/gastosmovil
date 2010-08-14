package deeloco.android.gastos.Movil;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class Franja implements Serializable{
	
	/**
	 * 
	 */
	private static final String TAG = "Clase Franja";
	private static final long serialVersionUID = 1L;

	private final double iva=1.18;
	
	/**
	 * identificar de la franja.
	 * No se como que uso le voy a dar a este atributo
	 */
	private int identificador;
	
	/**
	 * nombre de la franja
	 */
	private String nombre;
	
	/**
	 * hora de inicio de la franja
	 */
	private Time horaInicio;
	
	/**
	 * hora final de la franja 
	 */
	private Time horaFinal;
	
	/**
	 * dias de la semana en la que se aplica la franja
	 */
	private ArrayList <String> dias=new ArrayList <String>();
	
	/**
	 * Coste, en centimos, de las llamadas llevadas a cabo en esta franja (sin iva y por minuto). 
	 */
	private double coste;
	
	/**
	 * Coste, en centimos, del establecimiento de llamadas para esta franja (sin iva).
	 */
	private double establecimiento;
	
	/**
	 * Limite, en segundos, de las llamadas para esta franja.
	 */
	private int limite;
	
	/**
	 * Coste de las llamadas superado el límite de tiempo.
	 */
	private double costeFueraLimite;
	
	

	/**
	 * Constructor de la clase
	 * @param id
	 * @param nombre
	 * @param horaIni
	 * @param horaFinal
	 * @param dias
	 * @param coste
	 * @param establecimiento
	 * @param limite
	 * @param costeFueraLimite
	 */
	Franja(int id,String nombre,Time horaIni,Time horaFinal,ArrayList <String> dias, double coste, double establecimiento, int limite, double costeFueraLimite){
		this.identificador=id; //hay que calcularlo
		this.nombre=nombre;
		this.horaInicio=horaIni;
		this.horaFinal=horaFinal;
		this.dias=dias;
		this.coste=coste;
		this.establecimiento=establecimiento;
		this.limite=limite;
		this.costeFueraLimite=costeFueraLimite;
	}
	
	/**
	 * Constructor de la clase
	 * @param id
	 * @param nombre
	 * @param horaIni
	 * @param horaFinal
	 * @param dias
	 * @param coste
	 * @param establecimiento
	 */
	Franja(int id,String nombre,Time horaIni,Time horaFinal,ArrayList <String> dias, double coste, double establecimiento){
		this.identificador=id;
		this.nombre=nombre;
		this.horaInicio=horaIni;
		this.horaFinal=horaFinal;
		this.dias=dias;
		this.coste=coste;
		this.establecimiento=establecimiento;
		this.limite=0;
		this.costeFueraLimite=0;
	}
	
	Franja(String id){
		this.identificador=Integer.parseInt(id);
	}
	
	Franja (int id){
		this.identificador=id;
	}
	/**
	 * Retorna el valor del identificar
	 * @return
	 */
	int getIdentificador(){
		return this.identificador;
	}
	
	/**
	 * Retorna el valor del nombre de la franja
	 * @return
	 */
	String getNombre(){
		return this.nombre;
	}
	
	/**
	 * Retorna la hora de inicio de la franja
	 * @return
	 */
	Time getHoraInicio(){
		return this.horaInicio;
	}
	
	/**
	 * Retorna la hora final de la franja
	 * @return
	 */
	Time getHoraFinal(){
		return this.horaFinal;
	}
	
	/**
	 * Retorna el coste de laa llamadas en esta franja
	 * @return
	 */
	double getCoste(){
		return this.coste;
	}
	
	/**
	 * Retorna el coste del establecimiento de llamadas
	 * @return
	 */
	double getEstablecimiento(){
		return this.establecimiento;
	}
	
	/**
	 * Retorna el limite del coste de las llamadas
	 * @return
	 */
	public int getLimite(){
		return this.limite;
	}
	
	/**
	 * Retorna el coste de las llamadas que superan el límite
	 * @return
	 */
	double getCosteFueraLimite(){
		return this.costeFueraLimite;
	}
	
	/**
	 * Retorna una lista con los días a los que se le asigna la franja
	 * @return
	 */
	List <String> getDias(){
		return this.dias;
	}
	
	/**
	 * Asigna un valor al identificador
	 * @param id
	 */
	void setIdentificador(int id){
		this.identificador=id;
	}
	void setIdentificador(String id){
		this.identificador=Integer.parseInt(id);
	}
	
	/**
	 * Asigna el valor de nombre a la franja
	 * @param nombre
	 */
	void setNombre(String nombre){
		this.nombre=nombre;
	}
	
	/**
	 * Asigna la hora de inicio de la franja
	 * @param horaInicio
	 */
	void setHoraInicio(Time horaInicio){
		this.horaInicio=horaInicio;
	}
	void setHoraInicio(String horaInicio){
		Log.d(TAG,"Parametro horaInicio ="+horaInicio);
		this.horaInicio=Time.valueOf(horaInicio);
	}
	
	/**
	 * Asigna la hora final de la franja
	 * @param horaFinal
	 */
	void setHoraFinal(Time horaFinal){
		this.horaFinal=horaFinal;
	}
	void setHoraFinal(String horaFinal){
		this.horaFinal=Time.valueOf(horaFinal);
	}
	
	/**
	 * Asigna los días en los que la franja esta vigente
	 * @param dias
	 */
	void setDias(ArrayList <String> dias){
		this.dias=dias;
	}
	void setDias(String dias){
		if (dias.indexOf("[")!=-1)
		{
			//tiene []
			dias=dias.substring(1, dias.length()-1);
			Log.d(TAG,"Dias recortado="+dias);
		}
    	String data[] = dias.split(",");
    	for (int i=0; i < data.length; i++) {
    		 this.dias.add(data[i].trim());
    		}
	}
	
	
	/**
	 * Añade un día a la lista de dias
	 * @param dia
	 */
	void addDia(String dia){
		if (this.dias.indexOf(dia)<0)
			this.dias.add(dia);
	}
	void deleteDia(String dia){
		if (this.dias.indexOf(dia)>-1)
			this.dias.remove(dia);
	}
	/**
	 * Asigna el coste de las llamadas para esta franja
	 * @param coste
	 */
	void setCoste(double coste){
		this.coste=coste;
	}
	void setCoste(String coste){
		this.coste=Double.parseDouble(coste);
	}
	
	/**
	 * Asigna el coste del establecimiento de llamadas para esta franja
	 * @param establecimientos
	 */
	void setEstablecimiento(double establecimientos){
		this.establecimiento=establecimientos;
	}
	void setEstablecimiento(String establecimiento){
		this.establecimiento=Double.parseDouble(establecimiento);
	}
	
	/**
	 * Asigna el limite, en segundos, que marca el coste de las llamadas
	 * @param limite
	 */
	void setLimite(int limite){
		this.limite=limite;
	}
	void setLimite(String limite){
		this.limite=Integer.parseInt(limite);
	}
	
	/**
	 * Asigna el coste de las llamadas que pasan el límite de duración
	 * @param costeFueraLimite
	 */
	void setCosteFueraLimite(double costeFueraLimite){
		this.costeFueraLimite=costeFueraLimite;
	}
	void setCosteFueraLimite(String costeFueraLimite){
		this.costeFueraLimite=Double.parseDouble(costeFueraLimite);
	}
	
	/**
	 * Devuelva si un día y una hora esta incluida en la franja
	 * @param dia
	 * @param hora
	 * @return
	 */
	boolean pertenece(int dia, Time hora){
		
		Time mediaNoche24=Time.valueOf("24:00:00");
		Time mediaNoche00=Time.valueOf("00:00:00");
		
		System.out.println("Días de la franja "+this.identificador+" = "+this.dias.size());
		if (this.dias.indexOf(dia)==-1)
			return false;
		else
		{
			//El día coincide, hay que comprobar la franja
			System.out.println("Hora Inicio="+this.horaInicio.toString()+" - Hora final="+this.horaFinal.toString());
			if (this.horaInicio.before(this.horaFinal))
			{
				//Las horas estan en el mismo día
				System.out.println("Las horas estan en el mismo día");
				if (this.horaInicio.before(hora)&&this.horaFinal.after(hora))
				{
					System.out.println("**");
					return true;
				}
			}
			else
			{
				//La hora final esta en el día siguiente
				System.out.println("La hora final estan en el día siguiente");
				if ((this.horaInicio.before(hora)&&mediaNoche24.after(hora))||(mediaNoche00.before(hora)&&this.horaFinal.after(hora)))
				{
					System.out.println("*");
					return true;
				}
				
			}
			return false;
		}
	}
	
	/**
	 * Devuelve el coste de la llamada.
	 * Si el día y la hora no pertenece a la franja, devuelve -1.0
	 * @param dia
	 * @param hora
	 * @param duracion
	 * @return
	 */
	double coste(int dia, Time hora, int duracion){
		
		System.out.println("Calculando el coste para los datos "+dia+" - "+hora.toString()+" - "+duracion);
		double conIvaPorSegundosEnEuros;
		double costePorSegundo;
		double costeTotal;
		if (pertenece(dia,hora))
		{
			costePorSegundo=(this.coste/100)/60;
			System.out.println("Coste por Segundos . "+costePorSegundo);
			conIvaPorSegundosEnEuros=costePorSegundo*iva;
			System.out.println("Con Iva por Segundos . "+conIvaPorSegundosEnEuros);
			costeTotal=conIvaPorSegundosEnEuros*duracion;
			//Le añadimos el establecimiento de llamada
			System.out.println("Coste Total . "+costeTotal);
			costeTotal=costeTotal+((this.establecimiento/100)*iva);
			System.out.println("Coste Total con establecimiento de llamada . "+costeTotal);
			return costeTotal;
		}
		else
			return -1.0;
	}
	
	
	//String[] colores = getResources().getStringArray(R.array.colores);
	
	public boolean[] diasSeleccionados()
	{
		String [] diasSemana={"Lun","Mar","Mie","Jue","Vie","Sab","Dom"};
		boolean [] retorno={false,false,false,false,false,false,false};
		Log.d(TAG,"Dias de la semana almacenados"+this.dias.toString());
		for (int i=0;i<7;i++)
		{
			Log.d(TAG,"Para i="+i+" - Buscar "+diasSemana[i]+" en "+this.dias.toString());
			if (this.dias.indexOf(diasSemana[i])>-1)
			{
				Log.d(TAG," Coincidencia "+i+" -- "+diasSemana[i]);
				retorno[i]=true;
			}
		}
		
		return retorno;
	}
}