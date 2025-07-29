package com.inmobiliaria.ui;

import com.inmobiliaria.model.Receipt;
import com.inmobiliaria.service.ReceiptService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.springframework.context.ConfigurableApplicationContext;

import java.math.BigDecimal;
import java.util.Optional;

public class ReceiptFormView {
    
    private final ConfigurableApplicationContext springContext;
    private final ReceiptService receiptService;
    private final String currentUser;
    private final TableView<Receipt> receiptsTable;
    private VBox view;
    
    public ReceiptFormView(ConfigurableApplicationContext springContext, String currentUser, 
                          TableView<Receipt> receiptsTable) {
        this.springContext = springContext;
        this.receiptService = springContext.getBean(ReceiptService.class);
        this.currentUser = currentUser;
        this.receiptsTable = receiptsTable;
        createView();
    }
    
    private void createView() {
        view = new VBox(20);
        view.setAlignment(Pos.TOP_CENTER);
        view.setPadding(new Insets(20));
        view.setStyle("-fx-background-color: #f5f5f5;");
        
        // Título
        Label title = new Label("Crear Nuevo Recibo");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        // Formulario
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(15);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.CENTER);
        
        // Campos del formulario
        TextField tenantField = new TextField();
        tenantField.setPromptText("Nombre del arrendatario");
        tenantField.setPrefWidth(300);
        
        TextField amountField = new TextField();
        amountField.setPromptText("Monto (ej: 1500.00)");
        amountField.setPrefWidth(300);
        
        TextField conceptField = new TextField();
        conceptField.setPromptText("Concepto del pago");
        conceptField.setPrefWidth(300);
        
        TextField periodField = new TextField();
        periodField.setPromptText("Período (ej: Enero 2024)");
        periodField.setPrefWidth(300);
        
        // Agregar campos al formulario
        form.add(new Label("Arrendatario:"), 0, 0);
        form.add(tenantField, 1, 0);
        
        form.add(new Label("Monto:"), 0, 1);
        form.add(amountField, 1, 1);
        
        form.add(new Label("Concepto:"), 0, 2);
        form.add(conceptField, 1, 2);
        
        form.add(new Label("Período:"), 0, 3);
        form.add(periodField, 1, 3);
        
        // Botones
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);
        
        Button saveButton = new Button("Guardar Recibo");
        saveButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        saveButton.setPrefWidth(150);
        
        Button cancelButton = new Button("Cancelar");
        cancelButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
        cancelButton.setPrefWidth(150);
        
        buttons.getChildren().addAll(saveButton, cancelButton);
        
        // Acciones
        saveButton.setOnAction(event -> {
            if (validateFields(tenantField, amountField, conceptField, periodField)) {
                try {
                    BigDecimal amount = new BigDecimal(amountField.getText());
                    
                    // Obtener usuario actual
                    Optional<User> user = authService.getUserRepository().findByUsername(currentUser);
                    if (user.isPresent()) {
                        Receipt receipt = receiptService.createReceipt(
                            tenantField.getText().trim(),
                            amount,
                            conceptField.getText().trim(),
                            periodField.getText().trim(),
                            user.get()
                        );
                        
                        // Actualizar tabla
                        receiptsTable.getItems().add(0, receipt);
                        
                        // Mostrar confirmación
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Éxito");
                        alert.setHeaderText("Recibo creado exitosamente");
                        alert.setContentText("Número de recibo: " + receipt.getReceiptNumber());
                        alert.showAndWait();
                        
                        // Limpiar formulario
                        clearFields(tenantField, amountField, conceptField, periodField);
                        
                        // Volver a la vista principal
                        MainView mainView = new MainView(springContext, currentUser);
                        view.getScene().setRoot(mainView.getView());
                    }
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("El monto debe ser un número válido");
                    alert.showAndWait();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error al crear el recibo");
                    alert.showAndWait();
                }
            }
        });
        
        cancelButton.setOnAction(event -> {
            MainView mainView = new MainView(springContext, currentUser);
            view.getScene().setRoot(mainView.getView());
        });
        
        view.getChildren().addAll(title, form, buttons);
    }
    
    private boolean validateFields(TextField... fields) {
        for (TextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Por favor complete todos los campos");
                alert.showAndWait();
                return false;
            }
        }
        return true;
    }
    
    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }
    
    public VBox getView() {
        return view;
    }
}
