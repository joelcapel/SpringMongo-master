package com.example.springmongo.controller;

import com.example.springmongo.model.Coche;
import com.example.springmongo.repositories.CocheDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CocheController {
    CocheDao cocheDao;


    @Autowired
    public CocheController(CocheDao cocheDao) {
        this.cocheDao = cocheDao;
    }

    public List<Coche> getAllCoches() {
        return cocheDao.findAll();
    }

    public void addAllCoches(List<Coche> coches) {
        cocheDao.saveAll(coches);
    }

    public void añadir(Coche coche){
        if (!cocheDao.existsById(coche.getId())){
            cocheDao.save(coche);
        }
    }

    public Coche getCoche(int id){
        return cocheDao.findById(id).get();
    }

    public void deleteById(int id){
        cocheDao.deleteById(id);
    }

    public void actualizar(int id, Coche coche) {
        Coche user = getCoche(id);

        user.setName(coche.getName());
        user.setPrecio(coche.getPrecio());
        user.setQuantity(coche.getQuantity());

        cocheDao.save(user);
    }

    public void actualizarTodo(List<Coche> coches){
        for (Coche c : getAllCoches()){
            for (Coche cc: coches){
                if (c.getId() == cc.getId()){
                    c.setName(cc.getName());
                    c.setPrecio(cc.getPrecio());
                    c.setQuantity(cc.getQuantity());
                    cocheDao.save(c);
                }
            }
        }
    }

    public void patchCoche(int id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        Coche coche = getCoche(id);
        Coche cochePatched = applyPatch(patch, coche);

        cocheDao.save(cochePatched);

    }

    private Coche applyPatch(JsonPatch patch, Coche targetCoche) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode patched = patch.apply(objectMapper.convertValue(targetCoche, JsonNode.class));
        return objectMapper.treeToValue(patched, Coche.class);
    }

}
