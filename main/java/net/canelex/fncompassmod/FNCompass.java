package net.canelex.fncompassmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.common.config.Configuration;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class FNCompass
{
	private Minecraft mc;
	private FontRenderer fr;
	private final double PRIMARY_CARDINAL_SCALE = 1.5D;
	private final double SECONDARY_CARDINAL_SCALE = 1D;
	private final double ANGLE_SCALE = 0.75D;

	// Settings
	public boolean enabled;
	public int details;
	public int offX;
	public int offY;
	public int width;
	public int height;
	public int cwidth;
	public boolean background;
	public boolean chroma;
	public boolean shadow;
	public int tintMarker;
	public int tintDirection;

	// Rendering
	private int offsetAll;
	private int centerX;
	private int colorMarker;
	private int colorDirection;

	public FNCompass(Minecraft mc)
	{
		this.mc = mc;
		this.fr = mc.fontRendererObj;
	}

	public void drawCompass(int screenWidth)
	{
		// Update with player's direction.
		int direction = normalize((int) mc.thePlayer.rotationYaw);
		offsetAll = cwidth * direction / 360;
		centerX = (screenWidth / 2) + offX;

		// Render opaque black background.
		if (background)
		{
			Gui.drawRect(centerX - width / 2, offY, centerX + width / 2, offY + height, 0xAA000000);
		}

		// Update colors.
		if (!chroma)
		{
			if (tintMarker != 0)
			{
				colorMarker = Color.HSBtoRGB(tintMarker / 100F, 1F, 1F);
			}
			else
			{
				colorMarker = 0xFFFFFFFF;
			}

			if (tintDirection != 0)
			{
				colorDirection = Color.HSBtoRGB(tintDirection / 100F, 1F, 1F);
			}
			else
			{
				colorDirection = 0xFFFFFFFF;
			}
		}
		else
		{
			colorDirection = colorMarker = Color.HSBtoRGB((System.currentTimeMillis() % 3000) / 3000F, 1F, 1F);
		}

		renderMarker();

		if (details >= 0) // LOW
		{
			drawDirection("S", 0, PRIMARY_CARDINAL_SCALE);
			drawDirection("W", 90, PRIMARY_CARDINAL_SCALE);
			drawDirection("N", 180, PRIMARY_CARDINAL_SCALE);
			drawDirection("E", 270, PRIMARY_CARDINAL_SCALE);
		}

		if (details >= 1) // MED
		{
			drawDirection("SW", 45, SECONDARY_CARDINAL_SCALE);
			drawDirection("NW", 135, SECONDARY_CARDINAL_SCALE);
			drawDirection("NE", 225, SECONDARY_CARDINAL_SCALE);
			drawDirection("SE", 315, SECONDARY_CARDINAL_SCALE);
		}

		if (details >= 2) // HIGH
		{
			drawDirection("15", 15, ANGLE_SCALE);
			drawDirection("30", 30, ANGLE_SCALE);
			drawDirection("60", 60, ANGLE_SCALE);
			drawDirection("75", 75, ANGLE_SCALE);
			drawDirection("105", 105, ANGLE_SCALE);
			drawDirection("120", 120, ANGLE_SCALE);
			drawDirection("150", 150, ANGLE_SCALE);
			drawDirection("165", 165, ANGLE_SCALE);
			drawDirection("195", 195, ANGLE_SCALE);
			drawDirection("210", 210, ANGLE_SCALE);
			drawDirection("240", 240, ANGLE_SCALE);
			drawDirection("255", 255, ANGLE_SCALE);
			drawDirection("285", 285, ANGLE_SCALE);
			drawDirection("300", 300, ANGLE_SCALE);
			drawDirection("330", 330, ANGLE_SCALE);
			drawDirection("345", 345, ANGLE_SCALE);
		}
	}


	private void renderMarker()
	{
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager
				.color((colorMarker >> 16 & 255) / 255F, (colorMarker >> 8 & 255) / 255F, (colorMarker & 255) / 255F,
						1F);
		worldrenderer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);
		worldrenderer.pos(centerX, offY + 3, 0.0D).endVertex();
		worldrenderer.pos(centerX + 3, offY, 0.0D).endVertex();
		worldrenderer.pos(centerX - 3, offY, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	private void drawDirection(String dir, int degrees, double scale)
	{
		int offset = (cwidth * degrees / 360) - offsetAll;

		if (offset > cwidth / 2)
		{
			offset -= cwidth;
		}

		if (offset < -cwidth / 2)
		{
			offset += cwidth;
		}

		double opacity = 1 - (Math.abs(offset) / (width / 2D));

		if (opacity > 0.1D)
		{
			int defcolor = colorDirection & 0x00FFFFFF;
			int color = defcolor | (int) (opacity * 255) << 24;
			int posX = centerX + offset - (int) (fr.getStringWidth(dir) * scale / 2);
			int posY = offY + height / 2 - (int) (fr.FONT_HEIGHT * scale / 2);

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glPushMatrix();
			GL11.glTranslated(-posX * (scale - 1), -posY * (scale - 1), 0);
			GL11.glScaled(scale, scale, 1);
			if (shadow)
			{
				fr.drawStringWithShadow(dir, posX, posY, color);
			}
			else
			{
				fr.drawString(dir, posX, posY, color);
			}
			GL11.glPopMatrix();
			GL11.glDisable(GL11.GL_BLEND);
		}
	}

	public void save(Configuration config)
	{
		config.get("general", "enabled", true).set(enabled);
		config.get("general", "details", 2).set(details);
		config.get("position", "offX", 0).set(offX);
		config.get("position", "offY", 0).set(offY);
		config.get("scale", "width", 150).set(width);
		config.get("scale", "height", 20).set(height);
		config.get("scale", "cwidth", 500).set(cwidth);
		config.get("color", "background", true).set(background);
		config.get("color", "chroma", false).set(chroma);
		config.get("color", "shadow", true).set(shadow);
		config.get("color", "tintMarker", 0).set(tintMarker);
		config.get("color", "tintDirection", 0).set(tintDirection);
	}

	public void load(Configuration config)
	{
		enabled = config.get("general", "enabled", true).getBoolean();
		details = config.get("general", "details", 2).getInt();
		offX = config.get("position", "offX", 0).getInt();
		offY = config.get("position", "offY", 0).getInt();
		width = config.get("scale", "width", 150).getInt();
		height = config.get("scale", "height", 20).getInt();
		cwidth = config.get("scale", "cwidth", 500).getInt();
		background = config.get("color", "background", true).getBoolean();
		chroma = config.get("color", "chroma", false).getBoolean();
		shadow = config.get("color", "shadow", true).getBoolean();
		tintMarker = config.get("color", "tintMarker", 0).getInt();
		tintDirection = config.get("color", "tintDirection", 0).getInt();
	}

	private int normalize(int direction)
	{
		if (direction > 360)
		{
			direction %= 360;
		}

		while (direction < 0)
		{
			direction += 360;
		}

		return direction;
	}
}
