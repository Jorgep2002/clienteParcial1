package org.example.views;

import org.example.cliente.FileClient;
import org.example.shared.entities.UserEntity;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UserView extends JFrame {
    private JMenuBar menuBar;
    private JTextField searchBar;
    private JTree directoryTree;
    private FileClient fileClient;
    private UserEntity actualUser;
    private List<String> filePaths; // Variable para almacenar las rutas de archivos
    private String selectedFolderPath; // Variable global para almacenar la carpeta seleccionada
    private JPanel fileDetailPanel; // Panel para mostrar detalles del archivo

    public UserView(FileClient fileClient, UserEntity actualUser) {
        this.fileClient = fileClient;
        this.actualUser = actualUser;

        // Inicializar las rutas de archivos
        initializeFilePaths();

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

        // Crear el árbol de directorios dinámicamente a partir de las rutas
        DefaultMutableTreeNode root = createDirectoryTree(filePaths);
        directoryTree = new JTree(root);
        JScrollPane treeScrollPane = new JScrollPane(directoryTree);

        // Ajustar el ancho del JScrollPane que contiene el JTree
        treeScrollPane.setPreferredSize(new Dimension(300, getHeight())); // Ajusta el ancho aquí

        // Crear el panel para mostrar detalles del archivo
        fileDetailPanel = new JPanel();
        fileDetailPanel.setLayout(new BoxLayout(fileDetailPanel, BoxLayout.Y_AXIS));
        JScrollPane detailScrollPane = new JScrollPane(fileDetailPanel);

        // Añadir listener para capturar la selección de nodos en el árbol
        directoryTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) directoryTree.getLastSelectedPathComponent();
                if (selectedNode != null) {
                    // Obtener la ruta completa del nodo seleccionado
                    selectedFolderPath = getFullPath(selectedNode);
                    System.out.println("Carpeta seleccionada: " + selectedFolderPath);

                    // Actualizar el panel de detalles del archivo si es un archivo
                    if (selectedNode.isLeaf()) {
                        String fileName = selectedNode.toString();
                        showFileDetails(fileName);
                    } else {
                        fileDetailPanel.removeAll();
                        fileDetailPanel.add(new JLabel("Selecciona un archivo para ver detalles."));
                    }
                }
            }
        });

        // Añadir componentes al panel principal
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(treeScrollPane, BorderLayout.WEST);
        mainPanel.add(detailScrollPane, BorderLayout.CENTER);

        // Añadir el panel principal a la ventana
        this.add(mainPanel);

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

    // Método para construir el árbol de directorios a partir de las rutas de archivo
    private DefaultMutableTreeNode createDirectoryTree(List<String> filePaths) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Empresa");
        Map<String, DefaultMutableTreeNode> nodeMap = new TreeMap<>();

        for (String path : filePaths) {
            // Reemplazar barras invertidas por barras normales
            path = path.replace("\\", "/");

            String[] parts = path.split("/");
            DefaultMutableTreeNode currentNode = root;

            String currentPath = "";
            for (String part : parts) {
                currentPath += "/" + part;

                // Si el nodo para el directorio actual no existe, se crea
                if (!nodeMap.containsKey(currentPath)) {
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(part);
                    currentNode.add(newNode);
                    nodeMap.put(currentPath, newNode);
                }
                currentNode = nodeMap.get(currentPath);
            }
        }

        return root;
    }

    private void initializeFilePaths() {
        // Obtener la lista de archivos usando el método getFilesByUser
        this.filePaths = getFilesByUser(actualUser.getId());
    }

    public List<String> getFilesByUser(String userId) {
        try {
            return fileClient.getFilesByUser(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para obtener la ruta completa de un nodo seleccionado en el árbol
    private String getFullPath(DefaultMutableTreeNode node) {
        StringBuilder path = new StringBuilder(node.toString());
        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
        while (parentNode != null) {
            path.insert(0, parentNode.toString() + "/");
            parentNode = (DefaultMutableTreeNode) parentNode.getParent();
        }
        return path.toString();
    }

    // Método para mostrar detalles del archivo (icono y nombre) en el panel
    private void showFileDetails(String fileName) {
        fileDetailPanel.removeAll();

        // Aquí puedes agregar el icono del archivo. Usamos un icono genérico por ahora.
        JLabel fileNameLabel = new JLabel("Nombre del archivo: " + fileName);
        fileDetailPanel.add(fileNameLabel);

        // Puedes agregar más detalles aquí, como el icono del archivo
        ImageIcon fileIcon = (ImageIcon) UIManager.getIcon("FileView.fileIcon");
        JLabel iconLabel = new JLabel(fileIcon);
        fileDetailPanel.add(iconLabel);

        // Actualizar la vista del panel
        fileDetailPanel.revalidate();
        fileDetailPanel.repaint();
    }
}
