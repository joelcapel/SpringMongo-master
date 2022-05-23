package com.example.springmongo.resources;

import com.example.springmongo.controller.CocheController;
import com.example.springmongo.model.Coche;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(CocheResource.Coche_RESOURCE)
public class CocheResource {
    public final static String Coche_RESOURCE = "/coches";

    CocheController cocheController;

    @Autowired
    public CocheResource(CocheResource cocheResource) {
        this.cocheController = cocheController;
    }

    @GetMapping
    public List<Coche> coches(){
        return cocheController.getAllCoches();
    }

    @GetMapping("{id}")
    public Coche coche(@PathVariable("id") int id){
        return cocheController.getCoche(id);
    }

    @PostMapping
    public void addCoche(@RequestBody Coche coche){
        cocheController.añadir(coche);
    }

    @DeleteMapping("{id}")
    public void deleteCoche(@PathVariable("id") int id){
        cocheController.deleteById(id);
    }

    @PutMapping("{id}")
    public void putCoche(@PathVariable("id") int id, @RequestBody Coche coche){
        cocheController.actualizar(id,coche);
    }

    @PatchMapping("{id}")
    public void patchCoche(@PathVariable("id") int id, @RequestBody JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        cocheController.patchCoche(id,patch);
    }
}
