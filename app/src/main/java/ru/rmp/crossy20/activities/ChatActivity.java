package ru.rmp.crossy20.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import ru.rmp.crossy20.fragments.ChatFragment;
import ru.rmp.crossy20.fragments.ProfileFragment;
import ru.rmp.crossy20.R;

public class ChatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.feedFragment:
                        Intent intent = new Intent(ChatActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        return false;

                    case R.id.profileFragment:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container_profile_activity, ProfileFragment.newInstance(), null)
                                .addToBackStack("tag")
                                .commit();
                        return false;

                    case R.id.chatFragment:
                        return false;
                }
                return false;
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container_chat_activity, ChatFragment.newInstance(), null)
                .commit();
    }


}
