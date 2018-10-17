package todo;

import java.util.Calendar;

import done.ClockInput;
import done.ClockOutput;
import se.lth.cs.realtime.semaphore.MutexSem;
import se.lth.cs.realtime.semaphore.Semaphore;

public class Data {

	private static ClockInput input;
	private static ClockOutput output;
	private MutexSem mutex;
	private boolean alarm;
	private int timeH;
	private int timeM;
	private int timeS;
	private int alarmTime; // hhmmss
	private int alarmTimeLeft;
	private int prevChoice;


	public Data(ClockInput i, ClockOutput o, Semaphore sem, MutexSem mutex) {
		input = i;
		output = o;

		// Läser in systemets tid
		Calendar calendar = Calendar.getInstance();
		String text = calendar.getTime().toString();
		String h = text.substring(11, 13);
		String m = text.substring(14, 16);
		String s = text.substring(17, 19);
		timeH = Integer.parseInt(h);
		timeM = Integer.parseInt(m);
		timeS = Integer.parseInt(s);

		alarmTime = 0;
		alarmTimeLeft = 0;
		alarm = false;
		this.mutex = mutex;
	}

	public void countTime() {
		mutex.take();
		timeS++;
		if (timeS >= 60) {
			timeS = 0;
			timeM++;
		}
		if (timeM >= 60) {
			timeM = 0;
			timeH++;
		}
		if (timeH >= 24) {
			timeH = 0;
		}
		output.showTime((timeH * 10000) + (timeM * 100) + (timeS));
		checkAlarm();
		mutex.give();
	}

	private void checkAlarm() {
		if (alarm) {
			//Check alarmtime with clocktime
			if (alarmTime == (timeH * 10000) + (timeM * 100) + (timeS)) {
				//Sets the amount of beeps
				alarmTimeLeft = 20;
			}

			if (alarmTimeLeft > 0) {
				output.doAlarm();
				alarmTimeLeft--;
			}
		}
	}

	public void checkButtons() {


		switch (input.getChoice()) {
		case ClockInput.SET_ALARM:
			mutex.take();
			alarmCheck();
			prevChoice = ClockInput.SET_ALARM;
			mutex.give();
			break;
		case ClockInput.SET_TIME:
			mutex.take();
			alarmCheck();
			prevChoice = ClockInput.SET_TIME;
			mutex.give();
			break;
		case ClockInput.SHOW_TIME:
			mutex.take();
			if(prevChoice == ClockInput.SET_TIME) {
				int time = input.getValue();
				
				timeH = time / 10000;
				timeM = time / 100 % 100;
				timeS = time % 100;
			} else if(prevChoice == ClockInput.SET_ALARM) {
				alarmTime = input.getValue();
			}
			prevChoice = ClockInput.SHOW_TIME;
			mutex.give();
			break;
		default:
			mutex.take();
			alarmCheck();
			prevChoice = -1;
			mutex.give();
		}
	}
	
	private void alarmCheck() {
		//Toggle alarm when shift+ctrl is pressed
		if (input.getAlarmFlag() != alarm) {
			alarm = input.getAlarmFlag();
			System.out.println("Alarm is " + (alarm ? "on" : "off"));
		}

		//Turn off alarm if it's beeping
		if (alarm && alarmTimeLeft > 0) {
			alarmTimeLeft = 0;
		}
	}

}
