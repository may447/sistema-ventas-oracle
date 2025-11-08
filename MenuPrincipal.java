package view;

import controller.VentaController;
import model.Producto;
import model.Pedido;
import model.Cliente;
import java.util.List;
import java.util.Scanner;
import java.text.SimpleDateFormat;

public class MenuPrincipal {

    private final Scanner sc = new Scanner(System.in);
    private final VentaController controller = new VentaController();

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n===== SISTEMA DE GESTIÓN DE VENTAS =====");
            System.out.println("1. Listar productos");
            System.out.println("2. Registrar producto");
            System.out.println("3. Actualizar precio de producto");
            System.out.println("4. Eliminar producto");
            System.out.println("5. Listar clientes");
            System.out.println("6. Registrar cliente");
            System.out.println("7. Actualizar teléfono cliente");
            System.out.println("8. Eliminar cliente");
            System.out.println("9. Registrar pedido");
            System.out.println("10. Listar pedidos");
            System.out.println("11. Actualizar pedido");
            System.out.println("12. Eliminar pedido");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1:
                    listarProductos();
                    break;
                case 2:
                    registrarProducto();
                    break;
                case 3:
                    actualizarPrecioProducto();
                    break;
                case 4:
                    eliminarProducto();
                    break;
                case 5:
                    listarClientes();
                    break;
                case 6:
                    registrarCliente();
                    break;
                case 7:
                    actualizarTelefonoCliente();
                    break;
                case 8:
                    eliminarCliente();
                    break;
                case 9:
                    registrarPedido();
                    break;
                case 10:
                    listarPedidos();
                    break;
                case 11:
                    actualizarPedido();
                    break;
                case 12:
                    eliminarPedido();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
        } while (opcion != 0);
    }

    private void listarProductos() {
        List<Producto> productos = controller.obtenerProductos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }
        System.out.println("\nID | NOMBRE | PRECIO");
        for (Producto p : productos) {
            System.out.printf("%d | %s | %.2f%n", p.getIdProducto(), p.getNombre(), p.getPrecio());
        }
    }

    private void registrarProducto() {
        System.out.print("ID producto: ");
        int id = leerEntero();
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Precio: ");
        double precio = leerDouble();
        Producto p = new Producto(id, nombre, precio);
        boolean ok = controller.insertarProducto(p);
        System.out.println(ok ? "Producto registrado." : "Error al registrar producto.");
    }

    private void actualizarPrecioProducto() {
        System.out.print("ID producto: ");
        int id = leerEntero();
        if (!controller.existeProducto(id)) {
            System.out.println("Producto no encontrado.");
            return;
        }
        System.out.print("Nuevo precio: ");
        double nuevo = leerDouble();
        boolean ok = controller.actualizarPrecioProducto(id, nuevo);
        System.out.println(ok ? "Precio actualizado." : "Error al actualizar.");
    }

    private void eliminarProducto() {
        System.out.print("ID producto: ");
        int id = leerEntero();
        boolean ok = controller.eliminarProducto(id);
        System.out.println(ok ? "Producto eliminado." : "Error al eliminar producto.");
    }

    private void listarClientes() {
        List<Cliente> clientes = controller.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        System.out.println("\nID | NOMBRE | TELÉFONO");
        for (Cliente c : clientes) {
            System.out.printf("%d | %s | %s%n", c.getIdCliente(), c.getNombre(), c.getTelefono());
        }
    }

    private void registrarCliente() {
        System.out.print("ID cliente: ");
        int id = leerEntero();
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Teléfono: ");
        String tel = sc.nextLine();
        Cliente c = new Cliente(id, nombre, tel);
        boolean ok = controller.insertarCliente(c);
        System.out.println(ok ? "Cliente registrado." : "Error al registrar cliente.");
    }

    private void actualizarTelefonoCliente() {
        System.out.print("ID cliente: ");
        int id = leerEntero();
        if (!controller.existeCliente(id)) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        System.out.print("Nuevo teléfono: ");
        String tel = sc.nextLine();
        boolean ok = controller.actualizarTelefonoCliente(id, tel);
        System.out.println(ok ? "Teléfono actualizado." : "Error al actualizar.");
    }

    private void eliminarCliente() {
        System.out.print("ID cliente: ");
        int id = leerEntero();
        boolean ok = controller.eliminarCliente(id);
        System.out.println(ok ? "Cliente eliminado." : "Error al eliminar cliente.");
    }

    private void registrarPedido() {
        System.out.print("ID cliente: ");
        int idCliente = leerEntero();
        if (!controller.existeCliente(idCliente)) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        System.out.print("ID producto: ");
        int idProducto = leerEntero();
        if (!controller.existeProducto(idProducto)) {
            System.out.println("Producto no encontrado.");
            return;
        }
        System.out.print("Cantidad: ");
        int cantidad = leerEntero();
        Double precio = controller.obtenerPrecioProducto(idProducto);
        if (precio == null) {
            System.out.println("Error al obtener precio.");
            return;
        }
        double total = cantidad * precio;
        boolean ok = controller.insertarPedidoConStock(idCliente, idProducto, cantidad, total);
        System.out.println(ok ? "Pedido registrado. Total S/ " + total : "Error al registrar pedido.");
    }

    private void listarPedidos() {
        List<Pedido> pedidos = controller.listarPedidos();
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos.");
            return;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("\nID | FECHA | CLIENTE | PRODUCTO | CANT | TOTAL");
        for (Pedido ped : pedidos) {
            String fecha = ped.getFecha() != null ? df.format(ped.getFecha()) : "NULL";
            System.out.printf("%d | %s | %d | %d | %d | %.2f%n",
                    ped.getIdPedido(),
                    fecha,
                    ped.getIdCliente(),
                    ped.getIdProducto(),
                    ped.getCantidad(),
                    ped.getTotal());
        }
    }

    private void actualizarPedido() {
        System.out.print("ID pedido: ");
        int id = leerEntero();
        System.out.print("Nueva cantidad: ");
        int cant = leerEntero();
        System.out.print("Nuevo total: ");
        double total = leerDouble();
        boolean ok = controller.actualizarPedido(id, cant, total);
        System.out.println(ok ? "Pedido actualizado." : "Error al actualizar pedido.");
    }

    private void eliminarPedido() {
        System.out.print("ID pedido: ");
        int id = leerEntero();
        boolean ok = controller.eliminarPedido(id);
        System.out.println(ok ? "Pedido eliminado." : "Error al eliminar pedido.");
    }

    private int leerEntero() {
        while (true) {
            try {
                String line = sc.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Ingrese un número: ");
            }
        }
    }

    private double leerDouble() {
        while (true) {
            try {
                String line = sc.nextLine().trim();
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Ingrese un número: ");
            }
        }
    }
}



