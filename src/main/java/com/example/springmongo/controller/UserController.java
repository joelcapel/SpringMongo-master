package com.example.springmongo.controller;

import com.example.springmongo.model.Coche;
import com.example.springmongo.model.User;
import com.example.springmongo.repositories.UserDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class UserController {
    UserDao userDao;
    CocheController cocheController;
    @Autowired
    public UserController(UserDao userDao, CocheController cocheController) {
        this.userDao = userDao;
        this.cocheController = cocheController;
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public User getUser(int id) {
        return userDao.findById(id).get();
    }

    public void addUser(User user) {
        List<Coche> coches = user.getCoches();
        cocheController.addAllCoches(coches);
        userDao.save(user);
    }

    public void deleteUser(int id) {
        User user = getUser(id);
        userDao.delete(user);
    }

    public void putUser(User user, int id) {

        User user1 = getUser(id);
        user1.setEmail(user.getEmail());
        user1.setName(user.getName());

        List<Coche> coches = user.getCoches();
        for (Coche c: user1.getCoches()){
            for (Coche cc: coches){
                if (c.getId() == cc.getId()){
                    c.setName(cc.getName());
                    c.setPrecio(cc.getPrecio());
                    c.setQuantity(cc.getQuantity());
                }
            }
        }
        cocheController.actualizarTodo(user.getCoches());

        userDao.save(user1);
    }

    public void patchUser(int id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        User user = getUser(id);
        User userPatched = applyPatch(patch, user);

        if (user.getCoches().size() == userPatched.getCoches().size()){
            cocheController.actualizarTodo(userPatched.getCoches());
        }else if (user.getCoches().size() < userPatched.getCoches().size()){
            cocheController.addAllCoches(userPatched.getCoches());
        }

        userDao.save(userPatched);

    }

    private User applyPatch(JsonPatch patch, User targetUser) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode patched = patch.apply(objectMapper.convertValue(targetUser, JsonNode.class));
        return objectMapper.treeToValue(patched, User.class);
    }

    public void addCoche(Coche coche, int id) {
        User u = getUser(id);
        cocheController.añadir(coche);
        Coche c = cocheController.getCoche(coche.getId());
        boolean find = false;
        for (Coche cc : u.getCoches()){
            if (cc.getId() == c.getId()) {
                find = true;
                break;
            }
        }
        if (!find){
            u.addProduct(c);
        }
        userDao.save(u);
    }

    public void deleteProductOnUser(int id, int index) {
        User u = getUser(id);
        u.getCoches().remove(index);
        userDao.save(u);
    }

}
    /*
    {
        "op":"replace",
        "path":"/coches/0/name",
        "value":"sdfasda"
    }
    {
         "op":"add",
        "path":"/coches/0",
        "value": {"id":10}
    }
    {
        "op":"remove",
        "path":"/id"
    }
    {
        "op":"move",
        "from":"/coches/0",
        "path":"/coches/4"
    }
    {
        "op":"copy",
        "from":"/coches/0",
        "path":"/coches/2"
    }
    {
        "op":"test",
        "path":"/id",
        "value":"fsdfdf"
    }
*/