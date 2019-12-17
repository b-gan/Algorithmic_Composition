package finalproject;

import com.softsynth.jmsl.*;

public class TrackMusicShape {

    MusicShape s;
    PlayLurker indexListener;

    public void buildMusicShape(MusicShape a) {
        s = new MusicShape(4);
        s.useStandardDimensionNameSpace();
        s = a;
    }

    public void buildIndexListener() {
        indexListener = new PlayLurker() {
            public void notifyPlayLurker(double playTime, MusicJob musicShape, int index) {
                MusicShape ms = (MusicShape) musicShape;
                System.out.println("Index " + index + " being played in musicShape " + musicShape.getName() + " at playTime="
                        + playTime + ", pitch=" + ms.get(index, 1));
            }
        };
        s.addPlayLurker(indexListener);
    }

    public void play() {
        s.launch(JMSL.now());
    }

//    public static void main(String[] args) {
//        TrackMusicShape tracker = new TrackMusicShape();
//        tracker.buildMusicShape();
//        tracker.buildIndexListener();
//        tracker.play();
//    }

}