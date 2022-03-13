package ru.rmp.crossy20;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.rmp.crossy20.adapters.MessageAdapter;
import ru.rmp.crossy20.models.Message;
import ru.rmp.crossy20.utils.FirebaseManager;

public class ChatFragment extends Fragment {
    View chatView;

    private static final String TAG = "MainActivity";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER = 2;

    //create constants notification channel
    private static final int NOTIFICATION_ID = 101;
    private static final String CHANNEL_ID = "Default channel";

    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;
    private Button mSendButton;

    private String mUsername;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseDatabase mFirebaseDatabase; //точка входа для приложения для доступа к бд
    private DatabaseReference mMessagesDatabaseReference; //ссылается на конкретный объект в бд, в данном случае на сообщеие
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;

    public ChatFragment() {
        // Required empty public constructor
        super(R.layout.chat_fragment);
    }

    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        getUsername();
        System.out.println("USERNAME IS: " + mUsername);

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages"); //getReference() возвращает ссылку на корневой узел бд

        // Initialize references to views
        System.out.println("GET VIEW: " + chatView.toString());

        mProgressBar = chatView.findViewById(R.id.progressBar);
        mMessageListView =  chatView.findViewById(R.id.messageListView);
        mMessageEditText =  chatView.findViewById(R.id.messageEditText);
        mSendButton =  chatView.findViewById(R.id.sendButton);

        // Initialize message ListView and its adapter
        List<Message> messages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this.getContext(), R.layout.item_message, messages);
        System.out.println("CONTEXT: " + this.getContext().toString() + " MESSAGES: " + messages.toString() + " ADAPTER: " + mMessageAdapter.toString() + " LISTVIEW: " + mMessageListView.toString());
        mMessageListView.setAdapter(mMessageAdapter);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        attachDatabaseReadListener();

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click
                Message friendlyMessage = new Message(mMessageEditText.getText().toString(), mUsername);
                mMessagesDatabaseReference.push().setValue(friendlyMessage);
                // Clear input box
                mMessageEditText.setText("");
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chatView = inflater.inflate(R.layout.chat_fragment, container, false);
        return chatView;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void onSignedInInitialize(String username) {
        mUsername = username;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
        mMessageAdapter.clear();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Message friendlyMessage = snapshot.getValue(Message.class);
                    mMessageAdapter.add(friendlyMessage);
                }


                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    private void getUsername() {
        DocumentReference documentReference = db.collection("bookholder").document(mFirebaseAuth.getCurrentUser().getUid());

        documentReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            mUsername = task.getResult().getData().get("nickname").toString();
                        }
                    }
                });

    }
}