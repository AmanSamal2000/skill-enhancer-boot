package com.learning.mongoservice.Impl;

import java.util.*;
import java.util.stream.Collectors;

import com.learning.constants.NumberConstants;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.learning.collection.TrainerEntity;
import com.learning.model.TrainerModel;
import com.learning.mongorepository.TrainerRepository;
import com.learning.mongoservice.CommonService;

@Service
@RequiredArgsConstructor
public class TrainerService implements CommonService<TrainerModel, Long> {

    private final TrainerRepository trainerRepository;

    private final ModelMapper modelMapper;


    @Override
    public List<TrainerModel> getAllRecords() {
        List<TrainerEntity> trainerEntityList = trainerRepository.findAll();
        if (!CollectionUtils.isEmpty(trainerEntityList)) {
            List<TrainerModel> trainerModelList = trainerEntityList.stream().map(trainerEntity -> {

                TrainerModel trainerModel = new TrainerModel();
                //BeanUtils.copyProperties(trainerEntity, trainerModel);
                modelMapper.map(trainerEntity, trainerModel);
                return trainerModel;
            }).collect(Collectors.toList());
            return trainerModelList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<TrainerModel> getLimitedRecords(int count) {
        List<TrainerEntity> trainerEntityList = trainerRepository.findAll();
        if (Objects.nonNull(trainerEntityList) && trainerEntityList.size() > NumberConstants.ZERO) {
            List<TrainerModel> trainerModelList = trainerEntityList.stream().limit(count).map(trainerEntity -> {
                TrainerModel trainerModel = modelMapper.map(trainerEntity, TrainerModel.class);
                return trainerModel;
            }).collect(Collectors.toList());
            return trainerModelList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<TrainerModel> getSortedRecords(String sortBy) {
        List<TrainerEntity> trainerEntityList = trainerRepository.findAll();
        if (Objects.nonNull(trainerEntityList) && trainerEntityList.size() > NumberConstants.ZERO) {
            Comparator<TrainerEntity> comparator = findSuitableComparator(sortBy);
            List<TrainerModel> trainerModelList = trainerEntityList.stream().sorted(comparator).map(trainerEntity -> {
                TrainerModel trainerModel = modelMapper.map(trainerEntity, TrainerModel.class);
                return trainerModel;
            }).collect(Collectors.toList());
            return trainerModelList;
        } else {
            return Collections.emptyList();
        }

    }

    @Override
    public TrainerModel saveRecord(TrainerModel record) {
        if (Objects.nonNull(record)) {
            TrainerEntity trainerEntity = new TrainerEntity();
            //BeanUtils.copyProperties(record, trainerEntity);
            modelMapper.map(record, trainerEntity);
            TrainerEntity savedObject = trainerRepository.save(trainerEntity);
        }
        return record;
    }

    @Override
    public List<TrainerModel> saveAll(List<TrainerModel> trainerModelList) {
        if (Objects.nonNull(trainerModelList) && trainerModelList.size() > NumberConstants.ZERO) {
            List<TrainerEntity> trainerEntityList = trainerModelList.stream().map(trainerModel -> {
                TrainerEntity entity = modelMapper.map(trainerModel, TrainerEntity.class);
                return entity;
            }).collect(Collectors.toList());
            trainerEntityList = trainerRepository.saveAll(trainerEntityList);
        }
        return trainerModelList;

    }

    @Override
    public Optional<TrainerModel> getRecordById(Long id) {
        Optional<TrainerEntity> optionalEntity = trainerRepository.findById(id);
        if (optionalEntity.isPresent()) {
            TrainerEntity trainerEntity = optionalEntity.get();
            TrainerModel trainerModel = modelMapper.map(trainerEntity, TrainerModel.class);
            return Optional.of(trainerModel);

        }
        return Optional.empty();
    }

    @Override
    public void deleteRecordById(Long id) {
        trainerRepository.deleteById(id);

    }

    @Override
    public TrainerModel updatedRecordById(Long id, TrainerModel record) {
        Optional<TrainerEntity> optionalTrainerEntity = trainerRepository.findById(id);
        if (optionalTrainerEntity.isPresent()) {
            TrainerEntity trainerEntity = optionalTrainerEntity.get();
            //BeanUtils.copyProperties(record, trainerEntity);
            modelMapper.map(record, trainerEntity);
            trainerEntity.setId(id);
            trainerRepository.save(trainerEntity);
        }
        return record;
    }

    private Comparator<TrainerEntity> findSuitableComparator(String sortBy) {
        Comparator<TrainerEntity> comparator;
        switch (sortBy) {
            case "name": {
                comparator = (trainerOne, trainerTwo) -> trainerOne.getName().compareTo(trainerTwo.getName());
                break;
            }
            case "specialization": {
                comparator = (trainerOne, trainerTwo) -> trainerOne.getSpecialization()
                        .compareTo(trainerTwo.getSpecialization());
                break;
            }
            default: {
                comparator = (trainerOne, trainerTwo) -> trainerOne.getId().compareTo(trainerTwo.getId());
            }
        }
        return comparator;

    }

}
