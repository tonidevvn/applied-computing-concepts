package ca.uwindsor.appliedcomputing.final_project.util;

import ca.uwindsor.appliedcomputing.final_project.dto.ProductData;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * The Sorting class provides sorting algorithms for sorting lists of map entries.
 * It includes implementations for quick sort and heap sort.
 */
public class Sorting {

    /**
     * Sorts a list of map entries using the heap sort algorithm.
     *
     * @param entries the list of map entries to be sorted
     */
    public static void heapSort(List<Map.Entry<String, Integer>> entries) {
        PriorityQueue<Map.Entry<String, Integer>> heap = new PriorityQueue<>((x, y) -> y.getValue() - x.getValue());
        heap.addAll(entries);
        entries.clear();
        while (!heap.isEmpty()) {
            entries.add(heap.poll());
        }
    }

    /**
     * Sorts a list of ProductData by price using the quick sort algorithm.
     *
     * @param products the list of ProductData to be sorted
     * @param type the type of sorting: 0 for low to high price, 1 for high to low price
     */
    public static void quickSort(List<ProductData> products, int type) {
        quickSort(products, 0, products.size() - 1, type);
    }

    private static void quickSort(List<ProductData> products, int low, int high, int type) {
        if (low < high) {
            int pi = partition(products, low, high, type);
            quickSort(products, low, pi - 1, type);
            quickSort(products, pi + 1, high, type);
        }
    }

    private static int partition(List<ProductData> products, int low, int high, int type) {
        ProductData pivot = products.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (comparePrice(products.get(j), pivot, type)) {
                i++;
                Collections.swap(products, i, j);
            }
        }
        Collections.swap(products, i + 1, high);
        return i + 1;
    }

    private static void swap(List<ProductData> products, int i, int j) {
        ProductData temp = products.get(i);
        products.set(i, products.get(j));
        products.set(j, temp);
    }

    private static boolean comparePrice(ProductData a, ProductData b, int type) {
        double priceA = parsePrice(a.getPrice());
        double priceB = parsePrice(b.getPrice());
        return type == 0 ? priceA < priceB : priceA > priceB;
    }

    private static double parsePrice(String price) {
        try {
            return Double.parseDouble(price.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}