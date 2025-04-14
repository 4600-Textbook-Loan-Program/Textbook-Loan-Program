package com.example.textbook_loan_program.view;

import com.example.textbook_loan_program.dao.JdbcBookDao;
import com.example.textbook_loan_program.model.Book;
import com.example.textbook_loan_program.service.BookService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    public static void show(Stage stage) {
        Label isbnLabel = new Label("ISBN:");
        TextField isbnField = new TextField();

        Button searchButton = new Button("Search");
        Button addButton = new Button("Add Book");

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
                    coverView.setImage(new Image(book.getCoverUrl()));
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
                statusLabel.setText("📚 Book already exists. Quantity increased.");
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
                statusLabel.setText("✅ Book added to database");
            }
        });

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

        HBox buttons = new HBox(10, addButton);
        VBox layout = new VBox(10, form, buttons, statusLabel);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-font-size: 14px; -fx-font-family: 'Arial';");


        Scene scene = new Scene(layout, 900, 800);
        stage.setScene(scene);
        stage.setTitle("Admin Dashboard - Book Management");
        stage.show();
    }
}

