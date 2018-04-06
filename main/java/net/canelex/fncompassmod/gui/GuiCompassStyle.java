package net.canelex.fncompassmod.gui;

import net.canelex.fncompassmod.CompassMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.awt.*;
import java.io.IOException;

public class GuiCompassStyle extends GuiCompass
{
	private GuiSlider sliderMarkerTint;
	private GuiSlider sliderDirectionTint;

	public GuiCompassStyle(CompassMod mod, GuiScreen parent)
	{
		super(mod, parent);
	}

	@Override
	public void initGui()
	{
		buttonList.add(new GuiButton(0, width / 2 - 60, height / 2 - 75, 120, 20,
				getColoredBool("Background: ", compass.background)));
		buttonList.add(new GuiButton(1, width / 2 - 60, height / 2 - 50, 120, 20,
				getColoredBool("Chroma: ", compass.chroma)));
		buttonList.add(new GuiButton(2, width / 2 - 60, height / 2 - 25, 120, 20,
				getColoredBool("Shadow: ", compass.shadow)));
		buttonList.add(sliderMarkerTint = new GuiSlider(3, width / 2 - 60, height / 2, 120, 20, "Marker Tint: ", "", 0,
				100, compass.tintMarker, false, true));
		buttonList.add(sliderDirectionTint = new GuiSlider(4, width / 2 - 60, height / 2 + 25, 120, 20,
				"Direction Tint: ", "", 0, 100, compass.tintDirection, false, true));
		buttonList.add(new GuiButton(5, width / 2 - 60, height / 2 + 50, 120, 20, "Done"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		switch (button.id)
		{
			case 0:
				compass.background = !compass.background;
				button.displayString = getColoredBool("Background: ", compass.background);
				break;
			case 1:
				compass.chroma = !compass.chroma;
				button.displayString = getColoredBool("Chroma: ", compass.chroma);
				break;
			case 2:
				compass.shadow = !compass.shadow;
				button.displayString = getColoredBool("Shadow: ", compass.shadow);
				break;
			case 5:
				mc.displayGuiScreen(parent);
				break;
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);

		updateSliders();
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
	{
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

		updateSliders();
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state)
	{
		super.mouseReleased(mouseX, mouseY, state);

		updateSliders();
	}

	private void updateSliders()
	{
		compass.tintMarker = sliderMarkerTint.getValueInt();
		compass.tintDirection = sliderDirectionTint.getValueInt();
	}
}
