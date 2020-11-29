/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runosoftware.controller;

import com.runosoftware.logica.Empleado;
import com.runosoftware.view.FrmPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vidal
 */
public class controllerEmpleado implements ActionListener {

    /**
     * Variables globales el DefaultTablemodel para la tabla la creación del
     * EntityManager como inicializacion global para el archivo persistence.xml.
     */
    private FrmPrincipal frame;
    private DefaultTableModel modeloempleado;
    private static EntityManager manager;
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("PersistenciaConfig");
    private int opc;

    public controllerEmpleado() {
        this.frame = new FrmPrincipal();
        this.modeloempleado = new DefaultTableModel();
        this.frame.getBtnAgregar().addActionListener(this);
        this.frame.getBtnBuscar().addActionListener(this);
        this.frame.getBtnEditar().addActionListener(this);
        this.frame.getBtnEliminar().addActionListener(this);
        this.frame.getBtnTest().addActionListener(this);
        this.opc = 0;

    }

    /**
     * mostrar la ventana del frame principal.
     */
    public void mostrarVentana() {
        this.frame.setLocationRelativeTo(frame);
        this.frame.setTitle("Empleados");
        this.frame.setVisible(true);
        this.mostrarDatos();
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == this.frame.getBtnAgregar()) {
            insertarDatos();
        }

        if (event.getSource() == this.frame.getBtnEliminar()) {
            eliminar();
        }

        if (event.getSource() == this.frame.getBtnEditar()) {
            editar();
        }

