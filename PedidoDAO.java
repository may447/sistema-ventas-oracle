package dao;

import model.Pedido;
import services.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    private int obtenerNextIdPedido(Connection con) throws SQLException {
        String sql = "SELECT SEQ_PEDIDO.NEXTVAL AS nextval FROM DUAL";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("nextval");
            throw new SQLException("No se pudo obtener NEXTVAL de SEQ_PEDIDO");
        }
    }

    public boolean insertarPedidoConActualizacionStock(int idCliente, int idProducto, int cantidad, double total) {
        String sqlInsert = "INSERT INTO pedido (id_pedido, fecha, id_cliente, id_producto, cantidad, total) VALUES (?, SYSDATE, ?, ?, ?, ?)";
        String sqlSelectStock = "SELECT stock FROM producto WHERE id_producto = ? FOR UPDATE";
        String sqlUpdateStock = "UPDATE producto SET stock = stock - ? WHERE id_producto = ?";
        try (Connection con = DatabaseConnection.getConnection()) {
            try {
                con.setAutoCommit(false);
                int idPedido = obtenerNextIdPedido(con);
                int stockActual = 0;
                try (PreparedStatement psSel = con.prepareStatement(sqlSelectStock)) {
                    psSel.setInt(1, idProducto);
                    try (ResultSet rs = psSel.executeQuery()) {
                        if (rs.next()) stockActual = rs.getInt("stock");
                        else throw new SQLException("Producto no encontrado");
                    }
                }
                if (stockActual < cantidad) {
                    con.rollback();
                    return false;
                }
                try (PreparedStatement psIns = con.prepareStatement(sqlInsert)) {
                    psIns.setInt(1, idPedido);
                    psIns.setInt(2, idCliente);
                    psIns.setInt(3, idProducto);
                    psIns.setInt(4, cantidad);
                    psIns.setDouble(5, total);
                    psIns.executeUpdate();
                }
                try (PreparedStatement psUpd = con.prepareStatement(sqlUpdateStock)) {
                    psUpd.setInt(1, cantidad);
                    psUpd.setInt(2, idProducto);
                    psUpd.executeUpdate();
                }
                con.commit();
                return true;
            } catch (SQLException ex) {
                con.rollback();
                return false;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Pedido> listarPedidos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT id_pedido, fecha, id_cliente, id_producto, cantidad, total FROM pedido ORDER BY fecha DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pedido p = new Pedido(
                        rs.getInt("id_pedido"),
                        rs.getTimestamp("fecha"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_producto"),
                        rs.getInt("cantidad"),
                        rs.getDouble("total")
                );
                lista.add(p);
            }
        } catch (SQLException e) {}
        return lista;
    }

    public boolean actualizarPedido(int idPedido, int nuevaCantidad, double nuevoTotal) {
        String sql = "UPDATE pedido SET cantidad = ?, total = ? WHERE id_pedido = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nuevaCantidad);
            ps.setDouble(2, nuevoTotal);
            ps.setInt(3, idPedido);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean eliminarPedido(int idPedido) {
        String sql = "DELETE FROM pedido WHERE id_pedido = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}

