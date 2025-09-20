package infra;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Mp3Player {

    private Player player;

    private void setPlayer(String mp3FilePath) throws JavaLayerException, FileNotFoundException {
        BufferedInputStream bis= new BufferedInputStream(new FileInputStream(mp3FilePath));
        this.player = new Player(bis);
    }

    public void startPlayer(String mp3FilePath) throws JavaLayerException, FileNotFoundException {
        stopPlayer(); // 현재 재생중인 음악 중지

        setPlayer(mp3FilePath);

        Thread playerThread = new Thread(() -> {
            try {
                player.play();

            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        });

        playerThread.setDaemon(true); // 백그라운드 재생 설정
        playerThread.start();
    }

    public void stopPlayer() {
        if (player != null) {
            player.close();
        }
    }
}
