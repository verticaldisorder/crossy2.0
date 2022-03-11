package ru.rmp.crossy20.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import ru.rmp.crossy20.R;
import ru.rmp.crossy20.models.Book;
import ru.rmp.crossy20.models.BookRequest;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    static FirebaseUser mUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference bookholderRef = db.collection("bookholder");
    CollectionReference bookRef = db.collection("book");


    private final LayoutInflater inflater;
    private final List<Book> books;

    public FeedAdapter(Context context, List<Book> books) {
        this.inflater = LayoutInflater.from(context);
        this.books = books;

    }

    @NonNull
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_book_in_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.ViewHolder holder, int position) {
        Book book = books.get(position);
        holder.usernameView.setText(book.getBookholder()); //возможно вернется ключ в бд, а не ник
        holder.authorView.setText(book.getAuthor());
        holder.titleView.setText(book.getTitle());
        holder.genreView.setText(book.getGenre());

        initButton(holder.sendIntentToBook, getUidByNickname(holder.usernameView.getText().toString()), getBookByTitle(holder.titleView.getText().toString()));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView usernameView;
        final TextView authorView;
        final TextView titleView;
        final TextView genreView;
        final Button sendIntentToBook;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameView = itemView.findViewById(R.id.item_feed_username_textview);
            authorView = itemView.findViewById(R.id.item_feed_author_textview);
            titleView = itemView.findViewById(R.id.item_feed_title_textview);
            genreView = itemView.findViewById(R.id.item_feed_genre_textview);
            sendIntentToBook = itemView.findViewById(R.id.item_feed_set_application_book_button);


        }
    }



    private static void initButton(Button button, String applicant, String book) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(button.getContext(), "Заявка отправлена", Toast.LENGTH_LONG).show();
                BookRequest bookRequest = new BookRequest(mUser.getUid(), applicant, book, button.getContext());
                System.out.println("ITS OKAY: " + bookRequest.toString());
            }
        });
    }

    private String getUidByNickname(String nickname) {
        final String[] result = new String[1];
        bookholderRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            System.out.println("NICKNAME GET SUCCESSFULLY");
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (doc.getData().get("nickname").equals(nickname)) {
                                    result[0] = doc.getId();
                                }
                            }
                        }
                    }
                });
        return result[0];
    }

    private String getBookByTitle(String title) {
        final String[] result = new String[1];
        bookRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            System.out.println("BOOK GET SUCCESSFULLY");
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if(doc.getData().get("title").equals(title)) {
                                    result[0] = doc.getId();
                                }
                            }
                        }
                    }
                });
        return result[0];
    }


}
