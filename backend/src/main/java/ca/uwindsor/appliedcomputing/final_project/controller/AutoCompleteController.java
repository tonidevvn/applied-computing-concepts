package ca.uwindsor.appliedcomputing.final_project.controller;

import ca.uwindsor.appliedcomputing.final_project.dto.WordFrequency;
import ca.uwindsor.appliedcomputing.final_project.service.WordCompletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/auto-complete")
public class AutoCompleteController {
    @Autowired
    private WordCompletionService wordCompletionService;

    @GetMapping
    public List<String> getWordCompleteList(@RequestParam("prefix") String text) {
        return wordCompletionService.getWordSuggestions(text).stream().map(WordFrequency::getWord).toList();
    }
}
