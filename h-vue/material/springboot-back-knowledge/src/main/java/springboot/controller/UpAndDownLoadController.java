package springboot.controller;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springboot.dto.MessageDto;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * 用来测试(批量)上传或下载文件的测试类.
 * <p>如果想要实现批量下载,需要事先通过某些方法将所有待下载的文件压缩,这里不再演示</p>
 */
@RestController
@RequestMapping("storage")
public class UpAndDownLoadController {
    /**
     * 上传文件的路径.
     * <p>{@code System.getProperty("user.dir")} 方法为固定写法,获取项目根目录文件夹的路径</p>
     * <p>这里的文件操作默认在项目中的 storage 文件夹下,如果想要自定义绝对路径可以事先在 application.yml 文件中声明
     * ,然后使用 {@code @Autowired} 注解注入</p>
     * <p>另一种写法是在 application.yml 文件中配置 spring:servlet:multipart:location 默认路径地址,如若此做,
     * 本项目中所有相对路径的根目录都是该地址</p>
     */
    String uploadDirPath = System.getProperty("user.dir") + "\\storage\\upload";
    /**
     * 下载文件的路径.
     * <p>同上,不过在项目启动前就已经创建好了</p>
     */
    String downloadDirPath = System.getProperty("user.dir") + "\\storage\\download";
//    String uploadDirPath = "storage\\upload"; //如果你配置了项目根路径地址,则可以使用此相对路径
//    String downloadDirPath = "storage\\download"; //同上

    /**
     * 单文件上传的操作方法.
     * <p>关键在于 {@code 待上传的文件.transferTo(上传到的文件)} 方法,此方法是将请求体内的数据存入本地文件的核心方法,会抛出 IOException ,该异常可以自行处理</p>
     * <p>前端使用 post 方法将文件二进制数据封装于请求体中,使用 form-data 格式(请求头声明 Content-Type: multipart/form-data )
     * ,当使用postman进行端口测试时需要如此声明</p>
     * <p>关于 {@code @RequestParam()} 形参注解参数:其value值 "file" 为通用写法,大多数表单默认请求体文件传输对象的键值就是这个,
     * 所以不建议使用其他键值.当用户传入不含有 "file" 键值的请求体时,服务器可能无法处理该错误请求(会抛出500错误),这里将其 require
     * 属性设置为 false ,我们自行处理当该请求数据为 null 时候的情况(即 {@code MultipartFile} 对象为空)</p>
     * <p>注意:如果上传的文件与已存在的文件文件名相同,则新文件会覆盖原有的旧文件</p>
     * <p>本方法为单文件上传的测试方法,如果前端使用一次请求发送多个文件则只会识别第一个文件</p>
     *
     * @param fileFromUser 从前端发送过来的文件,可能为null
     * @return 根据处理结果返回给前端的响应体
     * @throws IOException 该异常仅由 transferTo() 方法抛出,最常见的是文件不存在异常
     */
    @PostMapping("/upload")
    public ResponseEntity<MessageDto> getUploadFile(@RequestParam(value = "file", required = false) MultipartFile fileFromUser) throws IOException {
        File uploadDir = new File(uploadDirPath); //存放上传文件的文件夹的位置
        if (!uploadDir.exists() && uploadDir.mkdirs()) System.out.println("已创建接收上传文件的仓库"); //若该文件夹不存在则创建
        //若用户请求体中无 file 键值则 fileFromUser 对象为空,若请求体有 file 键值但键值对象为空则 isEmpty() 为 true
        if (fileFromUser == null || fileFromUser.isEmpty()) //若出现以上情况则向服务器返回400异常
            return ResponseEntity.badRequest().body(new MessageDto("错误", "未选择上传文件"));
        //自定义的文件存储位置.通常情况下会根据用户的uuid生成其对应的存储文件夹,此处省略了这个过程
        File storageFile = new File(uploadDirPath + "\\" + fileFromUser.getOriginalFilename());
        fileFromUser.transferTo(storageFile); //核心方法,用于数据的转换存储
        return ResponseEntity.ok(new MessageDto("成功", "文件上传成功"));
    }

    /**
     * 多文件上传的操作方法.
     * <p>这里的形参类型为 MultipartFile[] 数组,在方法中依次遍历该数组对象中的文件数据并存储在对应位置</p>
     * <p>上一个方法相当于存储数组中的第一个文件,代码本方法的代码大部分都与之相同</p>
     *
     * @param filesFromUser 从前端发送过来的文件数组
     * @return 根据处理结果返回给前端的响应体
     * @throws IOException 该异常仅由 transferTo() 方法抛出,最常见的是文件不存在异常
     */
    @PostMapping("/multi_upload")
    public ResponseEntity<MessageDto> getMultiUploadFiles(@RequestParam(value = "file", required = false) MultipartFile[] filesFromUser) throws IOException {
        File uploadDir = new File(uploadDirPath);
        if (!uploadDir.exists() && uploadDir.mkdirs()) System.out.println("已创建接收上传文件的仓库");
        if (filesFromUser == null || filesFromUser[0].isEmpty()) //如果数组中第一个文件数据为空则认为用户未传入文件
            return ResponseEntity.badRequest().body(new MessageDto("错误", "未选择上传文件"));
        for (MultipartFile fileFromUser : filesFromUser) { //遍历文件数据对象
            File storageFile = new File(uploadDirPath + "\\" + fileFromUser.getOriginalFilename());
            fileFromUser.transferTo(storageFile);
        }
        return ResponseEntity.ok(new MessageDto("成功", "文件上传成功"));
    }

