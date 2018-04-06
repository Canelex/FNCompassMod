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
			} else
			{
				colorMarker = 0xFFFFFFFF;
			}

			if (tintDirection != 0)
			{
				colorDirection = Color.HSBtoRGB(tintDirection / 100F, 1F, 1F);
			} else
			{
				colorDirection = 0xFFFFFFFF;
			}
		} else
		{
			colorDirection = colorMarker = Color.HSBtoRGB((System.currentTimeMillis() % 3000) / 3000F, 1F, 1F);
		}

		renderMarker();

		if (details >= 0) // LOW
		{
			drawDirection("S", 0, 1.5D);
			drawDirection("W", 90, 1.5D);
			drawDirection("N", 180, 1.5D);
			drawDirection("E", 270, 1.5D);
		}

		if (details >= 1) // MED
		{
			drawDirection("SW", 45, 1D);
			drawDirection("NW", 135, 1D);
			drawDirection("NE", 225, 1D);
			drawDirection("SE", 315, 1D);
		}

		if (details >= 2) // HIGH
		{
			drawDirection("15", 15, 0.75D);
			drawDirection("30", 30, 0.75D);
			drawDirection("60", 60, 0.75D);
			drawDirection("75", 75, 0.75D);
			drawDirection("105", 105, 0.75D);
			drawDirection("120", 120, 0.75D);
			drawDirection("150", 150, 0.75D);
			drawDirection("165", 165, 0.75D);
			drawDirection("195", 195, 0.75D);
			drawDirection("210", 210, 0.75D);
			drawDirection("240", 240, 0.75D);
			drawDirection("255", 255, 0.75D);
			drawDirection("285", 285, 0.75D);
			drawDirection("300", 300, 0.75D);
			drawDirection("330", 330, 0.75D);
			drawDirection("345", 345, 0.75D);
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
		config.get("scale", "cwidth", 300).set(cwidth);
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
		cwidth = config.get("scale", "cwidth", 300).getInt();
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

	private void renderMarker()
	{
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color((colorMarker >> 16 & 255) / 255F, (colorMarker >> 8 & 255) / 255F, (colorMarker & 255) / 255F, 1F);
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
}
