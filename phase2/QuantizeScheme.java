import java.lang.Math;
import java.util.Scanner;
import java.util.InputMismatchException;

public class QuantizeScheme {
    private int[] numBuckets;
    private static int[] minVal;
    private static int[] maxVal;
    

    public static final int Y=0;
    public static final int U=1;
    public static final int V=2;
    
    
    public QuantizeScheme(int bucketY, int bucketU, int bucketV){
        numBuckets = new int[3];
        numBuckets[Y] = bucketY;
        numBuckets[U] = bucketU;
        numBuckets[V] = bucketV;
        minVal = new int[3];
        maxVal = new int[3];
        minVal[Y] = 0;
        maxVal[Y] = 255;
        minVal[U] = -128;
        maxVal[U] = 128;
        minVal[V] = -128;
        maxVal[V] = 128;
    }
    public QuantizeScheme(){
        numBuckets = new int[3];
        numBuckets[Y] = 1;
        numBuckets[U] = 1;
        numBuckets[V] = 1;
        minVal[Y] = 0;
        maxVal[Y] = 255;
        minVal[U] = -128;
        maxVal[U] = 128;
        minVal[V] = -128;
        maxVal[V] = 128;
    }
    
    public int quantize(float num, int Type){
	    int numBucket = numBuckets[Type];
	    int min = minVal[Type];
	    int max = maxVal[Type];
	    float stepSize = (Math.abs(min)+Math.abs(max))/numBucket;
	    if (numBuckets[Type] % 2 == 0){
	        return (int) (ceiling(num,Type)-stepSize/2);
	    } else {
	        return (int) (floor(num-stepSize/2,Type));
	    }
    }
    
    private float ceiling(float num, int Type){
        int numBucket = numBuckets[Type];
        int min = minVal[Type];
	    int max = maxVal[Type];
	    float stepSize = (Math.abs(min)+Math.abs(max))/numBucket;
        float ceil = min;
        while(ceil < num){
            ceil += stepSize;
        }
        return ceil;
    }
    private float floor(float num, int Type){
        int numBucket = numBuckets[Type];
        int min = minVal[Type];
	    int max = maxVal[Type];
	    float stepSize = (Math.abs(min)+Math.abs(max))/numBucket;
	    float floor = max;
	    while(floor > num){
	        floor = floor-stepSize;
	    }
        return floor;
    }
    public static int prompt(int Type){
        Scanner scan = new Scanner(System.in);
        String typeName = "Y";
        if(Type == 1) typeName = "U";
        if(Type == 2) typeName = "V";
        System.out.println("How many Buckets do you want for "+typeName);
        System.out.print(">");
        int target = 10;
        try {
            target = scan.nextInt();
        } catch(InputMismatchException ex){
            scan.next();
            target = 10;
        }
        return target;
    }
}
