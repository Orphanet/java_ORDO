use strict;
use warnings;
use Data::Dumper;
use XML::Simple qw(:strict);
use utf8;




my $fh = "OBO_EBI_en.xml";

### if you need to convert a file into utf-8 use this. Could be used globally or specifically on each file type
if ($^O ne "MSWin32"){
	system "iconv -f iso-8859-1 -t UTF-8 $fh > $fh.bis";
	$fh = "$fh.bis";
}

# open $fh, open( my $ft, '-|',"iconv -f iso-8859-1 -t UTF-8 $fh") or die "open($fh): $!";  ### /\ don't work on Windows. ORDO generation use OBO_EBI_<ISO>.xml to get labels, syns and definition, it is critical to have no encoding issues
my $enHash     = XMLin($fh,   KeyAttr => { Disorder => 'OrphaNumber' }, ForceArray => [ 'Disorder','DisorderFlag','Synonym','ExternalReference','DisorderDisorderAssociation','TextualInformation','TextSection']) or die ("Can't open Orpha file : $!");


my $fileTocomplete = "ORDO_pl_2.8.owl";
my $fileOutput     = "test.owl";

if ($#ARGV>-1){
	$fileTocomplete = $ARGV[0];
	if($#ARGV>0){
		$fileOutput = $ARGV[1];
	}
}

open(FIN, "<", $fileTocomplete) or die ("$!");

my @lines = <FIN>;
close FIN;

my $allLines = join("", @lines);

my @concepts = split(/<!-- http:\/\/www\.orpha\.net\/ORDO\/Orphanet_\d+ -->/,$allLines);

foreach my $concept (@concepts){
	my $oriConcept = $concept;
	if(($concept=~/<rdfs:subClassOf rdf:resource="http:\/\/www.orpha.net\/ORDO\/Orphanet_3777(\d{2})"\/>/ and $1 > 87 and $1 <98) or $concept=~/<rdfs:subClassOf rdf:resource="http:\/\/www.orpha.net\/ORDO\/ObsoleteClass"\/>/){
		my $iri      = "";
		my $orphanum = "";
		if($concept=~/<owl:Class rdf:about="http:\/\/www.orpha.net\/ORDO\/(Orphanet_(\d+))">/){
			$iri      = $1;
			$orphanum = $2;
			
			if ($concept!~/rdfs:label/){
				print "ERROR for $iri\n";
				print "$concept\n";
			}elsif($concept=~/<rdfs:label xml:lang="pl"><\/rdfs:label>/){
				# print "EMPTY LABEL for Orpha:$orphanum\n";
				
				my $enLab = $enHash->{DisorderList}{Disorder}{$orphanum}{Name}{content};
				$concept=~s/<rdfs:label xml:lang="pl"><\/rdfs:label>/<rdfs:label xml:lang="en">$enLab<\/rdfs:label>/;
				# print "$concept\n";
				
				$allLines=~s/\Q$oriConcept\E/$concept/;
			}
		}
	}
}

open (FOUT, ">", $fileOutput);

print FOUT $allLines;
close FOUT;