package com.pat.starter.common.util;

import com.pat.starter.common.bo.RequestBO;
import com.pat.starter.common.bo.ResponseBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import java.io.*;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

/**
 * createby chouway on 2020.07.24
 *
 * @Title: HttpUtil
 */
public class PatHttpUtil {

    private static Logger log = LoggerFactory.getLogger(com.pat.starter.common.util.PatHttpUtil.class);


    //通讯方式get
    public static final String METHOD_GET = "GET";
    //通讯方式post
    public static final String METHOD_POST = "POST";

    //通讯编码UTF-8
    public static final String CHAT_UTF8 = "UTF-8";


    public static Map<String, String> getJsonHearders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json;charset=utf-8");
        return headers;
    }

    public static Map<String, String> getXformHearders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }

    /**
     * get请求
     *
     * @param url
     * @param headers
     * @return
     * @throws Exception
     */
    public static String getResult(String url, Map<String, String> headers) throws RuntimeException {
        return request(url, null, CHAT_UTF8, METHOD_GET, headers);
    }


    /**
     * post请求
     *
     * @param url
     * @param headers
     * @return
     * @throws Exception
     */
    public static String postResult(String url, String params, Map<String, String> headers) throws RuntimeException {
        return request(url, params, CHAT_UTF8, METHOD_POST, headers);
    }

    private static String request(String urlStr, String requestMessage, String chartSet, String method, Map<String, String> headers) throws RuntimeException {
        RequestBO httpPubRequest = new RequestBO();
        httpPubRequest.setUrl(urlStr);
        httpPubRequest.setParams(requestMessage);
        httpPubRequest.setChartSet(chartSet);
        httpPubRequest.setMethod(method);
        httpPubRequest.setHeaders(headers);
        httpPubRequest.setFollowRedirects(true);
        ResponseBO response = com.pat.starter.common.util.PatHttpUtil.request(httpPubRequest);
        int responseCode = response.getResponseCode();
        if (responseCode < 200 || responseCode > 299) {//　200-299 用于表示请求成功。
            throw new RuntimeException("error resp code【" + response.getResponseCode() + "】,msg【" + response.getResponseMessage() + "】");
        }
        return response.getBody();
    }


    public static ResponseBO request(RequestBO requestBO) throws RuntimeException {

        if (requestBO == null) {
            throw new RuntimeException("请求为空");
        }
        String urlStr = requestBO.getUrl();
        String requestMessage = requestBO.getParams();
        String chartSet = requestBO.getChartSet();
        if (chartSet == null || chartSet.trim().length() == 0) {
            chartSet = "UTF-8";
        }
        String method = requestBO.getMethod();
        if (method == null) {
            method = METHOD_GET;
        }
        if (urlStr == null) {
            throw new RuntimeException("请求url为空");
        }
        Map<String, String> headers = requestBO.getHeaders();


        ResponseBO responseBO = new ResponseBO();
        String result = "";
        HttpURLConnection conn = null;
        OutputStream writer = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();

            conn.setInstanceFollowRedirects(requestBO.isFollowRedirects());
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
            conn.setUseCaches(false);

            //消息头
            if (headers == null) {
                conn.setRequestProperty("Content-Type", "text/xml");
//                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            } else {
                for (Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            conn.setDoInput(true);
            System.out.println("------------------向外发送" + "请求------------------");
            System.out.println("请求URL:" + url);
            System.out.println("请求METHOD:" + method);
            Map<String, List<String>> requestProperties = conn.getRequestProperties();
//			System.out.println("消息头："  + requestProperties);
            if (METHOD_POST.equals(method)) {
                conn.setRequestMethod(METHOD_POST);
                conn.setDoOutput(true);
                if (StringUtils.hasText(requestMessage)) {
                    System.out.println("请求报文体：" + requestMessage);
                    System.out.println("-------------------------------------------------------------");

                    writer = conn.getOutputStream();

                    writer.write(requestMessage.getBytes(chartSet));
                }
            } else {
                conn.setRequestMethod(method);
            }
            String responseStr = "", sCurrentLine = "";

            responseBO.setHeaders(conn.getHeaderFields());
            responseBO.setResponseCode(conn.getResponseCode());
            responseBO.setResponseMessage(conn.getResponseMessage());
            System.out.println("resp code==>" + conn.getResponseCode());
            System.out.println("resp msg==>" + conn.getResponseMessage());
            if (conn.getResponseCode() == 200) {

                inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, chartSet));

                while ((sCurrentLine = reader.readLine()) != null) {
                    responseStr = responseStr + sCurrentLine;
                }
            }

//          throw new Exception("error resp code【" + conn.getResponseCode() + "】,msg【" + conn.getResponseMessage() + "】");

            System.out.println("响应内容：" + responseStr);
            if (!"".equals(responseStr)) {
                result = responseStr;
            }
            responseBO.setBody(result);
        } catch (Exception e) {
            throw new RuntimeException("通讯失败", e);
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("关流失败", e);
            }
        }
        return responseBO;
    }


    public static Map<String, String> cookies2json(String cookies) {

        if (cookies == null) {
            return Collections.emptyMap();
        }
        Map<String, String> cookiesMap = new HashMap<String, String>();
        String[] splitA = cookies.split(";");
        for (String a : splitA) {
            String[] splitB = a.split("=");
            if (splitB.length == 2) {
                cookiesMap.put(splitB[0], splitB[1]);
            }
        }
        return cookiesMap;
    }


    public static void requestUrl(String loginUrl, String nextUrl) {
        HttpURLConnection connection = null;
        Reader read;
        BufferedReader bufferReader;
        try {
            CookieManager manager = new CookieManager();//关键的地方
            CookieHandler.setDefault(manager);
            URL url = new URL(loginUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            //打印请求头信息
            Map hfs = con.getHeaderFields();
            Set<String> keys = hfs.keySet();
            for (String str : keys) {
                List<String> vs = (List) hfs.get(str);
                System.out.print(str + ":");
                for (String v : vs) {
                    System.out.print(v + "\t");
                }
                System.out.println();
            }
            System.out.println("-----------------------");
            url = new URL(nextUrl);
            connection = (HttpURLConnection) url.openConnection();
            //connection.setConnectTimeout(30000);//可以去掉
            connection.setRequestMethod("GET");
            //connection.setReadTimeout(30000);
            InputStream in = connection.getInputStream();
            //获取读取的方式
            read = new InputStreamReader(connection.getInputStream());
            bufferReader = new BufferedReader(read);
            //获取服务器返回的字符串
            String str;//读取每一行数据
            StringBuffer buffer = new StringBuffer();//接受全部数据
            while ((str = bufferReader.readLine()) != null) {
                buffer.append(str + "\n");
            }
            //关闭连接
            read.close();
            connection.disconnect();

            //测试
            System.out.println("发出去的请求:" + nextUrl.toString());
            System.out.println("读取来的数据:" + buffer.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
