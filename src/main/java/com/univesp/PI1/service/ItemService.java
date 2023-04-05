package com.univesp.PI1.service;

import com.univesp.PI1.entity.Applicant;
import com.univesp.PI1.entity.Item;
import com.univesp.PI1.entity.ItemStatus;
import com.univesp.PI1.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    public List<Item> findAll(){
        return itemRepository.findAll();
    }

    public void save(Item item){
        Optional<Item> existingItem = itemRepository.findById(item.getId());

        if(existingItem.isPresent()) {
            existingItem.get().setName(item.getName());
            existingItem.get().setDescription(item.getDescription());
            existingItem.get().setStatus(item.getStatus());
            itemRepository.save(existingItem.get());
        }else{
            item.setStatus(ItemStatus.AVAILABLE);
            itemRepository.save(item);
        }
    }

}
