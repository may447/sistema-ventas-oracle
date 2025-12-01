package dao;

import model.Venta;
import services.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VentaDAO {

    public int insert(Venta v) throws SQLException {
        String sql = "INSERT INTO venta(id_empleado, id_cliente, fecha) VALUES(?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, v.getIdEmpleado());
            ps.setInt(2, v.getIdCliente());
            if (v.getFecha() != null) ps.setDate(3, new java.sql.Date(v.getFecha().getTime()));
            else ps.setDate(3, new java.sql.Date(new Date().getTime()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public List<Venta> getAll() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM venta ORDER BY id_venta";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Venta(
                        rs.getInt("id_venta"),
                        rs.getInt("id_empleado"),
                        rs.getInt("id_cliente"),
                        rs.getDate("fecha")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error listar ventas: " + e.getMessage());
        }
        return lista;
    }
}

