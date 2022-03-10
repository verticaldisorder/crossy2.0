package ru.rmp.crossy20.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.rmp.crossy20.LogInFragment;
import ru.rmp.crossy20.R;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(mUser != null) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);

        } else {
            //открыть окно авторизации
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container_main_activity, LogInFragment.class, null)
                    .commit();
        }
    }
}