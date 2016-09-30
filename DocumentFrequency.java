
public class DocumentFrequency {
	
	private int df;
	
	public DocumentFrequency() {
		df = 0;
	}
	
	public DocumentFrequency(int df) {
		this.df = df;
	}
	
	public void updateFrequency() {
		df += 1;
	}
	
	public int getDocumentFrequency() {
		return df;
	}
	public String toString()
	{
		return Integer.toString(df);
	}

}
