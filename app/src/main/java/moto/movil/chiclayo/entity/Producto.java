package moto.movil.chiclayo.entity;

public class Producto {
    private String idProducto;
    private String nombreProducto;
    private Double precioProducto;
    private String descripcionBreve;
    private String descripcionDetallada;
    
    public Producto(String idProducto, String nombreProducto, Double precioProducto, String descripcionBreve, String descripcionDetallada) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.precioProducto = precioProducto;
        this.descripcionBreve = descripcionBreve;
        this.descripcionDetallada = descripcionDetallada;
    }
    
    public String getIdProducto() {
        return idProducto;
    }
    
    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }
    
    public String getNombreProducto() {
        return nombreProducto;
    }
    
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
    
    public Double getPrecioProducto() {
        return precioProducto;
    }
    
    public void setPrecioProducto(Double precioProducto) {
        this.precioProducto = precioProducto;
    }
    
    public String getDescripcionBreve() {
        return descripcionBreve;
    }
    
    public void setDescripcionBreve(String descripcionBreve) {
        this.descripcionBreve = descripcionBreve;
    }
    
    public String getDescripcionDetallada() {
        return descripcionDetallada;
    }
    
    public void setDescripcionDetallada(String descripcionDetallada) {
        this.descripcionDetallada = descripcionDetallada;
    }
}
