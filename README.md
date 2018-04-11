
#### Aprašymas

JPA Egzamino užduotis.

#### Vertinimas

Pažymį sudarys testų rezultatai ir kodo tvarka.
Nepamiškite, kad svarbiau yra veikianti implementacija,
nei gražus, bet neveikiantis kodas.
Testų pabaigoje yra atspausdinamas galimas pažymys:

```
Score:         26/26
Avarage grade: 10.00
Grade:         10
```

Nusirašinėti, dalintis patarimais,
kopijuoti iš kolegų github'o yra draudžiama.
Destytojų akis yra pakankamai įgudusi pamatyti copy-paste.

Paleidimas iš komandinės eilutės:

```
$ mvn clean spring-boot:run
```

#### Užduotis 1

Jums reikės pabaigti implementaciją pakete `ex01simple`

Modeliuojame automobilį (Car).

Automobilis aprašomas laukais `model` ir `manufactureDate`.
Kiekvienas automobilis turi unikalų vidinį ID.

Naudodamiesi JPA galimybėmis suprogramuokite standartines
CRUD (create, read, update, delete) operacijas `CarRepository` klasėje.

Vertinant bus kviečiami `CarRepository` metodai ir tikrinama ar jie veikia pagal specifikaciją.

#### Užduotis 2

Modeliuosime labai supaprastintą bibliotekos modelį.

##### Biblioteka - Library

Biblioteką modeliuoja `Library` klasė.
Visos bibliotekai priklausančios knygos yra saugomos sąraše `Library.books`
Visi bibliotekai priklausantys skaitytojai yra saugomi sąraše `Library.readers`
Kiekvina biblioteka turi turėti unikalų vidinį ID

##### Knyga - Book

Knygą aprašo pavadinimas ir autorius. Kiekviena knyga turi unikalų vidinį ID.
Skaitytojai, kurie yra pasiėmę knygą skaitymui, yra `bookReaders` sąraše.

##### Skaitytojas - LibraryReader

Skaitytoją aprašo vardas ir pavardė. Kiekvienas skaitytojas turi unikalų vidinį ID.
`borrowedBooks` sąraše saugomos knygos, kurias skaitytojas pasiėmęs skaityti.
`addresses` laukas saugo žinomus skaitytojų adresus

##### Implementacijos žingsniai

Galite vadovautis šiais žingsniais tačiau tai nėra privaloma.

1. `LibraryRepository.saveOrUpdate` sukuria naują arba atnaujina `Library`.
Kol kas sąryšių galite nekurti. Kursime juos vėliau.

2. `LibraryRepository.find` suranda `Library` pagal vidinį ID

3. `LibraryReaderRepository.saveOrUpdate` sukuria naują arba atnaujina `LibraryReader`.
Kol kas sąryšių galite nekurti. Kursime juos vėliau.

4. `LibraryReaderRepository.find` suranda `LibraryReader` pagal vidinį ID

5. `BookRepository.saveOrUpdate` sukuria naują arba atnaujina `Book`.
Kol kas sąryšių galite nekurti. Kursime juos vėliau.

6. `BookRepository.find` suranda `Book` pagal vidinį ID

7. `LibraryReader.addresses` sąryšis. Užtikrinkite, kad
kuriant arba atnaujinant skaitytoją, taip pat būtų atnaujinamai jo adresai.
Adresų atskirai neįvedinėsime.

8. `Library.readers`
Šiuo atveju yra svarbu, jog kuriant ar atnaujinant bibliotekos informaciją,
knygų ir skaitytojų informacija nebūtų atnaujinama.
Knygas ir skaitytojus kursime atskirai.
Kuriant/atnaujinant biblioteką perduoti žmonių/ knygų duomenys turi būti ingnoruojami ir neišsaugomi duomenų bazėje.
Šie sąryšiai bus naudojami tam, kad susietume skaitytojus su biblioteka, tačiau kuriant biblioteką skaitytojai nesikurs.

9. `Library.books`
Analogiška situacija kaip ir su `Library.readers`. Turi tenkinti tuos pačius reikalavimus.

10. `LibraryReader.borrowedBooks` sąryšis parodo kokias knygas skaito skaitytojas.
Saugant skaitytoją pačių knygų neturi iššsaugoti. Turi tik iššsaugoti sąryšį.

11. `Book.bookReaders` abipusis sąryšis su `LibraryReader.borrowedBooks`.
Atitinkamai pasako kokie skaitytojai yra pasiėmę šią knygą.

12. `Library.addBorrowedBook` methodas skirtas tvarkingai pridėtį skaitomą knyga.
Sutvarko abipusio sąryšio būseną.



Sėkmės darbe. Nebijokite - kodo reikės mažiau nei aš čia parašiau teksto :)

# Papildoma užduotis (2 balai prie galutinio pažymio)

Turbūt pastebėjote, jog `saveOrUpdate` ir `find` metodai yra identiški skirtingiems entičiams.
Sugalvokite, kaip suprogramuoti šiuos metodus vienoje klasėje ir pernaudoti visose repositorijose.
