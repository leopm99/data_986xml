package handlers.voicedcommandhandlers;

import java.util.Map;
import java.util.StringTokenizer;

import l2r.gameserver.handler.IVoicedCommandHandler;
import l2r.gameserver.instancemanager.InstanceManager;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.util.StringUtil;

public class ClearInstances implements IVoicedCommandHandler
{
	private static final String[] _voicedCommands =
	{
		"clearinstance",
		"display"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
	{
		if (command.equalsIgnoreCase(_voicedCommands[0]))
		{
			try
			{
				StringTokenizer st = new StringTokenizer(command, " ");
				
				st.nextToken();
				final int instanceId = Integer.parseInt(st.nextToken());
				final String name = InstanceManager.getInstance().getInstanceIdName(instanceId);
				int penaltyRemovalPrice = 1000;
				if (activeChar.getAdena() >= penaltyRemovalPrice)
				{
					InstanceManager.getInstance().deleteInstanceTime(activeChar.getObjectId(), instanceId);
					activeChar.sendMessage("Instance zone " + name + " cleared." + activeChar.getName());
					activeChar.sendMessage("Admin cleared instance zone " + name + " for you");
					return true;
				}
				
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Failed clearing instance time: " + e.getMessage());
				activeChar.sendMessage("Usage: //instancezone_clear <playername> [instanceId]");
				return false;
			}
			
		}
		else if (command.equalsIgnoreCase(_voicedCommands[1]))
		{
			display(activeChar);
		}
		return true;
	}
	
	private void display(L2PcInstance player)
	{
		Map<Integer, Long> instanceTimes = InstanceManager.getInstance().getAllInstanceTimes(player.getObjectId());
		
		final StringBuilder html = StringUtil.startAppend(500 + (instanceTimes.size() * 200), "<html><center><table width=260><tr>" + "<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>" + "<td width=180><center>Character Instances</center></td>" + "<td width=40><button value=\"Back\" action=\"bypass -h admin_current_player\" width=40 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>" + "</tr></table><br><font color=\"LEVEL\">Instances for ", player.getName(), "</font><center><br>" + "<table>" + "<tr><td width=150>Name</td><td width=50>Time</td><td width=70>Action</td></tr>");
		
		for (int id : instanceTimes.keySet())
		{
			int hours = 0;
			int minutes = 0;
			long remainingTime = (instanceTimes.get(id) - System.currentTimeMillis()) / 1000;
			if (remainingTime > 0)
			{
				hours = (int) (remainingTime / 3600);
				minutes = (int) ((remainingTime % 3600) / 60);
			}
			
			StringUtil.append(html, "<tr><td>", InstanceManager.getInstance().getInstanceIdName(id), "</td><td>", String.valueOf(hours), ":", String.valueOf(minutes), "</td><td><button value=\"Clear\" action=\"bypass -h admin_instancezone_clear ", player.getName(), " ", String.valueOf(id), "\" width=60 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>");
		}
		
		StringUtil.append(html, "</table></html>");
		
		NpcHtmlMessage ms = new NpcHtmlMessage();
		ms.setHtml(html.toString());
		
		player.sendPacket(ms);
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}
