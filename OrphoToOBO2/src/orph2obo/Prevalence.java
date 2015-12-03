package orph2obo;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents a single gene as represented in the
 * Orphanet file "http://www.orphadata.org/data/xml/en_product6.xml"
 * @author Samuel Demarest maj Nov2015
 *
 */
public class Prevalence {
	private String prevalclass;
	private String valMoy;
	private String geo;
	private String geoLab;
	private String type;
	private String typeLab;
	
	
	public Prevalence() {}

	public void setPrevalClass(String tempVal) { this.prevalclass = tempVal; }
	public void setValMoy(String tempVal) { this.valMoy = tempVal;}
	public void setGeo(String tempVal) { this.geo = tempVal; }
	public void setGeoLab(String tempVal) { this.geoLab = tempVal; }
	public void setType(String tempVal) { this.type = tempVal; }
	public void setTypeLab(String tempVal) { this.typeLab = tempVal; }
	
	public String getPrevalClass() { return this.prevalclass; }
	public String getValMoy() { return this.valMoy;}
	public String getGeo() { return this.geo; }
	public String getGeoLab() { return this.geoLab; }
	public String getType() { return this.type; }
	public String getTypeLab() { return this.typeLab; }
	
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
