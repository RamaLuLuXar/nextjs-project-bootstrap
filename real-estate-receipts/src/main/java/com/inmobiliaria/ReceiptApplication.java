package com.inmobiliaria;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ReceiptApplication extends Application {

    private ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        // Desactivar la consola de JavaFX
        System.setProperty("java.awt.headless", "true");
        launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(ReceiptApplication.class);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Configurar la ventana principal
            primaryStage.setTitle("Sistema de Recibos - Inmobiliaria");
            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);
            primaryStage.setResizable(true);
            
            // Cargar la vista de login
            LoginView loginView = new LoginView(springContext);
            Scene scene = new Scene(loginView.getView(), 400, 300);
            
            primaryStage.setScene(scene);
            primaryStage.show();
            
            // Configurar cierre de aplicación
            primaryStage.setOnCloseRequest(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmar salida");
                alert.setHeaderText("¿Está seguro de que desea salir?");
                alert.setContentText("Todos los cambios no guardados se perderán.");
                
                if (alert.showAndWait().get() == ButtonType.OK) {
                    Platform.exit();
                    System.exit(0);
                } else {
                    event.consume();
                }
            });
            
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al iniciar la aplicación");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        if (springContext != null) {
            springContext.close();
        }
    }
}
