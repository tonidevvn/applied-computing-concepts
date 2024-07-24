package ca.uwindsor.appliedcomputing.final_project.dto;

public class PriceConditionItem {
    public String op;
    public double value;
    public PriceConditionItem(String op, double v) {
        this.op = op;
        this.value = v;
    }
}