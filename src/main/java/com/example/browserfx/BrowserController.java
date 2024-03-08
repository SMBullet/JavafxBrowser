package com.example.browserfx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class BrowserController {

    @FXML
    private Button requestButton;

    @FXML
    private TextArea responseArea;

    @FXML
    private void initialize() {
        requestButton.setOnAction(event -> {
            try {
                String response = getResponseFromServer();
                responseArea.setText(response);
            } catch (IOException e) {
                e.printStackTrace();
                responseArea.setText("Error: Failed to communicate with server.");
            }
        });
    }

    @FXML
    private TextField urlInputArea;
    private String getResponseFromServer() throws IOException {
        String urlString = urlInputArea.getText().trim();

        // Validate the URL string
        if (urlString.isEmpty()) {
            return "Error: URL is empty.";
        }
        if (!urlString.startsWith("http://") && !urlString.startsWith("https://")) {
            urlString = "http://" + urlString;
        }

        URL targetURL = new URL(urlString);
        URLConnection connection = targetURL.openConnection();
        connection.connect();

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
                response.append("\n");
            }
        }
        return response.toString();
    }
}
