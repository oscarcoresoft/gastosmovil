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
double iva=1.18;

public tarifas getTarifas(){
	return ts;
}
public void setTarifas(tarifas ts){
	this.ts=ts;
}

public void setIva(double iva){
	this.iva=iva;
}
/**
 * Evento que se lanza al inicio del documento XML
 */

@Override
public void startDocument() throws SAXException 
{
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
	//System.out.println("Numeros.."+ts.getTarifas().get(0).getNumeros());
}
 
/**
 * Evento que se lanza al iniciar un elemento del documento XML <elemento>
 */
@Override
public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
{ 
	if (localName.compareTo("tarifa")==0)
	{
		t=new tarifa(attributes.getValue(0));
	}

	if (localName.compareTo("franja")==0)
	{
		f=new Franja(attributes.getValue(0));
		//f.setIva(iva);
		
	}	
}

/**
 * Evento que se lanza al final de un elemento del documento XML </elemento>
 */
@Override
public void endElement(String uri, String localName, String qName) throws SAXException 
{
	
	if (localName.compareTo("tarifa")==0)
	{
		ts.addTarifa(t);
	}
	if (localName.compareTo("nombreTarifa")==0)
	{
		t.setNombre(this.datosElemento);
	}
	if (localName.compareTo("gastoMinimo")==0)
	{
		t.setMinimo(Double.parseDouble(this.datosElemento));
	}
	if (localName.compareTo("limiteLlamadas")==0)
	{
		t.setLimite(Integer.parseInt(this.datosElemento));
	}
	if (localName.compareTo("limiteLlamadasDia")==0)
	{
		t.setLimiteDia(Integer.parseInt(this.datosElemento));
	}
	if (localName.compareTo("color")==0)
	{
		t.setColor(this.datosElemento);
	}
	
	if (localName.compareTo("numeros")==0)
	{
		t.setNumeros(this.datosElemento);
	} 
	
	if (localName.compareTo("franja")==0)
	{
		t.addFranja(f);
	}
	if (localName.compareTo("nombre")==0)
	{
		f.setNombre(this.datosElemento);
	}
	if (localName.compareTo("horaInicio")==0)
	{
		f.setHoraInicio(this.datosElemento);
	}
	if (localName.compareTo("horaFinal")==0)
	{
		f.setHoraFinal(this.datosElemento);
	}
	if (localName.compareTo("dias")==0)
	{
		f.setDias(this.datosElemento);
	}
	if (localName.compareTo("coste")==0)
	{
		f.setCoste(this.datosElemento);
	}
	if (localName.compareTo("establecimiento")==0)
	{
		f.setEstablecimiento(this.datosElemento);
	}
	if (localName.compareTo("limite")==0)
	{
		f.setLimite(this.datosElemento);
	}
	if (localName.compareTo("costeFueraLimite")==0)
	{
		f.setCosteFueraLimite(this.datosElemento);
	}
	if (localName.compareTo("establecimientoFueraLimite")==0)
	{
		f.setEstablecimientoFueraLimite(this.datosElemento);
	}
	this.datosElemento="";
}

/**
 * Evento que se lanza con los caracteres que aparecen entre la etiquetas de apertura y cierre
 */
@Override
public void characters(char[] ch, int start, int length) throws SAXException 
{
	String datos=new String (ch,start,length);
	this.datosElemento=datos;
}
 
 
 

}
