package upload;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.AppendObjectRequest;
import com.aliyun.oss.model.AppendObjectResult;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.UploadFileRequest;

import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * @Author: ykbian
 * @Date: 2018/11/29 10:53
 * @Todo:   测试一下阿里存储对象的上传操作
 */

public class OSSUploadDemo {

    // Endpoint以北京为例，其它Region请按实际情况填写。
    private String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    private String accessKeyId  ="你的accessKeyId";
    private String accessKeySecret = "你的";
    // 这是你创建的存储桶的名称
    private String bucketName = "ykbian";


    /**
     *@Author:      ykbian
     *@date_time:   2018/11/29 10:54
     *@Description:  简单的字符串上传
     *@param:
    */
    public void uploadString() {
    // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    // 上传字符串。
        String content = "人生得意须尽欢，莫使金樽空对月。\n" +
                "天生我材必有用，千金散尽还复来。\n" +
                "烹羊宰牛且为乐，会须一饮三百杯。\n" +
                "岑夫子，丹丘生，将进酒，杯莫停。";
        ossClient.putObject(bucketName, "将进酒", new ByteArrayInputStream(content.getBytes()));
    // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     *@Author:      ykbian
     *@date_time:   2018/11/29 10:54
     *@Description:  上传Byte数组
     *@param:
    */
    public void uploadArrary(){
    // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId,accessKeySecret);
    // 上传Byte数组。
        String s = "与君歌一曲，请君为我倾耳听。" +
                "钟鼓馔玉不足贵，但愿长醉不复醒。" +
                "古来圣贤皆寂寞，惟有饮者留其名。" +
                "陈王昔时宴平乐，斗酒十千恣欢谑。" +
                "主人何为言少钱，径须沽取对君酌。" +
                "五花马，千金裘，呼儿将出换美酒，与尔同销万古愁。";
        byte[] content = s.getBytes();
        ossClient.putObject(bucketName, "上传Byte数组", new ByteArrayInputStream(content));
    // 关闭OSSClient。
        ossClient.shutdown();
    }
    /**
     *@Author:      ykbian
     *@date_time:   2018/11/29 10:54
     *@Description: 图片（包括视频，都是文件上传）
     *@param:
    */
    public void uploadFile(){
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 上传文件。<yourLocalFile>由本地文件路径加文件名包括后缀组成，例如/users/local/myfile.txt。
        ossClient.putObject(bucketName, "图片", new File("E:\\test\\1541661265204.jpg"));
        ossClient.putObject(bucketName, "视频", new File("E:\\test\\xxx.mp4"));
        ossClient.putObject(bucketName, "压缩包", new File("E:\\test\\201707095.rar"));
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     *@Author:      ykbian
     *@date_time:   2018/11/29 10:55
     *@Description:  表单
     *@param:
    */
    public void uploadForm(){

    }

    /**
     *@Author:      ykbian
     *@date_time:   2018/11/29 10:55
     *@Description:   断点续传
     *@param:
    */
    public void uploadBreakpoint(){

        try {
            // 创建OSSClient实例。
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            ObjectMetadata meta = new ObjectMetadata();
            // 指定上传的内容类型。
            meta.setContentType("text/plain");

            // 通过UploadFileRequest设置多个参数。
            UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName,"断点续传");

            // 指定上传的本地文件。
            uploadFileRequest.setUploadFile("E:\\test\\断点续传.zip");
            // 指定上传并发线程数，默认为1。
            uploadFileRequest.setTaskNum(5);
            // 指定上传的分片大小，范围为100KB~5GB，默认为文件大小/10000。
            uploadFileRequest.setPartSize(1 * 1024 * 1024);
            // 开启断点续传，默认关闭。
            uploadFileRequest.setEnableCheckpoint(true);
            // 记录本地分片上传结果的文件。开启断点续传功能时需要设置此参数，上传过程中的进度信息会保存在该文件中，
            // 如果某一分片上传失败，再次上传时会根据文件中记录的点继续上传。上传完成后，该文件会被删除。默认与待上传的本地文件同目录，为uploadFile.ucp。
            uploadFileRequest.setCheckpointFile("E:\\test\\uploadFile.ucp");
            // 文件的元数据。
            uploadFileRequest.setObjectMetadata(meta);

            /**
             *  这里可以设置回调类型 ，我这里不需要回调信息
             */
        //        uploadFileRequest.setCallback("<yourCallbackEvent>");

            // 断点续传上传。
            ossClient.uploadFile(uploadFileRequest);

            // 关闭OSSClient。
            ossClient.shutdown();
        }catch (Throwable throwable){
            System.out.println("出现错误");
        }

    }


    /**
     *@Author:      ykbian
     *@date_time:   2018/11/29 10:55
     *@Description: 追加上传   很好理解，就是把文件2接在文件1的后面，使用的是同一个ObjectName。
     *              两种上传方式，这里文字追加是使用一次设置多个参数，图片（文件）上传使用单个设置参数
     *@param:
    */
    public void uploadAppend(){

        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ObjectMetadata meta = new ObjectMetadata();
        // 指定上传的内容类型。
        meta.setContentType("text/plain");
//        ==============================================简单文字追加开始=====================================
        String content1 = "归园田居 \n";
        String content2 = "陶渊明 \n";
        String content3 = "晋朝 \n";
        // 通过AppendObjectRequest设置多个参数。
        AppendObjectRequest appendObjectRequest = new AppendObjectRequest(bucketName, "简单文字追加", new ByteArrayInputStream(content1.getBytes()), meta);

//        ==============================================简单文字追加结束=====================================

//        ==============================================多文件追加开始=====================================

        /**
         *  不知道这个玩意为什么没有默认的无参构造方法
         *
         *  我放弃了  我想追加几张图片试试，但是代码执行完以后查看结果，其实只有第一张图片。
         */
//        AppendObjectRequest appendObjectRequest01 = new AppendObjectRequest();
        String path1 = "E:\\test\\project\\g1.jpg";
        String path2 = "E:\\test\\project\\g2.jpg";
        String path3 = "E:\\test\\project\\g3.jpg";
        String path4 = "E:\\test\\project\\g4.jpg";
        String path5 = "E:\\test\\project\\g5.jpg";
        String path6 = "E:\\test\\project\\g6.jpg";
        // 通过AppendObjectRequest设置单个参数。
        // 设置存储空间名称。
        appendObjectRequest.setBucketName(bucketName);
        // 设置文件名称。
        appendObjectRequest.setKey("文件追加上传");
        // 设置待追加的内容。有两种可选类型：InputStream类型和File类型。这里为InputStream类型。
//        appendObjectRequest.setInputStream(new ByteArrayInputStream(content1.getBytes()));
        // 设置待追加的内容。有两种可选类型：InputStream类型和File类型。这里为File类型。
        appendObjectRequest.setFile(new File(path1));
        // 指定文件的元信息，第一次追加时有效。
        appendObjectRequest.setMetadata(meta);
        //        ==============================================多文件追加结束=====================================

        // 第一次追加。
        // 设置文件的追加位置。
        appendObjectRequest.setPosition(0L);
        AppendObjectResult appendObjectResult = ossClient.appendObject(appendObjectRequest);
        // 文件的64位CRC值。此值根据ECMA-182标准计算得出。
        System.out.println(appendObjectResult.getObjectCRC());

        // 第二次追加。
        // nextPosition指明下一次请求中应当提供的Position，即文件当前的长度。
        appendObjectRequest.setPosition(appendObjectResult.getNextPosition());
        /**
         *   追加文字的话用第一个，追加文件用第二个
         */
//        appendObjectRequest.setInputStream(new ByteArrayInputStream(content2.getBytes()));
        appendObjectRequest.setFile(new File(path2));
        appendObjectResult = ossClient.appendObject(appendObjectRequest);
        // 第三次追加。
        appendObjectRequest.setPosition(appendObjectResult.getNextPosition());
        appendObjectRequest.setFile(new File(path3));
        appendObjectResult = ossClient.appendObject(appendObjectRequest);


        appendObjectRequest.setPosition(appendObjectResult.getNextPosition());
        appendObjectRequest.setFile(new File(path4));
        appendObjectResult = ossClient.appendObject(appendObjectRequest);

        appendObjectRequest.setPosition(appendObjectResult.getNextPosition());
        appendObjectRequest.setFile(new File(path5));
        appendObjectResult = ossClient.appendObject(appendObjectRequest);

        appendObjectRequest.setPosition(appendObjectResult.getNextPosition());
        appendObjectRequest.setFile(new File(path6));
        appendObjectResult = ossClient.appendObject(appendObjectRequest);


        // 关闭OSSClient。
        ossClient.shutdown();
    }
    /**
     *@Author:      ykbian
     *@date_time:   2018/11/29 10:56
     *@Description: 分片上传
     *@param:
    */
    public void uploadShard(){

    }




    public static void main(String[] args) {
        OSSUploadDemo ossUploadDemo = new OSSUploadDemo();
        Long aLong = System.currentTimeMillis();
//        ossUploadDemo.uploadString();
//        ossUploadDemo.uploadArrary();
//        ossUploadDemo.uploadFile();
//        ossUploadDemo.uploadAppend();
            ossUploadDemo.uploadBreakpoint();
        Long aLong1 = System.currentTimeMillis();
        System.out.println(" 上传完成,共用时："+(aLong1 - aLong)+"毫秒");
    }
}
