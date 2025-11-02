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
            System.out.println("2. Registrar pedido");
            System.out.println("3. Listar pedidos");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> listarProductos();
                case 2 -> registrarPedido();
                case 3 -> listarPedidos();
                case 0 -> System.out.println("Saliendo... hasta luego.");
                default -> System.out.println("Opción inválida. Intente nuevamente.");
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

    private void registrarPedido() {
        System.out.println("\n--- Registrar Pedido ---");
        System.out.print("ID cliente: ");
        int idCliente = leerEntero();
        if (!controller.existeCliente(idCliente)) {
            System.out.println("Cliente no encontrado (verifique el id).");
            return;
        }

        System.out.print("ID producto: ");
        int idProducto = leerEntero();
        if (!controller.existeProducto(idProducto)) {
            System.out.println("Producto no encontrado (verifique el id).");
            return;
        }

        System.out.print("Cantidad: ");
        int cantidad = leerEntero();
        if (cantidad <= 0) {
            System.out.println("Cantidad inválida.");
            return;
        }

        Double precio = controller.obtenerPrecioProducto(idProducto);
        if (precio == null) {
            System.out.println("No se pudo obtener el precio del producto.");
            return;
        }

        double total = cantidad * precio;
        boolean ok = controller.insertarPedido(idCliente, idProducto, cantidad, total);
        if (ok) {
            System.out.println("Pedido registrado correctamente. Total S/. " + String.format("%.2f", total));
        } else {
            System.out.println("Error al registrar pedido.");
        }
    }

    private void listarPedidos() {
        List<Pedido> pedidos = controller.listarPedidos();
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
            return;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("\nID | FECHA | ID_CLIENTE | ID_PRODUCTO | CANT | TOTAL");
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

}

