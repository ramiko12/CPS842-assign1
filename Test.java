import java.util.*;
import java.io.*;
public class Test{
	

	private static Set<String> stopwords;
	private static Set<String> dict;
	private static Set<Integer> positions;
	private static Map<Integer, Document> documents;
	private static Map<String,DocumentFrequency> documentFrequencies;
	private static DocumentParser docParser;
	
	private static boolean stemmer;
	private static boolean stop;
	static boolean found ;
	private static ArrayList<Long> times= new ArrayList<Long>();

	public static String myStemStr(String word)
	{
		Stemmer stem = new Stemmer();
		String stemmed = null;
		String thisWord = null;
		char[] wordArr = word.toCharArray();
		for (int i = 0; i < wordArr.length; i++)
		{
			stem.add(wordArr[i]);
		}
		stem.stem();
		thisWord = stem.toString();
		stemmed = thisWord;

		return stemmed;
	}

public static void main(String[] args) throws  FileNotFoundException,UnsupportedEncodingException {
	

		System.out.println("Welcome to the search , would you like stemming y/n");

		

		
		
		long diff=0;
		File fileP = new File ("postings.txt");
		File fileD = new File ("dict.txt");
		File bigFile = new File ("cacm.all");
		Scanner scan= new Scanner(System.in);

		if(scan.next().equalsIgnoreCase("y"))
		{
			stemmer=true;

		}
		System.out.println("Welcome to the search , would you like stopwords to be removed y/n");
		if(scan.next().equalsIgnoreCase("y"))
		{
			stop=true;

		}

		Invert invert = new Invert(stop,stemmer);
		System.out.println("BUILDING FILES PLEAAASE WAIT");
		invert.invert();
		System.out.println("Enter a term!!");

		String line ="";
		Scanner scanner = new Scanner(System.in);
		long startTimeSlow ;
		String input = scanner.next().toLowerCase();
		String unstemmed=input;
		
		try{
		
				
		
		while(!input.equalsIgnoreCase("ZZEND"))
		 {
		 	startTimeSlow=System.nanoTime();
		 	diff=0;
		 				if(stemmer)
		 				{
							input=myStemStr(input);
						}
		 	Scanner fileReader = new Scanner(fileD);
		int df=0;
		while(fileReader.hasNext())
		{
		 line = fileReader.next();
			if(line.equalsIgnoreCase(input))
			{
				df=fileReader.nextInt();
				System.out.println("DocFreq:"+df);
				found=true;
				
			}
			//System.out.println(found);
			

	    }
	  // done dict
		int lineInt;
		fileReader.close();
		 fileReader = new Scanner(fileP);
		 //if(!found){System.out.println("the term you entered was not found please try another term");}

		 

		 while(fileReader.hasNextLine())
		 {
		 		 line=fileReader.next();
		 		 if(line.equals(input))
		 		 {
		 		 	for(int j=0;j<df;j++)
		 		 	{

		 		 	lineInt=fileReader.nextInt();
		 		 	System.out.println(" ");
		 		 	System.out.println("DOC ID----->" +lineInt);


		 			 Scanner bigReader = new Scanner(bigFile);
		 		 			while(bigReader.hasNextLine())
		 		 		{
		 		  				line=bigReader.nextLine();
		 		  				String id=".I "+lineInt;
		 		  				if(line.equals(id))
		 		  				{
		 		  					String title="";
		 		  					line=bigReader.nextLine();
		 		  					line=bigReader.nextLine();
		 		  					while(!line.startsWith(".W") && !line.equals(".B"))
		 		  					{
		 		  						title+=line;
		 		  						line=bigReader.nextLine();
		 		  					}
		 		  						System.out.println("The title is----->"+title);
		 		  					if(line.equals(".W"))
		 		  					{
		 		  					line=bigReader.nextLine();
		 		  					while(!line.equals(".B"))
		 		  					{
		 		  					//System.out.println("test");
		 		  					if(line.toLowerCase().contains(input)||line.toLowerCase().contains(unstemmed))
		 		  					{
		 								System.out.println("Summary of doc----->>>"+line);
		 		  						break;
		 		  					}
		 		  			line=bigReader.nextLine();
		 		  		}
		 		  	}
		 		  	else
		 		  	{
		 		  		System.out.println("no content");
		 		  	}
		 		  }
		 		  



		 		 }
		 		 
					 int dfs=fileReader.nextInt();
		 		 	System.out.println("Term Freq:"+dfs);

		 		 	for (int i=0;i<dfs ;i++ )
		 		 	 {
		 		 		lineInt=fileReader.nextInt();
		 		 		System.out.print("Position:"+lineInt+" ");

		 		 	 }

		 		 	}
		 		 	break;
		 		 }
		 		 	else
				{
					if(fileReader.hasNextLine())
					line=fileReader.nextLine();
				    else
				    	
				    	break;
				}

		 }
		  			diff = System.nanoTime() - startTimeSlow;
		  			times.add(diff);
				    System.out.println("Search Time for term is:"+diff/1000000000+" seconds");
		 			System.out.println(" ");
		 			if(!found)
		 				{
		 					System.out.println("the term you entered was not found please try another term");
		 				}
					System.out.println("Do you want to keep searching ? , Enter another term  OR type ZZEND to quit!");
		 			input=scanner.next().toLowerCase();
		 			unstemmed=input;
		 			found=false;
		 }
			
		 //done posting


		 	}
		 	catch(Exception e){}
		 	long est=0;
		 		for (long l : times )
		 		{
		 			 est+=l;
		 		}
		 		System.out.println("Average Time is: "+(est/times.size())/1000000000+" seconds");

		}


}