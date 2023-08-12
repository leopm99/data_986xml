package custom.instancemaster;

import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.actor.templates.L2NpcTemplate;

public class InstanceMaster extends L2Npc
{
	private static final int PENALTY_REMOVAL_PRICE = 1000; // O preço em Adena para remover a penalidade.
	
	public InstanceMaster(int objectId, L2NpcTemplate template)
	{
		super(template);
	}
	
	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		
		if (command.startsWith("remove_penalty"))
		{
			// L2InstanceManager inst = player.getInstanceId();
			if (player.getAdena() >= PENALTY_REMOVAL_PRICE)
			{
				
				// Remova a penalidade de instância para o jogador.
				// Coloque aqui a lógica específica para remover a penalidade.
				
				// Atualize a quantidade de Adena do jogador após o pagamento.
				player.reduceAdena("InstanceMaster", PENALTY_REMOVAL_PRICE, this, true);
				
				// Envie uma mensagem de sucesso ao jogador.
				player.sendMessage("Você removeu a penalidade de espera da instância.");
				
			}
			else
			{
				// O jogador não tem Adena suficiente.
				// Envie uma mensagem informando que o pagamento não pode ser realizado.
				player.sendMessage("Você não tem Adena suficiente para remover a penalidade.");
			}
		}
		else
		{
			// Se o comando não for reconhecido, exiba a janela HTML para o jogador.
			showHtmlWindow(player);
		}
	}
	
	private void showHtmlWindow(L2PcInstance player)
	{
		// Implemente a lógica para criar e exibir a janela HTML.
		// Você pode usar o NpcHtmlMessage ou outros recursos do L2JServer para criar a janela.
	}
}
