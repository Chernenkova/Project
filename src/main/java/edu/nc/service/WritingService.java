package edu.nc.service;

import edu.nc.dataaccess.entity.CardEntity;
import edu.nc.dataaccess.entity.TaskEntity;
import edu.nc.dataaccess.entity.TaskProgressEntity;
import edu.nc.dataaccess.entity.User;
import edu.nc.dataaccess.repository.CardRepository;
import edu.nc.dataaccess.repository.TaskProgressRepository;
import edu.nc.dataaccess.repository.TaskRepository;
import edu.nc.dataaccess.repository.UserRepository;
import edu.nc.dataaccess.serializerwrappers.ChoosingTranslationTaskSerializerWrapper;
import edu.nc.dataaccess.wrapper.cardtask.CardWrapper;
import edu.nc.security.JwtUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class WritingService {



    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private CardRepository cardRepository;
    private TaskProgressRepository taskProgressRepository;
    @Autowired
    public WritingService(TaskRepository taskRepository, UserRepository userRepository,
                          CardRepository cardRepository, TaskProgressRepository taskProgressRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.taskProgressRepository = taskProgressRepository;
    }



    public ResponseEntity getTask(Long id){
        TaskEntity entity = taskRepository.findOne(id);
        if(entity == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        ChoosingTranslationTaskSerializerWrapper cttsw = JsonClassParser.getObject(entity.getTask(),
                ChoosingTranslationTaskSerializerWrapper.class);
        long[] array = cttsw.getCardsIds();
        return new ResponseEntity<>(getCards(array), HttpStatus.OK);
    }

    private CardWrapper[] getCards(long[] array){
        List<CardWrapper> list = new ArrayList<>(array.length);
        for (long x: array) {
            CardEntity current = cardRepository.findOne(x);
            if(current == null){
                continue;
            }
            list.add(new CardWrapper(current.getWord(), current.getTranslation()));
        }
        return list.toArray(new CardWrapper[list.size()]);
    }

}
