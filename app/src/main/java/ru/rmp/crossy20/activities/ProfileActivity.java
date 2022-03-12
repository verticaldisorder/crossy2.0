package ru.rmp.crossy20.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import ru.rmp.crossy20.FeedFragment;
import ru.rmp.crossy20.ProfileFragment;
import ru.rmp.crossy20.R;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
//        NavController controller = Navigation.findNavController(this, R.id.nav_host_fragment);
//
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.feedFragment:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container_profile_activity, FeedFragment.newInstance(), null)
                                .commit();
                        return false;

                    case R.id.profileFragment:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container_profile_activity, ProfileFragment.newInstance(), null)
                                .commit();
                        return false;

                    case R.id.placeholder:
                        Toast.makeText(ProfileActivity.this, "It will be chat", Toast.LENGTH_LONG).show();
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
