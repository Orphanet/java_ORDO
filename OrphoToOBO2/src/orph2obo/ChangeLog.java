package orph2obo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;


import uk.ac.ebi.efo.bubastis.CompareOntologies;
import uk.ac.ebi.efo.bubastis.exceptions.Ontology1LoadException;
import uk.ac.ebi.efo.bubastis.exceptions.Ontology2LoadException;

public class ChangeLog {
    public static void main(String[] args) throws Ontology1LoadException, Ontology2LoadException {
    	FileOutputStream f;
		PrintStream defaultOut =  System.out;
		String product = "ORDO";
		//String version = "2.8";
		System.out.println("Comparing versions "+Conf.version+" and "+Conf.prevVersion+" of "+product+" in "+Conf.lang);
		try {
			 f = new FileOutputStream("C:\\OrphoToOBO2\\bubastis_change_log_"+product+"_"+Conf.lang+"_"+Conf.version+"_"+Conf.prevVersion+".txt");			 
			 System.setOut(new PrintStream(f));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CompareOntologies bubastis = new CompareOntologies();
		bubastis.doFindAllChanges("file:/OrphoToOBO2/"+product+"_"+Conf.lang+"_"+Conf.prevVersion+".owl","file:/OrphoToOBO2/"+product+"_"+Conf.lang+"_"+Conf.version+".owl");
		bubastis.writeDiffAsXMLFile("C:\\OrphoToOBO2\\bubastis_change_log_"+product+"_"+Conf.lang+"_"+Conf.version+"_"+Conf.prevVersion+".xml");
		System.setOut(defaultOut);
		System.out.println("Exit program after saving Change Log");
    }
}
