Ako direktorijum "example" ubacimo u folder projekta, pokrenemo program i unesemo komande:
ad example/data
ad example/data2

U tom slučaju treba da budu analizirani:
-corpus_riker (nalazi se direktno u data)
-corpus_sagan (nalazi se direktno u data)
-corpus_mcfly (nalazi se u poddirektorijumu subdir u data2)

Ispis programa bi mogao da bude sledeći:
ad example/data
Adding dir /home/pengu/Apps/eclipse-se/workspace/KiDSDomaci1/example/data
Starting file scan for file|corpus_riker
Starting file scan for file|corpus_sagan
ad example/data2
Adding dir /home/pengu/Apps/eclipse-se/workspace/KiDSDomaci1/example/data2
Starting file scan for file|corpus_mcfly
get file|corpus_riker
{one=3, two=1, three=1}

Takođe, ne treba da budu analizirani:
trump_corpus (ne počinje sa "corpus_")
corporate.txt (nije deo korpusa)
corpus_troll (nalazi se u data3 za koji nismo zadali komandu obilaženja)

Ovde pretpostavljamo da je u konfiguraciji navedeno da je "corpus_" prefiks za korpuse.

Svi direktorijumi koji predstavljaju korpuse imaju sledeće osobine:
-Nemaju poddirektorijume.
-Sadrže samo tekstualne datoteke koje su deo korpusa.

Tekst u fajlovima generisan pomoću sledećih "Lorem ipsum" generatora:
http://saganipsum.com/
http://www.rikeripsum.com/
https://deloreanipsum.com/
https://www.cipsum.com/
http://trollemipsum.appspot.com/
https://trumpipsum.net/

