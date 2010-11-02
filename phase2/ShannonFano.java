import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

public class ShannonFano{
        
    
    public ShannonFano(String filename,String destination,boolean compress,boolean verbose){        	
	if(compress)
	    {if(verbose)
	         System.out.println("compression started, source : "+filename);
	     HashMap<String,Integer> occurenceTable=new HashMap<String,Integer>();
	     int total=0;
	     try{if(verbose)
	             System.out.println("counting occurences...");
	         BufferedReader in=new BufferedReader(new FileReader(filename));		    
		 String s=null,c=null;
		 char tab[]=new char[1];
		 int i,lineCount=0;
		 while((s=in.readLine())!=null)
		     {for(i=0;i<s.length();i++)
	        	  {tab[0]=s.charAt(i);
			   c=new String(tab);
			   if(occurenceTable.containsKey(c))
			       occurenceTable.put(c,new Integer(occurenceTable.get(c).intValue()+1));
			   else
			       occurenceTable.put(c,new Integer(1));
			  }
		      total+=s.length()+1;
		      //count each newline too
		      lineCount++;
		     }
		  in.close();
		  //put ("newline",...) in occurenceTable
		  occurenceTable.put("newline",new Integer(lineCount));
		  if(verbose)
	              {System.out.println(lineCount+" line(s) in the file : "+filename);
		       System.out.println("occurence table");
		       for(Map.Entry<String,Integer> m:occurenceTable.entrySet())
			   System.out.println(m.getKey()+" "+m.getValue());
		      }
		 }
	     catch(IOException ioe)
	     {ioe.printStackTrace();
	      return;
	     }
	     if(verbose)
	         System.out.println("computing frequency table...");
	     LinkedHashMap<String,Float> tmpfrequencyTable=new LinkedHashMap<String,Float>();	
	     for(String tmp:occurenceTable.keySet())
		 tmpfrequencyTable.put(tmp,new Float(((float)occurenceTable.get(tmp).intValue())/total));	
	     Vector<Float> l=new Vector<Float>();
	     l.addAll(tmpfrequencyTable.values());
	     Collections.sort(l,Collections.reverseOrder());
	     LinkedHashMap<String,Float> frequencyTable=new LinkedHashMap<String,Float>();
	     for(Float value:l)
		 for(String key:tmpfrequencyTable.keySet())	         
	             if(tmpfrequencyTable.get(key).equals(value))
	        	 {frequencyTable.put(key,value);
			  tmpfrequencyTable.remove(key);
			  break;
			 }
	     if(verbose)
		 {System.out.println("frequency table");
		  for(Map.Entry<String,Float> m:frequencyTable.entrySet())
		      System.out.println(m.getKey()+" "+m.getValue());
		 }
	     if(verbose)
	         System.out.println("computing code table...");
	     LinkedHashMap<String,StringBuffer> codeTable=new LinkedHashMap<String,StringBuffer>();
	     for(String tmp:frequencyTable.keySet())
		 codeTable.put(tmp,new StringBuffer(""));		
	     updateTables(frequencyTable,codeTable);    
	     if(verbose)
		 {System.out.println("code table");
		  for(Map.Entry<String,StringBuffer> m:codeTable.entrySet())
		      System.out.println(m.getKey()+" "+m.getValue());
		 }
	     File f;
	     if(destination==null)
	         f=new File(filename+".zsf");
	     else
	         f=new File(destination);
	     if(verbose)
		 System.out.println("destination file : "+f.getName());	     
	     try {if(verbose)
	              System.out.println("computing bit set from source file by using code table (main compression step)...");
	          BufferedReader in=new BufferedReader(new FileReader(filename));
		  String s=null,c=null;
		  char tab[]=new char[1];
		  int i=0,j=0,k=0;		  
		  BitSet content=new BitSet();		  
		  String value=null,newlineValue=new String(codeTable.get("newline"));
		  while((s=in.readLine())!=null)
	              {for(i=0;i<s.length();i++)
	        	  {tab[0]=s.charAt(i);
			   c=new String(tab);
			   value=new String(codeTable.get(c));
			   for(j=0;j<value.length();j++,k++)
		               if(value.charAt(j)=='1')
				   content.set(k,true);
			  }
		       for(j=0;j<newlineValue.length();j++,k++)
		           if(newlineValue.charAt(j)=='1')
			       content.set(k,true);		       
		      }
		  content.set(k,true);
		  in.close();		  
		  if(f.createNewFile() && verbose)
	              System.out.println("file "+f.getName()+" created");
		  if(verbose)
		      System.out.println("writing code table...");
		  PrintWriter out=new PrintWriter(new BufferedWriter(new FileWriter(f)));
		  out.println(codeTable.size());
		  for(Map.Entry<String,StringBuffer> m:codeTable.entrySet())
		      out.println(m.getKey()+" "+m.getValue());		  
		  byte[] byteContent=convertBitSetInBytes(content);
		  if(verbose)
		      System.out.println("writing size of compressed content : "+byteContent.length+" bytes");
		  out.println(byteContent.length);
		  out.close();
		  if(verbose)
		      System.out.println("writing compressed content...");
		  BufferedOutputStream out2=new BufferedOutputStream(new FileOutputStream(f,true));
		  out2.write(byteContent);		  
		  out2.close();
		  if(verbose)
		      System.out.println("compression successful");
		 }
	     catch(IOException ioe)
	     {ioe.printStackTrace();
	      return;
	     }
	    }
	else
	    {if(verbose)
		 System.out.println("decompression started, source : "+filename);
	     int lineCount,byteCount;	     
	     LinkedHashMap<String,String> codeTable=new LinkedHashMap<String,String>();
	     try{if(verbose)
		     System.out.println("loading code table...");
	         BufferedReader in=new BufferedReader(new FileReader(filename));		    
		 lineCount=Integer.parseInt(in.readLine());
		 String s=null;
		 char tab[]=new char[1];
		 for(int j=0;j<lineCount && (s=in.readLine())!=null;j++)		     
	             {tab[0]=s.charAt(0);
		      if(s.startsWith("newline"))
		          codeTable.put(new String(s.substring(8,s.length())),"newline");
		      else
		          codeTable.put(new String(s.substring(2,s.length())),new String(tab));
		     }
		 if(verbose)
		     {System.out.println("code table");
	              for(Map.Entry<String,String> m:codeTable.entrySet())
			  System.out.println(m.getKey()+" "+m.getValue());
		     }
		 byteCount=Integer.parseInt(in.readLine());
		 if(verbose)
		      System.out.println("reading size of compressed content : "+byteCount+" bytes");		    
		 in.close();
		 BufferedInputStream in2=new BufferedInputStream(new FileInputStream(filename));
		 byte[] byteContent=new byte[byteCount];
		 in2.skip(new File(filename).length()-byteCount);
		 if(verbose)
		      System.out.println("reading compressed content...");
		 in2.read(byteContent);
		 in2.close();
		 File f;
		 if(destination==null)
		     f=new File(filename.substring(0,filename.length()-4));
		 else
		     f=new File(destination);
		 if(f.createNewFile() && verbose)
	             System.out.println("file "+f.getName()+" created");		 
		 PrintWriter out=new PrintWriter(new BufferedWriter(new FileWriter(f)));
		 String tmp;
		 if(verbose)
		     System.out.println("computing bit set from compressed content...");
		 BitSet bitset=convertBytesInBitSet(byteContent);
		 if(verbose)
		     System.out.println("writing decompressed data thanks to bit set and code table...");
		 for(int i=0,j=1;i<bitset.length()-2 && j<bitset.length()-1;)
		     if((tmp=codeTable.get(toBitString(bitset.get(i,j+1),j-i+1)))!=null)
		         {if(tmp.equals("newline"))
			      out.println("");
			  else
			      out.print(tmp);
			  i+=j-i+1;
			  j=i+1;
			 }
		     else
		         j++;	
		 out.close();
		 if(verbose)
		      System.out.println("decompression successful");
		}
	     catch(IOException ioe)
	     {ioe.printStackTrace();
	      return;
	     }
	    }	
    }
    
