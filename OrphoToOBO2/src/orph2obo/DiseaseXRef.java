package orph2obo;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Dont use this class
 * @author drashtti
 *
 */
public class DiseaseXRef {
	String orphanum;
	String name;
	String prevalenceClass;
	String synonym;
	ArrayList<String> experts;
	ArrayList<ExternalReference> extref;
	ArrayList<Gene> genes;
	ArrayList<String> is_a;
	
	public DiseaseXRef() {
		experts = new ArrayList<String>();
		extref = new ArrayList<ExternalReference>();
		is_a = new ArrayList<String>();
	}

	public void setOrphanum(String tempVal) {
		this.orphanum = tempVal;
	}

	public void addExpertLink(String tempVal) {
		experts.add(tempVal);
	}

	public void setName(String tempVal) {
		this.name = tempVal;		
	}
	
	public String get_orphanum() {return this.orphanum; }
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[Term]\n");
		//System.out.println("you have reached here");
		sb.append("id: ORPH:" + this.orphanum + "\n");
		sb.append("name: " + this.name + "\n");
		if (this.prevalenceClass != null)
			sb.append("comment: prevalence_class: " + this.prevalenceClass + "\n");
		
		Iterator<ExternalReference> iter = this.extref.iterator();
		while (iter.hasNext()) {
			ExternalReference er = iter.next();
			sb.append(er + "\n");
		}
		if (this.genes != null) {
			Iterator<Gene> it2 = this.genes.iterator();
			while (it2.hasNext()) {
				Gene g = it2.next();
				sb.append(g + "\n");
			}
		}
		Iterator<String> it3 = this.is_a.iterator();
		while (it3.hasNext()) {
			String isa = it3.next();
			sb.append("is_a: ORPH:" + isa + "\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	public void addExternalReference(ExternalReference tmpExtRef) {
		this.extref.add(tmpExtRef);
		
	}
	
	
	public void setPrevalenceClass(String prev) {
		this.prevalenceClass = prev;
		
	}
	
	

	public void add_genes(ArrayList<Gene> gen) {
		this.genes= gen;
	}

	public void add_is_a_link(String lastOrphanum) {
		if (this.is_a.contains(lastOrphanum)) return; // don't add more than once
		this.is_a.add(lastOrphanum);
	}


}
