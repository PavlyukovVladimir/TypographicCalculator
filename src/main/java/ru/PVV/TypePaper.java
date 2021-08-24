package ru.PVV;

public class TypePaper {
    private String name;
    private Double cost;

    public TypePaper(String name, Double cost) {
        this.name = name;
        this.cost = cost;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public Double getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)return false;
        if(obj.getClass()!=getClass())return false;
        return ((TypePaper)obj).name==this.name && ((TypePaper)obj).cost==this.cost;
    }

    @Override
    public TypePaper clone() {
        return new TypePaper(name,cost);
    }

    @Override
    public String toString() {
        return name;
    }
}
