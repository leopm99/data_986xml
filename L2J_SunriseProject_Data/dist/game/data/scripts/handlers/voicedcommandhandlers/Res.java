package handlers.voicedcommandhandlers;

import l2r.Config;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.handler.IVoicedCommandHandler;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.entity.olympiad.OlympiadManager;
import l2r.gameserver.model.items.instance.L2ItemInstance;
import l2r.gameserver.network.serverpackets.ActionFailed;

import gr.sr.interf.SunriseEvents;

/**
 * @author Prims
 */
public class Res implements IVoicedCommandHandler
{
	private static final String[] _voicedCommands =
	{
		"res"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		
		if (command.equalsIgnoreCase("res"))
		{
			
			final int CoinCount = Config.PRICE_RESS;
			final L2ItemInstance Coin = activeChar.getInventory().getItemByItemId(Config.ITEM_ID_RESS);
			
			if (!activeChar.isAlikeDead() || activeChar.isFakeDeath())
			{
				activeChar.sendMessage("You cannot revive, since you're alive!");
				return false;
			}
			
			if (activeChar.getKarma() > 0)
			{
				activeChar.sendMessage("Cannot use while have karma.");
				return false;
			}
			
			if (activeChar.getPvpFlag() > 0)
			{
				activeChar.sendMessage("Cannot use while have pvp flag.");
				return false;
			}
			
			if (activeChar.isInOlympiadMode() || activeChar.inObserverMode() || OlympiadManager.getInstance().isRegistered(activeChar))
			{
				activeChar.sendMessage("Cannot use while in Olympiad.");
				return false;
			}
			
			if (SunriseEvents.isInEvent(activeChar) || SunriseEvents.isRegistered(activeChar))
			{
				activeChar.sendMessage("Cannot use while in event.");
				return false;
			}
			
			if (activeChar.isJailed())
			{
				activeChar.sendMessage("Cannot use while in jail.");
				return false;
			}
			
			if (!activeChar.isInsideZone(ZoneIdType.PEACE) && activeChar.isInCombat())
			{
				activeChar.sendMessage("Cannot use while in combat outside of peace zone.");
				return false;
			}
			
			if (Coin == null)
			{
				activeChar.sendMessage("You do not have enough money.");
			}
			
			if ((CoinCount != 0) && (activeChar.getInventory().getItemByItemId(Config.ITEM_ID_RESS).getCount() < CoinCount))
			{
				activeChar.sendMessage("You do not have enough money.");
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				return false;
			}
			
			if (Config.COMMAND_RES)
			{
				activeChar.destroyItemByItemId(".res", Config.ITEM_ID_RESS, CoinCount, null, true);
				activeChar.doRevive(100.0);
				activeChar.restoreExp(activeChar.getExpBeforeDeath());
				activeChar.setCurrentCp(activeChar.getMaxCp());
				activeChar.setCurrentHp(activeChar.getMaxHp());
				activeChar.setCurrentMp(activeChar.getMaxMp());
				activeChar.sendMessage("You rose!");
				activeChar.sendMessage("You have successfully paid for the resurrection service. Thank you!");
			}
			
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}
