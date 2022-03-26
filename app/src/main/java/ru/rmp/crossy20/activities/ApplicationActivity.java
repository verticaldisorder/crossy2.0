package ru.rmp.crossy20.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.rmp.crossy20.R;
import ru.rmp.crossy20.utils.FirebaseManager;

public class ApplicationActivity extends AppCompatActivity {
    TextView nickname;
    TextView book;
    Button accept;
    Button decline;

    String email;
    String uid;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference colRef = db.collection("exchange");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);


        initFields();
        setValuesInFields();
        initButtons();
    }

    private void initFields() {
        nickname = findViewById(R.id.application_nickname_textview);
        book = findViewById(R.id.application_book_textview);
        accept = findViewById(R.id.application_accept_button);
        decline = findViewById(R.id.application_decline_button);
    }

    private void setValuesInFields() {
        nickname.setText(getIntent().getExtras().getString("Applicant"));
        book.setText(getIntent().getExtras().getString("Book"));
    }

    private void initButtons() {
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(Intent.ACTION_SEND);
                uid = getUserUidByNickname(nickname.getText().toString());
                getUserEmail(uid);
                intent.putExtra(Intent.EXTRA_EMAIL, email);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Обмен книгами с пользователем " + nickname);

                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    startActivity(intent);
                }

                Map<String, Object> bookMap = new HashMap<>();
                bookMap.put("accepted_id", mUser.getUid());
                bookMap.put("accepted_nickname", getUserNicknameByUid(mUser.getUid()));
                bookMap.put("applicant_id", uid);
                bookMap.put("applicant_nickname", nickname);
                bookMap.put("book_title", book);
                bookMap.put("is_crossed_by_accepted", false);
                bookMap.put("is_crossed_by_applicant", false);
                colRef
                        .document()
                        .set(bookMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        });
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //отклонение заявки
                finish();
            }
        });
    }

    private void getUserEmail(String uid) {
        final String[] result = new String[1];
        DocumentReference docRef = db.collection("bookholder").document(uid);


        docRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            email = task.getResult().getData().get("email").toString();
                        }
                    }
                });

    }

    private String getUserUidByNickname(String nickname) {
        final String[] result = new String[1];
        CollectionReference colRef = db.collection("bookholder");

        colRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (doc.getData().get("nickname").equals(nickname)) {
                                    result[0] = doc.getId();
                                }
                            }
                        }
                    }
                });
        return result[0];
    }

    private String getUserNicknameByUid(String uid) {
        final String[] result = new String[1];
        DocumentReference docRef = db.collection("bookholder").document(uid);

        docRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            result[0] = task.getResult().getId();
                        }
                    }
                });
        return result[0];
    }
}
