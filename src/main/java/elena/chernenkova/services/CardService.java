package elena.chernenkova.services;


import elena.chernenkova.entities.CardEntity;
import elena.chernenkova.repositories.CardRepository;
import elena.chernenkova.wrappers.cardtask.CardWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

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
        CardEntity entity = new CardEntity(wrapper);
        entity = cardRepository.save(entity);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }
}
