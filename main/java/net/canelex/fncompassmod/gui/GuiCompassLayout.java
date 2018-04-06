package net.canelex.fncompassmod.gui;

import net.canelex.fncompassmod.CompassMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;

public class GuiCompassLayout extends GuiCompass
{
	private GuiSlider sliderWidth;
	private GuiSlider sliderCWidth;

	public GuiCompassLayout(CompassMod mod, GuiScreen parent)
	{
		super(mod, parent);
	}

	@Override
	public void initGui()
	{
		buttonList.add(new GuiButton(0, width / 2 - 60, height / 2 - 25, 120, 20, getDetailString()));
		buttonList.add(sliderWidth = new GuiSlider(1, width / 2 - 60, height / 2, 120, 20, "Width: ", "", 50, 300, compass.width, false, true));
		buttonList.add(sliderCWidth = new GuiSlider(2, width / 2 - 60, height / 2 + 25, 120, 20, "Spacing: ", "", 200, 1200, compass.cwidth, false, true));
		buttonList.add(new GuiButton(3, width / 2 - 60, height / 2 + 50, 120, 20, "Done"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		switch (button.id)
		{
			case 0:
				compass.details = (compass.details + 1) % 3;
				button.displayString = getDetailString();
				break;
			case 3:
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
		compass.width = sliderWidth.getValueInt();
		compass.cwidth = sliderCWidth.getValueInt();
	}

	private String getDetailString()
	{
		switch (compass.details)
		{
			case 0:
				return "Details: " + EnumChatFormatting.GOLD + "LOW";
			case 1:
				return "Details: " + EnumChatFormatting.YELLOW + "MED";
			case 2:
				return "Details: " + EnumChatFormatting.GREEN + "HIGH";
			default:
				return "Details: ???";
		}
	}
}
