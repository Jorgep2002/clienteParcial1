package org.example.cliente;

import org.example.Service.UserServiceClient;
import org.example.shared.entities.GroupEntity;
import org.example.shared.entities.UserEntity;

import java.rmi.RemoteException;
import java.util.List;

public class UserClient {
    private UserServiceClient serviceClient;

    public UserClient(String ip, String port, String serviceName) {
        this.serviceClient = new UserServiceClient(ip, port, serviceName);
    }

    public List<GroupEntity> getAllGroups() {
        try{
            return serviceClient.getAllGroups();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
    }
    }

    public List<UserEntity> getUsersByGroupId(int groupId) {
        try{
            return serviceClient.getUsersByGroupId(groupId);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addUserToGroup(int groupId, String userId) {
        try{
            return serviceClient.addUserToGroup(groupId, userId);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public List<GroupEntity> getGroupsByUserId(String userId){
        try{
            return serviceClient.getGroupsByUserId(userId);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean login(UserEntity user) {
        try {
            return serviceClient.login(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean createGroup(String groupName, String groupDescription, UserEntity user) {
        try {
            return serviceClient.createGroup(groupName, groupDescription, user);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<UserEntity> getAllUsers() {
        try{
            return serviceClient.getAllUsers();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean createUser(UserEntity user) {
        try{
            return serviceClient.createUser(user);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
