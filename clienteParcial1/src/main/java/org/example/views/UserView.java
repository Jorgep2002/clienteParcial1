package org.example.views;

import org.example.cliente.FileClient;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class UserView extends JFrame {
    private JMenuBar menuBar;
    private JTextField searchBar;
    private JTree directoryTree;
    private FileClient fileClient;

    public UserView(FileClient fileClient) {
        this.fileClient = fileClient;

        // Configuración de la ventana principal
        this.setTitle("Document Management System");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear el menú
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem uploadMenuItem = new JMenuItem("Upload File");
        fileMenu.add(uploadMenuItem);
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);

        // Crear el panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Crear el panel de búsqueda
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchBar = new JTextField(20);
        searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        searchPanel.add(searchBar, BorderLayout.CENTER);

        // Crear el árbol de directorios con nodos predeterminados
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        DefaultMutableTreeNode folder1 = new DefaultMutableTreeNode("Folder 1");
        DefaultMutableTreeNode folder2 = new DefaultMutableTreeNode("Folder 2");

        // Agregar archivos dentro de las carpetas
        DefaultMutableTreeNode file1 = new DefaultMutableTreeNode("File 1.txt");
        DefaultMutableTreeNode file2 = new DefaultMutableTreeNode("File 2.docx");
        DefaultMutableTreeNode file3 = new DefaultMutableTreeNode("File 3.pdf");

        // Añadir los archivos a las carpetas
        folder1.add(file1);
        folder1.add(file2);
        folder2.add(file3);

        // Añadir carpetas al nodo raíz
        root.add(folder1);
        root.add(folder2);

        // Crear el árbol de directorios
        directoryTree = new JTree(root);
        JScrollPane treeScrollPane = new JScrollPane(directoryTree);

        // Añadir componentes al panel principal
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(treeScrollPane, BorderLayout.WEST);

        // Añadir el panel principal a la ventana
        this.add(mainPanel);

        // Añadir listener al menú "Upload File"
        uploadMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        // Convertir el archivo a un array de bytes
                        byte[] fileData = convertFileToBytes(file);

                        // Subir el archivo mediante el cliente remoto
                        fileClient.uploadFile(file.getName(), fileData);

                        JOptionPane.showMessageDialog(UserView.this, "File uploaded successfully!");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(UserView.this, "Failed to upload file: " + ex.getMessage());
                    }
                }
            }
        });

        // Hacer visible la ventana
        this.setVisible(true);
    }

    // Método para convertir un archivo a un array de bytes
    private byte[] convertFileToBytes(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] fileData = new byte[(int) file.length()];
        fileInputStream.read(fileData);
        fileInputStream.close();
        return fileData;
    }
}
