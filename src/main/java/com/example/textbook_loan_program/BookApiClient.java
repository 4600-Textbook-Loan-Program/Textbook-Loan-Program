package com.example.textbook_loan_program;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BookApiClient {
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
                return new Book(String.valueOf(0), foundTitle, author, String.valueOf(1));
            }
        } catch (Exception e) {
            System.out.println("❌ Error fetching book: " + e.getMessage());
        }

        return null;
    }
}
