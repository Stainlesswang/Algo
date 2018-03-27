package edu.tjut.algo;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import edu.tjut.algo.testdata.TestData;

/**
 * Created by Administrator on 2018/3/26.
 * 该类是整个应用的工具类，所有通用的方法全部写在这里
 */
public class AlgoUtil {

    public static TestData getDataFromTxt(Context context,TextEnum textEnum){
         ArrayList<Integer> values = new ArrayList<Integer>();//所有物品价值的数组
         ArrayList<Integer> sizes = new ArrayList<Integer>();//物品体积的数组
         int numItems = 0;//物品数量
         int capacity = 0;//背包容量
         boolean[] optimal;//用来保存最优解的boole数组
        int totalValue = 0;//所有物品总价值

        //获取背包的总容量
        Scanner cScanner = new Scanner(context.getResources().openRawResource(textEnum.c));
        capacity = cScanner.nextInt();
        cScanner.close();

        //获取重量数组
        Scanner wScanner = new Scanner(context.getResources().openRawResource(textEnum.w));
        while(wScanner.hasNextInt())
        {
            numItems++;
            sizes.add(wScanner.nextInt());
        }
        wScanner.close();


        //获取价值数组
        Scanner pScanner = new Scanner(context.getResources().openRawResource(textEnum.p));
        while(pScanner.hasNextInt())
        {
            values.add(pScanner.nextInt());
            totalValue += values.get(values.size()-1);
        }
        pScanner.close();


            Scanner sScanner = new Scanner(context.getResources().openRawResource(textEnum.s));
            optimal = new boolean[numItems];
            for(int i = 0; i < numItems && sScanner.hasNextInt(); i++)
            {
                if( sScanner.nextInt() == 0 )
                    optimal[i] = false;
                else
                    optimal[i] = true;
            }
            sScanner.close();
        return new TestData(sizes,values,capacity,numItems,totalValue,optimal);
    }
}
