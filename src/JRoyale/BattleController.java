import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * this class is the controller for the battle controller
 * @version 1.0
 */
public class BattleController 
{
    /**
     * the timer for handling game loop
     */
    private Timer timer;

    /**
     * the main model object that use singleton
     */
    private GameManager gameManager = GameManager.getInstance();

    /**
     * the arena view element in view 
     */
    private ArenaView arenaView = new ArenaView();

    /**
     * the chosen card of the player
     */
    private Card chosenCard;

    /**
     * the number of chosen card
     */
    private int chosenCardNumber;

    /**
     * the battle intro sound that plays first of battle
     */
    private MediaPlayer mediaPlayerGo = new MediaPlayer(new Media(new File("resources/battle/go.mp3").toURI().toString()));

    /**
     * the background music that plays in battle
     */
    private MediaPlayer battleBackgroundMusic = new MediaPlayer(new Media(new File("resources/battle/battleBackgroundMusic.mp3").toURI().toString()));
    
    /**
     * this sound plays when battle finishes
     */
    private MediaPlayer battleFinished = new MediaPlayer(new Media(new File("resources/battle/battleFinished.mp3").toURI().toString()));
    
    /**
     * is the battle finished
     */
    private boolean isBattleFinished;
    
    @FXML
    private AnchorPane arenaPane;

    @FXML
    private Line leftUpLine;

    @FXML
    private Line upMiddleLine;

    @FXML
    private Line downMiddleLine;

    @FXML
    private Line middleLine;

    @FXML
    private Line downRightLine;

    @FXML
    private Line upRightLine;

    @FXML
    private Line rightLine;

    @FXML
    private Line leftDownLine;

    @FXML
    private Line upLeftLine;

    @FXML
    private Line downLeftLine;

    @FXML
    private ImageView nextCardImageView;
    
    @FXML
    private ImageView card1ImageView;
    
    @FXML
    private ImageView card2ImageView;
    
    @FXML
    private ImageView card3ImageView;

    @FXML
    private ImageView card4ImageView;
    
    @FXML
    private Label battleTimerLabel;
    
    @FXML
    private ProgressBar elixirBarProgressBar;
    
    @FXML
    private Label elixirBarLabel;
    
    @FXML
    private ImageView playerScoreImageView;
    
    @FXML
    private ImageView botScoreImageView;
    
    @FXML
    private Label playerUsernameLabel;
    
    @FXML
    private Label botUsernameLabel;
    
    @FXML
    private Rectangle card1Border;
    
    @FXML
    private Rectangle card2Border;
    
    @FXML
    private Rectangle card3Border;
    
    @FXML
    private Rectangle card4Border;
    
    @FXML
    void card1Pressed(MouseEvent event) 
    {
        if(gameManager.getBattle().getPlayerCardsQueue().get(4).getCost() <= gameManager.getBattle().getPlayerElixirBar().getElixir())
        {
            chosenCard = gameManager.getBattle().getPlayerCardsQueue().get(4);
            chosenCardNumber = 1;
        }
    }
    
    @FXML
    void card2Pressed(MouseEvent event) 
    {
        if(gameManager.getBattle().getPlayerCardsQueue().get(5).getCost() <= gameManager.getBattle().getPlayerElixirBar().getElixir())
        {
            chosenCard = gameManager.getBattle().getPlayerCardsQueue().get(5);
            chosenCardNumber = 2;
        }
    }
    
    @FXML
    void card3Pressed(MouseEvent event) 
    {
        if(gameManager.getBattle().getPlayerCardsQueue().get(6).getCost() <= gameManager.getBattle().getPlayerElixirBar().getElixir())
        {
            chosenCard = gameManager.getBattle().getPlayerCardsQueue().get(6);
            chosenCardNumber = 3;
        }
    }

    @FXML
    void card4Pressed(MouseEvent event) 
    {
        if(gameManager.getBattle().getPlayerCardsQueue().get(7).getCost() <= gameManager.getBattle().getPlayerElixirBar().getElixir())
        {
            chosenCard = gameManager.getBattle().getPlayerCardsQueue().get(7);
            chosenCardNumber = 4;
        }
    }
    
