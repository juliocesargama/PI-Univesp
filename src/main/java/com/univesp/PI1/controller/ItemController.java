package com.univesp.PI1.controller;

import com.univesp.PI1.entity.Item;
import com.univesp.PI1.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/item")
public class ItemController {

    @Autowired
    ItemService itemService;
    @GetMapping(value = "/all")
    public List<Item> getItems(){
        return itemService.findAll();
    }

    @PostMapping(value = "/save")
    public void saveItem(@RequestBody Item item){
        itemService.save(item);
    }


}
