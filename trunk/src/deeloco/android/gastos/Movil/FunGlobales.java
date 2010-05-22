package deeloco.android.gastos.Movil;


public class FunGlobales {
	
	
	
    //--- Redondear un número con x decimales
    public static double redondear(double numero, int decimales ) {
        return Math.round(numero*Math.pow(10,decimales))/Math.pow(10,decimales);
    }


}
