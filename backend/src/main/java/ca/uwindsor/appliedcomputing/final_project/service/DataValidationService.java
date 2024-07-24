package ca.uwindsor.appliedcomputing.final_project.service;

import ca.uwindsor.appliedcomputing.final_project.dto.ValidationData;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class DataValidationService {

    // Regular expressions for validation
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s,'\\-_.()&%/]+$");
    private static final Pattern PRICE_PATTERN = Pattern.compile("^\\$?\\d+(\\.\\d{1,2})?\\s*$");
    private static final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$");
    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("^[\\p{L}\\p{N}\\p{P}\\p{Zs}\\r\\n\\u00A9\\u00AE]+$");
    private static final Pattern CATEGORY_PATTERN = Pattern.compile("^[a-zA-Z\\s&]+$");
    private static final Pattern STORE_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");

    // File path for the CSV dataset containing product data
    private final String csvFilePath = Objects.requireNonNull(getClass().getClassLoader()
            .getResource("data/merged_dataset.csv")).getFile();

    public List<ValidationData> validateData() {
        List<ValidationData> validationResults = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] headers = reader.readNext(); // Assuming first row is header

            String[] record;
            while ((record = reader.readNext()) != null) {
                ValidationData data = new ValidationData();
                data.setProductName(record[0]); // name
                data.setStore(record[1]); // store
                data.setCategory(record[2]); // category
                data.setPrice(record[3]); // price
                data.setImageUrl(record[4]); // image
                data.setProductUrl(record[5]); // link
                data.setProductDescription(record.length > 6 ? record[6] : ""); // description

                validate(data);
                validationResults.add(data);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return validationResults;
    }

    public ValidationData validate(ValidationData data) {
        boolean isNameValid = NAME_PATTERN.matcher(data.getProductName()).matches();
        boolean isStoreValid = STORE_PATTERN.matcher(data.getStore()).matches();
        boolean isCategoryValid = CATEGORY_PATTERN.matcher(data.getCategory()).matches();
        boolean isPriceValid = PRICE_PATTERN.matcher(data.getPrice()).matches();
        boolean isImageUrlValid = URL_PATTERN.matcher(data.getImageUrl()).matches();
        boolean isProductUrlValid = URL_PATTERN.matcher(data.getProductUrl()).matches();
        boolean isDescriptionValid = DESCRIPTION_PATTERN.matcher(data.getProductDescription()).matches();

        data.setValidName(isNameValid);
        data.setValidStore(isStoreValid);
        data.setValidCategory(isCategoryValid);
        data.setValidPrice(isPriceValid);
        data.setValidImageUrl(isImageUrlValid);
        data.setValidProductUrl(isProductUrlValid);
        data.setValidDescription(isDescriptionValid);

        return data;
    }
}
