package com.example.springmongo.resources;

import com.example.springmongo.model.Coche;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(CocheResource.Product_RESOURCE)
public class CocheResource {
    public final static String Product_RESOURCE = "/coches";

    CocheResource cocheResource;

    @Autowired
    public CocheResource(CocheResource cocheResource) {
        this.cocheResource = cocheResource;
    }

    @GetMapping
    public List<Coche> coches(){
        return cocheResource.getAllCoches();
    }

    @GetMapping("{id}")
    public Coche coche(@PathVariable("id") int id){
        return cocheController.getProduct(id);
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
    public void putProduct(@PathVariable("id") int id, @RequestBody Coche coche){
        cocheController.actualizar(id,product);
    }

    @PatchMapping("{id}")
    public void patchProduct(@PathVariable("id") int id, @RequestBody JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        productController.patchProduct(id,patch);
    }
}
