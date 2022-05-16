package ru.rmp.crossy20.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.rmp.crossy20.R;

public class SettingsFragment extends Fragment {
    View settingsView;
    Toolbar toolbar;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    Switch notificationSwitch;
    Button logoutButton;

    public SettingsFragment() {
        super(R.layout.settings_fragment);
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        logoutButton = settingsView.findViewById(R.id.settings_fragment_logout_button);
        notificationSwitch = settingsView.findViewById(R.id.settings_fragment_notifications_switch);
        toolbar = view.findViewById(R.id.toolbar);
        initToolbar();

        initButton();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        settingsView = inflater.inflate(R.layout.settings_fragment, container, false);
        return settingsView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initButton() {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                getActivity().finish();
            }
        });
    }

    private void initToolbar() {
        toolbar.setTitle("Настройки");
        toolbar.setTitleTextColor(Color.BLACK);
        getActivity().setActionBar(toolbar);
    }
}

