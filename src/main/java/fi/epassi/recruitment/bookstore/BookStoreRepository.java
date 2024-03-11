package fi.epassi.recruitment.bookstore;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookStoreRepository extends JpaRepository<BookStoreModel, Integer> {

    Optional<BookStoreModel> findByStoreId(Integer store_id);

    List<BookStoreModel> findByName(String name);
}
