package edu.nc.controller;

import edu.nc.common.GeneralSettings;
import edu.nc.dataaccess.wrapper.cardtask.CardWrapper;
import edu.nc.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = GeneralSettings.DICTIONARY)
public class DictionaryController {

    private DictionaryService dictionaryService;

    @Autowired
    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }



    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<CardWrapper[]> get(){
        return dictionaryService.get();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<CardWrapper[]> put(@RequestBody CardWrapper wrapper) {
        return dictionaryService.put(wrapper);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete")
    public ResponseEntity<CardWrapper[]> delete(@RequestBody CardWrapper[] array){

        return dictionaryService.delete(array);
    }


}
