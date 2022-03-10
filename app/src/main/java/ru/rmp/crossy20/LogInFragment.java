package ru.rmp.crossy20;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.rmp.crossy20.activities.ProfileActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {
    private final String TAG = "LOG_TAG";

    AppCompatEditText emailEditText;
    AppCompatEditText passwordEditText;
    TextView signUp;
    Button signInButton;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    public LogInFragment() {
        super(R.layout.log_in_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailEditText = view.findViewById(R.id.login_email_edittext);
        passwordEditText = view.findViewById(R.id.login_password_edittext);
        signInButton = view.findViewById(R.id.login_login_button);

        initButtons();
    }

    // TODO: Rename and change types and number of parameters
    public static LogInFragment newInstance(String param1, String param2) {
        LogInFragment fragment = new LogInFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.log_in_fragment, container, false);
    }

    private void initButtons() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    Log.d(TAG, "user successfully logged in");
                                    mAuth.updateCurrentUser(mAuth.getCurrentUser());

                                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                                    startActivity(intent);
                                } else {
                                    Log.d(TAG, "some problems");
                                }
                            }
                        });
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_main_activity, SignUpFragment.class, null)
                        .commit();
            }
        });
    }

}