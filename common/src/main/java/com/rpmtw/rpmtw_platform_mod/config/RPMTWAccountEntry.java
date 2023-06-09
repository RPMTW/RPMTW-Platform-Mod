package com.rpmtw.rpmtw_platform_mod.config;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rpmtw.rpmtw_platform_mod.handlers.RPMTWAuthHandler;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RPMTWAccountEntry extends AbstractConfigListEntry<Object> {
    private final List<AbstractWidget> widgets = new ArrayList<>();

    public RPMTWAccountEntry() {
        super(Component.literal(UUID.randomUUID().toString()), false);
    }

    @Override
    public int getItemHeight() {
        return 48;
    }

    @Nullable
    public ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent) {
        return null;
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return widgets;
    }

    @Override
    public Iterator<String> getSearchTags() {
        return Collections.emptyIterator();
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public Optional<Object> getDefaultValue() {
        return Optional.empty();
    }

    @Override
    public boolean isMouseInside(int mouseX, int mouseY, int x, int y, int entryWidth, int entryHeight) {
        return false;
    }

    @Override
    public void render(PoseStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);

        boolean isLogin = RPMTWConfig.get().isLogin();
        String authStatus;
        if (isLogin) {
            authStatus = I18n.get("auth.rpmtw_platform_mod.status.logged_in");
        } else {
            authStatus = I18n.get("auth.rpmtw_platform_mod.status.not_logged_in");
        }

        if (!isLogin) {
            Button loginButton = new Button.Builder(Component.translatable("auth.rpmtw_platform_mod.button.login"), button -> RPMTWAuthHandler.INSTANCE.login(RPMTWAuthHandler.port))
                    .bounds(entryWidth / 2 + 20, y + 15, 65, 20)
                    .tooltip(Tooltip.create(Component.translatable("auth.rpmtw_platform_mod.button.login.tooltip")))
                    .build();

            loginButton.render(matrices, mouseX, mouseY, delta);
            widgets.add(loginButton);
        } else {
            Button logoutButton = Button.builder(
                            Component.translatable("auth.rpmtw_platform_mod.button.logout")
                            , button -> RPMTWAuthHandler.INSTANCE.logout())
                    .bounds(entryWidth / 2 + 20, y + 15, 65, 20)
                    .build();

            logoutButton.render(matrices, mouseX, mouseY, delta);
            widgets.add(logoutButton);
        }

        Font font = Minecraft.getInstance().font;
        font.drawShadow(
                matrices,
                authStatus,
                (float) (x - 4 + entryWidth / 2 - Minecraft.getInstance().font.width(authStatus) / 2),
                (float) y,
                -1
        );
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return widgets;
    }
}