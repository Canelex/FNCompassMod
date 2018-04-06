package net.canelex.fncompassmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = "fncompassmod", version = "1.0")
public class CompassMod
{
	private Minecraft mc;
	private FNCompass compass;
	private Configuration config;

	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{
		config = new Configuration(event.getSuggestedConfigurationFile());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		mc = Minecraft.getMinecraft();
		compass = new FNCompass(mc);
		loadConfig();

		MinecraftForge.EVENT_BUS.register(this);
		ClientCommandHandler.instance.registerCommand(new CommandEditCompass(this));
	}

	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent.Post event)
	{
		if (event.type == RenderGameOverlayEvent.ElementType.ALL)
		{
			if (!compass.enabled)
			{
				return;
			}

			if (mc.thePlayer == null)
			{
				return;
			}

			if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat))
			{
				return;
			}

			compass.drawCompass(event.resolution.getScaledWidth());
		}
	}

	public void saveConfig()
	{
		compass.save(config);
		config.save();
	}

	public void loadConfig()
	{
		config.load();
		compass.load(config);
	}

	public FNCompass getCompass()
	{
		return compass;
	}
}
