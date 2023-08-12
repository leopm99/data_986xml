package handlers.voicedcommandhandlers;

import l2r.gameserver.data.xml.impl.ClassListData;
import l2r.gameserver.handler.IVoicedCommandHandler;
import l2r.gameserver.handler.VoicedCommandHandler;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class SubClassCommand implements IVoicedCommandHandler
{
	
	private static final String[] COMMANDS =
	{
		"subclass"
	};
	
	private static final String HTML_HEADER = "<html><body><title>Subclass Transfer</title>";
	private static final String HTML_FOOTER = "</body></html>";
	
	@Override
	public String[] getVoicedCommandList()
	{
		return COMMANDS;
	}
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance player, String params)
	{
		StringBuilder htmlContent = new StringBuilder(HTML_HEADER);
		
		if ((params != null) && !params.isEmpty())
		{
			int selectedSubclass = Integer.parseInt(params);
			if (selectedSubclass > player.getSubClasses().size())
			{
				return false;
			}
			
			if (!player.getFloodProtectors().getSubclass().tryPerformAction("change class"))
			{
				return false;
			}
			
			if (player.getClassIndex() == selectedSubclass)
			{
				return false;
			}
			
			if (!checkConditions(player))
			{
				return false;
			}
			
			player.setActiveClass(selectedSubclass);
			player.sendPacket(SystemMessageId.SUBCLASS_TRANSFER_COMPLETED);
			return true;
		}
		
		StringBuilder linkList = new StringBuilder();
		linkList.append(makeLink(0, getClassDescription(player.getBaseClass())));
		
		for (int i = 1; i <= player.getSubClasses().size(); i++)
		{
			linkList.append(makeLink(i, getClassDescription(player.getSubClasses().get(i).getClassId())));
		}
		
		htmlContent.append(linkList).append(HTML_FOOTER);
		
		NpcHtmlMessage html = new NpcHtmlMessage(player.getObjectId());
		html.setHtml(htmlContent.toString());
		player.sendPacket(html);
		return true;
	}
	
	private String getClassDescription(int classId)
	{
		try
		{
			return ClassListData.getInstance().getClass(classId).getClientCode();
		}
		catch (Exception e)
		{
			return ClassListData.getInstance().getClass(classId).getClassName();
			
		}
	}
	
	private String makeLink(int classIndex, String description)
	{
		return "<a action=\"bypass -h voice .subclass " + classIndex + "\">" + description + "</a><br>";
	}
	
	private boolean checkConditions(L2PcInstance player)
	{
		if (player.isInCombat() || player.isDead())
		{
			return false;
		}
		
		if (player.isInStoreMode() || player.isInCraftMode() || player.isFakeDeath() || !player.isOnline() || player.getFishingEx().isFishing())
		{
			return false;
		}
		
		return true;
	}
	
	public SubClassCommand()
	{
		VoicedCommandHandler.getInstance().registerHandler(this);
		System.out.println("INFO: SubClassCommand registered. Players can use .subclass command.");
	}
	
}
