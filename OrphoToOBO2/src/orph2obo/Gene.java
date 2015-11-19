package orph2obo;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents a single gene as represented in the
 * Orphanet file "http://www.orphadata.org/data/xml/en_product6.xml"
 * @author peter
 *
 */
public class Gene {
	private String orphanum;
	private String name;
	private String symbol;
	private ArrayList<ExternalReference> xrefs;
	
	
	public Gene() {
		xrefs = new ArrayList<ExternalReference>();
	}

	public void setOrphanum(String tempVal) { this.orphanum = tempVal; }
	public void setName(String tempVal) { this.name = tempVal;}
	public void setSymbol(String tempVal) { this.symbol = tempVal; }
	public void addExternalReference(ExternalReference tmpExtRef) {
		this.xrefs.add(tmpExtRef);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("comment: gene: " + this.symbol);
		sb.append(" [orphanum: " + orphanum +"; name:\"" + name +"\"");
		Iterator<ExternalReference> it = xrefs.iterator();
		while (it.hasNext()) {
			ExternalReference xr = it.next();
			sb.append(";" + xr.getSource() + ":" + xr.getReference());
		}
		sb.append("]\n");
		return sb.toString();
	}
	
	
		
		

}
