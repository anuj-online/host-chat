package com.example.application.views;

import com.example.application.repo.WebsiteContent;
import com.example.application.repo.WebsiteContentRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Route("w")
public class WebsiteDownloaderView extends VerticalLayout {

    private TextField urlField;
    private Button fetchButton;
    private Button saveButton;
    private List<TextArea> textAreas = new ArrayList<>();
    private List<String> textBlocks = new ArrayList<>();

    @Autowired
    private WebsiteContentRepository contentRepository;

    public WebsiteDownloaderView() {
        urlField = new TextField("Website URL");
        fetchButton = new Button("Fetch Website Content");
        saveButton = new Button("Save to Database");

        fetchButton.addClickListener(e -> fetchWebsiteContent(urlField.getValue()));
        saveButton.addClickListener(e -> saveContentToDatabase());

        // Initially hide the save button
        saveButton.setVisible(false);

        add(urlField, fetchButton, saveButton);
    }

    private void fetchWebsiteContent(String url) {
        try {
            // Fetch the website HTML using Jsoup
            Document doc = Jsoup.connect(url).get();
            Elements paragraphs = doc.select("p"); // You can extend this to other HTML tags if needed

            // Clear previous content
            textBlocks.clear();
            textAreas.forEach(area -> remove(area));
            textAreas.clear();

            // Extract text from each paragraph and display it
            for (Element p : paragraphs) {
                String text = p.text();
                if (!text.isEmpty()) {
                    textBlocks.add(text);
                    TextArea textArea = new TextArea();
                    textArea.setValue(text);
                    textArea.setReadOnly(true); // Make it read-only so users can only approve
                    textArea.setHeight("100px"); // Adjust height as necessary
                    textArea.addValueChangeListener(event -> textBlocks.set(textAreas.indexOf(textArea), event.getValue()));
                    textAreas.add(textArea);
                    add(textArea);
                }
            }

            // Show the save button after loading content
            saveButton.setVisible(true);
            Notification.show("Website content fetched successfully!");

        } catch (Exception ex) {
            Notification.show("Error fetching website: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void saveContentToDatabase() {
        String fullText = String.join("\n\n", textBlocks);
        saveTextContent(fullText);
        Notification.show("Content saved to the database successfully!");

        // Optionally clear the UI after saving
        textBlocks.clear();
        textAreas.forEach(area -> remove(area));
        textAreas.clear();
        saveButton.setVisible(false);
    }

    private void saveTextContent(String fullText) {
        WebsiteContent websiteContent = new WebsiteContent();
        websiteContent.setUrl(urlField.getValue());
        websiteContent.setTextContent(fullText);
        contentRepository.save(websiteContent);
    }
}
