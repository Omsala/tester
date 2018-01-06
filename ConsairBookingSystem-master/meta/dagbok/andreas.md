# Dagbok för Andreas Rubensson

OSPP (1DT096) 2015 - Grupp 04

<img src="../images/andreas.png" width="200">

Gör en kort anteckning i dagboken under aktuell vecka och dag nedan
varje gång du arbetat på projektet.

## Vecka 16

##### Måndag 
Diskuterade projektförslag, kom fram till ett par idéer där ett flygbokningssystem var bland de populäraste men inget specifikt bestämdes. Är öppen för de flesta förslag och språk men vill gärna göra något som kan visas visuellt. Tidsåtgång 1h

##### Tisdag
Träffades för att börja göra den muntliga samt skriftliga presentationen. Vi har gjort en mall men inte fyllt i så mycket ännu på den skriftliga presentationen men desto mer på den muntliga. Vi har beslutat att ta idén om flygbokningssystemet. Beslutade att träffas imorgon onsdag från klockan 10 och framåt. Tidsåtgång 3h
##### Onsdag
Vi träffades för att färdigställa våran muntliga presentation och gjorde det också. Vi delade upp arbetet på presentationen också och jag ska prata om den övergripande systemarkitekturen. Detta tycker jag är bra då jag brukar ta de mindre komplicerade delarna. Vi bestämde också att färdigställa det skriftliga projektförslaget imorgon torsdag efter presentationerna, då vi förmodligen fått feedback och säkerhet på hur vi ska göra projektet. Tidsåtgång 2h 45min
##### Torsdag

##### Fredag
Jobbade på skriftliga projektförslaget tillsammans med gruppen. Tidsåtgång 3h

#### Söndag
Skrev färdigt skriftliga projektförslaget tillsammans med gruppen och lämnade in. Tidsåtgång 1h
## Vecka 17

##### Måndag
Har planerat och diskuterat uppdelning av projektet och bestämt att ha ett möte imorgon. Jag fick ansvar för att kolla upp hur TCP/IP fungerar mellan Erlang och Java. Har hittat en del info och det verkar helt klart möjligt att implementera. Tidsåtgång 2h + 1h

##### Tisdag
Vi träffades för ett kort möte, jag var tyvärr tvungen att gå efter ca 1 timme. Delade med mig om det jag hittils lärt mig om TCP samt att man skulle kunna använda jinterface som finns inbyggt i erlang/OTP men vi vill fortfarande köra på TCP som det verkar. Vi började arbeta på peer-reviewen av grupp 7's projektförslag. Tidsåtgång 1h

##### Onsdag
Satt en snabbis med att kolla igenom peer reviewen och reflektionen som hade deadline samma dag. Tidsåtgång <1h

##### Torsdag
Träffades för att gå igenom hur meddelandena mellan front och backend skulle utformas, med första Byten avgörande vilken operation man vill utföra och resten av meddelandet med argument osv. Diskuterade och var med på programmeringen i back-end. Tidsåtgång 3h
##### Fredag
Satt med Erik och försökte få GUIt att kompilera, men jag somna till och från och var inte direkt nöjd med en insats. Tveksamt om den platsar här. Tidsåtgång 2h

##### Lördag
Diskuterat hur vi ska göra med server och booking delen i back-enden samt hur vi ska formatera meddelanden, bestämde oss för att mötas imorgon för att arbeta tills vi har en prototyp som vi känner oss nöjda med. Effektiv tidsåtgång 1h

##### Söndag
Vi åkte aldrig till skolan utan jobbade hemifrån istället och snackade på skype. Lade till i vår loop där vi kollar vilken operation vi vill utföra i modulen där vi kollar inkommande meddelanden i tcp servern att vi ska returnera tcp meddelanden istället för att bara ha ett kall på en funktion. Tidsåtgång 1h
## Vecka 18

##### Måndag
Efter progress meeting 1 så hade vi ett möte och började rätta vårat projektförslag efter att ha fått peer-reviewen av den grupp som läste den. Vi blev i princip klara, osäker om något fattas. Tidsåtgång 1h
##### Tisdag

