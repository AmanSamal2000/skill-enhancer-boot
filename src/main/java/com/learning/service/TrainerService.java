package com.learning.service;

import com.learning.entity.mongo.TrainerCollection;
import com.learning.entity.mysql.TimeSlotEntity;
import com.learning.entity.mysql.TrainerEntity;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.model.TrainerModel;
import com.learning.repository.mongo.TrainerMongoRepository;
import com.learning.repository.mysql.TrainerRepository;
import com.learning.utility.excel.TrainerReader;
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
public class TrainerService {

    private final TrainerRepository jpaRepo;
    
    private final TrainerMongoRepository mongoRepo;
    private final ModelMapper modelMapper;

    private final TrainerReader trainerReader;


    public List<TrainerModel> getAllRecords() {
        List<TrainerCollection> trainerCollectionsList = mongoRepo.findAll();
        List<TrainerEntity> trainerEntityList = jpaRepo.findAll();
        if (!CollectionUtils.isEmpty(trainerCollectionsList)) {
            List<TrainerModel> trainerModelList = trainerCollectionsList.stream()
                    .map(trainerCollection -> modelMapper.map(trainerCollection, TrainerModel.class))
                    .collect(Collectors.toList());
            return trainerModelList;
        } else if (!CollectionUtils.isEmpty(trainerEntityList)) {
            List<TrainerModel> trainerModelList = trainerEntityList.stream()
                    .map(trainerEntity -> modelMapper.map(trainerEntity, TrainerModel.class))
                    .collect(Collectors.toList());
            return trainerModelList;
        } else {
            return Collections.emptyList();
        }
    }

    public List<TrainerModel> getLimitedRecords(int count) {
        List<TrainerCollection> trainerCollectionsList = mongoRepo.findAll();
        List<TrainerEntity> trainerEntityList = jpaRepo.findAll();
        if (!CollectionUtils.isEmpty(trainerCollectionsList)) {
            List<TrainerModel> trainerModelList = trainerEntityList.stream().limit(count)
                    .map(trainerCollection -> modelMapper.map(trainerCollection, TrainerModel.class))
                    .collect(Collectors.toList());
            return trainerModelList;
        }else if (!CollectionUtils.isEmpty(trainerEntityList)){
            List<TrainerModel> trainerModelList = trainerEntityList.stream().limit(count)
                    .map(trainerEntity -> modelMapper.map(trainerEntity, TrainerModel.class))
                    .collect(Collectors.toList());
            return trainerModelList;

        } else {
            return Collections.emptyList();
        }

    }

    public List<TrainerModel> getSortedRecords(String sortBy) {
        List<TrainerCollection> trainerCollectionsList = mongoRepo.findAll();
        List<TrainerEntity> trainerEntityList = jpaRepo.findAll();
        if ((!CollectionUtils.isEmpty(trainerCollectionsList))){
            Comparator<TrainerCollection> comparator = findSuitableComparatorSortBy(sortBy);
            List<TrainerModel> trainerModelList = trainerEntityList.stream()
                    .map(trainerCollection -> modelMapper.map(trainerCollection, TrainerModel.class))
                    .collect(Collectors.toList());
            return trainerModelList;
        }else if (!CollectionUtils.isEmpty(trainerEntityList)){
            Comparator<TrainerEntity> comparator = findSuitableComparator(sortBy);
            List<TrainerModel> trainerModelList = trainerEntityList.stream()
                    .map(trainerEntity -> modelMapper.map(trainerEntity, TrainerModel.class))
                    .collect(Collectors.toList());
            return trainerModelList;

        } else {
            return Collections.emptyList();
        }
    }

    public TrainerModel saveRecord(TrainerModel record) {
        if (Objects.nonNull(record)) {
            TrainerEntity trainerEntity = new TrainerEntity();
            modelMapper.map(record, trainerEntity);
            jpaRepo.save(trainerEntity);

            Runnable runnable = () -> {
                TrainerCollection trainerCollection = new TrainerCollection();
                modelMapper.map(trainerEntity, trainerCollection);
                mongoRepo.save(trainerCollection);
            };
            CompletableFuture.runAsync(runnable);
        }
        return record;
    }

    public List<TrainerModel> saveAll(List<TrainerModel> trainerModelList) {
        if (!CollectionUtils.isEmpty(trainerModelList)) {
            List<TrainerEntity> trainerEntityList = trainerModelList.stream()
                    .map(trainerModel -> modelMapper.map(trainerModel, TrainerEntity.class))
                    .collect(Collectors.toList());
            jpaRepo.saveAll(trainerEntityList);

            Runnable runnable = () -> {
                List<TrainerCollection> trainerCollectionsList = trainerEntityList.stream()
                        .map(trainerEntity -> modelMapper.map(trainerEntity, TrainerCollection.class))
                        .collect(Collectors.toList());
                mongoRepo.saveAll(trainerCollectionsList);
            };
        }
        return trainerModelList;

    }

    public TrainerModel getRecordById(Long id) {
        if (mongoRepo.existsById(id)){
            TrainerCollection trainerCollection = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            return modelMapper.map(trainerCollection, TrainerModel.class);
        }
        TrainerEntity trainerEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        return modelMapper.map(trainerEntity, TrainerModel.class);    
    }

    public void deleteRecordById(Long id) {
        if (jpaRepo.existsById(id)) {
            jpaRepo.deleteById(id);
            log.info("deleted successfully ");

            CompletableFuture.runAsync(() -> {
                mongoRepo.deleteById(id);
                log.info("delete Successfully mongo");
            });
        }
    }

    public TrainerModel updatedRecordById(Long id, TrainerModel record) {
        TrainerEntity trainerEntity= jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, trainerEntity);
        jpaRepo.save(trainerEntity);
        log.info("update data mysql");

        CompletableFuture.runAsync(() -> {
            TrainerCollection trainerCollection = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            modelMapper.map(record, trainerCollection);
            mongoRepo.save(trainerCollection);
            log.info("update data mongo");
        });
        return record;
    }


    public void saveExcelFile(MultipartFile file) {
        if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            try {
                List<TrainerEntity> trainerEntityList = trainerReader.getTrainerObjects(file.getInputStream());
                jpaRepo.saveAll(trainerEntityList);
                log.info("saving data in mysql");

                CompletableFuture.runAsync(() -> {
                    List<TrainerCollection> trainerCollectionList = trainerEntityList.stream()
                            .map(trainerEntity -> modelMapper.map(trainerEntity, TrainerCollection.class))
                            .collect(Collectors.toList());
                    mongoRepo.saveAll(trainerCollectionList);
                    log.info("saving data in mongo");
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private Comparator<TrainerCollection> findSuitableComparatorSortBy(String sortBy){
        return null;
    }

    private Comparator<TrainerEntity> findSuitableComparator(String sortBy) {
        Comparator<TrainerEntity> comparator;
        switch (sortBy) {
            case "name": {
                comparator = (trainerOne, trainerTwo) -> trainerOne.getName().compareTo(trainerTwo.getName());
                break;
            }
            case "specialization": {
                comparator = (trainerOne, trainerTwo) -> trainerOne.getSpecialization()
                        .compareTo(trainerTwo.getSpecialization());
                break;
            }
            default: {
                comparator = (trainerOne, trainerTwo) -> trainerOne.getId().compareTo(trainerTwo.getId());
            }
        }
        return comparator;

    }
}
