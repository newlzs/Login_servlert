import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class avatarUpload extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // 上传文件的存储目录
    private static final String UPLOAD_DIRECTORY = "avatar";

    // 上传配置
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40;
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50;

    // 上传数据及保存文件
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 检测是否为多媒体上传
        PrintWriter out = response.getWriter();
        if(!ServletFileUpload.isMultipartContent(request)){
            // 如果不是则停止
            out.println("Error: 表单必须包含 enctype=multipart/form-data");
            out.flush();
            return;
        }
        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储与临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);
        // 设置最大请求值（包含文件和表单数据）
        upload.setFileSizeMax(MAX_REQUEST_SIZE);

        // 中文处理
        upload.setHeaderEncoding("utf-8");

        // 构造临时路径来存储上传的文件
        // 这个路径相对挡墙应用的目录
        String uploadPath = request.getServletContext().getRealPath("./") + File.separator + UPLOAD_DIRECTORY;

        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if(!uploadDir.exists())
            uploadDir.mkdir();
        try {
            // 解析请求的内容，提取文件数据
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);
            if(formItems != null && formItems.size() > 0){
                // 迭代表单数据
                for(FileItem item : formItems){
                    // 处理不在表单的字段
                    if(!item.isFormField()){
                        String fileName = new File(item.getName()).getName();

                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        // 在控制台输出文件上的路径
                        System.out.println(filePath);
                        // 保存文件到硬盘
                        item.write(storeFile);
                        // 写入文件名到数据库
                        System.out.println("Save successful");
                        out.println("Save successful");
                        out.flush();
                    }
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
