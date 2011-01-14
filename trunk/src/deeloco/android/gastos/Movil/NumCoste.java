/*(C) Copyright

    This file is part of GastosMovil.
    
    GastosMovil is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    GastosMovil is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with GastosMovil.  If not, see <http://www.gnu.org/licenses/>.
    
    Autor: Antonio Cristóbal Álvarez Abellán -> acabellan@gmail.com
    
    */

package deeloco.android.gastos.Movil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class NumCoste
{

    private ArrayList<String> numSC= new ArrayList <String>();
    private ArrayList<String> numEsp1= new ArrayList <String>();
    private ArrayList<String> numEsp2= new ArrayList <String>();
    String path="/sdcard/gastosmovil/num_sin_coste.txt";
    String pathEsp1="/sdcard/gastosmovil/num_especial1.txt";
    String pathEsp2="/sdcard/gastosmovil/num_especial2.txt";
    private final int NUMSC=1;
    private final int NUMESP1=2;
    private final int NUMESP2=3;

    //---Constructor de la clase---
    public NumCoste() 
    {
    	loadNumSC();
    	loadNumEsp1();
    	loadNumEsp2();
    }
    
    //---Carga los numeros del fichero de la tarjeta SD al ArrayList
   

    //---Insertar un numero---
    //lista= lista en la que hay que añadir el número 1.- SC, 2.- Esp1, 3.- Esp2
    public boolean add(String numero, int lista)
    {
    	switch (lista)
    	{
    	case NUMSC:
    		if (isNumCosteCero(numero)||isNumEsp1(numero)||isNumEsp2(numero))
    		{
    			return false;
    		}
    		else
    		{
	        	numSC.add(numero); //añadimos el numero a la lista
	        	return saveNumSD();
    		}
    	case NUMESP1:
    		if (isNumCosteCero(numero)||isNumEsp1(numero)||isNumEsp2(numero))
    		{
    			return false;
    		}
    		else
    		{
	        	numEsp1.add(numero); //añadimos el numero a la lista
	        	return saveNumEsp1();
    		}
    	case NUMESP2:
    		if (isNumCosteCero(numero)||isNumEsp1(numero)||isNumEsp2(numero))
    		{
    			return false;
    		}
    		else
    		{
	        	numEsp2.add(numero); //añadimos el numero a la lista
	        	return saveNumEsp2();
    		}
    	}
    	return false;
    	//numSC.add(numero); //añadimos el numero a la lista
    	//return saveNumSD();

    }
    
    //---Eliminar un numero---
    //lista= lista en la que hay que añadir el número 1.- SC, 2.- Esp1, 3.- Esp2
    public boolean delete(String numero, int lista)
    {
    	switch (lista)
    	{
    	case NUMSC:
        	if (isNumCosteCero(numero))
        	{
        		numSC.remove(numero);
        		return saveNumSD();
        	}
        	else
        	{
        		return false;
        	}
    	case NUMESP1:
        	if (isNumEsp1(numero))
        	{
        		numEsp1.remove(numero);
        		return saveNumEsp1();
        	}
        	else
        	{
        		return false;
        	}

    	case NUMESP2:
        	if (isNumEsp2(numero))
        	{
        		numEsp2.remove(numero);
        		return saveNumEsp2();
        	}
        	else
        	{
        		return false;
        	}
    	}
    	
    	return false;
    }
    
    //---Devuelve si un número esta en la lista---
    public boolean isNumCosteCero(String num)
    {
    	//System.out.println("***** isNumCosteCero("+num+"): "+numeros.indexOf(num));
    	boolean retorno=true;
    	if (numSC.indexOf(num)==-1) retorno=false;
    	return retorno;
    }
    
    
    public boolean isNumEsp1(String num)
    {
    	//System.out.println("***** isNumCosteCero("+num+"): "+numeros.indexOf(num));
    	boolean retorno=true;
    	if (numEsp1.indexOf(num)==-1) retorno=false;
    	return retorno;
    }
    
    
    public boolean isNumEsp2(String num)
    {
    	//System.out.println("***** isNumCosteCero("+num+"): "+numeros.indexOf(num));
    	boolean retorno=true;
    	if (numEsp2.indexOf(num)==-1) retorno=false;
    	return retorno;
    }
    
    //---Devuelve todos los números---
    public List<String> allNumeros()
    {
    	return numSC;
    }
    
    //---Guardar los números en el fichero
    public boolean saveNumSD()
    {
    	String sNumeros="";
    	//escribimos en el fichero
        for (int i=0;i<numSC.size();i++)
        {
        	sNumeros=numSC.get(i)+","+sNumeros;
        }
        System.out.println("***** Cadena de numeros "+sNumeros);
         
        //Escribir en la tarjeta SD
        
        FileWriter fWriter;
        boolean retorno=true;
        try{
             fWriter = new FileWriter(path);
             fWriter.write(sNumeros);
             fWriter.flush();
             fWriter.close();

         }catch(Exception e){

                  e.printStackTrace();
                  retorno=false;

         }
         return retorno;
    }
    
    public boolean saveNumEsp1()
    {
    	String sNumeros="";
    	//escribimos en el fichero
        for (int i=0;i<numEsp1.size();i++)
        {
        	sNumeros=numEsp1.get(i)+","+sNumeros;
        }
         
        //Escribir en la tarjeta SD
        
        FileWriter fWriter;
        boolean retorno=true;
        try{
             fWriter = new FileWriter(pathEsp1);
             fWriter.write(sNumeros);
             fWriter.flush();
             fWriter.close();

         }catch(Exception e){

                  e.printStackTrace();
                  retorno=false;

         }
         return retorno;
    }
    
    
    public boolean saveNumEsp2()
    {
    	String sNumeros="";
    	//escribimos en el fichero
        for (int i=0;i<numEsp2.size();i++)
        {
        	sNumeros=numEsp2.get(i)+","+sNumeros;
        }
         
        //Escribir en la tarjeta SD
        
        FileWriter fWriter;
        boolean retorno=true;
        try{
             fWriter = new FileWriter(pathEsp2);
             fWriter.write(sNumeros);
             fWriter.flush();
             fWriter.close();

         }catch(Exception e){

                  e.printStackTrace();
                  retorno=false;

         }
         return retorno;
    }
    
  //---Cargar los números sin coste
    public boolean loadNumSC()
    {
    	boolean retorno=true;
  	
    	//Leer los números sin coste del fichero de la tarjeta SD
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
    			e.printStackTrace();
    			return false;
    		}
    	}
    	
    	//Leemos el fichero
        FileReader fReader;
        String s="";
        try{
	        fReader=new FileReader(path);
	        BufferedReader br = new BufferedReader(fReader);  
	        s=br.readLine();
	        fReader.close();
        }
        catch(Exception e){ //Error al leer el fichero
        	e.printStackTrace();
        	return false;
        }
        if (s!=null)
        {
        	String data[] = s.split(",");
        	for (int i=0; i < data.length; i++) {
        		 numSC.add(data[i]);
        		}
        }  	
    	
    	return retorno;
    }

