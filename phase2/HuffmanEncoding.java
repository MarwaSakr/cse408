import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;


public class HuffmanEncoding {	
	
	ArrayList<Record> huffman;
		
	int index = 0;
	
	public HuffmanEncoding(ArrayList<Record> huffman) {
		this.huffman = huffman;
	}
	
	public ArrayList<Record> buildTree() {
		
		Record leftChild = new Record();
		Record rightChild = new Record();
		Record parent = new Record();
		
		while(huffman.size() > 1) {
			rightChild = huffman.get(huffman.size() - 1);
			leftChild = huffman.get(huffman.size() - 2);
			parent = new Record("P" + String.valueOf(index), rightChild.getFrequency() + leftChild.getFrequency());
			parent.left = leftChild;
			parent.right = rightChild;
			index++;
			huffman.add(parent);
			Collections.sort(huffman);
			huffman.remove(huffman.size() - 1);
			huffman.remove(huffman.size() - 1);
			
		}
		return huffman;
	}
	
	public void traverse(Record huffmanRecord, String s, Hashtable codeTable) {
				
		if(!huffmanRecord.isLeaf()) {
			traverse(huffmanRecord.left, s + "0", codeTable);
			traverse(huffmanRecord.right, s + "1", codeTable);
			
		} else {
			codeTable.put(huffmanRecord.getValue(), s);
		}		
	}	
	
	public String[] encode(int[] original, Hashtable codeTable) {
		
		String[] encodedPixelArray = new String[original.length];
		Enumeration e;
		
		for(int i = 0; i < original.length; i++) {
			e = codeTable.keys();
			while(e.hasMoreElements()){
				String value = (String) e.nextElement();
				String code = (String) codeTable.get(value);
				if(Integer.valueOf(value) == original[i]){
					encodedPixelArray[i] = code;
				}
			}
		}
		
		return encodedPixelArray;
	}
	
	public int[] decode(String[] encoded, Hashtable codeTable) {
		int[] decodedPixelArray = new int[encoded.length];
		Enumeration e;
		
		for(int i = 0; i < encoded.length; i++) {
			e = codeTable.keys();
			while(e.hasMoreElements()){
				String value = (String) e.nextElement();
				String code = (String) codeTable.get(value);
				if(code.equals(encoded[i])){
					decodedPixelArray[i] = Integer.valueOf(value);
				}
			}
		}
		return decodedPixelArray;
	}
	
	public int calculateTotalBits(ArrayList<Record> YUV, Hashtable codeTable) {
		int totalNumberBits = 0;
		int bitLength = 0;
		int count = 0;
		String symbol = "";
		
		for(int i = 0; i < YUV.size(); i++) {
			symbol = YUV.get(i).getValue();
			if(codeTable.containsKey(symbol)) {
				count = YUV.get(i).getFrequency();
				bitLength = codeTable.get(symbol).toString().length();
				totalNumberBits += (bitLength * count);
			}			
		}
		
		return totalNumberBits;
	}
}