    private static void updateTables(LinkedHashMap<String,Float> frequencyTablePart,
                                     LinkedHashMap<String,StringBuffer> codeTablePart){
	float leftSum=0,rightSum=0;
	int limit=0;
	for(Float val:frequencyTablePart.values())
	    rightSum+=val.floatValue();
	for(Float x:frequencyTablePart.values())
	    {leftSum+=x;
	     rightSum-=x;
	     limit++;
	     if(leftSum>=rightSum)
	       	 break;	     
	    }
	int j=0;
	for(Map.Entry<String,StringBuffer> m:codeTablePart.entrySet())
	    {if(j<limit)
	         codeTablePart.put(m.getKey(),m.getValue().append("0"));
             else
	         codeTablePart.put(m.getKey(),m.getValue().append("1"));
	     j++;
	    }	
	LinkedHashMap<String,Float> nextFrequencyTablePart;
	LinkedHashMap<String,StringBuffer> nextCodeTablePart;
	Iterator<String> frequencyKeySetIterator;
	Iterator<Float> frequencyValuesIterator;
	Iterator<String> codeKeySetIterator;
	Iterator<StringBuffer> codeValuesIterator;
	if(limit>1)
	    {frequencyKeySetIterator=frequencyTablePart.keySet().iterator();
	     frequencyValuesIterator=frequencyTablePart.values().iterator();
	     codeKeySetIterator=codeTablePart.keySet().iterator();
	     codeValuesIterator=codeTablePart.values().iterator();
	     nextFrequencyTablePart=new LinkedHashMap<String,Float>();	     
	     for(int i=0;i<limit;i++)
	         nextFrequencyTablePart.put(frequencyKeySetIterator.next(),frequencyValuesIterator.next());
	     nextCodeTablePart=new LinkedHashMap<String,StringBuffer>();
	     for(int i=0;i<limit;i++)
	         nextCodeTablePart.put(codeKeySetIterator.next(),codeValuesIterator.next());
	     updateTables(nextFrequencyTablePart,nextCodeTablePart);
	    }
	if(limit<codeTablePart.size()-1)
	    {frequencyKeySetIterator=frequencyTablePart.keySet().iterator();
	     frequencyValuesIterator=frequencyTablePart.values().iterator();
	     codeKeySetIterator=codeTablePart.keySet().iterator();
	     codeValuesIterator=codeTablePart.values().iterator();
	     for(int i=0;i<limit;i++)
	         {frequencyKeySetIterator.next();
		  frequencyValuesIterator.next();
		  codeKeySetIterator.next();
		  codeValuesIterator.next();
		 }
	     nextFrequencyTablePart=new LinkedHashMap<String,Float>();	     
	     while(frequencyKeySetIterator.hasNext())
	         nextFrequencyTablePart.put(frequencyKeySetIterator.next(),frequencyValuesIterator.next());	       
	     nextCodeTablePart=new LinkedHashMap<String,StringBuffer>();
	     while(codeKeySetIterator.hasNext())
	         nextCodeTablePart.put(codeKeySetIterator.next(),codeValuesIterator.next());	     	     
	     updateTables(nextFrequencyTablePart,nextCodeTablePart);
	    }
    }
    
