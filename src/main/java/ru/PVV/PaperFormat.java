package ru.PVV;

public class PaperFormat{
    private String name;
    private int count;

    public PaperFormat(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)return false;
        if(obj.getClass()!=getClass())return false;
        return ((PaperFormat)obj).name==this.name && ((PaperFormat)obj).count ==this.count;
    }

    @Override
    public PaperFormat clone() {
        return new PaperFormat(name, count);
    }

    @Override
    public String toString() {
        return name+" "+ count +" на A3";
    }
}
