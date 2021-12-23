package siongsng.rpmtwupdatemod.translation;

import java.util.HashMap;
import java.util.Map;

public class TranslationData {
    private final Map<String, TranslationInfo> TRANS_DATA = new HashMap<>();

    public TranslationInfo getTranslateInfo(String langCode) {
        return TRANS_DATA.get(langCode);
    }

    public void addTranslateInfo(String langCode, TranslationInfo text) {
        TRANS_DATA.put(langCode, text);
    }

    public boolean isTranslated(String langCode) {

        return TRANS_DATA.containsKey(langCode);
    }

    public record TranslationInfo(String text, Exception error, long time) {

        public String getText() {
            return text;
        }

        public Exception getError() {
            return error;
        }

        public long getTime() {
            return time;
        }
    }

}
