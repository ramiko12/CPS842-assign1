import java.text.ParseException;

import java.util.*;
import java.io.*;

public class FileParser {

	//all the flags turned to constants for ease of lookup
	private final String DOC_ID = ".I";
	private final String TITLE = ".T";
	private final String ABSTRACT = ".W";
	private final String PUBLICATION_DATE = ".B";
	private final String AUTHOR = ".A";
	private final String SPLIT = "\\s+";

	public FileParser() {}

	public Map<Integer, Document> extractDocuments(String fileName) throws ParseException {
		Map<Integer, Document> documents = new TreeMap<Integer, Document>();
		try {
			File file = new File(fileName);		
			BufferedReader reader = new BufferedReader(new FileReader(file));
			int currentID = 0;
			String line = reader.readLine();
			while (line != null) {
				if (line.startsWith(DOC_ID)) {
					currentID = parseID(line);
					Document doc = new Document(currentID);
					documents.put(currentID, doc);
					line = reader.readLine();
				}
				else if (line.startsWith(TITLE)) {
					line = reader.readLine();
					StringBuilder builder = new StringBuilder();
					while (line != null && !(line.startsWith(DOC_ID) || line.startsWith(TITLE) || line.startsWith(ABSTRACT) ||
							line.startsWith(PUBLICATION_DATE) || line.startsWith(AUTHOR))) {
						builder.append(line + "\n");
						line = reader.readLine();
					}
					Document doc = documents.get(currentID);
					doc.setTitle(builder.toString().trim());
				}	
				else if (line.startsWith(ABSTRACT)) {
					line = reader.readLine();
					StringBuilder builder = new StringBuilder();
					while (line != null && !(line.startsWith(DOC_ID) || line.startsWith(TITLE) || line.startsWith(ABSTRACT) ||
							line.startsWith(PUBLICATION_DATE) || line.startsWith(AUTHOR) )) {
						builder.append(line + "\n");
						line = reader.readLine();
					}
					Document doc = documents.get(currentID);
					doc.setAbstract(builder.toString());
				}	
				else if (line.startsWith(PUBLICATION_DATE)) {
					line = reader.readLine();
					StringBuilder builder = new StringBuilder();
					while (line != null && !(line.startsWith(DOC_ID) || line.startsWith(TITLE) || line.startsWith(ABSTRACT) ||
							line.startsWith(PUBLICATION_DATE) || line.startsWith(AUTHOR) )) {
						builder.append(line + "\n");
						line = reader.readLine();
					}
					Document doc = documents.get(currentID);
					doc.setDate(builder.toString().trim());
					
				}
				else if (line.startsWith(AUTHOR)) {
					line = reader.readLine();
					StringBuilder builder = new StringBuilder();
					while (line != null && !(line.startsWith(DOC_ID) || line.startsWith(TITLE) || line.startsWith(ABSTRACT) ||
							line.startsWith(PUBLICATION_DATE) || line.startsWith(AUTHOR) )) {
						builder.append(line + "\n");
						line = reader.readLine();
					}
					Document doc = documents.get(currentID);
					doc.setAuthor(builder.toString().trim());
				}	
				
				else {
					line = reader.readLine();
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not find file: " + fileName);
		} catch (IOException e) {
			System.out.println("Could not read file: " + fileName);
		}		

		return documents;
	}

	public int parseID(String line) {
		String[] split = line.split(SPLIT);
		String id = split[1];
		return Integer.parseInt(id);
	}

	
}