package ApplicationRunner;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;

public class LightControl {

    protected int counter = 0;
    protected final static String[] lightControlColors = {
        "#666600", "#979700", "#CCCC00", "#FFFF00", "#FFFF99", "#FFFFCC"};

    public void clicked(Rectangle[] setting, boolean next, int counter) {
        if (next == true && counter < setting.length) {
            this.counter++;
        } else if (next == false && counter > 0) {
            this.counter--;
        }
        for (int i = 0; i < setting.length; i++) {
            if (i < this.counter) {
                setting[i].setFill(Paint.valueOf(lightControlColors[i]));
            } else {
                setting[i].setFill(Paint.valueOf("#333300"));
            }
        }

    }
}
