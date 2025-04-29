package com.example.textbook_loan_program.view;

import com.example.textbook_loan_program.dao.JdbcBookDao;
import com.example.textbook_loan_program.dao.JdbcHoldDao;
import com.example.textbook_loan_program.dao.JdbcLoanDao;
import com.example.textbook_loan_program.dao.JdbcUserDao;
import com.example.textbook_loan_program.model.Book;
import com.example.textbook_loan_program.model.Hold;
import com.example.textbook_loan_program.model.Loan;
import com.example.textbook_loan_program.model.Student;
import com.example.textbook_loan_program.service.BookService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class StudentDashboard {
    private static final JdbcBookDao bookDao = new JdbcBookDao();
    private static final JdbcHoldDao holdDao = new JdbcHoldDao();
    private static final JdbcLoanDao loanDao = new JdbcLoanDao();
    private static final JdbcUserDao userDao = new JdbcUserDao();

    private static ObservableList<Book> bookList;
    private static TableView<Book> bookTable;

    public static void show(Stage stage, Student student) {
        int userId = userDao.findIdByUsername(student.getUsername());
        String username = userDao.findUsernameById(userId);

        Label welcomeLabel = new Label("Welcome, " + username + "!");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label isbnLabel = new Label("ISBN:");
        TextField isbnField = new TextField();
        Button searchButton = new Button("Search");

        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField();
        titleField.setEditable(false);

        Label authorLabel = new Label("Author:");
        TextField authorField = new TextField();
        authorField.setEditable(false);

        Label descriptionLabel = new Label("Description:");
        TextArea descriptionArea = new TextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(3);

        Label coverUrlLabel = new Label("Cover URL:");
        TextField coverUrlField = new TextField();
        coverUrlField.setEditable(false);

        Label coverLabel = new Label("Cover:");
        ImageView coverView = new ImageView();
        coverView.setFitWidth(100);
        coverView.setFitHeight(150);

        Label statusLabel = new Label();

        Button placeHoldButton = new Button("Place Hold");
        Button historyButton = new Button("View Loan History");
        Button refreshButton = new Button("Refresh List");

        searchButton.setOnAction(e -> {
            Book book = BookService.fetchBookByIsbn(isbnField.getText().trim());
            if (book != null) {
                updateBookDetailFields(book, titleField, authorField, descriptionArea, coverUrlField, coverView);
                statusLabel.setText("Book found.");
            } else {
                statusLabel.setText("Book not found.");
            }
        });

        placeHoldButton.setOnAction(e -> {
            Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
            if (selectedBook == null) {
                showAlert(Alert.AlertType.ERROR, "Please select a book to place on hold.");
                return;
            }

            List<Hold> existingHolds = holdDao.findHoldsForBook(selectedBook.getId());
            boolean alreadyHeld = existingHolds.stream().anyMatch(h -> h.getStudentUsername().equals(username));

            if (alreadyHeld) {
                showAlert(Alert.AlertType.INFORMATION, "You already have a hold on this book.");
            } else {
                try (java.sql.Connection conn = com.example.textbook_loan_program.config.DatabaseConnector.getConnection();
                     java.sql.PreparedStatement stmt = conn.prepareStatement("INSERT INTO holds (student_username, book_id, hold_date) VALUES (?, ?, ?)");) {
                    stmt.setString(1, username);
                    stmt.setInt(2, selectedBook.getId());
                    stmt.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
                    stmt.executeUpdate();
                    showAlert(Alert.AlertType.INFORMATION, "Hold placed successfully for book: " + selectedBook.getTitle());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Failed to place hold.");
                }
            }
        });

        historyButton.setOnAction(e -> {
            List<Loan> allLoans = loanDao.findAll();
            List<Loan> userLoans = allLoans.stream()
                    .filter(loan -> loan.getUserId() == userId)
                    .collect(Collectors.toList());

            StringBuilder history = new StringBuilder();
            for (Loan loan : userLoans) {
                Book book = bookDao.findById(loan.getBookId());
                history.append("- ").append(book.getTitle())
                        .append(" (Status: ")
                        .append(loan.getReturnDate() != null ? "Returned" : "On Loan")
                        .append(")\n");
            }
            Alert historyAlert = new Alert(Alert.AlertType.INFORMATION);
            historyAlert.setTitle("Loan History");
            historyAlert.setHeaderText("Loan history for " + username);
            historyAlert.setContentText(history.toString());
            historyAlert.showAndWait();
        });

        refreshButton.setOnAction(e -> {
            bookList.setAll(bookDao.findAll());
            statusLabel.setText("List refreshed.");
        });

        bookTable = new TableView<>();
        bookList = FXCollections.observableArrayList(bookDao.findAll());

        bookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateBookDetailFields(newSelection, isbnField, titleField, authorField, descriptionArea, coverUrlField, coverView);
            }
        });

        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsbn()));

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));

        TableColumn<Book, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());

        bookTable.getColumns().addAll(isbnCol, titleCol, authorCol, quantityCol);
        bookTable.setItems(bookList);
        bookTable.setPrefHeight(300);

        GridPane form = new GridPane();
        form.setPadding(new Insets(20));
        form.setHgap(10);
        form.setVgap(10);

        form.add(isbnLabel, 0, 0);
        form.add(isbnField, 1, 0);
        form.add(searchButton, 2, 0);

        form.add(titleLabel, 0, 1);
        form.add(titleField, 1, 1);
        form.add(authorLabel, 0, 2);
        form.add(authorField, 1, 2);
        form.add(descriptionLabel, 0, 3);
        form.add(descriptionArea, 1, 3);
        form.add(coverUrlLabel, 0, 4);
        form.add(coverUrlField, 1, 4);
        form.add(coverLabel, 0, 5);
        form.add(coverView, 1, 5);

        HBox buttons = new HBox(10, placeHoldButton, historyButton, refreshButton);

        VBox layout = new VBox(20, welcomeLabel, form, buttons, statusLabel, new Label("Available Books:"), bookTable);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-font-size: 14px; -fx-font-family: 'Arial'; -fx-background-color: #f0f0f0;");

        Scene scene = new Scene(layout, 900, 750);
        stage.setScene(scene);
        stage.setTitle("Student Dashboard");
        stage.show();
    }

    private static void updateBookDetailFields(Book book, TextField titleField, TextField authorField, TextArea descriptionArea, TextField coverUrlField, ImageView coverView) {
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        descriptionArea.setText(book.getDescription());
        coverUrlField.setText(book.getCoverUrl());
        if (book.getCoverUrl() != null && !book.getCoverUrl().isEmpty()) {
            coverView.setImage(new Image(book.getCoverUrl(), true));
        } else {
            coverView.setImage(null);
        }
    }

    private static void updateBookDetailFields(Book book, TextField isbnField, TextField titleField, TextField authorField, TextArea descriptionArea, TextField coverUrlField, ImageView coverView) {
        isbnField.setText(book.getIsbn());
        updateBookDetailFields(book, titleField, authorField, descriptionArea, coverUrlField, coverView);
    }

    private static void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
