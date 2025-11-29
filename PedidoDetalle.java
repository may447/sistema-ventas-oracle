package model;

import java.sql.Timestamp;

public class PedidoDetalle {
    private int idPedido;
    private String cliente;
    private String producto;
    private int cantidad;
    private double total;
    private Timestamp fecha;

    public PedidoDetalle(int idPedido, String cliente, String producto, int cantidad, double total, Timestamp fecha) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.producto = producto;
        this.cantidad = cantidad;
        this.total = total;
        this.fecha = fecha;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public String getCliente() {
        return cliente;
    }

    public String getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getTotal() {
        return total;
    }

    public Timestamp getFecha() {
        return fecha;
    }
}

