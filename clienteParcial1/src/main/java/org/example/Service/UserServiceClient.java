package org.example.Service;

import org.example.shared.RMIInterfaces.AuthService;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

public class UserServiceClient {
    private AuthService service;

    public UserServiceClient(String ip, String port, String serviceName) {
        try {
            String url = "rmi://" + ip + ":" + port + "/" + serviceName;
            this.service = (AuthService) Naming.lookup(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para autenticar un usuario
    public boolean login(String username, String password) throws RemoteException {
        return service.login(username, password);
    }
}
