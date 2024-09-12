package org.example.cliente;

import org.example.Service.FileServiceClient;
import org.example.Service.UserServiceClient;
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

    public  void uploadFile(String filename, byte[] fileData) throws RemoteException, IOException {

            serviceClient.uploadFile(filename, fileData);
    }


    public List<String> getFilesByUser(String userId) {
        try{
            return serviceClient.getFilesByUser(userId);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
