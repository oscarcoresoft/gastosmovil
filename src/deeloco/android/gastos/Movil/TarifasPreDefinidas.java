package deeloco.android.gastos.Movil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TarifasPreDefinidas {
	
	//{"Nombre Tarifa","Numero de franjas","Gasto Mínimo","Limite","Color","Numeros",
	//"Nombre Franja","hora Inicio","Hora Final","Dias","Coste","Establecimiento","Cantabilizar",
	//"CostePasado","EstablecimientoPasado"}
	private int numTarifas=5;
	private String[][] tarifasPreDefinidas={
			{"Simyo - Tarifa 5 cent.","1","6.99","Naranja","","24 Horas","00:00:00","23:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","5","15","No","0","0"},
			{"Simyo - Tarifa 8 cent.","1","0","Naranja","","24 Horas","00:00:00","23:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","8","15","No","0","0"},
			{"Simyo - Numero Simyo.","1","0","Verde","","24 Horas","00:00:00","23:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","0","0","No","0","0"},
			{"Yoigo - La del seis.","1","25","Verde","","24 Horas","00:00:00","23:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","6","15","No","0","0"},
			{"Yoigo - La del ocho.","1","6","Verde","","24 Horas","00:00:00","23:00:00","[Lun,Mar,Mie,Jue,Vie,Sab,Dom]","8","15","No","0","0"}
	};

	public TarifasPreDefinidas() {
		super();
	}

	
	/**
	 * Devuelve el una lista de String con los nombres de todas las tarifas predefinidas
	 * @return
	 */
	public List <String> nombresTarifas(){
		ArrayList <String> nomTarifas=new ArrayList <String>();
		//Recorrer la matriz y devuelve el nombre de las tarifas predefinidas
        for (int f=0;f<this.numTarifas;f++)
        {
        	nomTarifas.add(this.tarifasPreDefinidas[f][0]);
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
		tarifa tarifaRetorno=new tarifa(0); //0= tarifa nueva
		//Cargamos los valores de la tarifa
		tarifaRetorno.setMinimo(Double.parseDouble(this.tarifasPreDefinidas[indice][2]));
		tarifaRetorno.setLimite(Integer.parseInt(this.tarifasPreDefinidas[indice][3]));
		tarifaRetorno.setColor(this.tarifasPreDefinidas[indice][4]);
		tarifaRetorno.setNumeros("");
		//Cargamos los valores de las franjas
		int numFranjas=Integer.parseInt(this.tarifasPreDefinidas[indice][1]);
		//numFranjas++;
		int indiceDatosFranja=6;
		for (int f=0;f<numFranjas;f++)
        {
        	indiceDatosFranja=indiceDatosFranja+(f*9);
        	Franja nuevaFranja=new Franja(f+1);
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
		return tarifaRetorno;
	}
	
}
