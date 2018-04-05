package edu.tjut.algo.data;
import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/3/29.
 * 每次数据经过处理后的结果类
 * 包括计算结果、耗费时间、哪个算法产生的、
 * 这是要存入数据库分析的东西
 * 下一步计划，先把方法修改了，给参数，给测试数据，返回该类封装成这个
 * 然后把这些类信息存入数据库
 * 然后想想办法怎么显示在图表中
 */
public class ResultData extends DataSupport {

    private int id;
    private int dataId;//测试数据编号,根据编号来查找数据的所有属性
    private String bestStr;//对应的最优解字符串 解析字符串获取最优解
    private float time;//耗费的时间  毫秒级别
    private int method;//0,遗传     1-模拟退火     2-爬山法
    private double percent;//匹配度  单位 %

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public String getBestStr() {
        return bestStr;
    }

    public void setBestStr(String bestStr) {
        this.bestStr = bestStr;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }
}
