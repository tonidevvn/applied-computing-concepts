package ca.uwindsor.appliedcomputing.final_project.util;

import ca.uwindsor.appliedcomputing.final_project.dto.ProductData;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.sun.tools.javac.Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Resource {
    /**
     * This method is used to walk through the resources directory and return a list of absolute paths of all regular files.
     *
     * @return A list of absolute paths of all regular files in the resources directory. If an exception occurs, it returns an empty list.
     */
    public static List<Path> walkResources() {
        try {
            Path resourcePath = Paths.get(Main.class.getClassLoader().getResource("data").toURI());
            try (Stream<Path> paths = Files.walk(resourcePath)) {
                return paths.filter(Files::isRegularFile)
                        .map(Path::toAbsolutePath)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * This method reads a CSV file from the given path and extracts all the words from it.
     * It reads the file line by line, splits each line into words, filters out non-alphabetic words and links,
     * converts all words to lower case, and collects them into a list.
     *
     * @param csvFilePath The path of the CSV file to read.
     * @return A list of words extracted from the file. If an exception occurs during the process, it returns an empty list.
     */
    public static List<String> readVocabularyFromCSV(Path csvFilePath) {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath.toFile()))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                // Skip the header line if present
                if (lineNumber++ == 0) {
                    continue;
                }
                String[] columns = line.split(",");
                for (String column : columns) {
                    List<String> wordsInColumn = Arrays.stream(column.strip().split("\\s+"))
                            .filter(word -> word.matches("[a-zA-Z]+"))
                            .map(String::toLowerCase)
                            .toList();
                    words.addAll(wordsInColumn);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }



    public List<ProductData> loadProductsFromCSV(int limit) {
        List<ProductData> responseProducts = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(Paths.get(Main.class.getClassLoader().getResource("data/merged_dataset.csv").toURI()).toFile()))) {
            List<String[]> records = csvReader.readAll();
            records.remove(0); // Remove header row

            int count = 0;
            for (String[] record : records) {
                ProductData responseProduct = new ProductData();
                if (record.length == 5) {
                    responseProduct.setName(record[0]);
                    //responseProduct.setBrand(record[2]);
                    responseProduct.setPrice(Double.parseDouble(record[1]));
                    responseProduct.setImage(record[2]);
                    responseProduct.setUrl(record[3]);
                    responseProduct.setDescription(record[4]);
                    if (responseProduct.getName() != null && count++ < limit)
                        responseProducts.add(responseProduct);
                }
            }
            return responseProducts;
        } catch (IOException | CsvException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
