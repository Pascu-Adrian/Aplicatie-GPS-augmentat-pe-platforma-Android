/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gps.augmentat.rutare.object;


public class Ruta<T> {
    
    private T _ultimulPas;
    private Ruta<T> _ultimiiPasi;
    private double CostTotal;

    private Ruta(T _ultimulPas, Ruta<T> _ultimiiPasi, double CostTotal) {
        this._ultimulPas = _ultimulPas;
        this._ultimiiPasi = _ultimiiPasi;
        this.CostTotal = CostTotal;
    }
    public Ruta(T nodStart){
        this(nodStart,null,0);
    }
    public Ruta<T> AdaugaPas(T pas,double cost)
    {
        return new Ruta<T>(pas,this,CostTotal+cost);
    }
    
    public T getUltimulPas()
    {
        return _ultimulPas;
    }
    public Ruta<T> UltimiiPasi()
    {
        return _ultimiiPasi;
    }
    
    public double GetCostTotal()
    {
        return CostTotal;
    }
}
