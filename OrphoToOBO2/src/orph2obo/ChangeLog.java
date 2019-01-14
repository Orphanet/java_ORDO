/***
 * 
 * @author samuel demarest
 * 
 * Export : export => java => runnable jar => select ChangeLog in "Launch configuration" and select "extract required librairies" 
 * WARNING : sous windows, les chemins des deux ontologies à comparer doivent être sous formes unix (/path/file.owl et pas C:\path\file.owl) sans le nom du disque,
 * 			contrairement au fichier de sortie. Ce dernier doit être saisi sans l'extension car deux fichiers (.txt et .xml) sont générés
 * 
 */

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
		String prevFile      = "/OrphoToOBO2/ORDO_"+ORDOVariables.lang+"_"+ORDOVariables.prevVersion+".owl";
		String newFile       = "/OrphoToOBO2/ORDO_"+ORDOVariables.lang+"_"+ORDOVariables.version+".owl";
		String changeLogFile = "C:\\OrphoToOBO2\\bubastis_change_log_ORDO_"+ORDOVariables.lang+"_"+ORDOVariables.version+"_"+ORDOVariables.prevVersion;
		/* prev version : "file:/OrphoToOBO2/ORDO_"+Conf.lang+"_"+Conf.prevVersion+".owl"
		 * new version :  "file:/OrphoToOBO2/"+product+"_"+Conf.lang+"_"+Conf.version+".owl"
		 * txt output "C:\\OrphoToOBO2\\bubastis_change_log_"+product+"_"+Conf.lang+"_"+Conf.version+"_"+Conf.prevVersion+".txt"
		 * xml output "C:\\OrphoToOBO2\\bubastis_change_log_"+product+"_"+Conf.lang+"_"+Conf.version+"_"+Conf.prevVersion+".xml"
		 * 
		 */
		
		System.setOut(defaultOut);
		
		if(args.length>2){
			System.out.println("using ARGS");
			prevFile      = args[0];
			newFile       = args[1];
			changeLogFile = args[2];			
		}else{
			System.out.println("default files used (ORDO last generation)\nYou can specify input/output using args changelog.jar prevVersion.owl newVersion.owl changelogFile");
		}
		
		//String version = "2.8";
		System.out.println("Comparing versions "+prevFile+" and "+newFile+" and writing in "+changeLogFile+".xml/txt");
		try {
			 f = new FileOutputStream(changeLogFile+".txt");			 
			 System.setOut(new PrintStream(f));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CompareOntologies bubastis = new CompareOntologies();
		bubastis.doFindAllChanges("file:"+prevFile,"file:"+newFile);
		bubastis.writeDiffAsXMLFile(changeLogFile+".xml");
		System.setOut(defaultOut);
		System.out.println("Exit program after saving Change Log");
    }
}
