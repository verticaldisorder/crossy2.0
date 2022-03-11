package ru.rmp.crossy20.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import ru.rmp.crossy20.R;
import ru.rmp.crossy20.utils.FirebaseManager;

public class ApplicationActivity extends AppCompatActivity {
    TextView nickname;
    TextView book;
    Button accept;
    Button decline;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();
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
                intent.putExtra(Intent.EXTRA_EMAIL, FirebaseManager.getUserEmail(FirebaseManager.getUserUidByNickname(nickname.getText().toString())));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Обмен книгами с пользователем " + mUser.getDisplayName());

                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    startActivity(intent);
                }
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
}
