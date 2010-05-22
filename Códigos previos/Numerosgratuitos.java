
//Función que retorna el valor del campo txtNumeros de los ajustes de la aplicación.
private string getPreferenciasNumeros()
{
	String retorno=Preferenciasmanager.getDefaultSharePreferencias(getBaseContext()).getString("txtNumeros","");
	return retorno;
}

//Función que retorna si un número el gratuito o no
private boolean esGratuito(string numero)
{
	boolean retorno=false;
	string numeros=getPreferenciasNumeros();
	int posicion=numeros.indexOf(numero);
	if (posicion>-1) retorno=true;
	return retorno;
}
