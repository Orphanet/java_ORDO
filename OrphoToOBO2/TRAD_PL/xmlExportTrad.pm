###  Description : 
###  Author : Samuel Demarest
###  Last Update : 21/02/2018
###  

my $ISO     = "pl";
my $DEBUG   = 0;
my $VERBOSE = 0;
my $DEL_EN  = 0;

# defini l'ordre d'apparition des champs

my %keys_order = (
		Disorder                        =>  0,
		DisorderDisorderAssociation     =>  0,
		Classification                  =>  0,
		TextualInformation              =>  0,
		TextSection                     =>  0,
		TypeOfInheritance               =>  0,
		DisorderFlag                    =>  0,
		AverageAgeOfOnset               =>  0,
		AverageAgeOfDeath               =>  0,
		Prevalence                      =>  0,
		id                              =>  0,
		lang                            =>  0,
		cycle                           =>  0,
		count                           =>  0,
		content                         =>  0,
		ClassificationFlag              =>  0,
		ClassificationNode              =>  0,
		OrphaNumber                     =>  1,
		Info                            =>  1,
		Disorder1                       =>  1,
		TextSectionList                 =>  1,
		TextSectionType                 =>  1,
		Value                           =>  1,
		PrevalenceType                  =>  1,
		Disorder2                       =>  2,
		Contents                        =>  2,
		ExpertLink                      =>  2,
		PrevalenceQualification         =>  2,
		Name                            =>  3,
		DisorderDisorderAssociationType =>  3,
		PrevalenceClass                 =>  3,
		Label                           =>  3,
		DisorderType                    =>  4,
		ValMoy                          =>  4,
		ClassificationFlagList          =>  4,
		DisorderFlagList                =>  4,
		ClassificationNodeChildList     =>  4,
		Symbol                          =>  4,
		ClassificationNodeRootList      =>  5,
		PrevalenceList                  =>  5,
		PrevalenceGeographic            =>  5,
		Source                          =>  5,
		SynonymList                     =>  5,
		Synonym                         =>  5,
		Reference                       =>  6,
		PrevalenceValidationStatus      =>  6,
		AverageAgeOfOnsetList           =>  6,
		GeneType                        =>  6,
		AverageAgeOfDeathList           =>  7,
		DisorderMappingRelation         =>  7,
		GeneList                        =>  7,
		TypeOfInheritanceList           =>  8,
		ExternalReferenceList           =>  8,
		DisorderMappingICDRelation      =>  8,
		ExternalReference               =>  8,
		LocusList                       =>  9,
		DisorderDisorderAssociationList =>  9,
		DisorderMappingValidationStatus =>  9,
		Locus                           =>  9,
		GeneLocus                       => 10,
		TextualInformationList          => 10,
		TextAuto                        => 11,
		LocusKey                        => 11,
		DisorderGeneAssociationList     => 12,
		DisorderGeneAssociation         => 12,
		DisorderGeneAssociationType     => 13,
		DisorderGeneAssociationStatus   => 14,
		Gene                            => 15,
);

### dictionnaire de substitution à apporter automatiquement sur un type précis de contenu sous la forme de :
#
#	"NOM DU CHAMP" => {ORI => "CHAINE DE CHAR A MATCHER", SUBS => "CHAINE DE CHAR DE SUBSTITUTION"}
#
# Ce dictionnaire peut etre incremente et ORI et SUBS peuvent contenir des éléments de regex
my %DEFAULT_MODIF = (
	"ExpertLink" => {ORI => "lng=en&", SUBS => "lng=$ISO&amp;" },
);

my $OUTPUT=STDOUT;

our %terms = ("test" => "test");

##### fonctions ########

### activation de l'application des regles de suppressions de contenu si $bool = 1; désactivation si $bool = 0
sub setDeleteEn{
	my ($bool) = @_;
	$DEL_EN = $bool==0?0:1;
}

### definir les types de messages de suivi de processus
sub setVerbose{
	my ($bool) = @_;
	$VERBOSE = $bool==0?0:1;
}
sub setDebug{
	my ($bool) = @_;
	$DEBUG = $bool==0?0:1;
}
# Definir la sortie de parcours
sub setOutput{
	($OUTPUT) = @_;
}

