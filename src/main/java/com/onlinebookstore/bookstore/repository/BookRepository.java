package com.onlinebookstore.bookstore.repository;

import com.onlinebookstore.bookstore.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT * FROM Book " +
            "WHERE (:title IS NULL OR title = :title) " +
            "AND (:author IS NULL OR author = :author) " +
            "AND (:yearOfPublication IS NULL OR year_of_publication = :yearOfPublication) " +
            "AND (:genre IS NULL OR genre = :genre) " +
            "ORDER BY date_created DESC",
            countQuery = "SELECT COUNT(*) FROM Book " +
                    "WHERE (:title IS NULL OR title = :title) " +
                    "AND (:author IS NULL OR author = :author) " +
                    "AND (:yearOfPublication IS NULL OR year_of_publication = :yearOfPublication) " +
                    "AND (:genre IS NULL OR genre = :genre) ",
            nativeQuery = true)
    Page<Book> findAllBy(
            @Param("title") String title,
            @Param("author") String author,
            @Param("yearOfPublication") String yearOfPublication,
            @Param("genre") String genre,
            Pageable pageable
    );


    @Query(value = "SELECT * FROM Book WHERE (:id IS NULL OR id = :id) ",
            nativeQuery = true)
    Optional<Book> findBookById(@Param("id") Long id);

    Book findByIsbn(String isbn);


}
