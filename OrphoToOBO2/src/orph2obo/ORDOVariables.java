package orph2obo;

import java.util.ArrayList;

public class ORDOVariables {
	static public String lang             = "de"; 
	static public String version          = "2.7"; 
	static public String prevVersion      = "2.6"; 
	static public String xmlDir           = "novembre_2018"; 
  	static public String dateOfCreation   = "2013-06-20T12:00:00";
  	static public String licensePrefixIRI = "https://creativecommons.org/licenses/";
  	static public String licenseClassIRI  = "by/4.0/";
  	//static public String licenseIRI     = "https://creativecommons.org/licenses/by/4.0/";
  	
  	
  	static public String[][] legalTerms = {
  		{"permits","http://web.resource.org/cc/Reproduction"},
  		{"permits","http://web.resource.org/cc/DerivativeWorks"},
  		{"permits","http://web.resource.org/cc/Distribution"},
  		{"requires","http://web.resource.org/cc/Notice"},
  		{"requires","http://web.resource.org/cc/Attribution"},  	
  	};
  	
	static public ArrayList <String> creatorList = new ArrayList<String>() {{
	    add("James Malone");
	    add("Drashtti Vasant");
	    add("Valérie Lanneau");
  	    add("Céline Rousselot");
	    add("Samuel Demarest");
	    add("David Lagorce");
	    add("Annie Olry");
	    add("Marc Hanauer");
	    add("Ana Rath");
  	}};
}
