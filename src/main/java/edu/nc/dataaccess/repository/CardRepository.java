package edu.nc.dataaccess.repository;

import edu.nc.dataaccess.entity.CardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<CardEntity, Long>, PagingAndSortingRepository<CardEntity, Long> {
    default Optional<CardEntity> findByWordAndTranslation(String word, String translation) {
        return findByWordBytesAndTranslationBytes(word.getBytes(), translation.getBytes());
    }

    Optional<CardEntity> findByWordBytesAndTranslationBytes(byte[] wordBytes, byte[] translationBytes);

    @Query(value = "SELECT card " +
            "FROM CardEntity card " +
            "WHERE card.wordBytes like CONCAT('%',?1,'%') " +
            "OR card.translationBytes like CONCAT('%',?1,'%')")
    Page<CardEntity> findAllByFilter(String filter, Pageable pageable);


}
