package iCraft.client.mp3;

import iCraft.core.ICraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import org.tritonus.share.sampled.file.TAudioFileFormat;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MP3Player
{
	private final static int NOTSTARTED = 0;
	private final static int PLAYING = 1;
	private final static int PAUSED = 2;
	private final static int FINISHED = 3;

	// the player actually doing all the work
	private Player player;

	// locking object used to communicate with player thread
	private final Object playerLock = new Object();

	// status variable what player thread is doing/supposed to do
	private int playerStatus = NOTSTARTED;

	// Repeat type for this player
	private int repeatType = 0;

	public void setMusic(final InputStream inputStream) throws JavaLayerException
	{
		player = new Player(inputStream);
	}

	public int getPlayerStatus()
	{
		return playerStatus;
	}

	public void resetPlayerStatus()
	{
		playerStatus = NOTSTARTED;
	}

	public void setRepeatType(int newRepeatType)
	{
		repeatType = newRepeatType;
	}

	public int getRepeatType()
	{
		return repeatType;
	}

	/**
	 * Starts playback (resumes if paused)
	 */
	public void play() throws JavaLayerException
	{
		if (player == null)
			return;

		synchronized (playerLock)
		{
			switch (playerStatus)
			{
			case NOTSTARTED:
				final Runnable r = new Runnable() {
					public void run() {
						playInternal();
					}
				};
				Thread t = new Thread(r);
				t.setPriority(Thread.MAX_PRIORITY);
				playerStatus = PLAYING;
				t.start();
				break;
			case PAUSED:
				resume();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Replays music.
	 */
	public void rePlay() throws JavaLayerException
	{
		try {
			FileInputStream input = new FileInputStream(ICraft.musics.get((ICraft.currentMusicId)));
			resetPlayerStatus();
			setMusic(input);
			play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Pauses playback. Returns true if new state is PAUSED.
	 */
	public boolean pause()
	{
		synchronized (playerLock)
		{
			if (playerStatus == PLAYING)
				playerStatus = PAUSED;

			return playerStatus == PAUSED;
		}
	}

	/**
	 * Resumes playback. Returns true if the new state is PLAYING.
	 */
	public boolean resume()
	{
		synchronized (playerLock)
		{
			if (playerStatus == PAUSED)
			{
				playerStatus = PLAYING;
				playerLock.notifyAll();
			}
			return playerStatus == PLAYING;
		}
	}

	private void playInternal()
	{
		while (playerStatus != FINISHED)
		{
			try {
				if (!player.play(1))
				{
					break;
				}
			} catch (JavaLayerException e) {
				break;
			}
			// check if paused or terminated
			synchronized (playerLock)
			{
				while (playerStatus == PAUSED)
				{
					try {
						playerLock.wait();
					} catch (InterruptedException e) {
						// terminate player
						break;
					}
				}
			}
		}
		close();
		if (repeatType == 1)
		{
			try {
				FileInputStream input = new FileInputStream(ICraft.musics.get((ICraft.currentMusicId + 1 > ICraft.musics.size() - 1 ? 0 : ICraft.currentMusicId + 1)).getPath());
				ICraft.currentMusicId = (ICraft.currentMusicId + 1 > ICraft.musics.size() - 1 ? 0 : ICraft.currentMusicId + 1);
				resetPlayerStatus();
				setMusic(input);
				play();
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "[" + EnumChatFormatting.GOLD + "iCraft" + EnumChatFormatting.BLUE + "] " + EnumChatFormatting.GREEN + "Playing " + EnumChatFormatting.DARK_PURPLE + ICraft.musicNames.get(ICraft.currentMusicId)));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		else if (repeatType == 2)
		{
			try {
				rePlay();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Stop the player, used for click-select music.
	 */
	public void stop()
	{
		synchronized (playerLock)
		{
			playerStatus = PAUSED;
		}
		try {
			player.close();
		} catch (Exception e) {}
	}

	/**
	 * Closes the player, regardless of current state.
	 */
	public void close()
	{
		synchronized (playerLock)
		{
			playerStatus = FINISHED;
		}
		try {
			player.close();
		} catch (Exception e) {}
	}

	public boolean hasPlayer()
	{
		return player != null;
	}

	public void setVolume(float ctrl)
	{
		try {
			Mixer.Info[] infos = AudioSystem.getMixerInfo();
			for (Mixer.Info info : infos) {
				Mixer mixer = AudioSystem.getMixer(info);
				if (mixer.isLineSupported(Port.Info.SPEAKER))
				{
					Port port = (Port) mixer.getLine(Port.Info.SPEAKER);
					port.open();
					if (port.isControlSupported(FloatControl.Type.VOLUME))
					{
						FloatControl volume = (FloatControl) port.getControl(FloatControl.Type.VOLUME);
						volume.setValue(ctrl);
					}
					port.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getDuration(File file)
	{
		try {
			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
			if (fileFormat instanceof TAudioFileFormat)
			{
				Map<?, ?> properties = fileFormat.properties();
				String key = "duration";
				return (int)(TimeUnit.MICROSECONDS.toMillis((Long)properties.get(key)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public String getMinDuration()
	{
		try {
			long minDuration = getDuration(ICraft.musics.get(ICraft.currentMusicId));
			return (new SimpleDateFormat("mm:ss")).format(new Date(minDuration));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getPosition()
	{
		if (player != null)
		{
			long position = player.getPosition();
			return (new SimpleDateFormat("mm:ss")).format(new Date(position));
		}
		return null;
	}

	public int getMusicStatus(int i)
	{
		try {
			return (playerStatus != FINISHED ? (getDuration(ICraft.musics.get(ICraft.currentMusicId)) == 0 ? 0 : player.getPosition() * i / getDuration(ICraft.musics.get(ICraft.currentMusicId))) : 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}