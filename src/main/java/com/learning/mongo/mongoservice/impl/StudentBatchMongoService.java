package com.learning.mongo.mongoservice.impl;

import com.learning.constants.NumberConstants;
import com.learning.model.StudentBatchModel;
import com.learning.mongo.collections.StudentBatchCollection;
import com.learning.mongo.mongorepository.StudentBatchMongoRepository;

import com.learning.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentBatchMongoService implements CommonService<StudentBatchModel, Long> {

    private final StudentBatchMongoRepository studentBatchMongoRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<StudentBatchModel> getAllRecords() {
        List<StudentBatchCollection> studentBatchCollectionList = studentBatchMongoRepository.findAll();
        if (!CollectionUtils.isEmpty(studentBatchCollectionList)) {
            List<StudentBatchModel> studentBatchModelList = studentBatchCollectionList.stream().map(studentBatchCollection -> {
                StudentBatchModel studentBatchModel = new StudentBatchModel();
                modelMapper.map(studentBatchCollection, studentBatchModel);
                return studentBatchModel;
            }).collect(Collectors.toList());
            return studentBatchModelList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<StudentBatchModel> getLimitedRecords(int count) {
        List<StudentBatchCollection> studentBatchCollectionList = studentBatchMongoRepository.findAll();
        if (Objects.nonNull(studentBatchCollectionList) && studentBatchCollectionList.size() > NumberConstants.ZERO) {
            List<StudentBatchModel> studentBatchModelList = studentBatchCollectionList.stream()
                    .limit(count)
                    .map(studentBatchCollection -> {
                        StudentBatchModel studentBatchModel = modelMapper.map(studentBatchCollection, StudentBatchModel.class);
                        return studentBatchModel;
                    })
                    .collect(Collectors.toList());
            return studentBatchModelList;
        } else {
            return Collections.emptyList();
        }

    }

    @Override
    public List<StudentBatchModel> getSortedRecords(String sortBy) {
        List<StudentBatchCollection> studentBatchCollectionList = studentBatchMongoRepository.findAll();
        if (Objects.nonNull(studentBatchCollectionList) && studentBatchCollectionList.size() > NumberConstants.ZERO) {
            Comparator<StudentBatchCollection> comparator = findSuitableComparator(sortBy);
            List<StudentBatchModel> studentBatchModelList = studentBatchCollectionList.stream()
                    .sorted(comparator)
                    .map(studentBatchCollection -> {
                        StudentBatchModel studentBatchModel = modelMapper.map(studentBatchCollection, StudentBatchModel.class);
                        return studentBatchModel;
                    })
                    .collect(Collectors.toList());
            return studentBatchModelList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public StudentBatchModel saveRecord(StudentBatchModel record) {
        if (Objects.nonNull(record)) {
            StudentBatchCollection studentBatchCollection = new StudentBatchCollection();
            modelMapper.map(record, studentBatchCollection);
            studentBatchMongoRepository.save(studentBatchCollection);
        }
        return record;
    }

    @Override
    public List<StudentBatchModel> saveAll(List<StudentBatchModel> studentBatchModelList) {
        if (Objects.nonNull(studentBatchModelList) && studentBatchModelList.size() > NumberConstants.ZERO) {
            List<StudentBatchCollection> studentBatchCollectionList = studentBatchModelList.stream()
                    .map(studentBatchModel -> {
                        StudentBatchCollection collection = modelMapper.map(studentBatchModel, StudentBatchCollection.class);
                        return collection;
                    })
                    .collect(Collectors.toList());
            studentBatchMongoRepository.saveAll(studentBatchCollectionList);
        }
        return studentBatchModelList;

    }

    @Override
    public Optional<StudentBatchModel> getRecordById(Long id) {
        Optional<StudentBatchCollection> optionalCollection = studentBatchMongoRepository.findById(id);
        if (optionalCollection.isPresent()) {
            StudentBatchCollection studentBatchCollection = optionalCollection.get();
            StudentBatchModel studentBatchModel = modelMapper.map(studentBatchCollection, StudentBatchModel.class);
            return Optional.of(studentBatchModel);
        }
        return Optional.empty();

    }

    @Override
    public void deleteRecordById(Long id) {
        studentBatchMongoRepository.deleteById(id);

    }

    @Override
    public StudentBatchModel updatedRecordById(Long id, StudentBatchModel record) {
        Optional<StudentBatchCollection> optionalStudentBatchCollection = studentBatchMongoRepository.findById(id);
        if (optionalStudentBatchCollection.isPresent()) {
            StudentBatchCollection studentBatchCollection = optionalStudentBatchCollection.get();
            modelMapper.map(record, studentBatchCollection);
            studentBatchCollection.setId(id);
            studentBatchMongoRepository.save(studentBatchCollection);
        }
        return record;
    }

    private Comparator<StudentBatchCollection> findSuitableComparator(String sortBy) {
        Comparator<StudentBatchCollection> comparator;
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
