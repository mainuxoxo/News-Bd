package com.example.newsbd;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class NewsDetails extends AppCompatActivity {

    ImageView coverImage;
    TextView tvTitle, tvDes;
    FloatingActionButton fabButton;

    public static String TITLE = "";
    public static String DESCRIPTION = "";
    public static Bitmap MY_BITMAP = null;

    TextToSpeech textToSpeech;
    boolean isSpeaking = false; // Flag to track speaking state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        coverImage = findViewById(R.id.coverImage);
        tvTitle = findViewById(R.id.tvTitle);
        tvDes = findViewById(R.id.tvDes);
        fabButton = findViewById(R.id.fabButton);

        tvTitle.setText(TITLE != null ? TITLE : "No Title Available");
        tvDes.setText(DESCRIPTION != null ? DESCRIPTION : "No Description Available");
        if (MY_BITMAP != null) {
            coverImage.setImageBitmap(MY_BITMAP);
        } else {
            coverImage.setImageResource(R.drawable.news); // Replace with your placeholder image
        }

        textToSpeech = new TextToSpeech(NewsDetails.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.US); // Set desired language
                } else {
                    Toast.makeText(NewsDetails.this, "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSpeaking) {
                    // Stop speaking if currently speaking
                    textToSpeech.stop();
                    isSpeaking = false;
                    fabButton.setImageResource(R.drawable.voice_icon); // Reset icon if applicable
                } else {
                    // Start speaking if not already speaking
                    String text = tvDes.getText().toString();
                    if (!text.isEmpty()) {
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                        isSpeaking = true;
                        fabButton.setImageResource(R.drawable.stop); // Change to stop icon if applicable
                    } else {
                        Toast.makeText(NewsDetails.this, "No text to read!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}