####################################################################
###      fonction de parcours du hash à exporter / traduire      ###
### gere les appel aux differents outils de construction du xml  ###
####################################################################
sub parcours{
	my ($prevName,$name,$depth,$hashRef) = @_;
	my $attr="";
	my $addLang = 0;
	
	if($depth == 1){
		print $OUTPUT header();
	}
	
	
	# print Dumper $hashRef;
	if(ref $hashRef ne "HASH"){
		print $OUTPUT monoBalise($prevName,$name, $hashRef, "", $depth);
		return 0;
	}

	my @allKeywords =jdborBaliseSort( keys(%{$hashRef}));
	my @keywords;
	debug("prevName :",$prevName);
	debug("name :",$name);
	debug("keywords :",@allKeywords);
	my $index=0;
	foreach my $key (@allKeywords){
		
		if(! exists $keys_order{$key}){$keys_order{$key}=20; print "New key ? : $key\n";}
		
		if ($key eq 'id'){
			$attr .= " id=\"".$hashRef->{id}."\"";
			# splice(@keywords,$index,1);
		}elsif($key eq 'lang'){
		
			# $attr .= " lang=\"".$ISO."\"";  ### insérer $hashRef->{lang} plus tard
			$attr .= " lang=\"".$hashRef->{lang}."\"";  
			if($hashRef->{lang} ne $ISO){}
			# splice(@keywords,$index,1);
		}elsif($key eq 'count'){
			$attr .= " count=\"".$hashRef->{count}."\"";
			# splice(@keywords,$index,1);
		}elsif($key eq 'cycle'){
			$attr .= " cycle=\"".$hashRef->{cycle}."\"";
			# splice(@keywords,$index,1);
		}else{
			push @keywords, $key;
		}
		
	}
	debug("attr :",$attr);
	if($attr=~/count/ and $hashRef->{count} ne 0){
		my $childKey = $name;
		$childKey=~s/List$//i;
		if(!exists $hashRef->{$childKey}){
			if($childKey eq "ClassificationNodeChild"){ ### exception pour les ClassificationNodeChildList qui contiennent des ClassificationNode
				$childKey = "ClassificationNode";
			}elsif($childKey eq "ClassificationNodeRoot"){ ### exception pour les ClassificationNodeRootList qui contiennent des ClassificationNode
				$childKey = "ClassificationNode";
			}else{
				die "$name do not contain $childKey\n";
			}
			
		}
		print $OUTPUT baliseOuver($name,$attr,$depth);
		if(ref $hashRef->{$childKey} eq "ARRAY"){
			foreach my $child (@{$hashRef->{$childKey}}){
				parcours($name,$childKey,$depth+1,$child);
			}
		}elsif(ref $hashRef->{$childKey} eq "HASH"){
			while(my ($id, $child) = each %{$hashRef->{$childKey}}){
				if($id=~/^\d+$/){$child->{id}=$id;}				
				parcours($name,$childKey,$depth+1,$child);
			}
			
		}else{
			print STDERR "ERROR : not ARRAY nor HASH : $childKey\n";
		}
		print $OUTPUT baliseFerm($name,$depth);
	}elsif($attr=~/count/ and $hashRef->{count} == 0){
		print $OUTPUT baliseOuver($name,$attr,$depth);
		print $OUTPUT baliseFerm($name,$depth);
	}elsif ($#keywords eq 0 and $keywords[0] eq "content"){
		print $OUTPUT monoBalise($prevName,$name, $hashRef->{$keywords[0]}, $attr, $depth);
		
	}elsif ($#keywords eq 0 and ref $hashRef->{$keywords[0]} eq ""){
		
		print $OUTPUT baliseOuver($name,$attr,$depth);
		print $OUTPUT monoBalise($name,$keywords[0], $hashRef->{$keywords[0]}, "", $depth+1);
		print $OUTPUT baliseFerm($name,$depth);
		
	}
	else{
		print $OUTPUT baliseOuver($name,$attr,$depth);
		foreach my $key (@keywords){
			# print "  "x$depth+1;
			# print "$key\n";
			
			parcours($name,$key,$depth+1,$hashRef->{$key});
			
		}
		print $OUTPUT baliseFerm($name,$depth);
	}
	
	if($depth == 1){
		print $OUTPUT footer();
	}
}


