package com.univesp.PI1.service;

import com.univesp.PI1.entity.Enums.ItemStatus;
import com.univesp.PI1.entity.Item;
import com.univesp.PI1.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    ItemRepository repository;

    @InjectMocks
    ItemService service;

    List<Item> items = new ArrayList<>();
    Item item = new Item();

    @BeforeEach
    void setup(){
        item.setId(1);
        item.setName("Projetor");
        item.setDescription("com entrada hdmi");
        item.setStatus(ItemStatus.AVAILABLE);

        items.add(item);
    }

    @Test
    void FindAllItemsTest(){
        Mockito.when(repository.findAll()).thenReturn(items);
        assertEquals(items, service.findAll());
    }

    @Test
    void SaveItemTest(){
        item.setId(null);

        Mockito.when(repository.save(item)).thenReturn(item);
        assertEquals(item, service.save(item));
    }

    @Test
    void UpdateItemTest(){
        item.setDescription("com entrada hdmi e vga");

        Mockito.when(repository.save(item)).thenReturn(item);
        Mockito.when(repository.findById(item.getId())).thenReturn(Optional.ofNullable(item));
        assertEquals(item, service.update(1, item));
    }

    @Test
    void UpdateItemFailedTest(){
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> service.update(99,item));
        assertEquals("Item does not exists", runtimeException.getMessage());
    }

    @Test
    void UpdateItemFailedItemNotAvailableTest(){
        item.setStatus(ItemStatus.BORROWED);
        Mockito.when(repository.findById(item.getId())).thenReturn(Optional.ofNullable(item));

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> service.update(1, item));
        assertEquals("Item is not available, must return it first.", runtimeException.getMessage());
    }

    @Test
    void ExecuteLoanTest(){
        service.executeLoan(item);
        assertEquals(ItemStatus.BORROWED, item.getStatus());
        verify(repository).save(item);
    }

    @Test
    void ExecuteDevolutionTest(){
        item.setStatus(ItemStatus.BORROWED);

        service.executeDevolution(item);
        assertEquals(ItemStatus.AVAILABLE, item.getStatus());
        verify(repository).save(item);
    }
}
