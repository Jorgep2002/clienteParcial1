package org.example.Service;

import org.example.cliente.UserClient;
import org.example.shared.RMIInterfaces.AuthService;
import org.example.shared.entities.GroupEntity;
import org.example.shared.entities.UserEntity;

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
    public boolean login(UserEntity user) throws RemoteException {
        return service.login(user);
    }

    public boolean createGroup(String groupName, String groupDescription, UserEntity user) throws RemoteException {
        return service.createGroup(groupName, groupDescription, user);
    }

    public List<GroupEntity> getAllGroups() throws RemoteException {
        return service.getAllGroups();
    }

    public List<UserEntity> getUsersByGroupId(int groupId) throws RemoteException {
        return service.getUsersByGroupId(groupId);
    }

    public boolean addUserToGroup(int groudId,String userId) throws RemoteException {
        return service.addUserToGroup(groudId, userId);
    }

    public List<UserEntity> getAllUsers() throws RemoteException {
        return service.getAllUsers();
    }
    public List<GroupEntity> getGroupsByUserId(String userId) throws RemoteException{
        return service.getGroupsByUserId(userId);
    }
    public boolean createUser(UserEntity user) throws RemoteException{
        return service.createUser(user);
    }

}
