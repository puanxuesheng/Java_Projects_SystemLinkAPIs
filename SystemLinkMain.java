/**
 * Go to :https://www.systemlinkcloud.com/data
 * User: asean.support@ni.com
 * pass: ASEAN
 *
 * Go to demo.systemlink.io
 * User:lvuser
 * Pass:Demo1234!
 *
 * Need to use VPN as server is running in NI network - Shanghai's server
 * Go to http://systemlink-ni-sha:9090/
 * user: admin
 * pass: welcome
 *
 * Read System data using GET. Data queried is displayed on console
 *https://api.systemlinkcloud.com/?urls.primaryName=Tag%20Service
 *http://demo.systemlink.io/niapis/
 *
 *
 * Author: Xue Sheng
 */


import com.fasterxml.jackson.jr.ob.JSON;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SystemLinkMain {

/*Test Data  for systemlink Cloud*/
public static final String cloudTag1 = "https://api.systemlinkcloud.com/nitag/v2/tags-with-values/Energy.Systems.RotorSpeed";
public static final String cloudTag2 = "https://api.systemlinkcloud.com/nitag/v2/tags-with-values/Energy.Systems.Car.Speed";
public static final String cloudTag3 = "https://api.systemlinkcloud.com/nitag/v2/tags-with-values/Health_index";
public static final String cloudTag4 = "https://api.systemlinkcloud.com/nitag/v2/tags-with-values/Demo_FanRPM";
public static final String cloudTag5 = "https://api.systemlinkcloud.com/nitag/v2/tags-with-values/date";

/*Test data for Systemlink Demo (NIC)*/
public static final String demoTag1 = "http://demo.systemlink.io/niapm/v1/asset-summary";


/*Test data for Systemlink Demo in Shanghai*/
public static final String demoTag2 = "http://demo.systemlink.io/nitestmonitor/v1/results";

    //Query and return the number of active not active assets
    public static void QuerySystemLinkDemo () throws IOException{

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(demoTag2)
                .method("GET", null)
                .addHeader("Authorization", "Basic bHZ1c2VyOkRlbW8xMjM0IQ==")
                .build();
        Response response = client.newCall(request).execute();
        /*Retrieve data from the query and put it into a String*/
        String dataQueryFromSystemLink = response.body().string();
        System.out.println(dataQueryFromSystemLink);
    }

    //Query a tag data from systemlink >> parse it as a json object >> return only required data
    public static void QuerySystemLinkCloud () throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(cloudTag2)
                .addHeader("x-ni-api-key","ZewdnjUqyayqkWGZepPpZ06_s9cChX4zWuvGbpbLW3")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();

        /*Retrieve data from the query and put it into a String*/
        String dataQueryFromSystemLink = response.body().string();

        /*Parse data as a Json object*/
        TagsWithValues bean = JSON.std.beanFrom(TagsWithValues.class, dataQueryFromSystemLink);

        System.out.println();
        System.out.println(dataQueryFromSystemLink);
        System.out.println();
        System.out.println(bean.getCurrent().getValue().getType());
        System.out.println(bean.getCurrent().getValue().getValue());
    }

    //shanghai server
    public static void uploadDatatoSystemLink () throws IOException{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "{\r\n  \"results\": [\r\n    {\r\n      \"programName\": \"Xue Sheng\",\r\n      \"status\": {\r\n        \"statusType\": \"PASSED\",\r\n        \"statusName\": \"Passed\"\r\n      },\r\n      \"systemId\": \"my-system\",\r\n      \"hostName\": \"My-Host\",\r\n      \"properties\": {\r\n        \"key1\": \"value1\"\r\n      },\r\n      \"keywords\": [\r\n        \"keyword1\",\r\n        \"keyword2\"\r\n      ],\r\n      \"serialNumber\": \"123-456\",\r\n      \"operator\": \"xuesheng\",\r\n      \"product\": \"my random hardware\",\r\n      \"fileIds\": [\r\n        \"5e30934193cac8046851acb2\"\r\n      ],\r\n      \"startedAt\": \"2018-05-07T18:58:05.219692Z\",\r\n      \"totalTimeInSeconds\": 29.9\r\n    }\r\n  ]\r\n}");
        Request request = new Request.Builder()
                .url("http://systemlink-ni-sha:9090/nitestmonitor/v1/results")
                .method("POST", body)
                .addHeader("Authorization", "Basic YWRtaW46d2VsY29tZQ==")
                .addHeader("Content-Type", "text/plain")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
        //the id is the id use to delate data from the server.
    }

    public static void main(String[] args) throws IOException {
//        QuerySystemLinkCloud();
//        QuerySystemLinkDemo();
        uploadDatatoSystemLink();

    }
}
