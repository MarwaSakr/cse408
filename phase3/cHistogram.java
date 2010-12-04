
import java.util.ArrayList;



class pair{
    int min[] = new int[3];
    int max[] = new int[3];

    pair(int[] minVal, int[] maxVal)
    {
        min = minVal;
        max = maxVal;
    }

}
public class cHistogram {
    protected int numBins;
    //public static pair[] bins;
    protected static String colorspace;
    public static ArrayList bin = new ArrayList();
    
    public cHistogram(int bins, String cs)
    {
        this.numBins = bins;
        colorspace = cs;
        //this.bins = new pair[numBins];
    }

    public static void addBin(int[] minVals, int[] maxVals)
    {
        bin.add(new pair(minVals, maxVals));
    }
    
    //public static void addBin(int binNumber, int[] minVals, int[] maxVals)
    //{
        //bins[binNumber] = new pair(minVals, maxVals);
    //}

    public static void printBin(int binNumber)
    {
        /*System.out.println("Bin Number: " + (binNumber+1));
        System.out.println(colorspace.charAt(0) + ": ");
        System.out.println("[" + bins[binNumber].min[0] + ", " +
                            bins[binNumber].max[0] + "]");

        System.out.println(colorspace.charAt(1) + ": ");
        System.out.println("[" + bins[binNumber].min[1] + ", " +
                            bins[binNumber].max[1] + "]");

        System.out.println(colorspace.charAt(2) + ": ");
        System.out.println("[" + bins[binNumber].min[2] + ", " +
                            bins[binNumber].max[2] + "]" + "\n");*/


        System.out.println("Bin Number: " + (binNumber+1));
        System.out.println(colorspace.charAt(0) + ": ");
        System.out.println("[" + ((pair) bin.get(binNumber)).min[0] + ", " +
                            ((pair) bin.get(binNumber)).max[0] + "]");

        System.out.println(colorspace.charAt(1) + ": ");
        System.out.println("[" + ((pair) bin.get(binNumber)).min[1] + ", " +
                            ((pair) bin.get(binNumber)).max[1] + "]");

        System.out.println(colorspace.charAt(2) + ": ");
        System.out.println("[" + ((pair) bin.get(binNumber)).min[2] + ", " +
                            ((pair) bin.get(binNumber)).max[2] + "]" + "\n");

    }


}
