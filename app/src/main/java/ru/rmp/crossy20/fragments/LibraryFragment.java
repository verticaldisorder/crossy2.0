package ru.rmp.crossy20.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ru.rmp.crossy20.R;
import ru.rmp.crossy20.adapters.LibraryAdapter;
import ru.rmp.crossy20.models.Book;

public class LibraryFragment extends Fragment {
    RecyclerView libraryRecyclerView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference colRef = db.collection("book");

    public LibraryFragment() {
        super(R.layout.library_fragment);
    }

    public static LibraryFragment newInstance() {
        LibraryFragment fragment = new LibraryFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        libraryRecyclerView = view.findViewById(R.id.library_recyclerview);
        setData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.library_fragment, container, false);
    }

    private void setData() {
        ArrayList<Book> books = new ArrayList<>();
        colRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (doc.getData().get("bookholder_id").equals(user.getUid()) && doc.getData().get("is_crossed").equals(false)) {

                                    System.out.println("NOW IN BOOKS ARE: " + doc.getData().toString());

                                    Book book = new Book(doc.getData().get("author").toString(), doc.getData().get("title").toString(), doc.getData().get("genre_id").toString(), doc.getData().get("bookholder_id").toString(), doc.getData().get("bookholder_nickname").toString(), false);
                                    books.add(book);

                                    System.out.println("ARE BOOKS IN SETDATA EMPTY: " + books.isEmpty());


                                }

                                LibraryAdapter adapter = new LibraryAdapter(getActivity(), books);
                                System.out.println("BOOKS ARE EMPTY: " + books.isEmpty());
                                libraryRecyclerView.setAdapter(adapter);
                            }
                        }


                    }
                });
    }
}