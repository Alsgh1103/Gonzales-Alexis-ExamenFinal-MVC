package org.unisiga.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.unisiga.model.Asignatura;
import org.unisiga.model.Evaluacion;
import org.unisiga.model.Seccion;
import org.unisiga.view.EvaluacionView;

public class EvaluacionController {

    private EvaluacionView view;
    private List<Asignatura> asignaturas;
    private DefaultTableModel modelo;
    private int contadorId = 1;
    private int filaSeleccionada = -1;

    public EvaluacionController(EvaluacionView view, List<Asignatura> asignaturas) {
        this.view = view;
        this.asignaturas = asignaturas;
        this.modelo = (DefaultTableModel) view.tblEvaluacion.getModel();
        view.setLocationRelativeTo(null);
        view.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        cargarComboSecciones();
        initListeners();
        refrescarTabla();
        actualizarSumaPonderacion();
    }

    private void cargarComboSecciones() {
        view.cmbSeccion.removeAllItems();
        for (Asignatura a : asignaturas) {
            view.cmbSeccion.addItem(a.getCodigo());
        }
    }

    private void initListeners() {
        view.bntAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregar();
            }
        });

        view.btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificar();
            }
        });

        view.btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminar();
            }
        });

        view.btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiar();
            }
        });

        view.btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        });

        view.cmbSeccion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refrescarTabla();
                actualizarSumaPonderacion();
            }
        });

        view.tblEvaluacion.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cargarDesdeTabla();
            }
        });
    }

    private void agregar() {
        String titulo = view.txtTitulo.getText().trim();
        String ponderacionStr = view.txtPonderacion.getText().trim();
        String codigoAsig = (String) view.cmbSeccion.getSelectedItem();

        if (titulo.isEmpty() || ponderacionStr.isEmpty() || codigoAsig == null) {
            javax.swing.JOptionPane.showMessageDialog(view, "Todos los campos son obligatorios.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        float ponderacion;
        try {
            ponderacion = Float.parseFloat(ponderacionStr);
            if (ponderacion <= 0 || ponderacion > 100) throw new IllegalArgumentException();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "La ponderación debe ser un número entre 0 y 100.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        Asignatura asig = buscarAsignatura(codigoAsig);
        if (asig == null) return;

        float sumaActual = calcularSuma(asig);
        if (sumaActual + ponderacion > 100f) {
            javax.swing.JOptionPane.showMessageDialog(view, "La suma de ponderaciones supera 100%. Suma actual: " + sumaActual + "%", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        asig.crearEvaluacion(contadorId++, titulo, ponderacion);
        refrescarTabla();
        actualizarSumaPonderacion();
        limpiar();
    }

    private void modificar() {
        if (filaSeleccionada < 0) {
            javax.swing.JOptionPane.showMessageDialog(view, "Seleccione una evaluación de la tabla.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String ponderacionStr = view.txtPonderacion.getText().trim();
        String titulo = view.txtTitulo.getText().trim();
        String codigoAsig = (String) view.cmbSeccion.getSelectedItem();

        if (titulo.isEmpty() || ponderacionStr.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(view, "Título y ponderación son obligatorios.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        float ponderacion;
        try {
            ponderacion = Float.parseFloat(ponderacionStr);
            if (ponderacion <= 0 || ponderacion > 100) throw new IllegalArgumentException();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(view, "La ponderación debe ser un número entre 0 y 100.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        String tituloOriginal = modelo.getValueAt(filaSeleccionada, 1).toString();
        Asignatura asig = buscarAsignatura(codigoAsig);
        if (asig == null) return;

        float sumaActual = 0;
        Evaluacion evalObjetivo = null;
        for (Evaluacion ev : asig.getEvaluaciones()) {
            if (ev.getTitulo().equals(tituloOriginal)) {
                evalObjetivo = ev;
            } else {
                sumaActual += ev.getPonderacion();
            }
        }

        if (sumaActual + ponderacion > 100f) {
            javax.swing.JOptionPane.showMessageDialog(view, "La suma de ponderaciones supera 100%. Disponible: " + (100f - sumaActual) + "%", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (evalObjetivo != null) {
            evalObjetivo.setTitulo(titulo);
            evalObjetivo.setPonderacion(ponderacion);
        }

        refrescarTabla();
        actualizarSumaPonderacion();
        limpiar();
    }

    private void eliminar() {
        boolean eliminado = false;
        String codigoAsig = (String) view.cmbSeccion.getSelectedItem();
        Asignatura asig = buscarAsignatura(codigoAsig);
        if (asig == null) return;

        for (int i = modelo.getRowCount() - 1; i >= 0; i--) {
            Boolean sel = (Boolean) modelo.getValueAt(i, 0);
            if (sel != null && sel) {
                String tituloEv = modelo.getValueAt(i, 1).toString();
                asig.getEvaluaciones().removeIf(ev -> ev.getTitulo().equals(tituloEv));
                eliminado = true;
            }
        }
        if (!eliminado) {
            javax.swing.JOptionPane.showMessageDialog(view, "Marque al menos una evaluación para eliminar.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        refrescarTabla();
        actualizarSumaPonderacion();
        limpiar();
    }

    private void cargarDesdeTabla() {
        int fila = view.tblEvaluacion.getSelectedRow();
        if (fila < 0) return;
        filaSeleccionada = fila;
        view.txtTitulo.setText(modelo.getValueAt(fila, 1).toString());
        view.txtPonderacion.setText(modelo.getValueAt(fila, 2).toString());
    }

    private void limpiar() {
        view.txtTitulo.setText("");
        view.txtPonderacion.setText("");
        view.tblEvaluacion.clearSelection();
        filaSeleccionada = -1;
    }

    private void actualizarSumaPonderacion() {
        String codigoAsig = (String) view.cmbSeccion.getSelectedItem();
        Asignatura asig = buscarAsignatura(codigoAsig);
        if (asig == null) {
            view.lblSumaPonderacion.setText("Suma: 0%");
            return;
        }
        float suma = calcularSuma(asig);
        view.lblSumaPonderacion.setText("Suma actual: " + suma + "% / 100%");
        if (suma > 100f) {
            view.lblSumaPonderacion.setForeground(java.awt.Color.RED);
        } else if (suma == 100f) {
            view.lblSumaPonderacion.setForeground(java.awt.Color.GREEN);
        } else {
            view.lblSumaPonderacion.setForeground(java.awt.Color.BLUE);
        }
    }

    private float calcularSuma(Asignatura asig) {
        float suma = 0;
        for (Evaluacion ev : asig.getEvaluaciones()) {
            suma += ev.getPonderacion();
        }
        return suma;
    }

    private void refrescarTabla() {
        modelo.setRowCount(0);
        String codigoAsig = (String) view.cmbSeccion.getSelectedItem();
        Asignatura asig = buscarAsignatura(codigoAsig);
        if (asig == null) return;
        for (Evaluacion ev : asig.getEvaluaciones()) {
            modelo.addRow(new Object[]{false, ev.getTitulo(), ev.getPonderacion()});
        }
    }

    private Asignatura buscarAsignatura(String codigo) {
        if (codigo == null) return null;
        for (Asignatura a : asignaturas) {
            if (a.getCodigo().equals(codigo)) return a;
        }
        return null;
    }
}
