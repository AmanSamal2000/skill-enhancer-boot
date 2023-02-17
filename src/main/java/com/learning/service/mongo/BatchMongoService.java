package com.learning.service.mongo;

import com.learning.constants.NumberConstants;
import com.learning.model.BatchModel;
import com.learning.entity.mongo.BatchCollection;
import com.learning.repository.mongo.BatchMongoRepository;
import com.learning.service.common.CommonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BatchMongoService implements CommonService<BatchModel, Long> {

    private final BatchMongoRepository batchMongoRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<BatchModel> getAllRecords() {
        List<BatchCollection> batchCollectionList = batchMongoRepository.findAll();
        if (!CollectionUtils.isEmpty(batchCollectionList)) {
            List<BatchModel> batchModelList = batchCollectionList.stream().map(batchCollection -> {
                BatchModel batchModel = new BatchModel();
                modelMapper.map(batchCollection, batchModel);
                return batchModel;
            }).collect(Collectors.toList());
            return batchModelList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<BatchModel> getLimitedRecords(int count) {
        List<BatchCollection> batchCollectionList = batchMongoRepository.findAll();
        if (Objects.nonNull(batchCollectionList) && batchCollectionList.size() > NumberConstants.ZERO) {
            List<BatchModel> batchModelList = batchCollectionList.stream().limit(count).map(batchCollection -> {
                BatchModel batchModel = modelMapper.map(batchCollection, BatchModel.class);
                return batchModel;
            }).collect(Collectors.toList());
            return batchModelList;
        } else {
            return Collections.emptyList();
        }
    }


    @Override
    public List<BatchModel> getSortedRecords(String sortBy) {
        List<BatchCollection> batchCollectionList = batchMongoRepository.findAll();
        if (Objects.nonNull(batchCollectionList) && batchCollectionList.size() > NumberConstants.ZERO) {
            Comparator<BatchCollection> comparator = findSuitableComparator(sortBy);
            List<BatchModel> batchModelList = batchCollectionList.stream().sorted(comparator).map(batchCollection -> {
                BatchModel batchModel = modelMapper.map(batchCollection, BatchModel.class);
                return batchModel;
            }).collect(Collectors.toList());
            return batchModelList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public BatchModel saveRecord(BatchModel record) {
        if (Objects.nonNull(record)) {
            BatchCollection batchCollection = new BatchCollection();
            modelMapper.map(record, batchCollection);
            batchMongoRepository.save(batchCollection);
        }
        return record;
    }

    @Override
    public List<BatchModel> saveAll(List<BatchModel> batchModelList) {
        if (Objects.nonNull(batchModelList) && batchModelList.size() > NumberConstants.ZERO) {
            List<BatchCollection> batchCollectionList = batchModelList.stream().map(batchModel -> {
                BatchCollection collection = modelMapper.map(batchModel, BatchCollection.class);
                return collection;
            }).collect(Collectors.toList());
            batchMongoRepository.saveAll(batchCollectionList);
        }
        return batchModelList;
    }

    @Override
    public Optional<BatchModel> getRecordById(Long id) {
        Optional<BatchCollection> optionalEntity = batchMongoRepository.findById(id);
        if (optionalEntity.isPresent()) {
            BatchCollection batchCollection = optionalEntity.get();
            BatchModel batchModel = modelMapper.map(batchCollection, BatchModel.class);
            return Optional.of(batchModel);
        }
        return Optional.empty();
    }

    @Override
    public void deleteRecordById(Long id) {
        batchMongoRepository.deleteById(id);

    }

    @Override
    public BatchModel updatedRecordById(Long id, BatchModel record) {
        Optional<BatchCollection> optionalBatchCollection = batchMongoRepository.findById(id);
        if (optionalBatchCollection.isPresent()) {
            BatchCollection batchCollection = optionalBatchCollection.get();
            modelMapper.map(record, batchCollection);
            batchCollection.setId(id);
            batchMongoRepository.save(batchCollection);
        }
        return record;
    }

    private Comparator<BatchCollection> findSuitableComparator(String sortBy) {
        Comparator<BatchCollection> comparator;
        switch (sortBy) {
            case "studentCount": {
                comparator = (batchOne, batchTwo) -> batchOne.getStudentCount().compareTo(batchTwo.getStudentCount());
                break;
            }
            case "timeSlotId": {
                comparator = (batchOne, batchTwo) -> batchOne.getTimeSlotId().compareTo(batchTwo.getTimeSlotId());
                break;
            }
            default: {
                comparator = (batchOne, batchTwo) -> batchOne.getId().compareTo(batchTwo.getId());
            }
        }
        return comparator;
    }

}