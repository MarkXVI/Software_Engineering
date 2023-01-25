package Task2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static javafx.application.Platform.exit;

public class Server extends Application {
    private Label textLabel;
    private Label dateLabel;
    private final Integer PORT = 1234;

    @Override
    public void start(Stage primaryStage)  {
        textLabel = new Label("TEXT");
        dateLabel = new Label("DATE");
        VBox root = new VBox();
        root.getChildren().add(textLabel);
        root.getChildren().add(dateLabel);
        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Server");
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread(() -> {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(PORT);
            }catch (IOException e) {
                System.out.println("Could not listen on port: " + PORT);
                System.exit(-1);
            }
            try{
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Connection accepted to: " + clientSocket.getInetAddress());
                    new Thread(() -> { // multi - connection server
                        try {
                            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                            Object inputObj;
                            while ((inputObj = in.readObject()) != null) {
                                Description finalDescription = (Description) inputObj;
                                Platform.runLater(() -> {
                                    textLabel.setText("Text: " + finalDescription.getText());
                                    dateLabel.setText("Date: " + finalDescription.getAddedDate().toString());
                                });
                            }
                            in.close();
                            clientSocket.close();
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                            System.err.println("Failed in reading, writing");
                            System.exit(-1);
                        }
                    }).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
