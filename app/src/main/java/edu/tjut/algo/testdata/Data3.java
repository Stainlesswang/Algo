package edu.tjut.algo.testdata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/25.
 */

public class Data3 {private List<Integer> weight=new ArrayList<>();
    private List<Integer> values=new ArrayList<>();
    private int capacity;
    public Data3(List weight,List values,int capacity){
        this.weight=weight;
        this.values=values;
        this.capacity=capacity;
    }
    public List<Integer> getWeight() {
        return weight;
    }

    public void setWeight(List<Integer> weight) {
        this.weight = weight;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}
