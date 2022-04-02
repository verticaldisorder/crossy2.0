package ru.rmp.crossy20.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.rmp.crossy20.R;
import ru.rmp.crossy20.adapters.FeedAdapter;
import ru.rmp.crossy20.models.Book;
import ru.rmp.crossy20.utils.MathModelUtil;

public class FeedFragment extends Fragment {
    ArrayList<Book> books = new ArrayList<Book>();

    TextView nickname;
    TextView author;
    TextView title;
    TextView genre;

    public FeedFragment() {
        super(R.layout.feed_fragment);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nickname = view.findViewById(R.id.item_feed_username_textview);
        author = view.findViewById(R.id.item_feed_author_textview);
        title = view.findViewById(R.id.item_feed_title_textview);
        genre = view.findViewById(R.id.item_feed_genre_textview);

        books = MathModelUtil.returnBooks();

        RecyclerView recyclerView = view.findViewById(R.id.feed_recyclerview);
        FeedAdapter adapter = new FeedAdapter(getContext(), books);
        recyclerView.setAdapter(adapter);
    }

    public static FeedFragment newInstance() {

        FeedFragment fragment = new FeedFragment();
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

        return inflater.inflate(R.layout.feed_fragment, container, false);
    }


}