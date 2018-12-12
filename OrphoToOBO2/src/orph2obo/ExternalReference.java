package orph2obo;

public class ExternalReference {
	String source;
	String reference;
	
	

	public void setSource(String tempVal) {
		this.source = tempVal;	
		//System.out.println("Source =" + source );
	}

	public void setReference(String tempVal) {
		this.reference = tempVal;
		//System.out.println("reference =" + reference );
	}	
	
	public String toString() {
		//System.out.println(source + reference);
		StringBuilder sb = new StringBuilder();
		sb.append("xref: ");
		sb.append(this.source + ":" + this.reference);
		return sb.toString();
	}
	
	public String getSource() { return this.source;  }
	public String getReference() { return this.reference; }
}
