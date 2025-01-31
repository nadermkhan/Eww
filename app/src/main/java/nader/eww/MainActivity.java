package nader.eww;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import nader.eww.adapter.MessageAdapter;
import nader.eww.databinding.ActivityMainBinding;
import nader.eww.model.Message;
import nader.eww.viewmodel.ChatViewModel;
import android.view.View;
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ChatViewModel viewModel;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        adapter = new MessageAdapter(message -> viewModel.markMessageAsSeen(message.id));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        viewModel.getMessages().observe(this, messages -> {
            adapter.submitList(messages);
        });

        viewModel.isUserCreated().observe(this, isCreated -> {
            binding.sendButton.setEnabled(isCreated);
            binding.messageInput.setEnabled(isCreated);
            if (!isCreated) {
                binding.createUserButton.setVisibility(View.VISIBLE);
            } else {
                binding.createUserButton.setVisibility(View.GONE);
            }
        });

        binding.createUserButton.setOnClickListener(v -> {
            viewModel.createUser();
        });

        binding.sendButton.setOnClickListener(v -> {
            String content = binding.messageInput.getText().toString().trim();
            if (!content.isEmpty()) {
                Message message = new Message();
                message.content = content;
                viewModel.sendMessage(message);
                binding.messageInput.setText("");
            }
        });

        binding.profileButton.setOnClickListener(v -> {
            // TODO: Open profile activity
            Toast.makeText(this, "Profile button clicked", Toast.LENGTH_SHORT).show();
        });

        viewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
}