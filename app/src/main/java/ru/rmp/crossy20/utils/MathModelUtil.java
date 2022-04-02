package ru.rmp.crossy20.utils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import io.grpc.internal.JsonUtil;
import ru.rmp.crossy20.models.Book;
import ru.rmp.crossy20.models.User;

public class MathModelUtil {
    static User user = new User();
    static ArrayList<String> userGenres;
    static ArrayList<Book> books = new ArrayList<>();

    static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    static FirebaseUser mUser = mAuth.getCurrentUser();
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static CollectionReference bookCollectionReference = db.collection("book");
    static CollectionReference bookholderCollectionReference = db.collection("bookholder");

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
                                if (doc.getData().get("nickname").toString().equals(user.getNickname())) {
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

    public static void getRecommendedBooksForCurrentUser() {
        System.out.println("get recommended started");

        bookholderCollectionReference
                .document(mUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                                    user = new User(task.getResult().get("nickname").toString(), task.getResult().get("address").toString(), (Boolean)task.getResult().get("hand_over_personally"), (Boolean)task.getResult().get("hand_over_post"));
                        }
                    }
                });

        setPreparatoryBooksArrayList();

        checkBooksFlags();

        if (!user.isHandOnPost()) {
            System.out.println("SET BOOKS YAY");
            setBooksWithRelevantAddress();
        } else {
            System.out.println("SET BOOKS 2 YAY");
            setBooksWithRelevantGenre();
        }

        if (books.isEmpty()) {
            setTestData();
        }


    }

    public static ArrayList<Book> returnBooks() {
        return books;
    }

    private static void setPreparatoryBooksArrayList() {
        bookCollectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                System.out.println("DOCS ARE: " + doc.getData().get("bookholder_id"));
                                if (!doc.getData().get("bookholder_id").toString().equals(mUser.getUid())) {
                                    System.out.println("IN SET PREPORATORY bookholder found");
                                    bookholderCollectionReference
                                            .document("Tway8Gc6ZlPmliW7MdQMxnMZ0oj2")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                                    System.out.println("TASK2 result: " + task2.getResult().get("hand_over_personally"));
                                                    if (task2.isSuccessful() && ((Boolean)doc.getData().get("hand_over_personally") == (Boolean)task2.getResult().get("hand_over_personally") || (Boolean)doc.getData().get("hand_over_post") == (Boolean)task2.getResult().get("hand_over_post"))) {
//                                                        books.add(new Book(doc.getData().get("author").toString(), doc.getData().get("title").toString(), doc.getData().get("genre_id").toString(), "user", false));
                                                        System.out.println("IN SET PREPORATORY: " + books.toString());
                                                    }
                                                }
                                            });


                                }
                            }
                        }
                    }
                });
    }

    private static void checkBooksFlags() {
        for (Book b : books) {
            if (b.isCrossed()) {
                books.remove(b);
            }
         }
    }

    private static void setBooksWithRelevantAddress() {

        ArrayList<Book> recommendBooks = new ArrayList<>();
        ArrayList<Book> rejectedBooks = new ArrayList<>();
        for (Book b : books) {
            System.out.println("сюда заходит");
            bookholderCollectionReference
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot doc : task.getResult()) {

                                    if(b.getBookholderId().equals(doc.getData().get("nickname"))) {

                                        if (user.getAddress().equals(doc.getData().get("address"))) {
                                            System.out.println("added rec book: " + b.toString());
                                            recommendBooks.add(b);
                                        } else {
                                            System.out.println("added rej book: " + b.toString());
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

    private static void setBooksWithRelevantGenre() {
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

            books.addAll(recommendedBooks);
            books.addAll(rejectedBooks);
        }
    }

    private static void setTestData() {
//        books.add(new Book("Дж. Р. Р. Толкин", "Возвращение короля", "фэнтези", "alex", false));
//        books.add(new Book("Терри Пратчетт", "Патриот", "Детектив", "somebookguy", false));
//        books.add(new Book("Виктор Пелевин", "Бэтман Аполло", "фэнтези", "notanickname", false));

        bookCollectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                System.out.println("IS EMPTY " + doc.exists());
                                if (!doc.getData().get("bookholder_id").toString().equals(mUser.getUid())) {
                                    books.add(new Book(doc.getData().get("author").toString(), doc.getData().get("title").toString(), doc.getData().get("genre_id").toString(), doc.getData().get("bookholder_id").toString(), doc.getData().get("bookholder_nickname").toString(), false));
                                }
                            }
                        }
                    }
                });
    }

}
