package edu.tjut.algo.data;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/4/8.
 * 每个物品的实体类，方便日后的维护和显示
 */

public class Item extends DataSupport {
    private int id;//唯一标识
    private String name;//物品名称
    private Integer weight;//物品的重量
    private Integer value;//物品的价值
    private Integer dataID;//数据属于哪一组测试数据

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getDataID() {
        return dataID;
    }

    public void setDataID(Integer dataID) {
        this.dataID = dataID;
    }
}
