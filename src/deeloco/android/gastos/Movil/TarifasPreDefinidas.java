package deeloco.android.gastos.Movil;


import android.util.Log;

public class TarifasPreDefinidas {
	
	//
	//2da Actualización: Ardilla 12, Ardilla 6, Delfin 79, Delfin 32, Delfin 20, PepePhone - Tarifa 4 cent, PepePhone - Tarifa 7 cent
	//Movistar - Planazo Global, Movistar - Contrato Único, Movistar - Contrato Tiempo Libre, Movistar - Planazo Global, Movistar - Planazo Global L, Movistar - Planazo Global XL,Movistar - Planazo Global XXL
	
	//{"Nombre tarifa","numero de franjas","consumo minimo","limite de llamadas mensuales","limite de llamadas diarias","Color","numeros asociados","Defecto",
	//	"Nombre franja","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","coste","establecimiento","cuenta?","coste pasado limites","establecimiento pasado limites"},
	private int numTarifas=62;
	private String[][] tarifasPreDefinidas={
			{"El Androide Libre - PepePhone","1","0","0","0","Rojo","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","4","15","No","0","0"},
			{"HTCMania - PepePhone","1","7","0","0","Rojo","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","3,9","15","No","0","0"},
			{"Movistar - Contrato Único","1","9","0","0","Azul","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","18","15","No","0","0"},
			{"Movistar - Contrato Tiempo Libre ","3","9","0","0","Azul","","Si",
				"Mañana","07:00:00","17:00:00","[Lun,Mar,Mie,Jue,Vie]","30","15","No","0","0",
				"Tarde","17:00:00","07:00:00","[Lun,Mar,Mie,Jue,Vie]","8","15","No","0","0",
				"Fin de Semana","00:00:00","00:00:00","[Sab,Dom]","8","15","No","0","0"},
			{"Movistar - Planazo + Sin Horarios ","1","0","0","20","Azul","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","18","15"},
			{"Movistar - Planazo","1","19.9","500","0","Azul","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","18","15"},
			{"Movistar - Planazo Tiempo Libre ","3","19.9","300","0","Azul","","Si",
				"Mañana","07:00:00","17:00:00","[Lun,Mar,Mie,Jue,Vie]","18","15","No","0","0",
				"Tarde","17:00:00","07:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","18","15",
				"Fin de Semana","00:00:00","00:00:00","[Sab,Dom]","0","0","Si","18","15"},
			{"Movistar - Planazo Global ","1","59.9","500","0","Azul","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","18","15"},
			{"Movistar - Planazo Global L","1","79.9","800","0","Azul","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","18","15"},
			{"Movistar - Planazo Global XL","1","99.9","1200","0","Azul","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","18","15"},
			{"Movistar - Planazo Global XXL","1","159.9","2000","0","Azul","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","18","15"},
			{"Orange - Ardilla 12","1","12","0","0","Naranja","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","17","15","No","0","0"},
			{"Orange- Ardilla 9","3","9","300","0","Naranja","","Si",
			    "Franja 1","08:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie]","30","15","No","0","0",
			    "Franja 2","18:00:00","08:00:00","[Lun,Mar,Mie,Jue,Vie]","7","15","No","0","0",
			    "Fin de Semana","00:00:00","00:00:00","[Sab,Dom]","7","15","No","0","0"},
			{"Orange - Ardilla 6","3","6","0","0","Naranja","","Si",
				"Mañana","08:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie]","24","15","No","0","0",
				"Tarde","18:00:00","08:00:00","[Lun,Mar,Mie,Jue,Vie]","6","15","No","0","0",
				"Fin de Semana","00:00:00","00:00:00","[Sab,Dom]","6","15","No","0","0",},	
			{"Orange - Delfín 79","1","0","1200","0","Naranja","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","18","15"},
			{"Orange - Delfín 59-Móviles","1","0","500","0","Naranja","6","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","18","15"},	
			{"Orange - Delfín 59-Fijos","1","0","500","0","Naranja","9[1-8]","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","18","15"},
			{"Orange - Delfín 42-Móviles","1","0","300","0","Naranja","6","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","18","15"},	
			{"Orange - Delfín 42-Fijos","1","0","300","0","Naranja","9[1-8]","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","18","15"},
			{"Orange - Delfín 32","2","0","700","0","Naranja","","Si",
				"Mañana","08:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","18","15","No","0","0",
				"Tarde","18:00:00","8:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","18","15"},
			{"Orange - Delfín 20","2","0","300","0","Naranja","","Si",
				"Mañana","08:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","18","15","No","0","0",
				"Tarde","18:00:00","8:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","18","15"},
			{"Orange - León 49-Móviles","1","49","500","0","Naranja","6","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","18","15"},
			{"Orange - León 49-Fijos","1","49","500","0","Naranja","9[1-8]","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","18","15"},
			{"Orange - León 30-Móviles","1","30","300","0","Naranja","6","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","18","15"},	
			{"Orange - León 30-Fijos","1","30","300","0","Naranja","9[1-8]","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","18","15"},
			{"Orange - León 25","2","25","400","0","Naranja","","Si",
				"Mañana","09:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","18","15",
				"Tarde","18:00:00","09:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","18","15","No","0","0"},
			{"Orange- Panda 26","3","26","1200","0","Naranja","","Si",
			    "Mañana","08:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie]","18","15","No","0","0",
			    "Tarde","18:00:00","08:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","18","15",
			    "Fin de Semana","00:00:00","00:00:00","[Sab,Dom]","0","0","Si","18","15"},
			{"Orange- Panda 22","3","22","700","0","Naranja","","Si",
			    "Mañana","08:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie]","18","15","No","0","0",
			    "Tarde","18:00:00","08:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","18","15",
			    "Fin de Semana","00:00:00","00:00:00","[Sab,Dom]","0","0","Si","18","15"},
			{"Orange- Panda 20","3","20","300","0","Naranja","","Si",
			    "Mañana","08:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie]","18","15","No","0","0",
			    "Tarde","18:00:00","08:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","18","15",
			    "Fin de Semana","00:00:00","00:00:00","[Sab,Dom]","0","0","Si","18","15"},
			{"Orange- Panda 15","3","15","300","0","Naranja","","Si",
			    "Mañana","08:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie]","18","15","No","0","0",
			    "Tarde","18:00:00","08:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","18","15",
			    "Fin de Semana","00:00:00","00:00:00","[Sab,Dom]","0","0","Si","18","15"},
		    {"PepePhone - NavegaYHabla","1","0","0","0","Rojo","","Si",
		        "24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","9","0","No","0","0"},
			{"PepePhone - Tarifa 4 cent","1","0","0","0","Rojo","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","4","15","No","0","0"},	
			{"PepePhone - Tarifa 5 cent","1","0","0","0","Rojo","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","5","15","No","0","0"},
			{"PepePhone - Tarifa 6 cent","1","0","0","0","Rojo","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","6","15","No","0","0"},
			{"PepePhone - Tarifa 7 cent","1","0","0","0","Rojo","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","7","15","No","0","0"},
			{"Simyo - Tarifa 5 cent","1","6.99","0","0","Naranja","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","5","15","No","0","0"},
			{"Simyo - Tarifa 8 cent","1","0","0","0","Naranja","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","8","15","No","0","0"},
			{"Simyo - Numero Simyo","1","0","0","0","Verde","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","No","0","0"},
			//{"Nombre tarifa","numero de franjas","consumo minimo","limite de llamadas mensuales","limite de llamadas diarias","Color","numeros asociados","Defecto",
			//	"Nombre franja","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","coste","establecimiento","cuenta?","coste pasado limites","establecimiento pasado limites"},
			{"Vodafone - XL-Tarde y Fin Sem.","2","0","1000","0","Rojo","","Si",
				    "Tarde","18:00:00","08:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","19.9","15",
				    "Fin Sem.","00:00:00","00:00:00","[Sab,Dom]","0","0","Si","19.9","15"},
		    {"Vodafone - XL-Mañana","1","0","1000","0","Rojo","","Si",
				    "Mañana","08:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","19.9","15"},
		    {"Vodafone - L-Tarde y Fin Sem.","2","0","1000","0","Rojo","","Si",
				    "Tarde","18:00:00","08:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","19.9","15",
				    "Fin Sem.","00:00:00","00:00:00","[Sab,Dom]","0","0","Si","19.9","15"},
		    {"Vodafone - L-Mañana","1","0","350","0","Rojo","","Si",
				    "Mañana","08:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","19.9","15"},
		    {"Vodafone - L+ -Tarde y Fin Sem.","2","0","1000","0","Rojo","","Si",
				    "Tarde","18:00:00","08:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","19.9","15",
				    "Fin Sem.","00:00:00","00:00:00","[Sab,Dom]","0","0","Si","19.9","15"},
		    {"Vodafone - L+ -Mañana","1","0","700","0","Rojo","","Si",
				    "Mañana","08:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","19.9","15"},
		    {"Vodafone - M","3","0","1000","0","Rojo","","Si",
			    	"Mañana","08:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie]","19.9","15","No","0","0",	
			    	"Tarde","18:00:00","08:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","19.9","15",
				    "Fin Sem.","00:00:00","00:00:00","[Sab,Dom]","0","0","Si","19.9","15"},
		    {"Vodafone - M+ -Tarde y Fin Sem.","2","0","1000","0","Rojo","","Si",
			    	"Tarde","18:00:00","08:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","19.9","15",
				    "Fin Sem.","00:00:00","00:00:00","[Sab,Dom]","0","0","Si","19.9","15"},
		    {"Vodafone - M+ -Mañana","1","0","150","0","Rojo","","Si",
				    "Mañana","08:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","19.9","15"},
		    {"Vodafone - S","3","0","350","0","Rojo","","Si",
			    	"Mañana","08:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie]","19.9","15","No","0","0",	
			    	"Tarde","18:00:00","08:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","19.9","15",
				    "Fin Sem.","00:00:00","00:00:00","[Sab,Dom]","0","0","Si","19.9","15"},
		    {"Vodafone - S+ -Tarde y Fin Sem.","2","0","350","0","Rojo","","Si",
			    	"Tarde","18:00:00","08:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","19.9","15",
				    "Fin Sem.","00:00:00","00:00:00","[Sab,Dom]","0","0","Si","19.9","15"},
		    {"Vodafone - S+ -Mañana","1","0","150","0","Rojo","","Si",
				    "Mañana","08:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","19.9","15"},
		    {"Vodafone - XS","3","0","350","0","Rojo","","Si",
			    	"Mañana","08:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie]","19.9","15","No","0","0",	
			    	"Tarde","18:00:00","08:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","19.9","15",
				    "Fin Sem.","00:00:00","00:00:00","[Sab,Dom]","0","0","Si","19.9","15"},
		    {"Vodafone - XS+ -Tarde y Fin Sem.","2","0","350","0","Rojo","","Si",
			    	"Tarde","18:00:00","08:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","19.9","15",
				    "Fin Sem.","00:00:00","00:00:00","[Sab,Dom]","0","0","Si","19.9","15"},
		    {"Vodafone - XS+ -Mañana","1","0","150","0","Rojo","","Si",
				    "Mañana","08:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","19.9","15"},
			{"Vodafone - Diminuto 8","1","6","0","0","Rojo","","Si",
			    "24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","8","15","No","0","0"},
			{"Vodafone - Super T. Plana","1","99.9","1000","0","Rojo","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","19","15"},
			{"Vodafone - Super T. P. Mini","1","59.9","500","0","Rojo","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","Si","19","15"},
			{"Vodafone - T. P. a todos","3","29.9","1000","0","Rojo","","Si",
				"Mañana","08:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie]","19","15","No","0","0",
				"Tarde","18:00:00","8:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","19","15",
				"Fin de Semana","00:00:00","00:00:00","[Sab,Dom]","0","0","No","0","0"},
			{"Vodafone - T. P. Mini a todos","3","19.9","350","0","Rojo","","Si",
				"Mañana","08:00:00","18:00:00","[Lun,Mar,Mie,Jue,Vie]","19","15","No","0","0",
				"Tarde","18:00:00","8:00:00","[Lun,Mar,Mie,Jue,Vie]","0","0","Si","19","15",
				"Fin de Semana","00:00:00","00:00:00","[Sab,Dom]","0","0","No","0","0"},
			{"Yoigo - La del cero a yoigo","1","6","0","60","Verde","","Si",
					"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","15","Si","12","15"},
			{"Yoigo - La del cero al resto ","1","6","0","0","Verde","","Si",
					"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","12","15","No","0","0"},
			{"Yoigo - La del seis","1","25","0","0","Verde","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","6","15","No","0","0"},
			{"Yoigo - La del ocho","1","6","0","0","Verde","","Si",
				"24 Horas","00:00:00","00:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","8","15","No","0","0"}	
	};

