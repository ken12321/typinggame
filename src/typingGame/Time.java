package typingGame;

import java.util.TimerTask;

public class Time extends TimerTask {

  static Integer seconds = 10; // HOW MANY SECONDS THE PLAYER INITALLY HAS
  static Integer secondsPassed = 0;

  @Override
  public void run() {
    secondsPassed++;
    seconds--;
    System.out.println("time: " + seconds);
    System.out.println("time passed: " + secondsPassed);

    main.timeLabel.setText("Time: " + seconds.toString());

    if (seconds <= 0) {
      main.timeLabel.setText("Time up! Press ENTER to continue to score screen!");
      main.timer.cancel();
    }
  }
}
