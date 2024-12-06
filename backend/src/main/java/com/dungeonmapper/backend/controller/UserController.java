package com.dungeonmapper.backend.controller;

import com.dungeonmapper.backend.entity.User;
import com.dungeonmapper.backend.exceptions.DuplicateException;
import com.dungeonmapper.backend.exceptions.NotFoundException;
import com.dungeonmapper.backend.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log
@RestController
@RequestMapping("api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/user", produces = "application/json")
    public ResponseEntity<User> apiCreateUser(@RequestBody User user) throws DuplicateException {
        String email = user.getEmail();
        if (userService.getUserByEmail(email) != null) {
            String errMsg = String.format("An user with email %s already exists!", email);
            throw new DuplicateException(errMsg);
        }
        User newUser = userService.createUser(user);
        return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }

    @GetMapping(value = "/user", produces = "application/json")
    public ResponseEntity<List<User>> apiGetAllUsers() {
        List<User> foundUsers = userService.getAllUsers();
        return new ResponseEntity<List<User>>(foundUsers, HttpStatus.FOUND);
    }

    @SneakyThrows
    @GetMapping(value = "/user/{uid}", produces = "application/json")
    public ResponseEntity<User> apiGetUserById(@PathVariable("uid") Long id) throws NotFoundException {
        User foundUser = userService.getUserById(id);
        if (foundUser == null) {
            String errMsg = String.format("The user with id %s does not exist!", id);
            throw new NotFoundException(errMsg);
        }
        return new ResponseEntity<User>(foundUser, HttpStatus.FOUND);
    }

    @SneakyThrows
    @GetMapping(value = "/user/email/{email}", produces = "application/json")
    public ResponseEntity<User> apiGetUserByEmail(@PathVariable("email") String email) throws NotFoundException {
        User foundUser = userService.getUserByEmail(email);
        if (foundUser == null) {
            String errMsg = String.format("The user with email %s does not exist!", email);
            throw new NotFoundException(errMsg);
        }
        return new ResponseEntity<User>(foundUser, HttpStatus.FOUND);
    }

    @SneakyThrows
    @PutMapping(value = "/user/{id}", produces = "application/json")
    public ResponseEntity<User> apiUpdateUser(@PathVariable("id") Long id, @RequestBody User user) throws NotFoundException {
        User userToUpdate = this.userService.getUserById(id);
        if (userToUpdate == null) {
            String errMsg = String.format("The user with id %s does not exist!", id);
            throw new NotFoundException(errMsg);
        }
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPassword(user.getPassword());
        return new ResponseEntity<User>(userToUpdate, HttpStatus.OK);
    }

    @SneakyThrows
    @DeleteMapping(value = "/user/{id}", produces = "application/json")
    public ResponseEntity<User> apiDeleteUser(@PathVariable("id") Long id) throws NotFoundException {
        User userToDelete = this.userService.getUserById(id);
        if (userToDelete == null) {
            String errMsg = String.format("The user with id %s does not exist!", id);
            throw new NotFoundException(errMsg);
        }
        this.userService.deleteUser(id);
        return new ResponseEntity<User>(userToDelete, HttpStatus.OK);
    }

}
