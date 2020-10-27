package com.hcan53.android.http.api;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * <p>描述：通用的的api接口</p>
 * <p>
 * 1.加入基础API，减少Api冗余<br>
 * 2.支持多种方式访问网络（get,put,post,delete），包含了常用的情况<br>
 * 3.传统的Retrofit用法，服务器每增加一个接口，就要对应一个api，非常繁琐<br>
 * </p>
 * <p>
 * 注意事项：<br>
 * 1.使用@url,而不是@Path注解,后者放到方法体上,会强制先urlencode,然后与baseurl拼接,请求无法成功<br>
 * 2.注意事项： map不能为null,否则该请求不会执行,但可以size为空.<br>
 * </p>
 */
public interface ApiService {

    @GET()
    Observable<String> get(@Url String url, @QueryMap Map<String, String> maps);

    @FormUrlEncoded
    @POST()
    Observable<String> postForm(@Url String url, @FieldMap Map<String, Object> maps);

    @POST()
    Observable<String> postBody(@Url String url, @Body RequestBody body);

    @Multipart
    @POST()
    Observable<String> uploadFlie(@Url String fileUrl, @Part("description") RequestBody description, @Part("files") MultipartBody.Part file);

    @Multipart
    @POST()
    Observable<String> uploadFiles(@Url String url, @PartMap() Map<String, RequestBody> maps);

    @Multipart
    @POST()
    Observable<String> uploadFiles(@Url String url, @Part() List<MultipartBody.Part> parts);

    @Headers("Accept-Encoding: identity")
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl, @QueryMap Map<String, String> maps);
}