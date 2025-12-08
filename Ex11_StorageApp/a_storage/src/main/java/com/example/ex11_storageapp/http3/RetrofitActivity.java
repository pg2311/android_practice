package com.example.ex11_storageapp.http3;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ex11_storageapp.ApplicationClass;
import com.example.ex11_storageapp.databinding.ActivityRetrofitBinding;
import com.example.ex11_storageapp.sqlite.DBHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitActivity extends AppCompatActivity {

    private static final String TAG = "RetrofitActivity";

    private ActivityRetrofitBinding binding;
    private DBHelper dbHelper;

    LocalApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRetrofitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        dbHelper = new DBHelper(this, "mydb.db", null, 1);
//        dbHelper.open();
        apiService = ApplicationClass.localRetrofit.create(LocalApiService.class);

//        initEvent();
//        refresh();

//        testProducts();
        testLogin();
    }
    private void testLogin(){
        Log.i(TAG,"testLogin");
        String id = "id1";
        String pwd = "p1";
//        pwd = "XXX";
        apiService.login(id, pwd).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i(TAG, "call=" + call);
                Log.i(TAG, "response=" + response);
                if(response.isSuccessful()){
                    Log.i(TAG, "응답내용=" + response.body());
                    binding.tvResult.setText("로그인성공");
                }else if(response.code() == 500){
                    binding.tvResult.setText("로그인실패");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }
    private void testProducts(){
        apiService.getProducts().enqueue(new Callback<List<ProductDTO>>() {
            @Override
            public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                if(response.isSuccessful()){
                    List<ProductDTO> list = response.body();
                    StringBuilder result = new StringBuilder();
                    list.forEach( e ->{
                        result.append(e.getProdNo() + ", " + e.getProdName() + ", " + e.getProdPrice());
                        result.append("\n");
                    });
                    // 비동기로 호출되므로 여기서 호출해야 한다.
                    binding.tvResult.setText(result.toString());
                }
            }
            /**
             * 네트워크 요청 자체가 성공적으로 완료되지 못했을 때 호출됩니다
             * @param call
             * @param t
             */
            @Override
            public void onFailure(Call<List<ProductDTO>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                // 네트워크 연결 문제 발생 시 요청 재시도
                // call.clone().enqueue(this);
            }
        });
    }
/*
    private void refresh() {
//        String result = dbHelper.selectAll();

        //enqueue의 결과로 호출되는 callback은 UI THread에서 동작한다.
        apiService.getAllMessage().enqueue(new Callback<List<MessageDto>>() {
            @Override
            public void onResponse(Call<List<MessageDto>> call, Response<List<MessageDto>> response) {
                if(response.isSuccessful()){
                    List<MessageDto> list = response.body();
                    StringBuilder result = new StringBuilder();
                    list.forEach(e -> {
                        result.append(e.getId() + ", " + e.getMessage() + "\n");
                    });
                    // 비동기로 호출되므로 여기서 호출해야 한다.
                    binding.tvResult.setText(result.toString());

                }
            }

            @Override
            public void onFailure(Call<List<MessageDto>> call, Throwable t) {

            }
        });
    }

    private void initEvent() {

        binding.btnSelectAll.setOnClickListener(v -> {
            refresh();
        });

        binding.btnInsert.setOnClickListener(view -> {
            MessageDto dto = new MessageDto();
            dto.setMessage(binding.etMessage.getText()+"");

            apiService.postMessage(dto).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        String result = response.body();
                        if("success".equals(result)){
                            refresh();
                            Toast.makeText(RetrofitActivity.this, "추가되었습니다.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });


        });

        binding.btnSelect.setOnClickListener(view -> {
            int id = -1;
            try{
                id = Integer.parseInt(binding.etId.getText().toString());
            }catch(Exception e){
                Toast.makeText(this, "숫자를 입력하세요", Toast.LENGTH_SHORT).show();
                return;
            }

            apiService.getMessage(id).enqueue(new Callback<MessageDto>() {
                @Override
                public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                    if(response.isSuccessful()){
                        MessageDto result = response.body();
                        String text = "";
                        if(result != null){
                            text = result.getId() + ", " + result.getMessage();
                            binding.tvResult.setText(text);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MessageDto> call, Throwable t) {

                }
            });
        });

        binding.btnUpdate.setOnClickListener(view -> {
            MessageDto dto = new MessageDto();
            try{
                dto.setId(Integer.parseInt(binding.etId.getText().toString()));
            }catch(Exception e){
                Toast.makeText(this, "숫자를 입력하세요", Toast.LENGTH_SHORT).show();
                return;
            }
            dto.setMessage(binding.etMessage.getText()+"");

            apiService.putMessage(dto).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        String result = response.body();
                        if("success".equals(result)){
                            refresh();
                            Toast.makeText(RetrofitActivity.this, "수정되었습니다.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });


        });

        binding.btnDelete.setOnClickListener(view -> {
            int id = -1;
            try{
                id = Integer.parseInt(binding.etId.getText().toString());
            }catch(Exception e){
                Toast.makeText(this, "숫자를 입력하세요", Toast.LENGTH_SHORT).show();
                return;
            }

            apiService.deleteMessage(id).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        String result = response.body();
                        if("success".equals(result)){
                            refresh();
                            Toast.makeText(RetrofitActivity.this, "삭제되었습니다.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });


        });
    }
*/

}