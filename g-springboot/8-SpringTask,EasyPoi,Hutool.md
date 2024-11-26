# SpringTask,EasyPoi,Hutool

## SpringTask

???

## EasyPoi

### 读取excel

1. 在 [pom.xml](material\springboot-knowledge\pom.xml) 中导入 EasyPoi 相关的依赖(也可以使用其基本版):
   ```xml
   <dependency>
       <groupId>cn.afterturn</groupId>
       <artifactId>easypoi-spring-boot-starter</artifactId>
       <version>4.5.0</version>
   </dependency>
   ```

2. 在项目内创建文件夹 [excel](material\springboot-knowledge\excel) 并创建一个excel文件样例: [待读取的excel文件.xlsx](material\springboot-knowledge\excel\待读取的excel文件.xlsx) ,在 dto 中创建映射其中数据的传输类  [ExcelDto.java](material\springboot-knowledge\src\main\java\springboot\dto\ExcelDto.java) :
   ```java
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public class ExcelDto {
       //员工ID,要使用 @Excel() 注解指明其对应的行数或表头名.其余字段同理,由于该注解有中文注释其用法不再赘述
       @Excel(name = "员工ID", orderNum = "1")
       private Long id;
       @Excel(name = "员工姓名", orderNum = "2")
       private String name;
       //员工性别,是否为男性.该字段为Boolean类型,需要添加 replace 属性使用特定的字符串声明映射规则
       @Excel(name = "员工性别", orderNum = "3", replace = {"男_true", "女_false"})
       private Boolean isMale;
       @Excel(name = "员工年龄", orderNum = "4")
       private Integer age;
       //词条创建日期.同理,要使用 exportFormat 属性声明使用何种格式读取和写出日期时间
       @Excel(name = "词条创建日期", orderNum = "5", width = 20, exportFormat = "yyyy年MM月dd日")
       private Date createDate;
       @Excel(name = "员工评价", orderNum = "6")
       private String evaluate;
   }
   ```

3. 创建测试类 [ExcelTest.java](material\springboot-knowledge\src\test\java\springboot\ExcelTest.java) ,创建并运行 readFromExcel() 的方法来读取相应的excel文件:
   ```java
   public class ExcelTest {
       @Test
       public void readFromExcel() {
           ImportParams params = new ImportParams(); // 创建导入参数对象
           params.setTitleRows(1); // 数据表中标题占几行
           params.setHeadRows(1); // 数据表中表头占几行
           params.setSheetName("统计"); // 要读取的sheet的名称
           List<ExcelDto> resultList = ExcelImportUtil.importExcel(new File("excel\\待读取的excel文件.xlsx"), ExcelDto.class, params);//使用事先创建好的dto类读取数据,获取数据类对象列表
           System.out.println("循环输出excel文件读取结果: ");
           resultList.forEach(System.out::println);
       }
   }
   ```

### 导出excel

在测试类 [ExcelTest.java](material\springboot-knowledge\src\test\java\springboot\ExcelTest.java) 中创建静态方法 getAllExcelDto() 模拟从数据库读取数据并返回dto列表的方法,然后创建并运行 writeToExcel() 方法将这些数据导出至指定的位置( [excel](material\springboot-knowledge\excel) 文件夹中):
```java
@Test
public void writeToExcel() {
//用ExportParams声明列表的标题和表头名,用ExcelExportUtil工具类创建待创建的excel文件对象,指明使用的dto类,最终返回Workbook对象.getAllExcelDto()返回一个dto数据列表,一般为从数据库查询得知.本句话被写在try()中,可确保方法执行完毕后关闭本句话所使用的流
    try (Workbook sheets = ExcelExportUtil.exportExcel(new ExportParams("员工信息统计表", "统计"), ExcelDto.class, getAllExcelDto())) {
        FileOutputStream outputStream = new FileOutputStream("excel\\用java创建并写入数据的excel文件.xlsx");
        System.out.println("正在创建excel文件并写入数据");
        sheets.write(outputStream); // 创建文件,将数据写入流中
        outputStream.close(); // 不要忘记关闭流
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
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
```

## Hutool

???
