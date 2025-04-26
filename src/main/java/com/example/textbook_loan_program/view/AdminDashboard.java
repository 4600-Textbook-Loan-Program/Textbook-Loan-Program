package com.example.textbook_loan_program.view;

import com.example.textbook_loan_program.dao.JdbcBookDao;
import com.example.textbook_loan_program.model.Book;
import com.example.textbook_loan_program.service.BookService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AdminDashboard {

    private static final JdbcBookDao bookDao = new JdbcBookDao();
    private static TextField coverUrlField;
    private static TextArea descriptionArea;
    private static TableView<Book> bookTable;
    private static ObservableList<Book> bookList;

    public static void show(Stage stage) {
        Label isbnLabel = new Label("ISBN:");
        TextField isbnField = new TextField();

        Button searchButton = new Button("Search");
        Button addButton = new Button("Add Book");

        Button deleteButton = new Button("Delete Selected Book");
        Button refreshButton = new Button("Refresh Table");

        Button updateButton = new Button("Update Selected Book");

        Button clearButton = new Button("Clear");

        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField();

        Label authorLabel = new Label("Author:");
        TextField authorField = new TextField();

        Label coverLabel = new Label("Cover:");
        ImageView coverView = new ImageView();
        coverView.setFitWidth(100);
        coverView.setFitHeight(150);

        Label coverUrlLabel = new Label("Cover URL:");
        coverUrlField = new TextField();

        Label descriptionLabel = new Label("Description:");
        descriptionArea = new TextArea();
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(3);

        Label statusLabel = new Label();


        searchButton.setOnAction(e -> {
            String isbn = isbnField.getText().trim();
            Book book = BookService.fetchBookByIsbn(isbn);
            if (book != null) {
                titleField.setText(book.getTitle());
                authorField.setText(book.getAuthor());
                descriptionArea.setText(book.getDescription());
                coverUrlField.setText(book.getCoverUrl());

                if (book.getCoverUrl() != null && !book.getCoverUrl().isEmpty()) {
                    coverView.setImage(new Image(book.getCoverUrl(), true));
                } else {
                    coverView.setImage(null);
                }

                statusLabel.setText("Book found");
            } else {
                statusLabel.setText("Book not found");
            }
        });

        addButton.setOnAction(e -> {
            String isbn = isbnField.getText().trim();
            Book existing = bookDao.findByIsbn(isbn);

            if (existing != null) {
                existing.setQuantity(existing.getQuantity() + 1);
                bookDao.update(existing);
                statusLabel.setText("Book already exists. Quantity increased.");
            } else {
                Book newBook = new Book(0,
                        isbn,
                        titleField.getText().trim(),
                        authorField.getText().trim(),
                        1,
                        "Available",
                        coverUrlField.getText().trim(),
                        descriptionArea.getText().trim()
                );
                try {
                    bookDao.save(newBook);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                statusLabel.setText("Book added to database");
            }
        });

        deleteButton.setOnAction(e -> {
            Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Deletion");
                confirmAlert.setHeaderText("Delete one copy of \"" + selectedBook.getTitle() + "\"?");
                confirmAlert.setContentText("This will reduce quantity by 1. If this is the last copy, the book will be removed completely.");

                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (selectedBook.getQuantity() > 1) {
                            // Reduce quantity by 1
                            selectedBook.setQuantity(selectedBook.getQuantity() - 1);
                            bookDao.update(selectedBook);
                            bookList.setAll(bookDao.findAll());
                        } else {
                            // Last copy - delete the book
                            bookDao.delete(selectedBook.getId());
                            bookList.remove(selectedBook);
                        }
                    }
                });
            }
        });

        refreshButton.setOnAction(e -> {
            bookList.setAll(bookDao.findAll());
            isbnField.clear();
            titleField.clear();
            authorField.clear();
            descriptionArea.clear();
            coverUrlField.clear();
            coverView.setImage(null);
            statusLabel.setText("Table refreshed.");
        });

        updateButton.setOnAction(e -> {
            Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                selectedBook.setTitle(titleField.getText().trim());
                selectedBook.setAuthor(authorField.getText().trim());
                selectedBook.setCoverUrl(coverUrlField.getText().trim());
                selectedBook.setDescription(descriptionArea.getText().trim());

                bookDao.update(selectedBook);
                bookList.setAll(bookDao.findAll());
            }
        });


        clearButton.setOnAction(e -> {
            isbnField.clear();
            titleField.clear();
            authorField.clear();
            descriptionArea.clear();
            coverUrlField.clear();
            coverView.setImage(null);
            statusLabel.setText("");
        });


        bookTable = new TableView<>();
        bookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                isbnField.setText(newSelection.getIsbn());
                titleField.setText(newSelection.getTitle());
                authorField.setText(newSelection.getAuthor());
                coverUrlField.setText(newSelection.getCoverUrl());
                descriptionArea.setText(newSelection.getDescription());

                if (newSelection.getCoverUrl() != null && !newSelection.getCoverUrl().isEmpty()) {
                    coverView.setImage(new Image(newSelection.getCoverUrl()));
                } else {
                    coverView.setImage(null);
                }
            }
        });

        bookList = FXCollections.observableArrayList(bookDao.findAll());

        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));



        TableColumn<Book, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionCol.setPrefWidth(300);

        bookTable.getColumns().addAll( isbnCol, titleCol, authorCol, quantityCol, descriptionCol);
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

        HBox buttons = new HBox(10, addButton, deleteButton,updateButton, clearButton, refreshButton);
        VBox layout = new VBox(20, form, buttons, statusLabel, new Label("All Books in System:"), bookTable);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-font-size: 14px; -fx-font-family: 'Arial'; -fx-background-color: #f9f9f9;");



        Scene scene = new Scene(layout, 1000, 850);
        stage.setScene(scene);
        stage.setTitle("Admin Dashboard - Book Management");
        stage.show();
    }
}

