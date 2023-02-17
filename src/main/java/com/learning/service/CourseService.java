package com.learning.service;


import com.learning.entity.mongo.CourseCollection;
import com.learning.entity.mongo.CourseCollection;
import com.learning.entity.mysql.CourseEntity;
import com.learning.entity.mysql.StudentEntity;
import com.learning.enums.ErrorMessages;
import com.learning.exceptions.DataNotFoundException;
import com.learning.model.CourseModel;
import com.learning.repository.mongo.CourseMongoRepository;
import com.learning.repository.mysql.CourseRepository;
import com.learning.utility.excel.CourseReader;
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
public class CourseService {

    private final CourseRepository jpaRepo;
    private final CourseMongoRepository mongoRepo;
    private final ModelMapper modelMapper;
    private final CourseReader courseReader;

    public List<CourseModel> getAllRecords() {
        List<CourseCollection> courseCollectionsList = mongoRepo.findAll();
        List<CourseEntity> courseEntityList = jpaRepo.findAll();
        if (!CollectionUtils.isEmpty(courseCollectionsList)) {
            List<CourseModel> courseModelList = courseCollectionsList.stream()
                    .map(courseCollection -> modelMapper.map(courseCollection, CourseModel.class))
                    .collect(Collectors.toList());
            return courseModelList;
        } else if (!CollectionUtils.isEmpty(courseEntityList)) {
            List<CourseModel> courseModelList = courseEntityList.stream()
                    .map(courseEntity -> modelMapper.map(courseEntity, CourseModel.class))
                    .collect(Collectors.toList());
            return courseModelList;
        } else {
            return Collections.emptyList();
        }    }

    public List<CourseModel> getLimitedRecords(int count) {
        List<CourseCollection> courseCollectionsList = mongoRepo.findAll();
        List<CourseEntity> courseEntityList = jpaRepo.findAll();
        if (!CollectionUtils.isEmpty(courseCollectionsList)) {
            List<CourseModel> courseModelList = courseEntityList.stream().limit(count)
                    .map(courseCollection -> modelMapper.map(courseCollection, CourseModel.class))
                    .collect(Collectors.toList());
            return courseModelList;
        }else if (!CollectionUtils.isEmpty(courseEntityList)){
            List<CourseModel> courseModelList = courseEntityList.stream().limit(count)
                    .map(courseEntity -> modelMapper.map(courseEntity, CourseModel.class))
                    .collect(Collectors.toList());
            return courseModelList;

        } else {
            return Collections.emptyList();
        }

    }

    public List<CourseModel> getSortedRecords(String sortBy) {
        List<CourseCollection> courseCollectionsList = mongoRepo.findAll();
        List<CourseEntity> courseEntityList = jpaRepo.findAll();
        if ((!CollectionUtils.isEmpty(courseCollectionsList))){
            Comparator<CourseCollection> comparator = findSuitableComparatorSortBy(sortBy);
            List<CourseModel> courseModelList = courseEntityList.stream()
                    .map(courseCollection -> modelMapper.map(courseCollection, CourseModel.class))
                    .collect(Collectors.toList());
            return courseModelList;
        }else if (!CollectionUtils.isEmpty(courseEntityList)){
            Comparator<CourseEntity> comparator = findSuitableComparator(sortBy);
            List<CourseModel> courseModelList = courseEntityList.stream()
                    .map(courseEntity -> modelMapper.map(courseEntity, CourseModel.class))
                    .collect(Collectors.toList());
            return courseModelList;

        } else {
            return Collections.emptyList();
        }
    }

    public CourseModel saveRecord(CourseModel record) {
        if (Objects.nonNull(record)) {
            CourseEntity courseEntity = new CourseEntity();
            modelMapper.map(record, courseEntity);
            jpaRepo.save(courseEntity);

            Runnable runnable = () -> {
                CourseCollection courseCollection = new CourseCollection();
                modelMapper.map(courseEntity, courseCollection);
                mongoRepo.save(courseCollection);
            };
            CompletableFuture.runAsync(runnable);
        }
        return record;
    }

    public List<CourseModel> saveAll(List<CourseModel> courseModelList) {
        if (!CollectionUtils.isEmpty(courseModelList)) {
            List<CourseEntity> courseEntityList = courseModelList.stream()
                    .map(courseModel -> modelMapper.map(courseModel, CourseEntity.class))
                    .collect(Collectors.toList());
            jpaRepo.saveAll(courseEntityList);

            Runnable runnable = () -> {
                List<CourseCollection> courseCollectionsList = courseEntityList.stream()
                        .map(courseEntity -> modelMapper.map(courseEntity, CourseCollection.class))
                        .collect(Collectors.toList());
                mongoRepo.saveAll(courseCollectionsList);
            };
        }
        return courseModelList;
    }

    public CourseModel getRecordById(Long id) {
        if (mongoRepo.existsById(id)){
            CourseCollection courseCollection = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            return modelMapper.map(courseCollection, CourseModel.class);
        }
        CourseEntity courseEntity = jpaRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        return modelMapper.map(courseEntity, CourseModel.class);

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
    }

    public CourseModel updatedRecordById(Long id, CourseModel record) {
        CourseEntity courseEntity= jpaRepo.findById(id).orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        modelMapper.map(record, courseEntity);
        jpaRepo.save(courseEntity);
        log.info("update data mysql");

        CompletableFuture.runAsync(() -> {
            CourseCollection courseCollection = mongoRepo.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
            modelMapper.map(record, courseCollection);
            mongoRepo.save(courseCollection);
            log.info("update data mongo");
        });
        return record;
    }

    public void saveExcelFile(MultipartFile file) {
        if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            try {
                List<CourseEntity> courseEntityList = courseReader.getCourseObjects(file.getInputStream());
                jpaRepo.saveAll(courseEntityList);
                log.info("saving data in mysql");

                CompletableFuture.runAsync(() -> {
                    List<CourseCollection> courseCollectionList = courseEntityList.stream()
                            .map(courseEntity -> modelMapper.map(courseEntity, CourseCollection.class))
                            .collect(Collectors.toList());
                    mongoRepo.saveAll(courseCollectionList);
                    log.info("saving data in mongo");
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

 private Comparator<CourseCollection> findSuitableComparatorSortBy(String sortBy){
        return null;
 }
    private Comparator<CourseEntity> findSuitableComparator(String sortBy) {
        Comparator<CourseEntity> comparator;
        switch (sortBy) {
            case "name": {
                comparator = (courseOne, courseTwo) -> courseOne.getName().compareTo(courseTwo.getName());
                break;
            }
            case "duration": {
                comparator = (courseOne, courseTwo) -> courseOne.getDuration().compareTo(courseTwo.getDuration());
                break;
            }
            default: {
                comparator = (courseOne, courseTwo) -> courseOne.getId().compareTo(courseTwo.getId());
            }
        }
        return comparator;
    }


}
