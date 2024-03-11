package fi.epassi.recruitment.book;

import fi.epassi.recruitment.exception.BookNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public UUID createBook(BookDto bookDto) {
        BookModel bookModel = toBookModel(bookDto);
        var savedBook = bookRepository.save(bookModel);
        return savedBook.getIsbn();
    }

    public void deleteBookWithIsbn(@NonNull UUID isbn) {
        bookRepository.deleteById(isbn);
    }

    public BookDto getBookByIsbn(@NonNull UUID isbn) throws BookNotFoundException {
        return bookRepository.findByIsbn(isbn)
            .map(BookService::toBookDto)
            .orElseThrow(() -> new BookNotFoundException(isbn.toString()));
    }


    public Page<BookDto> getBooks(String author, String title, Pageable pageable) {

        if (StringUtils.isNotBlank(author) && StringUtils.isNotBlank(title)) {
            return bookRepository.findByAuthorAndTitle(author, title,pageable).map(BookService::toBookDto);
        } else if (StringUtils.isNotBlank(author) && StringUtils.isBlank(title)) {
            return bookRepository.findByAuthor(author, pageable).map(BookService::toBookDto);
        } else if (StringUtils.isNotBlank(title) && StringUtils.isBlank(author)) {
            return bookRepository.findByTitle(title, pageable).map(BookService::toBookDto);
        }

        return bookRepository.findAll(pageable).map(BookService::toBookDto);
    }

//    public int getBookQuantityByIsbn(@NonNull UUID isbn){
//        Optional<Integer> quantityOptional = bookRepository.findByIsbn(isbn).map(BookModel::getQuantity);
//        return  quantityOptional.orElseThrow(() -> new BookNotFoundException(isbn.toString()));
//    }

//    public int getBookQuantityByAuthor(@NonNull String author){
//        return bookRepository.findByAuthor(author,Pageable.unpaged())
//                .stream()
//                .mapToInt(BookModel::getQuantity)
//                .sum();
//    }
//
//    public int getBookQuantityByTitle(@NonNull String title){
//        return bookRepository.findByTitle(title,Pageable.unpaged())
//                .stream()
//                .mapToInt(BookModel::getQuantity)
//                .sum();
//    }

    public UUID updateBook(BookDto bookDto) {
        if (bookRepository.findByIsbn(bookDto.getIsbn()).isPresent()) {
            var bookModel = toBookModel(bookDto);
            var savedBook = bookRepository.save(bookModel);
            return savedBook.getIsbn();
        }

        throw new BookNotFoundException(bookDto.getIsbn().toString());
    }

//    public UUID updateBookQuantity(BookQuantityDto bookQuantityDto) {
//        if (bookRepository.findByIsbn(bookQuantityDto.getIsbn()).isPresent()) {
//            BookModel bookModel = bookRepository.findByIsbn(bookQuantityDto.getIsbn()).get();
//            bookModel.setQuantity(bookQuantityDto.getQuantity());
//
//            var savedBook = bookRepository.save(bookModel);
//            return savedBook.getIsbn();
//        }
//
//        throw new BookNotFoundException(bookQuantityDto.getIsbn().toString());
//    }

    private static BookModel toBookModel(BookDto bookDto) {
        return BookModel.builder()
            .isbn(bookDto.getIsbn())
            .author(bookDto.getAuthor())
            .title(bookDto.getTitle())
            .price(bookDto.getPrice())
            .build();
    }

    private static BookDto toBookDto(BookModel bookModel) {
        return BookDto.builder()
            .isbn(bookModel.getIsbn())
            .author(bookModel.getAuthor())
            .title(bookModel.getTitle())
            .price(bookModel.getPrice())
            .build();
    }
}
