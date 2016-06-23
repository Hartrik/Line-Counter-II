package cz.hartrik.linecount.analyze;

import cz.hartrik.common.reflect.StopWatch;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Zatímco {@link LineCounter} provádí jen analýzu jednotlivých souborů,
 * tato třída je určena pro dávkové zpracování.
 * Rekurzivně prochází složky a umožňuje filtrování, do logu přidává souhrnné
 * statistiky.
 *
 * @version 2016-05-18
 * @author Patrik Harag
 */
public class LineCountProvider {

    private final ResourceBundle rb;

    private Predicate<Path> filter = (p) -> true;
    private Consumer<Path> onAnalyzed = (p) -> {};

    public LineCountProvider(ResourceBundle rb) {
        this.rb = rb;
    }

    /**
     * Analyzuje soubory a podrobnosti vypíše do logu.
     *
     * @param paths cesty k souborům a složkám, budou rekurzivně prohledány
     * @param logConsumer přebírá logy
     * @param onFiltered zavolán po provedení filtrace souborů
     * @return statistiky
     */
    public Map<FileType, DataTypeCode> process(Collection<Path> paths,
            Consumer<String> logConsumer, Consumer<FileFilter> onFiltered) {

        FileFilter fileFilter = initFilter(logConsumer);
        Collection<Path> filteredPaths = fileFilter.filter(paths);

        onFiltered.accept(fileFilter);

        LineCounter counter = new LineCounter(logConsumer, rb);

        StopWatch stopWatch = StopWatch.measure(() -> {
            for (Path path : filteredPaths) {
                counter.analyze(path);
                onAnalyzed.accept(path);
            }
        });

        logConsumer.accept(rb.getString("log/separator"));

        String filesTotalFormat = rb.getString("log/files");
        int filesTotal = fileFilter.getPassed()+ fileFilter.getFailed();
        logConsumer.accept(String.format(filesTotalFormat, filesTotal));

        String failedFormat = rb.getString("log/files-filtered");
        logConsumer.accept(String.format(failedFormat, fileFilter.getFailed()));

        String passedFormat = rb.getString("log/files-analyzed");
        logConsumer.accept(String.format(passedFormat, fileFilter.getPassed()));

        String tFormat = rb.getString("log/time");
        logConsumer.accept(String.format(tFormat, stopWatch.getMillis()));

        return counter.getStats();
    }

    protected FileFilter initFilter(Consumer<String> logConsumer) {
        String format = rb.getString("log/not-exists");
        FileFilter fileFilter = new FileFilter(
                filter,
                (p, e) -> logConsumer.accept(String.format(format, p)));

        return fileFilter;
    }

    /**
     * Nastaví posluchače, který je volán po každém zpracovaném souboru
     * (úspěšně i neúspěšně).
     *
     * @param onAnalyzed posluchač
     */
    public void setOnAnalyzed(Consumer<Path> onAnalyzed) {
        this.onAnalyzed = onAnalyzed;
    }

    /**
     * Nastaví filtr. Co neprojde filtrem, nebude dále zpracováváno.
     *
     * @param filter filtr
     */
    public void setFilter(Predicate<Path> filter) {
        this.filter = filter;
    }

}