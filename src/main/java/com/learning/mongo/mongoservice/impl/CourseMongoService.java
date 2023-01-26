package com.learning.mongo.mongoservice.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.learning.constants.NumberConstants;
import com.learning.mongo.collections.CourseCollection;
import com.learning.mongo.mongorepository.CourseMongoRepository;
import com.learning.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.learning.model.CourseModel;


@Service
@RequiredArgsConstructor
public class CourseMongoService implements CommonService<CourseModel, Long> {

    private final CourseMongoRepository courseMongoRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<CourseModel> getAllRecords() {
        List<CourseCollection> courseCollectionList = courseMongoRepository.findAll();
        if (!CollectionUtils.isEmpty(courseCollectionList)) {
            List<CourseModel> courseModelList = courseCollectionList.stream().map(courseCollection -> {
                CourseModel courseModel = new CourseModel();
                modelMapper.map(courseCollection, courseModel);
                return courseModel;
            }).collect(Collectors.toList());
            return courseModelList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<CourseModel> getLimitedRecords(int count) {
        List<CourseCollection> courseCollectionList = courseMongoRepository.findAll();
        if (Objects.nonNull(courseCollectionList) && courseCollectionList.size() > NumberConstants.ZERO) {
            List<CourseModel> courseModelList = courseCollectionList.stream().limit(count).map(courseCollection -> {
                CourseModel courseModel = modelMapper.map(courseCollection, CourseModel.class);
                return courseModel;
            }).collect(Collectors.toList());
            return courseModelList;
        } else {
            return Collections.emptyList();

        }
    }

    @Override
    public List<CourseModel> getSortedRecords(String sortBy) {
        List<CourseCollection> courseCollectionList = courseMongoRepository.findAll();
        if (Objects.nonNull(courseCollectionList) && courseCollectionList.size() > NumberConstants.ZERO) {
            Comparator<CourseCollection> comparator = findSuitableComparator(sortBy);
            List<CourseModel> courseModelList = courseCollectionList.stream().sorted(comparator).map(courseCollection -> {
                CourseModel courseModel = modelMapper.map(courseCollection, CourseModel.class);
                return courseModel;
            }).collect(Collectors.toList());
            return courseModelList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public CourseModel saveRecord(CourseModel record) {
        if (Objects.nonNull(record)) {
            CourseCollection courseCollection = new CourseCollection();
            modelMapper.map(record, courseCollection);
            CourseCollection savedObject = courseMongoRepository.save(courseCollection);
        }
        return record;
    }

    @Override
    public List<CourseModel> saveAll(List<CourseModel> courseModelList) {
        if (Objects.nonNull(courseModelList) && courseModelList.size() > NumberConstants.ZERO) {
            List<CourseCollection> courseCollectionList = courseModelList.stream().map(courseModel -> {
                CourseCollection collection = modelMapper.map(courseModel, CourseCollection.class);
                return collection;
            }).collect(Collectors.toList());
            courseMongoRepository.saveAll(courseCollectionList);
        }
        return courseModelList;
    }

    @Override
    public Optional<CourseModel> getRecordById(Long id) {
        Optional<CourseCollection> optionalCollection = courseMongoRepository.findById(id);
        if (optionalCollection.isPresent()) {
            CourseCollection courseCollection = optionalCollection.get();
            CourseModel courseModel = modelMapper.map(courseCollection, CourseModel.class);
            return Optional.of(courseModel);
        }
        return Optional.empty();

    }

    @Override
    public void deleteRecordById(Long id) {
        courseMongoRepository.deleteById(id);

    }

    @Override
    public CourseModel updatedRecordById(Long id, CourseModel record) {
        Optional<CourseCollection> optionalCourseCollection = courseMongoRepository.findById(id);
        if (optionalCourseCollection.isPresent()) {
            CourseCollection courseCollection = optionalCourseCollection.get();
            modelMapper.map(record, courseCollection);
            courseCollection.setId(id);
            courseMongoRepository.save(courseCollection);
        }
        return record;
    }


    private Comparator<CourseCollection> findSuitableComparator(String sortBy) {
        Comparator<CourseCollection> comparator;
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
