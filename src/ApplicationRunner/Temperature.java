package ApplicationRunner;

import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class Temperature {

    public static double degreesToInt = 200;

    protected static Label clicked(Label degrees, boolean up) {
        if (up == true && degreesToInt < 275) {
            degreesToInt += 1;
            String tmpDeg = degreesToInt / 10 + "";
            tmpDeg = tmpDeg.substring(0, 4);
            degrees.setText(tmpDeg);
        } else if (up == false && degreesToInt > 100) {
            degreesToInt -= 1;
            String tmpDeg = (double) degreesToInt / 10 + "";
            tmpDeg = tmpDeg.substring(0, 4);
            degrees.setText(tmpDeg);
        } else if (degreesToInt == 10) {
            degrees.setText("10.0");
        } else if (degreesToInt == 27.5) {
            degrees.setText("27.5");
        }
        return degrees;
    }

}
