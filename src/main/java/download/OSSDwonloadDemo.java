package download;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;

import java.io.*;

/**
 * @Author: ykbian
 * @Date: 2018/11/29 15:46
 * @Todo:
 */

public class OSSDwonloadDemo {


    // Endpoint以北京为例，其它Region请按实际情况填写。
    private String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    private String accessKeyId  ="你的accessKeyId";
    private String accessKeySecret = "你的accessKeySecret";
    // 这是你创建的存储桶的名称
    private String bucketName = "ykbian";

    /**
     *@Author:      ykbian
     *@date_time:   2018/11/29 15:47
     *@Description:  流式下载
     *@param:
    */
    public void streamDownload(){
        //这里可以认为是你要下载的文件名
        String objectName = "将进酒";
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(bucketName, objectName);

        try {
            // 读取文件内容。
            BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
            System.out.println("读取到的文件信息：\n" );
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                System.out.println("\n" + line);
            }
            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            reader.close();
        }catch (IOException e){
            System.out.println("IOException======>文件下载失败");
        }
        // 关闭OSSClient。
        ossClient.shutdown();
    }



    /**
     *@Author:      ykbian
     *@date_time:   2018/11/29 15:55
     *@Description:  下载文件到本地
     *@param:
    */
    public void localDown(){
        String objectName = "断点续传";
        /**
         *  这个地方不太理解，
         *  如果路径只写到文件夹的话，会出现拒绝访问的错误，只能先本地先创建一个同名文件，然后再操作？
         */
        String pathName = "E:\\test\\project\\断点续传.zip";
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(pathName));
        // 关闭OSSClient。
        ossClient.shutdown();
    }


    /**
     *@Author:      ykbian
     *@date_time:   2018/11/29 16:18
     *@Description:  范围下载（就是下载其中一段）
     *@param:
    */
    public void range(){

        String objectName = "简单文字追加";

        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, objectName);
        // 获取0~1000字节范围内的数据，包括0和1000，共1001个字节的数据。
        // 如果指定的范围无效（比如开始或结束位置的指定值为负数，或指定值大于文件大小），则下载整个文件。
        getObjectRequest.setRange(0, 10);


        try {
            // 范围下载。
            OSSObject ossObject = ossClient.getObject(getObjectRequest);
            // 读取数据。
            byte[] buf = new byte[1024];
            InputStream in = ossObject.getObjectContent();
            for (int n = 0; n != -1; ) {
                n = in.read(buf, 0, buf.length);
            }
            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            /**
             *   这里可以把 InputStream in写入本地文件
             */
            System.out.println("+++++++++++++"+in);
            in.close();
        }catch (IOException e){
            System.out.println("IOException====");
        }

        // 关闭OSSClient。
        ossClient.shutdown();
    }



    public static void main(String[] args) {
        OSSDwonloadDemo ossDwonloadDemo = new OSSDwonloadDemo();
        Long aLong = System.currentTimeMillis();
//        ossDwonloadDemo.streamDownload();
//        ossDwonloadDemo.localDown();
        ossDwonloadDemo.range();
        Long aLong1 = System.currentTimeMillis();
        System.out.println(" 下载完成,共用时："+(aLong1 - aLong)+"毫秒");
    }
}
