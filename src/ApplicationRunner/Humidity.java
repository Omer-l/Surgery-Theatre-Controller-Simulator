package ApplicationRunner;

import javafx.scene.control.Label;

public class Humidity {

    public static int humidityInt = 40;

    protected static Label clicked(Label humidity, boolean up) {
        if (up == true && humidityInt < 55) {
            humidityInt += 1;
            humidity.setText(humidityInt + "");
        } else if (up == false && humidityInt > 30) {
            humidityInt -= 1;
            humidity.setText(humidityInt + "");
        } else if (humidityInt == 55) {
            humidity.setText("55");
        } else if (humidityInt == 30) {
            humidity.setText("30");
        }
        return humidity;
    }

}
