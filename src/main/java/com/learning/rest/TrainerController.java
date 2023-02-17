package com.learning.rest;

import java.util.*;

import com.learning.model.TrainerModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import com.learning.service.mysql.TrainerMysqlService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerMysqlService trainerService;


    @GetMapping
    public List<TrainerModel> getAllRecords() {
        return trainerService.getAllRecords();
    }

    @GetMapping("/limited-records")
    public List<TrainerModel> getLimitedRecords(@RequestParam(value = "count" ,required = false , defaultValue = "0") int count){
        return trainerService.getLimitedRecords(count);
    }

    @GetMapping("/sorted-records")
    public List<TrainerModel> getSortedRecords(@RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy){
        return trainerService.getSortedRecords(sortBy);
    }

    @GetMapping("/recordBy-id")
    public Optional<TrainerModel> getRecordById(Long id){
        return trainerService.getRecordById(id);
    }

    @PostMapping("/save-all")
    public List<TrainerModel> saveAll(List<TrainerModel> trainerModelList){
        return trainerService.saveAll(trainerModelList);
    }

    @PostMapping
    public TrainerModel save(@RequestBody TrainerModel trainerModel) {
        return trainerService.saveRecord(trainerModel);
    }


    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        trainerService.deleteRecordById(id);
    }

    @PutMapping("/{id}")
    public TrainerModel updateById(@PathVariable Long id, @RequestBody TrainerModel trainerModel) {
        return trainerService.updatedRecordById(id, trainerModel);
    }

    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file){
        trainerService.saveExcelFile(file);
        return "file uploaded successfully";
    }

//Array postmapping method

 /*   @GetMapping("get-records")
    public List<TrainerModel> getAllRecords(@RequestParam(value = "count", required = false, defaultValue = "0") int count, @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
        if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
            return trainerService.getAllRecords();
        } else if (count > 0) {
            return trainerService.getLimitedRecords(count);
        } else {
            return trainerService.getSortedRecords(sortBy);
        }
    }

    @PostMapping
    public List<TrainerModel> save(@RequestBody List<TrainerModel> trainerModelList) {
        try {
            if (trainerModelList.size() == 1) {
                return Arrays.asList(trainerService.saveRecord(trainerModelList.get(0)));
            } else {
                return trainerService.saveAll(trainerModelList);
            }
        } catch (Exception exception) {
            System.out.println("Exception Occurs in StudentController || saveAll");
            System.err.print(exception);
            return Collections.emptyList();
        }
    }*/


}
