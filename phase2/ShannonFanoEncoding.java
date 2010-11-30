import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;


public class ShannonFanoEncoding {

	public int findSplit(ArrayList<Record> arraylist, int first, int last) {
		int length = last - first;

		int temp = 0;

		for(int i = first; i < last; i++) {
			temp += arraylist.get(i).getFrequency();
		}
		temp = temp/length;

		int split = -1;

		for(int i =first; i < last; i++) {
			if(arraylist.get(i).getFrequency() > temp) {
				split = i;
			}
		}

		if(split == -1) {
			split = length/2;
		}
		//System.out.println("Temp: " + temp);
		//System.out.println("Split: " + split);

		return split;

	}

	public void buildCode(ArrayList<Record> yuv, int total, int first, int last) {

		int size = 0;
		int newLast = 0;
		if(first < last - 1) {
			for(int i = first; i < last; i++) {
				//int split = findSplit(yuv, first, last);
				if(size < total/2) {
					newLast = i;
					size += yuv.get(i).getFrequency();
					yuv.get(i).coding += '0';
				} else {
					yuv.get(i).coding += '1';
				}
			}
			newLast++;
			buildCode(yuv, size, first, newLast);
			buildCode(yuv, total - size, newLast, last);
		}
	}

	public Hashtable buildCodeTable(ArrayList<Record> yuv, Hashtable codeTable) {

		for(int i = 0; i < yuv.size(); i++) {
			codeTable.put(yuv.get(i).getValue(), yuv.get(i).coding);
		}
		return codeTable;

	}

	public String[] encode(int[] original, Hashtable codeTable) {
		String[] encodedPixelArray = new String[original.length];
		Enumeration e;

		for(int i = 0; i < original.length; i++) {
			e = codeTable.keys();
			while(e.hasMoreElements()){
				String value =(String) e.nextElement();
				String code = (String) codeTable.get(value);
				if(Integer.valueOf(value) == original[i]){
					encodedPixelArray[i] = code;
				}
			}
		}

		return encodedPixelArray;

		//YUV.setEncodedArray(encodedPixelArray);
	}

	public static int[] decode(String[] encoded, Hashtable codeTable) {
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

	public int calculateTotalBits(ArrayList<Record> YUV) {
		int totalNumberBits = 0;
		int bitLength = 0;
		int count = 0;

		for(int i = 0; i < YUV.size(); i++) {
			bitLength = YUV.get(i).coding.length();
			count = YUV.get(i).getFrequency();
			totalNumberBits += (bitLength * count);
		}

		return totalNumberBits;

	}

}
