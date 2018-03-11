package edu.nc.service;

import edu.nc.dataaccess.entity.CardEntity;
import edu.nc.dataaccess.repository.CardRepository;
import edu.nc.dataaccess.wrapper.cardtask.CardWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CardService {

    private CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public ResponseEntity<Iterable<CardEntity>> getAll() {
        return new ResponseEntity<>(cardRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<CardEntity> addCard(CardWrapper wrapper) {
        return new ResponseEntity<>(saveWord(wrapper), HttpStatus.OK);
    }

    public ResponseEntity<CardEntity[]> addCards(CardWrapper[] wrapper) {
        CardEntity[] cardEntities = new CardEntity[wrapper.length];
        for (int i = 0; i < wrapper.length; i++) {
            cardEntities[i] = saveWord(wrapper[i]);
        }
        return new ResponseEntity<CardEntity[]>(cardEntities, HttpStatus.OK);
    }

    private CardEntity saveWord(CardWrapper cardWrapper) {
        Optional<CardEntity> optional = cardRepository.findByWordAndTranslation(cardWrapper.getWord(), cardWrapper.getTranslation());
        if (optional.isPresent()) {
            return optional.get();
        }
        CardEntity entity = new CardEntity(cardWrapper);
        entity = cardRepository.save(entity);
        return entity;
    }

    public ResponseEntity<Page<CardEntity>> getAllFromPage(int page, int size, String direction, String active, String filter) {

        Sort sort = new Sort(Sort.Direction.fromStringOrNull(direction), active + "Bytes");

        Page<CardEntity> cardPage = cardRepository.findAllByFilter(filter, new PageRequest(page, size, sort));
        return new ResponseEntity<>(cardPage, HttpStatus.OK);
    }
}
