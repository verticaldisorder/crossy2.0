package ru.rmp.crossy20.utils;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.ObjectValue;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import ru.rmp.crossy20.models.Book;
import ru.rmp.crossy20.models.User;

public class FirebaseManager {
    static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    static FirebaseUser user = mAuth.getCurrentUser();
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static User resultUser = new User();

    static CollectionReference bookholderReference = db.collection("bookholder");

    public static boolean addBook(String author, String title, String genre) {
        Map<String, Object> bookMap = new HashMap<>();
        bookMap.put("author", author);
        bookMap.put("bookholder_id", user.getUid());
        bookMap.put("genre_id", genre);
        bookMap.put("is_crossed", false);
        bookMap.put("title", title);

        db.collection("book")
                .document()
                .set(bookMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
        if (bookMap.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public static String getUserEmail(String uid) {
        final String[] result = new String[1];
        DocumentReference docRef = db.collection("bookholder").document(uid);


        docRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            result[0] = task.getResult().getData().get("email").toString();
                        }
                    }
                });
        return result[0];
    }

    public static String getUserUidByNickname(String nickname) {
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
}
