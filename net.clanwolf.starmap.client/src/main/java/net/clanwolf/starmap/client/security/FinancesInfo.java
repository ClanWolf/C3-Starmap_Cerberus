package net.clanwolf.starmap.client.security;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FinancesInfo {
    private final StringProperty income;
    private final StringProperty incomeDescription;

    public FinancesInfo(String income, String incomeDescription) {
        this.income = new SimpleStringProperty(income);
        this.incomeDescription = new SimpleStringProperty(incomeDescription);
    }

    public String getIncome() {
        return income.get();
    }

    public void setIncome(String income) {
        this.income.set( income);
    }

    public StringProperty incomeProperty() {
        return income;
    }

    public String getIncomeDescription() {
        return incomeDescription.get();
    }

    public void setIncomeDescription(String incomeDescription) {
        this.incomeDescription.set(incomeDescription);
    }

    public StringProperty incomeDescriptionProperty() {
        return incomeDescription;
    }
}
