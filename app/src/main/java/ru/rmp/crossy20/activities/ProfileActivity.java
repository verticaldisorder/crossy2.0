package ru.rmp.crossy20.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.rmp.crossy20.ProfileFragment;
import ru.rmp.crossy20.R;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_profile_activity, ProfileFragment.class, null)
                .commit();
    }
}
