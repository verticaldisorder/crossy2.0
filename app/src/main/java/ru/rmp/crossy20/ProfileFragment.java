package ru.rmp.crossy20;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import ru.rmp.crossy20.utils.FirebaseManager;

public class ProfileFragment extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    TextView nickname;
    TextView address;
    TextView addBookTextView;
    CheckBox handOnPersonally;
    CheckBox handOnPost;
    TextView booksInLibrary;
    TextView booksCrossed;
    TextView booksReviews;

    public ProfileFragment() {
        super(R.layout.profile_fragment);
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nickname = view.findViewById(R.id.profile_nickname_textview);
        address = view.findViewById(R.id.profile_address_textview);
        addBookTextView = view.findViewById(R.id.profile_add_book_in_library_button);
        handOnPersonally = view.findViewById(R.id.profile_hand_on_personally_uneditable_checkbox);
        handOnPost = view.findViewById(R.id.profile_hand_on_post_uneditable_checkbox);
        booksInLibrary = view.findViewById(R.id.profile_books_in_library_count_textview);
        booksCrossed = view.findViewById(R.id.profile_crossed_books_count_textview);
        booksReviews = view.findViewById(R.id.profile_reviews_count_textview);

        setDataInFields();
        setProfileData();
        initClickableFields();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    private void setDataInFields() {
        List<Object> profileData = FirebaseManager.getUserProfileInfo(user.getUid());
        nickname.setText(profileData.get(0).toString());
        address.setText(profileData.get(1).toString());
        handOnPersonally.setChecked((Boolean) profileData.get(2));
        handOnPost.setChecked((Boolean) profileData.get(3));
    }

    private void setProfileData() {
        List<String> profileData = FirebaseManager.getProfileData(user.getUid());
        booksInLibrary.setText(profileData.get(0).toString());
        booksCrossed.setText(profileData.get(0).toString());
        booksReviews.setText(profileData.get(0).toString());
    }

    private void initClickableFields() {
        booksInLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO переход в библиотеку
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_profile_activity, LibraryFragment.class, null)
                        .commit();
            }
        });

        addBookTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_profile_activity, AddBookFragment.class, null)
                        .commit();
            }
        });
    }
}