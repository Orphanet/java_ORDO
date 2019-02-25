use strict;
use warnings;


my $rootDirectory = ".";
my $lang          = "";
my $date          = "";
my $file          = "";
my $fileFilter    = "";



my $usage = "USAGE : escape_lt_gt.pl ([-lng=<lng>] [-date=<dateFolder>] [-root=<rootDirectory>] [-filter=<fileFilter>]) | -file=<fileName> \n  Example: escape_lt_gt.pl -lng=en -filter=OBO_EBI\n\n\>>>  Type escape_lt_gt.pl -help for more informations  <<<\n\n";
my $help  = "
    -root   : define the directory containing the language directories. By default the root directory is set to '.'.
    -lng    : will limit the file research to the directory named before the ISO (ex: <rootDirectory>/en/). By default, it will explore all tow letters directory into root directory.
    -date   : will limit the file research to the directory named before the date of extraction (ex: <rootDirectory>/<lang>/07_01_2019/ or <rootDirectory>/<lang>/novembre_2018/). By default, it will explore all directory in language directories.
    -filter : set a filter to limit the exectution of this script to files containing the string in argument (ex: <rootDirectory>/<lang>/<date>/*OBO_EBI*).
    -file   : this argument override all other. The script will be process only on the file specified.
";
if($#ARGV>=0){
	foreach my $arg (@ARGV){
		if($arg=~/^-lng=(.*)$/){
			$lang = $1;
		}elsif($arg=~/^-date=(.*)$/){
			$date = $1;
		}elsif($arg=~/^-root=(.*)$/){
			$rootDirectory = $1;
		}elsif($arg=~/^-filter=(.*)$/){
			$fileFilter = $1;
		}elsif($arg=~/^-file=(.*)$/){
			$file = $1;
		}elsif($arg=~/^--?h(elp)?$/){
			die("$usage\n$help\n");
		}else{
			die ("$arg is not a valid option\n $usage");
		
		}
		
	}
	
}else{
	print "It is recommanded to use arguments to prevent to modify all your files\n $usage";
	die();
}



if($file ne ""){
	print escapeChars($file);
}else{
	opendir (ROOTDIR, $rootDirectory) or die $!;
	while (my $dh1 = readdir(ROOTDIR)) {
		
		if($lang ne "" and $dh1 ne $lang){next;}
		
		if($dh1=~/^\w{2}$/ and -d "$rootDirectory/$dh1"){
			opendir (LNGDIR, "$rootDirectory/$dh1") or die $!;
			while (my $dh2 = readdir(LNGDIR)) {
				
				if($date ne "" and $dh2 ne $date){next;}
				
				if($dh2!~/\.+$/ and -d "$rootDirectory/$dh1/$dh2"){
					opendir (DATEDIR, "$rootDirectory/$dh1/$dh2") or die $!;
					while (my $dh3 = readdir(DATEDIR)) {
						
						if($dh3!~/$fileFilter/){next;}
						
						if($dh3!~/\.+$/ and -f "$rootDirectory/$dh1/$dh2/$dh3"){
							
							print escapeChars("$rootDirectory/$dh1/$dh2/$dh3");
							
							
						}
					}
				}
			}
		}
	}
}


sub escapeChars{
	my ($filename) = @_;
	open(FIN,"<",$filename) or return "ERROR OCCURED in opening $filename $!";
	my @lines = <FIN>;
	close FIN;
	my $fileText = join("",@lines);
	
	my $nbI  = ($fileText=~s/((&lt;)|(&gt;))\/?i((&lt;)|(&gt;))//gi);
	my $nbBr = ($fileText=~s/((&lt;)|(&gt;))\/?br((&lt;)|(&gt;))//gi);
	my $nbLt = ($fileText=~s/&lt;/||lt||/g);
	my $nbGt = ($fileText=~s/&gt;/||gt||/g);
	
	open(FOUT,">",$filename) or return "ERROR OCCURED in writting $filename $!";
	print FOUT $fileText;
	close FOUT;
	return "'<' and '>' escaped for $filename\nNb of <i> : $nbI ; <br> : $nbBr; < : $nbLt; > : $nbGt\n";
}






