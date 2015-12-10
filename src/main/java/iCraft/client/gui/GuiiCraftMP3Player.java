package iCraft.client.gui;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;

import com.google.common.io.Files;

import iCraft.core.ICraft;
import iCraft.core.utils.ICraftClientUtils;
import iCraft.core.utils.ICraftUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

@SideOnly(Side.CLIENT)
public class GuiiCraftMP3Player extends GuiiCraftBase
{
	private float scroll;
	private boolean isDragging = false;
	private boolean canScroll = false;
	private int dragOffset = 0;
	private int displayInt;
	private FileInputStream input;
	private Random rnd = new Random();

	public GuiiCraftMP3Player(String resource)
	{
		super(resource);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		canScroll = ICraft.musics.size() > 5;
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		canScroll = ICraft.musics.size() > 5;
	}

	public int getScroll()
	{
		return Math.max(Math.min((int)(scroll * 55), 55), 0);
	}

	public int getIndex()
	{
		if(ICraft.musics.size() <= 5)
		{
			return 0;
		}

		return (int)((ICraft.musics.size() * scroll) - ((5 / (float)ICraft.musics.size())) * scroll);
	}

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);

        drawTexturedModalRect(guiWidth + 113, guiHeight + 50 + getScroll(), (canScroll ? 0 : 11), 180, 11, 15);

        int xAxis = mouseX - guiWidth;
        int yAxis = mouseY - guiHeight;

        for (int i = 0; i < 5; i++)
        {
            if (getIndex() + i < ICraft.musics.size())
            {
                int yStart = i * 14 + 50;
                int rnd1 = rnd.nextInt(8), rnd2 = rnd.nextInt(8), rnd3 = rnd.nextInt(8);
                boolean mouseOver = xAxis >= 52 && xAxis <= 111 && yAxis >= yStart && yAxis <= yStart + 14;

                drawTexturedModalRect(guiWidth + 52, guiHeight + yStart, mouseOver ? 0 : 60, 166, 60, 14);

                if (ICraft.mp3Player != null && ICraft.mp3Player.hasPlayer() && ICraft.mp3Player.getPlayerStatus() != 0 && ICraft.mp3Player.getPlayerStatus() != 3 && ICraft.musics.get(getIndex() + i) == ICraft.musics.get(ICraft.currentMusicId))
                {
                    drawTexturedModalRect(guiWidth + 101, guiHeight + yStart + 12 - (ICraft.mp3Player.getPlayerStatus() == 1 ? rnd1 : (ICraft.mp3Player.getPlayerStatus() == 2 ? 5 : 0)), 22, 188 - (ICraft.mp3Player.getPlayerStatus() == 1 ? rnd1 : (ICraft.mp3Player.getPlayerStatus() == 2 ? 5 : 0)), 2, (ICraft.mp3Player.getPlayerStatus() == 1 ? rnd1 : (ICraft.mp3Player.getPlayerStatus() == 2 ? 5 : 0)));
                    drawTexturedModalRect(guiWidth + 104, guiHeight + yStart + 12 - (ICraft.mp3Player.getPlayerStatus() == 1 ? rnd2 : (ICraft.mp3Player.getPlayerStatus() == 2 ? 8 : 0)), 22, 188 - (ICraft.mp3Player.getPlayerStatus() == 1 ? rnd2 : (ICraft.mp3Player.getPlayerStatus() == 2 ? 8 : 0)), 2, (ICraft.mp3Player.getPlayerStatus() == 1 ? rnd2 : (ICraft.mp3Player.getPlayerStatus() == 2 ? 8 : 0)));
                    drawTexturedModalRect(guiWidth + 107, guiHeight + yStart + 12 - (ICraft.mp3Player.getPlayerStatus() == 1 ? rnd3 : (ICraft.mp3Player.getPlayerStatus() == 2 ? 2 : 0)), 22, 188 - (ICraft.mp3Player.getPlayerStatus() == 1 ? rnd3 : (ICraft.mp3Player.getPlayerStatus() == 2 ? 2 : 0)), 2, (ICraft.mp3Player.getPlayerStatus() == 1 ? rnd3 : (ICraft.mp3Player.getPlayerStatus() == 2 ? 2 : 0)));
                }

                if (isMouseOver(52, (i * 14 + 50), 60, 14, mouseX, mouseY))
                {
                    List<String> tooltip = new ArrayList<String>();
                    tooltip.add(ICraft.musicNames.get(getIndex() + i));
                    tooltip.add(ICraftClientUtils.getAuthor(ICraft.musics.get(getIndex() + i)));

                    drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
                }
            }
        }

        if (ICraft.mp3Player != null && ICraft.mp3Player.hasPlayer())
        {
            drawTexturedModalRect(guiWidth + 51, guiHeight + 121, 182, (ICraft.mp3Player.getPlayerStatus() == 1 ? 58 : (ICraft.mp3Player.getPlayerStatus() == 2 ? 41 : (ICraft.mp3Player.getPlayerStatus() == 3 ? 41 : 0))), 74, 17);
            drawTexturedModalRect(guiWidth + 51, guiHeight + 34, 182, 91, 74, 15);
            drawTexturedModalRect(guiWidth + 53, guiHeight + 41, 184, (ICraft.mp3Player.getRepeatType() == 0 ? 107 : (ICraft.mp3Player.getRepeatType() == 1 ? 116 : (ICraft.mp3Player.getRepeatType() == 2 ? 125 : 0))), 7, 7);

            displayInt = ICraft.mp3Player.getMusicStatus(68);
            drawTexturedModalRect(guiWidth + 54, guiHeight + 37, 185, 89, displayInt, 2);
        }

        if (isMouseOver(63, 41, 5, 7, mouseX, mouseY))
        {
            List<String> tooltip = new ArrayList<String>();
            tooltip.add(ICraftUtils.localize("mp3.reload"));
            drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        if (ICraft.mp3Player != null && ICraft.mp3Player.hasPlayer())
            drawResizedString(ICraft.mp3Player.getPosition() + "/" + ICraft.mp3Player.getMinDuration(), 186, 84, 0xffffff, 0.5F);
        for (int i = 0; i < 5; i++)
        {
            if (getIndex() + i < ICraft.musics.size())
            {
                int yStart = i * 28 + 49;

                drawResizedString((ICraft.musicNames.get(getIndex() + i).length() > 13 ? ICraft.musicNames.get(getIndex() + i).substring(0, 13) : ICraft.musicNames.get(getIndex() + i)), 110, yStart + 56, 0xffffff, 0.5F);
                drawResizedString(ICraftClientUtils.getAuthor(ICraft.musics.get(getIndex() + i)), 110, yStart + 66, 0xffffff, 0.5F);
            }
        }

        drawTime();

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

	@Override
	protected void mouseClicked(int x, int y, int button)
	{
		super.mouseClicked(x, y, button);

		if (button == 0)
		{
			int xAxis = x - guiWidth;
			int yAxis = y - guiHeight;
			// Exit
			if(xAxis >= 80 && xAxis <= 95 && yAxis >= 143 && yAxis <= 158)
			{
				mc.thePlayer.openGui(ICraft.instance, 0, mc.theWorld, 0, 0, 0);
			}
			// Slider
			if(xAxis >= 113 && xAxis <= 123 && yAxis >= getScroll() + 50 && yAxis <= getScroll() + 50 + 15)
			{
				if (canScroll)
				{
					dragOffset = yAxis - (getScroll() + 50);
					isDragging = true;
				}
			}
			// Reload gallery
			if(xAxis >= 63 && xAxis <= 67 && yAxis >= 41 && yAxis <= 47)
			{
				reloadGallery();
			}
			// Musics
			for (int i = 0; i < 5; i++)
			{
				if (getIndex() + i < ICraft.musics.size())
				{
					int yStart = i * 14 + 50;

					if(xAxis >= 52 && xAxis <= 111 && yAxis >= yStart && yAxis <= yStart + 14)
					{
						try {
							ICraft.mp3Player.stop();
							input = new FileInputStream(ICraft.musics.get(getIndex() + i).getPath());
							ICraft.currentMusicId = (getIndex() + i);
							ICraft.mp3Player.setMusic(input);
							ICraft.mp3Player.resetPlayerStatus();
							ICraft.mp3Player.setRepeatType(ICraft.mp3Player.getRepeatType());
							ICraft.mp3Player.play();

							mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "[" + EnumChatFormatting.GOLD + "iCraft" + EnumChatFormatting.BLUE + "] " + EnumChatFormatting.GREEN + "Playing " + EnumChatFormatting.DARK_PURPLE + ICraft.musicNames.get(getIndex() + i)));
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
			if (ICraft.mp3Player != null && ICraft.mp3Player.hasPlayer() && ICraft.mp3Folder.listFiles().length != 0)
			{
				// Play / Pause
				if(xAxis >= 85 && xAxis <= 91 && yAxis >= 121 && yAxis <= 129)
				{
					int status = ICraft.mp3Player.getPlayerStatus();
					switch (status)
					{
						case 1:
							ICraft.mp3Player.pause();
							break;
						case 2:
							ICraft.mp3Player.resume();
							break;
						case 3:
							if (ICraft.mp3Player.getRepeatType() == 0)
							{
								try {
									ICraft.mp3Player.rePlay();
								} catch (Exception e) {
									throw new RuntimeException(e);
								}
							}
							break;
						default:
							break;
					}
				}
				// Change Repeat
				if(xAxis >= 53 && xAxis <= 59 && yAxis >= 41 && yAxis <= 47)
				{
					ICraft.mp3Player.setRepeatType((ICraft.mp3Player.getRepeatType() + 1 > 2 ? 0 : ICraft.mp3Player.getRepeatType() + 1));
				}
				// Forward
				if(xAxis >= 103 && xAxis <= 112 && yAxis >= 122 && yAxis <= 129)
				{
					ICraft.mp3Player.stop();
					try {
						input = new FileInputStream(ICraft.musics.get((ICraft.currentMusicId + 1 > ICraft.musics.size() - 1 ? 0 : ICraft.currentMusicId + 1)).getPath());
						ICraft.currentMusicId = (ICraft.currentMusicId + 1 > ICraft.musics.size() - 1 ? 0 : ICraft.currentMusicId + 1);
						ICraft.mp3Player.setMusic(input);
						ICraft.mp3Player.resetPlayerStatus();
						ICraft.mp3Player.play();

						mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "[" + EnumChatFormatting.GOLD + "iCraft" + EnumChatFormatting.BLUE + "] " + EnumChatFormatting.GREEN + "Playing " + EnumChatFormatting.DARK_PURPLE + ICraft.musicNames.get(ICraft.currentMusicId)));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
				// Backward
				if(xAxis >= 64 && xAxis <= 73 && yAxis >= 122 && yAxis <= 129)
				{
					ICraft.mp3Player.stop();
					try {
						input = new FileInputStream(ICraft.musics.get((ICraft.currentMusicId - 1 < 0 ? ICraft.musics.size() - 1 : ICraft.currentMusicId - 1)).getPath());
						ICraft.currentMusicId = (ICraft.currentMusicId - 1 < 0 ? ICraft.musics.size() - 1 : ICraft.currentMusicId - 1);
						ICraft.mp3Player.setMusic(input);
						ICraft.mp3Player.resetPlayerStatus();
						ICraft.mp3Player.play();

						mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "[" + EnumChatFormatting.GOLD + "iCraft" + EnumChatFormatting.BLUE + "] " + EnumChatFormatting.GREEN + "Playing " + EnumChatFormatting.DARK_PURPLE + ICraft.musicNames.get(ICraft.currentMusicId)));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int button, long ticks)
	{
		super.mouseClickMove(mouseX, mouseY, button, ticks);

		int yAxis = mouseY - guiHeight;

		if(isDragging)
		{
			scroll = Math.min(Math.max((float)(yAxis - 50 - dragOffset) / 55, 0), 1);
		}
	}

	@Override
	protected void mouseMovedOrUp(int x, int y, int type)
	{
		super.mouseMovedOrUp(x, y, type);

		if(type == 0 && isDragging)
		{
			dragOffset = 0;
			isDragging = false;
		}
	}

	@Override
	public void handleMouseInput()
	{
		super.handleMouseInput();

		int i = Mouse.getEventDWheel();
		if (i != 0 && canScroll)
		{
			if (i > 0)
				i = 1;
			if (i < 0)
				i = -1;

			scroll = Math.min(Math.max((float)((double)scroll - (double)i / (double)20), 0), 1);
		}
	}

	private void reloadGallery()
	{
		List<File> musics = new ArrayList<File>();
		List<String> names = new ArrayList<String>();
		if (ICraft.mp3Folder != null && ICraft.mp3Folder.listFiles().length > 0)
		{
			for (File file : ICraft.mp3Folder.listFiles())
			{
				if (!file.isDirectory() && Files.getFileExtension(file.getAbsolutePath()).equals("mp3"))
				{
					musics.add(file);
					String patchedName = file.getName().replaceAll(".mp3", "");
					names.add(patchedName);
				}
			}
		}
		if (ICraft.mp3Player != null)
		{
			ICraft.mp3Player.setRepeatType(0);
			ICraft.mp3Player.close();
		}
		ICraft.musics = musics;
		ICraft.musicNames = names;
		ICraft.currentMusicId = 0;
	}
}