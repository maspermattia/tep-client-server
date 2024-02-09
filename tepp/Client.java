import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Application {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private TextField nameInput;
    private Label serverResponseLabel;
    private TextField userInput;

    @Override
    public void start(Stage primaryStage) {
        nameInput = new TextField();
        nameInput.setPromptText("Inserisci il tuo nome");

        Button submitButton = new Button("Invia");
        submitButton.setOnAction(e -> sendName());

        userInput = new TextField();
        userInput.setDisable(true);

        Button sendButton = new Button("Invia");
        sendButton.setOnAction(e -> sendUserInput());

        serverResponseLabel = new Label();

        VBox layout = new VBox(10);
        layout.getChildren().addAll(nameInput, submitButton, userInput, sendButton, serverResponseLabel);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sette e Mezzo - Client");
        primaryStage.setOnCloseRequest(e -> {
            try {
                if (socket != null)
                    socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Platform.exit();
        });
        primaryStage.show();

        new Thread(() -> {
            try {
                socket = new Socket(SERVER_ADDRESS, PORT);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String serverResponse;
                while ((serverResponse = in.readLine()) != null) {
                    if (serverResponse.startsWith("Inserisci il tuo nome:")) {
                        serverResponseLabel.setText(serverResponse);
                    } else {
                        serverResponseLabel.setText("Server: " + serverResponse);
                        if (serverResponse.startsWith("Vuoi giocare di nuovo?")) {
                            Platform.runLater(() -> userInput.setDisable(false));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendName() {
        String playerName = nameInput.getText();
        if (!playerName.isEmpty()) {
            out.println(playerName);
        }
    }

    private void sendUserInput() {
        String userInputText = userInput.getText();
        if (!userInputText.isEmpty()) {
            out.println(userInputText.toLowerCase());
            if (userInputText.equalsIgnoreCase("no")) {
                userInput.setDisable(true);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
