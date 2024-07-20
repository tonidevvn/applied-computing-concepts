package ca.uwindsor.appliedcomputing.final_project.controller;

import ca.uwindsor.appliedcomputing.final_project.service.InvertedIndexingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/inverted-index")
public class InvertedIndexingController {
    @Autowired
    private InvertedIndexingService invertedIndexingService;

    @GetMapping
    public Set<Map<String, String>> search(@RequestParam("query") String query) {
        return invertedIndexingService.search(query);
    }
}
