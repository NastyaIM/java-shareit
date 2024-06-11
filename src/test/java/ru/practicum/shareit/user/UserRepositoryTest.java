package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void findIdsAllOtherUsers() {
        User user1 = userRepository.save(UserDataUtils.getUser1());
        User user2 = userRepository.save(UserDataUtils.getUser2());

        List<Long> ids = userRepository.findIdsAllOtherUsers(user2.getId());

        assertEquals(1, ids.size());
        assertEquals(List.of(user1.getId()), ids);
    }

    @Test
    void findIdsAllOtherUsersIfThereIsOnlyOneUser() {
        User user1 = userRepository.save(UserDataUtils.getUser1());

        List<Long> ids = userRepository.findIdsAllOtherUsers(user1.getId());

        assertThat(ids).isEmpty();
    }

    @Test
    void findIdsAllOtherUsersIfUsersIsEmpty() {
        List<Long> ids = userRepository.findIdsAllOtherUsers(1);

        assertThat(ids).isEmpty();
    }
}