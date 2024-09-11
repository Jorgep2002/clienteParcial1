package org.example.views;

import org.example.cliente.FileClient;
import org.example.cliente.UserClient;
import org.example.shared.entities.GroupEntity;
import org.example.shared.entities.UserEntity;

import java.util.List;

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
    private JPanel mainPanel; // Panel principal
    private JPanel rightPanel; // Panel derecho donde mostraremos los grupos


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
        JMenuItem createGroupsMenuItem = new JMenuItem("Crear Grupo");
        groupMenu.add(createGroupsMenuItem);

        ///----VER GRUPOS BOTON
        JMenuItem watchGroupsMetuItems = new JMenuItem("Ver Grupo");
        groupMenu.add(watchGroupsMetuItems);


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

        // Panel derecho donde se mostrarán los grupos y miembros
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        JScrollPane rightScrollPane = new JScrollPane(rightPanel);

        //Agregar los paneles a la vista
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(treeScrollPane, BorderLayout.WEST);
        mainPanel.add(rightScrollPane, BorderLayout.CENTER);

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
        createGroupsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCreateGroupDialog();
            }
        });

        this.setVisible(true);

        watchGroupsMetuItems.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGroups();
            }
        });

    }
    //Función para mostrar los grupos
    private void loadGroups() {
        rightPanel.removeAll(); // Limpiar el panel derecho

        List<GroupEntity> groups = userClient.getAllGroups(); // Obtener todos los grupos
        for (GroupEntity group : groups) {
            JPanel groupPanel = new JPanel();
            groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
            System.out.println(group.getId());
            groupPanel.setBorder(BorderFactory.createTitledBorder(group.getName()));

            JLabel groupDescLabel = new JLabel("Descripción: " + group.getDescription());
            JButton viewMembersButton = new JButton("Ver Miembros");

            // Añadir funcionalidad para ver y gestionar miembros
            viewMembersButton.addActionListener(e -> openMembersDialog(group));

            groupPanel.add(groupDescLabel);
            groupPanel.add(viewMembersButton);
            rightPanel.add(groupPanel);
        }

        rightPanel.revalidate();
        rightPanel.repaint();
    }
    //Vista usuarios
    private void openMembersDialog(GroupEntity group) {
        System.out.println(group);
        JDialog membersDialog = new JDialog(this, "Miembros de " + group.getName(), true);
        membersDialog.setLayout(new BorderLayout());

        JPanel membersPanel = new JPanel();
        membersPanel.setLayout(new BoxLayout(membersPanel, BoxLayout.Y_AXIS));

//         Obtener miembros del grupo (supongo que tienes una función en UserClient para esto)
        List<UserEntity> members = userClient.getUsersByGroupId(group.getId());
        System.out.println(members);

        for (UserEntity member : members) {
            JPanel memberPanel = new JPanel();
            memberPanel.setLayout(new BorderLayout());

            JLabel memberLabel = new JLabel(member.getId());
            JButton removeButton = new JButton("Eliminar");

            // Añadir listener para eliminar miembro
//            removeButton.addActionListener(e -> {
//                userClient.removeMember(group.getId(), member);
//                membersDialog.dispose();
//                openMembersDialog(group); // Recargar la lista de miembros
//            });

            memberPanel.add(memberLabel, BorderLayout.CENTER);
            memberPanel.add(removeButton, BorderLayout.EAST);
            membersPanel.add(memberPanel);
        }

        JButton addButton = new JButton("Agregar Miembro");
        addButton.addActionListener(e -> {
            String newMember = JOptionPane.showInputDialog(membersDialog, "Ingrese el nombre del nuevo miembro:");
            if (newMember != null && !newMember.trim().isEmpty()) {
                userClient.addUserToGroup(group.getId(), newMember);
                membersDialog.dispose();
                openMembersDialog(group); // Recargar la lista de miembros
            }
        });

        membersDialog.add(new JScrollPane(membersPanel), BorderLayout.CENTER);
        membersDialog.add(addButton, BorderLayout.SOUTH);

        membersDialog.setSize(300, 400);
        membersDialog.setLocationRelativeTo(this);
        membersDialog.setVisible(true);
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