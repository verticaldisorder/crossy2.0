package ru.rmp.crossy20;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import ru.rmp.crossy20.adapters.LibraryAdapter;
import ru.rmp.crossy20.models.Book;
import ru.rmp.crossy20.utils.FirebaseManager;

public class LibraryFragment extends Fragment {
    RecyclerView libraryRecyclerView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    public LibraryFragment() {
        super(R.layout.library_fragment);
    }

    public static LibraryFragment newInstance(String param1, String param2) {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
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

        books = FirebaseManager.getBooksForUserLibrary(user.getUid());

        LibraryAdapter adapter = new LibraryAdapter(getContext(), books);
        System.out.println("BOOKS ARE EMPTY: " + books.isEmpty());
        libraryRecyclerView.setAdapter(adapter);
    }
}