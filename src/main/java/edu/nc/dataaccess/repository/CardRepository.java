package edu.nc.dataaccess.repository;

import edu.nc.dataaccess.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<CardEntity, Long>, PagingAndSortingRepository<CardEntity, Long> {
    Optional<CardEntity> findByWordAndTranslation(String word, String translation);
}
