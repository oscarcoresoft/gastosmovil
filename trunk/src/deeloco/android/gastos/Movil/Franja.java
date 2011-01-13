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
	private static final int COSTE=0;
	private static final int ESTABLECIMIENTO=1;
	private static final int GASTOMINIMO=2;
	private static final int LIMITE=3;
	private static final int COSTE_FUERA_LIMITE=4;
	private static final int ESTABLECIMIENTO_FUERA_LIMITE=5;

	/**
	 * Impuestos que se van a aplicar a la hora de calcular el coste de las llamadas. Por defecto 1.18
	 */
	private double iva=1;
	
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
	 * Si las llamadas que pertenecen a la franja se contabilizan para el limite.
	 */
	private boolean limite;
	
	/**
	 * Coste de las llamadas superado el límite de tiempo.
	 */
	private double costeFueraLimite;
	
	/**
	 * Establecimiento de las llamadas superado el límite de tiempo.
	 */
	private double establecimientoFueraLimite;

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
	 * @param establecimientoFueraLimite
	 */
	Franja(int id,String nombre,Time horaIni,Time horaFinal,ArrayList <String> dias, double coste, double establecimiento, boolean limite, double costeFueraLimite, double establecimientoFueraLimite){
		this.identificador=id; //hay que calcularlo
		this.nombre=nombre;
		this.horaInicio=horaIni;
		this.horaFinal=horaFinal;
		this.dias=dias;
		this.coste=coste;
		this.establecimiento=establecimiento;
		this.limite=limite;
		this.costeFueraLimite=costeFueraLimite;
		this.establecimientoFueraLimite=establecimientoFueraLimite;

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
		this.limite=false;
		this.costeFueraLimite=0;
		this.establecimientoFueraLimite=0;
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
	 * Retorna si la franja cuenta para el límite o no.
	 * @return
	 */
	public boolean getLimite(){
		return this.limite;
	}
	
	public String getLimiteSiNo(){
		if (this.limite)
			return "Si";
		else
			return "No";
	}
	
	/**
	 * Retorna el coste de las llamadas que superan el límite
	 * @return
	 */
	double getCosteFueraLimite(){
		return this.costeFueraLimite;
	}
	
	/**
	 * Retorna el establecimiento de las llamadas fuera del limite
	 * @return
	 */
	public double getEstablecimientoFueraLimite(){
		return this.establecimientoFueraLimite;
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
		//Log.d(TAG,"Parametro horaInicio ="+horaInicio);
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
			//Log.d(TAG,"Dias recortado="+dias);
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
		try
		{
			coste=coste.replace(",",".");
			this.coste=Double.parseDouble(coste);
		}
		catch (Exception e)
		{
			this.coste=0.0;
		}
	}
	
	/**
	 * Asigna el coste del establecimiento de llamadas para esta franja
	 * @param establecimientos
	 */
	void setEstablecimiento(double establecimientos){
		this.establecimiento=establecimientos;
	}
	void setEstablecimiento(String establecimiento){
		
		try
		{
			establecimiento=establecimiento.replace(",",".");
			this.establecimiento=Double.parseDouble(establecimiento);
		}
		catch (Exception e)
		{
			this.establecimiento=0.0;
		}
	}
	
	/**
	 * Asigna el limite, en segundos, que marca el coste de las llamadas
	 * @param limite
	 */
	void setLimite(boolean limite){
		this.limite=limite;
	}
	void setLimite(String limite){
		if (limite.equals("si")||limite.equals("Si")||limite.equals("SI")||limite.equals("Verdadero")||limite.equals("verdadero")||limite.equals("true")||limite.equals("True"))
			this.limite=true;
		else
			this.limite=false;
	}
	
	/**
	 * Asigna el coste de las llamadas que pasan el límite de duración
	 * @param costeFueraLimite
	 */
	void setCosteFueraLimite(double costeFueraLimite){
		this.costeFueraLimite=costeFueraLimite;
	}
	void setCosteFueraLimite(String costeFueraLimite){
		
		try
		{
			costeFueraLimite=costeFueraLimite.replace(",",".");
			this.costeFueraLimite=Double.parseDouble(costeFueraLimite);
		}
		catch (Exception e)
		{
			this.costeFueraLimite=0.0;
		}
	}
	
	
	/**
	 * Asigna el coste del establecimiento de llamada que pasan el límite de duración
	 * @param establecimientoFueraLimite
	 */
	void setEstablecimientoFueraLimite(double establecimientoFueraLimite){
		this.establecimientoFueraLimite=establecimientoFueraLimite;
	}
	
	void setEstablecimientoFueraLimite(String establecimientoFueraLimite){
		
		try
		{
			establecimientoFueraLimite=establecimientoFueraLimite.replace(",",".");
			this.establecimientoFueraLimite=Double.parseDouble(establecimientoFueraLimite);
		}
		catch (Exception e)
		{
			this.establecimientoFueraLimite=0.0;
		}
	}
	
	/**
	 * Asigna el iva que se va utilizar
	 * @param iva
	 */
	public void setIva(double iva){
		this.iva=iva;
	}
	
	/**
	 * Devuelva si un día y una hora esta incluida en la franja
	 * @param dia
	 * @param hora
	 * @return
	 */
	boolean pertenece(int dia, Time hora){
		
		//REVISAR ESTO.
		String [] diasSemana={"Lun","Mar","Mie","Jue","Vie","Sab","Dom"};
		
		Time mediaNoche24=Time.valueOf("24:00:00");
		Time mediaNoche00=Time.valueOf("00:00:00");
		//Comprobamos que el día esta incluido en la tarifa
		if (this.dias.indexOf(diasSemana[dia])==-1)
			return false;
		else
		{
			//El día coincide, hay que comprobar la franja
			if (this.horaInicio.before(this.horaFinal))
			{
				//Las horas estan en el mismo día
				if (this.horaInicio.before(hora)&&this.horaFinal.after(hora))
				{
					return true;
				}
			}
			else
			{
				//La hora final esta en el día siguiente
				//System.out.println("La hora final estan en el día siguiente");
				if ((this.horaInicio.before(hora)&&mediaNoche24.after(hora))||(mediaNoche00.before(hora)&&this.horaFinal.after(hora)))
				{
					//System.out.println("*");
					return true;
				}
				
			}
			return false;
		}
	}
	
	/**
	 * Devuelve el coste de la llamada.
	 * Si el día y la hora no pertenece a la franja, devuelve -1.0
	 * Antes de utilizar el método coste hay que asegurarse que asegurarse que el dia y la hora pertenece
	 * a la franja, utilizar el método pertenece
	 * @param dia
	 * @param hora
	 * @param duracion
	 * @return
	 */
	public double[] coste(int dia, Time hora, int duracion){
		
		//System.out.println("Calculando el coste para los datos "+dia+" - "+hora.toString()+" - "+duracion);
		double conIvaPorSegundosEnEuros;
		double costePorSegundo;
		double costeTotal;
		double[] retorno={0.0,0.0,0.0,0.0,0.0,0.0};
		
		costePorSegundo=(this.coste/100)/60;
		//System.out.println("Coste por Segundos . "+costePorSegundo);
		conIvaPorSegundosEnEuros=costePorSegundo*iva;
		//System.out.println("Con Iva por Segundos . "+conIvaPorSegundosEnEuros);
		costeTotal=conIvaPorSegundosEnEuros*duracion;
		//Le añadimos el establecimiento de llamada
		//System.out.println("Coste Total . "+costeTotal);
		//costeTotal=costeTotal+((this.establecimiento/100)*iva);
		retorno[COSTE]=costeTotal+((this.establecimiento/100)*iva);
		retorno[ESTABLECIMIENTO]=this.establecimiento;
		retorno[GASTOMINIMO]=0.0; //En tarifa
		retorno[LIMITE]=0.0; //En Tarifa
		retorno[COSTE_FUERA_LIMITE]=0.0;
		retorno[ESTABLECIMIENTO_FUERA_LIMITE]=0.0;
		if (this.limite)
		{
			//Calcular el coste fuera del limite
			costePorSegundo=(this.costeFueraLimite/100)/60;
			conIvaPorSegundosEnEuros=costePorSegundo*iva;
			costeTotal=conIvaPorSegundosEnEuros*duracion;
			retorno[COSTE_FUERA_LIMITE]=costeTotal+((this.establecimientoFueraLimite/100)*iva);
			retorno[ESTABLECIMIENTO_FUERA_LIMITE]=this.establecimientoFueraLimite;
		}
			
		//System.out.println("Coste Total con establecimiento de llamada . "+costeTotal);
		return retorno;
	}
	
	
	
	/**
	 * Calcula el coste con iva y establecimento, de una llamada, dado la tarifa a la que pertenece y la duración
	 * @param t
	 * @param duracion
	 * @return
	 */
	public double coste (tarifa t,int duracion){
		double conIvaPorSegundosEnEuros;
		double costePorSegundo;
		double costeTotal;
		double costeTotalConEstablecimiento;
		//Log.d(TAG,"Consumido="+t.getSegConsumidosLimiteMes()+" | Limite"+t.getLimite());
		if ( ((t.getSegConsumidosLimiteMes()>(t.getLimite()*60))|| (t.getSegConsumidosLimiteDia()>(t.getLimiteDia()*60)))
				&& this.getLimite() )
		{
			//Se han consumido más segundos de los limites mensuales
			//El coste hay que calcularlo con los valores de coste fuera de limite
			//Aqui hay que controlar la parte de la llamada que sobre pasa del limite
			//MES
			if ((t.getSegConsumidosLimiteMes()-duracion)<(t.getLimite()*60)&&((t.getSegConsumidosLimiteMes()>(t.getLimite()*60))&&t.getLimite()>0))
				duracion=t.getSegConsumidosLimiteMes()-(t.getLimite()*60);
			else
				//DIA
				if ((t.getSegConsumidosLimiteDia()-duracion)<(t.getLimiteDia()*60)&&((t.getSegConsumidosLimiteDia()>(t.getLimiteDia()*60))&&t.getLimiteDia()>0))
					duracion=t.getSegConsumidosLimiteDia()-(t.getLimiteDia()*60);
			
			
			/*
			if (((t.getSegConsumidosLimiteMes()-duracion)<(t.getLimite()*60))||((t.getSegConsumidosLimiteDia()-duracion)<(t.getLimiteDia()*60)))
			{
				Log.d(TAG,"Duración Original ="+duracion);
				//se ha sobrepasado los limite durante esta llamada
				if ((t.getSegConsumidosLimiteMes()>(t.getLimite()*60))&&t.getLimite()>0)
					duracion=t.getSegConsumidosLimiteMes()-(t.getLimite()*60);
				
				if ((t.getSegConsumidosLimiteDia()>(t.getLimiteDia()*60))&&t.getLimiteDia()>0)
					duracion=t.getSegConsumidosLimiteDia()-(t.getLimiteDia()*60);
					
				Log.d(TAG,"Duración Modificada ="+duracion);
			}*/
			
			
			//Log.d(TAG,"IVA="+iva);
			costePorSegundo=(this.costeFueraLimite/100)/60;
			//conIvaPorSegundosEnEuros=costePorSegundo*iva;
			conIvaPorSegundosEnEuros=costePorSegundo;
			costeTotal=conIvaPorSegundosEnEuros*duracion;
			//costeTotalConEstablecimiento=costeTotal+((this.establecimientoFueraLimite/100)*iva);
			costeTotalConEstablecimiento=costeTotal+((this.establecimientoFueraLimite/100));
		}
		else
		{
			//Log.d(TAG,"IVA="+iva);
			costePorSegundo=(this.coste/100)/60;
			//conIvaPorSegundosEnEuros=costePorSegundo*iva;
			conIvaPorSegundosEnEuros=costePorSegundo;
			costeTotal=conIvaPorSegundosEnEuros*duracion;
			//costeTotalConEstablecimiento=costeTotal+((this.establecimiento/100)*iva);
			costeTotalConEstablecimiento=costeTotal+((this.establecimiento/100));
		}
		return costeTotalConEstablecimiento;
	}
	
	/**
	 * Retorna el establecimiento para una llamada, dado la tarifa a la que pertecene y teniendo en cuenta si se han sobrepasado los límite o no (Sin IVA).
	 * @param t
	 * @param duracion
	 * @return
	 */
	public double establecimiento(tarifa t){

		double establecimiento;
		
		if (t.getSegConsumidosLimiteMes()>t.getLimite()*60)
		{

			establecimiento=(this.establecimientoFueraLimite/100)*iva;
		}
		else
		{
			establecimiento=(this.establecimiento/100)*iva;
		}
		return establecimiento;
		
	}
	
	//String[] colores = getResources().getStringArray(R.array.colores);
	
	public boolean[] diasSeleccionados()
	{
		String [] diasSemana={"Lun","Mar","Mie","Jue","Vie","Sab","Dom"};
		boolean [] retorno={false,false,false,false,false,false,false};
		//Log.d(TAG,"Dias de la semana almacenados"+this.dias.toString());
		for (int i=0;i<7;i++)
		{
			//Log.d(TAG,"Para i="+i+" - Buscar "+diasSemana[i]+" en "+this.dias.toString());
			if (this.dias.indexOf(diasSemana[i])>-1)
			{
				//Log.d(TAG," Coincidencia "+i+" -- "+diasSemana[i]);
				retorno[i]=true;
			}
		}
		
		return retorno;
	}
}
