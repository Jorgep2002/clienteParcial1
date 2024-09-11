package org.example.views;

import org.example.cliente.FileClient;
import org.example.cliente.UserClient;
import org.example.shared.entities.GroupEntity;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class AdminView extends JFrame {
    private JMenuBar menuBar;
    private JTextField searchBar;
    private JTree directoryTree;
    private FileClient fileClient;
    private UserClient userClient;

    public AdminView(UserClient userClient, FileClient fileClient) {
        this.fileClient = fileClient;
        this.userClient = userClient;

        // Configuración de la ventana principal
        this.setTitle("Admin Document Management System");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear el menú
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem uploadMenuItem = new JMenuItem("Upload File");
        fileMenu.add(uploadMenuItem);

        //Menú de grupos
        JMenu groupMenu = new JMenu("Grupos");
        //Btn para crear grupos
        JMenuItem createGroupMenuItem = new JMenuItem("Crear Grupo");
        groupMenu.add(createGroupMenuItem);

        //Añadir botones al menú
        menuBar.add(fileMenu);
        menuBar.add(groupMenu);
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

        DefaultMutableTreeNode file1 = new DefaultMutableTreeNode("File 1.txt");
        DefaultMutableTreeNode file2 = new DefaultMutableTreeNode("File 2.docx");
        DefaultMutableTreeNode file3 = new DefaultMutableTreeNode("File 3.pdf");

        folder1.add(file1);
        folder1.add(file2);
        folder2.add(file3);

        root.add(folder1);
        root.add(folder2);

        directoryTree = new JTree(root);
        JScrollPane treeScrollPane = new JScrollPane(directoryTree);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(treeScrollPane, BorderLayout.WEST);
        this.add(mainPanel);

        // Añadir listener para subir archivos
        uploadMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        byte[] fileData = convertFileToBytes(file);
                        fileClient.uploadFile(file.getName(), fileData);
                        JOptionPane.showMessageDialog(AdminView.this, "File uploaded successfully!");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(AdminView.this, "Failed to upload file: " + ex.getMessage());
                    }
                }
            }
        });


        //Crear grupos

        // Añadir listener para crear grupos
        createGroupMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCreateGroupDialog();
            }
        });

        this.setVisible(true);
    }


    private void openCreateGroupDialog() {
        JDialog groupDialog = new JDialog(this, "Crear Grupo", true);
        groupDialog.setLayout(new GridLayout(3, 2));

        JLabel nameLabel = new JLabel("Nombre del grupo:");
        JTextField nameField = new JTextField();

        JLabel descLabel = new JLabel("Descripción:");
        JTextField descField = new JTextField();

        JButton createButton = new JButton("Crear");
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String groupName = nameField.getText();
                String groupDesc = descField.getText();

                if (!groupName.isEmpty() && !groupDesc.isEmpty()) {
                    // Crear instancia de GroupEntity
                    GroupEntity group = new GroupEntity();
                    group.setName(groupName);
                    group.setDescription(groupDesc);

                    // Aquí puedes manejar la creación del grupo o enviarlo a un servicio para guardarlo
                    userClient.createGroup(groupName,groupDesc);
                    JOptionPane.showMessageDialog(groupDialog, "Grupo '" + group.getName() + "' creado con éxito.");
                    groupDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(groupDialog, "Todos los campos son obligatorios.");
                }
            }
        });

        groupDialog.add(nameLabel);
        groupDialog.add(nameField);
        groupDialog.add(descLabel);
        groupDialog.add(descField);
        groupDialog.add(new JLabel()); // Placeholder
        groupDialog.add(createButton);

        groupDialog.pack();
        groupDialog.setLocationRelativeTo(this);
        groupDialog.setVisible(true);
    }

    private byte[] convertFileToBytes(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] fileData = new byte[(int) file.length()];
        fileInputStream.read(fileData);
        fileInputStream.close();
        return fileData;
    }
}
