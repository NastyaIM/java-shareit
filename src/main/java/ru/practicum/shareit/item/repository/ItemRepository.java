package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByOwnerId(long ownerId, Pageable pageable);

    @Query("select i from Item i " +
            "where ((lower(i.name) like lower(concat('%',?1,'%'))) " +
            "or (lower(i.description) like lower(concat('%',?1,'%')))) " +
            "and i.available = true")
    Page<Item> search(String text, Pageable pageable);

    List<Item> findAllByRequestId(long requestId);
}