sub header{
	my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst) = localtime(time);
	$year = sprintf("%04d", ($year+1900));
	$mon  = sprintf("%02d", $mon);
	$mday = sprintf("%02d", $mday);
	$hour = sprintf("%02d", $hour);
	$min  = sprintf("%02d", $min);
	$sec  = sprintf("%02d", $sec);
	
	my $head="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	$head .= baliseOuver("JDBOR"," date=\"$year-$mon-$mday $hour:$min:$sec\" version=\"1.2.7 / 4.1.6 [2017-03-09] (orientdb version)\" copyright=\"Orphanet (c) 2019\"",0);
	return $head;
}

sub footer{
	return baliseFerm("JDBOR",0);
}


#### Regles de suppression de contenu. A voir si création de hash de regles pour permettre modifications des regles a la volée.
sub deleteContent{
	my ($prevObject, $balise, $attr) = @_;
	if($DEL_EN == 0){
		return 0;
	}
	
	if($attr=~/lang="en"/){
		if($prevObject eq "Gene"){
			return 0;
		}
		return 1;
	}
	
	return 0;
}

#### Fonction faisant la sortie du balise contenant uniquement un champs texte et des attributs (Orphanumber ou Name par exemple), vide ou auto fermante
sub monoBalise{
	my ($prevObject,$balise, $content, $attr, $depth) = @_;
	my $print = "  "x$depth;
	my $treatedContent = $content;
	
	$treatedContent=~s/^OBSOLETE:\s*//i;
	
	# print "monobalise($prevObject,$balise, $content, $attr, $depth)\n";
	if(exists $terms{$content} or exists $terms{$treatedContent} or defined $DEFAULT_MODIF{$balise}){
		if ($attr=~/lang="\w{2}"/){
			$attr=~s/lang="\w{2}"/lang="$ISO"/;
		}
	}
	
	$print .= "<$balise";
	
	if(!defined $content){
		$print .="/>\n";
	}else{
		if ($attr ne ""){
			$print .= "$attr>";
		}else{
			$print .= ">";
		}
		
		if(exists $terms{$content}){
			$print .= $terms{$content};
		}elsif(defined $DEFAULT_MODIF{$balise}){
			my $defaultModif = $content;
			my $ori = $DEFAULT_MODIF{$balise}{ORI};
			my $sub = $DEFAULT_MODIF{$balise}{SUBS};
			$defaultModif=~s/$ori/$sub/g;
			$print .= $defaultModif;
		}elsif(deleteContent($prevObject, $balise, $attr)){
			### NO PRINTING FOR DELETING
		}else{
			if($content=~/https?:\/\//i){
				$content=~s/&(?!amp;)/&amp;/g;
			}
			$print .= trim($content);
		}
		$print .= "</$balise>\n";
	}
	return $print;
}

### Ouverture d'une balise
sub baliseOuver{
	my ($balise, $attr, $depth) = @_;
	my $print = "  "x$depth;
	
	$print .= "<$balise";

	
	if ($attr ne ""){
		$print .= "$attr";
	}
	$print .= ">\n";
	return $print;
}

### Fermeture d'une balise
sub baliseFerm{
	my ($balise, $depth) = @_;
	my $print = "  "x$depth;
	
	$print .= "</$balise>\n";
	
	return $print;
}


sub debug{
	if ($DEBUG){print STDERR join("\t",@_)."\n";}
}

sub verbose{
	if ($VERBOSE){print join("\t",@_)."\n";}
}

sub jdborBaliseSort{
	my (@keys) = @_;
	my @sorted_keys = sort {$keys_order{$a} <=> $keys_order{$b}} @keys;
	

}

sub getIndexTrad{
	my ($id)=@_;
	for (my $i = $#{$trads{DisorderList}{Disorder}}; $i>=0;$i--){
		if($trads{DisorderList}{Disorder}[$i]{id} eq $id){
			return $i;
		}
	}
	return -1;
}

sub trim{
	my ($text)=@_;
	
	$text=~s/\s+$//i;
	$text=~s/^\s+//i;
	return $text;
	
}

1;
