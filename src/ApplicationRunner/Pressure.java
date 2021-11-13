package ApplicationRunner;

import javafx.scene.control.Label;

public class Pressure {

    public static int pressureInt = 70;

    protected static Label clicked(Label pressure, boolean up, Label pressureSymbol) {
        if (up == true && pressureInt < 120) {
            pressureInt += 10;
            pressure.setText(pressureInt + "");
        } else if (up == false && pressureInt > 50) {
            pressureInt -= 10;
            pressure.setText(pressureInt + "");
        } else if (pressureInt == 120) {
            pressure.setText("120");
        } else if (pressureInt == 50) {
            pressure.setText("50");
        }
        if(pressureInt >= 99)
        {
            //set style of 'kPa' again to adjust and not move when figure is 3 digits
            pressureSymbol.setStyle("-fx-text-fill: white;"
                + "-fx-font-size: 30;"
                + "-fx-padding: 10 0 0 8;");
        } else if(pressureInt < 100)
        {
            pressureSymbol.setStyle("-fx-text-fill: white;"
                + "-fx-font-size: 30;"
                + "-fx-padding: 10 0 0 40;");
        }
        return pressure;
    }
}
