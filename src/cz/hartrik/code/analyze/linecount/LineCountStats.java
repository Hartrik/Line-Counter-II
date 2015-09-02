
package cz.hartrik.code.analyze.linecount;

import cz.hartrik.code.analyze.FileStats;
import cz.hartrik.code.analyze.FileType;
import cz.hartrik.code.analyze.IStats;
import cz.hartrik.util.io.NioUtil;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Vytváří statistiky počtu řádků, znaků atd...
 * 
 * @version 2014-08-06
 * @author Patrik Harag
 */
public class LineCountStats implements IStats {
    
    protected final Consumer<String> logConsumer;
    protected final FileStats fileStats;
    protected final Map<FileType, DataTypeSource> stats = new LinkedHashMap<>();
    
    protected final UnknownFileAnalyzer unknownFileAnalyzer;
    protected final TextFileAnalyzer textFileAnalyzer;
    protected final SourceFileAnalyzer sourceFileAnalyzer;
    
    public LineCountStats() {
        this(string -> {});
    }
    
    public LineCountStats(Consumer<String> logConsumer) {
        this.logConsumer = logConsumer;
        this.fileStats = new FileStats(this::consume, logConsumer);
        
        this.unknownFileAnalyzer = new UnknownFileAnalyzer();
        this.textFileAnalyzer    = new TextFileAnalyzer();
        this.sourceFileAnalyzer  = new SourceFileAnalyzer();
    }

    // instanční metody
    
    @Override
    public void analyze(Path... paths) {
        fileStats.analyze(paths);
    }

    @Override
    public void analyze(Collection<Path> paths) {
        fileStats.analyze(paths);
    }
    
    protected void consume(Path path) {
        if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {
            
            if (!Files.isReadable(path)) {
                logConsumer.accept("Soubor není určen ke čtení - " + path.toString());
                return;
            }
            
            String extension = NioUtil.getExtension(path);
            FileType type = FileType.getByExtension(extension);
            
            if (type == FileType.OTHER)
                logConsumer.accept("Soubor neznámého typu - " + path.toString());
            
            DataTypeSource typeData = getData(type);
            chooseFileAnalyzer(path, typeData);
            
        } else if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            
        } else if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            logConsumer.accept(
                    "Neexistující složka/soubor - " + path.toString());
        }
    }
    
    protected void chooseFileAnalyzer(Path path, DataTypeSource typeData) {
        FileType fileType = typeData.getFileType();
        
        try {
            if (fileType.isSourceCode())
                sourceFileAnalyzer.analyze(path, typeData);
            else if (fileType.isTextDocument())
                textFileAnalyzer.analyze(path, typeData);
            else
                unknownFileAnalyzer.analyze(path, typeData);
            
        } catch (UncheckedIOException e) {
            logConsumer.accept("Nepodporované kódování - " + path.toString());
        } catch (IOException e) {
            logConsumer.accept("Chyba při čtení souboru - " + path.toString());
        }
    }
    
    protected DataTypeSource getData(FileType type) {
        if (!stats.containsKey(type)) {
            // nová položka
            DataTypeSource typeData = new DataTypeSource(type);
            stats.put(type, typeData);
            return typeData;

        } else {
            // stávající položka
            return stats.get(type);
        }
    }

    // gettery
    
    public Map<FileType, DataTypeSource> getStats() {
        return stats;
    }
    
    public Set<String> getAnalyzed() {
        return fileStats.getAnalyzed();
    }
    
}