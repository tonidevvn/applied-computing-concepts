package ca.uwindsor.appliedcomputing.final_project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PageRankingServiceTest {

    private PageRankingService pageRankingService;

    @BeforeEach
    public void setUp() {
        pageRankingService = new PageRankingService();
    }

    @Test
    public void testCalculatePageRanks() {
        String[] searchKeywords = {"pizza"};

        List<String> topRankedProducts = pageRankingService.calculatePageRanks(searchKeywords);
        System.out.println(topRankedProducts);
        System.out.println(topRankedProducts.size());

        assertNotNull(topRankedProducts);
        assertFalse(topRankedProducts.isEmpty());
        assertEquals(10, topRankedProducts.size());
    }

}
