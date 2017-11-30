package elena.chernenkova.controllers;


import elena.chernenkova.common.GeneralSettings;
import elena.chernenkova.entities.CardEntity;
import elena.chernenkova.services.CardService;
import elena.chernenkova.wrappers.cardtask.CardWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = GeneralSettings.CARD_CONTROLLER)
public class CardController {

    private CardService service;

    @Autowired
    public CardController(CardService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<CardEntity>> getAll() {
        return service.getAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CardEntity> addCard(@RequestBody CardWrapper wrapper) {
        return service.addCard(wrapper);
    }


}
