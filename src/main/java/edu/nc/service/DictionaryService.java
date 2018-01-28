package edu.nc.service;

import edu.nc.common.GeneralSettings;
import edu.nc.dataaccess.entity.CardEntity;
import edu.nc.dataaccess.entity.TaskEntity;
import edu.nc.dataaccess.entity.User;
import edu.nc.dataaccess.repository.CardRepository;
import edu.nc.dataaccess.repository.TaskRepository;
import edu.nc.dataaccess.repository.UserRepository;
import edu.nc.dataaccess.wrapper.cardtask.CardWrapper;
import edu.nc.dataaccess.wrapper.cardtask.ChoosingTranslationTaskWrapper;
import edu.nc.dataaccess.wrapper.cardtask.ChoosingTranslationWrapper;
import edu.nc.security.JwtUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.xml.transform.OutputKeys;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;


@Component
public class DictionaryService {

    private CardRepository cardRepository;
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    @Autowired
    public DictionaryService(CardRepository cardRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<CardWrapper[]> delete(CardWrapper[] array){
        //get User
        User user = userRepository.findByUsername(JwtUserDetails.getUserName());
        if(null == user){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        TaskEntity taskEntity = taskRepository.findByAuthorAndType(user, GeneralSettings.DICTIONARY_TYPE);
        if(null == taskEntity){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ChoosingTranslationTaskWrapper wrapper = JsonClassParser.getObject(taskEntity.getTask(), ChoosingTranslationTaskWrapper.class);
        wrapper.setCardsIds(removeIds(wrapper.getCardsIds(), getCardsIds(array)));
        taskEntity.setTask(JsonClassParser.getBytes(wrapper));
        taskEntity = taskRepository.saveAndFlush(taskEntity);
        wrapper = JsonClassParser.getObject(taskEntity.getTask(), ChoosingTranslationTaskWrapper.class);
        return new ResponseEntity<>(getDictionary(wrapper.getCardsIds()), HttpStatus.OK);
    }

    /**
     *
     * @return cards' array
     */
    public ResponseEntity<CardWrapper[]> get(){
        //get User
        User user = userRepository.findByUsername(JwtUserDetails.getUserName());
        if(null == user){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        //get dictionaryEntity
        TaskEntity taskEntity = taskRepository.findByAuthorAndType(user, GeneralSettings.DICTIONARY_TYPE);
        if(taskEntity == null){
            return new ResponseEntity<>(new CardWrapper[0], HttpStatus.OK);
        }
        ChoosingTranslationTaskWrapper wrapper = JsonClassParser.getObject(taskEntity.getTask(), ChoosingTranslationTaskWrapper.class);
        return new ResponseEntity<>(getDictionary(wrapper.getCardsIds()), HttpStatus.OK);
    }

    /** add the word to user's dictionary.
     * if user does not have dictionary, this method creates it.
     *
     * @param cardWrapper contains word with translation
     * @return created
     */
    public ResponseEntity<CardWrapper[]> put(CardWrapper cardWrapper){
        //get User
        User user = userRepository.findByUsername(JwtUserDetails.getUserName());
        if(null == user){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        //get dictionaryEntity
        TaskEntity taskEntity = taskRepository.findByAuthorAndType(user, GeneralSettings.DICTIONARY_TYPE);
        if(null == taskEntity){
            //There is no dictionary for this person
            ChoosingTranslationTaskWrapper task = new ChoosingTranslationTaskWrapper();
            byte[] bytes = JsonClassParser.getBytes(task);
            taskEntity = new TaskEntity(GeneralSettings.DICTIONARY_TYPE, bytes, null, user,"",0,0);
        }
        long newId = putWordToDB(cardWrapper);
        ChoosingTranslationTaskWrapper taskWrapper = JsonClassParser.getObject(taskEntity.getTask(), ChoosingTranslationTaskWrapper.class);
        //check this word in dictionary
        boolean contains = LongStream.of(taskWrapper.getCardsIds()).anyMatch(x -> x == newId);
        if(contains){
            return new ResponseEntity<>(getDictionary(taskWrapper.getCardsIds()), HttpStatus.OK);
        }
        //add new index to array
        long[] newArray = new long[taskWrapper.getCardsIds().length + 1];
        System.arraycopy(taskWrapper.getCardsIds(), 0, newArray, 0, taskWrapper.getCardsIds().length);
        newArray[newArray.length - 1] = newId;
        taskWrapper.setCardsIds(newArray);
        taskEntity.setTask(JsonClassParser.getBytes(taskWrapper));
        //update in DB
        taskEntity = taskRepository.saveAndFlush(taskEntity);
        return new ResponseEntity<>(getDictionary(JsonClassParser.getObject(taskEntity.getTask(), ChoosingTranslationTaskWrapper.class).getCardsIds()), HttpStatus.OK);
    }

    /**
     * checks existing the same card in DB and saves it, if there is no one
     * @param wrapper
     * @return card's id
     */
    private long putWordToDB(CardWrapper wrapper){
        Optional<CardEntity> cardEntityOptional = cardRepository.findByWordAndTranslation(wrapper.getWord(), wrapper.getTranslation());
        if(cardEntityOptional.isPresent()){
            return cardEntityOptional.get().getId();
        }
        CardEntity cardEntity = new CardEntity(wrapper);
        cardEntity = cardRepository.save(cardEntity);
        return cardEntity.getId();
    }

    /**
     * gets the card entities from DB and creates array
     * @param indexes
     * @return array of cards
     */
    private CardWrapper[] getDictionary(long[] indexes){
        CardWrapper[] array = new CardWrapper[indexes.length];
        for (int i = 0; i < array.length; i++) {
            CardEntity entity = cardRepository.getOne(indexes[i]);
            array[i] = new CardWrapper(entity.getWord(), entity.getTranslation());
        }
        return array;
    }

    private long[] removeIds(long[] array1, long[] array2){
        List<Long> list1 = Arrays.stream(array1).boxed().collect(Collectors.toList());
        List<Long> list2 = Arrays.stream(array2).boxed().collect(Collectors.toList());
        list1.removeAll(list2);
        return list1.stream().mapToLong(x -> x).toArray();
    }

    private long[] getCardsIds(CardWrapper[] wrappers){
        LongStream.Builder lsb = LongStream.builder();
        Arrays.stream(wrappers).forEach(x -> lsb.add(cardRepository.findByWordAndTranslation(x.getWord(), x.getTranslation()).get().getId()));
        return lsb.build().toArray();
    }
}
