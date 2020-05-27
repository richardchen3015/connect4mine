package game;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TwoPlayer extends Gamemode {
    JFrame frame;
    public TwoPlayer(JFrame frame) throws IOException {
        super(frame);
    }
    public TwoPlayer(JFrame frame, File file) throws IOException, FontFormatException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        super(frame, file);
    }

}
