package ru.rmp.crossy20.utils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ru.rmp.crossy20.models.Book;
import ru.rmp.crossy20.models.User;

public class MathModelUtil {
    User user;
    ArrayList<String> userGenres;
    ArrayList<Book> books;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference bookCollectionReference = db.collection("book");
    CollectionReference bookholderCollectionReference = db.collection("bookholder");

    /** модель:
    1. получает пользователя, относительно которого расчитывается рекомендация и устанавливает его в поле user --сделано
    2. получает все книги из бд, которые разместил НЕ пользователь -- сделано
    3. удаляет из массива книг те, которые не подходят по флагам лично/по почте -- сделано
    4. (если пользователь не получает книги по почте) в первые элементы устанавливает те книги, которые значатся в том же городе -- сделано
    5. (если пользователь получает книги по почте) в первые элементы устанавливает книги, схожие с жанрами, которые пользователь сам выставляет -- сделано
    6. переработанный список книг вернуть в FeedFragment

     **/

    public MathModelUtil() {
        bookholderCollectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (doc.getData().get("nickname").equals(user.getNickname())) {
                                    user = new User(doc.getData().get("nickname").toString(), doc.getData().get("address").toString(), (Boolean)doc.getData().get("hand_over_personally"), (Boolean)doc.getData().get("hand_over_post"));
                                }
                            }
                        }
                    }
                });

        

        //TODO вызывает остальные методы для расчета, чтобы следующим обращением к мат модели извне был только возвратом результата
        setPreparatoryBooksArrayList();
        checkBooksFlags();
        if (!user.isHandOnPost()) {
            setBooksWithRelevantAddress();
        } else {
            setBooksWithRelevantGenre();
        }
    }

    public ArrayList<Book> getRecommendedBooksForCurrentUser() {
        if (books.isEmpty()) {
            setTestData();
        }
        return books;
    }

    private void setPreparatoryBooksArrayList() {
        bookCollectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (!doc.getData().get("bookholder_id").equals(mUser.getUid())) {
                                    bookholderCollectionReference
                                            .document(doc.getData().get("bookholder_id").toString())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                                    if (task2.isSuccessful() && (doc.getData().get("hand_over_personally") == task2.getResult().getData().get("hand_over_personally") || doc.getData().get("hand_over_post") == task2.getResult().getData().get("hand_over_post"))) {
                                                            books.add(new Book(doc.getData().get("author").toString(), doc.getData().get("title").toString(), doc.getData().get("genre").toString(), task2.getResult().get("nickname").toString(), false));
                                                    }
                                                }
                                            });


                                }
                            }
                        }
                    }
                });
    }

    private void checkBooksFlags() {
        for (Book b : books) {
            if (b.isCrossed()) {
                books.remove(b);
            }
         }
    }

    private void setBooksWithRelevantAddress() {
        ArrayList<Book> recommendBooks = new ArrayList<>();
        ArrayList<Book> rejectedBooks = new ArrayList<>();
        for (Book b : books) {
            bookholderCollectionReference
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    if(b.getBookholder().equals(doc.getData().get("nickname"))) {
                                        if (user.getAddress().equals(doc.getData().get("address"))) {
                                            recommendBooks.add(b);
                                        } else {
                                            rejectedBooks.add(b);
                                        }
                                    }
                                }
                            }
                        }
                    });
        }

        books.clear();
        books.addAll(recommendBooks);
        books.addAll(rejectedBooks);
    }

    private void setBooksWithRelevantGenre() {
        bookCollectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (doc.getData().get("bookholder_id").equals(mUser.getUid())) {
                                    userGenres.add(doc.getData().get("genre_id").toString());
                                }
                            }
                        }
                    }
                });

        if(!userGenres.isEmpty()) {
            ArrayList<Book> recommendedBooks = new ArrayList<>();
            ArrayList<Book> rejectedBooks = new ArrayList<>();

            for (Book b : books) {
                String genre = b.getGenre();
                for (String s : userGenres) {
                    if (s.equals(genre)) {
                        recommendedBooks.add(b);
                    }
                }
            }

            for (Book b : books) {
                if (!recommendedBooks.contains(b)) {
                    rejectedBooks.add(b);
                }
            }

            books.clear();
            books.addAll(recommendedBooks);
            books.addAll(rejectedBooks);
        }
    }

    private void setTestData() {
        books.add(new Book("author", "title", "genre", "bookholder", false));
        books.add(new Book("author2", "title2", "genre2", "bookholder2", false));
        books.add(new Book("author3", "title3", "genre3", "bookholder3", false));
    }

}
