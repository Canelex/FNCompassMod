package net.canelex.fncompassmod.gui;

import net.canelex.fncompassmod.CompassMod;
import net.minecraft.client.gui.GuiButton;

import java.io.IOException;

public class GuiCompassHub extends GuiCompass
{
    public GuiCompassHub(CompassMod mod)
    {
        super (mod, null);
    }

    @Override
    public void initGui()
    {
        buttonList.add(new GuiButton(0, width / 2 - 60, height / 2 - 50, 120, 20, getColoredBool("Enabled: ", compass.enabled)));
        buttonList.add(new GuiButton(1, width / 2 - 60, height / 2 - 25, 120, 20, "Edit Layout"));
        buttonList.add(new GuiButton(2, width / 2 - 60, height / 2, 120, 20, "Edit Style"));
        buttonList.add(new GuiButton(3, width / 2 - 60, height / 2 + 25, 120, 20, "Reset Position"));
        buttonList.add(new GuiButton(4, width / 2 - 60, height / 2 + 50, 120, 20, "Done"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        switch (button.id)
        {
            case 0:
                compass.enabled = !compass.enabled;
                button.displayString = getColoredBool("Enabled: ", compass.enabled);
                break;
            case 1:
                mc.displayGuiScreen(new GuiCompassLayout(mod, this));
                break;
            case 2:
                mc.displayGuiScreen(new GuiCompassStyle(mod, this));
                break;
            case 3:
                compass.offX = 0;
                compass.offY = 0;
                break;
            case 4:
                mc.displayGuiScreen(null);
        }
    }
}
