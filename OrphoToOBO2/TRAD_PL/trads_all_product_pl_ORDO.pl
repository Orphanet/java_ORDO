###  Description : based on translation file convert xml needed to generate ORDO in PL
###  Author : Samuel Demarest
###  Last Update : 21/02/2018
###  

# use strict;
use warnings;
use Data::Dumper;
use XML::Simple qw(:strict);
use Spreadsheet::ParseExcel;
use Encode qw(decode encode);
use utf8;
use IO::Handle qw( );  # For autoflush
STDOUT->autoflush(1);
## chargement de la librairie
BEGIN {push @INC, "."}

use xmlExportTrad;

### set verbose and debug set both at 0 to silent mode
setVerbose(1);
setDebug(0);

my $directory;
my $tradFileMLL;
my $tradFileDis;
my $tradFileCla;
my $tradFileText;



if($#ARGV>2){
	$ISO          = $ARGV[0];
	$directory    = $ARGV[1];
	$tradFileDis  = $ARGV[2];
	$tradFileMLL  = $ARGV[3];
	$tradFileCla  = $ARGV[4];
	$tradFileText = $ARGV[5];
}else{
	die ("USAGE : trads_produit_all_product_pl_ORDO.pl <ISO> <input_directory> <diseases_label_trad_file>.txt <MLL_trad_file>.txt <classif_trad_file>.txt <textInfo_trad_file>.txt");
}

verbose("Getting term trad");
# si setDeleteEn(1) supprime le contenu EN en fonction des rêgles dans xmlExportTrad::deleteContent;
setDeleteEn(1);


################ GET ORPHAPOLISH DATA ##########################
verbose("Input Excel loading");
my %textInfoHash;
my %disSyns;

# SELECT * FROM d_orprc1d1_00.Classification;
verbose("",$tradFileCla);
open(CLASSIF, "<", $tradFileCla) or die("cannot open classif file : $!");
<CLASSIF>;
while(my $line = <CLASSIF>){
	$line=~s/NULL//g;
	
	$line=~s/[<>]\/?i[<>]//g;
	$line=~s/[<>]\/?br[<>]//g;

	$line=~s/</||lt||/g;
	$line=~s/>/||gt||/g;
	my @elems = split("\t",$line);
	if(defined $terms{$elems[3]}){print "WARNING : "+$elems[3]+" ("+$elems[4]+") ALREADY REGISTERED :"+$terms{$elems[3]};}
	$terms{$elems[3]} = $elems[4];
}

close(CLASSIF);

#SELECT * FROM d_orprc1d1_00.MLL";
verbose("",$tradFileMLL);
open(MLL, "<", $tradFileMLL) or die("cannot open MLL file : $!");
<MLL>;
while(my $line = <MLL>){
	$line=~s/NULL//g;
	
	$line=~s/[<>]\/?i[<>]//g;
	$line=~s/[<>]\/?br[<>]//g;

	$line=~s/</||lt||/g;
	$line=~s/>/||gt||/g;
	my @elems = split("\t",$line);
	if(defined $terms{$elems[3]}){print "WARNING : "+$elems[5]+" ("+$elems[6]+") ALREADY REGISTERED :"+$terms{$elems[5]};}
	$terms{$elems[5]} = $elems[6];
}

close(MLL);

#SELECT * FROM d_orprc1d1_00.Disorder WHERE TypeLbl IN ("App", "Trs", "Pat", "Syn");
verbose("",$tradFileDis);
open(DISORDER, "<", $tradFileDis) or die("cannot open disorder file : $!");
<DISORDER>;
while(my $line = <DISORDER>){
	$line=~s/NULL//g;
	
	$line=~s/[<>]\/?i[<>]//g;
	$line=~s/[<>]\/?br[<>]//g;

	$line=~s/</||lt||/g;
	$line=~s/>/||gt||/g;
	my @elems = split("\t",$line);
	if( $elems[7] eq "Pat" and defined $terms{$elems[3]}){print STDERR "WARNING : ".$elems[3]." (".$elems[4].") ALREADY REGISTERED :".$terms{$elems[3]}."\n";}
	if($elems[7] eq "Syn"){
		if(defined $disSyns{$elems[1]}){

			 push @{$disSyns{$elems[1]}}, $elems[4];

			
		}else{
			@{$disSyns{$elems[1]}} = ($elems[4]);
		}
	}else{
		$terms{$elems[3]} = $elems[4];
		# $terms{"OBSOLETE: ".$elems[3]} = "OBSOLETE: ".$elems[4];
	}
}


close(DISORDER);

#SELECT * FROM d_orpop1d1_00.Text_Info WHERE TxtLNG IS NOT NULL AND SectId=16907;
verbose("",$tradFileText);
open(TEXTINFO, "<", $tradFileText) or die("cannot open Text Info file : $!");
<TEXTINFO>;
while(my $line = <TEXTINFO>){
	$line=~s/NULL//g;
	
	$line=~s/[<>]\/?i[<>]//g;
	$line=~s/[<>]\/?br[<>]//g;

	$line=~s/</||lt||/g;
	$line=~s/>/||gt||/g;
	my @elems = split("\t",$line);
	$textInfoHash{$elems[2]} = $elems[5];
}

close(TEXTINFO);



