package models;

import java.util.List;

/**
 * @author hhh23 and pdp83
 * Admin Subsystem implementation
 */

/**
 * Other notes
 * needs to create a file somewhere with all users so it can load on start
 */

public class Admin {
    private List<User> users;

    /**
     * constructor loads the "users" array list by calling loadUsers()
     */
    public Admin(){
        loadUsers();
    }

    /**
     * loads the "users" array list
     * creates/updates a file
     */
    private void loadUsers(){
        // some file stuff; refer to music lib program
    }

    /**
     * creates a user
     * @param name name of the new user
     */
    public void createUser(String name){
        users.add(new User(name));
    }

    /**
     * deletes a user
     * @param name name of the user to delete
     */
    public void deleteUser(String name){
        int i = 0;
        while(!users.get(i).equals(name)){
            i++;
        }
        users.remove(i);
    }


}
