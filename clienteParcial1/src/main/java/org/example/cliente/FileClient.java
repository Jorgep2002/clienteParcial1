package org.example.cliente;

import org.example.Service.FileServiceClient;
import org.example.Service.UserServiceClient;
import org.example.shared.entities.DirectorioEntity;
import org.example.shared.entities.UserEntity;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class FileClient {
    private FileServiceClient serviceClient;

    public FileClient(String ip, String port, String serviceName) {
        this.serviceClient = new FileServiceClient(ip, port, serviceName);
    }

    public void uploadFileToUser(String filename, byte[] fileData, String ownerId, String  directorio) throws IOException {
        this.serviceClient.uploadFileToUser(filename,  fileData, ownerId,  directorio);
    }


    public List<DirectorioEntity> getFilesByUser(String userId) {
        try{
            return serviceClient.getFilesByUser(userId);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<DirectorioEntity> getUserFiles(String userId) {
        try{
            return serviceClient.getUsersFiles(userId);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<DirectorioEntity> getAllFiles() {
        try{
            return serviceClient.getAllfiles();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DirectorioEntity> searchDirectories(String query) {
        try{
            return serviceClient.searchDirectories(query);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
