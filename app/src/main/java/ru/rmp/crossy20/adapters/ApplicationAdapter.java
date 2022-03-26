package ru.rmp.crossy20.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import ru.rmp.crossy20.R;
import ru.rmp.crossy20.models.Exchange;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {
    static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    static FirebaseUser mUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference bookholderRef = db.collection("bookholder");
    CollectionReference bookRef = db.collection("book");

    private final LayoutInflater inflater;
    private final List<Exchange> exchanges;

    public ApplicationAdapter(Context context, List<Exchange> exchanges, OnApplicationClickListener onClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.exchanges = exchanges;
        this.onClickListener = onClickListener;
    }

    public interface OnApplicationClickListener {
        void onApplicationClick(Exchange exchange, int position);
    }

    private final OnApplicationClickListener onClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_application, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exchange exchange = exchanges.get(position);
        holder.applicantView.setText(exchange.getApplicant());
        holder.bookView.setText(exchange.getBook());
        if (exchange.isCrossedByAccepted() && exchange.isCrossedByApplicant()) {
            holder.isCrossedView.setText("выполена");
        } else {
            holder.isCrossedView.setText("в процессе");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onApplicationClick(exchange, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exchanges.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView applicantView;
        final TextView bookView;
        final TextView isCrossedView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantView = itemView.findViewById(R.id.item_application_applicant_nickname_textview);
            bookView = itemView.findViewById(R.id.item_application_book_title_textview);
            isCrossedView = itemView.findViewById(R.id.item_application_is_crossed_textview);
        }
    }
}
