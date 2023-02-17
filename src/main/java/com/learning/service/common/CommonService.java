package com.learning.service.common;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CommonService<T, ID> {
	
	List<T> getAllRecords();
	
	List<T> getLimitedRecords(int count);
	
	List<T> getSortedRecords(String sortBy);
	
	T saveRecord(T record);
	
	List<T> saveAll(List<T> recordList);

	Optional<T> getRecordById(ID id);
    
    void deleteRecordById(ID id);
    
    T updatedRecordById(ID id,T record);

	//void saveExcelFile(MultipartFile file);
}
