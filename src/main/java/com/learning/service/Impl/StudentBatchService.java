package com.learning.service.Impl;


import java.util.*;
import java.util.stream.Collectors;

import com.learning.constants.NumberConstants;
import com.learning.entity.StudentEntity;
import com.learning.model.StudentModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.learning.entity.StudentBatchEntity;
import com.learning.model.StudentBatchModel;
import com.learning.repository.StudentBatchRepository;
import com.learning.service.CommonService;


@Service
@RequiredArgsConstructor
public class StudentBatchService implements CommonService<StudentBatchModel, Long> {

    private final StudentBatchRepository studentBatchRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<StudentBatchModel> getAllRecords() {
        List<StudentBatchEntity> studentBatchEntityList = studentBatchRepository.findAll();
        if (!CollectionUtils.isEmpty(studentBatchEntityList)) {
            List<StudentBatchModel> studentBatchModelList = studentBatchEntityList.stream().map(studentBatchEntity -> {

                StudentBatchModel studentBatchModel = new StudentBatchModel();
                //BeanUtils.copyProperties(studentBatchEntity, studentBatchModel);
                modelMapper.map(studentBatchEntity, studentBatchModel);
                return studentBatchModel;
            }).collect(Collectors.toList());
            return studentBatchModelList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<StudentBatchModel> getLimitedRecords(int count) {
        List<StudentBatchEntity> studentBatchEntityList = studentBatchRepository.findAll();
        if (Objects.nonNull(studentBatchEntityList) && studentBatchEntityList.size() > NumberConstants.ZERO) {
            List<StudentBatchModel> studentBatchModelList = studentBatchEntityList.stream()
                    .limit(count)
                    .map(studentBatchEntity -> {
                        StudentBatchModel studentBatchModel = modelMapper.map(studentBatchEntity, StudentBatchModel.class);
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
        List<StudentBatchEntity> studentBatchEntityList = studentBatchRepository.findAll();
        if (Objects.nonNull(studentBatchEntityList) && studentBatchEntityList.size() > NumberConstants.ZERO) {
            Comparator<StudentBatchEntity> comparator = findSuitableComparator(sortBy);
            List<StudentBatchModel> studentBatchModelList = studentBatchEntityList.stream()
                    .sorted(comparator)
                    .map(studentBatchEntity -> {
                        StudentBatchModel studentBatchModel = modelMapper.map(studentBatchEntity, StudentBatchModel.class);
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
            StudentBatchEntity studentBatchEntity = new StudentBatchEntity();
            //BeanUtils.copyProperties(record, studentBatchEntity);
            modelMapper.map(record, studentBatchEntity);
            StudentBatchEntity savedObject = studentBatchRepository.save(studentBatchEntity);
        }
        return record;
    }

    @Override
    public List<StudentBatchModel> saveAll(List<StudentBatchModel> studentBatchModelList) {
        if (Objects.nonNull(studentBatchModelList) && studentBatchModelList.size() > NumberConstants.ZERO) {
            List<StudentBatchEntity> studentBatchEntityList = studentBatchModelList.stream()
                    .map(studentBatchModel -> {
                        StudentBatchEntity entity = modelMapper.map(studentBatchModel, StudentBatchEntity.class);
                        return entity;
                    })
                    .collect(Collectors.toList());
            studentBatchRepository.saveAll(studentBatchEntityList);
        }
        return studentBatchModelList;

    }

    @Override
    public Optional<StudentBatchModel> getRecordById(Long id) {
        Optional<StudentBatchEntity> optionalEntity = studentBatchRepository.findById(id);
        if (optionalEntity.isPresent()) {
            StudentBatchEntity studentBatchEntity = optionalEntity.get();
            StudentBatchModel studentBatchModel = modelMapper.map(studentBatchEntity, StudentBatchModel.class);
            return Optional.of(studentBatchModel);
        }
        return Optional.empty();

    }

    @Override
    public void deleteRecordById(Long id) {
        studentBatchRepository.deleteById(id);

    }

    @Override
    public StudentBatchModel updatedRecordById(Long id, StudentBatchModel record) {
        Optional<StudentBatchEntity> optionalStudentBatchEntity = studentBatchRepository.findById(id);
        if (optionalStudentBatchEntity.isPresent()) {
            StudentBatchEntity studentBatchEntity = optionalStudentBatchEntity.get();
            //BeanUtils.copyProperties(record, studentBatchEntity);
            modelMapper.map(record, studentBatchEntity);
            studentBatchEntity.setId(id);
            studentBatchRepository.save(studentBatchEntity);
        }
        return record;
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
