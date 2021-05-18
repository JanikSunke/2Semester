package data.userHandling;

import Interfaces.IUser;
import Interfaces.IUserFacade;

import java.util.List;

public class UserFacade implements IUserFacade {
    private static final UserFacade USERFACADE = new UserFacade();

    @Override
    public List<IUser> getUsers() {
        return UserManager.getInstance().getUsers();
    }

    @Override
    public boolean makeUserProducer(IUser user) {
        return UserManager.getInstance().makeUserProducer(user);
    }

    @Override
    public boolean makeUserAdmin(IUser user) {
        return UserManager.getInstance().makeUserAdmin(user);
    }

    @Override
    public boolean deleteUser(IUser user) {
        return UserManager.getInstance().deleteUser(user);
    }

    @Override
    public boolean editUser(IUser user) {
        return UserManager.getInstance().editUser(user);
    }

    @Override
    public String getDatabasePassword(String username) {
        return UserManager.getInstance().getDatabasePassword(username);
    }

    public static UserFacade getInstance() {
        return USERFACADE;
    }
}