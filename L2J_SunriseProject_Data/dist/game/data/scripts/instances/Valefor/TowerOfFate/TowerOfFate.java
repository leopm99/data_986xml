package instances.Valefor.TowerOfFate;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import l2r.gameserver.enums.CtrlEvent;
import l2r.gameserver.enums.CtrlIntention;
import l2r.gameserver.instancemanager.InstanceManager;
import l2r.gameserver.model.L2CommandChannel;
import l2r.gameserver.model.L2Object;
import l2r.gameserver.model.L2Party;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.actor.L2Attackable;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.L2Summon;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.effects.L2EffectType;
import l2r.gameserver.model.entity.Instance;
import l2r.gameserver.model.instancezone.InstanceWorld;
import l2r.gameserver.model.quest.Quest;
import l2r.gameserver.model.quest.QuestState;
import l2r.gameserver.model.skills.L2Skill;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.ExSendUIEvent;
import l2r.gameserver.network.serverpackets.SystemMessage;
import l2r.gameserver.util.Broadcast;
import l2r.gameserver.util.Util;

public class TowerOfFate extends Quest
{
	private class DHSWorld extends InstanceWorld
	{
		public Map<L2Npc, Boolean> npcList = new HashMap<>();
		public L2Npc BOSS_ID = null;
		public boolean isBossesAttacked = false;
		public long[] storeTime =
		{
			0,
			0
		}; // 0: instance start, 1: finish time
		
		public DHSWorld()
		{
			// InstanceManager.getInstance().super();
		}
	}
	
	private static final int INSTANCEID = 163; // this is the client number
	private static final boolean debug = false;
	
	// Items
	
	// NPCs
	private static final int ENTRANCE_NPC = 38135;
	private static final int ENDING_NPC = 38137;
	
	// mobs
	private static final int BOSS_ID = 38134;
	private static final int CRYSTAL_ALIVE = 38122;
	// private static final int CRYSTAL_DEAD = 0;
	
	//@formatter:off
	private static final int[] TOWER_MOBIDS = {38111,38112,38113,38116,38114,38115,38117,38118,38119,38120,38121,38123,38124,38126,38125,38127,38128,38129,38130,38131,38132,38133};
	
