package services;

import dao.VentaDAO;
import dao.DetalleVentaDAO;
import dao.ProductoDAO;
import model.Venta;
import model.DetalleVenta;
import services.DatabaseConnection;

import java.sql.Connection;
import java.util.List;

public class VentaService {

    private final VentaDAO ventaDAO = new VentaDAO();
    private final DetalleVentaDAO detalleDAO = new DetalleVentaDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();

    public int crearVentaConDetalles(Venta v, List<DetalleVenta> detalles) {
        Connection cn = null;
        try {
            cn = DatabaseConnection.getConnection();
            cn.setAutoCommit(false);

            int idVenta = ventaDAO.insert(v); // debe retornar id generado
            if (idVenta <= 0) throw new RuntimeException("No se generó id venta");

            for (DetalleVenta d : detalles) {
                d.setIdVenta(idVenta);
                boolean stockOk = productoDAO.decreaseStock(d.getIdProducto(), d.getCantidad());
                if (!stockOk) throw new RuntimeException("Stock insuficiente id:" + d.getIdProducto());
                detalleDAO.insert(d);
            }

            cn.commit();
            return idVenta;
        } catch (Exception ex) {
            try { if (cn != null) cn.rollback(); } catch (Exception ignore) {}
            ex.printStackTrace();
            return -1;
        } finally {
            try { if (cn != null) cn.setAutoCommit(true); } catch (Exception ignore) {}
        }
    }
}

