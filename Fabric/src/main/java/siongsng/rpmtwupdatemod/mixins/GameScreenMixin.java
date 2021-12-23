package siongsng.rpmtwupdatemod.mixins;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;

@Mixin(Screen.class)
public abstract class GameScreenMixin {
    @Shadow
    protected abstract <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement);

    @Shadow
    public int width;
    @Shadow
    public int height;

    @Inject(method = "render", at = @At("RETURN"))
    private void renderOverlay(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Screen guiScreen = (Screen) (Object) this;

        if (guiScreen instanceof GameMenuScreen && RPMTWConfig.getConfig().contributorButton && RPMTWConfig.getConfig().isChinese) {
            TexturedButtonWidget rpmtwLogo = new TexturedButtonWidget(width / 2 - 98, height / 4 + 145, 15, 15, 0, 0, 0, new Identifier(RpmtwUpdateMod.Mod_ID, "textures/rpmtw_logo.png"), 15, 15, (buttonWidget) -> {
            });
            ButtonWidget buttonWidget = new ButtonWidget(width / 2 - 102, height / 4 + 142, 204, 20, new LiteralText("查看 RPMTW 翻譯貢獻者"), (button) -> Util.getOperatingSystem().open("https://www.rpmtw.com/Contributor"));
            addDrawableChild(buttonWidget);
            addDrawableChild(rpmtwLogo);
        }
    }
}

