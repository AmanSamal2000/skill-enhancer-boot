package com.learning.service.mongo;


import com.learning.constants.NumberConstants;
import com.learning.model.TimeSlotModel;
import com.learning.entity.mongo.TimeSlotCollection;
import com.learning.repository.mongo.TimeSlotMongoRepository;
import com.learning.service.common.CommonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeSlotMongoService implements CommonService<TimeSlotModel, Long> {

    private final TimeSlotMongoRepository timeSlotMongoRepository;

    private final ModelMapper modelMapper;


    @Override
    public List<TimeSlotModel> getAllRecords() {
        List<TimeSlotCollection> timeSlotCollectionList = timeSlotMongoRepository.findAll();
        if (!CollectionUtils.isEmpty(timeSlotCollectionList)) {
            List<TimeSlotModel> timeSlotModelList = timeSlotCollectionList.stream().map(timeSlotCollection -> {
                TimeSlotModel timeSlotModel = new TimeSlotModel();
                modelMapper.map(timeSlotCollection, timeSlotModel);
                return timeSlotModel;
            }).collect(Collectors.toList());
            return timeSlotModelList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<TimeSlotModel> getLimitedRecords(int count) {
        List<TimeSlotCollection> timeSlotCollectionList = timeSlotMongoRepository.findAll();
        if (Objects.nonNull(timeSlotCollectionList) && timeSlotCollectionList.size() > NumberConstants.ZERO) {
            List<TimeSlotModel> timeSlotModelList = timeSlotCollectionList.stream().limit(count).map(timeSlotCollection -> {
                TimeSlotModel timeSlotModel = modelMapper.map(timeSlotCollection, TimeSlotModel.class);
                return timeSlotModel;
            }).collect(Collectors.toList());
            return timeSlotModelList;
        } else {
            return Collections.emptyList();
        }
    }


    @Override
    public List<TimeSlotModel> getSortedRecords(String sortBy) {
        List<TimeSlotCollection> timeSlotCollectionList = timeSlotMongoRepository.findAll();
        if (Objects.nonNull(timeSlotCollectionList) && timeSlotCollectionList.size() > NumberConstants.ZERO) {
            Comparator<TimeSlotCollection> comparator = findSuitableComparator(sortBy);
            List<TimeSlotModel> timeSlotModelList = timeSlotCollectionList.stream().sorted(comparator)
                    .map(timeSlotCollection -> {
                        TimeSlotModel timeSlotModel = modelMapper.map(timeSlotCollection, TimeSlotModel.class);
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
            TimeSlotCollection timeSlotCollection = new TimeSlotCollection();
            modelMapper.map(record, timeSlotCollection);
            timeSlotMongoRepository.save(timeSlotCollection);
        }
        return record;
    }

    @Override
    public List<TimeSlotModel> saveAll(List<TimeSlotModel> timeSlotModelList) {
        if (Objects.nonNull(timeSlotModelList) && timeSlotModelList.size() > NumberConstants.ZERO) {
            List<TimeSlotCollection> timeSlotCollectionList = timeSlotModelList.stream().map(timeSlotModel -> {
                TimeSlotCollection collection = modelMapper.map(timeSlotModel, TimeSlotCollection.class);
                return collection;
            }).collect(Collectors.toList());
            timeSlotMongoRepository.saveAll(timeSlotCollectionList);
        }
        return timeSlotModelList;
    }

    @Override
    public Optional<TimeSlotModel> getRecordById(Long id) {
        Optional<TimeSlotCollection> optionalCollection = timeSlotMongoRepository.findById(id);
        if (optionalCollection.isPresent()) {
            TimeSlotCollection timeSlotCollection = optionalCollection.get();
            TimeSlotModel timeSlotModel = modelMapper.map(timeSlotCollection, TimeSlotModel.class);
            return Optional.of(timeSlotModel);
        }
        return Optional.empty();

    }

    @Override
    public void deleteRecordById(Long id) {
        timeSlotMongoRepository.deleteById(id);

    }

    @Override
    public TimeSlotModel updatedRecordById(Long id, TimeSlotModel record) {
        Optional<TimeSlotCollection> optionalTimeSlotCollection = timeSlotMongoRepository.findById(id);
        if (optionalTimeSlotCollection.isPresent()) {
            TimeSlotCollection timeSlotCollection = optionalTimeSlotCollection.get();
            modelMapper.map(record, timeSlotCollection);
            timeSlotCollection.setId(id);
            timeSlotMongoRepository.save(timeSlotCollection);
        }
        return record;
    }

    private Comparator<TimeSlotCollection> findSuitableComparator(String sortBy) {
        Comparator<TimeSlotCollection> comparator;
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
