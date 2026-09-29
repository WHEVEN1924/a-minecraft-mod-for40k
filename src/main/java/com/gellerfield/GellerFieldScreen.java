package com.gellerfield;

import com.mojang.blaze3d.pipeline.RenderPipeline;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/**
 * 盖勒力场电池管理界面（客户端渲染）。
 * 26.2 的 GUI 渲染走 extract* 系列 + GuiGraphicsExtractor；背景为 GUI sprite 图集。
 */
public class GellerFieldScreen extends AbstractContainerScreen<GellerFieldMenu> {
    /** Sprite id，对应 assets/gellerfield/textures/gui/sprites/geller_field.png。 */
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(GellerFieldMod.MODID, "geller_field");

    public GellerFieldScreen(GellerFieldMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 166);
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 72;
    }

    @Override
    public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        RenderPipeline pipeline = RenderPipelines.GUI_TEXTURED;
        guiGraphics.blitSprite(pipeline, TEXTURE, this.leftPos, this.topPos, this.imageWidth, this.imageHeight);
    }
}
