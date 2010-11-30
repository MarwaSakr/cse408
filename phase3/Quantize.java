/*import java.lang.Math;
import java.util.Scanner;
import java.util.InputMismatchException;

 //
 // What this class SHOULD do is go through every image in inputImages
 // and find the appropriate color boxes that give the best representation
 // of color in all the images. There should be numBins of them. This should
 // not modify the actual images themselves, but just analyze them. The output
 // should just be the boundaries of the bins and will be written to a file.
 //
public class Quantize {
    private int[] numBuckets;
    private static int[] minVal;
    private static int[] maxVal;


    public static final int Y=0;
    public static final int U=1;
    public static final int V=2;


    public Quantize(int bucketY, int bucketU, int bucketV){
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
    
    //http://www.drdobbs.com/184409309;jsessionid=KZBAXTUUU33JPQE1GHPCKHWATMY32JVN?pgno=24
    public Quantize(int numBins, double size)
    {
        numBuckets = new int[numBins];
        minVal = new int[numBins];
        maxVal = new int[numBins];

        int ratio = (int) Math.floor(size/numBins);

        minVal[0] = 0;
        for(int i = 0; i < numBuckets.length; i++)
        {
            //get height of image (size)
            // ratio = floor( height/numBins)
            // so every bin will get ratio number of pixels and the last bin will get
            // the rest of the remaining pixels
            numBuckets[i] = ratio;
            minVal[i] = ratio*i + 1;
            maxVal[i] = ratio*i + ratio;
        }
    }

    public Quantize(){
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
}*/

import java.lang.Math;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Quantize {
    private int[] numBuckets;
    private static int[] minVal;
    private static int[] maxVal;


    public static final int Y=0;
    public static final int U=1;
    public static final int V=2;


    public Quantize(int bucketY, int bucketU, int bucketV){
        numBuckets = new int[3];
        numBuckets[Y] = bucketY;
        numBuckets[U] = bucketU;
        numBuckets[V] = bucketV;
        minVal = new int[3];
        maxVal = new int[3];

        // This is all wrong! I need to dynamically find the min/maxVals based on
        // color values in pixels.
        /*minVal[Y] = 0;
        maxVal[Y] = 255;
        minVal[U] = -128;
        maxVal[U] = 128;
        minVal[V] = -128;
        maxVal[V] = 128;*/
    }
    public Quantize(){
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

    public int[] getMinVal()
    {
        return minVal;
    }

    public int[] getMaxVal()
    {
        return maxVal;
    }

}