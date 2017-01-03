package org.SpringbootHibernateExample.controller;

import org.SpringbootHibernateExample.model.User;
import org.SpringbootHibernateExample.model.UserDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A class to test interactions with the MySQL database using the UserDao class.
 *
 */
@RestController
public class UserController {

    // ------------------------
    // PUBLIC METHODS
    // ------------------------

    /**
     * /users  --> Return a list of users from the database
     *
     * @return A list of user from the databases
     */
    @RequestMapping(value = "/users",method = RequestMethod.GET)
    public List<User> findAllUser() {
        List<User> list = null;
        list = userDao.findAll();
        return list;
    }

    /**
     * /create  --> Create a new user and save it in the database.
     *
     * @return A string describing if the user is succesfully created or not.
     */
    @RequestMapping(value="/create",
            method= RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public String create(@RequestBody User requestUser) {
        User user = null;
        try {
            user = userDao.save(requestUser);
        }
        catch (Exception ex) {
            return "Error creating the user: " + ex.toString();
        }
        return "User succesfully created! (id = " + user.getId() + ")";
    }

    /**
     * /delete  --> Delete the user having the passed id.
     *
     * @param id The id of the user to delete
     * @return A string describing if the user is succesfully deleted or not.
     */
    @RequestMapping(value = "/delete/{id}",
                    method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") long id) {
        try {
            User user = new User(id);
            userDao.delete(user);
        }
        catch (Exception ex) {
            return "Error deleting the user: " + ex.toString();
        }
        return "User succesfully deleted!";
    }

    /**
     * /get-by-email  --> Return the id for the user having the passed email.
     *
     * @param email The email to search in the database.
     * @return The user id or a message error if the user is not found.
     */
    @RequestMapping(value = "/get-by-email/{email:.+}",
                    method = RequestMethod.GET)
    public String getByEmail(@PathVariable("email") String email) {
        String userId;
        try {
            User user = userDao.findByEmail(email);
            userId = String.valueOf(user.getId());
        }
        catch (Exception ex) {
            return "User not found";
        }
        return "The user id is: " + userId;
    }

    /**
     * /update  --> Update the email and the name for the user in the database
     * having the passed id.
     *
     * @return A string describing if the user is succesfully updated or not.
     */
    @RequestMapping(value = "/update",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public String updateUser(@RequestBody User requestUser) {
        try {
            User user = userDao.findOne(requestUser.getId());
            user.setEmail(requestUser.getEmail());
            user.setName(requestUser.getName());
            userDao.save(user);
        }
        catch (Exception ex) {
            return "Error updating the user: " + ex.toString();
        }
        return "User succesfully updated!";
    }

    // ------------------------
    // PRIVATE FIELDS
    // ------------------------

    @Autowired
    private UserDao userDao;

} // class UserController