##### Onsdag

##### Torsdag

##### Fredag

## Vecka 19

##### Måndag
Jobbade med mycket inläsning om tcp osv. Vi planerade en del och delade upp arbetet. Ska fortsätta jobba med hur man tar emot och skickar meddelanden i tcp i backend. Tidsåtgång 5h
##### Tisdag

##### Onsdag

##### Torsdag
Fick en genomgång av Erik om hur meddelandeöversättningen i erlang som dom arbetat på fungerar samt diskuterade en del om hur vi tyckte att GUIt ska se ut. Tidsåtgång 2h
##### Fredag
Jobbad på GUIt i javaFX med Erik och Wenting, första två sidorna har någorlunda design medans vi har en idé om hur resten ska se ut. Tidsåtgång 2h
## Vecka 20

##### Måndag
Hade möte om vad vi har gjort, jobbade sedan på GUIt med Erik och Wenting. Vi har gjort en övergripande design på de sidor vi tänkt att använda och har gjort så att man kan gå mellan dom. Nästa steg blir att kunna skicka requests till den del som kommunicerar med servern så att vi kan få svar från databasen. Tidsåtgång 6h 30 min
##### Tisdag

##### Onsdag
Jobbade med GUIt, vårt största fokus ligger på att försöka få till en funktion som autofyller när man skriver i sökrutan. Tidsåtgång 2h 30 min
##### Torsdag
Fortsatte med GUIt, vi jobbade med samma sak som tidigare och lade till en databas med en samling flygplatser och fick autofyllfunktionen att fungera som vi ville. Tidsåtgång 6h 30 min
##### Fredag
Fortsatte med GUIt, jobbade med mer design på GUIt och diskuterade om hur vi låg till på de olika delarna. Vårt nästa mål blir att implementera nätverksmodulen så att vi kan börja kommunicera med de andra delarna. Tidsåtgång 3h
## Vecka 21

##### Måndag
Började med att försöka sy ihop vårt GUI med nätverksadaptern som Lucas skrivit. Vi har gjort så att vi borde kunna ansluta till en server just nu med en ny "anslut till server" startsida. Tidsåtgång 5h 30 min
##### Tisdag
Jobbade med GUIts funktionalitet. Tidsåtgång 2h
##### Onsdag
Jobbade med färgscheman i css så att GUIt ser mycket bättre ut, samt skrev kommentarer på de stället där vi ska skicka meddelanden till servern. Tidsåtgång 6h
##### Torsdag
Började med att skriva på rapporten och gjorde ett skelett för den, samt presentationen. Lade också till i GUIt så att man inte kan söka på flights om man inte har matat in från vart man vill åka, till vart man vill åka, fler än 0 personer och inget valt datum. Tidsåtgång 5h
##### Fredag
Började jobba lite på rapporten och funderade på hur vi ska göra med demonstration. Tidsåtgång 2h

##### Lördag
Jobbade med rapporten till första draften, skrev klart en några delar och fyllde i så mycket jag kunde. Tidsåtgång 6h

##### Söndag
Jobbade med rapport, presentation och fixade till så att vi kan göra en demonstration om hur en komplett bokning kan se ut i vårt system. Tidsåtgång 7h
## Vecka 22

##### Måndag
Jobbade med det sista innan inlämning av rapport. Tidsåtgång 1h
##### Tisdag
Förberedde genrep av slutpresentation. Tidsåtgång 1h
##### Onsdag
Skrivande av rapport och diskussion om utveckling av GUIt. Tidsåtgång 5h 30 min
##### Torsdag
Skrivande och korrigerande av rapport. Tidsåtgång 5h
##### Fredag
Rapporten är i princip klar nu. Ska lägga till meddelandestruktur om vi tycker att det är värt att ta med, men annars inget mer som kan läggas till innan programmet är klart. Tidsåtgång 6h
## Vecka 23

##### Måndag

##### Tisdag

##### Onsdag
Jobbade med att färdigställa allt. Projektet, rapporten, presentationen, peer reviews. Allt som ska vara klart. Tidsåtgång 12h
##### Torsdag

##### Fredag
