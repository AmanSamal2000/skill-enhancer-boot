package com.learning.mongoservice.Impl;

import java.util.*;
import java.util.stream.Collectors;

import com.learning.constants.NumberConstants;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.learning.collection.TimeSlotEntity;
import com.learning.model.TimeSlotModel;
import com.learning.mongorepository.TimeSlotRepository;
import com.learning.mongoservice.CommonService;

@Service
@RequiredArgsConstructor
public class TimeSlotService implements CommonService<TimeSlotModel, Long> {

    private final TimeSlotRepository timeSlotRepository;

    private final ModelMapper modelMapper;


    @Override
    public List<TimeSlotModel> getAllRecords() {
        List<TimeSlotEntity> timeSlotEntityList = timeSlotRepository.findAll();
        if (!CollectionUtils.isEmpty(timeSlotEntityList)) {
            List<TimeSlotModel> timeSlotModelList = timeSlotEntityList.stream().map(timeSlotEntity -> {

                TimeSlotModel timeSlotModel = new TimeSlotModel();
                //	BeanUtils.copyProperties(timeSlotEntity, timeSlotModel);
                modelMapper.map(timeSlotEntity, timeSlotModel);
                return timeSlotModel;
            }).collect(Collectors.toList());
            return timeSlotModelList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<TimeSlotModel> getLimitedRecords(int count) {
        List<TimeSlotEntity> timeSlotEntityList = timeSlotRepository.findAll();
        if (Objects.nonNull(timeSlotEntityList) && timeSlotEntityList.size() > NumberConstants.ZERO) {
            List<TimeSlotModel> timeSlotModelList = timeSlotEntityList.stream().limit(count).map(timeSlotEntity -> {
                TimeSlotModel timeSlotModel = modelMapper.map(timeSlotEntity, TimeSlotModel.class);
                return timeSlotModel;
            }).collect(Collectors.toList());
            return timeSlotModelList;
        } else {
            return Collections.emptyList();
        }
    }


    @Override
    public List<TimeSlotModel> getSortedRecords(String sortBy) {
        List<TimeSlotEntity> timeSlotEntityList = timeSlotRepository.findAll();
        if (Objects.nonNull(timeSlotEntityList) && timeSlotEntityList.size() > NumberConstants.ZERO) {
            Comparator<TimeSlotEntity> comparator = findSuitableComparator(sortBy);
            List<TimeSlotModel> timeSlotModelList = timeSlotEntityList.stream().sorted(comparator)
                    .map(timeSlotEntity -> {
                        TimeSlotModel timeSlotModel = modelMapper.map(timeSlotEntity, TimeSlotModel.class);
                        return timeSlotModel;
                    }).collect(Collectors.toList());
            return timeSlotModelList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public TimeSlotModel saveRecord(TimeSlotModel record) {
        if (Objects.nonNull(record)) {
            TimeSlotEntity timeSlotEntity = new TimeSlotEntity();
            //BeanUtils.copyProperties(record, timeSlotEntity);
            modelMapper.map(record, timeSlotEntity);
            TimeSlotEntity savedObject = timeSlotRepository.save(timeSlotEntity);
        }
        return record;
    }

    @Override
    public List<TimeSlotModel> saveAll(List<TimeSlotModel> timeSlotModelList) {
        if (Objects.nonNull(timeSlotModelList) && timeSlotModelList.size() > NumberConstants.ZERO) {
            List<TimeSlotEntity> timeSlotEntityList = timeSlotModelList.stream().map(timeSlotModel -> {
                TimeSlotEntity entity = modelMapper.map(timeSlotModel, TimeSlotEntity.class);
                return entity;
            }).collect(Collectors.toList());
            timeSlotEntityList = timeSlotRepository.saveAll(timeSlotEntityList);
        }
        return timeSlotModelList;
    }

    @Override
    public Optional<TimeSlotModel> getRecordById(Long id) {
        Optional<TimeSlotEntity> optionalEntity = timeSlotRepository.findById(id);
        if (optionalEntity.isPresent()) {
            TimeSlotEntity timeSlotEntity = optionalEntity.get();
            TimeSlotModel timeSlotModel = modelMapper.map(timeSlotEntity, TimeSlotModel.class);
            return Optional.of(timeSlotModel);
        }
        return Optional.empty();

    }

    @Override
    public void deleteRecordById(Long id) {
        timeSlotRepository.deleteById(id);

    }

    @Override
    public TimeSlotModel updatedRecordById(Long id, TimeSlotModel record) {
        Optional<TimeSlotEntity> optionalTimeSlotEntity = timeSlotRepository.findById(id);
        if (optionalTimeSlotEntity.isPresent()) {
            TimeSlotEntity timeSlotEntity = optionalTimeSlotEntity.get();
            //BeanUtils.copyProperties(record, timeSlotEntity);
            modelMapper.map(record, timeSlotEntity);
            timeSlotEntity.setId(id);
            timeSlotRepository.save(timeSlotEntity);
        }
        return record;
    }

    private Comparator<TimeSlotEntity> findSuitableComparator(String sortBy) {
        Comparator<TimeSlotEntity> comparator;
        switch (sortBy) {
            case "startTime": {
                comparator = (timeSlotOne, timeSlotTwo) -> timeSlotOne.getStartTime().compareTo(timeSlotTwo.getStartTime());
                break;
            }
            case "endTime": {
                comparator = (timeSlotOne, timeSlotTwo) -> timeSlotOne.getEndTime().compareTo(timeSlotTwo.getEndTime());
                break;
            }
            default: {
                comparator = (timeSlotOne, timeSlotTwo) -> timeSlotOne.getId().compareTo(timeSlotTwo.getId());
            }

        }

        return comparator;

    }


}