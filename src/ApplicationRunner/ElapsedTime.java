package ApplicationRunner;

import static java.lang.Thread.sleep;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javafx.scene.text.Text;

public class ElapsedTime extends Thread
{
    Text timeToDisplay;
    ElapsedTime(Text text)
    {
        this.currentSecond = 0;
        this.timeToDisplay = text;
    }
    public static long currentSecond = 0;

    public static long currentMinute = currentSecond/60;

    public static long currentHour = currentMinute/60;
    
    public static boolean countOn = false;
    
    @Override
    public void run() {
        
        do {
            String tmpTime = "";
            currentSecond++;
            if(currentSecond == 60)
            {
                currentMinute++;
                currentSecond = 0;
            }
            if(currentMinute == 60)
            {
                currentHour++;
                currentMinute = 0;
            }
            //to ensure the digits are displayed at 01 if under 10
            if (currentHour >= 10) {
                tmpTime = currentHour + ":";

            } else {
                tmpTime = "0" + currentHour + ":";
            }
            if (currentMinute >= 10) {
                tmpTime += currentMinute + ":";
            } else {
                tmpTime += "0" + currentMinute + ":";
            }
            if (currentSecond >= 10) {
                tmpTime += currentSecond;
            } else {
                tmpTime += "0" + currentSecond;
            }
            
            timeToDisplay.setText(tmpTime);

            LiveTime.sleep1second();
        } while (countOn);
    }
}
