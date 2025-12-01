package dao;

import model.Empleado;
import services.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    public boolean insert(Empleado emp) {
        String sql = "INSERT INTO empleado(nombre, apellido, dni, salario) VALUES(?,?,?,?)";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, emp.getNombre());
            ps.setString(2, emp.getApellido());
            ps.setString(3, emp.getDni());
            ps.setDouble(4, emp.getSalario());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error insert empleado: " + e.getMessage());
            return false;
        }
    }

    public List<Empleado> getAll() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM empleado";

        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Empleado(
                        rs.getInt("id_empleado"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getDouble("salario")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error listar empleados: " + e.getMessage());
        }
        return lista;
    }

    public boolean update(Empleado emp) {
        String sql = "UPDATE empleado SET nombre=?, apellido=?, dni=?, salario=? WHERE id_empleado=?";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, emp.getNombre());
            ps.setString(2, emp.getApellido());
            ps.setString(3, emp.getDni());
            ps.setDouble(4, emp.getSalario());
            ps.setInt(5, emp.getIdEmpleado());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error update empleado: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM empleado WHERE id_empleado=?";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error delete empleado: " + e.getMessage());
            return false;
        }
    }
}
