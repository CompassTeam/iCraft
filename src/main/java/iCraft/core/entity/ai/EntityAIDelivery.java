package iCraft.core.entity.ai;

import iCraft.core.entity.EntityPizzaDelivery;
import iCraft.core.utils.ICraftUtils;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class EntityAIDelivery extends EntityAIBase
{
	private EntityPizzaDelivery theEntity;
	private float minDist;
	private World theWorld;
	private EntityPlayer player;

	public EntityAIDelivery(EntityPizzaDelivery theEntity, float min)
	{
		this.theEntity = theEntity;
		minDist = min;
		theWorld = theEntity.worldObj;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute()
	{
		EntityPlayer player = theWorld.getPlayerEntityByName(theEntity.getPlayer());

		if (player == null)
		{
			return false;
		}
		else if (theEntity.worldObj.provider.dimensionId != player.worldObj.provider.dimensionId)
		{
			return false;
		}
		else if (theEntity.getDistanceSqToEntity(player) > (minDist * minDist))
		{
			return false;
		}
		else if (theEntity.getAngry())
		{
			return false;
		}
		else
		{
			this.player = player;
			return true;
		}
	}

	@Override
	public boolean continueExecuting()
	{
		return !theEntity.getAngry() && theEntity.getDistanceSqToEntity(player) <= (minDist * minDist) && player.worldObj.provider.dimensionId == theEntity.worldObj.provider.dimensionId;
	}

	@Override
	public void resetTask()
	{
		player = null;
	}

	@Override
	public void updateTask()
	{
		if (!theEntity.getTrade())
		{
			if (theEntity.getPatience() > 0)
			{
				theEntity.setPatience(theEntity.getPatience() - 1);
			}
			else
			{
				if (theEntity.getDistanceSqToEntity(player) <= 100)
				{
					player.closeScreen();
					player.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + ICraftUtils.localize("msg.delivery") + ": " + EnumChatFormatting.RED + ICraftUtils.localize("msg.delivery.rage")));
				}
				theEntity.setAngry(true);
			}
		}
		else
		{
			player.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + ICraftUtils.localize("msg.delivery") + ": " + EnumChatFormatting.GREEN + ICraftUtils.localize("msg.delivery.allOk")));
			theEntity.setDead();
		}
	}
}