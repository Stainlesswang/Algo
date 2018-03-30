package edu.tjut.algo.date;
/**
 * Created by Administrator on 2018/3/29.
 * 每次数据经过处理后的结果类
 * 包括计算结果、耗费时间、哪个算法产生的、
 * 这是要存入数据库分析的东西
 *
 *
 * 下一步计划，先把方法修改了，给参数，给测试数据，返回该类封装成这个
 * 然后把这些类信息存入数据库
 * 然后想想办法怎么显示在图表中
 */
public class ResultData {
    private String dataId;//测试数据编号
    private String bestStr;//对应的最优解字符串
    private float time;//耗费的时间  毫秒级别
    private int method;//0,遗传     1-模拟退火     2-爬山法
}
