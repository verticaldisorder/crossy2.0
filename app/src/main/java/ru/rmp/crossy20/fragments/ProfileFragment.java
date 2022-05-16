package ru.rmp.crossy20.fragments;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import ru.rmp.crossy20.R;
import ru.rmp.crossy20.activities.ReviewActivity;
import ru.rmp.crossy20.utils.MathModelUtil;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    View profileView;
    static final int GALLERY_REQUEST = 1;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef = db.collection("bookholder").document(user.getUid());

    Toolbar toolbar;
    CircularProgressIndicator progressBar;
    TextView nickname;
    TextView address;
    Button addBookTextView;
    Button showApplicationsTextView;
    CheckBox handOnPersonally;
    CheckBox handOnPost;
    TextView booksInLibrary;
    TextView booksCrossed;
    TextView booksReviews;
    Button imageButton;
    ImageView profileImage;

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
        toolbar = profileView.findViewById(R.id.toolbar);
        progressBar = profileView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

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
        profileImage = profileView.findViewById(R.id.profile_image);

        initToolbar();
        setDataInFields();
        setProfileData();
        initClickableFields();
        initProfileImageButton();
        progressBar.setVisibility(View.GONE);
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

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nickname.getText().toString())
                        .build();

                StorageReference picRef = storageReference.child(task.getResult().getString("nickname"));

                if(picRef.toString() != null) {
                    final long ONE_MEGABYTE = 1024 * 1024;
                    picRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            profileImage.setImageBitmap(bmp);
                        }
                    });
                }
            }
        });


    }

    private void setProfileData() {
        MathModelUtil.getRecommendedBooksForCurrentUser();
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
                        .replace(R.id.fragment_container_profile_activity, LibraryFragment.newInstance(), null)
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

    private void initToolbar() {
        toolbar.setTitle("Профиль");
        toolbar.setTitleTextColor(Color.BLACK);
        getActivity().setActionBar(toolbar);
    }

    private void initProfileImageButton() {

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = null;

        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    profileImage.setImageURI(selectedImage);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //записать картинку в хранилище
                    profileImage.buildDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] byteData = baos.toByteArray();

                    UploadTask uploadTask = storageReference.child(nickname.getText().toString()).putFile(selectedImage);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "Profile image changed", Toast.LENGTH_LONG).show();
                        }
                    });
                }
        }


    }
}