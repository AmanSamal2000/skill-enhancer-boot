package com.learning.mongo.mongoservice.impl;

import com.learning.constants.NumberConstants;
import com.learning.model.StudentModel;
import com.learning.mongo.collections.StudentCollection;
import com.learning.mongo.mongorepository.StudentMongoRepository;
import com.learning.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentMongoService implements CommonService<StudentModel, Long> {

    private final StudentMongoRepository studentMongoRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<StudentModel> getAllRecords() {
        List<StudentCollection> studentCollectionList = studentMongoRepository.findAll();
        if (!CollectionUtils.isEmpty(studentCollectionList)) {
            List<StudentModel> studentModelList = studentCollectionList.stream().map(studentCollection -> {
                StudentModel studentModel = new StudentModel();
                modelMapper.map(studentCollection, studentModel);
                return studentModel;
            }).collect(Collectors.toList());
            return studentModelList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<StudentModel> getLimitedRecords(int count) {
        List<StudentCollection> studentCollectionList = studentMongoRepository.findAll();
        if (Objects.nonNull(studentCollectionList) && studentCollectionList.size() > NumberConstants.ZERO) {
            List<StudentModel> studentModelList = studentCollectionList.stream()
                    .limit(count)
                    .map(studentCollection -> {
                        StudentModel studentModel = modelMapper.map(studentCollection, StudentModel.class);
                        return studentModel;
                    })
                    .collect(Collectors.toList());
            return studentModelList;
        } else {
            return Collections.emptyList();
        }
    }


    @Override
    public List<StudentModel> getSortedRecords(String sortBy) {
        List<StudentCollection> studentCollectionList = studentMongoRepository.findAll();
        if (Objects.nonNull(studentCollectionList) && studentCollectionList.size() > NumberConstants.ZERO) {
            Comparator<StudentCollection> comparator = findSuitableComparator(sortBy);
            List<StudentModel> studentModelList = studentCollectionList.stream()
                    .sorted(comparator)
                    .map(studentCollection -> {
                        StudentModel studentModel = modelMapper.map(studentCollection, StudentModel.class);
                        return studentModel;
                    })
                    .collect(Collectors.toList());
            return studentModelList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public StudentModel saveRecord(StudentModel record) {
        if (Objects.nonNull(record)) {
            StudentCollection studentCollection = new StudentCollection();
            modelMapper.map(record, studentCollection);
            studentMongoRepository.save(studentCollection);
        }
        return record;
    }

    @Override
    public List<StudentModel> saveAll(List<StudentModel> studentModelList) {
        if (Objects.nonNull(studentModelList) && studentModelList.size() > NumberConstants.ZERO) {
            List<StudentCollection> studentCollectionList = studentModelList.stream()
                    .map(studentModel -> {
                        StudentCollection collection = modelMapper.map(studentModel, StudentCollection.class);
                        return collection;
                    })
                    .collect(Collectors.toList());
            studentMongoRepository.saveAll(studentCollectionList);
        }
        return studentModelList;
    }


    @Override
    public Optional<StudentModel> getRecordById(Long id) {
        Optional<StudentCollection> optionalCollection = studentMongoRepository.findById(id);
        if (optionalCollection.isPresent()) {
            StudentCollection studentCollection = optionalCollection.get();
            StudentModel studentModel = modelMapper.map(studentCollection, StudentModel.class);
            return Optional.of(studentModel);
        }
        return Optional.empty();
    }

    @Override
    public void deleteRecordById(Long id) {
        studentMongoRepository.deleteById(id);
    }

    @Override
    public StudentModel updatedRecordById(Long id, StudentModel record) {
        Optional<StudentCollection> optionalStudentCollection = studentMongoRepository.findById(id);
        if (optionalStudentCollection.isPresent()) {
            StudentCollection studentCollection = optionalStudentCollection.get();
            modelMapper.map(record, studentCollection);
            studentCollection.setId(id);
            studentMongoRepository.save(studentCollection);
        }
        return record;
    }


    private Comparator<StudentCollection> findSuitableComparator(String sortBy) {
        Comparator<StudentCollection> comparator;
        switch (sortBy) {
            case "name": {
                comparator = (studentOne, studentTwo) ->
                        studentOne.getName().compareTo(studentTwo.getName());
                break;
            }
            case "email": {
                comparator = (studentOne, studentTwo) ->
                        studentOne.getEmail().compareTo(studentTwo.getEmail());
                break;
            }
            default: {
                comparator = (studentOne, studentTwo) ->
                        studentOne.getId().compareTo(studentTwo.getId());
            }
        }
        return comparator;
    }


}
