package com.learning.service;


import com.learning.entity.mongo.StudentBatchCollection;
import com.learning.entity.mongo.StudentCollection;
import com.learning.entity.mysql.StudentBatchEntity;
import com.learning.entity.mysql.StudentEntity;
import com.learning.entity.mysql.TrainerEntity;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.model.StudentBatchModel;
import com.learning.repository.mongo.StudentBatchMongoRepository;
import com.learning.repository.mysql.StudentBatchRepository;
import com.learning.utility.excel.StudentBatchReader;
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
public class StudentBatchService{

    private final StudentBatchRepository jpaRepo;

    private final StudentBatchMongoRepository mongoRepo;

    private final ModelMapper modelMapper;

    private final StudentBatchReader studentBatchReader;

    public List<StudentBatchModel> getAllRecords() {
        List<StudentBatchCollection> StudentBatchCollectionsList = mongoRepo.findAll();
        List<StudentBatchEntity> studentBatchEntityList = jpaRepo.findAll();
        if (!CollectionUtils.isEmpty(StudentBatchCollectionsList)) {
            List<StudentBatchModel> studentBatchModelList = StudentBatchCollectionsList.stream()
                    .map(studentBatchCollection -> modelMapper
                            .map(studentBatchCollection, StudentBatchModel.class))
                    .collect(Collectors.toList());
            return studentBatchModelList;
        } else if (!CollectionUtils.isEmpty(studentBatchEntityList)) {
            List<StudentBatchModel> studentBatchModelList = studentBatchEntityList.stream()
                    .map(studentBatchEntity -> modelMapper
                            .map(studentBatchEntity, StudentBatchModel.class))
                    .collect(Collectors.toList());
            return studentBatchModelList;
        } else {
            return Collections.emptyList();
        }    }

    public List<StudentBatchModel> getLimitedRecords(int count) {
        List<StudentBatchCollection> studentBatchCollectionsList = mongoRepo.findAll();
        List<StudentBatchEntity> studentBatchEntityList = jpaRepo.findAll();
        if (!CollectionUtils.isEmpty(studentBatchCollectionsList)) {
            List<StudentBatchModel> studentBatchModelList = studentBatchEntityList.stream().limit(count)
                    .map(studentBatchCollection -> modelMapper
                            .map(studentBatchCollection, StudentBatchModel.class))
                    .collect(Collectors.toList());
            return studentBatchModelList;
        }else if (!CollectionUtils.isEmpty(studentBatchEntityList)){
            List<StudentBatchModel> studentBatchModelList = studentBatchEntityList.stream().limit(count)
                    .map(studentBatchEntity -> modelMapper
                            .map(studentBatchEntity, StudentBatchModel.class))
                    .collect(Collectors.toList());
            return studentBatchModelList;

        } else {
            return Collections.emptyList();
        }    
    }

    public List<StudentBatchModel> getSortedRecords(String sortBy) {
        List<StudentBatchCollection> studentBatchCollectionsList = mongoRepo.findAll();
        List<StudentBatchEntity> studentBatchEntityList = jpaRepo.findAll();
        if ((!CollectionUtils.isEmpty(studentBatchCollectionsList))){
            Comparator<StudentBatchCollection> comparator = findSuitableComparatorSortBy(sortBy);
            List<StudentBatchModel> studentBatchModelList = studentBatchEntityList.stream()
                    .map(studentBatchCollection -> modelMapper
                            .map(studentBatchCollection, StudentBatchModel.class))
                    .collect(Collectors.toList());
            return studentBatchModelList;
        }else if (!CollectionUtils.isEmpty(studentBatchEntityList)){
            Comparator<StudentBatchEntity> comparator = findSuitableComparator(sortBy);
            List<StudentBatchModel> studentBatchModelList = studentBatchEntityList.stream()
                    .map(studentBatchEntity -> modelMapper
                            .map(studentBatchEntity, StudentBatchModel.class))
                    .collect(Collectors.toList());
            return studentBatchModelList;

        } else {
            return Collections.emptyList();
        }
    }

