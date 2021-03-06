package edu.nc.controller;

import edu.nc.common.GeneralSettings;
import edu.nc.dataaccess.entity.CardEntity;
import edu.nc.service.CardService;
import edu.nc.dataaccess.wrapper.cardtask.CardWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<CardEntity[]> addCards(@RequestBody CardWrapper[] wrapper) {
        return service.addCards(wrapper);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/filtered")
    public ResponseEntity<Page<CardEntity>> getAllFromPage(@RequestParam int page,
                                                           @RequestParam int size,
                                                           @RequestParam(defaultValue = "ASC") String direction,
                                                           @RequestParam(defaultValue = "word") String active,
                                                           @RequestParam(defaultValue = "") String filter) {
        return service.getAllFromPage(page, size, direction, active, filter);
    }

}
