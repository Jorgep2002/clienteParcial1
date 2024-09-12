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
    private UserEntity actualUser
            ;

    private FileClient fileClient;
    private UserClient userClient;

    public AdminView(UserClient userClient, FileClient fileClient, UserEntity actualUser) {
        this.fileClient = fileClient;
        this.userClient = userClient;
        this.actualUser = actualUser;

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


        //Menú Usuarios
        JMenu usersMenu = new JMenu("Usuarios");
        //Btn para ver usuarios
        JMenuItem verUsuariosMenuItem = new JMenuItem("ver usuarios ");
        usersMenu.add(verUsuariosMenuItem);
        //Btn para crear usuarios
        JMenuItem crearUsuario = new JMenuItem("crear usuario ");
        usersMenu.add(crearUsuario);

        //Añadir botones al menú
        menuBar.add(fileMenu);
        menuBar.add(groupMenu);
        menuBar.add(usersMenu);
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


        //--Grupos

        // Añadir listener para crear grupos
        createGroupsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCreateGroupDialog();
            }
        });

        this.setVisible(true);
        //Listener para ver los grupos
        watchGroupsMetuItems.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGroups();
            }
        });

        //--Usuarios
        //Listener para ver los usuarios
        verUsuariosMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadUsers();
            }
        });
        crearUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCreateUserDialog();
            }
        });



    }
    //Función para mostrar los usuarios
    private void loadUsers() {
        rightPanel.removeAll(); // Limpiar el panel derecho

        rightPanel.setLayout(new GridBagLayout()); // Usar GridBagLayout para un diseño centrado
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Añadir margen alrededor de los elementos
        gbc.anchor = GridBagConstraints.CENTER; // Centrar el contenido
        gbc.fill = GridBagConstraints.HORIZONTAL; // Hacer que los paneles ocupen todo el ancho

        List<UserEntity> users = userClient.getAllUsers(); // Obtener todos los usuarios

        for (UserEntity user : users) {
            JPanel userPanel = new JPanel();
            userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
            userPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.GRAY),
                    "Usuario: " + user.getId(),
                    0, 0, null, Color.BLUE // Personalizar el borde y el color del título
            ));
            userPanel.setBackground(Color.WHITE); // Fondo blanco para cada panel

            // Añadir nombre de usuario
            JLabel userLabel = new JLabel("Nombre del usuario: " + user.getId());
            userLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT); // Centrar el texto
            userPanel.add(userLabel);

            // Obtener los grupos del usuario
            List<GroupEntity> groups = userClient.getGroupsByUserId(user.getId());
            if (!groups.isEmpty()) {
                JLabel groupsLabel = new JLabel("Grupos:");
                groupsLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT); // Centrar el texto
                userPanel.add(groupsLabel);

                for (GroupEntity group : groups) {
                    JLabel groupLabel = new JLabel(" - " + group.getName());
                    groupLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT); // Centrar el texto
                    userPanel.add(groupLabel);
                }
            } else {
                JLabel noGroupsLabel = new JLabel("El usuario no pertenece a ningún grupo.");
                noGroupsLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT); // Centrar el texto
                userPanel.add(noGroupsLabel);
            }

            // Añadir el panel de usuario al panel derecho
            gbc.gridx = 0; // Siempre en la columna 0
            gbc.gridy = GridBagConstraints.RELATIVE; // Colocarlo debajo del último componente agregado
            gbc.weightx = 1.0; // Ocupa todo el ancho disponible
            gbc.fill = GridBagConstraints.HORIZONTAL; // Estirar el panel horizontalmente

            rightPanel.add(userPanel, gbc);
        }

        // Asegurarse de que los cambios se reflejen en la interfaz
        rightPanel.revalidate();
        rightPanel.repaint();
    }
    //Crear usuarios
    private void openCreateUserDialog() {
        JDialog groupDialog = new JDialog(this, "Crear Usuario", true);
        groupDialog.setLayout(new GridLayout(3, 2));

        JLabel idLabel = new JLabel("Nombre del Usuario:");
        JTextField idField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JTextField passwordField = new JTextField();

        JButton createButton = new JButton("Crear");
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = idField.getText();
                String userPass = passwordField.getText();

                if (!userId.isEmpty() && !userPass.isEmpty()) {
                    // Crear instancia de GroupEntity
                    UserEntity User = new UserEntity(
                        userId,
                        userPass
                    );

                    // Aquí puedes manejar la creación del grupo o enviarlo a un servicio para guardarlo
                    userClient.createUser(User);
                    JOptionPane.showMessageDialog(groupDialog, "User'" + User.getId() + "' creado con éxito.");
                    groupDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(groupDialog, "Todos los campos son obligatorios.");
                }
            }
        });

        groupDialog.add(idLabel);
        groupDialog.add(idField);
        groupDialog.add(passwordLabel);
        groupDialog.add(passwordField);
        groupDialog.add(new JLabel()); // Placeholder
        groupDialog.add(createButton);

        groupDialog.pack();
        groupDialog.setLocationRelativeTo(this);
        groupDialog.setVisible(true);
    }
    //Función para mostrar los grupos
    private void loadGroups() {
        rightPanel.removeAll(); // Limpiar el panel derecho

        rightPanel.setLayout(new GridBagLayout()); // Usar GridBagLayout para un diseño centrado
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Añadir margen alrededor de los elementos
        gbc.anchor = GridBagConstraints.CENTER; // Centrar el contenido
        gbc.fill = GridBagConstraints.HORIZONTAL; // Hacer que los paneles ocupen todo el ancho

        List<GroupEntity> groups = userClient.getAllGroups(); // Obtener todos los grupos

        for (GroupEntity group : groups) {
            JPanel groupPanel = new JPanel();
            groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
            groupPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.GRAY),
                    group.getName(),
                    0, 0, null, Color.BLUE // Personalizar el borde y el color del título
            ));
            groupPanel.setBackground(Color.WHITE); // Fondo blanco para cada panel

            // Añadir descripción del grupo
            JLabel groupDescLabel = new JLabel("Descripción: " + group.getDescription());
            groupDescLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT); // Centrar el texto
            groupPanel.add(groupDescLabel);

            // Botón para ver miembros del grupo
            JButton viewMembersButton = new JButton("Ver Miembros");
            viewMembersButton.setAlignmentX(JButton.CENTER_ALIGNMENT); // Centrar el botón

            // Añadir funcionalidad al botón
            viewMembersButton.addActionListener(e -> openMembersDialog(group));
            groupPanel.add(viewMembersButton);

            // Añadir el panel del grupo al panel derecho
            gbc.gridx = 0; // Siempre en la columna 0
            gbc.gridy = GridBagConstraints.RELATIVE; // Colocarlo debajo del último componente agregado
            gbc.weightx = 1.0; // Ocupa todo el ancho disponible
            gbc.fill = GridBagConstraints.HORIZONTAL; // Estirar el panel horizontalmente

            rightPanel.add(groupPanel, gbc);
        }

        // Asegurarse de que los cambios se reflejen en la interfaz
        rightPanel.revalidate();
        rightPanel.repaint();
    }
    //Vista usuarios
    private void openMembersDialog(GroupEntity group) {
        // Crear el diálogo modal
        JDialog membersDialog = new JDialog(this, "Miembros de " + group.getName(), true);
        membersDialog.setLayout(new BorderLayout());

        // Crear el panel de miembros con BoxLayout para apilar los miembros verticalmente
        JPanel membersPanel = new JPanel();
        membersPanel.setLayout(new BoxLayout(membersPanel, BoxLayout.Y_AXIS));
        membersPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Añadir margen

        // Obtener los miembros del grupo
        List<UserEntity> members = userClient.getUsersByGroupId(group.getId());
        System.out.println(members);

        // Crear un cuadro para cada miembro que ocupe todo el ancho pero no todo el largo
        for (UserEntity member : members) {
            // Crear un panel para cada miembro con BorderLayout
            JPanel memberPanel = new JPanel(new BorderLayout());
            memberPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Margen entre los cuadros de miembros
            memberPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // Ocupar todo el ancho, pero limitar el largo

            JLabel memberLabel = new JLabel("Usuario: " + member.getId());
            JButton removeButton = new JButton("Eliminar");

            // Añadir listener para eliminar miembro
//            removeButton.addActionListener(e -> {
//                userClient.removeMember(group.getId(), member);
//                membersDialog.dispose(); // Cerrar el diálogo
//                openMembersDialog(group); // Recargar la lista de miembros
//            });

            // Añadir el nombre del miembro a la izquierda (CENTER)
            memberPanel.add(memberLabel, BorderLayout.CENTER);
            // Añadir el botón de eliminar a la derecha (EAST)
            memberPanel.add(removeButton, BorderLayout.EAST);

            // Añadir el panel del miembro al panel principal
            membersPanel.add(memberPanel);
        }

        // Botón para agregar un nuevo miembro
        JButton addButton = new JButton("Agregar Miembro");
        addButton.addActionListener(e -> {
            String newMember = JOptionPane.showInputDialog(membersDialog, "Ingrese el nombre del nuevo miembro:");
            if (newMember != null && !newMember.trim().isEmpty()) {
                userClient.addUserToGroup(group.getId(), newMember);
                membersDialog.dispose();
                openMembersDialog(group); // Recargar la lista de miembros
            }
        });

        // Añadir los paneles al diálogo
        membersDialog.add(new JScrollPane(membersPanel), BorderLayout.CENTER); // Añadir scroll si hay muchos miembros
        membersDialog.add(addButton, BorderLayout.SOUTH);

        // Configurar el tamaño y la posición del diálogo
        membersDialog.setSize(400, 500);
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


                    // Aquí puedes manejar la creación del grupo o enviarlo a un servicio para guardarlo
                    userClient.createGroup(groupName,groupDesc, actualUser);

                    JOptionPane.showMessageDialog(groupDialog, "Grupo '" + groupName + "' creado con éxito.");
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