package org.example;

import org.example.cliente.FileClient;
import org.example.cliente.UserClient;
import org.example.views.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {


        // Instancia los clientes para ambos servicios
        UserClient userClient = new UserClient("localhost", "6803", "AuthService");
        FileClient fileClient = new FileClient("localhost", "6802", "fileService");


        // Configura la interfaz gráfica para el login
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(userClient, fileClient);
            loginFrame.setVisible(true);
        });


    }
}