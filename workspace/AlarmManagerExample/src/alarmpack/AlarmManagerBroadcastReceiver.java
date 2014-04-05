package alarmpack;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.io.*;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

//~RT 04-02-2014 10:25 PM can't pull info form text files yet , alarm works sending reveiving on boot etc. prob need to remove
//repeating alarm


//~RT 03-25-2014 05:19 PM 
//I changed the alarm to am.set() from am.setRepeating() shouldn't cause boot to not repeat but if it does just change it back and
//figure out a way to cut it off through like am.cancel or something

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver /*implements  OnTouchListener*/ {

	final public static String ONE_TIME = "onetime";
	final public static String HOUR  = "hour";
	final public static String MINUTES  = "minutes";
	@Override
	public void onReceive(Context context, Intent intent) {
		 PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
         PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
         wl.acquire();
         
         if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())){
        	 //try {
				setOnetimeTimer(context, 0, 0, true, 0, "FIX THIS LATER");
			//} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			//} //might need to change later
         }
         
         Bundle extras = intent.getExtras();
         
         Calendar c = Calendar.getInstance(); 
         double c_min = c.get(Calendar.MINUTE), c_hour = c.get(Calendar.HOUR_OF_DAY); 
         //float is 32 bits, double is 64, int is 32, long is 64
         double time = c_hour + (c_min/100);
         //Toast.makeText(context,"BEFORE hours = " + extras.getInt(HOUR) +
        		// "minutes = " + extras.getInt(MINUTES) + " time= " + time, //Toast.LENGTH_SHORT).show();
         double getH = extras.getInt(HOUR);
         double getM = extras.getInt(MINUTES);
         double getHM = getH + (getM/10);
         
         Toast.makeText(context, "getH = " + getH + " getM = " + getM + " getM/10 = " + 
        		(getM/10) + " getHM = " + getHM ,Toast.LENGTH_LONG).show();
         //14.5 , 5.5 , at 9:57 Pm or 21:57
         AudioManager mgr= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
         Ringtone alarm_ring = AlarmManagerActivity.getRingtone(context);

         int alarm_stream_type = alarm_ring.getStreamType();
         int alarm_max_volume = mgr.getStreamMaxVolume(alarm_stream_type);
         //mgr.setStreamVolume(alarm_stream_type, alarm_max_volume, 0);
         
         Uri alert =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
         MediaPlayer mMediaPlayer = new MediaPlayer();
         try{
         mMediaPlayer.setDataSource(context, alert);

        //if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.prepare();
        //mMediaPlayer.setScreenOnWhilePlaying(true);  //hid at 11:07
        //mMediaPlayer.setLooping(true);
        //}
        }catch(Exception e) {
        } 
         
         int count = 0;
         if (c.get(Calendar.DAY_OF_WEEK) > 1 && c.get(Calendar.DAY_OF_WEEK) <7){
        	 while(time>= 7.45d && time <8.15d){
        	 ////Toast.makeText(context, "getHM = " + getHM + " time = " + time, //Toast.LENGTH_SHORT).show();
             //while(time>= getHM && time <getHM + .03d){
	        	 ////Toast.makeText(context, "ENTERED LOOP", //Toast.LENGTH_SHORT).show();
	        	 if (count == 0){
	        	 	mMediaPlayer.start();
	        	 } 
	        	 if (mgr.getStreamVolume(alarm_stream_type) != alarm_max_volume){ //stop trying to lower the volume sleepy stupid idiot
	        		  mgr.setStreamVolume(alarm_stream_type, alarm_max_volume, 0);
	        		 
	        	 }

	        	 c_min = c.get(Calendar.MINUTE); c_hour = c.get(Calendar.HOUR_OF_DAY);
	        	 time = c_hour + (c_min/100); 
	        	 ////Toast.makeText(context, "time = " + time, //Toast.LENGTH_SHORT).show();
	        	 
	        	 //Toast.makeText(context,mMediaPlayer.getCurrentPosition() + " media width/2 = " + 
	        			// mMediaPlayer.getVideoWidth()/2, //Toast.LENGTH_SHORT).show();
	             ////Toast.makeText(context, " GET UP", //Toast.LENGTH_SHORT).show();
	        	 count++;
	        	 
	        	 if (mMediaPlayer.getCurrentPosition() >=mMediaPlayer.getDuration()/2 ){
	        		//Toast.makeText(context, "BEFORE sending new alarm", //Toast.LENGTH_SHORT).show();
	        		Log.i(" starting new ALARM", "mMediaPlayer.getcurpos = " + mMediaPlayer.getCurrentPosition() + " video duration = " + 
	        				mMediaPlayer.getDuration());
	        		time = 23;
	        		mgr = null;
	        		mMediaPlayer.stop();
	        		mMediaPlayer.release();
	             	AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	                Intent new_intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
	                // i tried to do intent.putExtra after pending intent and wondered why it wasn't working when I pass the 
	                //intent to pending intent pi then i changed the intent, i am such a dummy,  //~RT 03-22-2014 04:42 PM 
	                new_intent.putExtra(ONE_TIME, true);
	                new_intent.putExtra(HOUR, c.get(Calendar.HOUR));
	                new_intent.putExtra(MINUTES, c.get(Calendar.MINUTE));
	                PendingIntent pi = PendingIntent.getBroadcast(context, 0, new_intent, 0);
	                am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, pi);
	                //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000, count, pi);
	                //Toast.makeText(context, "after sending new alarm"+ "after sending new alarm", //Toast.LENGTH_SHORT).show(); 
	        	 } 
	         }
         }

         else if( c.get(Calendar.DAY_OF_WEEK) == 1 || c.get(Calendar.DAY_OF_WEEK) == 7){//on saturday and sunday
         //if (c.get(Calendar.DAY_OF_WEEK) > 1 && c.get(Calendar.DAY_OF_WEEK) <7){
        	 ////Toast.makeText(context, "BEFORE WHILE" + " ,time = " +time, //Toast.LENGTH_SHORT).show();
        	 long time_track = SystemClock.elapsedRealtime();
        	 long time_track_future = SystemClock.elapsedRealtime() + 10000;
	         while(time>= 1.05d && time <1.08d){
	        	 ////Toast.makeText(context, "ENTERED LOOP", //Toast.LENGTH_SHORT).show();
	        	 if (count == 0){
	        	 	mMediaPlayer.start();
	        	 }	 
	        	 if (mgr.getStreamVolume(alarm_stream_type) != alarm_max_volume){ //stop trying to lower the volume sleepy stupid idiot
	        		 mgr.setStreamVolume(alarm_stream_type, alarm_max_volume, 0);
	        	 }
	        	 c_min = c.get(Calendar.MINUTE); c_hour = c.get(Calendar.HOUR_OF_DAY);
	        	 time = c_hour + (c_min/100); 
	        	 //Toast.makeText(context, "time = " + time, //Toast.LENGTH_SHORT).show();
	             ////Toast.makeText(context, " GET UP", //Toast.LENGTH_SHORT).show();
	        	 count++;
	        	 if (mMediaPlayer.getCurrentPosition() >=mMediaPlayer.getVideoWidth()/2 ){
	        		//Toast.makeText(context, "BEFORE sending new alarm", //Toast.LENGTH_SHORT).show();
	             	AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	                Intent new_intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
	                // i tried to do intent.putExtra after pending intent and wondered why it wasn't working when I pass the 
	                //intent to pending intent pi then i changed the intent, i am such a dummy,  //~RT 03-22-2014 04:42 PM 
	                intent.putExtra(ONE_TIME, true);
	                intent.putExtra(HOUR, c.get(Calendar.HOUR));
	                intent.putExtra(MINUTES, c.get(Calendar.MINUTE));
	                PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
	                am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pi); 
	                //Toast.makeText(context, "after sending new alarm"+ "after sending new alarm", //Toast.LENGTH_SHORT).show();
	        	 }
	         }
         }
 		//final EditText interval_text = (EditText) findViewById(R.id.enter_the_interval);
 		//interval_string = interval_text.getText().toString();
         
         //Release the lock
         wl.release();
        
         
         
	}
        
	public void SetAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(HOUR, "hours");
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        intent.putExtra(HOUR, "hours");
        intent.putExtra(MINUTES, MINUTES);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After after 30 seconds
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5 , pi); 
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
    
    @SuppressLint("NewApi")
	public void setOnetimeTimer(Context context, int hour, int minutes, Boolean from_boot, 
			int alarm_number, String currentAlarmString) {
    	AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    	
    	AlarmManagerActivity ama = new AlarmManagerActivity();
    	String al_str = ama.getAlarmString();
    	Toast.makeText(context, "alarm string passed to receiver = " + al_str, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        //Toast.makeText(context, currentAlarmString, //Toast.LENGTH_SHORT).show();
        
        intent.putExtra(ONE_TIME, true/*Boolean.TRUE*/);
        intent.putExtra(HOUR, hour);
        intent.putExtra(MINUTES, minutes);
        
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        
        Calendar c = Calendar.getInstance(); 
        double c_min = c.get(Calendar.MINUTE), c_hour = c.get(Calendar.HOUR_OF_DAY);
        double alarm_hour = hour, alarm_minutes = minutes;//int total = (hour*3600000) + (minutes*60000);
        if (( alarm_hour+(alarm_minutes/100) ) < ( c_hour+(c_min/100) ) ){//if alarmtime < current , alarm is tomorrow
        	double c_hourD = (double) c_hour;
        	double c_minD = (double) c_min;
        	double time_til_day_over = 24.0d - (c_hourD +(c_minD/100));
        	alarm_hour += Math.floor(time_til_day_over); //hours left in the day plus hour alarm fires tomorrow
        	alarm_minutes+= time_til_day_over - Math.floor(time_til_day_over);//minutes left in day plus mins tomorrow
        }
        else{
        	alarm_hour -=c_hour; //hours left til alarm fires today
        	alarm_minutes -=c_min; //minutes left til alarm fres today
        }
        //minutes still not converted currently 12 hours and 12 mins would be =,(hour*3600000) + (minutes*60000);
        
        
        //Toast.makeText(context, c_hour + ":" + c_min, //Toast.LENGTH_SHORT).show();
        //Toast.makeText(context, "alarm will go off in "+ alarm_hour +" hours & "+alarm_minutes + " minutes", 
        		//Toast.LENGTH_SHORT).show();
        long alarm_hour_long = (long) (alarm_hour);
        long alarm_minutes_long = (long) (alarm_minutes);
        Long temp = ( System.currentTimeMillis() + (alarm_hour_long*3600000) + (alarm_minutes_long*60000) )
        		- System.currentTimeMillis() ;
        Log.i(" times milleseconds ",  " time til alarm  = " + temp);
        Log.i("alarm_hour + alarm mins", (alarm_hour_long*3600000) + (alarm_minutes_long*60000) + " ");
        int seconds = c.get(Calendar.SECOND) *1000;
        //am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (alarm_hour_long*3600000) + 
        //		(alarm_minutes_long*60000) - seconds, pi);
        
        if (from_boot){
        	 /*~RT 03-25-2014 05:14 PM */am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000,30000, pi);
        	//am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000, pi);
        }
        else{
        	 //~RT 03-25-2014 05:15 PM 
        	Log.i("time stuff", "hour = " + hour + " minutes = " + minutes);
        	Toast.makeText(context, "in setonetimetimer(), hour = " + hour + " min = "+ minutes, Toast.LENGTH_SHORT).show();
        	Bundle extras = intent.getExtras();
        	Toast.makeText(context, "getEXTRASSS in setonetimetimer, = " + extras.getInt(HOUR) + " min = "+ extras.getInt(MINUTES), Toast.LENGTH_SHORT).show();
	        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (alarm_hour_long*3600000) + 
	        		(alarm_minutes_long*60000) - seconds,15000, pi);
	        
	        //am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (alarm_hour_long*3600000) + 
	        	//	(alarm_minutes_long*60000) - seconds, pi);
	        //am.cancel(null);
        }
    }
}
