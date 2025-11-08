package dao;

import model.Producto;
import services.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public boolean insertarProducto(Producto producto) {
        String sql = "INSERT INTO producto (id_producto, nombre, precio, stock) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, producto.getIdProducto());
            ps.setString(2, producto.getNombre());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, 50);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Producto> obtenerProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id_producto, nombre, precio FROM producto ORDER BY id_producto";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getDouble("precio")
                );
                lista.add(p);
            }
        } catch (SQLException e) {}
        return lista;
    }

    public Double obtenerPrecioProducto(int idProducto) {
        String sql = "SELECT precio FROM producto WHERE id_producto = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("precio");
                }
            }
        } catch (SQLException e) {}
        return null;
    }

    public boolean actualizarPrecio(int idProducto, double nuevoPrecio) {
        String sql = "UPDATE producto SET precio = ? WHERE id_producto = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, nuevoPrecio);
            ps.setInt(2, idProducto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean eliminarProducto(int idProducto) {
        String sql = "DELETE FROM producto WHERE id_producto = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean existeProducto(int idProducto) {
        String sql = "SELECT 1 FROM producto WHERE id_producto = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }
}

