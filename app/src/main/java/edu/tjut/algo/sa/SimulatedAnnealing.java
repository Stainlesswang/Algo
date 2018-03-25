package edu.tjut.algo.sa;
/**
 * https://github.com/AntonRidgway/Knapsack-GA/blob/master/src/SimulatedAnnealing.java
 * SimulatedAnnealing implements a simulated annealing algorithm to solve the 01-Knapsack
 * Problem. It collects parameters from the user, and performs the annealing, using one of
 * two perturbation functions:
 *
 * - N-Point Perturbation
 * - N-Slice Perturbation
 *
 * It loads datasets in the format of those hosted at
 * http://people.sc.fsu.edu/~jburkardt/datasets/knapsack_01/knapsack_01.html.
 *
 * After the temperature value passes an arbitrary threshold (.0001, practically zero), the annealing
 * ends, and the ending solution is presented, with the optimal solution for comparison.
 *
 * @author Anton Ridgway
 */

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.TreeSet;

public class SimulatedAnnealing
{

    public static boolean foolish = false;//true选择效率较慢的爬山算法,false模拟退火算法 (false = Simulated Annealing, true = Foolish Hill-climber)
    public static double tempVal = 60;//初始化温度 (40-60)
    public static double aValue = 0.95;//每次降温到原来温度的0.95倍  (.95-.99)
    public static double tempThreshold = 3;//温度的阀值，到达该温度停止  原计算方式 tempThreshold *= tempVal;取值范围(.01-.05)
    public static int numIter = 1000;//迭代的次数 (1000-5000)
    public static double bValue = 1.05;//每次迭代后扩大为原来的1.05倍 (1.01-1.05)
    public static int perturbSel = 1;//选择 N-Point 还是 N-Slice   改变方式 (0 = N-Point Perturbation, 1 = N-Slice Inversion)
    public static int nValue = 3;//反转的位数是几个  (1-3)


    public static Random randomizer = new Random();//用来产生随机数的类

    //01-Knapsack's variables
    static int capacity = 0;//背包容量
    static int numItems = 0;//物品数量
    static int totalValue = 0;//所有物品总价值
    static double penalty = 0;
    static double offset = 0;
    static boolean optimalKnown = false;
    static boolean[] optimal;
    static double optimalFitness;
    static ArrayList<Integer> values = new ArrayList<Integer>();//所有物品价值的数组
    static ArrayList<Integer> sizes = new ArrayList<Integer>();//物品体积的数组