	// Doors/Walls/Zones
	private static final int[][] ROOM_1_MOBS = {
		{38111,115326,14665,-5096}, {38112,115561,14327,-5096}, {38113,115800,14588,-5096},
		{38111,116353,14656,-5096}, {38112,116342,15104,-5096}, {38113,116413,14508,-5096},
		{38111,115706,15065,-5096}, {38112,115997,15415,-5096}, {38113,113145,14857,-5096},
		{38111,113257,15461,-5096}, {38112,112889,15176,-5096}, {38113,113443,15006,-5096},
		{38111,113874,14546,-5096}, {38112,113515,14149,-5096}, {38113,113226,14440,-5096},
		{38111,116084,16665,-5096}, {38112,116462,16871,-5096}, {38113,116183,17401,-5096},
		{38111,115807,17111,-5096}, {38112,115456,17612,-5096}, {38113,115232,17294,-5096},
		{38111,114031,17433,-5096}, {38112,113708,17765,-5096}, {38113,113844,18197,-5096},
		{38111,113601,17895,-5096}, {38112,113063,17823,-5096}, {38113,112913,17421,-5096},
		{38111,113224,16796,-5096}, {38112,113550,17089,-5096}, {38113,114031,17719,-5096},
		{38111,113877,14141,-4376}, {38112,112773,15094,-4376}, {38113,112572,16550,-4376},
		{38111,113681,17959,-4376}, {38112,115428,18061,-4376}, {38113,116531,16921,-4376},
		{38111,115125,15955,-5096}, {38112,115110,15521,-5096}, {38113,114264,15468,-5096},
		{38111,113873,15794,-5096}, {38112,114146,16588,-5096}, {38113,114766,16752,-5096},
		{38111,114042,18182,-4376}, {38112,113383,17920,-4376}, {38113,113279,17290,-4376},
		{38111,112700,16947,-4376}, {38112,112541,15784,-4376}, {38113,113147,14953,-4376},
		{38111,113407,14302,-4376}, {38112,114501,14199,-4376}, {38113,115140,14116,-4376},
		{38111,115925,14707,-4376}, {38112,116566,15770,-4376}, {38113,116832,16497,-4376},
		{38111,116581,15332,-4376}
	};
	private static final int[][] ROOM_2_MOBS = {
		{38116,114073,17457,-3608}, {38114,113822,16927,-3608}, {38115,113617,17448,-3608},
		{38116,112745,17204,-3608}, {38114,113139,17452,-3608}, {38115,113794,17879,-3608},
		{38116,113538,17408,-3608}, {38114,113969,16827,-3608}, {38115,114519,16867,-3608},
		{38116,114827,16659,-3608}, {38114,114878,16127,-3608}, {38115,114499,15851,-3608},
		{38116,113880,16023,-3608}, {38114,113896,15069,-3608}, {38115,113959,14778,-3608},
		{38116,113654,14191,-3608}, {38114,113007,14385,-3608}, {38115,112796,15036,-3608},
		{38116,115312,17456,-3608}, {38114,115795,17621,-3608}, {38115,116359,17766,-3608},
		{38116,116375,17186,-3608}, {38114,115755,16763,-3608}, {38115,115787,17186,-3608},
		{38116,115421,15213,-3608}, {38114,115269,14710,-3608}, {38115,115697,14187,-3608},
		{38116,116171,14869,-3608}, {38114,116068,15432,-3608}, {38115,115029,15770,-3608},
		{38116,114686,15814,-3608}, {38114,114656,16586,-3608}, {38115,114863,16573,-3608},
		{38116,114669,16201,-3608}, {38114,114714,16079,-3608}, {38115,114483,16178,-3608},
		{38116,116892,15888,-3608}, {38114,116889,16244,-3608}, {38115,116638,16093,-3608},
		{38116,113159,16186,-3608}, {38114,112674,16014,-3608}, {38115,113981,16085,-3608},
		{38116,114196,15686,-3608}
	};
	private static final int[][] ROOM_3_MOBS = {
		{38117,113894,16256,-2120}, {38118,114186,16572,-2120}, {38118,114881,16765,-2120},
		{38117,115059,16652,-2120}, {38118,115394,16267,-2120}, {38118,115414,15769,-2120},
		{38117,115101,15477,-2120}, {38118,114581,15362,-2120}, {38118,114046,15783,-2120},
		{38117,114345,15913,-2120}, {38118,114582,15828,-2120}, {38118,114749,16061,-2120},
		{38117,115379,17366,-2120}, {38118,115469,17594,-2120}, {38118,115931,17596,-2120},
		{38117,116343,17270,-2120}, {38118,116172,17005,-2120}, {38118,116090,16549,-2120},
		{38117,115930,16956,-2120}, {38118,115536,17229,-2120}, {38118,115654,17687,-2120},
		{38117,115484,14845,-2120}, {38118,115657,14286,-2120}, {38118,115539,13804,-2120},
		{38117,115867,14480,-2120}, {38118,116386,14436,-2120}, {38118,116640,14991,-2120},
		{38117,116303,15323,-2120}, {38118,113518,15204,-2120}, {38118,113156,15444,-2120},
		{38117,112793,15104,-2120}, {38118,113207,14744,-2120}, {38118,113530,14303,-2120},
		{38117,113941,14451,-2120}, {38118,113436,14611,-2120}, {38118,112980,14778,-2120},
		{38117,113627,15080,-2120}
	};
	private static final int[][] ROOM_4_MOBS = {
		{38119,114671,17405,-640}, {38120,114500,17014,-640}, {38121,114859,16887,-640},
		{38119,114005,16507,-640}, {38120,114288,16781,-640}, {38121,115232,17186,-640},
		{38119,115238,17555,-640}, {38120,115407,18101,-640}, {38121,113820,17341,-640},
		{38119,113978,16824,-640}, {38120,113696,17876,-640}, {38121,113272,17495,-640},
		{38119,112891,17389,-640}, {38120,113064,17036,-640}, {38121,113539,16463,-640},
		{38119,113792,16353,-640}, {38120,114082,15834,-640}, {38121,113595,15227,-640},
		{38119,112815,15003,-640}, {38120,113462,14251,-640}, {38121,113702,13700,-640},
		{38119,114219,15026,-640}, {38120,114597,15390,-640}, {38121,116456,16925,-640},
		{38119,115680,16405,-640}, {38120,117095,16174,-640}, {38121,116398,15892,-640},
		{38119,116507,14758,-640}
	};
	private static final int[][] ROOM_5_MOBS = {
		{38129,114643,17320,928}, {38124,114469,16968,928}, {38120,114807,17126,928},
		{38123,114458,16398,928}, {38124,114815,16460,928}, {38121,114568,16451,928},
		{38123,114708,16079,928}, {38124,114845,15765,928}, {38120,114679,15472,928},
		{38123,114524,15367,928}
	};
	private static final int[][] ROOM_6_MOBS = {
		{38123,113044,16254,1944}, {38124,112997,15822,1944}, {38121,113249,15834,1944},
		{38121,113667,15875,1944}, {38124,113473,14960,1944}, {38120,114233,15156,1944},
		{38123,115225,14621,1944}, {38124,115720,15002,1944}, {38120,114849,15787,1944},
		{38123,114548,16136,1944}, {38124,114972,16307,1944}, {38121,114278,15994,1944},
		{38120,115685,15899,1944}, {38124,115811,16448,1944}, {38121,116205,16101,1944},
		{38123,115843,17039,1944}, {38124,115637,17407,1944}, {38120,115502,17222,1944},
		{38123,113642,17441,1944}, {38124,113179,17078,1944}, {38121,113726,17026,1944},
		{38123,114460,17623,1944}, {38124,114565,17233,1944}, {38120,114913,16222,1944},
		{38123,113264,14986,1944}, {38124,113539,14784,1944}, {38120,113701,15082,1944},
		{38123,114351,14383,1944}, {38124,114675,14960,1944}, {38120,114952,15808,1944},
		{38123,114832,17423,1944}, {38124,115043,17767,1944}, {38120,114694,17199,1944},
		{38123,115683,16281,1944}, {38124,116095,15890,1944}, {38120,116083,16202,1944},
		{38123,113614,16282,1944}
	};
	private static final int[][] ROOM_7_MOBS = {
		{38130,114328,16016,2992}
	};
	private static final int[][] ROOM_8_MOBS = {
		{38126,114220,17150,3960}, {38125,114207,17443,3960}, {38125,113766,17510,3960},
		{38126,113971,17385,3960}, {38125,113989,17699,3960}, {38127,114412,17538,3960},
		{38126,114694,17188,3960}, {38125,114588,17049,3960}, {38126,115109,16850,3960},
		{38126,115506,17150,3960}, {38126,115550,17249,3960}, {38127,115122,17524,3960},
		{38126,115067,17128,3960}, {38125,115216,16859,3960}, {38126,114803,17376,3960},
		{38126,115267,17214,3960}, {38126,115380,17420,3960}, {38127,116178,15936,3960},
		{38126,115751,16021,3960}, {38125,115493,15654,3960}, {38126,115440,15401,3960},
		{38126,115868,15427,3960}, {38126,116198,15317,3960}, {38127,116200,15712,3960},
		{38126,114470,16587,3960}, {38125,114308,16278,3960}, {38125,114192,15843,3960},
		{38126,114441,15861,3960}, {38126,114681,15635,3960}, {38127,114988,16325,3960},
		{38126,114612,16190,3960}, {38125,114720,15756,3960}, {38127,113312,15660,3960},
		{38126,113147,15179,3960}, {38126,114075,15054,3960}, {38127,113973,15268,3960},
		{38126,114242,15208,3960}, {38125,114410,14531,3960}, {38127,114104,14796,3960},
		{38126,115136,14857,3960}, {38126,115003,14479,3960}, {38127,114600,14575,3960},
		{38126,114194,14972,3960}
	};
	private static final int[][] ROOM_9_MOBS = {
		{38132,113604,16039,4976}, {38120,115780,16046,4976}, {38120,114748,16804,4976},
		{38120,115644,17158,4976}, {38120,115371,16090,4976}, {38120,115754,15269,4976},
		{38120,114218,17448,4976}, {38120,113616,17042,4976}, {38120,114748,15515,4976},
		{38120,115515,14846,4976}
	};
	private static final int[][] ROOM_10_MOBS = {
		{38125,114157,15883,5984}, {38125,114190,16222,5984}, {38125,114355,16485,5984},
		{38127,114825,16453,5984}, {38127,115023,16317,5984}, {38127,115066,15963,5984},
		{38128,114758,15654,5984}, {38128,114379,15623,5984}, {38128,114101,15959,5984},
		{38125,114400,16253,5984}, {38125,114794,15981,5984}, {38127,114674,15730,5984},
		{38125,112743,14314,5984}, {38126,112569,14446,5984}, {38125,112885,14577,5984},
		{38127,113176,14412,5984}, {38127,113119,13996,5984}, {38126,113422,13995,5984},
		{38128,113702,13550,5984}, {38125,114215,13431,5984}, {38125,114462,13751,5984},
		{38125,114887,13498,5984}, {38128,115070,13717,5984}, {38127,115276,14021,5984},
		{38126,115742,13864,5984}, {38127,115803,14054,5984}, {38128,116187,14115,5984},
		{38127,116312,14594,5984}, {38128,116936,15026,5984}, {38125,116306,14073,5984},
		{38128,115659,14148,5984}, {38126,115406,13645,5984}, {38126,114790,13662,5984},
		{38125,114310,13571,5984}, {38125,114614,13885,5984}, {38125,114923,13686,5984},
		{38126,115555,13775,5984}
	};
	private static final int[][] ROOM_11_MOBS = {
		{38133,114647,16084,6992}
	};
	private static final int[][] ROOM_12_MOBS = {
		{38125,114404,15658,7992}, {38125,114729,15653,7992}, {38127,114914,15873,7992},
		{38127,114855,16366,7992}, {38127,114277,15722,7992}, {38128,114345,16277,7992},
		{38128,114668,16122,7992}, {38128,114319,15711,7992}, {38125,115482,16515,7992},
		{38125,115650,15882,7992}, {38126,115482,15455,7992}, {38126,115011,15156,7992},
		{38126,114398,15043,7992}, {38125,114997,16925,7992}, {38125,114605,17126,7992},
		{38126,114306,16750,7992}, {38127,114036,17028,7992}, {38125,113776,16340,7992},
		{38125,114467,17443,9000}, {38128,115030,17702,9000}, {38127,114520,17283,9000},
		{38127,114766,16889,9000}, {38125,113824,16738,9000}, {38125,113539,16928,9000},
		{38128,113973,17510,9000}, {38127,113529,16335,9000}, {38126,113633,15416,9000},
		{38125,115745,16312,9000}, {38128,116259,16021,9000}, {38126,116149,15408,9000},
		{38126,115235,15617,9000}, {38126,115186,14879,9000}, {38126,114585,14522,9000},
		{38125,114280,14819,9000}, {38125,113490,14699,9000}, {38126,116162,16212,9000},
		{38128,115639,15840,9000}
	};
	private static final int[][] CRYSTALS_SPAWNS = {
		{114643,15740,-5096},{114644,15983,-3608},{114691,16024,-2120},
		{114640,16806,-640},{114648,15485,928},{113338,16098,1944},
		{114427,16123,2992},{114616,15962,3960},{114645,16049,5008},
		{114655,16094,5984},{115436,16847,6992},{114604,16034,7992}
	};
	private static final int[][] BOSS_SPAWNS = {{38134,113618,15043,9560}};
	private static final int[] ENDING_NPC_SPAWN = {113618,15043,9560};
	//@formatter:on
	
