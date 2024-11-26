package springboot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import springboot.dto.MessageDto;

import java.util.List;

/**
 * 自定义的全局异常处理视图层.
 * <p>{@code @ControllerAdvice} 注解的使用方式与 {@code @Controller} 注解大同小异,二者皆为组件但并非继承关系</p>
 * <p>与之不同的是,{@code @ControllerAdvice} 用来处理发生异常时应当返回前端的数据,而不是根据特定的网址决定返回数据</p>
 * <p>同理, 也可以使用@RestControllerAdvice 来自定义全局异常处理,其与 @RestController 注解大同小异</p>
 */
@ControllerAdvice
public class ExceptionController {

    /**
     * 处理参数校验异常.
     * <p>添加 {@code @ExceptionHandler("要处理的异常类.class") 来声明当发生该异常时对视图进行处理的方法}</p>
     * <p>与一般的 controller 同理,本方法的形参中可以指定HttpServletRequest,HttpServletResponse形参</p>
     * <p>这里的形参指定对应的异常类对象,以获取该类中相关的错误信息文本/p>
     *
     * @param exception 待处理的异常对象
     * @return 错误信息响应实体.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<MessageDto> exception(MethodArgumentNotValidException exception) {
        //response.sendError(400); //从本质上来说,以下代码就相当于这一句话
        List<ObjectError> errors = exception.getAllErrors(); //获取全部错误信息的List集合
        StringBuilder errorStrings = new StringBuilder();
        for (ObjectError error : errors) { //遍历
            //使用 getDefaultMessage() 方法能获取到这些字段上的注解中的自定义 message 错误文本消息
            errorStrings.append(error.getDefaultMessage()).append(" "); //把它们连起来,用空格分隔
        }
        // MessageDto 为自定义dto类,用来封装返回给前端的消息,若直接返回该类的对象则会被前端认为是"请求成功"(200),
        // 要声明返回错误消息,使用 ResponseEntity.badRequest() ,返回的错误代码为400
        return ResponseEntity.badRequest().body(new MessageDto(errorStrings.toString()));
    }
}