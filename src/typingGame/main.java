package typingGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class main {

  private static ArrayList<String> wordList = new ArrayList<String>();

  private static Integer timeLeft;
  private static Integer incorrect;
  private static Integer score;
  private static Integer highScore;

  private static int top = 100; // HEIGHT
  private static int bottom = top;
  private static int left = 250; // WIDTH
  private static int right = left;

  private JFrame jframe;
  private JPanel jpanel;
  private JLabel words;
  private JLabel inputWord;
  static JLabel timeLabel;
  static Timer timer;
  private JLabel scoreLabel;
  private JTextField textField;
  private String gameListString;
  private boolean correctCheck;
  private Font wordFont = new Font("Dialog", Font.BOLD, 30);


  // SETS UP THE GAME SCREEN, UPDATES GAME SCREEN, UPDATES SCORE
  public main(ArrayList<String> gameList, Timer timer) {
    jframe = new JFrame();
    jpanel = new JPanel();

    // change this
    // gameListString = gameList.toString();
    gameListString = gameListToString(gameList);
    timeLabel = new JLabel("");
    words = new JLabel(gameListString);
    inputWord = new JLabel("");
    scoreLabel = new JLabel("");

    words.setFont(wordFont);
    inputWord.setFont(wordFont);
    timeLabel.setFont(new Font("Dialog", Font.BOLD, 15));
    textField = new JTextField();

    textField.setFont(wordFont);

    jpanel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    jpanel.setLayout(new GridLayout(0, 1));

    jpanel.add(timeLabel);
    jpanel.add(words);
    jpanel.add(textField);
    jpanel.add(inputWord);
    jpanel.add(scoreLabel);


    textField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (Time.seconds <= 0) {
          postGame(jframe);
        }
        String input = textField.getText();

        // checks if user typed the word correctly
        correctCheck = check(input, gameList);
        if (correctCheck) { // if user spells word right
          inputWord.setForeground(Color.GREEN);
          gameList.remove(0);
          gameList.add(randString());

          // Changes the arraylist to a string to remove brackets
          gameListString = gameListToString(gameList);

          words.setText(gameListString); // updates words
          textField.setText("");
          score++;
          // changes amount of extra seconds you get depending on how long the word was
          if (Time.secondsPassed >= 20) {
            Time.seconds += 1;
            inputWord.setText(input + " + 1 sec");

          } else if (input.length() > 10) {
            Time.seconds += 3;
            inputWord.setText(input + " + 3 secs");
          } else if (input.length() >= 6 && input.length() <= 9) {
            Time.seconds += 2;
            inputWord.setText(input + " + 2 secs");
          } else {
            Time.seconds += 1;
            inputWord.setText(input + " + 1 sec");
          }

        } else { // if user spells word wrong
          inputWord.setForeground(Color.RED);
          Time.seconds -= 1;
          inputWord.setText(input + " - 1 sec");
          incorrect++;
        }

        scoreLabel.setText(score.toString());
        timeLabel.setText("Time: " + Time.seconds.toString());

        jpanel.validate();
        jpanel.repaint();
      }
    });

    jframe.add(jpanel, BorderLayout.CENTER);
    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jframe.setTitle("Typing game");
    jframe.pack();
    jframe.setVisible(true);
  }

  public static void main(String[] args) {
    highScore = 0;
    loadWords();
    runGame();
  }

  // WHEN RUNS, CLOSES GAME WINDOW AND OPENS SCORE WINDOW
  public static void postGame(JFrame jframe) {
    jframe.setVisible(false);

    // calculates words per minute
    Float mins = (float) Time.secondsPassed / 60; // calculates mins from seconds
    Float wpm = score / mins;
    wpm = (float) Math.round(wpm); // rounds wpm for neatness

    JFrame endScreen = new JFrame();
    JPanel endPanel = new JPanel();
    JButton restart = new JButton("Restart game");

    endPanel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    endPanel.setLayout(new GridLayout(0, 1));

    if (highScore <= score) {
      highScore = score;
    }

    JLabel scoreLabel = new JLabel("Your score: " + score.toString());
    JLabel highScoreLabel = new JLabel("Your high score: " + highScore.toString());
    JLabel wpmLabel = new JLabel("Words per minute: " + wpm.toString());

    endPanel.add(wpmLabel);
    endPanel.add(scoreLabel);
    endPanel.add(highScoreLabel);
    endPanel.add(restart);

    restart.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        runGame();
        Time.seconds = 10;
        endScreen.setVisible(false);
      }
    });


    endScreen.add(endPanel, BorderLayout.CENTER);
    endScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    endScreen.setTitle("Typing game");
    endScreen.pack();
    endScreen.setVisible(true);

  }

  // PREPARES A LIST OF WORDS TO USE IN THE GAME
  public static void loadWords() {

    try {
      File file = new File("words\\words.txt");
      Scanner reader = new Scanner(file);
      int c = 0;
      while (reader.hasNextLine()) {
        String data = reader.nextLine();
        wordList.add(c, data);
        c++;
      }
      reader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  // RUNS TO START GAME
  public static void runGame() {
    ArrayList<String> gameList = new ArrayList<String>();
    gameList = initialiseGameList();
    incorrect = 0;
    score = 0;
    timeLeft = 10;
    Time timerClass = new Time();
    timer = new Timer();
    timer.schedule(timerClass, 1000, 1000);

    new main(gameList, timer);

  }

  // CHECKS IF USER INPUTS CORRECT VALUE
  public static boolean check(String input, ArrayList<String> gameList) {
    System.out.println(gameList);
    if (input.contentEquals(gameList.get(0))) {
      return true;
    } else {
      return false;
    }
  }

  // INITIALISES GAME VARIBALES
  public static ArrayList<String> initialiseGameList() {
    ArrayList<String> initialiseList = new ArrayList<String>();
    for (int i = 0; i < 6; i++) {
      initialiseList.add(i, randString());
    }
    return initialiseList;
  }

  // GETS A RANDOM STRING FROM THE LIST OF STRINGS
  public static String randString() {
    int min = 0;
    int max = wordList.size();
    int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
    String randomString = wordList.get(randomNum);

    return randomString;
  }

  // CONVERTS THE GAMELIST FROM AN ARRAYLIST TO A STRING FOR DISPLAY
  public static String gameListToString(ArrayList<String> gameList) {
    StringBuilder builder = new StringBuilder();
    for (String word : gameList) {
      builder.append(word);
      builder.append(" ");
    }

    String string = builder.toString();
    return string;
  }


}
