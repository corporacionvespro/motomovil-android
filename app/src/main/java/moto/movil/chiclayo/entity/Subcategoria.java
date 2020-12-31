package moto.movil.chiclayo.entity;

/**
 * By: El Bryant
 */

public class Subcategoria {
    private String idSubcategoria;
    private String nombre;
    private String imagen;
    
    public Subcategoria(String idSubcategoria, String nombre, String imagen) {
        this.idSubcategoria = idSubcategoria;
        this.nombre = nombre;
        this.imagen = imagen;
    }
    
    public String getIdSubcategoria() {
        return idSubcategoria;
    }
    
    public void setIdSubcategoria(String idSubcategoria) {
        this.idSubcategoria = idSubcategoria;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getImagen() {
        return imagen;
    }
    
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