	public TarifasPreDefinidas() {
		super();
	}

	
	/**
	 * Devuelve el una lista de String con los nombres de todas las tarifas predefinidas
	 * @return
	 */
	public CharSequence[] nombresTarifas(){
		CharSequence[] nomTarifas= new CharSequence[numTarifas];
		//Recorrer la matriz y devuelve el nombre de las tarifas predefinidas
        for (int f=0;f<this.numTarifas;f++)
        {
        	nomTarifas[f]=this.tarifasPreDefinidas[f][0];
        	//System.out.println(this.tarifas.get(i).getNombre());
        }
		return nomTarifas;
	}
	
	/**
	 * Devuelve un objeto tarifa con los datos de la misma.
	 * @param indice
	 * @return
	 */
	public tarifa getTarifa(int indice){
		//Log.d("TarifaPreDefinida","Inicio de la creación de tarifa predefinida="+this.tarifasPreDefinidas[indice][0]);
		tarifa tarifaRetorno=new tarifa(0); //0= tarifa nueva
		//Cargamos los valores de la tarifa
		tarifaRetorno.setNombre(this.tarifasPreDefinidas[indice][0]); //Nombre
		tarifaRetorno.setMinimo(Double.parseDouble(this.tarifasPreDefinidas[indice][2]));//Gasto minimo
		tarifaRetorno.setLimite(Integer.parseInt(this.tarifasPreDefinidas[indice][3])); //Limite mes
		tarifaRetorno.setLimiteDia(Integer.parseInt(this.tarifasPreDefinidas[indice][4])); //Limite día
		tarifaRetorno.setColor(this.tarifasPreDefinidas[indice][5]);//Color
		tarifaRetorno.setNumeros(this.tarifasPreDefinidas[indice][6]);//Numeros
		tarifaRetorno.setDefecto(this.tarifasPreDefinidas[indice][7]);
		//Cargamos los valores de las franjas
		int numFranjas=Integer.parseInt(this.tarifasPreDefinidas[indice][1]);
		//numFranjas++;
		int indiceDatosFranja=0;
		for (int f=0;f<numFranjas;f++)
        {
        	indiceDatosFranja=8+(f*9);
        	Franja nuevaFranja=new Franja(f+1);
        	Log.d("TarifaPreDefinida","indiceDatosFranja="+indiceDatosFranja);
        	nuevaFranja.setNombre(this.tarifasPreDefinidas[indice][indiceDatosFranja]); //Nombre
        	nuevaFranja.setHoraInicio(this.tarifasPreDefinidas[indice][indiceDatosFranja+1]); // Hora Inicio
        	nuevaFranja.setHoraFinal(this.tarifasPreDefinidas[indice][indiceDatosFranja+2]); // Hora Final
        	nuevaFranja.setDias(this.tarifasPreDefinidas[indice][indiceDatosFranja+3]); //Dias
        	nuevaFranja.setCoste(this.tarifasPreDefinidas[indice][indiceDatosFranja+4]); //Coste
        	nuevaFranja.setEstablecimiento(this.tarifasPreDefinidas[indice][indiceDatosFranja+5]); //Establecimiento
        	nuevaFranja.setLimite(this.tarifasPreDefinidas[indice][indiceDatosFranja+6]); //Limite
        	nuevaFranja.setCosteFueraLimite(this.tarifasPreDefinidas[indice][indiceDatosFranja+7]); //Coste fuera limite
        	nuevaFranja.setEstablecimientoFueraLimite(this.tarifasPreDefinidas[indice][indiceDatosFranja+8]); //Establecimiento fuera limite
        	tarifaRetorno.addFranja(nuevaFranja);
        }
		//Log.d("TarifaPreDefinida","Se ha creado una tarifa predefinida="+tarifaRetorno.getNombre());
		return tarifaRetorno;
	}
	
}
