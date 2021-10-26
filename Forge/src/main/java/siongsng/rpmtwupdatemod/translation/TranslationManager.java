package siongsng.rpmtwupdatemod.translation;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class TranslationManager {

    private static final Minecraft mc = Minecraft.getInstance();
    private static final Gson GSON = new Gson();
    private static final Component PROGRESS_TEXT = new TextComponent("翻譯中...").withStyle(ChatFormatting.GRAY);
    private static final Component ERROR_TEXT = new TextComponent("翻譯失敗").withStyle(ChatFormatting.GRAY);
    private static final Component NO_REQUIRED_TEXT = new TextComponent("不需翻譯").withStyle(ChatFormatting.GRAY);
    private static final TranslationManager INSTANCE = new TranslationManager();
    private static final String CashFilePath = System.getProperty("user.home") + "/.rpmtw/translation.json";

    private final Map<SourceLangText, TranslationData> Cash = new HashMap<>();
    private final List<String> PROGRESS = new ArrayList<>();
    private final Translator translator = new Translator();


    public static TranslationManager getInstance() {
        return INSTANCE;
    }

    public void init() {
        readCash();
    }

    public void readCash() {
        try {
            if (Paths.get(CashFilePath).toFile().exists()) {
                JsonObject jo = GSON.fromJson(new FileReader(CashFilePath), JsonObject.class);
                for (Map.Entry<String, JsonElement> lang : jo.entrySet()) {
                    JsonObject je = lang.getValue().getAsJsonObject();
                    for (Map.Entry<String, JsonElement> entry : je.entrySet()) {
                        JsonObject jk = entry.getValue().getAsJsonObject();
                        TranslationData data = new TranslationData();
                        for (Map.Entry<String, JsonElement> langs : jk.entrySet()) {
                            data.addTranslateInfo(langs.getKey(), new TranslationData.TranslationInfo(langs.getValue().getAsString(), null, System.currentTimeMillis()));
                        }
                        Cash.put(new SourceLangText(lang.getKey(), entry.getKey()), data);
                    }
                }
            }
        } catch (Exception e) {
            RpmtwUpdateMod.LOGGER.error(e);
        }
    }

    public void writeCash() {
        try {
            JsonObject jo = new JsonObject();
            mc.submit(() -> {
                for (String lang : Cash.keySet().stream().map(n -> n.langCode).collect(Collectors.toSet())) {
                    jo.add(lang, new JsonObject());
                }
                Cash.forEach((n, m) -> {
                    JsonObject ja = jo.get(n.langCode).getAsJsonObject();
                    JsonObject jk = new JsonObject();
                    m.getAllTranslateText().forEach((l, k) -> {
                        if (k.getError() == null && k.getText() != null)
                            jk.addProperty(l, k.getText());
                    });
                    ja.add(n.text, jk);
                });
            }).get();
            Files.writeString(Paths.get(CashFilePath), jo.toString());
        } catch (Exception e) {
            RpmtwUpdateMod.LOGGER.error(e);
        }
    }

    public List<Component> createToolTip(ItemStack item) {
        Component text = item.getDisplayName();
        String key = item.getDescriptionId();
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
                List<Component> ary = new ArrayList<>();
                ary.add(new TextComponent("機器翻譯結果: " + info.getText()).withStyle(ChatFormatting.GRAY));
                return ary;
            }

            return Collections.singletonList(ERROR_TEXT);
        }

        if (PROGRESS.contains(str)) {
            MutableComponent c = PROGRESS_TEXT.copy();
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
