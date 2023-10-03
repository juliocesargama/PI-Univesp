package com.univesp.PI1.service;

import com.univesp.PI1.entity.Item;
import com.univesp.PI1.entity.Enums.ItemStatus;
import com.univesp.PI1.repository.ItemRepository;
import com.univesp.PI1.utils.handler.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        return itemRepository.save(item);
    }

    public Item update(Integer id, Item item) {
        if (id != null) {
            Item existingItem = itemRepository
                    .findById(id)
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"Item não encontrado."));

            if (existingItem.getStatus().equals(ItemStatus.BORROWED)) {
                throw new CustomException(HttpStatus.BAD_REQUEST,"Item já está emprestado.");
            }
            existingItem.setName(item.getName());
            existingItem.setDescription(item.getDescription());
            existingItem.setStatus(item.getStatus());
            return itemRepository.save(existingItem);
        } else {
            throw new CustomException(HttpStatus.BAD_REQUEST,"Número do item é requerido.");
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