################### FOR EACH EN FILE #########################

$directory=~s/\/$//;
opendir (DIR, $directory) or die $!;

verbose("Converting all files from $directory/ :");

while (my $inputEnFile = readdir(DIR)) {
	if($inputEnFile!~/xml$/){next;}
	my $enHash;
	verbose("", "-$inputEnFile");
	verbose("", "", "Opening");
	## open and set encoding of the file. May need individual case in file handle. In this case open file based on best option depending on file type
	# open my $fh, '-|',"iconv -f iso-8859-1 -t UTF-8 $directory/$inputEnFile" or die "open($directory/$inputEnFile): $!";  ### /\ don't work on Windows. best option of opening file but can be too heavy for the process. Useless if input file is in utf8
	 my $fh = "$directory/$inputEnFile";  ### lighter option but could induce encoding error. Best option if input file is in utf8

	############################################################
	#### for each type of file, we need to open it properly ####
	############################################################
	if($inputEnFile=~/product3/){
		$enHash     = XMLin($fh,   KeyAttr => { Classification => 'id' }, ForceArray => [ 'Classification','ClassificationNode','ClassificationFlag']) or die ("Can't open Orpha file : $!");
	}elsif($inputEnFile=~/product2/){
		$enHash     = XMLin($fh,   KeyAttr => { Disorder => 'id' }, ForceArray => [ 'Disorder','Prevalence','AverageAgeOfOnset','AverageAgeOfDeath','TypeOfInheritance']) or die ("Can't open Orpha file : $!");
	}elsif($inputEnFile=~/product6/){
		$enHash     = XMLin($fh,   KeyAttr => { Disorder => 'id' }, ForceArray => [ 'Disorder','Gene','ClassificationFlag','Synonym','ExternalReference','Locus','DisorderGeneAssociation']) or die ("Can't open Orpha file : $!");
	}elsif($inputEnFile=~/OBO_EBI/){
		system "iconv -f iso-8859-1 -t UTF-8 $fh > $fh.bis";
		# open( $fh, '-|',"iconv -f iso-8859-1 -t UTF-8 $fh") or die "open($fh): $!";  ### /\ don't work on Windows. ORDO generation use OBO_EBI_<ISO>.xml to get labels, syns and definition, it is critical to have no encoding issues
		$enHash     = XMLin("$fh.bis",   KeyAttr => { Disorder => 'id' }, ForceArray => [ 'Disorder','DisorderFlag','Synonym','ExternalReference','DisorderDisorderAssociation','TextualInformation','TextSection']) or die ("Can't open Orpha file : $!");
		# close $fh;
		### Particular cases : 
		###   - TextualInformation : no Lng on the content field => not recognized as translatable
		###   - Synonym            : no correspondance between languages => need to be set manually
		foreach my $patId (keys(%{$enHash->{DisorderList}{Disorder}})){
			my $xmlTextInfoList = $enHash->{DisorderList}{Disorder}{$patId}{TextualInformationList}{TextualInformation};
			my $xmlSynonymList  = $enHash->{DisorderList}{Disorder}{$patId}{SynonymList}{Synonym};
			
			if(exists $textInfoHash{$patId}){
				foreach my $xmlTextInfo (@{$xmlTextInfoList}){
					if($xmlTextInfo->{TextSectionList}{count}==1){
						$xmlTextInfo->{TextSectionList}{TextSection}[0]{Contents} = $textInfoHash{$patId};
					}
				}
			}else{
				foreach my $xmlTextInfo (@{$xmlTextInfoList}){
					if($xmlTextInfo->{TextSectionList}{count}==1){
						$xmlTextInfo->{TextSectionList}{TextSection}[0]{Contents} = "";
					}
				}
			}
			
			if(exists $disSyns{$patId}){
				my $nbSyns = 0;
				@{$enHash->{DisorderList}{Disorder}{$patId}{SynonymList}{Synonym}} = ();
				
				foreach my $xmlSyn (@{$disSyns{$patId}}){

					$enHash->{DisorderList}{Disorder}{$patId}{SynonymList}{Synonym}[$nbSyns]{"lang"}="pl";
					$enHash->{DisorderList}{Disorder}{$patId}{SynonymList}{Synonym}[$nbSyns]{"content"}=$xmlSyn;
					$nbSyns++;
				}
				$enHash->{DisorderList}{Disorder}{$patId}{SynonymList}{count} = "$nbSyns";
			}else{
				$enHash->{DisorderList}{Disorder}{$patId}{SynonymList}= ("count" => "0");
			}
		}
		
	}else{
		print "ERROR $inputEnFile is incorect\n";
		next;
	}
	# close $fh;
	my $outName = "outputs/$inputEnFile";
	$outName=~s/en/$ISO/;
	
	verbose("", "", "Starting Output to $outName");
	
	open(OUTPUT, ">", $outName);
	setOutput(OUTPUT);
	

	
	if($inputEnFile=~/product3_\d+/){
		parcours("JDBOR","ClassificationList",1,$enHash->{ClassificationList});
	}else{
		parcours("JDBOR","DisorderList",1,$enHash->{DisorderList});
	}
	%{$enHash}=();
	undef %{$enHash};
	close(OUTPUT);
	verbose("", "", "$outName finished\n");
}

END:

verbose("\nAll files converted");