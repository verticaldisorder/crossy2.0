package ru.rmp.crossy20.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import ru.rmp.crossy20.R;
import ru.rmp.crossy20.utils.ApplicationDialogFragment;

public class LeaveReviewFragment extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference colRef = db.collection("review");

    View leaveReviewFragmentView;

    EditText review;
    Button addButton;

    public LeaveReviewFragment() {
        super(R.layout.leave_review_fragment);
    }

    public static LeaveReviewFragment newInstance() {
        LeaveReviewFragment fragment = new LeaveReviewFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        review = view.findViewById(R.id.leave_review_review_edittext);
        addButton = view.findViewById(R.id.leave_review_leave_review_button);

        initButton();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        leaveReviewFragmentView = super.onCreateView(inflater, container, savedInstanceState);
        return leaveReviewFragmentView;
    }

    private void initButton() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (review.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Введите отзыв", Toast.LENGTH_LONG).show();
                } else {
                    HashMap<String, Object> hashMap = new HashMap<>();

                    hashMap.put("author_id", user.getUid());
                    hashMap.put("content", review.getText().toString());
                    hashMap.put("reviewed_id", ApplicationDialogFragment.getExchangeDocument());

                    colRef
                            .document()
                            .set(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Отзыв успешно добавлен", Toast.LENGTH_LONG).show();

                                    getParentFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment_container_profile_activity, ProfileFragment.newInstance(), null)
                                            .commit();
                                }
                            });
                }
            }
        });
    }
}
