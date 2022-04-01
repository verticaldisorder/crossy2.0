package ru.rmp.crossy20.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
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

import ru.rmp.crossy20.R;
import ru.rmp.crossy20.activities.ReviewActivity;

public class ProfileFragment extends Fragment {
    View profileView;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef = db.collection("bookholder").document(user.getUid());

    TextView nickname;
    TextView address;
    TextView addBookTextView;
    TextView showApplicationsTextView;
    CheckBox handOnPersonally;
    CheckBox handOnPost;
    TextView booksInLibrary;
    TextView booksCrossed;
    TextView booksReviews;
    Button imageButton;

    public ProfileFragment() {
        super(R.layout.profile_fragment);
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nickname = profileView.findViewById(R.id.profile_nickname_textview);
        address = profileView.findViewById(R.id.profile_address_textview);
        addBookTextView = profileView.findViewById(R.id.profile_add_book_in_library_button);
        handOnPersonally = profileView.findViewById(R.id.profile_hand_on_personally_uneditable_checkbox);
        handOnPersonally.setEnabled(false);
        handOnPost = profileView.findViewById(R.id.profile_hand_on_post_uneditable_checkbox);
        handOnPost.setEnabled(false);
        booksInLibrary = profileView.findViewById(R.id.profile_books_in_library_count_textview);
        booksCrossed = profileView.findViewById(R.id.profile_crossed_books_count_textview);
        booksReviews = profileView.findViewById(R.id.profile_reviews_count_textview);
        showApplicationsTextView = profileView.findViewById(R.id.profile_show_applications_button);
        imageButton = profileView.findViewById(R.id.profile_nickname_image_button);

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
        profileView = inflater.inflate(R.layout.profile_fragment, container, false);
        return profileView;

    }

    private void setDataInFields() {

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                nickname.setText(task.getResult().getString("nickname"));
                address.setText(task.getResult().getString("address"));
                handOnPersonally.setChecked(task.getResult().getBoolean("hand_over_personally"));
                handOnPost.setChecked(task.getResult().getBoolean("hand_over_post"));
            }
        });


    }

    private void setProfileData() {
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
                                if (doc.getData().get("bookholder_id").equals(user.getUid())) {
                                    count++;
                                }
                            }

                            booksInLibrary.setText(""+String.valueOf(count));
                        } else {
                            System.out.println("smth wrong");
                        }


                    }
                });

        CollectionReference reviewsRef = db.collection("review");
        reviewsRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                System.out.println(doc);
                                if (doc.getData().get("reviewed_id").equals(user.getUid())) {
                                    count++;
                                }
                            }
                            System.out.println("Count: " + count);
                            booksReviews.setText(""+String.valueOf(count));
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
                                if (doc.getData().get("bookholder_id").equals(user.getUid()) && doc.getBoolean("is_crossed")) {
                                    count++;
                                }
                            }
                            booksCrossed.setText(""+String.valueOf(count));
                        }
                    }
                });

        CollectionReference reviewColRef = db.collection("review");
        Query thirdQuery = db.collection("review").whereEqualTo("author_id", user.getUid());
        reviewColRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            int count = 0;
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if(doc.getData().get("author_id").equals(user.getUid())) {
                                    count++;
                                    booksReviews.setText(""+String.valueOf(count));
                                }
                            }

                        }
                    }
                });

    }

    private void initClickableFields() {
        booksInLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_profile_activity, LibraryFragment.class, null)
                        .addToBackStack("tag")
                        .commit();
            }
        });

        booksReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ReviewActivity.class);
                startActivity(intent);
            }
        });

        addBookTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_profile_activity, AddBookFragment.class, null)
                        .addToBackStack("tag")
                        .commit();
            }
        });

        showApplicationsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_profile_activity, AllApplicationsFragment.newInstance(), null)
                        .addToBackStack("tag")
                        .commit();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO go to settings
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_profile_activity, SettingsFragment.newInstance(), null)
                        .addToBackStack("tag")
                        .commit();
            }
        });
    }
}