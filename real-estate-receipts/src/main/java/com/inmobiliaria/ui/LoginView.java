package com.inmobiliaria.ui;

import com.inmobiliaria.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.springframework.context.ConfigurableApplicationContext;

public class LoginView {
    
    private final ConfigurableApplicationContext springContext;
    private final AuthService authService;
    private VBox view;
    
    public LoginView(ConfigurableApplicationContext springContext) {
        this.springContext = springContext;
        this.authService = springContext.getBean(AuthService.class);
        createView();
    }
    
    private void createView() {
        view = new VBox(20);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(40));
        view.setStyle("-fx-background-color: #f5f5f5;");
        
        // Título
        Label title = new Label("Sistema de Recibos");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        // Campos de entrada
        TextField usernameField = new TextField();
        usernameField.setPromptText("Usuario");
        usernameField.setMaxWidth(250);
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");
        passwordField.setMaxWidth(250);
        
        // Botón de login
        Button loginButton = new Button("Iniciar Sesión");
        loginButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px;");
        loginButton.setPrefWidth(250);
        
        // Mensaje de error
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        
        // Acción del botón
        loginButton.setOnAction(event -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            
            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Por favor complete todos los campos");
                return;
            }
            
            if (authService.authenticate(username, password)) {
                // Login exitoso - abrir vista principal
                MainView mainView = new MainView(springContext, username);
                view.getScene().setRoot(mainView.getView());
            } else {
                errorLabel.setText("Usuario o contraseña incorrectos");
            }
        });
        
        view.getChildren().addAll(title, usernameField, passwordField, loginButton, errorLabel);
    }
    
    public VBox getView() {
        return view;
    }
}
