package io.github.chindeaone.collectiontracker.mixins;

import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiContainer.class)
public interface AccessorGuiContainer {

    @Invoker("drawGuiContainerBackgroundLayer")
    void invokeDrawGuiContainerBackgroundLayer_sct(float f, int i, int mouseY);
}
