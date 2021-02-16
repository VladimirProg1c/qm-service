package com.quoteme.qmservice.repository;

import com.quoteme.qmservice.domain.Category;
import com.quoteme.qmservice.domain.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface QuoteRepository extends JpaRepository<Quote, UUID>, PagingAndSortingRepository<Quote, UUID> {

    @Query(value = "SELECT q FROM quote q WHERE (q.userId = :userId or :userId is null or :userId = '' ) " +
            "and (q.category = :category or :category is null or :category = '') " +
            "and (UPPER(q.text) like CONCAT('%',UPPER(:text),'%')or :text is null or :text = '')" +
            "and q.deleted = false")
    Page<Quote> findAll(@Param("userId") UUID userId,
                        @Param("category") Category category,
                        @Param("text") String text,
                        Pageable pageable);

    @Query(value = "SELECT q FROM quote q WHERE (q.userId = :userId or :userId is null or :userId = '' ) " +
            "and (q.category = :category or :category is null or :category = '') " +
            "and (UPPER(q.text) like CONCAT('%',UPPER(:text),'%')or :text is null or :text = '')" +
            "and q.privateQuote = false and q.deleted = false")
    Page<Quote> findAllPublic(@Param("userId") UUID userId,
                        @Param("category") Category category,
                        @Param("text") String text,
                        Pageable pageable);

    Optional<Quote> findByIdAndDeletedFalse(UUID id);

    @Modifying
    @Query(value = "update quote q set q.deleted = true where q.id = ?1")
    void delete(UUID id);

}
