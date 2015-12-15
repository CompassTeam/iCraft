package iCraft.core.entity.ai;

import iCraft.core.entity.EntityPizzaDelivery;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityAIAttackCrazy extends EntityAIBase
{
    private EntityPizzaDelivery attacker;
    private int attackTick;
    private double speedTowardsTarget;
    private World theWorld;
    private PathEntity entityPathEntity;
    private int ticker;
    private double field_151497_i;
    private double field_151495_j;
    private double field_151496_k;

    private int failedPathFindingPenalty;

    public EntityAIAttackCrazy(EntityPizzaDelivery entity, double speed)
    {
        attacker = entity;
        speedTowardsTarget = speed;
        theWorld = entity.worldObj;
        setMutexBits(3);
    }

    public boolean shouldExecute()
    {
        EntityLivingBase entity = theWorld.getPlayerEntityByName(attacker.getPlayer());

        if (entity == null)
            return false;
        else if (!entity.isEntityAlive())
            return false;
        else if (attacker.getAngry())
        {
            if (--ticker <= 0)
            {
                entityPathEntity = attacker.getNavigator().getPathToEntityLiving(entity);
                ticker = 4 + attacker.getRNG().nextInt(7);
                return entityPathEntity != null;
            }
            else
                return true;
        }
        return false;
    }

    public boolean continueExecuting()
    {
        EntityLivingBase entity = theWorld.getPlayerEntityByName(attacker.getPlayer());
        return (entity != null && (entity.isEntityAlive() && !attacker.getNavigator().noPath()) && attacker.getAngry());
    }

    public void startExecuting()
    {
        attacker.getNavigator().setPath(entityPathEntity, speedTowardsTarget);
        ticker = 0;
    }

    public void resetTask()
    {
        attacker.getNavigator().clearPathEntity();
    }

    public void updateTask()
    {
        EntityLivingBase target = theWorld.getPlayerEntityByName(attacker.getPlayer());
        attacker.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        double d0 = attacker.getDistanceSq(target.posX, target.boundingBox.minY, target.posZ);
        double d1 = (double) (attacker.width * 2.0F * attacker.width * 2.0F + target.width);
        --ticker;

        if (attacker.getEntitySenses().canSee(target) && ticker <= 0 && (field_151497_i == 0.0D && field_151495_j == 0.0D && field_151496_k == 0.0D || target.getDistanceSq(field_151497_i, field_151495_j, field_151496_k) >= 1.0D || attacker.getRNG().nextFloat() < 0.05F))
        {
            field_151497_i = target.posX;
            field_151495_j = target.boundingBox.minY;
            field_151496_k = target.posZ;
            ticker = failedPathFindingPenalty + 4 + attacker.getRNG().nextInt(7);

            if (attacker.getNavigator().getPath() != null)
            {
                PathPoint finalPathPoint = attacker.getNavigator().getPath().getFinalPathPoint();
                if (finalPathPoint != null && target.getDistanceSq(finalPathPoint.xCoord, finalPathPoint.yCoord, finalPathPoint.zCoord) < 1)
                    failedPathFindingPenalty = 0;
                else
                    failedPathFindingPenalty += 10;
            }
            else
                failedPathFindingPenalty += 10;
            if (d0 > 1024.0D)
                ticker += 10;
            else if (d0 > 256.0D)
                ticker += 5;
            if (!attacker.getNavigator().tryMoveToEntityLiving(target, speedTowardsTarget))
                ticker += 15;
        }

        attackTick = Math.max(attackTick - 1, 0);

        if (d0 <= d1 && attackTick <= 20)
        {
            attackTick = 20;

            if (attacker.getHeldItem() != null)
                attacker.swingItem();

            target.attackEntityFrom(DamageSource.cactus, 1.0F);
        }
    }
}