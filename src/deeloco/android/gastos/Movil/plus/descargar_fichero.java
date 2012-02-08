/*(C) Copyright

    This file is part of widget Meteoclimatic.
    
    Meteoclimatic is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Meteoclimatic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Meteoclimatic.  If not, see <http://www.gnu.org/licenses/>.
    
    Autor: Antonio Cristóbal Álvarez Abellán -> acabellan@gmail.com
    
    */

package deeloco.android.gastos.Movil.plus;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import android.widget.Toast;


public class descargar_fichero {
	
	private Context contexto;
	private int retorno;
	
	private String tag="descargar_fichero";
	
	/**
	 * URL del fichero que se quiere descargar
	 */
	private String URL;
	
	/**
	 * nombre del fichero que se quiere descargar
	 */
	private String nombreFichero;
	
	/**
	 * path de la tarjeta SD donde se va a guardar el fichero descargado.
	 * Se guardara con el mismo nombre que tenga en la descarga
	 */
	private String pathSD;
	
	/**
	 * Get del atributo URL del fichero que se quiere descargar.
	 * @return
	 * String URL
	 */
	public String getURL() {
		return URL;
	}
	
	/**
	 * Set del atributo URL del fichero que se quiere descargar.
	 * @param 
	 * String URL
	 */
	public void setURL(String uRL) {
		URL = uRL;
	}
	
	/**
	 * Get del atributo pathSD, path donde se guardara el fichero descargado
	 * @return
	 * String pathSD
	 */
	public String getPathSD() {
		return pathSD;
	}
	
	/**
	 * Set del atributo pathSD, path donde se guardara el fichero descargado
	 * @param 
	 * String pathSD
	 * 
	 */
	public void setPathSD(String pathSD) {
		this.pathSD = pathSD;
	}

	/**
	 * Get del atributo nombreFichero 
	 * @return
	 * String nombreFichero
	 */
	public String getNombreFichero() {
		return nombreFichero;
	}

