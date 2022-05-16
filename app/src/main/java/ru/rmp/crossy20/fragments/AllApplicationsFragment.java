package ru.rmp.crossy20.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import ru.rmp.crossy20.adapters.ApplicationAdapter;
import ru.rmp.crossy20.models.Exchange;
import ru.rmp.crossy20.utils.ApplicationDialogFragment;

public class AllApplicationsFragment extends Fragment {
    View allApplicationsView;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference colRef = db.collection("exchange");

    static boolean isApplicant = false;
    static boolean isClicked = false;

    RecyclerView recyclerView;
    Toolbar toolbar;
    ArrayList<Exchange> exchanges = new ArrayList<>();

    public AllApplicationsFragment() {
        super(R.layout.all_applications_fragment);
    }

    public static AllApplicationsFragment newInstance() {
        AllApplicationsFragment fragment = new AllApplicationsFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setExchangesInArrayList();
        System.out.println("EXCHANGES AFTER SETTING: " + exchanges.toString());
        recyclerView = view.findViewById(R.id.all_applications_recyclerview);

        toolbar = view.findViewById(R.id.toolbar);
        initToolbar();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        allApplicationsView = super.onCreateView(inflater, container, savedInstanceState);
        return allApplicationsView;
    }

    private void setExchangesInArrayList() {
        colRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (doc.getData().get("accepted_id").equals(user.getUid()) || doc.getData().get("applicant_id").equals(user.getUid())) {
                                    exchanges.add(new Exchange(doc.getId(), doc.getData().get("accepted_nickname").toString(), doc.getData().get("applicant_nickname").toString(), doc.getData().get("book_title").toString()));
                                    System.out.println("EXCHANGES AFTER TASK: " + exchanges.toString());
                                }
                            }
                        }



                        ApplicationAdapter.OnApplicationClickListener applicationClickListener = new ApplicationAdapter.OnApplicationClickListener() {
                            @Override
                            public void onApplicationClick(Exchange exchange, int position) {
                                //обработка нажатия на заявку из списка
                                if (!exchange.isCrossedByApplicant() && !exchange.isCrossedByAccepted()) {
                                    //заявка не отмечена законченной ни с одной из сторон, кнопка о завершении заявки
                                    ApplicationDialogFragment dialog = new ApplicationDialogFragment("Заявка не завершена, отметьте завершенной", exchange);
                                    dialog.setDialogWithFinishApplicationButton();
                                    if (exchange.getApplicant().equals(user.getUid())) {
                                        isApplicant = true;
                                    } else {
                                        isApplicant = false;
                                    }
                                    dialog.show(getParentFragmentManager(), "custom");
                                    if(isClicked) {
                                        exchange.setCrossedByAccepted(true);
                                    }
                                }
                                if (exchange.isCrossedByApplicant() && !exchange.isCrossedByAccepted()) {
                                    //заявка отмечена законченной заявителем и не отмечена принимающим,
                                    // если заявитель текущий пользователь, то никаких действий,
                                    // если заявитель пользоаватель на стороне, то кнопка "завершить заявку"
                                    if (exchange.getApplicant().equals(user.getUid())) {
                                        isApplicant = true;
                                        Toast.makeText(getContext(), "Заявка не завершена другим пользователем", Toast.LENGTH_LONG).show();
                                    } else {
                                        isApplicant = false;
                                        ApplicationDialogFragment dialog = new ApplicationDialogFragment("Заявка не завершена, отметьте завершенной", exchange);
                                        dialog.setDialogWithFinishApplicationButton();
                                        dialog.show(getParentFragmentManager(), "custom");
                                        if(isClicked) {
                                            exchange.setCrossedByAccepted(true);
                                        }
                                    }
                                }
                                if (!exchange.isCrossedByApplicant() && exchange.isCrossedByAccepted()) {
                                    //заявка отмечена законченной принимающим и не отмечена заявителем,
                                    // если заявитель текущий пользователь, то кнопка "завершить заявку",
                                    // если заявить пользователь на стороне, то никаких действий
                                    if (exchange.getApplicant().equals(user.getUid())) {
                                        isApplicant = true;
                                        ApplicationDialogFragment dialog = new ApplicationDialogFragment("Заявка не завершена, отметьте завершенной", exchange);
                                        dialog.setDialogWithFinishApplicationButton();
                                        dialog.show(getParentFragmentManager(), "custom");
                                        if(isClicked) {
                                            exchange.setCrossedByAccepted(true);
                                        }
                                    } else {
                                        isApplicant = false;
                                        Toast.makeText(getContext(), "Заявка не завершена другим пользователем", Toast.LENGTH_LONG).show();
                                    }
                                }
                                if (exchange.isCrossedByApplicant() && exchange.isCrossedByAccepted()) {
                                    //заявка отмечена завершенной с обеих сторон, кнопка "оставить отзыв"
                                    if (exchange.getApplicant().equals(user.getUid())) {
                                        isApplicant = true;
                                    } else {
                                        isApplicant = false;
                                    }
                                    ApplicationDialogFragment dialog = new ApplicationDialogFragment("Заявка завершена, оставьте отзыв", exchange);
                                    dialog.setDialogWithReviewButton();
                                    dialog.show(getParentFragmentManager(), "custom");
                                }

                            }
                        };

                        ApplicationAdapter adapter = new ApplicationAdapter(getContext(), exchanges, applicationClickListener);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }

    public static boolean isCurrentUserAnApplicant() {
        return isApplicant;
    }

    public static boolean isPositiveButtonClicked() {
        isClicked = true;
        return isClicked;
    }

    private void initToolbar() {
        toolbar.setTitle("Заявки");
        toolbar.setTitleTextColor(Color.BLACK);
        getActivity().setActionBar(toolbar);
    }
}
