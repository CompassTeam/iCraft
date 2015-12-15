package iCraft.client.mp3;

import iCraft.core.ICraft;

import javax.media.*;
import javax.media.format.AudioFormat;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MP3Player implements ControllerListener
{
    private final static int NOTSTARTED = 0;
    private final static int PLAYING = 1;
    private final static int PAUSED = 2;

    // The player actually doing all the work
    private Player player;

    // Status variable what player thread is doing/supposed to do
    private int playerStatus = NOTSTARTED;

    // The path to the music
    private URL music;

    // Repeat type for this player
    private int repeatType = 0;

    // Time when the player was paused
    private Time mediaTime;

    public MP3Player()
    {
        Format input1 = new AudioFormat(AudioFormat.MPEGLAYER3);
        Format input2 = new AudioFormat(AudioFormat.MPEG);
        Format output = new AudioFormat(AudioFormat.LINEAR);
        PlugInManager.addPlugIn("com.sun.media.codec.audio.mp3.JavaDecoder", new Format[]{input1, input2}, new Format[]{output}, PlugInManager.CODEC);
    }

    public void setMusic(URL url) throws IOException, NoPlayerException
    {
        music = url;
    }

    public void setRepeatType(int type)
    {
        repeatType = type;
    }

    public int getRepeatType()
    {
        return repeatType;
    }

    public int getPlayerStatus()
    {
        return playerStatus;
    }

    public void resetPlayerStatus()
    {
        playerStatus = NOTSTARTED;
    }

    public boolean hasPlayer()
    {
        return player != null;
    }

    /**
     * Starts playback (resumes if paused)
     */
    public void play() throws IOException, NoPlayerException
    {
        switch (playerStatus)
        {
            case NOTSTARTED:
                player = Manager.createPlayer(new MediaLocator(music));
                player.addControllerListener(this);
                player.realize();
                player.start();

                playerStatus = PLAYING;
                break;
            case PAUSED:
                resume();
                break;
            default:
                break;
        }
    }

    /**
     * Pauses playback. Returns true if new state is PAUSED.
     */
    public boolean pause()
    {
        if (playerStatus == PLAYING)
        {
            mediaTime = player.getMediaTime();
            player.stop();

            playerStatus = PAUSED;
        }

        return playerStatus == PAUSED;
    }

    /**
     * Resumes playback. Returns true if the new state is PLAYING.
     */
    public boolean resume()
    {
        if (playerStatus == PAUSED)
        {
            player.setMediaTime(mediaTime);
            player.start();

            playerStatus = PLAYING;
        }
        return playerStatus == PLAYING;
    }

    /**
     * Closes the player, regardless of current state.
     */
    public void close()
    {
        if (player != null)
        {
            player.stop();
            player.close();
        }
    }

    private void playNextMusic() throws IOException, NoPlayerException
    {
        if (ICraft.currentMusicId + 1 > ICraft.musics.size() - 1 && repeatType == 0)
        {
            close();
            resetPlayerStatus();
        }
        else if (repeatType == 0 || repeatType == 1)
        {
            close();
            setMusic(ICraft.musics.get((ICraft.currentMusicId + 1 > ICraft.musics.size() - 1 ? 0 : ICraft.currentMusicId + 1)).toURI().toURL());

            resetPlayerStatus();

            play();
        }
    }

    @Override
    public void controllerUpdate(ControllerEvent evt)
    {
        if (player == null)
            return;

        if (evt instanceof EndOfMediaEvent)
        {
            try {
                switch (repeatType)
                {
                    case 0: case 1:
                        playNextMusic();
                        break;
                    case 2:
                        close();

                        resetPlayerStatus();

                        play();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getPosition()
    {
        return (new SimpleDateFormat("mm:ss")).format(new Date(TimeUnit.NANOSECONDS.toMillis(player.getMediaNanoseconds())));
    }

    public String getMinDuration()
    {
        return (new SimpleDateFormat("mm:ss")).format(new Date(TimeUnit.NANOSECONDS.toMillis(player.getDuration().getNanoseconds())));
    }

    public int getMusicStatus(int i)
    {
        if (player == null)
            return 0;

        return (int) (TimeUnit.NANOSECONDS.toSeconds(player.getMediaNanoseconds()) * i / player.getDuration().getSeconds());
    }
}