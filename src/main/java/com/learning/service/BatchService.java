package com.learning.service;



import com.learning.entity.mongo.BatchCollection;
import com.learning.entity.mongo.StudentCollection;
import com.learning.entity.mysql.BatchEntity;
import com.learning.entity.mysql.StudentEntity;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.model.BatchModel;
import com.learning.repository.mongo.BatchMongoRepository;
import com.learning.repository.mysql.BatchRepository;
import com.learning.utility.excel.BatchReader;
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
public class BatchService{

    private final BatchRepository jpaRepo;
    
    private final BatchMongoRepository mongoRepo;
    private final ModelMapper modelMapper;
    private final BatchReader batchReader;

    public List<BatchModel> getAllRecords() {
        List<BatchCollection> batchCollectionsList = mongoRepo.findAll();
        List<BatchEntity> batchEntityList = jpaRepo.findAll();
        if (!CollectionUtils.isEmpty(batchCollectionsList)) {
            List<BatchModel> batchModelList = batchCollectionsList.stream()
                    .map(batchCollection -> modelMapper.map(batchCollection, BatchModel.class))
                    .collect(Collectors.toList());
            return batchModelList;
        } else if (!CollectionUtils.isEmpty(batchEntityList)) {
            List<BatchModel> batchModelList = batchEntityList.stream()
                    .map(batchEntity -> modelMapper.map(batchEntity, BatchModel.class))
                    .collect(Collectors.toList());
            return batchModelList;
        } else {
            return Collections.emptyList();
        }
    }

    public List<BatchModel> getLimitedRecords(int count) {
        List<BatchCollection> batchCollectionsList = mongoRepo.findAll();
        List<BatchEntity> batchEntityList = jpaRepo.findAll();
        if (!CollectionUtils.isEmpty(batchCollectionsList)) {
            List<BatchModel> batchModelList = batchEntityList.stream()
                    .limit(count)
                    .map(batchCollection -> modelMapper.map(batchCollection, BatchModel.class))
                    .collect(Collectors.toList());
            return batchModelList;
        }else if (!CollectionUtils.isEmpty(batchEntityList)){
            List<BatchModel> batchModelList = batchEntityList.stream()
                    .limit(count)
                    .map(batchEntity -> modelMapper.map(batchEntity, BatchModel.class))
                    .collect(Collectors.toList());
            return batchModelList;

        } else {
            return Collections.emptyList();
        }    }


    public List<BatchModel> getSortedRecords(String sortBy) {
        List<BatchCollection> batchCollectionsList = mongoRepo.findAll();
        List<BatchEntity> batchEntityList = jpaRepo.findAll();
        if ((!CollectionUtils.isEmpty(batchCollectionsList))){
            Comparator<BatchCollection> comparator = findSuitableComparatorSortBy(sortBy);
            List<BatchModel> batchModelList = batchEntityList.stream()
                    .map(batchCollection -> modelMapper.map(batchCollection, BatchModel.class))
                    .collect(Collectors.toList());
            return batchModelList;
        }else if (!CollectionUtils.isEmpty(batchEntityList)){
            Comparator<BatchEntity> comparator = findSuitableComparator(sortBy);
            List<BatchModel> batchModelList = batchEntityList.stream()
                    .map(batchEntity -> modelMapper.map(batchEntity, BatchModel.class))
                    .collect(Collectors.toList());
            return batchModelList;

        } else {
            return Collections.emptyList();
        }
    }

    public BatchModel saveRecord(BatchModel record) {
        if (Objects.nonNull(record)) {
            BatchEntity batchEntity = new BatchEntity();
            modelMapper.map(record, batchEntity);
            jpaRepo.save(batchEntity);

            Runnable runnable = () -> {
                BatchCollection batchCollection = new BatchCollection();
                modelMapper.map(batchEntity, batchCollection);
                mongoRepo.save(batchCollection);
            };
            CompletableFuture.runAsync(runnable);
        }
        return record;
    }

    public List<BatchModel> saveAll(List<BatchModel> batchModelList) {
        if (!CollectionUtils.isEmpty(batchModelList)) {
            List<BatchEntity> batchEntityList = batchModelList.stream()
                    .map(batchModel -> modelMapper.map(batchModel, BatchEntity.class))
                    .collect(Collectors.toList());
            jpaRepo.saveAll(batchEntityList);

            Runnable runnable = () -> {
                List<BatchCollection> batchCollectionsList = batchEntityList.stream()
                        .map(batchEntity -> modelMapper.map(batchEntity, BatchCollection.class))
                        .collect(Collectors.toList());
                mongoRepo.saveAll(batchCollectionsList);
            };
        }
        return batchModelList;
    }

    public BatchModel getRecordById(Long id) {
        if (mongoRepo.existsById(id)){
            BatchCollection batchCollection = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            return modelMapper.map(batchCollection, BatchModel.class);
        }
        BatchEntity batchEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        return modelMapper.map(batchEntity, BatchModel.class);
    }

    public void deleteRecordById(Long id) {
        if (jpaRepo.existsById(id)) {
            jpaRepo.deleteById(id);
            log.info("deleted successfully");

            CompletableFuture.runAsync(() -> {
                mongoRepo.deleteById(id);
                log.info("delete Successfully mongo");
            });
        }
        jpaRepo.deleteById(id);

    }

    public BatchModel updatedRecordById(Long id, BatchModel record) {
        BatchEntity batchEntity= jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, batchEntity);
        jpaRepo.save(batchEntity);
        log.info("update data mysql");

        CompletableFuture.runAsync(() -> {
            BatchCollection batchCollection = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            modelMapper.map(record, batchCollection);
            mongoRepo.save(batchCollection);
            log.info("update data mongo");
        });
        return record;
    }

    public void saveExcelFile(MultipartFile file) {
        if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            try {
                List<BatchEntity> batchEntityList = batchReader.getBatchObjects(file.getInputStream());
                jpaRepo.saveAll(batchEntityList);
                log.info("saving data in mysql");

                CompletableFuture.runAsync(() -> {
                    List<BatchCollection> batchCollectionList = batchEntityList.stream()
                            .map(batchEntity -> modelMapper.map(batchEntity, BatchCollection.class))
                            .collect(Collectors.toList());
                    mongoRepo.saveAll(batchCollectionList);
                    log.info("saving data in mongo");
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Comparator<BatchCollection> findSuitableComparatorSortBy(String sortBy){
        return null;
    }
    private Comparator<BatchEntity> findSuitableComparator(String sortBy) {
        Comparator<BatchEntity> comparator;
        switch (sortBy) {
            case "studentCount": {
                comparator = (batchOne, batchTwo) -> batchOne.getStudentCount().compareTo(batchTwo.getStudentCount());
                break;
            }
            case "timeSlotId": {
                comparator = (batchOne, batchTwo) -> batchOne.getTimeSlotId().compareTo(batchTwo.getTimeSlotId());
                break;
            }
            default: {
                comparator = (batchOne, batchTwo) -> batchOne.getId().compareTo(batchTwo.getId());
            }
        }
        return comparator;
    }

}
