package com.kjmaster.generictechmod.client;

import com.kjmaster.generictechmod.GenericTechMod;
import com.kjmaster.generictechmod.tiles.fluxfurnace.ContainerFluxFurnace;
import com.kjmaster.generictechmod.tiles.fluxfurnace.TileFluxFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiFluxFurnace extends GuiContainer {

    private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation(GenericTechMod.MODID,
            "textures/gui/flux_furnace.png");

    private final InventoryPlayer playerInv;
    private final TileFluxFurnace tileFluxFurnace;
    private ProgressBar progressBar;

    public GuiFluxFurnace(InventoryPlayer playerInv, TileFluxFurnace fluxInv)
    {
        super(new ContainerFluxFurnace(playerInv, fluxInv));
        this.playerInv = playerInv;
        this.tileFluxFurnace = fluxInv;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = I18n.format(GenericTechMod.MODID + ".container.flux_furnace");
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(),
                8, this.ySize - 96 + 2, 4210752);
        this.progressBar = new ProgressBar(FURNACE_GUI_TEXTURES, ProgressBar.ProgressBarDirection.DOWN_TO_UP,
                20, 40,10, 15, 176, 0);
        this.progressBar.setMin(tileFluxFurnace.getField(0));
        this.progressBar.setMax(10000);
        this.progressBar.draw(mc);
        int l = this.getCookProgressScaled(24);
        this.drawTexturedModalRect(79, 34, 176, 42, l + 1, 16);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }

    private int getCookProgressScaled(int pixels)
    {
        int i = this.tileFluxFurnace.getField(1);
        int j = TileFluxFurnace.SMELT_TIME;
        return i != 0 ? i * pixels / j : 0;
    }
}
