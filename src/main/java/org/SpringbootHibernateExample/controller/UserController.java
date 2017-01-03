package org.SpringbootHibernateExample.controller;

import org.SpringbootHibernateExample.Utils.UserException;
import org.SpringbootHibernateExample.model.User;
import org.SpringbootHibernateExample.model.UserDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<User>> findAllUser() throws UserException{
        List<User> list = null;
        list = userDao.findAll();
        if(list == null) {
            return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<User>>(list,HttpStatus.OK);
    }

    /**
     * /create  --> Create a new user and save it in the database.
     *
     * @return A string describing if the user is succesfully created or not.
     */
    @RequestMapping(value = "/create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(@RequestBody User requestUser) throws UserException {
        User user = null;
        user = userDao.save(requestUser);
        return new ResponseEntity<String>("User succesfully created! (id = " + user.getId() + ")", HttpStatus.OK);
    }

    /**
     * /delete  --> Delete the user having the passed id.
     *
     * @param id The id of the user to delete
     * @return A string describing if the user is succesfully deleted or not.
     */
    @RequestMapping(value = "/delete/{id}",
            method = RequestMethod.DELETE)
    public HttpEntity<String> delete(@PathVariable("id") long id) throws UserException {
        User user = new User(id);
        userDao.delete(user);
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }

    /**
     * /get-by-email  --> Return the id for the user having the passed email.
     *
     * @param email The email to search in the database.
     * @return The user id or a message error if the user is not found.
     */
    @RequestMapping(value = "/get-by-email/{email:.+}",
            method = RequestMethod.GET)
    public ResponseEntity<String> getByEmail(@PathVariable("email") String email) throws UserException {
        String userId;
        User user = userDao.findByEmail(email);
        userId = String.valueOf(user.getId());
        return new ResponseEntity<String>("The user id is: " + userId, HttpStatus.OK);
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
    public ResponseEntity<String> updateUser(@RequestBody User requestUser) throws UserException {
        User user = userDao.findOne(requestUser.getId());
        user.setEmail(requestUser.getEmail());
        user.setName(requestUser.getName());
        userDao.save(user);
        return new ResponseEntity<String>("User succesfully updated!", HttpStatus.ACCEPTED);
    }

    // ------------------------
    // PRIVATE FIELDS
    // ------------------------

    @Autowired
    private UserDao userDao;

} // class UserController