    @FXML
    void arenaPanePressed(MouseEvent event) 
    {
        double x = event.getX();
        double y = event.getY();
        if(chosenCard != null)
        {
            if(chosenCard instanceof Spell)
            {
                ArrayList<Creature> creatures = chosenCard.makeCreature(new Point2D(x, y), 1);
                Creature c = creatures.get(0);

                gameManager.getBattle().getArena().getCreatures().add(c);
                gameManager.getBattle().getPlayerCardsQueue().remove(chosenCardNumber + 3);
                gameManager.getBattle().getPlayerCardsQueue().add(0, chosenCard);
                gameManager.getBattle().getPlayerElixirBar().takeExir(chosenCard.getCost());
                chosenCard = null;
                chosenCardNumber = 0;
            }
            else
            {
                if(isAppropriate(new Point2D(x, y)) && checkTowerStatus(x, y))
                {
                    ArrayList<Creature> creatures = chosenCard.makeCreature(new Point2D(x, y), 1);
                    int count = 0;
                    while (count < creatures.size())
                    {
                        ArrayList<Point2D> positions = findPositions(x, y, creatures.size());
                        for (int i = 0; i < creatures.size(); i++) 
                        {
                            if (checkTowerStatus(positions.get(i).getX(), positions.get(i).getY()) && count < creatures.size()) 
                            {
                                Creature c = creatures.get(count);
                                c.setPosition(positions.get(i));
                                gameManager.getBattle().getArena().getCreatures().add(c);
                                count++;
                            }
                        }
                    }
                    gameManager.getBattle().getPlayerCardsQueue().remove(chosenCardNumber + 3);
                    gameManager.getBattle().getPlayerCardsQueue().add(0, chosenCard);
                    gameManager.getBattle().getPlayerElixirBar().takeExir(chosenCard.getCost());
                    chosenCard = null;
                    chosenCardNumber = 0;
                }
                else if(!checkTowerStatus(x, y))
                    showBorders(x, y);
            }
        }
    }

    /**
     * shows the borders if the player has chosen the wrong place
     * @param x of the chosen point
     * @param y of the chosen point
     */
    private void showBorders(double x, double y) 
    {
        if(!gameManager.getBattle().getArena().getBotDownPrincess().isEliminated() && !gameManager.getBattle().getArena().getBotUpPrincess().isEliminated())
        {
            setVisibility(rightLine, downRightLine, downLeftLine, upLeftLine, upRightLine, leftDownLine, leftUpLine);
        }
        else if(!gameManager.getBattle().getArena().getBotDownPrincess().isEliminated() && gameManager.getBattle().getArena().getBotUpPrincess().isEliminated())
        {
            setVisibility(upRightLine, upMiddleLine, middleLine, leftDownLine, downRightLine, downLeftLine, rightLine);
        }
        else if(gameManager.getBattle().getArena().getBotDownPrincess().isEliminated() && gameManager.getBattle().getArena().getBotUpPrincess().isEliminated())
        {
            setVisibility(rightLine, upRightLine, downRightLine, downMiddleLine, upMiddleLine);
        }
        else if(gameManager.getBattle().getArena().getBotDownPrincess().isEliminated() && !gameManager.getBattle().getArena().getBotUpPrincess().isEliminated())
        {
            setVisibility(rightLine, upRightLine, upLeftLine, leftUpLine, middleLine, downMiddleLine,  downRightLine);
        }
    }

    /**
     * makes the given lines visible for 3 seconds
     * @param lines to make visible
     */
    private void setVisibility(Line... lines) 
    {
        Runnable task = new Runnable()
        {
            @Override
            public void run()
            {
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for(Line l : lines)
                            l.setVisible(true);
                    }
                });

