
package cz.hartrik.code.analyze;

import cz.hartrik.common.Pair;
import java.util.ArrayList;
import java.util.List;

/**
 * Parsuje komentáře ze zdrojového kódu.
 * 
 * @version 2014-08-05
 * @author Patrik Harag
 */
public class CommentParser {
    
    protected final Pair<String, String>[] comments;
    private final int numberOfComments;
    
    protected boolean isContinue;
    
    /**
     * Vytvoří novou instanci.
     * 
     * @param commentStyle typ komentářů
     */
    public CommentParser(CommentStyle commentStyle) {
        this.comments = commentStyle.getComments();
        this.numberOfComments = comments.length;
    }
    
    /**
     * Analyzuje zdrojový kód a vrátí seznam komentářů v něj obsažených.
     * Nepodporuje vnořené komentáře (Pascal).
     * 
     * @param source zdrojový kód
     * @return seznam obsahů komentářů
     */
    public List<String> analyze(String source) {
        return analyze(new StringBuilder(source));
    }
    
    /**
     * Analyzuje zdrojový kód a vrátí seznam komentářů v něj obsažených.
     * Nepodporuje vnořené komentáře (Pascal).
     * 
     * @param source zdrojový kód
     * @return seznam obsahů komentářů
     */
    public List<String> analyze(StringBuilder source) {
        isContinue = false;
        List<String> content = new ArrayList<>();
        
        while (true) {
            final int[] next = getNext(source);
            
            if (next[0] > -1) {
                final Pair<String, String> comment = comments[next[1]];
                source.delete(0, next[0] + comment.getFirst().length());
                
                int secondPos = source.indexOf(comment.getSecond());
                int to = secondPos == -1 ? source.length() : secondPos;
                
                content.add(source.substring(0, to));
                source.delete(0, secondPos == -1 ? to
                        : to + comment.getSecond().length());
                
                if (secondPos == -1 && !comment.getSecond().equals("\n")) {
                    // konec zdrojáku, ale blokový komentář pokračuje dál
                    this.isContinue = true;
                    return content;
                }
            } else {
                // nenalezeny komentáře
                return content;
            }
        }
    }
    
    private int[] getNext(StringBuilder source) {
        int[] indexOf = new int[numberOfComments];
        
        for (int i = 0; i < numberOfComments; i++)
            indexOf[i] = source.indexOf(comments[i].getFirst());
        
        int min = Integer.MAX_VALUE; int minIndex = -1;
        
        for (int i = 0; i < numberOfComments; i++) {
            int next = indexOf[i];
            if (next != -1 && next < min) {
                min = next;
                minIndex = i;
                if (next == 0) break; // nejlepší možný výsledek
            }
        }
        return new int[] { minIndex == -1 ? -1 : min, minIndex };
    }

    /**
     * Informuje o tom, zda by poslední komentář pokračoval i za konec řetězce.
     * Tedy pokud nebylo nalezeno zakončení pro poslední blokový komentář.
     * 
     * @return komentář by pokračoval
     */
    public boolean isContinue() {
        return isContinue;
    }
    
}