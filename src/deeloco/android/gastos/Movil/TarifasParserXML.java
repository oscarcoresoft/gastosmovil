package deeloco.android.gastos.Movil;

import java.util.MissingResourceException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TarifasParserXML extends DefaultHandler {

String datosElemento; 
Franja f;
tarifa t;
tarifas ts;

public tarifas getTarifas(){
	return ts;
}
public void setTarifas(tarifas ts){
	this.ts=ts;
}
/**
 * Evento que se lanza al inicio del documento XML
 */

@Override
public void startDocument() throws SAXException 
{
	//System.out.println("... INICIO DOCUMENTO ...");
	if (ts==null)
	{
		throw new MissingResourceException("Falta inicializar el atributo ts", "TarifasParserXML", "ts");
	}
}
 
/**
 * Evento que se lanza al finalizar el documento XML
 */
@Override
public void endDocument() throws SAXException 
{
	//System.out.println("... FINAL DEL PARSER ...");
	//System.out.println("... NUMERO DE TARIFAS CARGADAS: "+ts.numTarifas());
	//tarifa tar=ts.getTarifa(1);
	//System.out.println("... NUMERO DE FRANJAS DE LA PRIMERA TARIFA: "+tar.numFranjas());
	//System.out.println("... COSTE DE UNA LLAMADA FICTICIA: "+ts.costeLlamada("600245467", 6, Time.valueOf("12:15:00"), 30));
	
}
 
/**
 * Evento que se lanza al iniciar un elemento del documento XML <elemento>
 */
@Override
public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
{

	if (localName.compareTo("tarifas")==0)
	{
		//System.out.println("... ts=new tarifas(); ...");
		//ts=new tarifas();
	} 
	if (localName.compareTo("tarifa")==0)
	{
		//System.out.println("... t=new tarifa("+attributes.getValue(0)+"); ...");
		t=new tarifa(attributes.getValue(0));
	}

	if (localName.compareTo("franja")==0)
	{
		//System.out.println("... f=new franja("+attributes.getValue(0)+") ...");
		f=new Franja(attributes.getValue(0));
	}	
	
	//System.out.println("... --> INICIO DEL ELEMENTO ..."+localName.toString());
}

/**
 * Evento que se lanza al final de un elemento del documento XML </elemento>
 */
@Override
public void endElement(String uri, String localName, String qName) throws SAXException 
{
	
	if (localName.compareTo("tarifa")==0)
	{
		//System.out.println("... ts.addTarifa(t); ...");
		ts.addTarifa(t);
	}
	if (localName.compareTo("nombreTarifa")==0)
	{
		//System.out.println("... t.setNombre("+this.datosElemento+") ...");
		t.setNombre(this.datosElemento);
	}
	if (localName.compareTo("gastoMinimo")==0)
	{
		//System.out.println("... t.setGastoMinimo("+this.datosElemento+") ...");
		t.setMinimo(Double.parseDouble(this.datosElemento));
	}
	if (localName.compareTo("color")==0)
	{
		//System.out.println("... t.setColor("+this.datosElemento+") ...");
		t.setColor(this.datosElemento);
	}
	
	if (localName.compareTo("numeros")==0)
	{
		//System.out.println("... t.setNumero("+this.datosElemento+") ...");
		t.setNumeros(this.datosElemento);
	} 
	
	if (localName.compareTo("franja")==0)
	{
		//System.out.println("... t.addFranja(f); ...");
		t.addFranja(f);
	}
	if (localName.compareTo("nombre")==0)
	{
		//System.out.println("... f.setNombre("+this.datosElemento+"); ...");
		f.setNombre(this.datosElemento);
	}
	if (localName.compareTo("horaInicio")==0)
	{
		//System.out.println("... f.setHoraInicio("+this.datosElemento+"); ...");
		f.setHoraInicio(this.datosElemento);
	}
	if (localName.compareTo("horaFinal")==0)
	{
		//System.out.println("... f.setHoraFinal("+this.datosElemento+"); ...");
		f.setHoraFinal(this.datosElemento);
	}
	if (localName.compareTo("dias")==0)
	{
		//System.out.println("... f.setDias("+this.datosElemento+"); ...");
		f.setDias(this.datosElemento);
	}
	if (localName.compareTo("coste")==0)
	{
		//System.out.println("... f.setCoste("+this.datosElemento+"); ...");
		f.setCoste(this.datosElemento);
	}
	if (localName.compareTo("establecimiento")==0)
	{
		//System.out.println("... f.setEstablecimiento("+this.datosElemento+"); ...");
		f.setEstablecimiento(this.datosElemento);
	}
	if (localName.compareTo("limite")==0)
	{
		//System.out.println("... f.setLimite("+this.datosElemento+"); ...");
		f.setLimite(this.datosElemento);
	}
	if (localName.compareTo("costeFueraLimite")==0)
	{
		//System.out.println("... f.setCosteFueraLimite("+this.datosElemento+"); ...");
		f.setCosteFueraLimite(this.datosElemento);
	}
	//System.out.println("... --> FINAL DEL ELEMENTO ..."+localName.toString());
}

/**
 * Evento que se lanza con los caracteres que aparecen entre la etiquetas de apertura y cierre
 */
@Override
public void characters(char[] ch, int start, int length) throws SAXException 
{
	String datos=new String (ch,start,length);
	this.datosElemento=datos;
	//System.out.println("... --> --> UN CARACTER ...:"+datos);
}
 
 
 

}
