package org.example.Service;

import org.example.shared.RMIInterfaces.AuthService;
import org.example.shared.RMIInterfaces.FileService;
import org.example.shared.entities.DirectorioEntity;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.SQLException;
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

    public void uploadFileToUser(String filename, byte[] fileData, String ownerId, String directorio) throws RemoteException, IOException {
         service.uploadFileToUser(filename,  fileData, ownerId,  directorio);
    }

    public List<DirectorioEntity> getFilesByUser(String userId) throws RemoteException, SQLException{
        return service.getFilesByUser(userId);
    }
    public List<DirectorioEntity> getUsersFiles(String userId) throws RemoteException, SQLException{
        return service.getUsersFiles(userId);
    }

    public List<DirectorioEntity> getAllfiles() throws RemoteException, SQLException{
        return service.getALLFiles();
    }
    public List<DirectorioEntity> searchDirectories(String query) throws RemoteException, SQLException{
        return service.searchDirectories(query);
    }
}
