package edu.tjut.algo.sa;
/**
 * https://github.com/AntonRidgway/Knapsack-GA/blob/master/src/SimulatedAnnealing.java
 */
import java.util.Random;
import java.util.ArrayList;
import java.util.TreeSet;

import edu.tjut.algo.data.ResultData;
import edu.tjut.algo.data.TestData;
import edu.tjut.algo.ga.MyMethod;

public class SimulatedAnnealing implements MyMethod
{
    public static boolean foolish = false;//true选择效率较慢的爬山算法,false模拟退火算法 (false = Simulated Annealing, true = Foolish Hill-climber)
    public static double tempVal = 60;//初始化温度 (40-60)
    public static double aValue = 0.95;//每次降温到原来温度的0.95倍  (.95-.99)
    public static double tempThreshold = 3;//温度的阀值，到达该温度停止  原计算方式 tempThreshold *= tempVal;取值范围(.01-.05)
    public static int numIter = 100;//迭代的次数 (1000-5000)
    public static double bValue = 1.05;//每次迭代后扩大为原来的1.05倍 (1.01-1.05)
    public static int perturbSel = 1;//选择 N-Point 还是 N-Slice   改变方式 (0 = N-Point Perturbation, 1 = N-Slice Inversion)
    public static int nValue = 3;//反转的位数是几个  (1-3)

    public static Random randomizer = new Random();//用来产生随机数的类

    //01-Knapsack's variables
    private int capacity = 0;//背包容量
    private int numItems = 0;//物品数量
    private int totalValue = 0;//所有物品总价值
    private boolean[] optimal;
    private double optimalFitness;
    private ArrayList<Integer> values = new ArrayList<Integer>();//所有物品价值的数组
    private ArrayList<Integer> sizes = new ArrayList<Integer>();//物品体积的数组
    private double penalty = 0;
    private double offset = 0;
    private int dataId=0;


    //当foolish为true的时候选择 爬山算法！
    public SimulatedAnnealing(TestData testData,boolean foolish){
        if(foolish){
            this.foolish=foolish;
        }
        if (tempVal<=tempThreshold){
            tempVal=60;
        }
        this.capacity=testData.getCapacity();
        this.numItems=testData.getNumItems();
        this.totalValue=testData.getTotalValue();
        this.optimal=testData.getOptimal();
        this.values=testData.getValues();
        this.sizes=testData.getWeight();this.dataId=testData.getDataID();
        this.optimalFitness=testData.getOptimalFitness();

    }
    //构造函数将测试数据注入进来，系数暂时设为不可改变
    public SimulatedAnnealing(TestData testData){
        if (tempVal<=tempThreshold){
            tempVal=60;
        }
        this.capacity=testData.getCapacity();
        this.numItems=testData.getNumItems();
        this.totalValue=testData.getTotalValue();
        this.optimal=testData.getOptimal();
        this.values=testData.getValues();
        this.sizes=testData.getWeight();
        this.dataId=testData.getDataID();
        this.optimalFitness=testData.getOptimalFitness();
        this.penalty=testData.getPenalty();
        this.offset=testData.getOffset();
    }
    public SimulatedAnnealing(){}
    public ResultData solve()
    {
        //bit strings as a boolean array
        ResultData resultData=new ResultData();
        boolean[] sol = new boolean[numItems];
        for(int i = 0; i < numItems; i++)
        {
            sol[i] = randomizer.nextBoolean();
        }
        double solFitness = fitness(sol);

        //Keep a running best solution.
        boolean[] bestSol = new boolean[numItems];
        double bestFitness = 0;
        int pOfBest = 0;
        int pSoFar = 0;

        //-----------------------------------------------------------------------------------------
        // Step 3: Perform Simulated Annealing
        //记录当前执行开始时间
        long startTime=System.currentTimeMillis();


        while(tempVal > tempThreshold)
        {
            //开始迭代
            for( int i = 0; i < numIter; i++ )
            {
                boolean[] newSol;//用来保存新产生的组合
                //选择不同的方式来变化组合方式
                if(perturbSel == 1)
                    newSol = perturbNSlice(sol, nValue);
                else
                    newSol = perturbNPoint(sol, nValue);
                //计数加1
                pSoFar++;

                //计算符合度
                double newFitness = fitness(newSol);


                //新产生的较优秀 或者满足 一定的概率接受当前的任何解   更新当前的组合
                //此处是模拟退火算法的关键，当foolish=false的时候  !foolish=true，此时以一定的条件来接受比当前解
                if( newFitness >= solFitness || (!foolish && (randomizer.nextDouble()) < Math.exp((newFitness-solFitness)/tempVal)) )
                {
                    sol = newSol;
                    solFitness = newFitness;
                }

                //更新最新的最优解
                if(solFitness > bestFitness && getChromSize(sol) <= capacity)
                {

                    for(int j = 0; j < numItems; j++)
                        bestSol[j] = sol[j];
                    bestFitness = solFitness;
                    pOfBest = pSoFar;
                }
            }
            tempVal *= aValue;//降温
            numIter *= bValue;//更新迭代的次数
            //要不要降温的过程呢？  待定吧，只能使用于模拟退火算法
            //Print user information.
            System.out.println("Current Temperature: "+tempVal);
                double percent = (solFitness/optimalFitness)*100;
                System.out.println("solFitness::"+solFitness+"optimalFitness::"+optimalFitness+"percent:"+percent);
                System.out.printf("Current Fitness: %,.2f%% of known optimal.\n" , percent);
        }
        //跳出while循环 记录结束时间，然后计算出总共执行时间
        long endTime=System.currentTimeMillis();
        float excTime=(float)(endTime-startTime)/1000;
        System.out.println("执行时间："+excTime+"s");

        //-----------------------------------------------------------------------------------------
        // Step 4: Print Results
        String solStr = "";
        int solSize = 0;
        int solValue = 0;
        for(int i = 0; i < numItems; i++)
        {
            if(bestSol[i] == true)
            {
                solStr += "1";
                solSize += sizes.get(i);
                solValue += values.get(i);
            }
            else
                solStr += "0";
        }
        resultData.setBestStr(solStr);
        double percent = (solValue/optimalFitness)*100;
        resultData.setPercent(percent);
        System.out.println("Size out of Capacity: " + solSize + "/" + capacity);
        resultData.setNowWeight(solSize);
        resultData.setCapacity(capacity);
        resultData.setResultValue(solValue);

        System.out.println("Value: " + solValue );

        System.out.println("Number of perturbations to find: " + pOfBest);
        System.out.println("when the code id finish the tempval: "+tempVal);
        resultData.setDataId(dataId);
        if (foolish){
            resultData.setMethod(2);
        }else {
            resultData.setMethod(1);
        }
        resultData.setTime(excTime);
        //充值参数哇兄弟
        foolish = false;//true选择效率较慢的爬山算法,false模拟退火算法 (false = Simulated Annealing, true = Foolish Hill-climber)
          tempVal = 60;//初始化温度 (40-60)
          aValue = 0.95;//每次降温到原来温度的0.95倍  (.95-.99)
         tempThreshold = 3;//温度的阀值，到达该温度停止  原计算方式 tempThreshold *= tempVal;取值范围(.01-.05)
         numIter = 100;//迭代的次数 (1000-5000)
         bValue = 1.05;//每次迭代后扩大为原来的1.05倍 (1.01-1.05)
         perturbSel = 1;//选择 N-Point 还是 N-Slice   改变方式 (0 = N-Point Perturbation, 1 = N-Slice Inversion)
         nValue = 3;//反转的位数是几个  (1-3)
        return resultData;
    }




















/********************************* 以下是调用的各种方法 ***************************/

