package com.hjq.demo.api;

import com.hjq.demo.common.response.Result;
import com.hjq.demo.domain.user.Info;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface FileApi {

    //样例 download file

//    @GET("u=107188706,3427188039&fm=27&gp=0.jpg")
//    Call<ResponseBody> downloadFile();

//    call.enqueue(new Callback<ResponseBody>() {
//        @Override
//        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//            boolean writtenToDisk = writeResponseBodyToDisk(response.body());
//
//            btnDownload.setText(writtenToDisk ? "success" : "false");
//        }
//
//        @Override
//        public void onFailure(Call<ResponseBody> call, Throwable t) {
//            Toast.makeText(TestUploadFileActivity.this, "fail", Toast.LENGTH_SHORT).show();
//        }
//    });

    //样例 upload file

//    @Multipart
//    @POST("upload")
//    Call<ResponseBody> upload(@Part("description") RequestBody description,
//                              @Part MultipartBody.Part file);

    // 创建 RequestBody，用于封装构建RequestBody
//    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//    // MultipartBody.Part  和后端约定好Key，这里的partName是用image
//    MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
//    // 添加描述
//    String descriptionString = "hello, 这是文件描述";
//    RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
//    // 执行请求
//    Call<ResponseBody> call = service.upload(description, body);
//    call.enqueue(new Callback<ResponseBody>() {
//        @Override
//        public void onResponse(Call<ResponseBody> call,
//                Response<ResponseBody> response) {
//            Log.v("Upload", "success");
//        }
//
//        @Override
//        public void onFailure(Call<ResponseBody> call, Throwable t) {
//            Log.e("Upload error:", t.getMessage());
//        }
//    });
//}

    //上传或修改文件
    @Multipart
    @POST("{uid}")
    Call<RequestBody> uploadFile(@Path("uid") String uid, @Part MultipartBody.Part file);

    //获取一个资源的文件
    //文件保存在 response
    @GET("{uid}")
    Call<RequestBody> downloadFile(@Path("uid") String uid);
}
