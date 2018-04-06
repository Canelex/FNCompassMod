package net.canelex.fncompassmod;

import net.canelex.fncompassmod.gui.GuiCompassHub;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CommandEditCompass extends CommandBase
{
	private CompassMod mod;

	public CommandEditCompass(CompassMod mod)
	{
		this.mod = mod;
	}

	@Override
	public String getCommandName()
	{
		return "compassmod";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "/compassmod";
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender)
	{
		return true;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args)
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		MinecraftForge.EVENT_BUS.unregister(this);
		Minecraft.getMinecraft().displayGuiScreen(new GuiCompassHub(mod));
	}
}
