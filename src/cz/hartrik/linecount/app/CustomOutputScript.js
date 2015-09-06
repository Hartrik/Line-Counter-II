/* global List<DataTypeCode> data */

data.forEach(function(type) {
    var fileType = type.getFileType();

    if (fileType.isTextDocument())
        print(fileType.getName() + " - " + type.getLinesTotal());
});


/*
 * Useful methods:
 * ====================================
 * DataTypeCode type = ...
 *
 * type.getFileType().name()
 * type.getFileType().isTextDocument()
 * type.getFileType().isSourceCode()
 *
 * type.getFiles()
 * type.getSizeTotal()
 *
 * type.getLinesCode()
 * type.getLinesComment()
 * type.getLinesEmpty()
 * type.getLinesTotal()
 *
 * type.getCharsComment()
 * type.getCharsIndent()
 * type.getCharsWhitespace()
 * type.getCharsTotal()
 *
 */