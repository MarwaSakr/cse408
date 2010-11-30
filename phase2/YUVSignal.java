
import java.io.Serializable;
import java.util.Hashtable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * This is the structure which holds the flags and data for encoding/decoding a YUV file.
 */

/**
 *
 * @author jvmilazz
 */
public class YUVSignal implements Serializable {
    public int Yorg[];
    public int Uorg[];
    public int Vorg[];
    public int Ynew[];
    public int Unew[];
    public int Vnew[];
    public float Yquant[];
    public float Uquant[];
    public float Vquant[];
    public String Ynewencoded[];
    public String Unewencoded[];
    public String Vnewencoded[];
    protected int height = 0;
    protected int width = 0;
    protected int orgBits = 0;
    protected int newBits = 0;
    static final long serialVersionUID= 2404425284902146062L; //-1361412722817258113

    // Encoding Data
    public int Yerr[];
    public int Uerr[];
    public int Verr[];
    
    //HashTables
    public Hashtable YHash;
    public Hashtable UHash;
    public Hashtable VHash;


    // Additional flags... (use enum possibly?)
    protected int predictiveCodingFlag = 1; // 1 = No PC, 2 = Opt 2, 3 = Opt 3, ...., 6 = opt 6
    protected int quantizationFlag = 1; // 1 = No quantization, 2 = Uniform quant
    protected int encodingFlag = 1; // 1 = No encoding, 2 = run-length, 3 = shannon-fano, 4 = huffman


    public YUVSignal(int width, int height)
    {
        this.height = height;
        this.width = width;
        this.Yorg = new int[height*width];
        this.Uorg = new int[height*width];
        this.Vorg = new int[height*width];
        this.Ynew = new int[height*width];
        this.Unew = new int[height*width];
        this.Vnew = new int[height*width];
        this.Yerr = new int[height*width];
        this.Uerr = new int[height*width];
        this.Verr = new int[height*width];
        this.Yquant = new float[height*width];
        this.Uquant = new float[height*width];
        this.Vquant = new float[height*width];
    }

    public void setSizeOrg(int size)
    {
        this.orgBits = size;
    }

    public void setSizeNew(int size)
    {
        this.newBits = size;
    }


}