	/**
	 * Set del atributo nombreFichero
	 * @param
	 * String nombreFichero
	 * 
	 */
	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}

	/**
	 * Constructor
	 * @param uRL
	 * @param nombreFichero
	 * @param pathSD
	 */
	public descargar_fichero(Context contexto,String uRL, String nombreFichero, String pathSD) {
		super();
		URL = uRL;
		this.nombreFichero = nombreFichero;
		this.pathSD = pathSD;
		this.contexto=contexto;
		
		String path=this.pathSD+this.nombreFichero;
		//Comprobamos si el fichero esta creado. Si es que no, se crea.
    	File f=new File(path);
    	if (!f.exists())
    	{
    		//El fichero no existe, hay que crearlo
    		try{
    			boolean dir = new File(this.pathSD).mkdir(); //Creamos el directorio
    		}
    		catch (Exception e){ //Error al crear el directorio o el fichero
    			Log.e(tag,"Error al crear el directorio: " + e.getMessage());
    		}
    	}
		
		
		
	}
	
	/**
	 * Realiza la descarga del fichero en la URL especificada y la guarda en pathSD
	 * @return
	 * int: 0 - si la descarga se ha realizado correctamente
	 * 		1 - Error en la descarga del fichero
	 * 		2 - No hay conexión a Internet
	 */
	public int download()
	{
		//boolean retorno=true;
		retorno=1;
		
		
		Thread t=new Thread() 
			{
			public void run() 
				{
            	 
					if (HaveNetworkConnection())
					{
						try
						{
							URL url = new URL(URL+nombreFichero);
							File file = new File(pathSD+nombreFichero);
							long startTime = System.currentTimeMillis();
							
							// Abrir una conexión a la URL. 
				            URLConnection ucon = url.openConnection();
				            ucon.setConnectTimeout(3000);
				            ucon.setReadTimeout(3000);
				            //
				             // Define InputStreams para leer desde el URLConnection.
				             //
				            InputStream is = ucon.getInputStream();
				            BufferedInputStream bis = new BufferedInputStream(is);
				            
				            //
				             // Leer bytes del buffer hasta que no haya más que leer (-1)
				             //
				            
				            ByteArrayBuffer baf = new ByteArrayBuffer(50);
				            int current = 0;
				            while ((current = bis.read()) != -1) 
				            {
				            	baf.append((byte) current);
				            }
				            
				            // Convert the Bytes read to a String. 
				            FileOutputStream fos = new FileOutputStream(file);
				            fos.write(baf.toByteArray());
				            fos.close();
				            retorno=0;
				            Log.d(tag, "Descarga realizada en "+ ((System.currentTimeMillis() - startTime) / 1000)+ " sec, de"+url);
				
						}
						catch (SocketTimeoutException e)
						{
							retorno=1;
							Log.d(tag, "Error TimeOut al descargar el fichero "+nombreFichero+" : "+e.getMessage());
						}
						catch (IOException e)
						{
							//Error en la descarga del fichero
							retorno=1;
							Log.d(tag, "Error al descargar el fichero "+nombreFichero+" : "+e.getMessage());
						}
					}
					else
					{
						//Error en la descarga porque no hay conexión a Internet
						Log.d(tag, "Sin Conexión");
						retorno=2;
					}
            	                  
				}
			}; 
       t.start(); 
		
		
		return retorno;
		
		/*
		if (HaveNetworkConnection())
		{
			try
			{
				URL url = new URL(this.URL+this.nombreFichero);
				File file = new File(this.pathSD+this.nombreFichero);
				long startTime = System.currentTimeMillis();
				
				// Abrir una conexión a la URL. 
	            URLConnection ucon = url.openConnection();
	            ucon.setConnectTimeout(3000);
	            ucon.setReadTimeout(3000);
	            //
	             // Define InputStreams para leer desde el URLConnection.
	             //
	            InputStream is = ucon.getInputStream();
	            BufferedInputStream bis = new BufferedInputStream(is);
	            
	            //
	             // Leer bytes del buffer hasta que no haya más que leer (-1)
	             //
	            
	            ByteArrayBuffer baf = new ByteArrayBuffer(50);
	            int current = 0;
	            while ((current = bis.read()) != -1) 
	            {
	            	baf.append((byte) current);
	            }
	            
	            // Convert the Bytes read to a String. 
	            FileOutputStream fos = new FileOutputStream(file);
	            fos.write(baf.toByteArray());
	            fos.close();
	            retorno=0;
	            Log.d(tag, "Descarga realizada en "+ ((System.currentTimeMillis() - startTime) / 1000)+ " sec, de"+url);
	
			}
			catch (SocketTimeoutException e)
			{
				retorno=1;
				Log.d(tag, "Error TimeOut al descargar el fichero "+this.nombreFichero+" : "+e.getMessage());
			}
			catch (IOException e)
			{
				//Error en la descarga del fichero
				retorno=1;
				Log.d(tag, "Error al descargar el fichero "+this.nombreFichero+" : "+e.getMessage());
			}
			return retorno;
		}
		else
		{
			//Error en la descarga porque no hay conexión a Internet
			Log.d(tag, "Sin Conexión");
			retorno=2;
		}
		return retorno;
		*/
	}
	
	/**
	 * Retorna si hay o no conexión a Internet, sea wifi o móvil
	 * @return
	 * Boolean: true si hay conexión a Internet, false si no hay.
	 */
	private boolean HaveNetworkConnection()
	{
	    boolean HaveConnectedWifi = false;
	    boolean HaveConnectedMobile = false;
	    
	    
	    //contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
	    
	    //ConnectivityManager cm = meteoclimatic.getAppConnectivityManager();
	    ConnectivityManager cm =(ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE); 
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo)
	    {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	            {
	                HaveConnectedWifi = true;
	                Log.d(tag, "Conexión Wifi Activa");
	                
	            }
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	            {
	                HaveConnectedMobile = true;
	                Log.d(tag, "Conexión Mobile Activa");
	            }
	    }
	    if (!(HaveConnectedWifi || HaveConnectedMobile))
	    {
	    	Log.d(tag, "Sin Conexión Activa");
	    }
	    return HaveConnectedWifi || HaveConnectedMobile;
	}
	
	
	/**
	 * Comprueba si existe una nueva versión del fichero en el servidor. 
	 * Para ello se basa en las fechas del fichero en la máquina local y en la fecha del fichero en el servidor 
	 * @return
	 */
	public boolean nuevaVersionServidor()
	{
		boolean retorno=false;	
		
		if (HaveNetworkConnection())
		{
		//Comprobar si existen tarifas nuevas en el servidor
		//Ver si se puede meter dentro de la clase descargar_fichero.
		
		//File file = new File("/sdcard/gastosmovil/datosTarifasPre.xml");
		File file = new File(this.pathSD+this.nombreFichero);
		Date lastModDate = new Date(file.lastModified());
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy%20HH:mm:00");
		String fecha_fichero_local=sdf.format(lastModDate);
		HttpURLConnection urlConnection;
		try 
		   	{
				Log.d(tag,this.URL+"controlVer.php?fecha="+fecha_fichero_local+"&nombre="+this.nombreFichero);
			   	URL url = new URL(this.URL+"controlVer.php?fecha="+fecha_fichero_local+"&nombre="+this.nombreFichero);
			   	//URL url = new URL("http://gastosmovil.simahuelva.es/deeloco/tarifas/controlVer.php?fecha=10-10-2011%2018:00:00&nombre=datosTarifasPre.xml");
			   	urlConnection= (HttpURLConnection) url.openConnection();
			   	InputStream in = new BufferedInputStream(urlConnection.getInputStream());
		     	byte[] buffer = new byte[1];
		     	in.read(buffer);
		     	String respuesta = new String(buffer);
		     	int respuestaWS;
		     	respuestaWS=Integer.parseInt(respuesta);	     	
		     	urlConnection.disconnect();
		     	
		     	Log.d(tag,"Respuesta WS:"+respuesta);
		     	switch (respuestaWS) 
		     	{
		     		case 0:
		     			//No hay actualizaciones
		     			Toast.makeText(this.contexto,"No hay nuevas tarifas.",Toast.LENGTH_LONG).show();
		     			retorno=false;
		     			break;
		     		case 1:
		     			//Hay actualizaciones
		     			retorno=true;
		     			break;
		     		case 2:
		     			Toast.makeText(this.contexto,"No se ha podido descargar el fichero. Intentelo más tarde.",Toast.LENGTH_LONG).show();
		     			retorno=false;
		     		default:
		     			break;
				}
		     	
		   	}
		   	catch (MalformedURLException m)
		   	{
			   Log.e("PreferenciasTarifas.java",m.getMessage());
		   	}
		   	catch (IOException e)
		   	{
			   Log.e("PreferenciasTarifas.java",e.getMessage());
		   	}
		   	catch (Exception e)
		   	{
		   		Log.e("PreferenciasTarifas.java",e.getMessage());
		   	}
		}
		else
		{
			Toast.makeText(this.contexto,"Sin Conexion a Internet",Toast.LENGTH_LONG).show();
		}
		return retorno;
	}
	
	
	/**
	 * Devulve si existe un fichero o no
	 * @param nombreFichero: Nombre del fichero
	 * @param pathSD: Path de la SD donde está el fichero
	 * @return
	 * Boolean: true si existe y false si no existe
	 */
	public boolean existeFichero(String nombreFichero, String pathSD)
	{
		
		String path=pathSD+nombreFichero;
    	File f=new File(path);
    	return f.exists();
   	}
	

}
