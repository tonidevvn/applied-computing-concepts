package ca.uwindsor.appliedcomputing.final_project.controller;

import ca.uwindsor.appliedcomputing.final_project.dto.DistanceEntry;
import ca.uwindsor.appliedcomputing.final_project.service.SpellCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/spell-checking")
public class SpellCheckingController {
    @Autowired
    private SpellCheckingService spellCheckingService;

    @GetMapping
    public List<String> getWordCompleteList(@RequestParam("word") String word) {
        List<DistanceEntry> lDE = spellCheckingService.spellChecking(word);
        // FIXME: make limit become a service parameter, default to 10
        return lDE.stream().map(DistanceEntry::getWord).limit(3).toList();
    }
}
