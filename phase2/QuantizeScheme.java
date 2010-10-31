import java.lang.Math;

public class QuantizeScheme {
    private float[] stepSize;
    
    public static final int Y=0;
    public static final int U=1;
    public static final int V=2;
    
    public QuantizeScheme(float stepSizeY, float stepSizeU, float stepSizeV){
        stepSize = new float[3];
        stepSize[Y] = stepSizeY;
        stepSize[U] = stepSizeU;
        stepSize[V] = stepSizeV;
    }
    public QuantizeScheme(){
        stepSize = new float[3];
        stepSize[Y] = 1;
        stepSize[U] = 1;
        stepSize[V] = 1;
    }
    
    public float quantize(float num, int Type){
        return (float) Math.ceil(num) - stepSize[Type]/2;
    }
}
