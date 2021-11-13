package ApplicationRunner;

import javafx.scene.text.Text;

public class LiveTime extends Thread 
{

    Text liveTime;

    public LiveTime(Text displayedTime) 
    {
        liveTime = displayedTime;
    }

    @Override
    public void run() 
    {
        boolean liveTimeOn = true;
        String currentLiveTime = "";
        do {
            long totalMilliseconds = System.currentTimeMillis();

            long totalSeconds = totalMilliseconds / 1000;
            long currentSecond = totalSeconds % 60;

            long totalMinutes = totalSeconds / 60;
            long currentMinute = totalMinutes % 60;

            long totalHours = totalMinutes / 60;
            long currentHour = totalHours % 24 + 1;

            //to ensure the digits are displayed at 01 if under 10
            if (currentHour >= 10) 
            {
                currentLiveTime = currentHour + ":";

            } else 
            {
                currentLiveTime = "0" + currentHour + ":";
            }
            if (currentMinute >= 10) 
            {
                currentLiveTime += currentMinute + ":";
            } else 
            {
                currentLiveTime += "0" + currentMinute + ":";
            }
            if (currentSecond >= 10) 
            {
                currentLiveTime += currentSecond;
            } else 
            {
                currentLiveTime += "0" + currentSecond;
            }
            this.liveTime.setText(currentLiveTime);

            sleep1second();
        } while (liveTimeOn);
    }

    public static void sleep1second() 
    {
        try 
        {
            Thread.sleep(1000);
        } catch (InterruptedException ex) 
        {
            System.out.print("\nINTERRUPTED EXCEPTION.");
        }
    }
}
