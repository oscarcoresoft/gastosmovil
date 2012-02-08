package deeloco.android.gastos.Movil.plus;

import android.content.Context;

import java.io.FileReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import deeloco.android.gastos.Movil.plus.R;
import android.util.Log;

public class TarifasPreDefinidas {
	
	private tarifas ts=new tarifas();
	private ValoresPreferencias vp;
	public TarifasPreDefinidas(Context contexto) {
		//super();
		//Cargamos en la clase tarifas todas las tarifas predefinidas, que tienen que estar en
		//sd/gastosmovil/datosTarifasPre.xml
		vp=new ValoresPreferencias(contexto);
		//Comprobamos si el fichero existe.
		try
        {
        	//Comprobamos si el fichero esta creado. Si es que no, se crea.
    		//Parseamos el xml
	        SAXParserFactory spf = SAXParserFactory.newInstance();
	        SAXParser sp = spf.newSAXParser();
	        // Get the XMLReader of the SAXParser we created. 
	        XMLReader xr = sp.getXMLReader();
	        // Create a new ContentHandler and apply it to the XML-Reader
	        TarifasParserXML tarifasXML = new TarifasParserXML();
	        tarifasXML.setTarifas(ts);
	        xr.setContentHandler(tarifasXML);
	        xr.parse(new InputSource (new FileReader(contexto.getString(R.string.tarifasPre_path)+contexto.getString(R.string.tarifasPre_pre)+vp.getOperadora()+contexto.getString(R.string.tarifasPre_ext))));
	        // Parsing has finished. 
        }
        catch (Exception e)
        {
        	//Si el error se produce porque no existe el fichero xml, hay que crearlo.
        	//Tambien hay que crear el directorio
        	Log.e("TarifasPreDefinidas.java","ERROR:"+e.toString()+" ("+e.hashCode()+")");
        }
        
	}

	
	/**
	 * Devuelve el una lista de String con los nombres de todas las tarifas predefinidas
	 * @return
	 */
	public CharSequence[] nombresTarifas(){
		int numTarifas=ts.numTarifas();
		CharSequence[] nomTarifas= new CharSequence[numTarifas];
		nomTarifas=ts.getNombresTarifas();
		return nomTarifas;
	}
	
	/**
	 * Devuelve un objeto tarifa con los datos de la misma.
	 * @param indice
	 * @return
	 */
	public tarifa getTarifa(int indice){
		indice++;
		tarifa t=ts.getTarifa(indice);
		t.setIdentificador(0);
		return t; 
	}
	
}
