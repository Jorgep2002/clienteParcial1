package org.example.cliente;

import org.example.Service.UserServiceClient;

import java.rmi.RemoteException;
import java.util.List;

public class UserClient {
    private UserServiceClient serviceClient;

    public UserClient(String ip, String port, String serviceName) {
        this.serviceClient = new UserServiceClient(ip, port, serviceName);
    }



    public boolean login(String username, String password) {
        try {
            return serviceClient.login(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean createGroup(String groupName, String groupDescription) {
        try {
            return serviceClient.createGroup(groupName, groupDescription);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

}
