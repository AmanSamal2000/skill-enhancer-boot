package com.learning.service;

import com.learning.entity.mongo.StudentCollection;
import com.learning.entity.mongo.TimeSlotCollection;
import com.learning.entity.mysql.StudentEntity;
import com.learning.entity.mysql.TimeSlotEntity;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.model.TimeSlotModel;
import com.learning.repository.mongo.TimeSlotMongoRepository;
import com.learning.repository.mysql.TimeSlotRepository;
import com.learning.utility.excel.TimeSlotReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeSlotService{

    private final TimeSlotRepository jpaRepo;

    private final TimeSlotMongoRepository mongoRepo;

    private final ModelMapper modelMapper;

    private final TimeSlotReader timeSlotReader;


    public List<TimeSlotModel> getAllRecords() {
        List<TimeSlotCollection> timeSlotCollectionsList = mongoRepo.findAll();
        List<TimeSlotEntity> timeSlotEntityList = jpaRepo.findAll();
        if (!CollectionUtils.isEmpty(timeSlotCollectionsList)) {
            List<TimeSlotModel> timeSlotModelList = timeSlotCollectionsList.stream()
                    .map(timeSlotCollection -> modelMapper.map(timeSlotCollection, TimeSlotModel.class))
                    .collect(Collectors.toList());
            return timeSlotModelList;
        } else if (!CollectionUtils.isEmpty(timeSlotEntityList)) {
            List<TimeSlotModel> timeSlotModelList = timeSlotEntityList.stream()
                    .map(timeSlotModel -> modelMapper.map(timeSlotModel, TimeSlotModel.class))
                    .collect(Collectors.toList());
            return timeSlotModelList;
        } else {
            return Collections.emptyList();
        }
    }
    public List<TimeSlotModel> getLimitedRecords(int count) {
        List<TimeSlotCollection> timeSlotCollectionsList = mongoRepo.findAll();
        List<TimeSlotEntity> timeSlotEntityList = jpaRepo.findAll();
        if (!CollectionUtils.isEmpty(timeSlotCollectionsList)) {
            List<TimeSlotModel> timeSlotModelList = timeSlotEntityList.stream().limit(count)
                    .map(timeSlotCollection -> modelMapper.map(timeSlotCollection, TimeSlotModel.class))
                    .collect(Collectors.toList());
            return timeSlotModelList;
        }else if (!CollectionUtils.isEmpty(timeSlotEntityList)){
            List<TimeSlotModel> timeSlotModelList = timeSlotEntityList.stream().limit(count)
                    .map(timeSlotEntity -> modelMapper.map(timeSlotEntity, TimeSlotModel.class))
                    .collect(Collectors.toList());
            return timeSlotModelList;
        } else {
            return Collections.emptyList();
        }
    }
    
    public List<TimeSlotModel> getSortedRecords(String sortBy) {
        List<TimeSlotCollection> timeSlotCollectionsList = mongoRepo.findAll();
        List<TimeSlotEntity> timeSlotEntityList = jpaRepo.findAll();
        if ((!CollectionUtils.isEmpty(timeSlotCollectionsList))){
            Comparator<TimeSlotCollection> comparator = findsuitableComparatorSortBy(sortBy);
            List<TimeSlotModel> timeSlotModelList = timeSlotEntityList.stream()
                    .map(timeSlotCollection -> modelMapper.map(timeSlotCollection, TimeSlotModel.class))
                    .collect(Collectors.toList());
            return timeSlotModelList;
        }else if (!CollectionUtils.isEmpty(timeSlotEntityList)){
            Comparator<TimeSlotEntity> comparator = findSuitableComparator(sortBy);
            List<TimeSlotModel> timeSlotModelList = timeSlotEntityList.stream()
                    .map(timeSlotEntity -> modelMapper.map(timeSlotEntity, TimeSlotModel.class))
                    .collect(Collectors.toList());
            return timeSlotModelList;

        } else {
            return Collections.emptyList();
        }
    }

    public TimeSlotModel saveRecord(TimeSlotModel record) {
        if (Objects.nonNull(record)) {
            TimeSlotEntity timeSlotEntity = new TimeSlotEntity();
            modelMapper.map(record, timeSlotEntity);
            jpaRepo.save(timeSlotEntity);

            Runnable runnable = () -> {
                TimeSlotCollection timeSlotCollection = new TimeSlotCollection();
                modelMapper.map(timeSlotEntity, timeSlotCollection);
                mongoRepo.save(timeSlotCollection);
            };
            CompletableFuture.runAsync(runnable);
        }
        return record;
    }

    public List<TimeSlotModel> saveAll(List<TimeSlotModel> timeSlotModelList) {
        if (!CollectionUtils.isEmpty(timeSlotModelList)) {
            List<TimeSlotEntity> timeSlotEntityList = timeSlotModelList.stream()
                    .map(timeSlotModel -> modelMapper.map(timeSlotModel, TimeSlotEntity.class))
                    .collect(Collectors.toList());
            jpaRepo.saveAll(timeSlotEntityList);

            Runnable runnable = () -> {
                List<TimeSlotCollection> timeSlotCollectionsList = timeSlotEntityList.stream()
                        .map(timeSlotEntity -> modelMapper.map(timeSlotEntity, TimeSlotCollection.class))
                        .collect(Collectors.toList());
                mongoRepo.saveAll(timeSlotCollectionsList);
            };
        }
        return timeSlotModelList;
    }
    
    public TimeSlotModel getRecordById(Long id) {
        if (mongoRepo.existsById(id)){
            TimeSlotCollection timeSlotCollection = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            return modelMapper.map(timeSlotCollection, TimeSlotModel.class);
        }
        
        TimeSlotEntity timeSlotEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        return modelMapper.map(timeSlotEntity, TimeSlotModel.class);
    }

    public void deleteRecordById(Long id) {
        if (jpaRepo.existsById(id)){
            jpaRepo.deleteById(id);
            log.info("deleted successfully");

            CompletableFuture.runAsync(() -> {
                mongoRepo.deleteById(id);
                log.info("delete Successfully mongo");
            });
        }
        log.error(ErrorMessages.COULD_NOT_DELETE_RECORD.getErrorMessage());
    }
    public TimeSlotModel updatedRecordById(Long id, TimeSlotModel record) {
        TimeSlotEntity timeSlotEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, timeSlotEntity);
        jpaRepo.save(timeSlotEntity);
        log.info("update data mysql");

        CompletableFuture.runAsync(() -> {
            TimeSlotCollection timeSlotCollection = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            modelMapper.map(record, timeSlotCollection);
            mongoRepo.save(timeSlotCollection);
            log.info("update data mongo");
        });
        return record;
        }

    public void saveExcelFile(MultipartFile file) {
        if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            try {
                List<TimeSlotEntity> timeSlotEntityList = timeSlotReader.getTimeSlotObjects(file.getInputStream());
                jpaRepo.saveAll(timeSlotEntityList);
                log.info("saving data in mysql");

                CompletableFuture.runAsync(() -> {
                    List<TimeSlotCollection> timeSlotCollectionList = timeSlotEntityList.stream()
                            .map(timeSlotEntity -> modelMapper.map(timeSlotEntity, TimeSlotCollection.class))
                            .collect(Collectors.toList());
                    mongoRepo.saveAll(timeSlotCollectionList);
                    log.info("saving data in mongo");
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Comparator<TimeSlotCollection> findsuitableComparatorSortBy(String sortBy){
        return null;
    }


    private Comparator<TimeSlotEntity> findSuitableComparator(String sortBy) {
        Comparator<TimeSlotEntity> comparator;
        switch (sortBy) {
            case "startTime": {
                comparator = (timeSlotOne, timeSlotTwo) -> timeSlotOne.getStartTime().compareTo(timeSlotTwo.getStartTime());
                break;
            }
            case "endTime": {
                comparator = (timeSlotOne, timeSlotTwo) -> timeSlotOne.getEndTime().compareTo(timeSlotTwo.getEndTime());
                break;
            }
            default: {
                comparator = (timeSlotOne, timeSlotTwo) -> timeSlotOne.getId().compareTo(timeSlotTwo.getId());
            }

        }

        return comparator;

    }


}