                try
                {
                    Thread.sleep(3000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for(Line l : lines)
                            l.setVisible(false);
                    }
                });
            }
        };
        Thread showThread = new Thread(task);
        showThread.start();
    }

    /**
     * checks the borders status
     * @param x of the chosen point
     * @param y of the chosen point
     * @return true if the the chosen position is acceptable otherwise returns false
     */
    private boolean checkTowerStatus(double x, double y) 
    {
        if(gameManager.getBattle().getArena().getBotDownPrincess().isEliminated() && gameManager.getBattle().getArena().getBotUpPrincess().isEliminated())
        {
            return x <= 840;
        }
        else if(!gameManager.getBattle().getArena().getBotDownPrincess().isEliminated() && !gameManager.getBattle().getArena().getBotUpPrincess().isEliminated()) 
        {
            return x <= 600;
        }
        else if(gameManager.getBattle().getArena().getBotDownPrincess().isEliminated() && !gameManager.getBattle().getArena().getBotUpPrincess().isEliminated())
        {
            return x <= 600 || (x <= 840 && y >= 360);
        }
        else 
        {
            return x <= 600 || (x <= 840 && y <= 360);
        }
    }
    
    /**
     * finds appropriate points for the given center coordinates
     * @param x of the chosen point
     * @param y of the chosen point
     * @param number of the wanted points
     * @return the ArrayList of the acceptable points
     */
    private ArrayList<Point2D> findPositions(double x, double y, int number) 
    {
        ArrayList<Point2D> points = new ArrayList<>();
        double newX = x;
        double newY = y;

        if(number == 1 && isAppropriate(new Point2D(x, y))) 
        {
            points.add(new Point2D(x, y));
            return points;
        }
        int r = 40;
        while (r <= 60)
        {
            if(isAppropriate(new Point2D(x - r, y - r)))
                points.add(new Point2D(x - r, y - r));
            if(isAppropriate(new Point2D(x + r, y + r)))
                points.add(new Point2D(x + r, y + r));
            if(isAppropriate(new Point2D(x + r, y - r)))
                points.add(new Point2D(x + r, y - r));
            if(isAppropriate(new Point2D(x - r, y + r)))
                points.add(new Point2D(x - r, y + r));

            r += 7;
        }
        Random rand = new Random();
        while (points.size() < number)
        {
            newY = rand.nextInt(80) + y;
            newX = rand.nextInt(80) + x;
            if(isAppropriate(new Point2D(newX, newY)))
            {
                points.add(new Point2D(newX, newY));
            }
        }
        return points;
    }
    
    /**
     * checks whether the point is in the map coordinates or not
     * @param point to check
     * @return true if is in map otherwise false
     */
    public boolean isAppropriate(Point2D point)
    {
        int borderDistance = chosenCard instanceof Building ? 40 : 10;
        double x = point.getX();
        double y = point.getY();

            if(y < (720 - borderDistance) && x < (1280 - borderDistance) && x > borderDistance && y > borderDistance)
            {
                if(notInCreatures(point))
                {
                    if (chosenCard instanceof Building && (x < 720 && x > 560))
                        return false;
                    if (x < 680 && x > 600) 
                    {
                        if ((y >= 100 && y <= 180) || (y >= 540 && y <= 620))
                            return true;
                        else if(chosenCard instanceof Dragon)
                            return true;
                    }
                    else
                        return true;
                }
                else
                {
                    return true;
                }
            }
        return false;
    }
    
    /**
     * checks whether the point is not in other creatures
     * @param point to check
     * @return true if the point is not too close to other creatures
     */
    private boolean notInCreatures(Point2D point) 
    {
        Iterator<Creature> it = gameManager.getBattle().getArena().getCreatures().iterator();

        while (it.hasNext())
        {
            Creature c = it.next();

            if(!(chosenCard instanceof Spell) && !(chosenCard instanceof Giant) && !(c.getCard() instanceof Giant))
            {
                if (c.getPosition().distance(point) < 10 && ((!(chosenCard instanceof Dragon) && !(c.getCard() instanceof Dragon)) || ((chosenCard instanceof Dragon) && (c.getCard() instanceof Dragon)))) 
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * starts main process of a battle in the game
     */
    private void startTimer() 
    {
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() 
        {
            @Override
            public void run() 
            {
                Platform.runLater(new Runnable() 
                {
                    @Override
                    public void run() 
                    {
                        int status = gameManager.battleStep();
                        updateView();
                        
                        if(status != 0)
                        {
                            finishBattle(status);
                        }
                    }
                });
            }
        };
        this.timer.schedule(timerTask, 0, 1000 / GameManager.FPS);
    }
    
    /**
     * updates all battle view based on new situation of model
     */
    private void updateView()
    {
        updateScoreBoardView();
        updateElixirBarView();
        updateCardsQueueView();
        updateBattleTimerView();
        arenaView.updateView(gameManager.getBattle().getArena(), this);

        Iterator<Creature> it = GameManager.getInstance().getBattle().getArena().getCreatures().iterator();

        while (it.hasNext())
        {
            Creature temp = it.next();

            if(temp.getCard() instanceof Fireball || temp.getCard() instanceof Arrows)
            {
                it.remove();
            }
            else if(temp.getCard() instanceof Rage && ((Rage) temp.getCard()).getInBattleTime() <= 0)
            {
                it.remove();
            }
        }
    }
    
    /**
     * updates elixir bar in view based on elixir bar model
     */
    private void updateElixirBarView()
    {
        double elixir = gameManager.getBattle().getPlayerElixirBar().getElixir();
        elixirBarProgressBar.setProgress(elixir / 10);
        elixirBarLabel.setText(Integer.toString((int)Math.floor(elixir)));
    }
    
    /**
     * shows available cards and next card in view based on model 
     */
    private void updateCardsQueueView()
    {
        ArrayList<Card> cards = gameManager.getBattle().getPlayerCardsQueue();
        nextCardImageView.setImage(cards.get(3).getImage(0));
        
        if(gameManager.getBattle().getPlayerElixirBar().getElixir() >= cards.get(4).getCost())
        {
            card1ImageView.setImage(cards.get(4).getImage(0));
        }
        else
        {
            card1ImageView.setImage(cards.get(4).getImage(-1));
        }
        
        if(gameManager.getBattle().getPlayerElixirBar().getElixir() >= cards.get(5).getCost())
        {
            card2ImageView.setImage(cards.get(5).getImage(0));
        }
        else
        {
            card2ImageView.setImage(cards.get(5).getImage(-1));
        }
        
        if(gameManager.getBattle().getPlayerElixirBar().getElixir() >= cards.get(6).getCost())
        {
            card3ImageView.setImage(cards.get(6).getImage(0));
        }
        else
        {
            card3ImageView.setImage(cards.get(6).getImage(-1));
        }
        
        if(gameManager.getBattle().getPlayerElixirBar().getElixir() >= cards.get(7).getCost())
        {
            card4ImageView.setImage(cards.get(7).getImage(0));
        }
        else
        {
            card4ImageView.setImage(cards.get(7).getImage(-1));
        }
        
        if(chosenCardNumber == 1)
        {
            card1Border.setVisible(true);
            card2Border.setVisible(false);
            card3Border.setVisible(false);
            card4Border.setVisible(false);
        }
        else if(chosenCardNumber == 2)
        {
            card1Border.setVisible(false);
            card2Border.setVisible(true);
            card3Border.setVisible(false);
            card4Border.setVisible(false);
        }
        else if(chosenCardNumber == 3)
        {
            card1Border.setVisible(false);
            card2Border.setVisible(false);
            card3Border.setVisible(true);
            card4Border.setVisible(false);
        }
        else if(chosenCardNumber == 4)
        {
            card1Border.setVisible(false);
            card2Border.setVisible(false);
            card3Border.setVisible(false);
            card4Border.setVisible(true);
        }
        else
        {
            card1Border.setVisible(false);
            card2Border.setVisible(false);
            card3Border.setVisible(false);
            card4Border.setVisible(false);
        }
    }
    
    /**
     * updates battle timer in view based on battle timer model
     */
    private void updateBattleTimerView()
    {
        int time = gameManager.getBattle().getBattleTimer().getTime();
        if(time == 60)
        {
            battleTimerLabel.setStyle("-fx-text-fill: red");
        }
        int minutes = time / 60;
        int seconds = time % 60;
        if(seconds < 10)
        {
            battleTimerLabel.setText(minutes + ":0" + seconds);
        }
        else
        {
            battleTimerLabel.setText(minutes + ":" + seconds); 
        }
    }
    
    /**
     * updates view of score board based on model
     */
    private void updateScoreBoardView()
    {
        if(gameManager.getBattle().getScoreBoard().getPlayerStars() == 0)
        {   
            playerScoreImageView.setImage(new Image("resources/battle/0star.png"));
        }
        else if(gameManager.getBattle().getScoreBoard().getPlayerStars() == 1)
        {
            playerScoreImageView.setImage(new Image("resources/battle/1star.png"));
        }
        else if(gameManager.getBattle().getScoreBoard().getPlayerStars() == 2)
        {
            playerScoreImageView.setImage(new Image("resources/battle/2star.png"));
        }
        else if(gameManager.getBattle().getScoreBoard().getPlayerStars() == 3)
        {
            playerScoreImageView.setImage(new Image("resources/battle/3star.png"));
        }
        
        if(gameManager.getBattle().getScoreBoard().getBotStars() == 0)
        {   
            botScoreImageView.setImage(new Image("resources/battle/0star.png"));
        }
        else if(gameManager.getBattle().getScoreBoard().getBotStars() == 1)
        {
            botScoreImageView.setImage(new Image("resources/battle/1star.png"));
        }
        else if(gameManager.getBattle().getScoreBoard().getBotStars() == 2)
        {
            botScoreImageView.setImage(new Image("resources/battle/2star.png"));
        }
        else if(gameManager.getBattle().getScoreBoard().getBotStars() == 3)
        {
            botScoreImageView.setImage(new Image("resources/battle/3star.png"));
        }
    }
    
    /**
     * check the status from model to decide finish the game
     * @param status
     */
    private void finishBattle(int status)
    {
        timer.cancel();
        timer.purge();
        if(!isBattleFinished)
        {
            Player player = GameManager.getInstance().getCurrentPlayer();
            Bot bot = GameManager.getInstance().getCurrentBot();
            ScoreBoard scoreBoard = GameManager.getInstance().getBattle().getScoreBoard();
            String s = "";
            if(bot.getDifficulty() == 1)
            {
                s = "easy";
            }
            else if(bot.getDifficulty() == 2)
            {
                s = "normal";
            }
            else
            {
                s = "hard";
            }
            player.addBattleResult(new BattleResult(player.getUsername(), s, scoreBoard.getPlayerStars(), scoreBoard.getBotStars()));
            if(status == 1)
            {
                player.setXp(player.getXp() + 200);
            }
            else
            {
                player.setXp(player.getXp() + 70);
            }
            gameManager.finishBattle();
            battleBackgroundMusic.pause();
            battleBackgroundMusic.seek(Duration.ZERO);
            ImageView yourStars = new ImageView();
            ImageView botStars = new ImageView();
            Label yourName = new Label();
            Label botName = new Label();
            Label winnerName = new Label();
            yourStars.setLayoutX(40);
            yourStars.setLayoutY(40);
            yourStars.setFitWidth(600);
            yourStars.setFitHeight(300);
            yourStars.setImage(new Image("resources/battle/" + scoreBoard.getPlayerStars() + "star.png"));
            yourStars.setPreserveRatio(true);
            yourStars.setPickOnBounds(true);
            yourStars.setVisible(true);
            arenaPane.getChildren().add(yourStars);
            botStars.setLayoutX(640);
            botStars.setLayoutY(40);
            botStars.setFitWidth(600);
            botStars.setFitHeight(300);
            botStars.setImage(new Image("resources/battle/" + scoreBoard.getBotStars() + "star.png"));
            botStars.setPreserveRatio(true);
            botStars.setPickOnBounds(true);
            botStars.setVisible(true);
            arenaPane.getChildren().add(botStars);
            yourName.setText(player.getUsername());
            yourName.setLayoutX(40);
            yourName.setLayoutY(340);
            yourName.setPrefWidth(600);
            yourName.setPrefHeight(300);
            yourName.setPickOnBounds(true);
            yourName.setAlignment(Pos.TOP_CENTER);
            yourName.setFont(new Font("Arial Rounded MT Bold", 100));
            yourName.setTextFill(Color.BLUE);
            yourName.setVisible(true);
            arenaPane.getChildren().add(yourName);
            botName.setText(bot.getUsername());
            botName.setLayoutX(640);
            botName.setLayoutY(340);
            botName.setPrefWidth(600);
            botName.setPrefHeight(300);
            botName.setPickOnBounds(true);
            botName.setAlignment(Pos.TOP_CENTER);
            botName.setFont(new Font("Arial Rounded MT Bold", 100));
            botName.setTextFill(Color.RED);
            botName.setVisible(true);
            arenaPane.getChildren().add(botName);
            if(status == 1)
            {
                winnerName.setText(player.getUsername() + " WINS");
            }
            else
            {
                winnerName.setText(bot.getUsername() + " WINS");
            }
            winnerName.setLayoutX(40);
            winnerName.setLayoutY(500);
            winnerName.setPrefWidth(1200);
            winnerName.setPrefHeight(120);
            winnerName.setPickOnBounds(true);
            winnerName.setAlignment(Pos.TOP_CENTER);
            winnerName.setFont(new Font("Arial Rounded MT Bold", 120));
            winnerName.setTextFill(Color.web("#BF00FF"));
            winnerName.setVisible(true);
            arenaPane.getChildren().add(winnerName);
            
            Thread thread = new Thread(new Runnable()
            {
                @Override
                public void run() 
                {
                    try 
                    {
                        Thread.sleep(1000);   
                    } 
                    catch (Exception e) 
                    {
                    }
                    battleFinished.setVolume(0.5);
                    battleFinished.play();
                    try 
                    {
                        Thread.sleep(10000);    
                    } 
                    catch (Exception e) 
                    {
                    }
                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run() 
                        {
                            openMenu();                     
                        }
                        
                    });
                } 
            });
            thread.start();
        }
        isBattleFinished = true;
    }
    
    /**
     * opens menu scene and stops battle scene
     */
    private void openMenu()
    {
        battleBackgroundMusic.stop();

        Stage stage = (Stage)arenaPane.getScene().getWindow();
        Parent root;
        try 
        {
            root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
            Scene scene = new Scene(root);
            scene.setCursor(new ImageCursor(new Image("resources/cursor2.png")));
            stage.setScene(scene);
            stage.setX(-10);
            stage.setY(0);
        } 
        catch (IOException e) 
        {
            System.out.println("cant read fxml");
            e.printStackTrace();
        }
    }

    /**
     * @return the arena pane of the battle
     */
    public AnchorPane getArenaPane() 
    {
        return arenaPane;
    }
    
    @FXML
    public void initialize()
    {
        playerUsernameLabel.setText(gameManager.getCurrentPlayer().getUsername() + " : ");
        botUsernameLabel.setText(" : " + gameManager.getBattle().getBot().getUsername());
        mediaPlayerGo.setVolume(0.2);//volume percentage 0 to 1
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run() 
            {
                mediaPlayerGo.play();
                try 
                {
                    Thread.sleep(3000);
                } 
                catch (InterruptedException e) 
                {
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run() 
                    {
                        startTimer();
                    }
                });
                battleBackgroundMusic.setVolume(0.2);//volume percentage 0 to 1
                battleBackgroundMusic.play();
            }
        });
        thread.start();
    }
}