        // if(event.getSource()==this.frame.getBtnTest()) testInserccion();
    }

    /**
     * ------------------------------------ METODOS
     * ------------------------------------ *
     */
    /**
     * crear la instacia del manager para cualquier transaccion de la base de
     * dato.
     *
     * @return
     */
    public EntityManager crearManager() {
        EntityManager manager = emf.createEntityManager();
        manager.getTransaction().begin();
        return manager;
    }

    /**
     * Cerramos el manager de la transaacion en curso el cual se pasa por
     * parametros
     *
     * @param manager
     */
    public void cerrarManager(EntityManager manager) {
        if (manager.isOpen()) {
            manager.getTransaction().commit();
            manager.close();
        }
    }

    /**
     * Obtener datos de la vista para crear un nuevo registro.
     */
    public void insertarDatos() {
        if (opc == 0) {
            try {
                int cod = Integer.parseInt(this.frame.getTxtCodigo().getText());
                String nom = this.frame.getTxtNombre().getText();
                String apelli = this.frame.getTxtApellido().getText();
                Date fecha = this.frame.getJcFecha().getDate();
                System.out.println("fecha ." + fecha);
                EntityManager manager = crearManager();
                Empleado emp = new Empleado(cod, nom, apelli, fecha);
                manager.persist(emp);
                cerrarManager(manager);
                System.out.println("insertado");
                this.limpiar();
                this.mostrarDatos();

            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
            }
        } else if (opc == 1) {
            this.editarTrabajador();
        }

    }

    /**
     * Eliminar registros de la base de datos.
     */
    public void eliminar() {

        try {
            int fila = this.frame.getTblEmpleados().getSelectedRow();

            if (fila > -1) {
                int codi = Integer.parseInt(this.frame.getTblEmpleados().getValueAt(fila, 0).toString());

                System.out.println("codigo " + codi);
                int opcion = JOptionPane.showConfirmDialog(this.frame, "Desea eliminar el registro: "
                        + codi + "?", "Eliminar", JOptionPane.YES_NO_OPTION);
                if (opcion == JOptionPane.YES_OPTION) {
                    Empleado emp = null;
                    EntityManager manager = crearManager();
                    emp = manager.find(Empleado.class, codi);
                    manager.remove(emp);
                    this.cerrarManager(manager);
                    this.mostrarDatos();

                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una fila para eliminar");
            }

        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
        }

    }

    /**
     * Editar los datos del trabajador seleccionado.
     */
    public void editar() {
        int fila = this.frame.getTblEmpleados().getSelectedRow();
        if (fila > -1) {
            int codi = Integer.parseInt(this.frame.getTblEmpleados().getValueAt(fila, 0).toString());
            int opcion = JOptionPane.showConfirmDialog(this.frame, "Desea editar el registro:  "
                    + codi + "?", "Editar", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                obtenerTrabajador(codi);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una fila para editar.");
        }
    }

    /**
     *
     * @param codigo
     */
    public void obtenerTrabajador(int codigo) {
        System.out.println("codigo a obtener datos: " + codigo);
        Empleado empleado;
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        empleado = manager.find(Empleado.class, codigo);
        manager.getTransaction().commit();
        manager.close();
        System.out.println(empleado.toString());
        this.pintarDatos(empleado);
    }

    /**
     * Pintar el objeto en la vista para luego editar
     *
     * @param emp
     */
    public void pintarDatos(Empleado emp) {
        try {
            this.frame.getTxtCodigo().setEditable(false);
            this.frame.getTxtCodigo().setText(String.valueOf(emp.getCodigo()));
            this.frame.getTxtNombre().setText(emp.getNombre());
            this.frame.getTxtApellido().setText(emp.getApellido());
            this.frame.getJcFecha().setDate(emp.getFechaNacimiento());
            this.opc = 1;
        } catch (Exception e) {
            System.out.println("error al pintar datos: " + e.getMessage());
        }

    }

    /**
     * una ves confirmado los cambio, se edita el trabajador y se procede a
     * guardar.
     */
    public void editarTrabajador() {
        int cod = Integer.parseInt(this.frame.getTxtCodigo().getText());
        String nom = this.frame.getTxtNombre().getText();
        String apelli = this.frame.getTxtApellido().getText();
        Date fecha = this.frame.getJcFecha().getDate();
        System.out.println("editar");
        EntityManager manager = crearManager();
        Empleado empl = new Empleado(cod, nom, apelli, fecha);
        manager.merge(empl);
        cerrarManager(manager);
        System.out.println("insertado");
        this.limpiar();
        this.mostrarDatos();
        this.frame.getTxtCodigo().setEditable(true);
        this.opc = 0;

    }

    /**
     * Pintar los datos de la base de datos en la tabla.
     */
    public void mostrarDatos() {

        String[] title = {"codigo", "Nombre", "Apellido", "Fecha Nacimiento"};
        modeloempleado = new DefaultTableModel(null, title) {

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };

        try {
            EntityManager manager = crearManager();
            List<Empleado> empleado = manager.createQuery("from Empleado").getResultList();
            for (Empleado emp : empleado) {

                Object[] objeto = {emp.getCodigo(), emp.getNombre(),
                    emp.getApellido(), emp.getFechaNacimiento()};
                modeloempleado.addRow(objeto);
            }
            this.cerrarManager(manager);
            this.frame.getTblEmpleados().setModel(modeloempleado);
        } catch (Exception e) {
            System.out.println("Error al mostrar " + e.getMessage());
        }
    }

    /**
     * Limpiar las opciones del menu.
     */
    public void limpiar() {
        this.frame.getTxtApellido().setText("");
        this.frame.getTxtCodigo().setText("");
        this.frame.getTxtNombre().setText("");
    }

    /**
     * metodo prueba de insercciones, solo cambiar los indices del ciclo para
     * probar con una cantidad masiva de datos.
     */
    public void testInserccion() {
        for (int i = 10000; i < 15000; i++) {
            manager = emf.createEntityManager();
            Empleado emp = new Empleado(i, "Runo " + i, "Software" + i, new Date("Thu Nov 10 03:03:01 CST 2050"));
            manager.getTransaction().begin();
            manager.persist(emp);
            manager.getTransaction().commit();
            manager.close();
            System.out.println("Test Insertado: " + i);
        }
        this.mostrarDatos();

    }

}
