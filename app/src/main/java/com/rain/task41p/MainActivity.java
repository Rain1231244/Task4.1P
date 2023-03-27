package com.rain.task41p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rain.task41p.databinding.ActivityMainBinding;

import java.time.Period;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    CountDownTimer workoutTimer;

    CountDownTimer restTimer ;

    long totalTime;
    long restTime;
    long minutes;
    long seconds;
    int period;
    String restTimeEdit_value;
    String period_value;
    public void setTime(long timeInMillis,long totalTime) {
        minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis);
        seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis) - TimeUnit.MINUTES.toSeconds(minutes);

        binding.TimeMin.setText(String.format("%02d",minutes));
        binding.TimeSec.setText(String.format("%02d",seconds));

        double percent=((double)timeInMillis/(double)totalTime)*100;
        binding.ProgressBar.setProgress((int)percent);
    }

    public void enableChange(boolean bool){
        binding.StartButton.setEnabled(bool);
        binding.StopButton.setEnabled(!bool);
        binding.TimeMin.setEnabled(bool);
        binding.TimeSec.setEnabled(bool);
        binding.PeriodEdit.setEnabled(bool);
        binding.restTimeEdit.setEnabled(bool);
        if(bool==false){
            ColorStateList white=ColorStateList.valueOf(Color.parseColor("#FFFFFF"));
            binding.TimeMin.setBackgroundTintList(white);
            binding.TimeSec.setBackgroundTintList(white);
            binding.PeriodEdit.setBackgroundTintList(white);
            binding.restTimeEdit.setBackgroundTintList(white);
        }
        else{
            ColorStateList gray=ColorStateList.valueOf(Color.parseColor("#D3D3D3"));
            binding.TimeMin.setBackgroundTintList(gray);
            binding.TimeSec.setBackgroundTintList(gray);
            binding.PeriodEdit.setBackgroundTintList(gray);
            binding.restTimeEdit.setBackgroundTintList(gray);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.StopButton.setEnabled(false);

        binding.StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restTimeEdit_value=binding.restTimeEdit.getText().toString();
                period_value=binding.PeriodEdit.getText().toString();
                String minutes_value=binding.TimeMin.getText().toString();
                String seconds_value=binding.TimeSec.getText().toString();

                if (!restTimeEdit_value.matches("\\d+")){
                    restTimeEdit_value="0";
                }
                if(!period_value.matches("\\d+")){
                    period_value="1";
                }
                minutes=Long.parseLong(binding.TimeMin.getText().toString());
                seconds=Long.parseLong(binding.TimeSec.getText().toString());
                totalTime =(minutes*60000)+(seconds*1000);
                if(totalTime==0) return;

                restTime=(Long.parseLong(restTimeEdit_value)*1000);
                if (restTime<=0){restTime=0;}
                period=Integer.parseInt(period_value);
                if(period<=0){period=1;}
                binding.restTimeEdit.setText("Rest Time is: "+restTimeEdit_value);
                binding.PeriodEdit.setText("Period left: "+period_value);
                enableChange(false);
                workoutTimer = new CountDownTimer(totalTime, 100) {

                    public void onTick(long millisUntilFinished) {
                        setTime(millisUntilFinished,totalTime);

                    }

                    public void onFinish() {
                        //playSound();
                        if(period>1){
                            period-=1;
                            binding.PeriodEdit.setText("Period left: "+ String.valueOf(period));
                            restTimer.start();
                        }
                        else {
                            enableChange(true);
                            binding.ProgressBar.setProgress(0);
                            binding.restTimeEdit.setText(restTimeEdit_value);
                            binding.PeriodEdit.setText(period_value);
                            //restTimer.start();
                        }
                    }
                };
                restTimer = new CountDownTimer(restTime, 100) {
                    public void onTick(long millisUntilFinished) {
                        // Update the UI with the current time remaining
                        setTime(millisUntilFinished,restTime);
                    }

                    public void onFinish() {
                        workoutTimer.start();
                    }
                };
                workoutTimer.start();

            }
        });
        binding.StopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workoutTimer.cancel();
                restTimer.cancel();
                binding.ProgressBar.setProgress(0);
                binding.restTimeEdit.setText(restTimeEdit_value);
                binding.PeriodEdit.setText(period_value);
                enableChange(true);
            }
        });







       // private void playSound() {
            // Play a sound or vibrate the device to alert the user
       // }






    }
}