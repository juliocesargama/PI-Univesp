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

    public Item save(Item item){

        if(item.getId() != null) {
            Item existingItem = itemRepository
                    .findById(item.getId())
                    .orElseThrow(() ->new RuntimeException("Item does not exists"));

            if(!existingItem.getStatus().equals(ItemStatus.AVAILABLE)){
                throw new RuntimeException("Item is not available, must return it first.");
            }

            existingItem.setName(item.getName());
            existingItem.setDescription(item.getDescription());

            existingItem.setStatus(item.getStatus());
            return itemRepository.save(existingItem);
        }else{
            item.setStatus(ItemStatus.AVAILABLE);
            return itemRepository.save(item);
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
