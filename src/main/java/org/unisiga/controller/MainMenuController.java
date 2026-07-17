package org.unisiga.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import org.unisiga.model.Asignatura;
import org.unisiga.model.Calificacion;
import org.unisiga.model.Departamento;
import org.unisiga.model.Inscripcion;
import org.unisiga.model.MiembroUniversitario;
import org.unisiga.view.AcademicoView;
import org.unisiga.view.AsignaturaView;
import org.unisiga.view.CalificacionView;
import org.unisiga.view.DepartamentoView;
import org.unisiga.view.EstudianteView;
import org.unisiga.view.EvaluacionView;
import org.unisiga.view.InscripcionView;
import org.unisiga.view.MainMenuView;
import org.unisiga.view.SeccionView;

public class MainMenuController {

    private MainMenuView view;
    private List<MiembroUniversitario> usuarios;
    private List<Departamento> departamentos;
    private List<Asignatura> asignaturas;
    private List<Inscripcion> inscripciones;
    private List<Calificacion> calificaciones;

    public MainMenuController(MainMenuView view, List<MiembroUniversitario> usuarios) {
        this.view = view;
        this.usuarios = usuarios;
        this.departamentos = new java.util.ArrayList<>();
        this.asignaturas = new java.util.ArrayList<>();
        this.inscripciones = new java.util.ArrayList<>();
        this.calificaciones = new java.util.ArrayList<>();
        view.setLocationRelativeTo(null);
        view.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        initListeners();
    }

    private void abrirVista(javax.swing.JFrame subVista) {
        view.setVisible(false);
        subVista.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                view.setVisible(true);
                view.setLocationRelativeTo(null);
            }
        });
        subVista.setVisible(true);
    }

    private void initListeners() {
        view.btnEstudiantes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EstudianteView ev = new EstudianteView();
                new EstudianteController(ev, usuarios);
                abrirVista(ev);
            }
        });

        view.btnAcademicos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AcademicoView av = new AcademicoView();
                new AcademicoController(av, usuarios, departamentos);
                abrirVista(av);
            }
        });

        view.btnDepartamentos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DepartamentoView dv = new DepartamentoView();
                new DepartamentoController(dv, departamentos);
                abrirVista(dv);
            }
        });

        view.btnAsignaturas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AsignaturaView av = new AsignaturaView();
                new AsignaturaController(av, asignaturas);
                abrirVista(av);
            }
        });

        view.btnSecciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SeccionView sv = new SeccionView();
                new SeccionController(sv, asignaturas, usuarios);
                abrirVista(sv);
            }
        });

        view.btnInscripciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InscripcionView iv = new InscripcionView();
                new InscripcionController(iv, usuarios, asignaturas, inscripciones);
                abrirVista(iv);
            }
        });

        view.btnEvaluaciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EvaluacionView ev = new EvaluacionView();
                new EvaluacionController(ev, asignaturas);
                abrirVista(ev);
            }
        });

        view.btnCalificaciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CalificacionView cv = new CalificacionView();
                new CalificacionController(cv, inscripciones, asignaturas, calificaciones);
                abrirVista(cv);
            }
        });

        view.btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = javax.swing.JOptionPane.showConfirmDialog(view, "¿Desea salir del sistema?", "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION);
                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }
}
