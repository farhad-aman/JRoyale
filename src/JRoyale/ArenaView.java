import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ArenaView extends Group
{
    private GameManager gameManager = GameManager.getInstance();
    
    private ArrayList<Creature> arenaViewCreatures;

    private HashMap<Creature, Integer> oldStatus;

    private ArrayList<ImageView> arenaViewImageViews;

    private ArrayList<ProgressBar> arenaViewHPBars;

    public ArenaView()
    {
        arenaViewCreatures = new ArrayList<>();
        oldStatus = new HashMap<>();
        arenaViewImageViews = new ArrayList<>();
        arenaViewHPBars = new ArrayList<>();
    }

    public void updateView(Arena arena, BattleController bt)
    {
        for (Creature c : gameManager.getBattle().getArena().getCreatures()) 
        {
            if(arenaViewCreatures.contains(c))
            {
                ImageView iv = arenaViewImageViews.get(arenaViewCreatures.indexOf(c));
                ProgressBar pb = arenaViewHPBars.get(arenaViewCreatures.indexOf(c));
                if(c.isEliminated())
                {
                    deathTime(c, iv, pb);
                }
                iv.setX(c.getPosition().getX() - (c.getCard().getImageSize() / 2));
                iv.setY(c.getPosition().getY() - (c.getCard().getImageSize() / 2));
                pb.setLayoutX(c.getPosition().getX() - (c.getCard().getImageSize() / 2));
                pb.setLayoutY(c.getPosition().getY() - (c.getCard().getImageSize() / 2) - 15);
                if(c.getCard() instanceof Building)
                {
                    pb.setProgress((c.getHP()) / ((Building)c.getCard()).getInitHP(c.getLevel()));
                }
                else if(c.getCard() instanceof Troop)
                {
                    pb.setProgress((c.getHP()) / ((Troop)c.getCard()).getInitHP(c.getLevel()));
                }
                if(!oldStatus.get(c).equals(c.getStatus()))
                {
                    iv.setImage(c.getCard().getImage(c.getStatus()));
                    oldStatus.put(c, c.getStatus());
                }
            }
            else
            {
                if(c.getCard() instanceof Spell)
                {
                    System.out.println("spell used");
                    showSpell(c, bt);
                }
                else
                {
                    oldStatus.put(c, c.getStatus());
                    ImageView iv = new ImageView(c.getCard().getImage(c.getStatus()));
                    iv.setX(c.getPosition().getX() - (c.getCard().getImageSize() / 2));
                    iv.setY(c.getPosition().getY() - (c.getCard().getImageSize() / 2));
                    iv.setFitHeight(c.getCard().getImageSize());
                    iv.setFitWidth(c.getCard().getImageSize());
                    iv.setPreserveRatio(true);
                    iv.setPickOnBounds(true);
                    iv.setVisible(true);
                    bt.getArenaPane().getChildren().add(iv);
                    arenaViewImageViews.add(iv);
                    ProgressBar pb = new ProgressBar(1);
                    pb.setLayoutX(c.getPosition().getX() - (c.getCard().getImageSize() / 2));
                    pb.setLayoutY(c.getPosition().getY() - (c.getCard().getImageSize() / 2) - 15);
                    pb.setPrefHeight(10);
                    pb.setPrefWidth(c.getCard().getImageSize());
                    if(c.getSide() == 1)
                    {
                        pb.setStyle("-fx-accent: blue");
                    }
                    else
                    {
                        pb.setStyle("-fx-accent: red");
                    }
                    pb.setPickOnBounds(true);
                    if(!(c.getCard() instanceof Spell))
                    {
                        pb.setVisible(true);
                    }
                    bt.getArenaPane().getChildren().add(pb);
                    arenaViewHPBars.add(pb);
                    arenaViewCreatures.add(c);
                }
            }
        }
    }

    private void showSpell(Creature creature, BattleController bt)
    {
        if(creature.getCard() instanceof Rage)
        {
            int duration = ((Rage)creature.getCard()).getDuration(creature.getLevel());
            Circle circle = new Circle(creature.getPosition().getX(), creature.getPosition().getY(), creature.getCard().getRange() * 40, Color.PURPLE);
            circle.setOpacity(0.4);
            circle.setVisible(true);
            bt.getArenaPane().getChildren().add(circle);
            Thread thread = new Thread(new Runnable()
            {
                @Override
                public void run() 
                {
                    try 
                    {
                        Thread.sleep(duration);
                    } 
                    catch (Exception e) 
                    {
                        //TODO: handle exception
                    }
                    circle.setVisible(false);
                }
            });
            thread.start();
        }
        else if(creature.getCard() instanceof Fireball)
        {   
            Circle circle = new Circle(creature.getPosition().getX(), creature.getPosition().getY(), creature.getCard().getRange() * 40, Color.RED);
            circle.setOpacity(0.4);
            circle.setVisible(true);
            bt.getArenaPane().getChildren().add(circle);
            Thread thread = new Thread(new Runnable()
            {
                @Override
                public void run() 
                {
                    try 
                    {
                        Thread.sleep(5000);
                    } 
                    catch (Exception e) 
                    {
                        //TODO: handle exception
                    }
                    circle.setVisible(false);
                }
            });
            thread.start();
        }
        else
        {
            Circle circle = new Circle(creature.getPosition().getX(), creature.getPosition().getY(), creature.getCard().getRange() * 40, Color.BLUE);
            circle.setOpacity(0.4);
            circle.setVisible(true);
            bt.getArenaPane().getChildren().add(circle);
            Thread thread = new Thread(new Runnable()
            {
                @Override
                public void run() 
                {
                    try 
                    {
                        Thread.sleep(5000);
                    } 
                    catch (Exception e) 
                    {
                        //TODO: handle exception
                    }
                    circle.setVisible(false);
                }
            });
            thread.start();
        }
    }

    public void deathTime(Creature creature, ImageView imageView, ProgressBar progressBar)
    {
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run() 
            {
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run() 
                    {
                        creature.getCard().playDeathSound();
                        if(creature.getStatus() % 2 == 1)
                        {
                            imageView.setImage(creature.getCard().getImage(5));
                        }
                        else
                        {
                            imageView.setImage(creature.getCard().getImage(6));
                        }
                    }
                });                
                try 
                {
                    Thread.sleep(1000);    
                } 
                catch (Exception e) 
                {
                    
                }
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run() 
                    {
                        imageView.setVisible(false);
                        progressBar.setVisible(false);
                    }
                });       
            }
        });
        thread.start();
    }
}