    public  double fitness(boolean[] c,int numItems,ArrayList<Integer> values,ArrayList<Integer> sizes,
                           int capacity ,double penalty,double offset)
    {
        this.sizes=sizes;
        this.values=values;
        this.numItems=numItems;
        //Get the chromosome's value
        int runningValue = getChromValue(c);//当前组合的总价值
        int runningSize = getChromSize(c);//当前组合的总体积
        System.out.println(runningSize+"___________________________________++++++++++++");

        //判断当前体积是否大于背包容量
        if( runningSize > capacity )
        {
            double returnMe = runningValue - ((runningSize - capacity) * penalty + offset);
            if (returnMe < 0)
                return 0;
            else
                return returnMe;


        }
        else
            return runningValue;
    }
    public  double fitness( boolean[] c )
    {
        //Get the chromosome's value
        int runningValue = getChromValue(c);//当前组合的总价值
        int runningSize = getChromSize(c);//当前组合的总体积
        //判断当前体积是否大于背包容量
        if( runningSize > capacity )
        {
            double returnMe = runningValue - ((runningSize - capacity) * penalty + offset);
            if (returnMe < 0)
                return 0;
            else
                return returnMe;


        }
        else
            return runningValue;
    }


    public  boolean[] perturbNPoint(boolean[] s, int n)
    {
        //Get n unique, random, sorted indices to slice. (Each slice-number represents the point after
        //chromosome index n and before n+1.) Stop n from exceeding numItems-1, its max value.

        TreeSet<Integer> indices = new TreeSet<Integer>();
        int iNeeded;
        if(n < numItems)
            iNeeded = n;
        else
            iNeeded = numItems - 1;

        for (int j = 0; j < iNeeded; j++)
        {
            int guess = randomizer.nextInt(numItems-1);
            while(indices.contains(guess))
            {
                guess = (guess+1)%(numItems-1);
            }
            indices.add(guess);
        }

        for(Integer currSlice: indices)
        {
            s[currSlice] = !s[currSlice];
        }
        return s;
    }


    public  boolean[] perturbNSlice(boolean[] s, int n)
    {
        //Get n unique, random, sorted indices to slice. (Each slice-number represents the point after
        //chromosome index n and before n+1.) Stop n from exceeding numItems-1, its max value.

        TreeSet<Integer> indices = new TreeSet<Integer>();
        int iNeeded;
        if(n < numItems)
            iNeeded = n;
        else
            iNeeded = numItems - 1;

        for (int i = 0; i < iNeeded; i++)
        {
            int guess = randomizer.nextInt(numItems-1);
            while(indices.contains(guess))
            {
                guess = (guess+1)%(numItems-1);
            }
            indices.add(guess);
        }

        boolean inverting = randomizer.nextBoolean();
        int startSlice = 0;
        for(Integer nextSlice: indices)
        {
            if (inverting)
            {
                for( int i = startSlice; i <= nextSlice; i++ )
                    s[i] = !s[i];
            }
            inverting = !inverting;
            startSlice = nextSlice+1;
        }
        if(inverting)
        {
            for(int i = startSlice; i < numItems; i++)
                s[i] = !s[i];
        }

        return s;
    }

    public  String chromToString( boolean[] c )
    {
        String temp = "";
        for(int i = 0; i < numItems; i++ )
        {
            if (c[i] == true)
                temp += "1";
            else
                temp += "0";
        }
        return temp;
    }
    public  int getChromValue( boolean[] c )
    {
        int temp = 0;
        for(int i = 0; i < numItems; i++ )
        {
            if (c[i] == true)
                temp += values.get(i);
        }
        return temp;
    }
    public  int getChromSize( boolean[] c )
    {
        int temp = 0;
        for(int i = 0; i < numItems; i++ )
        {
            if (c[i] == true)
                temp += sizes.get(i);
        }
        return temp;
    }
}