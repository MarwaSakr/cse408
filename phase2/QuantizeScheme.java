import java.lang.Math;

public class QuantizeScheme {
    private int[] numBuckets;
    private int[] minVal;
    private int[] maxVal;
    

    public static final int Y=0;
    public static final int U=1;
    public static final int V=2;
    minVal[Y] = 0;
    maxVal[Y] = 255;
    minVal[U] = -128;
    maxVal[U] = 128;
    minVal[V] = -128;
    maxVal[V] = 128;
    
    public QuantizeScheme(int bucketY, int bucketU, int bucketV){
        numBuckets = new int[3];
        numBuckets[Y] = numBucketsY;
        numBuckets[U] = numBucketsU;
        numBuckets[V] = numBucketsV;
    }
    public QuantizeScheme(){
        numBuckets = new int[3];
        numBuckets[Y] = 1;
        numBuckets[U] = 1;
        numBuckets[V] = 1;
    }
    
    public int quantize(float num, int Type){
	    int numBucket = numBuckets[Type];
	    float stepSize = (abs(min)+abs(max))/numBucket;
	    if (numBuckets[Type] % 2 == 0){
	        return (int) (ceiling(num)-stepSize/2);
	    } else {
	        return (int) (floor(num-stepSize/2));
	    }
    }
    
    private float ceiling(float num, int Type){
        int min = minVal[Type];
	    int max = maxVal[Type];
	    float stepSize = (abs(min)+abs(max))/numBucket;
        float ceil = min;
        while(ceil < num){
            ceil += stepSize;
        }
        return ceil;
    }
    private float floor(float num, int Type){
        int min = minVal[Type];
	    int max = maxVal[Type];
	    float stepSize = (abs(min)+abs(max))/numBucket;
	    float floor = max;
	    while(floor > num){
	        floor = floor-stepSize;
	    }
        return floor;
    }
}
