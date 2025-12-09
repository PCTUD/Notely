package org.androidstudio.notely.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.androidstudio.notely.R
import org.androidstudio.notely.data.repository.RandomEmojiProvider
import org.androidstudio.notely.ui.adapter.UserAdapter
import org.androidstudio.notely.ui.viewmodel.UserViewModel
import org.androidstudio.notely.ui.viewmodel.UserViewModelFactory

class AccountActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel
    private lateinit var adapter: UserAdapter
    private var emoji = RandomEmojiProvider.randomEmoji()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        viewModel = ViewModelProvider(
            this,
            UserViewModelFactory.fromContext(this)
        )[UserViewModel::class.java]

        val recycler = findViewById<RecyclerView>(R.id.usersRecyclerView)
        recycler.layoutManager = LinearLayoutManager(this)

        adapter = UserAdapter(
            onSelect = { user ->
                viewModel.setActiveUser(user.id, user.name)
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            },
            onDelete = { user ->
                viewModel.deleteUser(user)
            }
        )
        recycler.adapter = adapter

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val emojiText = findViewById<TextView>(R.id.emojiText)
        emojiText.text = "Emoji: $emoji"

        findViewById<Button>(R.id.shuffleEmojiButton).setOnClickListener {
            emoji = RandomEmojiProvider.randomEmoji()
            emojiText.text = "Emoji: $emoji"
        }

        findViewById<Button>(R.id.createAccountButton).setOnClickListener {
            val name = nameInput.text.toString().trim()
            if (name.isNotBlank()) {
                viewModel.addUser(name, emoji)
            }
        }

        lifecycleScope.launch {
            viewModel.loadActiveUser()
        }

        lifecycleScope.launch {
            viewModel.getAllUsers().collect {
                adapter.submitList(it, viewModel.currentUserId.value)
            }
        }
    }
}
