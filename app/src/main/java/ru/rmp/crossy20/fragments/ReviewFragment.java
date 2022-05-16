package ru.rmp.crossy20.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

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
import ru.rmp.crossy20.activities.ReviewActivity;
import ru.rmp.crossy20.adapters.ReviewAdapter;
import ru.rmp.crossy20.models.Review;

public class ReviewFragment extends Fragment {
    Toolbar toolbar;
    View reviewsView;
    RecyclerView recyclerView;
    ArrayList<Review> reviews = new ArrayList<>();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference colRef = db.collection("review");

    public ReviewFragment() {
        super(R.layout.all_reviews_fragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        reviewsView = inflater.inflate(R.layout.all_reviews_fragment, container, false);
        return reviewsView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        initToolbar();

        setDataInReviews();
        System.out.println("reviews after setData(): " + reviews);

    }

    public static ReviewFragment newInstance() {
        ReviewFragment fragment = new ReviewFragment();
        return fragment;
    }

    private void setDataInReviews() {
        colRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (doc.getData().get("reviewed_id").equals(user.getUid())) {
                                    System.out.println(doc.getData().toString());
                                    reviews.add(new Review(doc.getData().get("content").toString(), doc.getData().get("author_id").toString(), user.getUid()));
                                    System.out.println(reviews.toString());
                                }
                            }

                            recyclerView = reviewsView.findViewById(R.id.all_reviews_recyclerview);
                            ReviewAdapter reviewAdapter = new ReviewAdapter(getContext(), reviews);
                            recyclerView.setAdapter(reviewAdapter);
                        }
                    }
                });
    }

    private void initToolbar() {
        toolbar.setTitle("Отзывы");
        toolbar.setTitleTextColor(Color.BLACK);
        getActivity().setActionBar(toolbar);
    }
}
