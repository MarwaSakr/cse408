
import java.util.ArrayList;
import java.util.Collections;

public class LineFinder {

    public static LineFeature[] find(TextureFeature[] textures, int numBins, String fileName){

        //For Each grid locale find params of 5 major lines using texture Feature as input
        double rMin;
        double rMax;
        double thetaMin = 0;
        double thetaMax = 6.28;
        //calc rmin & rmax
        for(TextureFeature texture: textures){
            for(int i=0; i<numBins; i++){
                double theta = thetaMin + i*(Math.abs(thetaMax)+Math.abs(thetaMin))/numBins;
                double r = texture.x*Math.cos(theta)+texture.y*Math.sin(theta);
                rMin = rMin<r ? rMin : r;
                rMax = rMax>r ? rMax : r;
            }
        }
        double thetaStep = (Math.abs(thetaMax)+Math.abs(thetaMin))/numBins;
        double rStep = (Math.abs(rMax)+Math.abs(rMin))/numBins;
        int[][] accumulator = new int[numBins][numBins];

        for(TextureFeature texture : textures) {
            for(int i = 0; i<numBins; i++){
                double theta = thetaMin + i*thetaStep;
                double r = texture.x*Math.cos(theta)+texture.y*Math.sin(theta);
                int indexR = 0;
                while (rMin+indexR*rStep < r && indexR < numBins-1){
                    indexR++;
                }
                accumulator[i][indexR]++;
                System.out.println("(X:"+texture.x+",Y:"+texture.y+"Theta:"+theta+"R:"+r+")");
            }
        }
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i<numBins; i++){
            for(int j = 0; j<numBins; j++){
                //Check Neighbors
                for(int x = -1; x<=1; x++){
                    if ( i+x < numBins && i+x >= 0){
                        for(int y = -1; y<=1; y++){
                            if(j+y < numBins && j+y >= 0){
                                if(accumulator[i+x][j+y] > accumulator[i][j])
                                    accumulator[i][j]=0;
                            }
                        }
                    }
                }
                list.add(accumulator[i][j]);
            }
        }
        ArrayList<LineFeature> lines = new ArrayList<LineFeature>();
        Collections.sort(list);
        for (int i : list){
            for(int theta= 0; theta<numBins; theta++){
                for(int r = 0; r<numBins;r++){
                    if(accumulator[theta][r] == i){
                        lines.add(new LineFeature(thetaMin+theta*thetaStep,rMin+r*rStep,fileName));
                    }
                }
            }
        }
        return (list.subList(0, 5).toArray());
    }
}
