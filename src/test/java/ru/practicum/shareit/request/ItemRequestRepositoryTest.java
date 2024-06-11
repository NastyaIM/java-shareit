package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserDataUtils;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    private final User user1 = UserDataUtils.getUser1();
    private final User user2 = UserDataUtils.getUser2();
    private final ItemRequest request1 = RequestDataUtils.getItemRequest1();
    private final ItemRequest request2 = RequestDataUtils.getItemRequest2();

    @BeforeEach
    public void beforeEach() {
        userRepository.save(user1);
        userRepository.save(user2);
        request1.setRequester(user1);
        request2.setRequester(user2);
        itemRequestRepository.save(request1);
        itemRequestRepository.save(request2);
    }

    @Test
    void findAllByRequesterIdOrderByCreatedDesc() {
        List<ItemRequest> requests = itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(user1.getId());

        assertEquals(1, requests.size());
        assertEquals(request1, requests.get(0));
    }

    @Test
    void findAllByRequesterIdIn() {
        Page<ItemRequest> requests = itemRequestRepository
                .findAllByRequesterIdIn(List.of(user1.getId(), user2.getId()), PageRequest.of(0, 10));

        assertEquals(2, requests.getContent().size());
    }
}