import java.text.ParseException;
import java.util.*;
import java.io.*;

public class Invert  {

	private final String CACM_FILE = "cacm.all";
	private final String STOPWORD_FILE = "stopwords.txt";

	private Set<String> dictionary;
	private Set<Integer> positions;
	private Map<Integer, Document> documents;
	private Map<String, DocumentFrequency> documentFrequencies;
	private Set<String> stopWords = new TreeSet<String>();
	private FileParser fileParser;
	private DocumentParser docParser;
	private boolean stopwordsOn;
	private static HashMap<String,ArrayList<Posting>> postingsList= new  HashMap<String,ArrayList<Posting>>();
	 
	


	public Invert (boolean stopwordsOn, boolean stemmerOn) throws  FileNotFoundException {
		fileParser = new FileParser();
		docParser = new DocumentParser(stemmerOn);
		dictionary = new TreeSet<String>();
		documentFrequencies = new TreeMap<String, DocumentFrequency>();
		try {
			documents = fileParser.extractDocuments(CACM_FILE);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.stopwordsOn = stopwordsOn;
		if (stopwordsOn) {
			stopWords = createStopWords(STOPWORD_FILE);
		}
	}

	public Set<String> getDictionary() {
		return dictionary;
	}
	public Set<Integer> getPositions() {
		return positions;
	}

	public Map<Integer,Document> getDocuments() {
		return documents;
	}

	public Map<String, DocumentFrequency> getDocumentFrequencies() {
		return documentFrequencies;
	}
	public static void printDict(Map<String,DocumentFrequency> unsorted, PrintWriter file)
	{

		Map<String, DocumentFrequency> map = new TreeMap<String, DocumentFrequency>(unsorted);
		Set<?> set2 = map.entrySet();
		Iterator<?> iterator2 = set2.iterator();
		while (iterator2.hasNext())
		{
			Map.Entry me2 = (Map.Entry) iterator2.next();
			file.print(me2.getKey()+" ");
			//System.out.println(me2.getKey());
			file.println(me2.getValue());

		}
		//System.out.println("Finished Sorting Dictionary");
	}

	public Set<String> createStopWords(String fileName) {
		Set<String> words = new TreeSet<String>();
		try {
			File stopwordFile = new File(fileName);
			BufferedReader reader = new BufferedReader(new FileReader(stopwordFile));			
			for (String word = reader.readLine(); word != null; word = reader.readLine()) {
				words.add(word);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return words;
	}

	public Set<String> getStopWords() {
		return stopWords;
	}

	public void invert() throws  FileNotFoundException,UnsupportedEncodingException{
		for (Integer docID : documents.keySet()) {
			Document doc = documents.get(docID);
			docParser.setDocument(doc);
			for (String term : docParser.findTerms()) {
				if (!stopwordsOn || !stopWords.contains(term)) {	
					dictionary.add(term);
					int tf = docParser.calculateFrequency(term);
					doc.setFrequency(term, tf);
					//positions=docParser.parsePositions(term);
					DocumentFrequency df = documentFrequencies.get(term);
					if (df == null) {
						df = new DocumentFrequency();
						if (tf > 0) {
							df.updateFrequency();
						}
						documentFrequencies.put(term, df);
					}
					else if (tf > 0) {
						df.updateFrequency();
					}
				}
			}
		}
		for (Map.Entry<Integer,Document> entry : documents.entrySet())
		{
			Document doc = entry.getValue();
			docParser.setDocument(doc);

			Set<String> list=doc.getTerms();
			for (String term : list)
			{
				positions= docParser.parsePositions(term);
				ArrayList<Posting> myList = new ArrayList<Posting>();
				if(postingsList.containsKey(term))
				{
					
				myList=postingsList.get(term);
				myList.add(new Posting(doc.getID(),doc.getFrequency(term),positions));
				//System.out.println(myList);
				}
					else
						{
							myList=new ArrayList<Posting>();
							myList.add(new Posting(doc.getID(),doc.getFrequency(term),positions));
							postingsList.put(term,myList);
						}
			}
		}
		PrintWriter writer = new PrintWriter("postings.txt", "UTF-8");
		for (Map.Entry<String,ArrayList<Posting>> kkk  : postingsList.entrySet()) {
			writer.print(kkk.getKey()+" ");
			
			ArrayList<Posting> temp = new ArrayList<Posting>();
			
			temp=kkk.getValue();
			for (Posting post:temp ) {
				writer.print(post.toString());
				
				//System.out.printl(post);
				
			}
			writer.println("");
		}
		writer.close();
		PrintWriter writer2 = new PrintWriter("dict.txt", "UTF-8");
		System.out.println("Number of terms:\t" + dictionary.size());
		System.out.println("Number of documents:\t" + documents.size());
		System.out.println("Created Dictionary and Postings , Ready to search now");
		printDict(documentFrequencies,writer2);
		writer2.close();


	}
	
}
