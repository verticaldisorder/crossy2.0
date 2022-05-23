package ru.rmp.crossy20.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.SecurityPermission;
import java.util.Arrays;
import java.util.List;

import ru.rmp.crossy20.R;
import ru.rmp.crossy20.utils.FirebaseManager;

public class AddBookFragment extends Fragment {
    EditText title;
    EditText author;
    Spinner genre;
    Button addButton;
    static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    static FirebaseUser user = mAuth.getCurrentUser();


    public AddBookFragment() {
        super(R.layout.library_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.add_book_title_edittext);
        author = view.findViewById(R.id.add_book_author_edittext);
        genre = view.findViewById(R.id.add_book_genre_edittext);
        addButton = view.findViewById(R.id.add_book_add_book_button);

        initGenresAutoComplete();
        initButton();

    }

    public static AddBookFragment newInstance(String param1, String param2) {
        AddBookFragment fragment = new AddBookFragment();
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
        return inflater.inflate(R.layout.add_book_fragment, container, false);
    }

    private void initButton() {
        System.out.println("DISPLAY NAME IN INIT BUTTON: " + user.getDisplayName());
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseManager.addBook(author.getText().toString(), title.getText().toString(), genre.getSelectedItem().toString(), user.getDisplayName())) {
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container_profile_activity, ProfileFragment.newInstance(), null)
                            .addToBackStack("tag")
                            .commit();
                } else {
                    Toast.makeText(getContext(), "Что-то пошло не так, книга не добавлена", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initGenresAutoComplete() {
        List<String> genres = Arrays.asList(getResources().getStringArray(R.array.genres));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.genre_row, genres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genre.setAdapter(adapter);
    }
}