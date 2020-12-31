package moto.movil.chiclayo.entity;

/**
 * By: El Bryant
 */

public class Pedido {
    private String idPedido;
    private String fechaSolicitado;
    private String horaSolicitado;
    private String fechaEntregado;
    private String horaEntregado;
    private String direccionOrigen;
    private String latitudOrigen;
    private String longitudOrigen;
    private String direccionEntrega;
    private String latitudDestino;
    private String longitudDestino;
    private Double total;
    private String dniRepartidor;
    private String idTienda;
    private String estado;
    private String nombreRepartidor;
    private String nombreTienda;
    
    public Pedido(String idPedido, String fechaSolicitado, String horaSolicitado, String fechaEntregado, String horaEntregado, String direccionOrigen, String latitudOrigen, String longitudOrigen,
                  String direccionEntrega, String latitudDestino, String longitudDestino, Double total, String dniRepartidor, String idTienda, String estado, String nombreRepartidor, String nombreTienda) {
        this.idPedido = idPedido;
        this.fechaSolicitado = fechaSolicitado;
        this.horaSolicitado = horaSolicitado;
        this.fechaEntregado = fechaEntregado;
        this.horaEntregado = horaEntregado;
        this.direccionOrigen = direccionOrigen;
        this.latitudOrigen = latitudOrigen;
        this.longitudOrigen = longitudOrigen;
        this.direccionEntrega = direccionEntrega;
        this.latitudDestino = latitudDestino;
        this.longitudDestino = longitudDestino;
        this.total = total;
        this.dniRepartidor = dniRepartidor;
        this.idTienda = idTienda;
        this.estado = estado;
        this.nombreRepartidor = nombreRepartidor;
        this.nombreTienda = nombreTienda;
    }
    
    public String getIdPedido() {
        return idPedido;
    }
    
    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }
    
    public String getFechaSolicitado() {
        return fechaSolicitado;
    }
    
    public void setFechaSolicitado(String fechaSolicitado) {
        this.fechaSolicitado = fechaSolicitado;
    }
    
    public String getHoraSolicitado() {
        return horaSolicitado;
    }
    
    public void setHoraSolicitado(String horaSolicitado) {
        this.horaSolicitado = horaSolicitado;
    }
    
    public String getFechaEntregado() {
        return fechaEntregado;
    }
    
    public void setFechaEntregado(String fechaEntregado) {
        this.fechaEntregado = fechaEntregado;
    }
    
    public String getHoraEntregado() {
        return horaEntregado;
    }
    
    public void setHoraEntregado(String horaEntregado) {
        this.horaEntregado = horaEntregado;
    }
    
    public String getDireccionOrigen() {
        return direccionOrigen;
    }
    
    public void setDireccionOrigen(String direccionOrigen) {
        this.direccionOrigen = direccionOrigen;
    }
    
    public String getLatitudOrigen() {
        return latitudOrigen;
    }
    
    public void setLatitudOrigen(String latitudOrigen) {
        this.latitudOrigen = latitudOrigen;
    }
    
    public String getLongitudOrigen() {
        return longitudOrigen;
    }
    
    public void setLongitudOrigen(String longitudOrigen) {
        this.longitudOrigen = longitudOrigen;
    }
    
    public String getDireccionEntrega() {
        return direccionEntrega;
    }
    
    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }
    
    public String getLatitudDestino() {
        return latitudDestino;
    }
    
    public void setLatitudDestino(String latitudDestino) {
        this.latitudDestino = latitudDestino;
    }
    
    public String getLongitudDestino() {
        return longitudDestino;
    }
    
    public void setLongitudDestino(String longitudDestino) {
        this.longitudDestino = longitudDestino;
    }
    
    public Double getTotal() {
        return total;
    }
    
    public void setTotal(Double total) {
        this.total = total;
    }
    
    public String getDniRepartidor() {
        return dniRepartidor;
    }
    
    public void setDniRepartidor(String dniRepartidor) {
        this.dniRepartidor = dniRepartidor;
    }
    
    public String getIdTienda() {
        return idTienda;
    }
    
    public void setIdTienda(String idTienda) {
        this.idTienda = idTienda;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getNombreRepartidor() {
        return nombreRepartidor;
    }
    
    public void setNombreRepartidor(String nombreRepartidor) {
        this.nombreRepartidor = nombreRepartidor;
    }
    
    public String getNombreTienda() {
        return nombreTienda;
    }
    
    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }
}
