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


    public static class TranslationInfo {
        private final String text;
        private final Exception error;
        private final long time;

        public TranslationInfo(String text, Exception error, long time) {
            this.text = text;
            this.error = error;
            this.time = time;
        }

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
