package moto.movil.chiclayo.entity;

public class Categoria {
    private String idCategoria;
    private String nombre;
    private String icono;
    private String imagen;

    public Categoria(String idCategoria, String nombre, String icono, String imagen) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.icono = icono;
        this.imagen = imagen;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
