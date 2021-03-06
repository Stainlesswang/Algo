package edu.tjut.algo.data;
import java.util.ArrayList;
/**
 * Created by Administrator on 2018/3/25.
 * 封装测试物品的信息，方便使用和管理
 */
public class TestData {
    //全部重量的list集合
    private ArrayList<Integer> weight=new ArrayList<>();
    //全部价值的list集合
    private ArrayList<Integer> values=new ArrayList<>();
    //所包含的物品
    private ArrayList<Item> items=new ArrayList<>();
    private int capacity;//背包容量
    private  int numItems;//物品数量



    private boolean[] optimal;//最优解的bool数组
    private double optimalFitness;//已知最优解的总价值
    private int totalValue = 0;//所有物品总价值
    //penalty，offset是一定规则算出的物品误差偏移值，可以不用考虑
    double penalty = 0;
     double offset=0;
    int dataID=0;

    public TestData(ArrayList<Integer> weight, ArrayList<Integer> values, int capacity,
                    int numItems,int totalValue,boolean[] optimal,double optimalFitness,int dataID,double penalty,double offset){
        this.weight=weight;
        this.values=values;
        this.capacity=capacity;
        this.numItems=numItems;
        this.totalValue=totalValue;
        this.optimal=optimal;
        this.optimalFitness=optimalFitness;
        this.dataID=dataID;
        this.penalty=penalty;
        this.offset=offset;
        for(int i = 0; i < numItems; i++)
        {
            Item item=new Item();
            item.setWeight(weight.get(i));
            item.setValue(values.get(i));
            item.setDataID(dataID);
            items.add(item);
        }
    }
    public TestData(ArrayList<Integer> weight, ArrayList<Integer> values, int capacity,
                    int numItems){
        this.weight=weight;
        this.values=values;
        this.capacity=capacity;
        this.numItems=numItems;
        for(int i = 0; i < numItems; i++)
        {
            Item item=new Item();
            item.setWeight(weight.get(i));
            item.setValue(values.get(i));
            item.setDataID(dataID);
            items.add(item);
        }
    }
    public TestData(ArrayList<Integer> weight, ArrayList<Integer> values,
                    int numItems){
        this.weight=weight;
        this.values=values;
        this.numItems=numItems;
        for(int i = 0; i < numItems; i++)
        {
            Item item=new Item();
            item.setWeight(weight.get(i));
            item.setValue(values.get(i));
            item.setDataID(dataID);
            items.add(item);
        }
    }
    public int getDataID() {
        return dataID;
    }

    public void setDataID(int dataID) {
        this.dataID = dataID;
    }

    public ArrayList<Integer> getWeight() {
        return weight;
    }

    public void setWeight(ArrayList<Integer> weight) {
        this.weight = weight;
    }

    public ArrayList<Integer> getValues() {
        return values;
    }

    public void setValues(ArrayList<Integer> values) {
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

    public double getOptimalFitness() {
        return optimalFitness;
    }

    public void setOptimalFitness(double optimalFitness) {
        this.optimalFitness = optimalFitness;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return " weight: "+weight.toString()+" values: "+values.toString()+"optimal: "+optimal.toString()
                +" capacity: "+capacity+" totalValue: "+totalValue+" numItems: "+numItems+" optimalFitness: "+optimalFitness
                +" penalty: "+penalty+" offset: "+offset;
    }
}
