package edu.tjut.algo.testdata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/25.
 */

public class TestData {
    //全部重量的list集合
    private List<Integer> weight=new ArrayList<>();
    //全部价值的list集合
    private List<Integer> values=new ArrayList<>();
    private int capacity;
    private boolean[] optimal;
    private  int numItems;//物品数量
    private int totalValue = 0;//所有物品总价值
    public TestData(List weight, List values, int capacity,int numItems,int totalValue,boolean[] optimal){
        this.weight=weight;
        this.values=values;
        this.capacity=capacity;
        this.numItems=numItems;
        this.totalValue=totalValue;
        this.optimal=optimal;
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

    public boolean[] getOptimal() {
        return optimal;
    }

    public void setOptimal(boolean[] optimal) {
        this.optimal = optimal;
    }

    public int getNumItems() {
        return numItems;
    }

    public void setNumItems(int numItems) {
        this.numItems = numItems;
    }

    public int getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    @Override
    public String toString() {
        return " weight: "+weight.toString()+" values: "+values.toString()+"optimal: "+optimal.toString()
                +" capacity: "+capacity+" totalValue: "+totalValue+" numItems: "+numItems;
    }
}
