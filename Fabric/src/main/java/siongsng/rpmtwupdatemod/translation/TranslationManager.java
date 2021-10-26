package siongsng.rpmtwupdatemod.translation;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class TranslationManager {

    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static final Text PROGRESS_TEXT = new LiteralText("翻譯中...").formatted(Formatting.GRAY);
    private static final Text ERROR_TEXT = new LiteralText("翻譯失敗").formatted(Formatting.GRAY);
    private static final Text NO_REQUIRED_TEXT = new LiteralText("不需翻譯").formatted(Formatting.GRAY);
    private static final TranslationManager INSTANCE = new TranslationManager();

    private final Map<SourceLangText, TranslationData> Cash = new HashMap<>();
    private final List<String> PROGRESS = new ArrayList<>();
    private final Translator translator = new Translator();


    public static TranslationManager getInstance() {
        return INSTANCE;
    }

    public List<Text> createToolTip(ItemStack item) {
        Text text = item.getName();
        String key = item.getTranslationKey();
        String str = Handler.getNoLocalizedMap().containsKey(key) ? Handler.getNoLocalizedMap().get(key) : text.getString();
        String lang = "en";

        if (currentLang().equals(lang))
            return Collections.singletonList(NO_REQUIRED_TEXT);

        SourceLangText langText = new SourceLangText(lang, str);

        if (Cash.containsKey(langText) && Cash.get(langText).isTranslated(currentLang())) {
            TranslationData.TranslationInfo info = Cash.get(langText).getTranslateInfo(currentLang());
            if (info.getError() != null)
                return Collections.singletonList(ERROR_TEXT);

            if (str.equals(info.getText()))
                return Collections.singletonList(NO_REQUIRED_TEXT);

            if (info.getText() != null) {
                List<Text> ary = new ArrayList<>();
                ary.add(new LiteralText("機器翻譯結果: " + info.getText()).formatted(Formatting.GRAY));
                return ary;
            }


            return Collections.singletonList(ERROR_TEXT);
        }

        if (PROGRESS.contains(str)) {
            MutableText c = PROGRESS_TEXT.copy();
            for (int i = 0; i < (System.currentTimeMillis() % 400) / 100; i++) {
                c.append(".");
            }
            return Collections.singletonList(c);
        }
        PROGRESS.add(str);
        TranslateThread tt = new TranslateThread(str, lang, currentLang());
        tt.start();

        return Collections.singletonList(ERROR_TEXT);
    }

    public String currentLang() {
        return "zh-TW";
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
}
