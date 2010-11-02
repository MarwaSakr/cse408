import java.util.ArrayList;


public class EncodedPixel {
	byte symbol;
	int count;
	boolean bit;
	ArrayList<Boolean> code = new ArrayList<Boolean>();
	
	EncodedPixel lChild = null;
	EncodedPixel rChild = null;
	
	public EncodedPixel(byte val, int freq){
		symbol = val;
		count = freq;
	}
	
	public EncodedPixel(byte val, int freq, ArrayList<Boolean> newCode, EncodedPixel child1, EncodedPixel child2){
		symbol = val;
		count = freq;
		lChild = child1;
		rChild = child2;
		for(int i=0; i<newCode.size(); i++){
			code.add(newCode.get(i));
		}
	}
	
	public EncodedPixel(byte val, boolean ibit){
		symbol = val;
		code.add(new Boolean(ibit));
	}
	
	public void setBitsRecursively(boolean bool){
		code.add(new Boolean(bool));
		bit = bool;
		if(lChild != null)
			lChild.setBitsRecursively(bool);
		if(rChild != null)
			rChild.setBitsRecursively(bool);
			
	}
	
	public EncodedPixel clone(){
		EncodedPixel clonedPixel = new EncodedPixel(symbol, count);
		clonedPixel.bit = bit;
		for(int i=0; i<code.size(); i++){
			clonedPixel.code.add(new Boolean(code.get(i).booleanValue()));
		}
		clonedPixel.lChild = lChild;
		clonedPixel.rChild = rChild;
		
		return clonedPixel;
	}
}	
