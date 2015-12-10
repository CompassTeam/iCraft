package iCraft.client.voice;

import iCraft.core.ICraft;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ConnectException;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;

public class VoiceClient extends Thread
{
	public Socket socket;

	public String ip;

	public AudioFormat format = new AudioFormat(16000F, 16, 1, true, true);

	public VoiceInput inputThread;
	public VoiceOutput outputThread;

	public DataInputStream input;
	public DataOutputStream output;

	public boolean running;

	public VoiceClient(String s)
	{
		ip = s;
	}

	@Override
	public void run()
	{
		ICraft.logger.info("VoiceServer: Starting client connection...");

		try {
			socket = new Socket(ip, ICraft.VOICE_PORT);
			running = true;

			input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

			(outputThread = new VoiceOutput(this)).start();
			(inputThread = new VoiceInput(this)).start();

			ICraft.logger.info("VoiceServer: Successfully connected to server.");
		} catch(ConnectException e) {
			ICraft.logger.error("VoiceServer: Server's VoiceServer is disabled.");
		} catch(Exception e) {
			ICraft.logger.error("VoiceServer: Error while starting client connection.");
			e.printStackTrace();
		}
	}

	public void disconnect()
	{
		ICraft.logger.info("VoiceServer: Stopping client connection...");

		try {
			try {
				inputThread.interrupt();
				outputThread.interrupt();
			} catch(Exception e) {}

			try {
				interrupt();
			} catch(Exception e) {}

			try {
				inputThread.close();
				outputThread.close();
			} catch(Exception e) {}

			try {
				output.flush();
				output.close();
				output = null;
			} catch(Exception e) {}

			try {
				input.close();
				input = null;
			} catch(Exception e) {}

			try {
				socket.close();
				socket = null;
			} catch(Exception e) {}


			running = false;
		} catch(Exception e) {
			ICraft.logger.error("VoiceServer: Error while stopping client connection.");
			e.printStackTrace();
		}
	}
}