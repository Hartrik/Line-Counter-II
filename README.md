# Počítač řádků II

*English localization is not available.*

Počítač řádků je jednoduchý program, který Vám pomůže zjistit přibližnou velikost Vašeho projektu.

Cesty k souborům mohou být pohodlně přidány přetažením do textové oblasti. Cesty mohou být relativní - vůči adresáři, ve kterém byl program spuštěn. Adresáře jsou procházeny rekurzivně.

Podporované jazyky: C, C++, C#, Clojure, CSS, Erlang, Groovy, HTML, Java, JavaScript, Lua, Objective-C, Pascal, PHP, Python, Scala, VBScript, Visual Basic, XML (FXML, XSL, XSD)

- [Návod jak přidat vlastní typy souborů](https://github.com/Hartrik/Line-Counter-II/wiki/P%C5%99id%C3%A1n%C3%AD-vlastn%C3%ADch-typ%C5%AF-soubor%C5%AF)

## Zjišťované informace

1. **Typ** – typ souboru nebo programovací jazyk
2. **Soubory** – počet analyzovaných souborů. Pokud dojde k chybě při čtení, tak se soubor nezapočítává.
3. **Velikost v bajtech** – souhrnná velikost analyzovaných souborů v bajtech
4. **Řádky**
  - **kód** – počet řádků s kódem (= *celkem* - *komentáře* - *prázdné*)
  - **komentáře** – počet řádek zabírajících komentáře (nepočítají se prázdné řádky uvnitř blokových komentářů)
  - **prázdné** – počet řádek obsahujících jen bílé znaky
  - **celkem** – součet tří předcházejících
5. **Znaky**
  - **komentáře** – počet znaků uvnitř komentářů (včetně whitespace)
  - **odsazení** – počet mezer nebo tabulátorů k prvnímu znaku (počítá se i uvnitř blokových komentářů)
  - **whitespace** – součet veškerých "bílých znaků"
  - **celkem** – součet veškerých znaků (včetně whitespace)

![Preview](https://cloud.githubusercontent.com/assets/6131815/9882226/de6347f6-5bd3-11e5-8f9e-1fe4f89ab6d7.png)

*Tip: Kliknutím do záhlaví se obsah seřadí podle hodnot ve sloupce.*

## Filtrování souborů

Dialogové okno pro filtrování souborů je možné vyvolat kliknutím na tlačítko s ikonou filtru (po pravé straně od vstupního textového pole).

Regulární výraz může být aplikován buď jen na název souboru (např.: `soubor.txt`) nebo na absolutní cestu k souboru (např.: `C:\Dokumenty\soubor.txt`).

## Mějte na paměti

- Textové dokumenty jsou načítány s kódováním UTF-8, Windows-1250 nebo ISO 8859-2.
- Parser nepodporuje vnořené komentáře. (Ve většině jazyků stejně nejsou.)

**Ke spuštění je vyžadována minimálně Java 8u40.** Program se neinstaluje ani v počítači nezanechává žádná data.

Při psaní jsem hleděl na objektový návrh a čistý kód. Používal jsem nejnovější knihovny a rozhraní (JavaFX, NIO, Stream API aj.).
