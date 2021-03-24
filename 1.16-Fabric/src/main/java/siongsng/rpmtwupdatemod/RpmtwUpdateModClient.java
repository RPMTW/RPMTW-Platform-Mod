package siongsng.rpmtwupdatemod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class RpmtwUpdateModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("Hello RPMTW world!");

    }
}