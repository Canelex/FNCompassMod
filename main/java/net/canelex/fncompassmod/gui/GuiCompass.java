package net.canelex.fncompassmod.gui;

import net.canelex.fncompassmod.CompassMod;
import net.canelex.fncompassmod.FNCompass;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;

public class GuiCompass extends GuiScreen
{
    protected CompassMod mod;
    protected FNCompass compass;
    protected GuiScreen parent;
    private boolean dragging;
    private int lastX;
    private int lastY;

    public GuiCompass(CompassMod mod, GuiScreen parent)
    {
        this.mod = mod;
        this.compass = mod.getCompass();
        this.parent = parent;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawDefaultBackground();

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (compass.enabled)
        {
            compass.drawCompass(width); // Render the compass.
        }

        if (dragging)
        {
            // Move the compass.
            compass.offX += mouseX - lastX;
            compass.offY += mouseY - lastY;
        }

        lastX = mouseX;
        lastY = mouseY;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseX >= (width - compass.width) / 2 + compass.offX && mouseY >= compass.offY &&
                mouseX <= (width + compass.width) / 2 + compass.offX && mouseY <= compass.offY + compass.height)
        {
            dragging = true;
            lastX = mouseX;
            lastY = mouseY;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);

        dragging = false;
    }

    @Override
    public void onGuiClosed()
    {
        mod.saveConfig();
    }

    protected String getColoredBool(String prefix, boolean bool)
    {
        if (bool)
        {
            return prefix + EnumChatFormatting.GREEN + "TRUE";
        }

        return prefix + EnumChatFormatting.RED + "FALSE";
    }
}
