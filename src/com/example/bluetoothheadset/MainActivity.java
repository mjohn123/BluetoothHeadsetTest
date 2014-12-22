package com.example.bluetoothheadset;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.io.File;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
public class MainActivity extends Activity {
	   private MediaRecorder _recorder;
	   private AudioManager _audioManager;
	   private TextView _text1, _text2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		_text1 = (TextView) findViewById(R.id.textView1);
	      _text2 = (TextView) findViewById(R.id.textView2);
	 
	      Button btn1 = (Button) findViewById(R.id.button1);
	      btn1.setOnClickListener(new View.OnClickListener()
	      {
	         @Override
	         public void onClick(View v)
	         {
	            start();
	        	
	         }
	      });
	 
	      Button btn2 = (Button) findViewById(R.id.button2);
	      btn2.setOnClickListener(new View.OnClickListener()
	      {
	         @Override
	         public void onClick(View v)
	         {
	        	 stop();
	         }
	      });
	 
	      _audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	      _audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	 @Override
	   protected void onDestroy()
	   {
	      stop();
	      super.onDestroy();
	   }
	 
	   private void start()
	   {
		      BroadcastReceiver scoReceiver = new BroadcastReceiver() {

			    public void onReceive(Context context, Intent intent) {

			        int scoState = intent.getIntExtra(
			                AudioManager.EXTRA_SCO_AUDIO_STATE, -1);

			        switch (scoState) {
			        case -1:
			            Log.d("D","SCO State: Error\n");
			            break;
			        case 0:
			        	Log.d("D","SCO State: Disconnected\n");
			            break;
			        case 1:
			        	Log.d("D","SCO State: Connected\n");
			            break;
			        case 2:
			            Log.d("D","SCO State: Connecting\n");
			            break;
			        }
			if (scoState == AudioManager.SCO_AUDIO_STATE_CONNECTED) {
				    Log.d("D","Startrecording\n");
					//record audio
					File path = new File(Environment.getExternalStorageDirectory() + "/VoiceRecord");
				      if (!path.exists())
				         path.mkdirs();
				 
				      Log.w("BluetoothReceiver.java | startRecord", "|" + path.toString() + "|");
				 
				      File file = null;
				      try
				      {
				         file = File.createTempFile("voice_", ".m4a", path);
				      }
				      catch (Exception e)
				      {
				         e.printStackTrace();
				      }
				      Log.w("BluetoothReceiver.java | startRecord", "|" + file.toString() + "|");
				      //_text1.setText(file.toString());
				 
				      try
				      {
				    	  
				         _audioManager.startBluetoothSco();
				         _recorder = new MediaRecorder();
				         _recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
				         _recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
				         _recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				         _recorder.setOutputFile(file.toString());
				         _recorder.prepare();
				         _recorder.start();
				 
				         _text2.setText("recording");
				      }
				      catch (Exception e)
				      {
				         e.printStackTrace();
				      }
	
				}
			}   
		   };
	      
	   }
	 
	   private void stop()
	   {
	      try
	      {
	         _recorder.stop();
	         _recorder.release();
	         _audioManager.stopBluetoothSco();
	         _text2.setText("stop");
	      }
	      catch (Exception e)
	      {
	         e.printStackTrace();
	      }
	   }
	   

	
}

