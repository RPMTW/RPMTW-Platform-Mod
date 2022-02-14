package com.rpmtw.rpmtw_platform_mod;

import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.Registries;

import java.util.function.Supplier;

public class RPMTWPlatformMod {
    public static final String MOD_ID = "rpmtw_platform_mod";
    public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));
    public static void init() {

        System.out.println(RPMTWPlatformModPlugin.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}
