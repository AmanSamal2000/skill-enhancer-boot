package com.learning.service.mysql;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.learning.constants.NumberConstants;
import com.learning.entity.mysql.StudentEntity;
import com.learning.utility.excel.BatchReader;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.learning.entity.mysql.BatchEntity;
import com.learning.model.BatchModel;
import com.learning.repository.mysql.BatchRepository;
import com.learning.service.common.CommonService;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class BatchMysqlService implements CommonService<BatchModel, Long> {

    private final BatchRepository batchRepository;

    private final ModelMapper modelMapper;
    private final BatchReader batchReader;

    @Override
    public List<BatchModel> getAllRecords() {
        List<BatchEntity> batchEntityList = batchRepository.findAll();
        if (!CollectionUtils.isEmpty(batchEntityList)) {
            List<BatchModel> batchModelList = batchEntityList.stream().map(batchEntity -> {

                BatchModel batchModel = new BatchModel();
                //BeanUtils.copyProperties(batchEntity, batchModel);
                modelMapper.map(batchEntity, batchModel);
                return batchModel;
            }).collect(Collectors.toList());
            return batchModelList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<BatchModel> getLimitedRecords(int count) {
        List<BatchEntity> batchEntityList = batchRepository.findAll();
        if (Objects.nonNull(batchEntityList) && batchEntityList.size() > NumberConstants.ZERO) {
            List<BatchModel> batchModelList = batchEntityList.stream().limit(count).map(batchEntity -> {
                BatchModel batchModel = modelMapper.map(batchEntity, BatchModel.class);
                return batchModel;
            }).collect(Collectors.toList());
            return batchModelList;
        } else {
            return Collections.emptyList();
        }
    }


    @Override
    public List<BatchModel> getSortedRecords(String sortBy) {
        List<BatchEntity> batchEntityList = batchRepository.findAll();
        if (Objects.nonNull(batchEntityList) && batchEntityList.size() > NumberConstants.ZERO) {
            Comparator<BatchEntity> comparator = findSuitableComparator(sortBy);
            List<BatchModel> batchModelList = batchEntityList.stream().sorted(comparator).map(batchEntity -> {
                BatchModel batchModel = modelMapper.map(batchEntity, BatchModel.class);
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
            BatchEntity batchEntity = new BatchEntity();
            //BeanUtils.copyProperties(record, batchEntity);
            modelMapper.map(record, batchEntity);
            batchRepository.save(batchEntity);
        }
        return record;
    }

    @Override
    public List<BatchModel> saveAll(List<BatchModel> batchModelList) {
        if (Objects.nonNull(batchModelList) && batchModelList.size() > NumberConstants.ZERO) {
            List<BatchEntity> batchEntityList = batchModelList.stream().map(batchModel -> {
                BatchEntity entity = modelMapper.map(batchModel, BatchEntity.class);
                return entity;
            }).collect(Collectors.toList());
            batchRepository.saveAll(batchEntityList);
        }
        return batchModelList;
    }

    @Override
    public Optional<BatchModel> getRecordById(Long id) {
        Optional<BatchEntity> optionalEntity = batchRepository.findById(id);
        if (optionalEntity.isPresent()) {
            BatchEntity batchEntity = optionalEntity.get();
            BatchModel batchModel = modelMapper.map(batchEntity, BatchModel.class);
            return Optional.of(batchModel);
        }
        return Optional.empty();
    }

    @Override
    public void deleteRecordById(Long id) {
        batchRepository.deleteById(id);

    }

    @Override
    public BatchModel updatedRecordById(Long id, BatchModel record) {
        Optional<BatchEntity> optionalBatchEntity = batchRepository.findById(id);
        if (optionalBatchEntity.isPresent()) {
            BatchEntity batchEntity = optionalBatchEntity.get();
            modelMapper.map(record, batchEntity);
            batchEntity.setId(id);
            batchRepository.save(batchEntity);
        }
        return record;
    }

    public void saveExcelFile(MultipartFile file) {
        if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            try {
                List<BatchEntity> batchEntityList = batchReader.getBatchObjects(file.getInputStream());
                batchRepository.saveAll(batchEntityList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private Comparator<BatchEntity> findSuitableComparator(String sortBy) {
        Comparator<BatchEntity> comparator;
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
