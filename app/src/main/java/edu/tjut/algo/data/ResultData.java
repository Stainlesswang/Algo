package edu.tjut.algo.data;
import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by Administrator on 2018/3/29.
 * 每次数据经过处理后的结果类
 * 包括计算结果、耗费时间、哪个算法产生的、
 * 这是要存入数据库分析的东西
 */
public class ResultData extends DataSupport {
    private int id;
    private int dataId;//测试数据编号,根据编号来查找数据的所有属性
    private String bestStr;//对应的最优解字符串 解析字符串获取最优解
    private float time;//耗费的时间  毫秒级别
    private int method;//0,遗传     1-模拟退火     2-爬山法
    private double percent;//匹配度  单位 %
    private String bianhao;
    private int resultValue;
    private int nowWeight;//当前装入重量
    private int capacity;//背包容量
    private Date date;
    public ResultData(int dataId,String bestStr,float time,int method,double percent,int resultValue,
                      int nowWeight,int capacity,Date date){
        this.dataId=dataId;
        this.bestStr=bestStr;
        this.time=time;
        this.method=method;
        this.percent=percent;
        this.resultValue=resultValue;
        this.nowWeight=nowWeight;
        this.capacity=capacity;
        this.date=date;
        if (!"".equals(bestStr)){
            String temp = "";
            int len = bestStr.length();
            for (int i = 0; i < len; i++) {
                if(Integer.parseInt(bestStr.substring(i, i + 1))==1){
                    temp+=i+1+";";
                }
            }
            this.bianhao=temp;
        }

    }
    public ResultData(){

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNowWeight() {
        return nowWeight;
    }

    public void setNowWeight(int nowWeight) {
        this.nowWeight = nowWeight;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

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

    public int getResultValue() {
        return resultValue;
    }

    public void setResultValue(int resultValue) {
        this.resultValue = resultValue;
    }

    public void setBestStr(String bestStr) {
        if (!"".equals(bestStr)){
            String temp = "";
            int len = bestStr.length();

            for (int i = 0; i < len; i++) {
               if(Integer.parseInt(bestStr.substring(i, i + 1))==1){
                   temp+=i+1+";";
               }
                                       }
            this.bianhao=temp;
              }
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

    public String getBianhao() {
        return bianhao;
    }

    public void setBianhao(String bianhao) {
        this.bianhao = bianhao;
    }

    @Override
    public String toString() {
        return " id: "+id+" dataID: "+dataId+"bestStr: "+bestStr
                +" Time: "+time+" Method: "+method+" percent: "+percent+" bianhao: "+bianhao
                +" resultValue: "+resultValue+" nowWeight: "+nowWeight +" capacity: "+capacity;
    }
}