    public static byte[] convertBitSetInBytes(BitSet bitSet){
        byte[] byteContent=new byte[(int)Math.ceil(bitSet.length()/8.0f)];	
	int i;
	for(i=0;i<byteContent.length;i++)
	    byteContent[i]=(byte)0x0000;
	for(i=0;i<bitSet.size();i++)
	    if(bitSet.get(i))
		byteContent[i/8]|=1<<(7-(i%8));
	return(byteContent);
    }
    
    public static BitSet convertBytesInBitSet(byte[] array){        
	BitSet bitset=new BitSet();
	int j;
	byte[] mask={(byte)0x01,(byte)0x02,(byte)0x04,(byte)0x08,(byte)0x10,(byte)0x20,(byte)0x40,(byte)0x80};
	for(int i=0;i<array.length;i++)
	    for(j=0;j<8;j++)
	        if((array[i] & mask[j])!=0)
		    bitset.set(i*8+(7-j),true);		 		    
	return(bitset);
    }
    
    public static String toBitString(BitSet bitset,int length){
        String s=new String("");
	for(int i=0;i<length;i++)
	    if(bitset.get(i))
	        s+="1";
            else
	        s+="0";
	return(s);
    }          
    
    public static void main(String[] args){
        if(args.length!=2 && args.length!=3)
	    {System.out.println("usage: java ShannonFano option filename [destination_filename]");
	     return;
	    }	
	String tmp=args[0].replaceAll("[czv]","");
	if(tmp.length()!=0)
	    {System.out.println("unknown option(s) "+tmp);
	     return;
	    }
	boolean compress=args[0].contains("c");
	boolean decompress=args[0].contains("z");
	boolean verbose=args[0].contains("v");	
	if(compress==decompress)
	    {if(verbose)
	         tmp=args[0].replaceAll("v","");
	     else
	         tmp=args[0];
	     if(compress)
	         System.out.println("invalid options "+tmp+", cannot compress and decompress at the same time");
	     else
	         System.out.println("invalid option v (used alone), nothing to do");
	     return;
	    }
	if(!(new File(args[1])).exists())
	    {System.out.println("file "+args[1]+" not found");
	     return;
	    }
	if(compress)
	    {if(args.length==3 && !args[2].endsWith(".zsf"))
	         {System.out.println("invalid file format for the destination file "+args[2]);
		  return;
		 }
	    }
	else
	    if(!args[1].endsWith(".zsf"))
	        {System.out.println("invalid file format for the source file "+args[1]);
		 return;
		}	
	new ShannonFano(args[1],args.length==3?args[2]:null,compress,verbose);	   
    }

}
