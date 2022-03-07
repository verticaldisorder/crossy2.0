package ru.rmp.crossy20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(mUser != null) {
            //TODO открыть страницу пользователя

        } else {
            //открыть окно авторизации
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container_main_activity, LogInFragment.class, null)
                    .commit();
        }
    }
}