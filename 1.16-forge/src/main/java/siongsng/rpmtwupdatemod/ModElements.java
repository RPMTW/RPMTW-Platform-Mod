package siongsng.rpmtwupdatemod;

public class ModElements {
    /*
    public final List<ModElement> elements = new ArrayList<>();
    private int messageID = 0;

    public ModElements() {
        try {
            ModFileScanData modFileInfo = ModList.get().getModFileById("rpmtw_update_mod").getFile().getScanResult();
            Set<ModFileScanData.AnnotationData> annotations = modFileInfo.getAnnotations();
            for (ModFileScanData.AnnotationData annotationData : annotations) {
                if (annotationData.getAnnotationType().getClassName().equals(ModElement.Tag.class.getName())) {
                    Class<?> clazz = Class.forName(annotationData.getClassType().getClassName());
                    if (clazz.getSuperclass() == ModElements.ModElement.class)
                        elements.add((ModElements.ModElement) clazz.getConstructor(this.getClass()).newInstance(this));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(elements);
        elements.forEach(ModElements.ModElement::initElements);
    }

    public <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, PacketBuffer> encoder, Function<PacketBuffer, T> decoder,
                                      BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        RpmtwUpdateMod.PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }

    public static class ModElement implements Comparable<ModElement> {
        protected final ModElements elements;
        protected final int sortid;

        public ModElement(ModElements elements, int sortid) {
            this.elements = elements;
            this.sortid = sortid;
        }

        public void initElements() {
        }

        @Override
        public int compareTo(ModElement other) {
            return this.sortid - other.sortid;
        }

        @Retention(RetentionPolicy.RUNTIME)
        public @interface Tag {
        }
    }
     */
}
