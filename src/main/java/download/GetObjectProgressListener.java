//package download;
//
//import com.aliyun.oss.OSSClient;
//import com.aliyun.oss.event.ProgressEventType;
//import com.aliyun.oss.model.GetObjectRequest;
//import sun.net.ProgressEvent;
//import sun.net.ProgressListener;
//
//import java.io.File;
//
///**
// * @Author: ykbian
// * @Date: 2018/11/29 16:33
// * @Todo:   带进度条下载  这个值api文档上赋值的   具体的实现还有一些问题，用的时候再说。
// */
//
//public  class GetObjectProgressListener implements ProgressListener {
//    private long bytesRead = 0;
//    private long totalBytes = -1;
//    private boolean succeed = false;
//
//    @Override
//    public void progressChanged(ProgressEvent progressEvent) {
//        long bytes = progressEvent.getBytes();
//        ProgressEventType eventType = progressEvent.getEventType();
//        switch (eventType) {
//            case TRANSFER_STARTED_EVENT:
//                System.out.println("Start to download......");
//                break;
//            case RESPONSE_CONTENT_LENGTH_EVENT:
//                this.totalBytes = bytes;
//                System.out.println(this.totalBytes + " bytes in total will be downloaded to a local file");
//                break;
//            case RESPONSE_BYTE_TRANSFER_EVENT:
//                this.bytesRead += bytes;
//                if (this.totalBytes != -1) {
//                    int percent = (int)(this.bytesRead * 100.0 / this.totalBytes);
//                    System.out.println(bytes + " bytes have been read at this time, download progress: " +
//                            percent + "%(" + this.bytesRead + "/" + this.totalBytes + ")");
//                } else {
//                    System.out.println(bytes + " bytes have been read at this time, download ratio: unknown" +
//                            "(" + this.bytesRead + "/...)");
//                }
//                break;
//            case TRANSFER_COMPLETED_EVENT:
//                this.succeed = true;
//                System.out.println("Succeed to download, " + this.bytesRead + " bytes have been transferred in total");
//                break;
//            case TRANSFER_FAILED_EVENT:
//                System.out.println("Failed to download, " + this.bytesRead + " bytes have been transferred");
//                break;
//            default:
//                break;
//        }
//    }
//    public boolean isSucceed() {
//        return succeed;
//    }
//
//    public static void main(String[] args) {
//        // Endpoint以杭州为例，其它Region请按实际情况填写。
//        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
//        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
//        String accessKeyId = "<yourAccessKeyId>";
//        String accessKeySecret = "<yourAccessKeySecret>";
//        String bucketName = "<yourBucketName>";
//        String objectName = "<yourObjectName>";
//        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
//        try {
//            // 带进度条的下载。
//            ossClient.getObject(new GetObjectRequest(bucketName, objectName).
//                            <GetObjectRequest>withProgressListener(new GetObjectProgressListener()),
//                    new File("<yourLocalFile>"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // 关闭OSSClient。
//        ossClient.shutdown();
//    }
//}