	// Instance reenter time
	// default: 24h
	private static final int INSTANCEPENALTY = 24;
	
	public class teleCoord
	{
		int instanceId;
		int x;
		int y;
		int z;
	}
	
	public TowerOfFate()
	{
		super(-1, TowerOfFate.class.getSimpleName(), "gracia/instances");
		
		addStartNpc(ENTRANCE_NPC);
		addTalkId(ENTRANCE_NPC);
		addStartNpc(ENDING_NPC);
		addTalkId(ENDING_NPC);
		addKillId(CRYSTAL_ALIVE);
		addKillId(BOSS_ID);
		addAttackId(BOSS_ID);
		addSkillSeeId(TOWER_MOBIDS);
		addKillId(TOWER_MOBIDS);
	}
	
	private boolean checkConditions(L2PcInstance player)
	{
		if (debug || player.isGM())
		{
			return true;
		}
		
		L2Party party = player.getParty();
		if (party == null)
		{
			player.sendPacket(SystemMessageId.NOT_IN_PARTY_CANT_ENTER);
			return false;
		}
		
		if (party.getLeader() != player)
		{
			player.sendPacket(SystemMessageId.ONLY_PARTY_LEADER_CAN_ENTER);
			return false;
		}
		
		L2CommandChannel channel = party.getCommandChannel();
		if (channel == null)
		{
			player.sendPacket(SystemMessageId.NOT_IN_COMMAND_CHANNEL_CANT_ENTER);
			return false;
		}
		
		if (channel.getLeader() != player)
		{
			player.sendPacket(SystemMessageId.ONLY_PARTY_LEADER_CAN_ENTER);
			return false;
		}
		
		if ((party.getCommandChannel().getMembers().size() < 4) || (party.getCommandChannel().getMembers().size() > 36))// 18 27
		{
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_S_LEVEL_REQUIREMENT_IS_NOT_SUFFICIENT_AND_CANNOT_BE_ENTERED);
			party.getCommandChannel().broadcastPacket(sm);
			return false;
		}
		
