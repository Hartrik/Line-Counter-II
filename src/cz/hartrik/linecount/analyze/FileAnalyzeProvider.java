package cz.hartrik.linecount.analyze;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Zprostředkuje analýzu souborů, zamezí opakované analýze stejného souboru.
 *
 * @version 2015-09-02
 * @author Patrik Harag
 */
public abstract class FileAnalyzeProvider {

    private final Set<String> analyzed;

    // konstruktory

    public FileAnalyzeProvider() {
        this.analyzed = new LinkedHashSet<>();
    }

    // abstraktní metody

    protected abstract void consumeLog(String message);

    protected abstract void consumePath(Path path);

    // metody

    public void analyze(Path... paths) {
        if (paths.length != 0)
            analyze(Arrays.asList(paths));
    }

    public void analyze(Collection<Path> paths) {
        explore(paths);
    }

    protected void explore(Collection<Path> paths) {
        for (Path path : paths) {
            try {
                Files.walk(path).forEach(this::explorePath);
            } catch (IOException ex) {
                consumeLog("Chyba při procházení souborového systému - "
                        + path.toString());
            }
        }
    }

    protected void explorePath(Path path) {
        path = path.toAbsolutePath();

        String stringPath = path.toString();
        if (analyzed.contains(stringPath)) {
            consumeLog("Duplikát - " + path.toString());
        } else {
            analyzed.add(stringPath);
            consumePath(path);
        }
    }

    public Set<String> getAnalyzed() {
        return new LinkedHashSet<>(analyzed);
    }

}