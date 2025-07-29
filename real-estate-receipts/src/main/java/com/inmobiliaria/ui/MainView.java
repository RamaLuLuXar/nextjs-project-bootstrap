package com.inmobiliaria.ui;

import com.inmobiliaria.model.Receipt;
import com.inmobiliaria.service.AuthService;
import com.inmobiliaria.service.ReceiptService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.springframework.context.ConfigurableApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MainView {
    
    private final ConfigurableApplicationContext springContext;
    private final ReceiptService receiptService;
    private final AuthService authService;
    private final String currentUser;
    private BorderPane view;
    
    public MainView(ConfigurableApplicationContext springContext, String currentUser) {
        this.springContext = springContext;
        this.receiptService = springContext.getBean(ReceiptService.class);
        this.authService = springContext.getBean(AuthService.class);
        this.currentUser = currentUser;
        createView();
    }
    
    private void createView() {
        view = new BorderPane();
        view.setStyle("-fx-background-color: #f5f5f5;");
        
        // Barra superior
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #007bff;");
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("Sistema de Recibos - Inmobiliaria");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setStyle("-fx-text-fill: white;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button changePasswordBtn = new Button("Cambiar Contraseña");
        changePasswordBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        
        Button logoutBtn = new Button("Cerrar Sesión");
        logoutBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
        
        topBar.getChildren().addAll(title, spacer, changePasswordBtn, logoutBtn);
        
        // Panel izquierdo - menú
        VBox leftPanel = new VBox(15);
        leftPanel.setPadding(new Insets(20));
        leftPanel.setPrefWidth(200);
        leftPanel.setStyle("-fx-background-color: #e9ecef;");
        
        Button createReceiptBtn = new Button("Crear Recibo");
        createReceiptBtn.setPrefWidth(180);
        createReceiptBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        
        Button searchReceiptsBtn = new Button("Buscar Recibos");
        searchReceiptsBtn.setPrefWidth(180);
        searchReceiptsBtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
        
        Button viewAllBtn = new Button("Ver Todos");
        viewAllBtn.setPrefWidth(180);
        viewAllBtn.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white;");
        
        leftPanel.getChildren().addAll(createReceiptBtn, searchReceiptsBtn, viewAllBtn);
        
        // Panel central - tabla de recibos
        TableView<Receipt> receiptsTable = new TableView<>();
        receiptsTable.setPlaceholder(new Label("No hay recibos registrados"));
        
        TableColumn<Receipt, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));
        idCol.setPrefWidth(50);
        
        TableColumn<Receipt, String> receiptNumberCol = new TableColumn<>("Número");
        receiptNumberCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getReceiptNumber()));
        receiptNumberCol.setPrefWidth(120);
        
        TableColumn<Receipt, String> tenantCol = new TableColumn<>("Arrendatario");
        tenantCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTenantName()));
        tenantCol.setPrefWidth(200);
        
        TableColumn<Receipt, BigDecimal> amountCol = new TableColumn<>("Monto");
        amountCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getAmount()));
        amountCol.setPrefWidth(100);
        
        TableColumn<Receipt, String> conceptCol = new TableColumn<>("Concepto");
        conceptCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getConcept()));
        conceptCol.setPrefWidth(150);
        
        TableColumn<Receipt, LocalDate> dateCol = new TableColumn<>("Fecha");
        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getDate()));
        dateCol.setPrefWidth(100);
        
        receiptsTable.getColumns().addAll(idCol, receiptNumberCol, tenantCol, amountCol, conceptCol, dateCol);
        
        // Cargar recibos iniciales
        loadReceipts(receiptsTable);
        
        // Acciones de los botones
        createReceiptBtn.setOnAction(event -> {
            ReceiptFormView formView = new ReceiptFormView(springContext, currentUser, receiptsTable);
            view.setCenter(formView.getView());
        });
        
        searchReceiptsBtn.setOnAction(event -> {
            showSearchDialog(receiptsTable);
        });
        
        viewAllBtn.setOnAction(event -> {
            loadReceipts(receiptsTable);
            view.setCenter(createCenterPanel(receiptsTable));
        });
        
        changePasswordBtn.setOnAction(event -> {
            showChangePasswordDialog();
        });
        
        logoutBtn.setOnAction(event -> {
            LoginView loginView = new LoginView(springContext);
            view.getScene().setRoot(loginView.getView());
        });
        
        view.setTop(topBar);
        view.setLeft(leftPanel);
        view.setCenter(createCenterPanel(receiptsTable));
    }
    
    private VBox createCenterPanel(TableView<Receipt> table) {
        VBox center = new VBox(20);
        center.setPadding(new Insets(20));
        center.setAlignment(Pos.TOP_CENTER);
        
        Label subtitle = new Label("Recibos Registrados");
        subtitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        center.getChildren().addAll(subtitle, table);
        return center;
    }
    
    private void loadReceipts(TableView<Receipt> table) {
        List<Receipt> receipts = receiptService.getAllReceipts();
        table.getItems().setAll(receipts);
    }
    
    private void showSearchDialog(TableView<Receipt> table) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar Recibos");
        dialog.setHeaderText("Buscar por nombre de arrendatario o número de recibo");
        dialog.setContentText("Ingrese su búsqueda:");
        
        dialog.showAndWait().ifPresent(query -> {
            List<Receipt> results = receiptService.searchReceipts(query);
            table.getItems().setAll(results);
        });
    }
    
    private void showChangePasswordDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Cambiar Contraseña");
        dialog.setHeaderText("Cambiar contraseña de usuario");
        
        ButtonType changeButtonType = new ButtonType("Cambiar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(changeButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        PasswordField oldPassword = new PasswordField();
        oldPassword.setPromptText("Contraseña actual");
        
        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("Nueva contraseña");
        
        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Confirmar contraseña");
        
        grid.add(new Label("Contraseña actual:"), 0, 0);
        grid.add(oldPassword, 1, 0);
        grid.add(new Label("Nueva contraseña:"), 0, 1);
        grid.add(newPassword, 1, 1);
        grid.add(new Label("Confirmar:"), 0, 2);
        grid.add(confirmPassword, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == changeButtonType) {
                return newPassword.getText();
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(newPass -> {
            if (!newPassword.getText().equals(confirmPassword.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Las contraseñas no coinciden");
                alert.showAndWait();
                return;
            }
            
            boolean success = authService.changePassword(currentUser, oldPassword.getText(), newPass);
            Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            alert.setTitle(success ? "Éxito" : "Error");
            alert.setHeaderText(success ? "Contraseña cambiada exitosamente" : "Contraseña actual incorrecta");
            alert.showAndWait();
        });
    }
    
    public BorderPane getView() {
        return view;
    }
}
