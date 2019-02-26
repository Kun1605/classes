import java.util.ArrayList;
import java.util.List;
 
/**
 * Created by Spirit on 2017/1/23.
 */
public class RedEnvelopesDemo {
    //红包最小值
    private static final float MINVALUE = 0.01F;
    //红包最大值
    private static final float MAXVALUE = 200F;
 
    /**
     * 这里为了避免某一个红包占用大量资金，我们需要设定非最后一个红包的最大金额，我们把他设置为红包金额平均值的N倍；
     */
    private static final float TIMES = 2.1F;
 
    /**
     * 判断红包是否合情理
     * @param money
     * @param count
     * @return
     */
    public boolean isRight(float money,int count) {
        float avg = money/count;
        if(avg < MINVALUE) {
            return false;
        } else if(avg > MAXVALUE) {
            return false;
        }
        return true;
    }
 
    /**
     * 分红包核心算法
     * @param money
     * @param minS
     * @param maxS
     * @param count
     * @return
     */
    public float randomRedPacket(float money,float minS,float maxS,int count) {
        //当人数剩余一个时，把当前剩余全部返回
        if(count == 1) {
            return money;
        }
        //如果当前最小红包等于最大红包，之间返回当前红包
        if(minS == maxS) {
            return minS;
        }
        float max = maxS>money?money:maxS;
        //随机产生一个红包
        float one = (float)(Math.random()*(max-minS)+minS);
        float balance = money - one;
        //判断此次分配后，后续是否合理
        if(isRight(balance,count-1)) {
            return one;
        } else {
            //重新分配
            float avg = balance/(count-1);
            //如果本次红包过大，导致下次不够分，走这一条
            if(avg < MINVALUE) {
                return randomRedPacket(money, minS, one, count);
            } else {
                return randomRedPacket(money, one, maxS, count);
            }
        }
    }
 
    /**
     * 分红包
     * @param money
     * @param count
     * @return
     */
    public List<Float> spiltRedPackets(float money,int count) {
        //首先判断红包是否合情理
        if(!isRight(money,count)) {
            return null;
        }
        List<Float> list = new ArrayList<Float>();
        float max = money/count*TIMES;
        max = max>money?money:max;
        for(int i = 0 ; i < count; i++) {
            float value = randomRedPacket(money,MINVALUE,max,count-i);
            list.add(value);
            money -= value;
        }
        return list;
    }
 
    public static void main(String[] args) {
        RedEnvelopesDemo red = new RedEnvelopesDemo();
        System.out.println(red.spiltRedPackets(150,10));
    }
}
