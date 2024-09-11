package org.example.Service;

import org.example.shared.RMIInterfaces.AuthService;
import org.example.shared.RMIInterfaces.FileService;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

public class FileServiceClient {
    private FileService service;

    public FileServiceClient(String ip, String port, String serviceName) {
        try {
            String url = "rmi://" + ip + ":" + port + "/" + serviceName;
            this.service = (FileService) Naming.lookup(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para autenticar un usuario
    public  void uploadFile(String filename, byte[] fileData) throws RemoteException, IOException {
         service.uploadFile(filename, fileData);
    }
}
