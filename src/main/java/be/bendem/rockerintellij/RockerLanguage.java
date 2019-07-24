package be.bendem.rockerintellij;

import com.intellij.lang.Language;

public class RockerLanguage extends Language {
    public static final RockerLanguage INSTANCE = new RockerLanguage();

    private RockerLanguage() {
        super("Rocker");
    }
}
