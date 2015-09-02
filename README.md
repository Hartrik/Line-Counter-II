# Počítač řádků II

Počítač řádků je jednoduchý program, který Vám pomůže zjistit přibližnou velikost Vašeho projektu.

Cesty k souborům mohou být pohodlně přidány přetažením do textové oblasti. Cesty mohou být relativní - vůči adresáři, ve kterém byl program spuštěn. Adresáře jsou procházeny rekurzivně.

Podporované jazyky: C, C++, C#, Clojure, CSS, Erlang, FXML, Groovy, HTML, Java, JavaScript, Kotlin, Lisp, Lua, Objective-C, Pascal, PHP, Python, Ruby, Scala, VBScript, Visual Basic, XML

## Popis sloupců

1. **Typ** – typ souboru nebo programovací jazyk
2. **Soubory** – počet analyzovaných souborů. Pokud dojde k chybě při čtení, tak se soubor nezapočítává.
3. **Velikost v bajtech** – souhrnná velikost analyzovaných souborů v bajtech
4. **Řádky**
  - **kód** – počet řádků s kódem (= *celkem* - *komentáře* - *prázdné*)
  - **komentáře** – počet řádek zabírajících komentáře
  - **prázdné** – počet řádek obsahujících jen bílé znaky
  - **celkem** – součet tří předcházejících
5. **Znaky**
  - **komentáře** – počet znaků uvnitř komentářů (včetně whitespace)
  - **whitespace - odsazení** – počet mezer nebo tabulátorů k prvnímu znaku
  - **whitespace - celkem** – součet veškerých whitespace
  - **celkem** – součet veškerých znaků (včetně whitespace)

*Tip: Kliknutím do záhlaví se obsah seřadí podle hodnot ve sloupce.*

## Filtrování souborů

Dialogové okno pro filtrování souborů je možné vyvolat kliknutím na tlačítko s ikonou filtru (po pravé straně od vstupního textového pole).

Regulární výraz může být aplikován buď jen na název souboru (např.: `soubor.txt`) nebo na absolutní cestu k souboru (např.: `C:\Dokumenty\soubor.txt`).

## Mějte na paměti

- Textové dokumenty jsou načítány s kódováním UTF-8.
- Parser nepodporuje vnořené komentáře. (Ve většině jazyků stejně nejsou.)
- Není prováděna syntaktická analýza kódu, a proto může v některých velice specifických případech dojít k nesprávnému započítání komentáře.

**Ke spuštění je vyžadována Java 8.** Program se neinstaluje ani v počítači nezanechává žádná data.

Při psaní jsem hleděl na objektový návrh a čistý kód. Používal jsem nejnovější knihovny a rozhraní (JavaFX, NIO, Stream API aj.).