		for (L2PcInstance partyMember : party.getCommandChannel().getMembers())
		{
			if ((partyMember.getLevel() < 75) || (partyMember.getLevel() > 85))
			{
				SystemMessage sm = SystemMessage.getSystemMessage(2097);
				sm.addPcName(partyMember);
				party.getCommandChannel().broadcastPacket(sm);
				return false;
			}
			
			if (!Util.checkIfInRange(1000, player, partyMember, true))
			{
				SystemMessage sm = SystemMessage.getSystemMessage(2096);
				sm.addPcName(partyMember);
				party.getCommandChannel().broadcastPacket(sm);
				return false;
			}
			
			Long reentertime = InstanceManager.getInstance().getInstanceTime(partyMember.getObjectId(), INSTANCEID);
			if (System.currentTimeMillis() < reentertime)
			{
				SystemMessage sm = SystemMessage.getSystemMessage(2100);
				sm.addPcName(partyMember);
				party.getCommandChannel().broadcastPacket(sm);
				return false;
			}
			
		}
		return true;
	}
	
	private void teleportplayer(L2PcInstance player, teleCoord teleto)
	{
		player.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
		player.setInstanceId(teleto.instanceId);
		player.teleToLocation(teleto.x, teleto.y, teleto.z);
		return;
	}
	
	protected void enterInstance(L2PcInstance player, String template, teleCoord teleto)
	{
		// check for existing instances for this player
		InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);
		// existing instance
		if (world != null)
		{
			if (!(world instanceof DHSWorld))
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_ENTERED_ANOTHER_INSTANT_ZONE_THEREFORE_YOU_CANNOT_ENTER_CORRESPONDING_DUNGEON));
				return;
			}
			teleto.instanceId = world.getInstanceId();
			teleportplayer(player, teleto);
			return;
		}
		
		if (checkConditions(player))
		{
			world = new DHSWorld();
			world.setInstanceId(InstanceManager.getInstance().createDynamicInstance(template));
			world.setTemplateId(INSTANCEID);
			world.setStatus(0);
			((DHSWorld) world).storeTime[0] = System.currentTimeMillis();
			InstanceManager.getInstance().addWorld(world);
			_log.info("The Tower of Fate Event started " + template + " Instance: " + world.getInstanceId() + " created by player: " + player.getName());
			runTumors((DHSWorld) world);
			// teleport players
			teleto.instanceId = world.getInstanceId();
			
			L2Party party = player.getParty();
			if (party == null)
			{
				teleportplayer(player, teleto);
				world.addAllowed(player.getObjectId());
			}
			else
			{
				for (L2PcInstance partyMember : player.getParty().isInCommandChannel() ? player.getParty().getCommandChannel().getMembers() : player.getParty().getMembers())
				{
					teleportplayer(partyMember, teleto);
					world.addAllowed(partyMember.getObjectId());
				}
			}
		}
	}
	
	protected void exitInstance(L2PcInstance player, teleCoord tele)
	{
		player.setInstanceId(0);
		player.teleToLocation(tele.x, tele.y, tele.z);
		L2Summon pet = player.getSummon();
		if (pet != null)
		{
			pet.setInstanceId(0);
			pet.teleToLocation(tele.x, tele.y, tele.z);
		}
	}
	
	protected boolean checkKillProgress(L2Npc mob, DHSWorld world)
	{
		if (world.npcList.containsKey(mob))
		{
			world.npcList.put(mob, true);
		}
		for (boolean isDead : world.npcList.values())
		{
			if (!isDead)
			{
				return false;
			}
		}
		return true;
	}
	
	protected int[][] getRoomSpawns(int room)
	{
		switch (room)
		{
			case 0:
				return ROOM_1_MOBS;
			case 1:
				return ROOM_2_MOBS;
			case 2:
				return ROOM_3_MOBS;
			case 3:
				return ROOM_4_MOBS;
			case 4:
				return ROOM_5_MOBS;
			case 5:
				return ROOM_6_MOBS;
			case 6:
				return ROOM_7_MOBS;
			case 7:
				return ROOM_8_MOBS;
			case 8:
				return ROOM_9_MOBS;
			case 9:
				return ROOM_10_MOBS;
			case 10:
				return ROOM_11_MOBS;
			case 11:
				return ROOM_12_MOBS;
		}
		_log.warn("");
		return new int[][] {};
	}
	
	protected void runTumors(DHSWorld world)
	{
		for (int[] mob : getRoomSpawns(world.getStatus()))
		{
			L2Npc npc = addSpawn(mob[0], mob[1], mob[2], mob[3], 0, false, 0, false, world.getInstanceId());
			world.npcList.put(npc, false);
		}
		L2Npc mob = addSpawn(CRYSTAL_ALIVE, CRYSTALS_SPAWNS[world.getStatus()][0], CRYSTALS_SPAWNS[world.getStatus()][1], CRYSTALS_SPAWNS[world.getStatus()][2], 0, false, 0, false, world.getInstanceId());
		mob.disableCoreAI(true);
		mob.setIsImmobilized(true);
		mob.setCurrentHp(mob.getMaxHp() * 1.0);
		world.npcList.put(mob, false);
		world.setStatus(world.getStatus() + 1);
	}
	
	protected void runTwins(DHSWorld world)
	{
		world.setStatus(world.getStatus() + 1);
		world.BOSS_ID = addSpawn(BOSS_SPAWNS[0][0], BOSS_SPAWNS[0][1], BOSS_SPAWNS[0][2], BOSS_SPAWNS[0][3], 0, false, 0, false, world.getInstanceId());
		world.BOSS_ID.setIsMortal(true);
	}
	
	protected void bossSimpleDie(L2Npc boss)
	{
		// killing is only possible one time
		synchronized (this)
		{
			if (boss.isDead())
			{
				return;
			}
			// now reset currentHp to zero
			boss.setCurrentHp(0);
			boss.setIsDead(true);
		}
		
		// Set target to null and cancel Attack or Cast
		boss.setTarget(null);
		
		// Stop movement
		boss.stopMove(null);
		
		// Stop HP/MP/CP Regeneration task
		boss.getStatus().stopHpMpRegeneration();
		
		boss.stopAllEffectsExceptThoseThatLastThroughDeath();
		
		// Send the Server->Client packet StatusUpdate with current HP and MP to all other L2PcInstance to inform
		boss.broadcastStatusUpdate();
		
		// Notify L2Character AI
		boss.getAI().notifyEvent(CtrlEvent.EVT_DEAD);
		
		if (boss.getWorldRegion() != null)
		{
			boss.getWorldRegion().onDeath(boss);
		}
	}
	
	@Override
	public String onSkillSee(L2Npc npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
		if (skill.hasEffectType(L2EffectType.REBALANCE_HP, L2EffectType.HEAL, L2EffectType.HEAL_PERCENT))
		{
			int hate = 2 * skill.getAggroPoints();
			if (hate < 2)
			{
				hate = 1000;
			}
			((L2Attackable) npc).addDamageHate(caster, 0, hate);
		}
		return super.onSkillSee(npc, caster, skill, targets, isPet);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet, L2Skill skill)
	{
		InstanceWorld tmpworld = InstanceManager.getInstance().getWorld(npc.getInstanceId());
		if (tmpworld instanceof DHSWorld)
		{
			if (!((DHSWorld) tmpworld).isBossesAttacked)
			{
				((DHSWorld) tmpworld).isBossesAttacked = true;
				Calendar reenter = Calendar.getInstance();
				reenter.add(Calendar.HOUR, INSTANCEPENALTY);
				
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.INSTANT_ZONE_FROM_HERE_S1_S_ENTRY_HAS_BEEN_RESTRICTED);
				sm.addString(InstanceManager.getInstance().getInstanceIdName(tmpworld.getTemplateId()));
				
				// set instance reenter time for all allowed players
				for (int objectId : tmpworld.getAllowed())
				{
					L2PcInstance player = L2World.getInstance().getPlayer(objectId);
					if ((player != null) && player.isOnline())
					{
						InstanceManager.getInstance().setInstanceTime(objectId, tmpworld.getTemplateId(), reenter.getTimeInMillis());
						player.sendPacket(sm);
					}
				}
			}
			else if (damage >= npc.getCurrentHp())
			{
				if (((DHSWorld) tmpworld).BOSS_ID.isDead())
				{
					((DHSWorld) tmpworld).BOSS_ID.setIsDead(false);
					((DHSWorld) tmpworld).BOSS_ID.doDie(attacker);
				}
			}
		}
		return null;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		InstanceWorld tmpworld = InstanceManager.getInstance().getWorld(npc.getInstanceId());
		if (tmpworld instanceof DHSWorld)
		{
			DHSWorld world = (DHSWorld) tmpworld;
			if (world.getStatus() < 12)
			{
				if (checkKillProgress(npc, world))
				{
					runTumors(world);
				}
			}
			else if (world.getStatus() == 12)
			{
				if (checkKillProgress(npc, world))
				{
					runTwins(world);
				}
			}
			else if ((world.getStatus() == 13) && ((npc.getId() == BOSS_ID)))
			{
				if (world.BOSS_ID.isDead())
				{
					world.setStatus(world.getStatus() + 1);
					// instance end
					world.storeTime[1] = System.currentTimeMillis();
					world.BOSS_ID = null;
					Broadcast.toAllOnlinePlayers("The Tower of Fate as been completed by brave heros!");
					addSpawn(ENDING_NPC, ENDING_NPC_SPAWN[0], ENDING_NPC_SPAWN[1], ENDING_NPC_SPAWN[2], 0, false, 0, false, world.getInstanceId());
					
					for (Integer pc : world.getAllowed())
					{
						L2PcInstance killer = L2World.getInstance().getPlayer(pc);
						if (killer != null)
						{
							killer.sendPacket(new ExSendUIEvent(killer, true, true, 0, 0, ""));
						}
					}
					
					Instance inst = InstanceManager.getInstance().getInstance(world.getInstanceId());
					inst.setDuration(5 * 60000);
					inst.setEmptyDestroyTime(0);
				}
			}
		}
		return "";
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		if (st == null)
		{
			return htmltext;
		}
		
		int npcId = npc.getId();
		if (npcId == ENTRANCE_NPC)
		{
			teleCoord tele = new teleCoord();
			tele.x = 114657;
			tele.y = 13699;
			tele.z = -5096;
			enterInstance(player, "TowerOfFate.xml", tele);
			return "";
		}
		else if (npcId == ENDING_NPC)
		{
			InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);
			Long finishDiff = ((DHSWorld) world).storeTime[1] - ((DHSWorld) world).storeTime[0];
			if (finishDiff < 1260000)
			{
				st.giveItems(13777, 1);
			}
			else if (finishDiff < 1380000)
			{
				st.giveItems(13778, 1);
			}
			else if (finishDiff < 1500000)
			{
				st.giveItems(13779, 1);
			}
			else if (finishDiff < 1620000)
			{
				st.giveItems(13780, 1);
			}
			else if (finishDiff < 1740000)
			{
				st.giveItems(13781, 1);
			}
			else if (finishDiff < 1860000)
			{
				st.giveItems(13782, 1);
			}
			else if (finishDiff < 1980000)
			{
				st.giveItems(13783, 1);
			}
			else if (finishDiff < 2100000)
			{
				st.giveItems(13784, 1);
			}
			else if (finishDiff < 2220000)
			{
				st.giveItems(13785, 1);
			}
			else
			{
				st.giveItems(13786, 1);
			}
			world.removeAllowed(player.getObjectId());
			teleCoord tele = new teleCoord();
			tele.instanceId = 0;
			tele.x = 83418;
			tele.y = 148619;
			tele.z = -3405;
			exitInstance(player, tele);
		}
		return "";
	}
}
