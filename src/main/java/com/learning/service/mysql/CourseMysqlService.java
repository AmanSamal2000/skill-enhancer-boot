package com.learning.service.mysql;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.learning.constants.NumberConstants;
import com.learning.entity.mysql.TrainerEntity;
import com.learning.utility.excel.CourseReader;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.learning.entity.mysql.CourseEntity;
import com.learning.model.CourseModel;
import com.learning.repository.mysql.CourseRepository;
import com.learning.service.common.CommonService;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CourseMysqlService implements CommonService<CourseModel, Long> {

    private final CourseRepository courseRepository;

    private final ModelMapper modelMapper;

    private final CourseReader courseReader;

    @Override
    public List<CourseModel> getAllRecords() {
        List<CourseEntity> courseEntityList = courseRepository.findAll();
        if (!CollectionUtils.isEmpty(courseEntityList)) {
            List<CourseModel> courseModelList = courseEntityList.stream().map(courseEntity -> {

                CourseModel courseModel = new CourseModel();
                //BeanUtils.copyProperties(courseEntity, courseModel);
                modelMapper.map(courseEntity, courseModel);
                return courseModel;
            }).collect(Collectors.toList());
            return courseModelList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<CourseModel> getLimitedRecords(int count) {
        List<CourseEntity> courseEntityList = courseRepository.findAll();
        if (Objects.nonNull(courseEntityList) && courseEntityList.size() > NumberConstants.ZERO) {
            List<CourseModel> courseModelList = courseEntityList.stream().limit(count).map(courseEntity -> {
                CourseModel courseModel = modelMapper.map(courseEntity, CourseModel.class);
                return courseModel;
            }).collect(Collectors.toList());
            return courseModelList;
        } else {
            return Collections.emptyList();

        }
    }

    @Override
    public List<CourseModel> getSortedRecords(String sortBy) {
        List<CourseEntity> courseEntityList = courseRepository.findAll();
        if (Objects.nonNull(courseEntityList) && courseEntityList.size() > NumberConstants.ZERO) {
            Comparator<CourseEntity> comparator = findSuitableComparator(sortBy);
            List<CourseModel> courseModelList = courseEntityList.stream().sorted(comparator).map(courseEntity -> {
                CourseModel courseModel = modelMapper.map(courseEntity, CourseModel.class);
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
            CourseEntity courseEntity = new CourseEntity();
            //BeanUtils.copyProperties(record, courseEntity);
            modelMapper.map(record, courseEntity);
            CourseEntity savedObject = courseRepository.save(courseEntity);
        }
        return record;
    }

    @Override
    public List<CourseModel> saveAll(List<CourseModel> courseModelList) {
        if (Objects.nonNull(courseModelList) && courseModelList.size() > NumberConstants.ZERO) {
            List<CourseEntity> courseEntityList = courseModelList.stream().map(courseModel -> {
                CourseEntity entity = modelMapper.map(courseModel, CourseEntity.class);
                return entity;
            }).collect(Collectors.toList());
            courseRepository.saveAll(courseEntityList);
        }
        return courseModelList;
    }

    @Override
    public Optional<CourseModel> getRecordById(Long id) {
        Optional<CourseEntity> optionalEntity = courseRepository.findById(id);
        if (optionalEntity.isPresent()) {
            CourseEntity courseEntity = optionalEntity.get();
            CourseModel courseModel = modelMapper.map(courseEntity, CourseModel.class);
            return Optional.of(courseModel);
        }
        return Optional.empty();

    }

    @Override
    public void deleteRecordById(Long id) {
        courseRepository.deleteById(id);

    }

    @Override
    public CourseModel updatedRecordById(Long id, CourseModel record) {
        Optional<CourseEntity> optionalCourseEntity = courseRepository.findById(id);
        if (optionalCourseEntity.isPresent()) {
            CourseEntity courseEntity = optionalCourseEntity.get();
            //BeanUtils.copyProperties(record, courseEntity);
            modelMapper.map(record, courseEntity);
            courseEntity.setId(id);
            courseRepository.save(courseEntity);
        }
        return record;
    }

    public void saveExcelFile(MultipartFile file) {
        if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            try {
                List<CourseEntity> courseEntityList = courseReader.getCourseObjects(file.getInputStream());
                courseRepository.saveAll(courseEntityList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
