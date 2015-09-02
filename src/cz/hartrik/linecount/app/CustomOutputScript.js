// ArrayList<DataTypeCode> data

print("Typ | Soubory | Velikost | Řádky - kód | Řádky - komentáře "
        + "| Řádky - prázdné | Řádky - celkem | Znaky - komentáře "
        + "| Znaky - odsazení | Znaky - ws | Znaky - celkem |");

function countCode(type) {
    if (type.getFileType().isSourceCode())
        return type.getLinesTotal()
                - (type.getLinesComment() + type.getLinesEmpty());
    else return 0;
}

for each (var type in data) {
    print(type.getFileType().getName()  + " | "
            + type.getFiles()           + " | "
            + type.getSizeTotal()       + " | "

            + countCode(type)           + " | "
            + type.getLinesComment()    + " | "
            + type.getLinesEmpty()      + " | "
            + type.getLinesTotal()      + " | "

            + type.getCharsComment()    + " | "
            + type.getCharsIndent()     + " | "
            + type.getCharsWhitespace() + " | "
            + type.getCharsTotal()      + " | "
    );
}