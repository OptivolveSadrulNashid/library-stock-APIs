package fi.epassi.recruitment.bookstore;

import fi.epassi.recruitment.exception.BookStoreNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookStoreService {
    private final BookStoreRepository bookStoreRepository;

    public int createBookStore(BookStoreDto bookStoreDto) {
        BookStoreModel storeModel = toBookStoreModel(bookStoreDto);
        var savedBookStore = bookStoreRepository.save(storeModel);
        return savedBookStore.getStoreId();
    }

    public void deleteBookStoreWithID(@NonNull int storeID) {
        bookStoreRepository.deleteById(storeID);
    }

    public BookStoreDto getBookStoreByID(@NonNull int storeID) throws BookStoreNotFoundException {
        return bookStoreRepository
                .findByStoreId(storeID)
                .map(BookStoreService::toBookStoreDto)
                .orElseThrow(() -> new BookStoreNotFoundException(Integer.toString(storeID)));
    }


    public List<BookStoreDto> getBookStores(String name) {

        if (StringUtils.isNotBlank(name)) {
            return bookStoreRepository
                    .findByName(name)
                    .stream()
                    .map(BookStoreService::toBookStoreDto)
                    .toList();
        }

        return bookStoreRepository.findAll()
                .stream()
                .map(BookStoreService::toBookStoreDto)
                .toList();
    }

    public int updateBookStore(BookStoreDto bookStoreDto) {
        if (bookStoreRepository.findByStoreId(bookStoreDto.getStore_id()).isPresent()) {
            var bookStoreModel = toBookStoreModel(bookStoreDto);
            var savedBook = bookStoreRepository.save(bookStoreModel);
            return savedBook.getStoreId();
        }

        throw new BookStoreNotFoundException(Integer.toString(bookStoreDto.getStore_id()));
    }

    private static BookStoreModel toBookStoreModel(BookStoreDto bookStoreDto) {
        return BookStoreModel.builder()
                .storeId(bookStoreDto.getStore_id())
                .name(bookStoreDto.getName())
                .address(bookStoreDto.getAddress())
                .contactPerson(bookStoreDto.getContact_person())
                .openDays(bookStoreDto.getOpen_days())
                .build();
    }

    private static BookStoreDto toBookStoreDto(BookStoreModel bookStoreModel) {
        return BookStoreDto.builder()
                .store_id(bookStoreModel.getStoreId())
                .name(bookStoreModel.getName())
                .address(bookStoreModel.getAddress())
                .contact_person(bookStoreModel.getContactPerson())
                .open_days(bookStoreModel.getOpenDays())
                .build();
    }
}
