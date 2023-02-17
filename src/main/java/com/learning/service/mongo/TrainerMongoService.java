package com.learning.service.mongo;


import com.learning.constants.NumberConstants;
import com.learning.model.TrainerModel;
import com.learning.entity.mongo.TrainerCollection;
import com.learning.repository.mongo.TrainerMongoRepository;
import com.learning.service.common.CommonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerMongoService implements CommonService<TrainerModel, Long> {

    private final TrainerMongoRepository trainerMongoRepository;

    private final ModelMapper modelMapper;


    @Override
    public List<TrainerModel> getAllRecords() {
        List<TrainerCollection> trainerCollectionList = trainerMongoRepository.findAll();
        if (!CollectionUtils.isEmpty(trainerCollectionList)) {
            List<TrainerModel> trainerModelList = trainerCollectionList.stream().map(trainerCollection -> {
                TrainerModel trainerModel = new TrainerModel();
                modelMapper.map(trainerCollection, trainerModel);
                return trainerModel;
            }).collect(Collectors.toList());
            return trainerModelList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<TrainerModel> getLimitedRecords(int count) {
        List<TrainerCollection> trainerCollectionList = trainerMongoRepository.findAll();
        if (Objects.nonNull(trainerCollectionList) && trainerCollectionList.size() > NumberConstants.ZERO) {
            List<TrainerModel> trainerModelList = trainerCollectionList.stream().limit(count).map(trainerCollection -> {
                TrainerModel trainerModel = modelMapper.map(trainerCollection, TrainerModel.class);
                return trainerModel;
            }).collect(Collectors.toList());
            return trainerModelList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<TrainerModel> getSortedRecords(String sortBy) {
        List<TrainerCollection> trainerCollectionList = trainerMongoRepository.findAll();
        if (Objects.nonNull(trainerCollectionList) && trainerCollectionList.size() > NumberConstants.ZERO) {
            Comparator<TrainerCollection> comparator = findSuitableComparator(sortBy);
            List<TrainerModel> trainerModelList = trainerCollectionList.stream().sorted(comparator).map(trainerCollection -> {
                TrainerModel trainerModel = modelMapper.map(trainerCollection, TrainerModel.class);
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
            TrainerCollection trainerCollection = new TrainerCollection();
            modelMapper.map(record, trainerCollection);
            trainerMongoRepository.save(trainerCollection);
        }
        return record;
    }

    @Override
    public List<TrainerModel> saveAll(List<TrainerModel> trainerModelList) {
        if (Objects.nonNull(trainerModelList) && trainerModelList.size() > NumberConstants.ZERO) {
            List<TrainerCollection> trainerCollectionList = trainerModelList.stream().map(trainerModel -> {
                TrainerCollection collection = modelMapper.map(trainerModel, TrainerCollection.class);
                return collection;
            }).collect(Collectors.toList());
            trainerMongoRepository.saveAll(trainerCollectionList);
        }
        return trainerModelList;

    }

    @Override
    public Optional<TrainerModel> getRecordById(Long id) {
        Optional<TrainerCollection> optionalCollection = trainerMongoRepository.findById(id);
        if (optionalCollection.isPresent()) {
            TrainerCollection trainerCollection = optionalCollection.get();
            TrainerModel trainerModel = modelMapper.map(trainerCollection, TrainerModel.class);
            return Optional.of(trainerModel);

        }
        return Optional.empty();
    }

    @Override
    public void deleteRecordById(Long id) {
        trainerMongoRepository.deleteById(id);

    }

    @Override
    public TrainerModel updatedRecordById(Long id, TrainerModel record) {
        Optional<TrainerCollection> optionalTrainerCollection = trainerMongoRepository.findById(id);
        if (optionalTrainerCollection.isPresent()) {
            TrainerCollection trainerCollection = optionalTrainerCollection.get();
            modelMapper.map(record, trainerCollection);
            trainerCollection.setId(id);
            trainerMongoRepository.save(trainerCollection);
        }
        return record;
    }


    private Comparator<TrainerCollection> findSuitableComparator(String sortBy) {
        Comparator<TrainerCollection> comparator;
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
