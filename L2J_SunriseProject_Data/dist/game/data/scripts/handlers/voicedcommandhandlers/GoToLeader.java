package handlers.voicedcommandhandlers;

import l2r.gameserver.handler.IVoicedCommandHandler;
import l2r.gameserver.instancemanager.GrandBossManager;
import l2r.gameserver.instancemanager.InstanceManager;
import l2r.gameserver.instancemanager.SiegeManager;
import l2r.gameserver.model.L2Clan;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.entity.olympiad.OlympiadManager;

import gr.sr.interf.SunriseEvents;

public class GoToLeader implements IVoicedCommandHandler
{
	
	private static final String[] _voicedCommands =
	{
		"gotoleader"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (activeChar == null)
		{
			return false;
		}
		
		if (command.equalsIgnoreCase(_voicedCommands[0]))
		{
			L2Clan clan = activeChar.getClan();
			if (clan != null)
			{
				L2PcInstance clanLeader = L2World.getInstance().getPlayer(clan.getLeaderId());
				if ((clanLeader != null))
				{
					if (!clanLeader.isOnline())
					{
						activeChar.sendMessage("Your clan leader it's not online right now.");
						return false;
					}
					
					if (activeChar == clanLeader)
					{
						activeChar.sendMessage("You cannot teleport to yourself.");
						return false;
					}
					
					if ((GrandBossManager.getInstance().getZone(activeChar) != null) || (GrandBossManager.getInstance().getZone(clanLeader) != null))
					{
						activeChar.sendMessage("Cannot be used if you or your leader are inside boss zone.");
						return false;
					}
					
					if ((InstanceManager.getInstance().getPlayerWorld(activeChar) != null) || (InstanceManager.getInstance().getPlayerWorld(clanLeader) != null))
					{
						activeChar.sendMessage("Cannot be used if you or your leader are inside instancezones.");
						return false;
					}
					
					if (activeChar.isInStoreMode())
					{
						activeChar.sendMessage("Cannot be used while you or your leader are in store mode.");
						return false;
					}
					
					if (activeChar.isCombatFlagEquipped() || clanLeader.isCombatFlagEquipped())
					{
						activeChar.sendMessage("Cannot be used while you or your leader are holding a Combat Flag or Territory Ward!");
						return false;
					}
					
					if (activeChar.isCursedWeaponEquipped() || clanLeader.isCursedWeaponEquipped())
					{
						activeChar.sendMessage("Cannot be used while you or your leader are holding a Cursed Weapon!");
						return false;
					}
					
					if (activeChar.isJailed() || clanLeader.isJailed())
					{
						activeChar.sendMessage("Cannot be used while you or your leader are in Jail!");
						return false;
					}
					
					if (activeChar.isAlikeDead() || clanLeader.isAlikeDead())
					{
						activeChar.sendMessage("Cannot use while you or your clan leader are dead or in fake death mode.");
						return false;
					}
					
					if (activeChar.isInCombat() || clanLeader.isInCombat())
					{
						activeChar.sendMessage("Cannot use while you or your clan leader are in combat mode.");
						return false;
					}
					
					if ((activeChar.getKarma() > 0) || (clanLeader.getKarma() > 0))
					{
						activeChar.sendMessage("Cannot use while you or your leader have karma.");
						return false;
					}
					
					if ((activeChar.getPvpFlag() > 0) || (clanLeader.getPvpFlag() > 0))
					{
						activeChar.sendMessage("Cannot use while you or your leader have pvp flag.");
						return false;
					}
					
					if (activeChar.isInOlympiadMode() || activeChar.inObserverMode() || OlympiadManager.getInstance().isRegistered(activeChar) || clanLeader.isInOlympiadMode() || clanLeader.inObserverMode() || OlympiadManager.getInstance().isRegistered(clanLeader))
					{
						activeChar.sendMessage("Cannot use while you or your leader are in Olympiad.");
						return false;
					}
					
					if (SunriseEvents.isInEvent(activeChar) || SunriseEvents.isRegistered(activeChar) || SunriseEvents.isInEvent(clanLeader) || SunriseEvents.isRegistered(clanLeader))
					{
						activeChar.sendMessage("Cannot use while you or your leader are in event.");
						return false;
					}
					
					if (activeChar.isInDuel() || clanLeader.isInDuel())
					{
						activeChar.sendMessage("Can't be used if you or your leader are in a duel!");
						return false;
					}
					
					if (activeChar.inObserverMode() || clanLeader.inObserverMode())
					{
						activeChar.sendMessage("Can't be used if you or your leader are in the observation.");
						return false;
					}
					
					if ((SiegeManager.getInstance().getSiege(activeChar) != null) && (SiegeManager.getInstance().getSiege(clanLeader) != null) && SiegeManager.getInstance().getSiege(activeChar).isInProgress())
					{
						activeChar.sendMessage("Can't be used if you or your leader are are in a siege.");
						return false;
					}
					
					if (activeChar.isFestivalParticipant() || clanLeader.isFestivalParticipant())
					{
						activeChar.sendMessage("Can't be used if you or your leader are in a festival.");
						return false;
					}
					
					if ((activeChar.isInParty() && activeChar.getParty().isInDimensionalRift()) || (clanLeader.isInParty() && clanLeader.getParty().isInDimensionalRift()))
					{
						activeChar.sendMessage("Can't be used if you or your leader are in the dimensional rift.");
						return false;
					}
					
					activeChar.teleToLocation(clanLeader.getX(), clanLeader.getY(), clanLeader.getZ());
					
					activeChar.sendMessage("You have been teleported to " + clanLeader.getName() + ", your clan leader.");
					
					clanLeader.sendMessage("Your clan member " + activeChar.getName() + " teleported to you.");
				}
			}
			else
			{
				activeChar.sendMessage("You are not a clan member and can't use this command.");
			}
			return true;
		}
		return false;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}
