package moto.movil.chiclayo.entity;

/**
 * By: El Bryant
 */

public class CarritoSubproducto {
    private String idSubproducto;
    private String nombre;
    private int cantidad;
    private double precio;
    
    public CarritoSubproducto(String idSubproducto, String nombre, int cantidad, double precio) {
        this.idSubproducto = idSubproducto;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
    }
    
    public String getIdSubproducto() {
        return idSubproducto;
    }
    
    public void setIdSubproducto(String idSubproducto) {
        this.idSubproducto = idSubproducto;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    public double getPrecio() {
        return precio;
    }
    
    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
