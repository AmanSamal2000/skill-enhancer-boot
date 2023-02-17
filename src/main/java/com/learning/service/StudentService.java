package com.learning.service;

import com.learning.entity.mongo.StudentCollection;
import com.learning.entity.mysql.StudentEntity;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.model.StudentModel;
import com.learning.repository.mongo.StudentMongoRepository;
import com.learning.repository.mysql.StudentRepository;
import com.learning.utility.email.EmailSender;
import com.learning.utility.excel.StudentReader;
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
public class StudentService {

    private final StudentRepository jpaRepo;
    private final StudentMongoRepository mongoRepo;
    private final ModelMapper modelMapper;
    private final StudentReader studentReader;
    private final EmailSender emailSender;

    public List<StudentModel> getAllRecords() {
        List<StudentCollection> studentCollectionsList = mongoRepo.findAll();
        List<StudentEntity> studentEntityList = jpaRepo.findAll();
        if (!CollectionUtils.isEmpty(studentCollectionsList)) {
            List<StudentModel> studentModelList = studentCollectionsList.stream()
                    .map(studentCollection -> modelMapper.map(studentCollection, StudentModel.class))
                    .collect(Collectors.toList());
            return studentModelList;
        } else if (!CollectionUtils.isEmpty(studentEntityList)) {
            List<StudentModel> studentModelList = studentEntityList.stream()
                    .map(studentEntity -> modelMapper.map(studentEntity, StudentModel.class))
                    .collect(Collectors.toList());
            return studentModelList;
        } else {
            return Collections.emptyList();
        }
    }

    public List<StudentModel> getLimitedRecords(int count) {
        List<StudentCollection> studentCollectionsList = mongoRepo.findAll();
        List<StudentEntity> studentEntityList = jpaRepo.findAll();
        if (!CollectionUtils.isEmpty(studentCollectionsList)) {
            List<StudentModel> studentModelList = studentEntityList.stream()
                    .limit(count)
                    .map(studentCollections -> modelMapper.map(studentCollections, StudentModel.class))
                    .collect(Collectors.toList());
            return studentModelList;
        }else if (!CollectionUtils.isEmpty(studentEntityList)){
            List<StudentModel> studentModelList = studentEntityList.stream()
                    .limit(count)
                    .map(studentEntity -> modelMapper.map(studentEntity, StudentModel.class))
                    .collect(Collectors.toList());
                return studentModelList;

        } else {
            return Collections.emptyList();
        }
    }

    public List<StudentModel> getSortedRecords(String sortBy) {
        List<StudentCollection> studentCollectionsList = mongoRepo.findAll();
        List<StudentEntity> studentEntityList = jpaRepo.findAll();
        if ((!CollectionUtils.isEmpty(studentCollectionsList))){
            Comparator<StudentCollection> comparator = findsuitableComparatorSortBy(sortBy);
        List<StudentModel> studentModelList = studentEntityList.stream()
                .map(studentCollections -> modelMapper.map(studentCollections, StudentModel.class))
                .collect(Collectors.toList());
        return studentModelList;
    }else if (!CollectionUtils.isEmpty(studentEntityList)){
            Comparator<StudentEntity> comparator = findSuitableComparator(sortBy);
        List<StudentModel> studentModelList = studentEntityList.stream()
                .map(studentEntity -> modelMapper.map(studentEntity, StudentModel.class))
                .collect(Collectors.toList());
        return studentModelList;

    } else {
        return Collections.emptyList();
    }
    }

    public StudentModel saveRecord(StudentModel record) {
        if (Objects.nonNull(record)) {
            StudentEntity studentEntity = new StudentEntity();
            modelMapper.map(record, studentEntity);
            jpaRepo.save(studentEntity);
            log.info("Data save Successfully mysql");

            Runnable runnable = () -> {
                StudentCollection studentCollection = new StudentCollection();
                modelMapper.map(studentEntity, studentCollection);
                mongoRepo.save(studentCollection);
                log.info("Data save Successfully mongo");
            };
            CompletableFuture.runAsync(runnable);
        }
        return record;
    }

