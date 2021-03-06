package edu.tjut.algo.util;
import edu.tjut.algo.R;
/**
 * Created by Administrator on 2018/3/26.
 *一些变量的枚举类，就是将一个一个都列出来，方便使用
 */

public enum TextEnum {
    TXT_P01(R.raw.p01_c,R.raw.p01_p,R.raw.p01_s,R.raw.p01_w,1),
    TXT_P02(R.raw.p02_c,R.raw.p02_p,R.raw.p02_s,R.raw.p02_w,2),
    TXT_P03(R.raw.p03_c,R.raw.p03_p,R.raw.p03_s,R.raw.p03_w,3),
    TXT_P04(R.raw.p04_c,R.raw.p04_p,R.raw.p04_s,R.raw.p04_w,4),
    TXT_P05(R.raw.p05_c,R.raw.p05_p,R.raw.p05_s,R.raw.p05_w,5),
    TXT_P06(R.raw.p06_c,R.raw.p06_p,R.raw.p06_s,R.raw.p06_w,6),
    TXT_P07(R.raw.p07_c,R.raw.p07_p,R.raw.p07_s,R.raw.p07_w,7),
    TXT_P08(R.raw.p08_c,R.raw.p08_p,R.raw.p08_s,R.raw.p08_w,8);
    public int c;
    public int p;
    public int s;
    public int w;
    public int id;
    TextEnum(int c, int p, int s, int w,int id){
        this.c=c;
        this.p=p;
        this.s=s;
        this.w=w;
        this.id=id;
    }
}
