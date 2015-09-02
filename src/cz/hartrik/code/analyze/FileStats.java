package cz.hartrik.code.analyze;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Zprostředkuje analýzu souborů, zamezí opakované analýze stejného souboru.
 *
 * @version 2014-08-05
 * @author Patrik Harag
 */
public class FileStats implements IStats {
    
    private final Consumer<Path> analyzer;
    private final Consumer<String> logConsumer;

    private final Set<String> analyzed;
    
    // konstruktory
    
    public FileStats(Consumer<Path> fileAnalyzer) {
        this(fileAnalyzer, string -> {});
    }
    
    public FileStats(Consumer<Path> fileAnalyzer, Consumer<String> logConsumer) {
        this.analyzer = fileAnalyzer;
        this.logConsumer = logConsumer;
        this.analyzed = new LinkedHashSet<>();
    }

    // metody
    
    @Override
    public void analyze(Path... paths) {
        if (paths.length != 0)
            analyze(Arrays.asList(paths));
    }
    
    @Override
    public void analyze(Collection<Path> paths) {
        explore(paths);
    }

    protected void explore(Collection<Path> paths) {
        for (Path path : paths) {
            try {
                Files.walk(path).forEach(this::explorePath);
            } catch (IOException ex) {
                logConsumer.accept(
                        "Chyba při procházení souborového systému - " +
                        path.toString());
            }
        }
    }

    protected void explorePath(Path path) {
        path = path.toAbsolutePath();
        
        String stringPath = path.toString();
        if (analyzed.contains(stringPath)) {
            logConsumer.accept("Duplikát - " + path.toString());
        } else {
            analyzed.add(stringPath);
            analyzer.accept(path);
        }
    }
    
    public Set<String> getAnalyzed() {
        return new LinkedHashSet<>(analyzed);
    }

}