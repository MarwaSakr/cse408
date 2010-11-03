import java.util.ArrayList;
import java.util.Collections;


public class RunlengthEncoding {
	
	private ArrayList<Integer> arraylist_YUV= new ArrayList<Integer>();
	
	
	public int[] encode(int[] originalYUV) {	
		int[] RLE;
		
		for(int i = 0; i < originalYUV.length; i++) {
			int runlength = 1;
			
			while(i+1 < originalYUV.length && originalYUV[i] == originalYUV[i+1]) {
				runlength++;
				i++;
			}
			//System.out.println("Runlength:"+runlength+" symbol:"+originalYUV[i]);	
			arraylist_YUV.add(runlength);
			arraylist_YUV.add(originalYUV[i]);		
		}
		
		RLE = new int[arraylist_YUV.size()];
		//System.out.println("arrraylist size: " + arraylist_YUV.size());
		
		for(int i = 0; i < arraylist_YUV.size(); i++) {
			RLE[i] = arraylist_YUV.get(i);
		}
		
		return RLE;
	}
	
	public int[] decode(int[] encodedYUV, int originalLength) {
		int[] decoded = new int[originalLength];
		int index = 0;
		
		for(int i = 0; i< encodedYUV.length; i++) {
			int freq = encodedYUV[i];
			while(freq > 0){
				decoded[index] = encodedYUV[i+1];
				index++;
				freq--;
			}
			i++;
		}
		
		return decoded;
	}
}
