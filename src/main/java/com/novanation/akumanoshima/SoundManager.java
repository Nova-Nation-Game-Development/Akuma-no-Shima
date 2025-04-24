package com.novanation.akumanoshima;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public final class SoundManager {

    HashMap<String, Clip> musicClips = new HashMap<>();
    HashMap<String, Clip> sfxClips = new HashMap<>();
	private final Clip menuClip;

	private static SoundManager instance = null;	// keeps track of Singleton instance

	private SoundManager ()
	{	
		// Menu Setup
		Clip clip = loadClip("audio/music/menu_ego_super_slowed.wav");
		menuClip = clip;

		// Music Setup
        clip = loadClip("audio/music/boss_fight_1_empire_slowed.wav");
		musicClips.put("boss1", clip);

		clip = loadClip("audio/music/boss_fight_2_aura2.wav");
		musicClips.put("boss2", clip);

		// Sound Effect Setup
	}

	// TODO: Add SFX
	public void setSFXVolume(float volume) { }

	public void setMenuVolume(float volume)
	{
		FloatControl volumeControl = (FloatControl) menuClip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(volume);
	}

	public void setMusicVolume(float volume)
	{
		for (Clip clip : musicClips.values())
		{
			FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        	volumeControl.setValue(volume);
		}
	}

	public void setMasterVolume(float volume)
	{
		FloatControl volumeControl = (FloatControl) menuClip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(volume);

		for (Clip clip : musicClips.values())
		{
			volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        	volumeControl.setValue(volume);
		}

		// TODO: Add SFX
	}

	// Class method to retrieve instance of Singleton
	public static SoundManager getInstance()
	{	
		if (instance == null)
			instance = new SoundManager();
		
		return instance;
	}

	// Gets clip from the specified file
    public Clip loadClip (String fileName) 
	{	
 		Clip clip = null;

		try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName))
		{
			if (is == null)
			{
				System.out.println("Audio file not found: " + fileName);
				return null;
			}

			BufferedInputStream bufferedIn = new BufferedInputStream(is);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);
			
			clip = AudioSystem.getClip();
			clip.open(audioIn);
		} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
			System.out.println("Error opening sound file: " + e);
		}

		return clip;
    }

	public Clip getClip (String title, ClipType clipType)
	{
		switch (clipType) {
			case MENU -> { return menuClip; }
			case MUSIC -> {  return musicClips.get(title); }
			case SFX -> { return sfxClips.get(title); }
			default -> { return null; }
		}
	}

    public void playClip(String title, ClipType clipType, boolean looping)
	{    
		Clip clip = getClip(title, clipType);

		if (clip != null) {
			clip.setFramePosition(0);
			if (looping)
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			else
				clip.start();
		}
    }

    public void stopClip(String title, ClipType clipType)
	{
		Clip clip = getClip(title, clipType);

		if (clip != null)
			clip.stop();
    }
}