//---Cargar los números especiales 1
public boolean loadNumEsp1()
{
	boolean retorno=true;
	
	//Leer los números sin coste del fichero de la tarjeta SD
	File f=new File(pathEsp1);
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
			e.printStackTrace();
			return false;
		}
	}
	
	//Leemos el fichero
    FileReader fReader;
    String s="";
    try{
        fReader=new FileReader(pathEsp1);
        BufferedReader br = new BufferedReader(fReader);  
        s=br.readLine();
        fReader.close();
    }
    catch(Exception e){ //Error al leer el fichero
    	e.printStackTrace();
    	return false;
    }
    if (s!=null)
    {
    	String data[] = s.split(",");
    	for (int i=0; i < data.length; i++) {
    		 numEsp1.add(data[i]);
    		}
    }  	
	
	return retorno;
}


//---Cargar los números especiales 1
public boolean loadNumEsp2()
{
	boolean retorno=true;
	
	//Leer los números sin coste del fichero de la tarjeta SD
	File f=new File(pathEsp2);
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
			e.printStackTrace();
			return false;
		}
	}
	
	//Leemos el fichero
    FileReader fReader;
    String s="";
    try{
        fReader=new FileReader(pathEsp2);
        BufferedReader br = new BufferedReader(fReader);  
        s=br.readLine();
        fReader.close();
    }
    catch(Exception e){ //Error al leer el fichero
    	e.printStackTrace();
    	return false;
    }
    if (s!=null)
    {
    	String data[] = s.split(",");
    	for (int i=0; i < data.length; i++) {
    		 numEsp2.add(data[i]);
    		}
    }  	
	
	return retorno;
}



}


