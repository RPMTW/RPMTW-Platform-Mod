package siongsng.rpmtwupdatemod.gui;


/*
@ModElements.ModElement.Tag
public class CrowdinGui extends ModElements.ModElement {
    public static HashMap guistate = new HashMap();
    private static ContainerType<GuiContainerMod> containerType = null;

    public CrowdinGui(ModElements instance) {
        super(instance, 3);
        containerType = new ContainerType<>(new GuiContainerModFactory());
        FMLJavaModLoadingContext.get().getModEventBus().register(new ContainerRegisterHandler());
    }

    @OnlyIn(Dist.CLIENT)
    public void initElements() {
        DeferredWorkQueue.runLater(() -> ScreenManager.registerFactory(containerType, CrowidnGuiWindow::new));
        ScreenManager.openScreen(containerType, Minecraft.getInstance(), 1, new StringTextComponent("test"));
    }


    private static class ContainerRegisterHandler {
        @SubscribeEvent
        public void registerContainer(RegistryEvent.Register<ContainerType<?>> event) {
            event.getRegistry().register(containerType.setRegistryName("crowdin_gui"));
        }
    }

    public static class GuiContainerModFactory implements IContainerFactory {
        public GuiContainerMod create(int id, PlayerInventory inv, PacketBuffer extraData) {
            return new GuiContainerMod(id, inv, extraData);
        }
    }

    public static class GuiContainerMod extends Container {
        World world;
        PlayerEntity entity;
        int x, y, z;

        public GuiContainerMod(int id, PlayerInventory inv, PacketBuffer extraData) {
            super(containerType, id);
            this.entity = inv.player;
            this.world = inv.player.world;
            BlockPos pos;
            if (extraData != null) {
                pos = extraData.readBlockPos();
                this.x = pos.getX();
                this.y = pos.getY();
                this.z = pos.getZ();
            }
        }


        @Override
        public boolean canInteractWith(PlayerEntity player) {
            return true;
        }
    }
}
*/

