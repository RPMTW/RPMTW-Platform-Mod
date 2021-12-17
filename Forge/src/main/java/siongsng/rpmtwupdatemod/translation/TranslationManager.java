package siongsng.rpmtwupdatemod.translation;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class TranslationManager {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final ITextComponent PROGRESS_TEXT = new TextComponentString("翻譯中...").setStyle(new Style().setColor(TextFormatting.GRAY));
    private static final ITextComponent ERROR_TEXT = new TextComponentString("翻譯失敗").setStyle(new Style().setColor(TextFormatting.GRAY));
    private static final ITextComponent NO_REQUIRED_TEXT = new TextComponentString("不需翻譯").setStyle(new Style().setColor(TextFormatting.GRAY));
    private static final TranslationManager INSTANCE = new TranslationManager();

    private final Map<SourceLangText, TranslationData> Cash = new HashMap<>();
    private final List<String> PROGRESS = new ArrayList<>();
    private final Translator translator = new Translator();


    public static TranslationManager getInstance() {
        return INSTANCE;
    }


    public List<ITextComponent> createToolTip(String source) {
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
                List<ITextComponent> ary = new ArrayList<>();
                ary.add(new TextComponentString("機器翻譯結果: " + info.getText()).setStyle(new Style().setColor(TextFormatting.GRAY)));
                return ary;
            }

            return Collections.singletonList(ERROR_TEXT);
        }

        if (PROGRESS.contains(source)) {
            ITextComponent c = PROGRESS_TEXT.createCopy();
            for (int i = 0; i < (System.currentTimeMillis() % 400) / 100; i++) {
                c.appendText(".");
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
                mc.addScheduledTask(() -> {
                    Cash.put(new SourceLangText(srcLang, text), finalData);
                }).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            PROGRESS.remove(text);
        }
    }

    public static class SourceLangText {
        private final String langCode;
        private final String text;

        public SourceLangText(String langCode, String text) {
            this.langCode = langCode;
            this.text = text;
        }

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
