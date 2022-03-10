package ru.rmp.crossy20.utils;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.rmp.crossy20.models.Book;

public class FirebaseManager {
    static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    static FirebaseUser user = mAuth.getCurrentUser();
    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    static CollectionReference bookholderReference = db.collection("bookholder");

    public static List<Object> getUserProfileInfo(String uid) {
        List<Object> result = new ArrayList<>();
        DocumentReference documentReference = bookholderReference.document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                result.add(task.getResult().getString("nickname"));
                result.add(task.getResult().getString("address"));
                result.add(task.getResult().getBoolean("hand_over_personally"));
                result.add(task.getResult().getBoolean("hand_over_post"));
            }
        });
        return result;
    }

    public static List<String> getProfileData(String uid) {
        List<String> result = new ArrayList<>();
        CollectionReference colRef = db.collection("book");
        colRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        System.out.println("success: " + task.isSuccessful());
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                System.out.println(doc);
                                if (doc.getData().get("bookholder_id").equals(uid)) {
                                    count++;
                                }
                            }

                            result.add(""+String.valueOf(count));
                        } else {
                            System.out.println("smth wrong");
                        }


                    }
                });

        colRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (doc.getData().get("bookholder_id").equals(uid) && doc.getBoolean("is_crossed")) {
                                    count++;
                                }
                            }
                            result.add(""+String.valueOf(count));
                        }
                    }
                });

        CollectionReference reviewColRef = db.collection("review");
        Query thirdQuery = db.collection("review").whereEqualTo("author_id", uid);
        reviewColRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            int count = 0;
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if(doc.getData().get("author_id").equals(uid)) {
                                    count++;
                                }
                            }
                            result.add(""+String.valueOf(count));
                        }
                    }
                });
        return result;
    }

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

    public static ArrayList<Book> getBooksForUserLibrary(String uid) {
        ArrayList<Book> books = new ArrayList<>();
        CollectionReference colRef = db.collection("book");
        colRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (doc.getData().get("bookholder_id").equals(uid) && doc.getData().get("is_crossed").equals(false)) {

                                    System.out.println("NOW IN BOOKS ARE: " + doc.getData().toString());
//                                Book book = new Book(doc.getData().get("author").toString(), doc.getData().get("title").toString(), doc.getData().get("genre_id").toString(), doc.getData().get("nickname").toString(), false);

                                    //ГОСПОДИ ИИСУСЕ ПОЧЕМУ ЭТО РАБОТАЕТ
                                    Book book = new Book("a", "t", "g", "n", false);
                                    books.add(book);

                                    System.out.println("ARE BOOKS IN SETDATA EMPTY: " + books.isEmpty());
                                }


                            }
                        }
                    }
                });
                return books;
    }

}