    public List<StudentModel> saveAll(List<StudentModel> studentModelList) {
        if (!CollectionUtils.isEmpty(studentModelList)) {
            List<StudentEntity> studentEntityList = studentModelList.stream()
                    .map(studentModel -> modelMapper.map(studentModel, StudentEntity.class))
                    .collect(Collectors.toList());
            jpaRepo.saveAll(studentEntityList);
            log.info("Data all save Successfully mysql");


            Runnable runnable = () -> {
                List<StudentCollection> studentCollectionsList = studentEntityList.stream()
                        .map(studentEntity -> modelMapper.map(studentEntity, StudentCollection.class))
                        .collect(Collectors.toList());
                mongoRepo.saveAll(studentCollectionsList);
                log.info("Data all save Successfully mongo");
            };
        }
        return studentModelList;
    }


    public StudentModel getRecordById(Long id) {
        if (mongoRepo.existsById(id)){
            StudentCollection studentCollection = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            return modelMapper.map(studentCollection, StudentModel.class);

        }
        StudentEntity studentEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
               return modelMapper.map(studentEntity, StudentModel.class);

    }

    //TODO : add log in all methods
    public void deleteRecordById(Long id) {
        if (jpaRepo.existsById(id)) {
            jpaRepo.deleteById(id);
            log.info("deleted successfully mysql");

            CompletableFuture.runAsync(() -> {
                mongoRepo.deleteById(id);
                log.info("delete Successfully mongo");
            });
        }
        log.error(ErrorMessages.COULD_NOT_DELETE_RECORD.getErrorMessage());
    }

    public StudentModel updatedRecordById(Long id, StudentModel record) {
        StudentEntity studentEntity= jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            modelMapper.map(record, studentEntity);
            jpaRepo.save(studentEntity);
            log.info("update data mysql");

            CompletableFuture.runAsync(() -> {
                StudentCollection studentCollection = mongoRepo.findById(id)
                         .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
                modelMapper.map(record, studentCollection);
                mongoRepo.save(studentCollection);
                log.info("update data mongo");
        });
        return record;
    }


    public void saveExcelFile(MultipartFile file) {
        if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            try {
                List<StudentEntity> studentEntityList = studentReader.getStudentObjects(file.getInputStream());
                jpaRepo.saveAll(studentEntityList);
                log.info("saving data in mysql");

                CompletableFuture.runAsync(() -> {
                    List<StudentCollection> studentCollectionList = studentEntityList.stream()
                            .map(studentEntity -> modelMapper.map(studentEntity, StudentCollection.class))
                            .collect(Collectors.toList());
                    mongoRepo.saveAll(studentCollectionList);
                    log.info("saving data in mongo");
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void emailSender() {

        List<String> emails = jpaRepo.findEmails();
        emailSender.sendMailWithAttachment(emails);
    }

    public void sentEmailWithAttachment() {
        List<String> emails = jpaRepo.findEmails();
        emailSender.sendMailWithAttachment(emails);

    }
    private Comparator<StudentCollection> findsuitableComparatorSortBy(String sortBy){
        return null;
    }


    private Comparator<StudentEntity> findSuitableComparator(String sortBy) {
        Comparator<StudentEntity> comparator;
        switch (sortBy) {
            case "name": {
                comparator = (studentOne, studentTwo) -> studentOne.getName().compareTo(studentTwo.getName());
                break;
            }
            case "email": {
                comparator = (studentOne, studentTwo) -> studentOne.getEmail().compareTo(studentTwo.getEmail());
                break;
            }
            default: {
                comparator = (studentOne, studentTwo) -> studentOne.getId().compareTo(studentTwo.getId());
            }
        }
        return comparator;
    }


}


//    public StudentModel saveRecordInMongo(StudentModel studentModel) {
//        if (Objects.nonNull(studentModel)) {
//            StudentCollection studentCollection = new StudentCollection();
//            modelMapper.map(studentModel, studentCollection);
//            mongoRepo.save(studentCollection);
//        }
//        return studentModel;
//    }
//

