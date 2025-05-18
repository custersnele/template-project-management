# Opdracht Multithreading
In deze oefening bouwen we een multithreaded applicatie die projecten aanmaakt, valideert, goedkeurt, kosten registreert en informatie opvraagt. 
We gebruiken 2 externe systemen: PostgreSQL en MongoDB. Deze kan je aanmaken via de docker file.

Je leert:

- REST-endpoints opzetten en servlets gebruiken
- Werken met Java threads en concurrerende datastructuren zoals BlockingQueue
- Asynchrone validaties uitvoeren
- Resources synchroniseren (bijv. budgetten)
- Werken met properties-bestanden
- Integreren met twee databases: PostgreSQL en MongoDB
- Logging gebruiken voor debugging en transparantie


Het doel is een robuuste, uitbreidbare en realistisch werkende applicatie te bouwen.

## 1. Projecten aanmaken
   Implementeer een methode die op basis van een **CreateProjectRequest** een nieuw project aanmaakt.

- Het project krijgt initieel de status NEW
- Het project wordt opgeslagen in een PostgreSQL-database
- Daarna wordt het project toegevoegd aan een verwerkingswachtrij (BlockingPriorityQueue)

## 2. Projecten goedkeuren

Gebruik **3 threads** om projecten uit de wachtrij te halen en te valideren via **ValidationRule**-implementaties.
Indien een validatie niet voldaan is, wordt een **InvalidProjectException** gegooid met een duidelijke foutboodschap.

Validatie gebeurt asynchroon en parallel. 
Voeg de volgende regels toe:
- **BudgetValidationRule**: Als description de tekst "POC" bevat moet het budget ≥ €300, anders moet het budget ≥ €500.
- **DueDateValidationRule**: De dueDate ligt minstens 5 minuten in de toekomst.
- **LongValidationRuleThatFailsSometimes**: Deze validatie duurt 10 seconden (gebruik Thread.sleep). In 10%
van de gevallen faalt deze taak.

Indien 1 van de validaties niet voldaan is, krijgt het project de status **REJECTED**.

Als alle validaties slagen wordt gecontroleerd of het budget van het project nog past binnen het maandelijks budget. 
Hiervoor wordt het bestand **budgets.properties** ingelezen wanneer de applicatie wordt opgestart. 
Aan de hand van de due date van een project wordt de maand bepaald. 

Als de dueDate van het project 03-09-2025 12:35 en het budget €2500 is, dan tellen we het volledige budget in de maand september. 
Indien we in september al voor €3000 projecten hebben goedgekeurd, en het totale budget is €4500, dan wordt het nieuwe project niet goedgekeurd en krijgt het project de status REJECTED. Indien er wel nog voldoende budget is, krijgt het project de status approved.

Als er voldoende budget is, krijgt het project de status **APPROVED**. Anders krijgt het project de
status **REJECTED**.

Zorg voor voldoende logging om het validatieproces te volgen.

## 3. Simulatie van projectaanmaak

Maak een zelfstandige Java-applicatie die via meerdere threads projecten aanmaakt:

- Elk thread maakt 10 projecten aan
- De description van een nieuw project is: threadnaam + volgnummer
- De dueDate van een project wordt bepaald via het bestand **simulation/projects.properties**

Voorbeeldbestand:

```
thread1.time=MINUTES_10
thread2.time=MONTHS_1
thread3.time=DAYS_5
```

Voor thread1 wordt het eerste project 10 minuten in de toekomst geplaatst, en elk volgend project telkens 10 minuten later.

De threads schrijven de UUIDs van de aangemaakte projecten naar het bestand simulation/ids.txt.

## 4. Servlet: overzicht per maand

Ontwikkel een servlet met parameters month en year.
Geef weer:
- Beschikbaar budget in die maand
- Totale goedgekeurde budget
- Projecten per status (NEW, APPROVED, REJECTED, CLOSED)

![Screenshot 2025-05-18 at 14.22.10.png](images/Screenshot%202025-05-18%20at%2014.22.10.png)

Extra: toon bij REJECTED ook de reden van afkeuring

## 5. Kosten bij projecten registreren

Voeg een endpoint toe voor het registreren van kosten aan een project.

![Screenshot 2025-05-17 at 14.32.11.png](images/Screenshot%202025-05-17%20at%2014.32.11.png)

Regels:

- Alleen voor projecten met status APPROVED
- Kosten worden opgeslagen in **MongoDB**
- Minimumbedrag: €0.10
- Omschrijving (description) is verplicht
- Totale kosten mogen het projectbudget niet overschrijden
- 
## 6. Thread voor kostenregistratie

Ontwikkel een thread die willekeurige kosten toevoegt aan projecten.
De project IDs lees je uit het bestand: simulation/ids.txt. Voor iedere project ID start je een nieuwe
thread. Elke thread voert 10 kostenregistraties uit, de bedragen zijn willekeurig. 
Maak gebruik van de hulpmethoden in CreateCostsTask. 


## 7. Endpoint: projectdetails opvragen

![Screenshot 2025-05-18 at 13.40.00.png](images/Screenshot%202025-05-18%20at%2013.40.00.png)

Maak een endpoint om de details van een project op te vragen. 
Zorg dat tegelijkertijd de projectgegevens uit PostgreSQL én de kosten uit MongoDB opgehaald worden.
Combineer alles in één ProjectDetailsResponse.

- dueDate formaat in response: "yyyy-MM-dd HH:mm:ss"

Extra: Toon ook het resterende budget van het project

## 8. Projecten afsluiten

Elke minuut moet een achtergrondtaak alle APPROVED projecten waarvan de dueDate is verstreken 
updaten naar status CLOSED.

