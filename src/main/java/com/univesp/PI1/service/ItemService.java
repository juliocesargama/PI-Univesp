package com.univesp.PI1.service;

import com.univesp.PI1.entity.Item;
import com.univesp.PI1.entity.Enums.ItemStatus;
import com.univesp.PI1.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    public List<Item> findAll(){
        return itemRepository.findAll();
    }

    public void save(Item item){

        if(item.getId() != null) {
            Item existingItem = itemRepository
                    .findById(item.getId())
                    .orElseThrow(() ->new RuntimeException("Item does not exists"));

            existingItem.setName(item.getName());
            existingItem.setDescription(item.getDescription());
            existingItem.setStatus(item.getStatus());
            itemRepository.save(existingItem);
        }else{
            item.setStatus(ItemStatus.AVAILABLE);
            itemRepository.save(item);
        }
    }
    public void executeDevolution(Item item){
        item.setStatus(ItemStatus.AVAILABLE);
        itemRepository.save(item);
    }

    public void executeLoan(Item item){
        item.setStatus(ItemStatus.BORROWED);
        itemRepository.save(item);
    }
}
