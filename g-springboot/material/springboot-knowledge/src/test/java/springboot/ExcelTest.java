package springboot;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import springboot.dto.ExcelDto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用 EasyPoi 对excel文件进行读写操作
 */
public class ExcelTest {
    /**
     * 读取excel文件中的数据.
     * <p>需要事先创建一个能映射结果的dto类</p>
     */
    @Test
    public void readFromExcel() {
        ImportParams params = new ImportParams(); // 创建导入参数对象
        params.setTitleRows(1); // 数据表中标题占几行
        params.setHeadRows(1); // 数据表中表头占几行
        params.setSheetName("统计"); // 要读取的sheet的名称
        //使用事先创建好的dto类读取数据,获取数据类对象列表
        List<ExcelDto> resultList = ExcelImportUtil.importExcel(new File("excel\\待读取的excel文件.xlsx"), ExcelDto.class, params);
        System.out.println("循环输出excel文件读取结果: ");
        resultList.forEach(System.out::println);
    }

    /**
     * 创建excel文件并写入数据.
     */
    @Test
    public void writeToExcel() {
        //使用ExportParams声明列表的标题和表头名,使用ExcelExportUtil工具类创建待创建的excel文件对象,要指明使用的dto类,最终返回一个Workbook对象.
        //getAllExcelDto()返回一个dto数据列表,一般为从数据库查询得知.本句话被写在try()中,可确保方法执行完毕后关闭本句话所使用的流
        try (Workbook sheets = ExcelExportUtil.exportExcel(new ExportParams("员工信息统计表", "统计"), ExcelDto.class, getAllExcelDto())) {
            FileOutputStream outputStream = new FileOutputStream("excel\\用java创建并写入数据的excel文件.xlsx");
            System.out.println("正在创建excel文件并写入数据");
            sheets.write(outputStream); // 创建文件,将数据写入流中
            outputStream.close(); // 不要忘记关闭流
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 向excel文件中追加数据.
     * <p>已知问题:使用本方法追加的数据格式与原文档不同</p>
     *
     * @throws IOException
     */
    @Test
    public void appendToExcel() throws IOException {
        FileInputStream inputStream = new FileInputStream("excel\\用java创建并写入数据的excel文件.xlsx");
        Workbook sheets = new XSSFWorkbook(inputStream); //获取原文件输入流,返回一个Workbook对象
        Sheet sheet = sheets.getSheetAt(0); // 获取第一个sheet,也可以用其sheet名获取
        //获取最后一行的行号.AtomicInteger 为java提供的进行整数原子操作的类,操作时即使不加锁也能保证线程安全,适用于数值频繁变动的场景
        AtomicInteger lastRowNum = new AtomicInteger(sheet.getLastRowNum());
        System.out.println("正在向excel文件中追加数据");
        getAllExcelDto().forEach(excelDto -> {
            //遍历数组,将数组以单元格为单位插入到excel中.incrementAndGet()方法会使行号自增1并返回
            Row newRow = sheet.createRow(lastRowNum.incrementAndGet());
            newRow.createCell(0).setCellValue(excelDto.getId());
            newRow.createCell(1).setCellValue(excelDto.getName());
            newRow.createCell(2).setCellValue(excelDto.getIsMale());
            newRow.createCell(3).setCellValue(excelDto.getAge());
            newRow.createCell(4).setCellValue(excelDto.getCreateDate());
            newRow.createCell(5).setCellValue(excelDto.getEvaluate());
        });
        inputStream.close();
        FileOutputStream outputStream = new FileOutputStream("excel\\用java创建并写入数据的excel文件.xlsx");
        sheets.write(outputStream); // 这次获取的是文件输出流并(覆盖)写入数据
        outputStream.close();
        sheets.close();
    }

    /**
     * 模拟从数据库中获取到的dto数据,这里直接进行创建.
     *
     * @return dto数据列表
     */
    private static ArrayList<ExcelDto> getAllExcelDto() {
        ExcelDto excelUser1 = new ExcelDto(1L, "张三", true, 18, new Date(), "风行天下,大运摩托车!");
        ExcelDto excelUser2 = new ExcelDto(2L, "李四", false, 19, new Date(), "呜哇哈哈哈我的推理华丽");
        ExcelDto excelUser3 = new ExcelDto(3L, "王五", true, 20, new Date(), null);
        ExcelDto excelUser4 = new ExcelDto(4L, "赵六", false, 21, new Date(), null);
        ArrayList<ExcelDto> excelUserList = new ArrayList<>();
        excelUserList.add(excelUser1);
        excelUserList.add(excelUser2);
        excelUserList.add(excelUser3);
        excelUserList.add(excelUser4);
        return excelUserList;
    }
}
