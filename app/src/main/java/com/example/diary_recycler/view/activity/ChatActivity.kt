package com.example.diary_recycler.view.activity

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diary_recycler.SdfFormateChange
import com.example.diary_recycler.ServerInterface
import com.example.diary_recycler.adapter.ChatAdapter
import com.example.diary_recycler.dataClass.ChatListResponse
import com.example.diary_recycler.dataClass.ChatModel
import com.example.diary_recycler.databinding.ActivityChatBinding
import com.example.diary_recycler.view.RetrofitClient
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity(){
    lateinit var chatAdapter: ChatAdapter
    internal lateinit var preferences: SharedPreferences
    private var hasConnection: Boolean = false
    private var thread2: Thread? = null
    private var startTyping = false
    private var time = 2
    private lateinit var mSocket: Socket

    private val binding: ActivityChatBinding by lazy {
        ActivityChatBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        preferences = getSharedPreferences("USERSIGN", Context.MODE_PRIVATE)
        Log.e("this is ChatActivity", "testing preferences roomName" + preferences.getString("roomName", "")+"이다")

        connectAdapter()

        if (savedInstanceState != null) {
            hasConnection = savedInstanceState.getBoolean("hasConnection")//화면 회전 등 화면 정도 담음
        }
        if (hasConnection) {
        } else {
            socketConnection()//소켓연결
        }
        hasConnection = true
        //뭔가 설정..?
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.navigationIcon?.apply {
            setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN)
        }

        //전송버튼
        binding.messageActivityImageView.setOnClickListener{
            sendMessage()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    internal var onNewMessage: Emitter.Listener = Emitter.Listener { args ->
        runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val name: String
            val script: String
            val profile_image: String
            val date_time: String
            try {
                Log.e("onNewMessage, try", data.toString())
                name = data.getString("name")
                script = data.getString("script")
                profile_image = data.getString("profile_image")
                date_time = data.getString("date_time")

                val format = ChatModel(name, script, profile_image, date_time)
                chatAdapter.addItem(format)//어댑터에 추가
                chatAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                return@Runnable
            }
        })
    }

    internal var onNewUser: Emitter.Listener = Emitter.Listener { args ->
        runOnUiThread(Runnable {
            val length = args.size

            if (length == 0) {
                return@Runnable
            }
            var username = args[0].toString()
            try {
                val `object` = JSONObject(username)
                username = `object`.getString("username")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        })
    }


    fun sendMessage() {//어댑터에 메시지 전송
        preferences = getSharedPreferences("USERSIGN", Context.MODE_PRIVATE)
        val sdf = SimpleDateFormat("hh:mm")
        val getTime = SdfFormateChange.makeSdfTime(sdf)

        val message = binding.messageActivityEditText.text.toString().trim({ it <= ' ' })//입력한 메시지 가져오기
        if (TextUtils.isEmpty(message)) {
            return
        }
        binding.messageActivityEditText.setText("")//메시지 입력창 비우기
        val jsonObject = JSONObject()
        try {
            jsonObject.put("name", preferences.getString("name", ""))
            jsonObject.put("script", message)
            jsonObject.put("profile_image", "example")
            jsonObject.put("date_time", getTime)
            jsonObject.put("roomName", preferences.getString("roomName", ""))//룸이름
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        mSocket.emit("chat message", jsonObject)
        //chatAdapter.notifyDataSetChanged()
    }

    fun socketConnection(){
        try {
            mSocket = IO.socket("http://ec2-44-201-147-197.compute-1.amazonaws.com:5000/")//커넥션이 성공해야 한다고!!!!!!
            //연결 왜 안되는데
        } catch(e: URISyntaxException) {
            e.printStackTrace()
        }//소켓 연결됨?
        mSocket.connect()//커넥션 성공했다!!! 와이파이 켰어야지 바보야,, 그리고 socket uninstall 하고 npm install socket.io@1로 설치함

        mSocket.on("connect user", onNewUser)
        mSocket.on("chat message", onNewMessage)//socket.on은 수신

        val userId = JSONObject()
        try {
            userId.put("username", preferences.getString("name", "") + " Connected")
            userId.put("roomName", preferences.getString("roomName", ""))
            Log.e("username",preferences.getString("name", "") + " Connected" + preferences.getString("roomName", ""))

            mSocket.emit("connect user", userId)//이거 하면 서버에 반응 떠야함/socket.emit은 메세지 전송임
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun connectAdapter(){
        //어댑터 연결
        chatAdapter = ChatAdapter(this)//this?
        binding.messageActivityRecyclerview.adapter = chatAdapter
        binding.messageActivityRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
        }
        binding.messageActivityRecyclerview.setHasFixedSize(true)
    }

}