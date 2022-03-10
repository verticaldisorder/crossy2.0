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
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import ru.rmp.crossy20.activities.ProfileActivity;

public class SignUpFragment extends Fragment {
    private final String TAG = "LOG_TAG";

    AppCompatEditText emailEditText;
    AppCompatEditText nicknameEditText;
    AppCompatEditText passwordEditText;
    AppCompatEditText addressEditText;
    Button registrationButton;
    CheckBox handOverPersonallyCheckbox;
    CheckBox handOnPostCheckbox;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public SignUpFragment() {
        super(R.layout.sign_up_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailEditText = view.findViewById(R.id.sign_up_email_edittext);
        nicknameEditText = view.findViewById(R.id.sign_up_nickname_edittext);
        passwordEditText = view.findViewById(R.id.sign_up_password_edittext);
        addressEditText = view.findViewById(R.id.sign_up_address_edittext);
        registrationButton = view.findViewById(R.id.sign_up_registration_button);
        handOverPersonallyCheckbox = view.findViewById(R.id.sign_up_hand_over_personally_checkbox);
        handOnPostCheckbox = view.findViewById(R.id.sign_up_hand_on_post_checkbox);

        initButtons();

    }

    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        return inflater.inflate(R.layout.sign_up_fragment, container, false);
    }

    private void initButtons() {
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRegisterUser();
            }
        });
    }

    //TODO set to FirebaseManager
    private void checkAndRegisterUser() {

        final String nickname = nicknameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        final String address = addressEditText.getText().toString().trim();
        final boolean isHandOverPersonally = handOverPersonallyCheckbox.isChecked();
        final boolean isHandOnPost = handOnPostCheckbox.isChecked();

        if (isHandOverPersonally == false && isHandOnPost == false) {
            Toast.makeText(this.getContext(), "Выберите как минимум одну опцию для обмена", Toast.LENGTH_LONG).show();
        } else if (nickname == null || email == null || password == null || address == null) {
            Toast.makeText(this.getContext(), "Заполните все поля", Toast.LENGTH_LONG);
        } else if (!email.contains("@") || !email.contains(".")) {
            Toast.makeText(this.getContext(), "Введите корректный e-mail", Toast.LENGTH_LONG).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "user created successfully");
                                user = mAuth.getCurrentUser();
                                mAuth.updateCurrentUser(user);

                                writeToDatabase(nickname, email, address, isHandOverPersonally, isHandOnPost);


                            } else {
                                Log.d(TAG, "user is not created");
                            }
                        }
                    });
        }
    }

    private void writeToDatabase(String nickname, String email, String address, boolean isHandOverPersonally, boolean isHandOnPost) {
        Map<String, Object> userMap = new HashMap<>();

        userMap.put("address", address);
        userMap.put("email", email);
        userMap.put("hand_over_personally", isHandOverPersonally);
        userMap.put("hand_over_post", isHandOnPost);
        userMap.put("nickname", nickname);

        db.collection("bookholder")
                .document(user.getUid())
                .set(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added");
                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error adding document", e);
                    }
                });
    }

}