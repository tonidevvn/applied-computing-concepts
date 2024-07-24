package ca.uwindsor.appliedcomputing.final_project.service;

import ca.uwindsor.appliedcomputing.final_project.data_structure.CuckooHashTable;
import ca.uwindsor.appliedcomputing.final_project.data_structure.StringHashFamily;
import ca.uwindsor.appliedcomputing.final_project.dto.DistanceEntry;
import ca.uwindsor.appliedcomputing.final_project.util.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


@Service
public class SpellCheckingService {
    enum SortOrder {
        ASCENDING,
        DESCENDING
    }
    // A convenient function that accepts default parameters
    private void mergeSort(List<DistanceEntry> arr) {
        mergeSort(arr, 0, arr.size() - 1, SortOrder.ASCENDING);
    }

    // Main merge sort function
    private void mergeSort(List<DistanceEntry> arr, int left, int right, SortOrder order) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            // divide and conquer
            mergeSort(arr, left, mid, order);
            mergeSort(arr, mid + 1, right, order);
            merge(arr, left, mid, right, order);
        }
    }

    // Merge the result
    private void merge(List<DistanceEntry> arr, int left, int mid, int right, SortOrder order) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        List<DistanceEntry> leftArr = new ArrayList<>(arr.subList(left, left + n1));
        List<DistanceEntry> rightArr = new ArrayList<>(arr.subList(mid + 1, mid + 1 + n2));

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if ((order == SortOrder.ASCENDING && leftArr.get(i).getDistance() <= rightArr.get(j).getDistance()) ||
                    (order == SortOrder.DESCENDING && leftArr.get(i).getDistance() >= rightArr.get(j).getDistance())) {
                arr.set(k, leftArr.get(i));
                i++;
            } else {
                arr.set(k, rightArr.get(j));
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr.set(k, leftArr.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            arr.set(k, rightArr.get(j));
            j++;
            k++;
        }
    }

    public int editDistance(String word1, String word2) {
        int iLen1 = word1.length();
        int iLen2 = word2.length();

        // len1+1, len2+1, because finally return dp[len1][len2]
        int[][] dp = new int[iLen1 + 1][iLen2 + 1];

        for (int i = 0; i <= iLen1; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= iLen2; j++) {
            dp[0][j] = j;
        }

        //iterate though, and check last char
        for (int i = 0; i < iLen1; i++) {
            char c1 = word1.charAt(i);
            for (int j = 0; j < iLen2; j++) {
                char c2 = word2.charAt(j);

                //if last two chars equal
                if (c1 == c2) {
                    //update dp value for +1 length
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;

                    int min = Math.min(replace, insert);
                    min = Math.min(delete, min);
                    dp[i + 1][j + 1] = min;
                }
            }
        }

        return dp[iLen1][iLen2];
    }

    private CuckooHashTable<String> uniqueKeywordFromCSV(Path filePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()));
            br.readLine(); // ignore header
            String strLine;
            CuckooHashTable<String> uniqueKeywords = new CuckooHashTable<>(new StringHashFamily( 3 ));
            while ((strLine = br.readLine()) != null) {
                // remove special characters from the line
                strLine = strLine.replaceAll("[^\\w\\s]|\\d","");

                // Collect words and ignore white spaces
                String[] strWords = strLine.split("\\s+");
                for (String strWord : strWords) {
                    if (strWord.isBlank()) {
                        continue;
                    }
                    String lcWord = strWord.toLowerCase();
                    uniqueKeywords.insert(lcWord);
                }
            }
            return uniqueKeywords;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<DistanceEntry> spellChecking(String strCheckingWord) {
        String trimWord = strCheckingWord.trim();
        if (trimWord.isBlank()) {
            return new ArrayList<>();
        }
        // build a vocabulary
        CuckooHashTable<String> cHashTable = uniqueKeywordFromCSV(Resource.getMergedDataSet());
//        List<String> hsVocabulary

        // calculate distances
        List<DistanceEntry> lDistanceWords = new ArrayList<>();
        for (String word : cHashTable.getAllKeys()) {
            int distance = editDistance(trimWord, word);
            DistanceEntry entry = new DistanceEntry(distance, word);
            lDistanceWords.add(entry);
        }

        // merge sort
        mergeSort(lDistanceWords);

        return lDistanceWords;
    }
}
