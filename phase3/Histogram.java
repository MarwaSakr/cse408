/*
 * @description A description of all colors in a set of images.
 *
 * @author Joseph Milazzo
 */

public class Histogram {
    private int numBins;
    private int minVal;
    private int maxVal;

    public Histogram(int numBins, int minVal, int maxVal)
    {
        this.numBins = numBins;
        this.minVal = minVal;
        this.maxVal = maxVal;
    }

    public void printDescription()
    {
        System.out.println("[" + minVal + "..." + maxVal + "]");
    }
}
