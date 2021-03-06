import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

/**
 * this class contains the controller for the main view
 * @version 1.0
 */
public class LoginController 
{
    /**
     * the sound that plays when login started
     */
    private MediaPlayer loginStarted = new MediaPlayer(new Media(new File("resources/login/loginStarted.mp3").toURI().toString()));

    @FXML
    private TextField loginUsernameTextField;

    @FXML
    private PasswordField loginPasswordTextField;

    @FXML
    private ImageView loginButton;

    @FXML
    private Label loginUsernameLabel;

    @FXML
    private Label loginPasswordLabel;

    @FXML
    private TextField signUpUsernameTextField;

    @FXML
    private PasswordField signUpPasswordTextField;

    @FXML
    private PasswordField signUpConfirmTextField;

    @FXML
    private ImageView signUpButton;

    @FXML
    private Label signUpUsernameLabel;

    @FXML
    private Label signUpPasswordLabel;

    @FXML
    private Label signUpConfirmLabel;

    @FXML
    void loginButtonPressed(MouseEvent event) 
    {
       try
       {
           Platform.runLater(new Runnable()
           {
               @Override
               public void run()
               {
                   logIn();
                   System.out.println("login ok");
                   MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("resources/login/click1.mp3").toURI().toString()));
                   mediaPlayer.setVolume(0.5);//volume percentage 0 to 1
                   mediaPlayer.play();
                   loginButton.setImage(new Image("resources/login/loginButtonPressed.png"));
               }
           });
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }
    }

    @FXML
    void loginButtonReleased(MouseEvent event) 
    {
        try
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    loginButton.setImage(new Image("resources/login/loginButton.png"));
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * asks GameManager.getInstance().login() for login and based on the given status interacts with the user//-1->username does not exist//0->password does not match//1->information is accurate
     * */
    private void logIn()
    {
        int status = GameManager.getInstance().login(loginUsernameTextField.getText(), loginPasswordTextField.getText());

        System.out.println(status);

        if(status == -1)
            showMessage("Wrong Username, Try Again", loginUsernameLabel);
        else if (status == 0)
            showMessage("Wrong Password, Try Again", loginPasswordLabel);
        else
        {
            openMenu();
        }
    }

    @FXML
    void signUpButtonPressed(MouseEvent event) 
    {
        try
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    signUp();
                    System.out.println("sign up ok");
                    MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("resources/login/click1.mp3").toURI().toString()));
                    mediaPlayer.setVolume(0.5);//volume percentage 0 to 1
                    mediaPlayer.play();
                    signUpButton.setImage(new Image("resources/login/signUpButtonPressed.png"));
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void signUpButtonReleased(MouseEvent event) 
    {
        try
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run() 
                {
                    signUpButton.setImage(new Image("resources/login/signUpButton.png"));
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * asks GameManager.getInstance().signUp() for sign up and based on the given status interacts with the user//status->-2 : passwords are not the same//-1->information is not accurate//0->username is occupied//1-> player is added
     * */
    private void signUp() 
    {
        int status = GameManager.getInstance().signUp(signUpUsernameTextField.getText(), signUpPasswordTextField.getText(), signUpConfirmTextField.getText());

        System.out.println("status: " + status);

        if(status == -2)
            showMessage("Wrong confirm password, try again", signUpConfirmLabel);
        else if(status == -1)
            showMessage("Inappropriate information, try again", signUpUsernameLabel, signUpPasswordLabel);
        else if(status == 0)
            showMessage("Username already exists, try another one", signUpUsernameLabel);
        else
        {
            try 
            {
                Thread.sleep(3000);    
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
            openMenu();
        }
    }

    @FXML
    void textFieldPressed(MouseEvent event) 
    {
        try
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("resources\\login\\click1.mp3").toURI().toString()));
                    mediaPlayer.setVolume(0.5);//volume percentage 0 to 1
                    mediaPlayer.play();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * open menu fxml file as new scene of app
     */
    private void openMenu() 
    {
        Stage stage = (Stage)loginButton.getScene().getWindow();
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
     * shows a message in a list of labels
     * @param message
     * @param labels
     */
    public void showMessage(String message, Label... labels)
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
                        for(Label label : labels)
                            label.setText(message);
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
                        for(Label label : labels)
                            label.setText("");
                    }
                });
            }
        };

        Thread showThread = new Thread(task);
        showThread.start();
    }

    @FXML
    void initialize()
    {
        loginStarted.setVolume(0.2);//volume percentage 0 to 1
        loginStarted.play();
    }

}