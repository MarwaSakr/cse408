public class QuantizeScheme {
    private float stepSizeY;
    private float stepSizeU;
    private float stepSizeV;
    
    public static final int Y=0;
    public static final int U=1;
    public static final int V=2;
    
    public QuantizeScheme(float stepSizeY, float stepSizeU, float stepSizeV){
        this.stepSizeY = stepSizeY;
        this.stepSizeU = stepSizeU;
        this.stepSizeV = stepSizeV;
    }
    
    public float quantize(float num, int Type){
        
        
        return num;
    }
}
