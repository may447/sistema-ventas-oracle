package dao;

import model.DetalleVenta;
import services.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaDAO {

    public boolean insert(DetalleVenta d) throws SQLException {
        String sql = "INSERT INTO detalle_venta(id_venta, id_producto, cantidad, subtotal) VALUES(?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, d.getIdVenta());
            ps.setInt(2, d.getIdProducto());
            ps.setInt(3, d.getCantidad());
            ps.setDouble(4, d.getSubtotal());
            ps.executeUpdate();
            return true;
        }
    }

    public List<DetalleVenta> findByVenta(int idVenta) {
        List<DetalleVenta> lista = new ArrayList<>();
        String sql = "SELECT dv.*, p.nombre AS producto FROM detalle_venta dv JOIN producto p ON dv.id_producto = p.id_producto WHERE id_venta = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleVenta d = new DetalleVenta(
                            rs.getInt("id_detalle"),
                            rs.getInt("id_venta"),
                            rs.getInt("id_producto"),
                            rs.getInt("cantidad"),
                            rs.getDouble("subtotal")
                    );
                    lista.add(d);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error find detalle: " + e.getMessage());
        }
        return lista;
    }

    public List<String[]> findViewByVenta(int idVenta) {
        // helper to show product name + amount + subtotal
        List<String[]> rows = new ArrayList<>();
        String sql = "SELECT p.nombre, dv.cantidad, dv.subtotal FROM detalle_venta dv JOIN producto p ON dv.id_producto = p.id_producto WHERE dv.id_venta = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new String[]{ rs.getString("nombre"), String.valueOf(rs.getInt("cantidad")), String.valueOf(rs.getDouble("subtotal")) });
                }
            }
        } catch (SQLException e) {
            System.out.println("Error detalle view: " + e.getMessage());
        }
        return rows;
    }
}

