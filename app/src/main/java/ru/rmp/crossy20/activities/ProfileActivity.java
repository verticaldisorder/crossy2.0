package ru.rmp.crossy20.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import ru.rmp.crossy20.fragments.FeedFragment;
import ru.rmp.crossy20.fragments.ProfileFragment;
import ru.rmp.crossy20.R;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.feedFragment:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container_profile_activity, FeedFragment.newInstance(), null)
                                .addToBackStack("tag")
                                .commit();
                        return false;

                    case R.id.profileFragment:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container_profile_activity, ProfileFragment.newInstance(), null)
                                .addToBackStack("tag")
                                .commit();
                        return false;

                    case R.id.chatFragment:
                        Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
                        startActivity(intent);
                        return false;
                }
                return false;
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_profile_activity, ProfileFragment.newInstance(), null)
                .commit();
    }
}
