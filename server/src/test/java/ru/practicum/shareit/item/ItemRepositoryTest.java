package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.RequestDataUtils;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserDataUtils;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    private User user1;
    private User user2;
    private Item item1;
    private Item item2;
    private Item item3;
    private ItemRequest request;

    @BeforeEach
    public void beforeEach() {
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
        user1 = userRepository.save(UserDataUtils.getUser1());
        user2 = userRepository.save(UserDataUtils.getUser2());
        item1 = itemRepository.save(ItemDataUtils.getItem1());
        item2 = itemRepository.save(ItemDataUtils.getItem2());
        item3 = itemRepository.save(ItemDataUtils.getItem3());
        request = itemRequestRepository.save(RequestDataUtils.getItemRequest1());
        item1.setOwner(user1);
        item2.setOwner(user2);
        item3.setOwner(user2);
        item2.setRequest(request);
    }

    @Test
    void findAllByOwnerId() {
        List<Item> items = itemRepository.findAllByOwnerId(user1.getId(), PageRequest.of(0, 10)).getContent();
        assertEquals(1, items.size());
        assertEquals(item1, items.get(0));
    }

    @Test
    void search() {
        String text = "name3";
        List<Item> items = itemRepository.search(text, PageRequest.of(0, 10)).getContent();
        assertEquals(1, items.size());
        assertEquals(item3, items.get(0));
    }

    @Test
    void findAllByRequestId() {
        List<Item> items = itemRepository.findAllByRequestId(request.getId());
        assertEquals(1, items.size());
        assertEquals(item2, items.get(0));
    }
}