package com.univesp.PI1.controller;

import com.univesp.PI1.entity.Item;
import com.univesp.PI1.service.ItemService;
import com.univesp.PI1.utils.handler.EndpointResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/item")
public class ItemController {

    @Autowired
    ItemService itemService;
    @GetMapping(value = "/all")
    public ResponseEntity<List<Item>> getItems(){

        return ResponseEntity.ok(itemService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Item> getItem(@PathVariable Integer id){
        return ResponseEntity.ok(itemService.findById(id));
    }

    @PostMapping(value = "/save")
    public ResponseEntity<EndpointResponse> saveItem(@RequestBody Item item){
        itemService.save(item);
        return ResponseEntity.ok().body(new EndpointResponse("Item salvo com sucesso!"));
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<EndpointResponse> updateItem(@PathVariable Integer id, @RequestBody Item item){
         itemService.update(id, item);
        return ResponseEntity.ok().body(new EndpointResponse("Item atualizado com sucesso!"));
    }


}
