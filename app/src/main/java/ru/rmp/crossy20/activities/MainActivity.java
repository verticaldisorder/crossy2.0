package ru.rmp.crossy20.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import ru.rmp.crossy20.fragments.LogInFragment;
import ru.rmp.crossy20.R;
import ru.rmp.crossy20.fragments.SignUpFragment;
import ru.rmp.crossy20.models.BookRequest;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container_main_activity, LogInFragment.newInstance("d", "d"), null)
                .commit();

//        if(mUser != null) {
//            new BookRequest().start();
//
//            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//            startActivity(intent);
//
//        } else {
//            //открыть окно авторизации
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .add(R.id.fragment_container_main_activity, LogInFragment.class, null)
//                    .commit();
//        }
    }
}