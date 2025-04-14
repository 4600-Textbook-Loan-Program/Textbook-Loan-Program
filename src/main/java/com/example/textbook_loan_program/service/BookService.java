package com.example.textbook_loan_program.service;

import com.example.textbook_loan_program.model.Book;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class BookService {
    public static Book fetchBookInfo(String title) {
        try {
            String encodedTitle = title.replace(" ", "+");
            String apiUrl = "https://openlibrary.org/search.json?title=" + encodedTitle;

            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            con.disconnect();

            JSONObject json = new JSONObject(response.toString());
            JSONArray docs = json.getJSONArray("docs");

            if (docs.length() > 0) {
                JSONObject first = docs.getJSONObject(0);
                String foundTitle = first.getString("title");
                String author = first.has("author_name") ? first.getJSONArray("author_name").getString(0) : "Unknown";
                return new Book(String.valueOf(0), foundTitle, author, 1);
            }
        } catch (Exception e) {
            System.out.println("Error fetching book: " + e.getMessage());
        }

        return null;
    }

    public static List<Book> fetchBooksByTitle(String title) {
        return null;
    }

    public static Book fetchBookByIsbn(String isbn) {
        try {
            String apiUrl = "https://openlibrary.org/api/books?bibkeys=ISBN:" + isbn + "&format=json&jscmd=data";
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            con.disconnect();

            JSONObject json = new JSONObject(response.toString());
            JSONObject bookJson = json.optJSONObject("ISBN:" + isbn);

            if (bookJson != null) {
                String title = bookJson.optString("title", "Unknown Title");
                String author = "Unknown Author";

                if (bookJson.has("authors")) {
                    JSONArray authors = bookJson.getJSONArray("authors");
                    if (authors.length() > 0) {
                        author = authors.getJSONObject(0).optString("name", author);
                    }
                }

                String description = bookJson.has("description")
                        ? (bookJson.get("description") instanceof JSONObject
                        ? bookJson.getJSONObject("description").optString("value", "")
                        : bookJson.getString("description"))
                        : "No description available";

                String coverUrl = bookJson.has("cover") ? bookJson.getJSONObject("cover").optString("medium", "") : "";

                return new Book(0,isbn, title, author, 1, "Available", coverUrl, description);
            }

        } catch (Exception e) {
            System.out.println(" Error fetching book by ISBN: " + e.getMessage());
        }

        return null;
    }

}
