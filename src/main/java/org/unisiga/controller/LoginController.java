package org.unisiga.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.unisiga.model.Academico;
import org.unisiga.model.Estudiante;
import org.unisiga.model.MiembroUniversitario;
import org.unisiga.view.LoginView;
import org.unisiga.view.MainMenuView;

public class LoginController {

    private LoginView view;
    private List<MiembroUniversitario> usuarios;

    public LoginController(LoginView view, List<MiembroUniversitario> usuarios) {
        this.view = view;
        this.usuarios = usuarios;
        view.setLocationRelativeTo(null);
        view.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        initListeners();
    }

    private void initListeners() {
        view.btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticar();
            }
        });
        view.btnIniciarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticar();
            }
        });
    }

    private void autenticar() {
        String rut = view.txtRut.getText().trim();
        String password = new String(view.txtPassword.getPassword()).trim();

        if (rut.isEmpty() || password.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(view, "RUT y contraseña son obligatorios.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        MiembroUniversitario encontrado = null;
        for (MiembroUniversitario u : usuarios) {
            if (u.getRut().equals(rut)) {
                encontrado = u;
                break;
            }
        }

        if (encontrado == null) {
            javax.swing.JOptionPane.showMessageDialog(view, "Usuario no encontrado.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean acceso = encontrado.login(password);
        if (!acceso) {
            javax.swing.JOptionPane.showMessageDialog(view, "Contraseña incorrecta.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        String bienvenida = "Bienvenido/a, " + encontrado.getNombre();
        if (encontrado instanceof Academico) {
            bienvenida += " (Académico)";
        } else if (encontrado instanceof Estudiante) {
            bienvenida += " (Estudiante)";
        }

        MainMenuView menu = new MainMenuView();
        MainMenuController menuCtrl = new MainMenuController(menu, usuarios);
        menu.lblBienvenida.setText(bienvenida);
        menu.setVisible(true);
        view.dispose();
    }
}
