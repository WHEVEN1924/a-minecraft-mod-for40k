package com.gellerfield;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/**
 * 盖勒力场电池管理界面（客户端渲染）。
 * 26.2 的容器背景走 extractBackground（与 ShulkerBoxScreen 一致）：在这里画背景纹理，
 * 槽位、物品、标签由父类 extractContents 负责，必须调用 super 才不会丢。
 */
public class GellerFieldScreen extends AbstractContainerScreen<GellerFieldMenu> {
    /** 界面背景纹理，256x256 画布，左上 176x166 为界面内容。 */
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(GellerFieldMod.MODID, "textures/gui/geller_field.png");

    public GellerFieldScreen(GellerFieldMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 166);
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 72;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, xo, yo, 0.0F, 0.0F,
                this.imageWidth, this.imageHeight, 256, 256);
    }
}
