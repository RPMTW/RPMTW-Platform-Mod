package siongsng.rpmtwupdatemod.translation;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class TranslationManager {

    private static final Minecraft mc = Minecraft.getInstance();
    private static final Component PROGRESS_TEXT = new TextComponent("翻譯中...").withStyle(ChatFormatting.GRAY);
    private static final Component ERROR_TEXT = new TextComponent("翻譯失敗").withStyle(ChatFormatting.GRAY);
    private static final Component NO_REQUIRED_TEXT = new TextComponent("不需翻譯").withStyle(ChatFormatting.GRAY);
    private static final TranslationManager INSTANCE = new TranslationManager();

    private final Map<SourceLangText, TranslationData> Cash = new HashMap<>();
    private final List<String> PROGRESS = new ArrayList<>();
    private final Translator translator = new Translator();


    public static TranslationManager getInstance() {
        return INSTANCE;
    }

    public List<Component> createToolTip(String source) {
        String lang = "en";

        if (currentLang().equals(lang))
            return Collections.singletonList(NO_REQUIRED_TEXT);

        SourceLangText langText = new SourceLangText(lang, source);

        if (Cash.containsKey(langText) && Cash.get(langText).isTranslated(currentLang())) {
            TranslationData.TranslationInfo info = Cash.get(langText).getTranslateInfo(currentLang());
            if (info.getError() != null)
                return Collections.singletonList(ERROR_TEXT);

            if (source.equals(info.getText()))
                return Collections.singletonList(NO_REQUIRED_TEXT);

            if (info.getText() != null) {
                List<Component> ary = new ArrayList<>();
                ary.add(new TextComponent("機器翻譯結果: " + info.getText()).withStyle(ChatFormatting.GRAY));
                return ary;
            }

            return Collections.singletonList(ERROR_TEXT);
        }

        if (PROGRESS.contains(source)) {
            MutableComponent c = PROGRESS_TEXT.copy();
            for (int i = 0; i < (System.currentTimeMillis() % 400) / 100; i++) {
                c.append(".");
            }
            return Collections.singletonList(c);
        }
        PROGRESS.add(source);
        TranslateThread tt = new TranslateThread(source, lang, currentLang());
        tt.start();

        return Collections.singletonList(ERROR_TEXT);
    }

    public String currentLang() {
        return "zh-TW";
    }

    public record SourceLangText(String langCode, String text) {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SourceLangText that = (SourceLangText) o;
            return Objects.equals(langCode, that.langCode) && Objects.equals(text, that.text);
        }

        @Override
        public int hashCode() {
            return Objects.hash(langCode, text);
        }
    }

    private class TranslateThread extends Thread {
        private final String text;
        private final String srcLang;
        private final String langCode;

        public TranslateThread(String text, String srcLang, String langCode) {
            this.text = text;
            this.srcLang = srcLang;
            this.langCode = langCode;
            this.setName("Translate Thread");
        }

        @Override
        public void run() {
            TranslationData data = Cash.get(new SourceLangText(srcLang, text));
            if (data == null)
                data = new TranslationData();

            TranslationData.TranslationInfo info;

            try {
                String str = translator.translate(text);
                info = new TranslationData.TranslationInfo(str, null, System.currentTimeMillis());
            } catch (Exception ex) {
                ex.printStackTrace();
                info = new TranslationData.TranslationInfo(null, ex, System.currentTimeMillis());
            }
            data.addTranslateInfo(langCode, info);

            TranslationData finalData = data;
            try {
                mc.submit(() -> {
                    Cash.put(new SourceLangText(srcLang, text), finalData);
                }).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            PROGRESS.remove(text);
        }
    }
}