    /**
     * 这里演示从服务器下载一个文件的方法,url使用rest风格.
     * <p>本方法将输入流中的数据循环写入到响应输出流中,通过响应输出流输出到前端.本方法不能有返回值,且需要解决跨域返回问题</p>
     * <p>在 {@code @GetMapping("/download/{file}")} 中,{file} 会被 {@code @PathVariable("file")} 解析为List字符串,
     * {file} 在前端具体字符串表现形式为 "路径1,路径2,文件名" 类似的样式,这样写是为了将文件路径和网址路径分开以免造成路径读取混乱,
     * 在后端要将该List中的字符串拼接为完整文件路径</p>
     * <p>前端接收的文件类型一般要设置为 Blob (好像没必要)</p>
     * <p>注意:前端接收响应体发送的文件时,若文件名中有中文,则需要将中文进行编码,否则会出现乱码问题</p>
     *
     * @param fileToUser 前端用户请求下载的文件路径,已被切分
     * @param response   根据结果返回给前端的响应体
     * @throws IOException 由于处理该异常的代码量非常大,不建议处理
     */
    @GetMapping("/download/{file}")
    public void pushDownloadFile(@PathVariable("file") List<String> fileToUser, HttpServletResponse response) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String pathName : fileToUser) sb.append("\\").append(pathName);
        File downloadFile = new File(downloadDirPath + sb); //这些方法将文件路径拼接为完整路径
        if (downloadFile.isFile()) {//文件存在且不是文件夹时,本方法处理不了文件夹
            response.reset(); // 清空response
            response.setContentType("application/octet-stream;charset=utf-8"); //octet-stream: 二进制流数据（如常见的文件下载）
            //Content-Disposition:告知浏览器以何种方式显示响应返回的文件,用浏览器打开还是以附件的形式下载到本地保存
            //"attachment" 表示以附件方式下载 "inline" 表示在线打开
            //filename表示文件的默认名称,因为网络传输只支持URL编码的相关支付,因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(downloadFile.getName(), StandardCharsets.UTF_8));
            //用于定义在跨域访问中哪些源(域名、协议和端口)被允许访问目标资源,若不声明则浏览器会实施同源策略,阻止本请求
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "GET, POST");//允许跨域访问的方式
            //将文件读入输入流中
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(downloadFile));
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = bis.read(buff)) > 0) {//从输入流中读取一定数量的字节,并将其存储在缓冲区字节数组中,读到末尾返回-1
                bos.write(buff, 0, len);
            }
            bos.close();//不要忘记关闭流
            bis.close();
        } else {
            response.sendError(404, "请求下载的文件不存在");
        }
    }

    /**
     * 从服务器下载文件的另一个方法.
     * <p>本方法使用 {@code ResponseEntity} 对象将文件以流的形式一次性读取到内存,通过响应输出流输出到前端</p>
     *
     * @param fileToUser 前端用户请求下载的文件路径,已被切分
     * @return 根据处理结果返回给前端的响应体
     * @throws IOException 由于处理该异常的代码量非常大,不建议处理
     */
    @GetMapping("/download2/{file}")
    public ResponseEntity<InputStreamResource> pushDownloadFile2(@PathVariable("file") List<String> fileToUser) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String pathName : fileToUser) sb.append("\\").append(pathName);
        File downloadFile = new File(downloadDirPath + sb);
        if (downloadFile.isFile()) {
            InputStreamResource isr = new InputStreamResource(new FileInputStream(downloadFile));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-disposition", "attachment; filename=" + URLEncoder.encode(downloadFile.getName(), StandardCharsets.UTF_8))
                    .body(isr);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 同上,本方法响应 post 请求,使用 ByteArrayResource 代替了 InputStreamResource.
     *
     * @param fileToUser 前端用户请求下载的文件路径,已被切分
     * @return 根据处理结果返回给前端的响应体
     * @throws IOException 由于处理该异常的代码量非常大,不建议处理
     */
    @PostMapping("/download3/{file}")
    public ResponseEntity<ByteArrayResource> pushDownloadFile3(@PathVariable("file") List<String> fileToUser) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String pathName : fileToUser) sb.append("\\").append(pathName);
        File downloadFile = new File(downloadDirPath + sb);
        if (downloadFile.isFile()) {
            byte[] bytes = Files.readAllBytes(downloadFile.toPath());
            ByteArrayResource bar = new ByteArrayResource(bytes);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-disposition", "attachment; filename=" + URLEncoder.encode(downloadFile.getName(), StandardCharsets.UTF_8))
                    .body(bar);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
