package moto.movil.chiclayo.entity;

public class Tienda {
    private String idTienda;
    private String nombreTienda;
    private String direccionTienda;
    private String latitudTienda;
    private String longitudTienda;
    private String logoTienda;
    private String imagenTienda;

    public Tienda(String idTienda, String nombreTienda, String direccionTienda, String latitudTienda, String longitudTienda, String logoTienda, String imagenTienda) {
        this.idTienda = idTienda;
        this.nombreTienda = nombreTienda;
        this.direccionTienda = direccionTienda;
        this.latitudTienda = latitudTienda;
        this.longitudTienda = longitudTienda;
        this.logoTienda = logoTienda;
        this.imagenTienda = imagenTienda;
    }

    public String getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(String idTienda) {
        this.idTienda = idTienda;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    public String getDireccionTienda() {
        return direccionTienda;
    }

    public void setDireccionTienda(String direccionTienda) {
        this.direccionTienda = direccionTienda;
    }

    public String getLatitudTienda() {
        return latitudTienda;
    }

    public void setLatitudTienda(String latitudTienda) {
        this.latitudTienda = latitudTienda;
    }

    public String getLongitudTienda() {
        return longitudTienda;
    }

    public void setLongitudTienda(String longitudTienda) {
        this.longitudTienda = longitudTienda;
    }

    public String getLogoTienda() {
        return logoTienda;
    }

    public void setLogoTienda(String logoTienda) {
        this.logoTienda = logoTienda;
    }

    public String getImagenTienda() {
        return imagenTienda;
    }

    public void setImagenTienda(String imagenTienda) {
        this.imagenTienda = imagenTienda;
    }
}
