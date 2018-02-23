package ru.psdevelop.tdapptemareg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button registerButton, requireButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerButton = (Button) findViewById(R.id.register_button);
        requireButton = (Button) findViewById(R.id.requires_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getBaseContext(),
                        RegistrationActivity.class);
                startActivity(registerIntent);
            }
        });

        requireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent requiresIntent = new Intent(getBaseContext(),
                        RequiresActivity.class);
                startActivity(requiresIntent);
            }
        });

    }
}
