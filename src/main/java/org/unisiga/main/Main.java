package org.unisiga.main;

import java.util.ArrayList;
import java.util.List;
import org.unisiga.controller.LoginController;
import org.unisiga.model.Academico;
import org.unisiga.model.Estudiante;
import org.unisiga.model.MiembroUniversitario;
import org.unisiga.view.LoginView;

public class Main {
    public static void main(String[] args) {
        List<MiembroUniversitario> usuarios = new ArrayList<>();

        Academico admin = new Academico("12345678-9", "Admin Académico", "admin@unisiga.cl", "EMP001", "Planta");
        usuarios.add(admin);

        Estudiante est = new Estudiante("98765432-1", "Juan Estudiante", "juan@unisiga.cl", "MAT2024001", 2024, 5.5f);
        usuarios.add(est);

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginView login = new LoginView();
                new LoginController(login, usuarios);
                login.setVisible(true);
            }
        });
    }
}

