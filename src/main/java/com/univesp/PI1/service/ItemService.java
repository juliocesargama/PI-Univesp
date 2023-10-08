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

    public Item findById(Integer id){
        return itemRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"Item não encontrado."));
    }

    public Item save(Item item){
        return itemRepository.save(item);
    }

    public Item update(Integer id, Item item) {
        if (id != null) {
            Item existingItem = itemRepository
                    .findById(id)
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"Item não encontrado."));

            existingItem.setName(item.getName());
            existingItem.setDescription(item.getDescription());
            if (!existingItem.getStatus().equals(ItemStatus.Emprestado)) {
                existingItem.setStatus(item.getStatus());
            }
            return itemRepository.save(existingItem);
        } else {
            throw new CustomException(HttpStatus.BAD_REQUEST,"Número do item é requerido.");
        }
    }
    public void executeDevolution(Item item){
        item.setStatus(ItemStatus.Disponivel);
        itemRepository.save(item);
    }

    public void executeLoan(Item item){
        item.setStatus(ItemStatus.Emprestado);
        itemRepository.save(item);
    }
}
