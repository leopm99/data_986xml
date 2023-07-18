package ai.sunriseNpc.EnchantManager;

import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.itemcontainer.Inventory;
import l2r.gameserver.model.items.instance.L2ItemInstance;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.InventoryUpdate;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

import ai.npc.AbstractNpcAI;

/**
 * @author L2fuLL
 */
public final class EnchantManager extends AbstractNpcAI
{
	private static final int NpcId = 576;
	private static final int SafeEnchantMax = 100;
	private static final int[] IdItemAndCost =
	{
		57, // Id Item
		1000000 // Count
	};
	
	public EnchantManager()
	{
		super(EnchantManager.class.getSimpleName(), "ai/sunriseNpc");
		addFirstTalkId(NpcId);
		addTalkId(NpcId);
		addStartNpc(NpcId);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		int armorType = -1;
		
		if (event.startsWith("safe_seteh"))
		{
			armorType = Inventory.PAPERDOLL_HEAD;
		}
		else if (event.startsWith("safe_setec"))
		{
			armorType = Inventory.PAPERDOLL_CHEST;
		}
		else if (event.startsWith("safe_seteg"))
		{
			armorType = Inventory.PAPERDOLL_GLOVES;
		}
		else if (event.startsWith("safe_seteb"))
		{
			armorType = Inventory.PAPERDOLL_FEET;
		}
		else if (event.startsWith("safe_setel"))
		{
			armorType = Inventory.PAPERDOLL_LEGS;
		}
		else if (event.startsWith("safe_setew"))
		{
			armorType = Inventory.PAPERDOLL_RHAND;
		}
		else if (event.startsWith("safe_setes"))
		{
			armorType = Inventory.PAPERDOLL_LHAND;
		}
		else if (event.startsWith("safe_setle"))
		{
			armorType = Inventory.PAPERDOLL_LEAR;
		}
		else if (event.startsWith("safe_setre"))
		{
			armorType = Inventory.PAPERDOLL_REAR;
		}
		else if (event.startsWith("safe_setlf"))
		{
			armorType = Inventory.PAPERDOLL_LFINGER;
		}
		else if (event.startsWith("safe_setrf"))
		{
			armorType = Inventory.PAPERDOLL_RFINGER;
		}
		else if (event.startsWith("safe_seten"))
		{
			armorType = Inventory.PAPERDOLL_NECK;
		}
		else if (event.startsWith("safe_setun"))
		{
			armorType = Inventory.PAPERDOLL_UNDER;
		}
		else if (event.startsWith("safe_setba"))
		{
			armorType = Inventory.PAPERDOLL_CLOAK;
		}
		else if (event.startsWith("safe_setbe"))
		{
			armorType = Inventory.PAPERDOLL_BELT;
		}
		
		if (armorType != -1)
		{
			setEnchant(player, SafeEnchantMax, armorType);
		}
		sendMainHtmlWindow(player, npc);
		return "";
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		sendMainHtmlWindow(player, npc);
		return "";
	}
	
	private void sendMainHtmlWindow(L2PcInstance player, L2Npc npc)
	{
		final NpcHtmlMessage html = getHtmlPacket(player, npc, "main.htm");
		html.replace("%player%", player.getName());
		html.replace("%PRICE%", String.valueOf(IdItemAndCost[1]));
		html.replace("%ITEM_ID%", ItemData.getInstance().getTemplate(IdItemAndCost[0]).getName());
		
		player.sendPacket(html);
	}
	
	private NpcHtmlMessage getHtmlPacket(L2PcInstance player, L2Npc npc, String htmlFile)
	{
		final NpcHtmlMessage packet = new NpcHtmlMessage(npc.getObjectId());
		packet.setHtml(getHtm(player, player.getHtmlPrefix(), htmlFile));
		return packet;
	}
	
	private void setEnchant(L2PcInstance activeChar, int ench, int armorType)
	{
		// now we need to find the equipped weapon of the targeted character...
		int curEnchant = 0; // display purposes only
		L2ItemInstance itemInstance = null;
		
		// only attempt to enchant if there is a weapon equipped
		L2ItemInstance parmorInstance = activeChar.getInventory().getPaperdollItem(armorType);
		if ((parmorInstance != null) && (parmorInstance.getLocationSlot() == armorType))
		{
			itemInstance = parmorInstance;
			
		}
		
		if (itemInstance != null)
		{
			
			curEnchant = itemInstance.getEnchantLevel();
			
			// set enchant value
			if ((curEnchant < SafeEnchantMax) && !itemInstance.isHeroItem() && (itemInstance.getLocationSlot() == Inventory.PAPERDOLL_RHAND))
			{
				if (activeChar.destroyItemByItemId("enchant", IdItemAndCost[0], IdItemAndCost[1], activeChar, true))
				{
					activeChar.getInventory().unEquipItemInSlot(armorType);
					itemInstance.setEnchantLevel(ench);
					activeChar.getInventory().equipItem(itemInstance);
					
					// send packets
					InventoryUpdate iu = new InventoryUpdate();
					iu.addModifiedItem(itemInstance);
					activeChar.sendInventoryUpdate(iu);
					activeChar.broadcastUserInfo();
					
					// informations
					activeChar.sendMessageS("Congratulation, You enchanted your item to +" + SafeEnchantMax + "!", 4);
					activeChar.sendMessage("Changed enchantment of " + activeChar.getName() + "'s " + itemInstance.getItem().getName() + " from " + curEnchant + " to " + ench + ".");
				}
			}
			else
			{
				activeChar.sendPacket(SystemMessageId.NOT_ENOUGH_ITEMS);
			}
		}
	}
}