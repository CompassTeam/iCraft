package iCraft.core.entity.ai;

import iCraft.core.entity.EntityPizzaDelivery;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIPizzaFollow extends EntityAIBase
{
    private EntityPizzaDelivery theEntity;
    private EntityPlayer player;
    private World theWorld;
    private float moveSpeed;
    private PathNavigate thePathfinder;
    private int ticker;

    private float minDist;
    private boolean avoidWater;

    public EntityAIPizzaFollow(EntityPizzaDelivery theEntity, float speed, float min)
    {
        this.theEntity = theEntity;
        theWorld = theEntity.worldObj;
        moveSpeed = speed;
        thePathfinder = theEntity.getNavigator();
        minDist = min;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
        EntityPlayer follow = theWorld.getPlayerEntityByName(theEntity.getPlayer());

        if (follow == null)
            return false;
        else if (theEntity.worldObj.provider.getDimensionId() != follow.worldObj.provider.getDimensionId())
            return false;
        else if (theEntity.getDistanceSqToEntity(follow) < (minDist * minDist))
            return false;
        else if (theEntity.getAngry())
            return false;
        else
        {
            player = follow;
            return true;
        }
    }

    @Override
    public boolean continueExecuting()
    {
        return !thePathfinder.noPath() && !theEntity.getAngry() && player.worldObj.provider.getDimensionId() == theEntity.worldObj.provider.getDimensionId();
    }

    @Override
    public void startExecuting()
    {
        ticker = 0;
        avoidWater = ((PathNavigateGround)theEntity.getNavigator()).getAvoidsWater();
        ((PathNavigateGround)theEntity.getNavigator()).setAvoidsWater(false);
    }

    @Override
    public void resetTask()
    {
        player = null;
        thePathfinder.clearPathEntity();
        ((PathNavigateGround)theEntity.getNavigator()).setAvoidsWater(avoidWater);
    }

    @Override
    public void updateTask()
    {
        theEntity.getLookHelper().setLookPositionWithEntity(player, 6.0F, theEntity.getVerticalFaceSpeed() / 10);

        if (--ticker <= 0)
        {
            ticker = 10;

            if (!thePathfinder.tryMoveToEntityLiving(player, moveSpeed))
            {
                if (theEntity.getDistanceSqToEntity(player) >= 144.0D)
                {
                    int x = MathHelper.floor_double(player.posX) - 2;
                    int y = MathHelper.floor_double(player.posZ) - 2;
                    int z = MathHelper.floor_double(player.getEntityBoundingBox().minY);

                    for (int l = 0; l <= 4; ++l)
                    {
                        for (int i1 = 0; i1 <= 4; ++i1)
                        {
                            if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && World.doesBlockHaveSolidTopSurface(theWorld, new BlockPos(x + l, z - 1, y + i1)) && !theWorld.getBlockState(new BlockPos(x + l, z, y + i1)).getBlock().isNormalCube() && !theWorld.getBlockState(new BlockPos(x + l, z + 1, y + i1)).getBlock().isNormalCube())
                            {
                                thePathfinder.clearPathEntity();
                                theEntity.setPosition(player.posX, player.posY, player.posZ);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}