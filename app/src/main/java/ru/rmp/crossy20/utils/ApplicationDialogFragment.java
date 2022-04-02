package ru.rmp.crossy20.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.safetynet.HarmfulAppsData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import ru.rmp.crossy20.R;
import ru.rmp.crossy20.fragments.AllApplicationsFragment;
import ru.rmp.crossy20.fragments.LeaveReviewFragment;
import ru.rmp.crossy20.models.Exchange;

public class ApplicationDialogFragment extends DialogFragment {
    Exchange exchangeDocument;
    static String reviewedId;
    String message;
    int id = 0;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference colRef = db.collection("exchange");
    CollectionReference reviewsColRef = db.collection("review");

    public ApplicationDialogFragment(String message, Exchange exchangeDocument) {
        this.message = message;
        this.exchangeDocument = exchangeDocument;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (id == 1) {
            builder
                    .setTitle("Подтвердите действие")
                    .setView(R.layout.dialog)
                    .setMessage(message)
                    .setPositiveButton("Завершить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AllApplicationsFragment.isPositiveButtonClicked();
                            Map<String, Object> object = new HashMap<>();
                            if(AllApplicationsFragment.isCurrentUserAnApplicant()) {
                                object.put("is_crossed_by_applicant", true);

                            } else {
                                object.put("is_crossed_by_accepted", true);
                            }
                            colRef
                                    .document(exchangeDocument.getExchangeId())
                                    .set(object, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            System.out.println("document re-wrote successfully");

                                        }
                                    });
                        }
                    })
                    .setNegativeButton("Отмена", null);
        }

        if (id == 2) {
            builder
                    .setTitle("Подтвердите действие")
                    .setView(R.layout.dialog)
                    .setMessage(message)
                    .setPositiveButton("Оставить отзыв", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //переход на экран оставления отзывов
                            AllApplicationsFragment.isPositiveButtonClicked();
                            if(AllApplicationsFragment.isCurrentUserAnApplicant()) {
                                reviewedId = exchangeDocument.getAccepted();

                            } else {
                                reviewedId = exchangeDocument.getApplicant();
                            }

                            getParentFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container_profile_activity, LeaveReviewFragment.newInstance(), null)
                                    .addToBackStack("tag")
                                    .commit();
                        }
                    })
                    .setNegativeButton("Отмена", null);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_profile_activity, AllApplicationsFragment.newInstance())
                    .commit();
        }


        return builder.create();
    }

    public void setDialogWithFinishApplicationButton() {
        id = 1;
    }

    public void setDialogWithReviewButton() {
        id = 2;
    }

    public static String getExchangeDocument() {
        return reviewedId;
    }

}
