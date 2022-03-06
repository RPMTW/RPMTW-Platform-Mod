package com.rpmtw.rpmtw_platform_mod;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class CosmicChatData {
    public static final int offset = 10;
    public static int lastY = 0;
    public static int lastMessageIndex = 0;
    public static float lastOpacity = 0;
    public static Map<String, ResourceLocation> avatarCache = new HashMap<>();
}