    public StudentBatchModel saveRecord(StudentBatchModel record) {
        if (Objects.nonNull(record)) {
            StudentBatchEntity studentBatchEntity = new StudentBatchEntity();
            modelMapper.map(record, studentBatchEntity);
            jpaRepo.save(studentBatchEntity);

            Runnable runnable = () -> {
                StudentBatchCollection studentBatchCollection = new StudentBatchCollection();
                modelMapper.map(studentBatchEntity, studentBatchCollection);
                mongoRepo.save(studentBatchCollection);
            };
            CompletableFuture.runAsync(runnable);
        }
        return record;

    }

    public List<StudentBatchModel> saveAll(List<StudentBatchModel> studentBatchModelList) {
        if (!CollectionUtils.isEmpty(studentBatchModelList)) {
            List<StudentBatchEntity> studentBatchEntityList = studentBatchModelList.stream()
                    .map(studentBatchModel -> modelMapper.map(studentBatchModel, StudentBatchEntity.class))
                    .collect(Collectors.toList());
            jpaRepo.saveAll(studentBatchEntityList);

            Runnable runnable = () -> {
                List<StudentBatchCollection> studentBatchCollectionsList = studentBatchEntityList.stream()
                        .map(studentBatchEntity -> modelMapper.map(studentBatchEntity, StudentBatchCollection.class))
                        .collect(Collectors.toList());
                mongoRepo.saveAll(studentBatchCollectionsList);
            };
        }
        return studentBatchModelList;

    }

    public StudentBatchModel getRecordById(Long id) {
        if (mongoRepo.existsById(id)){
            StudentBatchCollection studentBatchCollection = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            return modelMapper.map(studentBatchCollection, StudentBatchModel.class);
        }
        StudentBatchEntity studentBatchEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        return modelMapper.map(studentBatchEntity, StudentBatchModel.class);

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

    public StudentBatchModel updatedRecordById(Long id, StudentBatchModel record) {
        StudentBatchEntity studentBatchEntity= jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, studentBatchEntity);
        jpaRepo.save(studentBatchEntity);
        log.info("update data mysql");

        CompletableFuture.runAsync(() -> {
            StudentBatchCollection studentBatchCollection = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            modelMapper.map(record, studentBatchCollection);
            mongoRepo.save(studentBatchCollection);
            log.info("update data mongo");
        });
        return record;
    }

    public void saveExcelFile(MultipartFile file) {
        if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            try {
                List<StudentBatchEntity> studentBatchEntityList = studentBatchReader.getStudentBatchObjects(file.getInputStream());
                jpaRepo.saveAll(studentBatchEntityList);
                log.info("saving data in mysql");

                CompletableFuture.runAsync(() -> {
                    List<StudentBatchCollection> studentBatchCollectionList = studentBatchEntityList.stream()
                            .map(studentBatchEntity -> modelMapper.map(studentBatchEntity, StudentBatchCollection.class))
                            .collect(Collectors.toList());
                    mongoRepo.saveAll(studentBatchCollectionList);
                    log.info("saving data in mongo");
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private Comparator<StudentBatchCollection> findSuitableComparatorSortBy(String sortBy){
        return null;
    }

    private Comparator<StudentBatchEntity> findSuitableComparator(String sortBy) {
        Comparator<StudentBatchEntity> comparator;
        switch (sortBy) {
            case "fees": {
                comparator = (studentBatchOne, studentBatchTwo) ->
                        studentBatchOne.getFees().compareTo(studentBatchTwo.getFees());
                break;
            }
            case "batchId": {
                comparator = (studentBatchOne, studentBatchTwo) ->
                        studentBatchOne.getBatchId().compareTo(studentBatchTwo.getBatchId());
                break;
            }
            default: {
                comparator = (studentBatchOne, studentBatchTwo) ->
                        studentBatchOne.getId().compareTo(studentBatchTwo.getId());
            }
        }
        return comparator;
    }


}
