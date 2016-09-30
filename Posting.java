
import java.util.*;

public class Posting
{
	
		int docID;
		int docFreq;
		Set<Integer> positions;

	public Posting(int docID,int docFreq,Set<Integer> positions)
	{
		this.docID=docID;
		this.docFreq=docFreq;
		this.positions=positions;

	}



	public int getID(){
		return  docID;

	}
	public int getFreq(){

		return docFreq;
	}
	public String toString()
	{
		String result="";
			for (Integer s :positions ) {
					result+=s.toString()+" ";
			}
	 return  Integer.toString(docID) +  " " +Integer.toString(docFreq) + " "+ result;
	}
}