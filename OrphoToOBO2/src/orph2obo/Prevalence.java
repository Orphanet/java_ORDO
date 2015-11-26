package orph2obo;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents a single gene as represented in the
 * Orphanet file "http://www.orphadata.org/data/xml/en_product6.xml"
 * @author peter
 *
 */
public class Prevalence {
	private String prevalclass;
	private String valMoy;
	private String geo;
	private String type;
	
	
	public Prevalence() {}

	public void setPrevalClass(String tempVal) { this.prevalclass = tempVal; }
	public void setValMoy(String tempVal) { this.valMoy = tempVal;}
	public void setGeo(String tempVal) { this.geo = tempVal; }
	public void setType(String tempVal) { this.type = tempVal; }
	
	public String getPrevalClass() { return this.prevalclass; }
	public String getValMoy() { return this.valMoy;}
	public String getGeo() { return this.geo; }
	public String getType() { return this.type; }
	
	public String toString() {
		/*StringBuilder sb = new StringBuilder();
		sb.append("comment: gene: " + this.symbol);
		sb.append(" [orphanum: " + orphanum +"; name:\"" + name +"\"");
		Iterator<ExternalReference> it = xrefs.iterator();
		while (it.hasNext()) {
			ExternalReference xr = it.next();
			sb.append(";" + xr.getSource() + ":" + xr.getReference());
		}
		sb.append("]\n");*/
		return " [Class: "+prevalclass+"; Type: "+type+"; MeanVal: "+valMoy+"; Geo"+geo+"]\n";
	}
	
	
		
		

}
