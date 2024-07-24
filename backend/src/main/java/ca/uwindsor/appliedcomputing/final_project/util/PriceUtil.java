package ca.uwindsor.appliedcomputing.final_project.util;

import ca.uwindsor.appliedcomputing.final_project.dto.PriceConditionItem;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceUtil {
    public static void printPriceConditions(ArrayList<PriceConditionItem> items) {
        for (PriceConditionItem item : items) {
            System.out.print(item.op + " " + item.value + ", ");
        }
    }

    public static ArrayList<PriceConditionItem> parsePriceQuery(String query) {
        Pattern pat = Pattern.compile("^price:((\\d*\\.\\d+|\\d+)?-(\\d*\\.\\d+|\\d+)?)$");
        Matcher mat = pat.matcher(query);
        if (!mat.find()) {
            return new ArrayList<>();
        } else {
            ArrayList<PriceConditionItem> items = new ArrayList<>();
            if (mat.group(3) != null) {
                PriceConditionItem item = new PriceConditionItem("<=", Double.parseDouble(mat.group(3)));
                items.add(item);
            }
            if (mat.group(2) != null) {
                PriceConditionItem item = new PriceConditionItem(">=", Double.parseDouble(mat.group(2)));
                items.add(item);
            }
            return items;
        }
    }
}