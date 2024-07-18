package ca.uwindsor.appliedcomputing.final_project.controller;

import ca.uwindsor.appliedcomputing.final_project.dto.ValidationData;
import ca.uwindsor.appliedcomputing.final_project.service.DataValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/data-validation")
public class DataValidationController {

    @Autowired
    private DataValidationService dataValidationService;

    @GetMapping
    public ResponseEntity<List<ValidationData>> validateCSV() {
        List<ValidationData> validationResult = dataValidationService.validateData();
        return ResponseEntity.ok(validationResult);
    }
}

