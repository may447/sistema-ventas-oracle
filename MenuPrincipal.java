package view;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {

        setTitle("Sistema de Ventas");
        setSize(500, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(30, 60, 90));
        panelTitulo.setPreferredSize(new Dimension(500, 80));

        JLabel lblTitulo = new JLabel("SISTEMA DE VENTAS");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        panelTitulo.add(lblTitulo);

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new GridLayout(2, 2, 20, 20));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panelCentral.setBackground(new Color(235, 238, 241));

        JButton btnEmpleado = crearBoton("Empleados");
        JButton btnProducto = crearBoton("Productos");
        JButton btnCliente = crearBoton("Clientes");
        JButton btnVenta = crearBoton("Ventas");

        btnEmpleado.addActionListener(e -> new FrmEmpleados().setVisible(true));
        btnProducto.addActionListener(e -> new FrmProductos().setVisible(true));
        btnCliente.addActionListener(e -> new FrmClientes().setVisible(true));
        btnVenta.addActionListener(e -> new FrmVentas().setVisible(true));

        panelCentral.add(btnEmpleado);
        panelCentral.add(btnProducto);
        panelCentral.add(btnCliente);
        panelCentral.add(btnVenta);

        JPanel panelFooter = new JPanel();
        panelFooter.setBackground(new Color(245, 245, 245));
        panelFooter.setPreferredSize(new Dimension(500, 40));

        JLabel lblFooter = new JLabel("© 2025 Sistema de Ventas");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelFooter.add(lblFooter);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                int opcion = JOptionPane.showConfirmDialog(
                        null,
                        "¿Deseas salir del sistema?",
                        "Confirmación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (opcion == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        add(panelTitulo, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelFooter, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(30, 30, 30));
        btn.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(220, 230, 240));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.WHITE);
            }
        });

        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
