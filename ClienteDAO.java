package dao;

import model.Cliente;
import services.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public boolean insertarCliente(Cliente cliente) {
        String sql = "INSERT INTO cliente (id_cliente, nombre, dni, telefono) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cliente.getIdCliente());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getTelefono());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Cliente> listarClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id_cliente, nombre, telefono FROM cliente ORDER BY id_cliente";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("telefono")
                );
                lista.add(c);
            }
        } catch (SQLException e) {}
        return lista;
    }

    public boolean actualizarTelefono(int idCliente, String nuevoTelefono) {
        String sql = "UPDATE cliente SET telefono = ? WHERE id_cliente = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoTelefono);
            ps.setInt(2, idCliente);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean eliminarCliente(int idCliente) {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean existeCliente(int idCliente) {
        String sql = "SELECT 1 FROM cliente WHERE id_cliente = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }
}
