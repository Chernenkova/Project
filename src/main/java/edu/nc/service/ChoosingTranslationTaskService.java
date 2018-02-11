package edu.nc.service;

import edu.nc.dataaccess.entity.*;
import edu.nc.dataaccess.repository.TaskProgressRepository;
import edu.nc.dataaccess.repository.TaskRepository;
import edu.nc.dataaccess.repository.UserRepository;
import edu.nc.dataaccess.wrapper.cardtask.*;
import edu.nc.common.GeneralSettings;
import edu.nc.dataaccess.repository.CardRepository;
import edu.nc.dataaccess.serializerwrappers.ChoosingTranslationTaskSerializerWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.LongStream;

@Component
public class ChoosingTranslationTaskService {

    private TaskRepository taskRepository;
    private CardRepository cardRepository;
    private UserRepository userRepository;
    private TaskProgressRepository taskProgressRepository;

    @Autowired
    public ChoosingTranslationTaskService(TaskRepository taskRepository,
                                          CardRepository cardRepository,
                                          UserRepository userRepository,
                                          TaskProgressRepository taskProgressRepository) {
        this.taskRepository = taskRepository;
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.taskProgressRepository = taskProgressRepository;
    }

    public ResponseEntity<TaskEntity> addTask(ChoosingTranslationTaskWrapper wrapper) {
        ChoosingTranslationTaskSerializerWrapper c = new ChoosingTranslationTaskSerializerWrapper(wrapper.getCardsIds());
        byte[] bytes = JsonClassParser.getBytes(c);
        TaskEntity task = new TaskEntity(GeneralSettings.CHOOSING_TASK_BASIC_TYPE, bytes, null,null,"",0,0);
        taskRepository.save(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }


    public ResponseEntity<TaskEntity> getTaskById(Long id) {
        TaskEntity task = taskRepository.findOne(id);
        if (task == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    public ResponseEntity<ChoosingTranslationTaskWrapper> getById(Long id) {
        TaskEntity task = taskRepository.findOne(id);
        if (task == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ChoosingTranslationTaskSerializerWrapper c = JsonClassParser.getObject(task.getTask(), ChoosingTranslationTaskSerializerWrapper.class);
        ChoosingTranslationTaskWrapper wrapper = new ChoosingTranslationTaskWrapper(c.getCardsIds());
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }


    public ResponseEntity<ChoosingTranslationWrapper> getTask(Long id) {
        Optional<User> optional = userRepository.getCurrentUser();
        if(!optional.isPresent()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = optional.get();
        List<TaskProgressEntity> listOfTasks = user.getTasks();
        TaskEntity task = taskRepository.findOne(id);
        if (task == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<TaskProgressEntity> optionalTaskEntity = listOfTasks
                .stream()
                .filter(x -> x.getTask().getId().equals(task.getId()))
                .findAny();

        ChoosingTranslationWrapper  ctw = null;
        if (task.getType().equalsIgnoreCase(GeneralSettings.CHOOSING_TASK_BASIC_TYPE)) {
            ctw = getBasicTask(task);
        }
        if (task.getType().equalsIgnoreCase(GeneralSettings.CHOOSING_TASK_ADVANCED_TYPE)) {
            ctw = getAdvancedTask(task);
        }

        TaskProgressEntity tpe = null;
        if(optionalTaskEntity.isPresent()){
            //User has already executed this task
            tpe = optionalTaskEntity.get();
            tpe.setStatus(TaskProgressStatus.IN_PROGRESS);

        }else {
            //User has not executed this task yet
            tpe = new TaskProgressEntity(task, TaskProgressStatus.FIRST);
        }
        tpe.setTotalScore(ctw.getArray().length);
        tpe.setScore(0);
        tpe = taskProgressRepository.saveAndFlush(tpe);
        if(!optionalTaskEntity.isPresent()){
            user.getTasks().add(tpe);
        }
        user = userRepository.saveAndFlush(user);

        return new ResponseEntity<>(ctw, HttpStatus.OK);
    }

    public ResponseEntity<CardResponseWrapper> checkTranslation(CardWrapperIdAndTranslation wrapper, Long id) {
        Optional<User> optUser = userRepository.getCurrentUser();
        if(!optUser.isPresent()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = optUser.get();
        CardEntity entity = cardRepository.findOne(wrapper.getId());
        if (entity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<TaskProgressEntity> optTpe= user.getTasks().stream().filter(x -> x.getTask().getId().equals(id)).findAny();
        if(!optTpe.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        TaskProgressEntity tpe = optTpe.get();

        if (entity.getTranslation().equalsIgnoreCase(wrapper.getTranslation())) {
            //TODO increment the user's score
            tpe.setScore(tpe.getScore() + 1);
        } else {
            //TODO decrement the user's score
        }
        taskProgressRepository.saveAndFlush(tpe);
        return new ResponseEntity<>(new CardResponseWrapper(entity.getTranslation()), HttpStatus.OK);
    }

    private ChoosingTranslationWrapper getBasicTask(TaskEntity task) {
        ChoosingTranslationTaskSerializerWrapper c1 = JsonClassParser.getObject(task.getTask(), ChoosingTranslationTaskSerializerWrapper.class);

        //Getting cards which will be used in task
        List<CardEntity> cards = new ArrayList<>();
        for (int i = 0; i < c1.getCardsIds().length; i++) {
            CardEntity entity = cardRepository.findOne(c1.getCardsIds()[i]);
            if (entity == null) continue;
            cards.add(entity);
        }
        return createTask(cards);
    }

    private ChoosingTranslationWrapper getAdvancedTask(TaskEntity task) {
        //TODO: implements this method
        return null;
    }

    private ChoosingTranslationWrapper createTask(List<CardEntity> entities) {
        List<ChoosingTranslationCardWrapper> list = new ArrayList<>();

        for (int i = 0; i < entities.size(); i++) {
            CardEntity current = entities.get(i);
            ChoosingTranslationCardWrapper iteration = new ChoosingTranslationCardWrapper();
            iteration.setWord(current.getWord());
            iteration.setPossibleTranslations(getPossibleTranslations(entities, current));
            iteration.setId(current.getId());
            list.add(iteration);
        }
        Collections.shuffle(list);
        ChoosingTranslationCardWrapper[] array = new ChoosingTranslationCardWrapper[list.size()];
        list.toArray(array);
        return new ChoosingTranslationWrapper(array);
    }

    private String[] getPossibleTranslations(List<CardEntity> list, CardEntity current) {
        Random r = new Random();
        int num = list.size() > 4 ? 4 : list.size();
        String[] possibleTranslations = new String[num];
        possibleTranslations[0] = current.getTranslation();
        for (int j = 1; j < num; j++) {
            String temp;
            Iteration:
            do {
                int rand = r.nextInt(list.size());
                String possible = list.get(rand).getTranslation();
                for (int i = 0; i < num; i++) {
                    if (possible.equalsIgnoreCase(possibleTranslations[i])) continue Iteration;
                }
                temp = possible;
                break;
            } while (true);
            possibleTranslations[j] = temp;
        }

        List<String> stringList = Arrays.asList(possibleTranslations);
        Collections.shuffle(stringList);
        return (String[]) stringList.toArray();
    }

    public ResponseEntity createBasicTask(BasicTaskWrapper wrapper) {
        CardWrapper[] array = wrapper.getArray();
        List<Long> list = new ArrayList<>();
        for (CardWrapper x : array) {
            Optional<CardEntity> entityOptional = cardRepository.findByWordAndTranslation(x.getWord(), x.getTranslation());
            if (entityOptional.isPresent()) {
                //the DB contains this word
                list.add(entityOptional.get().getId());
            } else {
                //There is no such field in DB. Creating it
                CardEntity current = new CardEntity(x);
                current = cardRepository.save(current);
                list.add(current.getId());
            }
        }
        long[] ids = new long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ids[i] = list.get(i);
        }
        return addTask(new ChoosingTranslationTaskWrapper(ids));
    }

    public ResponseEntity createAdvancedTask(AdvancedTaskWrapper wrapper) {
        //TODO: implements this method
        return null;
    }
}