    public static void main(String[] args)
    {
//        boolean[] test = new boolean[]{false,false,false,false,false,true,false,false,};




        //-----------------------------------------------------------------------------------------
        // Step 1: Get User Input
        Scanner inputReader = new Scanner(System.in);
        int notFound;
        String prefix;
        System.out.print("Enter dataset prefix: ");

//        File source = new File(System.getProperty("java.class.path"));
//        String binDirectory = source.getAbsoluteFile().getParentFile().toString()+File.separator;
        String binDirectory="C:/Users/Administrator/Desktop/";
        System.out.println("\nbinDirectory " + binDirectory + "...");
        do // Check for the presence of the dataset.
        {
            notFound = 0;
            prefix = inputReader.next();
            File fileChecker = new File(binDirectory+prefix+"_c.txt");
            if(fileChecker.exists())
                notFound++;
            fileChecker = new File(binDirectory+prefix+"_p.txt");
            if(fileChecker.exists())
                notFound++;
            fileChecker = new File(binDirectory+prefix+"_w.txt");
            if(fileChecker.exists())
                notFound++;
            if(notFound < 3) System.out.print("Full dataset could not be found.\nTry again: ");
        } while (notFound < 3);

        File fileChecker = new File(binDirectory+prefix+"_s.txt");
        if(fileChecker.exists())
            optimalKnown = true;

        inputReader.close();

        //-----------------------------------------------------------------------------------------
        // Step 2: Load Selected Dataset
        try
        {
            System.out.println("\nGetting dataset " + prefix + "...");
            System.out.println("Getting "+prefix+"_c.txt");
            Scanner cScanner = new Scanner(new File(binDirectory+prefix+"_c.txt"));
            capacity = cScanner.nextInt();
            cScanner.close();

            System.out.println("Getting "+prefix+"_w.txt");
            Scanner wScanner = new Scanner(new File(binDirectory+prefix+"_w.txt"));
            while(wScanner.hasNextInt())
            {
                numItems++;
                sizes.add(wScanner.nextInt());
            }
            wScanner.close();

            System.out.println("Getting "+prefix+"_p.txt");
            Scanner pScanner = new Scanner(new File(binDirectory+prefix+"_p.txt"));
            while(pScanner.hasNextInt())
            {
                values.add(pScanner.nextInt());
                totalValue += values.get(values.size()-1);
            }
            pScanner.close();

            if(optimalKnown)//判断存在_s.txt，转化为optimal boolean数组
            {
                System.out.println("Getting "+prefix+"_s.txt");
                Scanner sScanner = new Scanner(new File(binDirectory+prefix+"_s.txt"));

                optimal = new boolean[numItems];
                for(int i = 0; i < numItems && sScanner.hasNextInt(); i++)
                {
                    if( sScanner.nextInt() == 0 )
                        optimal[i] = false;
                    else
                        optimal[i] = true;
                }
                optimalFitness = fitness(optimal);
                sScanner.close();
            }
        }
        catch(IOException e)
        {
            System.err.println("IOException: " + e);
        }
        System.out.println(" item # |  value |   size |");
        for(int i = 0; i < numItems; i++)
            System.out.printf("%7d |%7d |%7d |\n",i,values.get(i),sizes.get(i));
        System.out.println("Capacity: " + capacity);
        System.out.println("Number of Items: " + numItems);
        System.out.println("Total Value: " + totalValue);
        System.out.println();

        //Determine over-capacity penalties.
        //penalty per-unit-over-capacity is equal to the highest value per unit size ratio of any package
        //offset penalty is the average package value, times one tenth of the number of packages
        double temp;
        for(int i = 0; i < numItems; i++)
        {
            offset += values.get(i);
            temp = ((double)values.get(i))/sizes.get(i);
            if( temp > penalty )
                penalty = temp;
        }
        offset *= .3;

        //Uncomment to test SA components.
//        testBattery();

        //bit strings as a boolean array
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
            System.out.println("当前迭代的次数："+numIter);
            //Print user information.
            System.out.println("Current Temperature: "+tempVal);
            if(optimalKnown)
            {
                double percent = (solFitness/optimalFitness)*100;
                System.out.println("solFitness::"+solFitness+"optimalFitness::"+optimalFitness+"percent:"+percent);
                System.out.printf("Current Fitness: %,.2f%% of known optimal.\n" , percent);
            }
            else
            {
                System.out.println("Current Fitness: "+solFitness);
            }
        }

        //-----------------------------------------------------------------------------------------
        // Step 4: Print Results
        String solStr = "";
        int solSize = 0;
        int solValue = 0;
        String optStr = "";
        int optSize = 0;
        int optValue = 0;
        boolean sameAsOptimal = true;
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
            if(optimalKnown)
            {
                if(optimal[i] == true)
                {
                    optStr += "1";
                    optSize += sizes.get(i);
                    optValue += values.get(i);
                }
                else
                    optStr += "0";
                if(bestSol[i] != optimal[i])
                    sameAsOptimal = false;
            }
        }

        if(optimalKnown)//输出最优值
    {
        System.out.println("\nOptimal Solution: " + optStr);
        System.out.println("Optimal Fitness: " + optimalFitness);
        System.out.println("Optimal Size out of Capacity: " + optSize + "/" + capacity);
        System.out.println("Optimal Value: " + optValue );
    }
    else
    {
        sameAsOptimal = false;
    }

        System.out.println("\nFinal Solution: " + solStr);
        System.out.println("bestFitness: " + fitness(bestSol));
        System.out.println("solFitness: " + fitness(sol));

        if(optimalKnown)
        {
            double percent = (bestFitness/optimalFitness)*100;
            System.out.printf(" (%,.2f%% of optimal)\n" , percent);
            if(sameAsOptimal)
                System.out.println("==Found the optimal!==" );
        }
        System.out.println("Size out of Capacity: " + solSize + "/" + capacity);
        System.out.println("Value: " + solValue );
        System.out.println("Number of perturbations to find: " + pOfBest);
    }

