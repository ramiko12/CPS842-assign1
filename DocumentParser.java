import java.util.*;


public class DocumentParser {

	private final String WHITESPACE = "\\W+";
	private final String CHARACTER = "[a-zA-Z]+";
	private Document doc;
	private boolean stemmerOn;

	public DocumentParser() {

	}

	public DocumentParser(boolean stemmerOn) {
		this.stemmerOn = stemmerOn;
	}

	public void setDocument(Document doc) {
		this.doc = doc;
	}

	public Set<String> findTerms() {
		Set<String> terms = new TreeSet<String>();
		String title = doc.getTitle();
		if (title != null) {
			String[] titleWords = title.split(WHITESPACE);
			for (String term : titleWords) {
				if (term.matches(CHARACTER)) {
					if (stemmerOn) {
						Stemmer stemmer = new Stemmer();
						stemmer.add(term.toLowerCase().toCharArray(), term.length());
						stemmer.stem();
						String stemmedTerm = stemmer.toString();
						terms.add(stemmedTerm);

					}
					else {
						terms.add(term.toLowerCase());
					}
				}
			}
		}
		String abs = doc.getAbstract();
		if (abs != null) {
			String[] words = abs.split(WHITESPACE);
			for (String term: words) {
				if (term.matches(CHARACTER)) {
					if (stemmerOn) {
						Stemmer stemmer = new Stemmer();
						stemmer.add(term.toLowerCase().toCharArray(), term.length());
						stemmer.stem();
						String stemmedTerm = stemmer.toString();
						terms.add(stemmedTerm.toLowerCase());
					}
					else {
						terms.add(term.toLowerCase());
					}
				}
			}
		}
		
		return terms;
	}

	public int calculateFrequency(String term) {
		int frequency = 0;
		if (doc.getTitle() != null) {
			String[] words = doc.getTitle().split(WHITESPACE);			
			for (int i = 0; i < words.length; i++) {
				if (stemmerOn) {
					Stemmer stemmer = new Stemmer();
					stemmer.add(words[i].toLowerCase().toCharArray(), words[i].length());
					stemmer.stem();
					String stemmedTerm = stemmer.toString();
					if (stemmedTerm.equalsIgnoreCase(term)) {
						frequency++;
					}
				}
				else {
					if (words[i].equalsIgnoreCase(term)) {
						frequency++;
					}
				}
			}
		}
		if (doc.getAbstract() != null) {
			String[] words = doc.getAbstract().split(WHITESPACE);
			for (int i = 0; i < words.length; i++) {
				if (stemmerOn) {
					Stemmer stemmer = new Stemmer();
					stemmer.add(words[i].toLowerCase().toCharArray(), words[i].length());
					stemmer.stem();
					String stemmedTerm = stemmer.toString();
					if (stemmedTerm.equalsIgnoreCase(term)) {
						frequency++;
					}
				}
				else {
					if (words[i].equalsIgnoreCase(term)) {
						frequency++;
					}
				}
			}
		}
		
		return frequency;
	}
	

	public Set<Integer> parsePositions(String term) {
		Set<Integer> positions = new TreeSet<Integer>();
		String fullDocument = doc.getTitle();
		if (doc.getAbstract() != null) {
			fullDocument = fullDocument + " "+doc.getAbstract();
		}
		String[] words = fullDocument.split(WHITESPACE);
		for (int i = 0; i <words.length; i++) {
			if(stemmerOn){		
			Stemmer stemmer = new Stemmer();
			stemmer.add(words[i].toLowerCase().toCharArray(), words[i].length());
			stemmer.stem();
			String stemmedTerm = stemmer.toString();
			if (stemmedTerm.equalsIgnoreCase(term)) {
				positions.add(i);
			}
		}
		else{
			if(words[i].equalsIgnoreCase(term)){
				positions.add(i);
			}
		}
	}
		return positions;
	}

}