/********************************* 以下是调用的各种方法 ***************************/
    /**
     * fitness represents the following function:
     *
     * 		V-(X*(P*(S-C)+O))
     *
     * where
     * V is the total value of all items selected in a given chromosome.
     * S is the total size of all items selected.
     * C is the capacity of the knapsack.
     * X is 0 when S <= C, and 1 otherwise.
     * P is the penalty to be given per unit-size-over-capacity for the
     * 				current dataset (calculated when it is loaded).
     * O is an offset penalty to be automatically applied when a chromosome
     * 				is over capacity.
     *
     * Negative values are normalized to zero.
     *
     * @param c chromosome to evaluate
     * @return the fitness of the chromosome
     */
    public static double fitness( boolean[] c )
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

    /**
     * perturbNPoint takes the current solution, and perturbs it by inverting n unique,
     * random bits.
     *
     * @param s the current solution to perturb
     * @param n the number of bits to perturb
     * @return s the perturbed solution
     */
    public static boolean[] perturbNPoint(boolean[] s, int n)
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

    /**
     * perturbNSlice takes a set of slice points, and inverts the chromosome between them.  Note
     * that it is random per-use of the perturbation function whether the perturbation begins inverting
     * from the first of the chromosome, or waits until the first slice point.  Thus, in a sequence
     * 000X0000X000, where X represents a slice point, the result might be either 111X0000X111 or
     * 000X1111X000.
     *
     * @param s the chromosome to perturb
     * @param n the number of slice points
     * @return s the perturbed chromosome
     */
    public static boolean[] perturbNSlice(boolean[] s, int n)
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

    public static void testBattery()
    {
        boolean[] testC = new boolean[numItems];
        for(int i = 0; i < numItems; i++)
            testC[i] = randomizer.nextBoolean();

        System.out.println("Testing Fitness Function...");
        System.out.println("Random chromosome: " + chromToString(testC));
        int tempS = getChromSize(testC);
        System.out.println("Total Size: " + tempS);
        System.out.println("Total Value: " + getChromValue(testC));
        System.out.println("Capacity: " + capacity);
        System.out.println("Penalty: " + penalty);
        System.out.println("Offset: " + offset);
        System.out.println("Fitness Function Output: " + fitness(testC));
        System.out.println("(Hand-calculate fitness and compare to test.)");

        System.out.println("\nTesting N-Point Perturbation (n = 2)...");
        System.out.println("Before: " + chromToString(testC));
        perturbNPoint(testC,2);
        System.out.println("After:  " + chromToString(testC));
        System.out.println("\nTesting N-Point Perturbation (n = 3)...");
        System.out.println("Before: " + chromToString(testC));
        perturbNPoint(testC,3);
        System.out.println("After:  " + chromToString(testC));

        System.out.println("\nTesting N-Slice Perturbation (n = 2)...");
        System.out.println("Before: " + chromToString(testC));
        perturbNSlice(testC,2);
        System.out.println("After:  " + chromToString(testC));
        System.out.println("\nTesting N-Slice Perturbation (n = 3)...");
        System.out.println("Before: " + chromToString(testC));
        perturbNSlice(testC,3);
        System.out.println("After:  " + chromToString(testC));

        System.out.println("\nTesting finished.\n");
    }

    /**
     * chromToString is a method that generates a string from a given chromosome.
     *
     * @param c the chromosome to make the string from
     * @return temp the string
     */
    public static String chromToString( boolean[] c )
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

    /**
     * getChromValue calculates the total value of a given chromosome.
     *
     * @param c the chromosome to get the value of
     * @return temp the total value
     */
    public static int getChromValue( boolean[] c )
    {
        int temp = 0;
        for(int i = 0; i < numItems; i++ )
        {
            if (c[i] == true)
                temp += values.get(i);
        }
        return temp;
    }

    /**
     * getChromSize calculates the total size of the given chromosome.
     *
     * @param c the chromosome to get the size of
     * @return temp the total size of the chromosome
     */
    public static int getChromSize( boolean[] c )
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