package ${groupId}.common.exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import ${groupId}.domain.Result;

import com.alibaba.fastjson.JSON;
@Component
public class GloableExceptionHandler  extends SimpleMappingExceptionResolver{
	public static final String X_REQUESTED_WIDTH="X-Requested-With";
	public static final String XML_HTTP_REQUEST="XMLHttpRequest";
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception){
		
		String xr=request.getHeader(X_REQUESTED_WIDTH);
	     boolean isAjax = xr!=null && XML_HTTP_REQUEST.equalsIgnoreCase(xr);
	     
	     if(isAjax) {
            Result result = new Result();
           if(exception instanceof BusinessException) {
        	   BusinessException be= (BusinessException)exception;
        	   result.setCode(be.code);
        	   result.setDesc(be.desc);
            }else{ 
            	result.setCode(ExceptionEnum.SYSTEM_ERROR.code);
	        	result.setDesc(ExceptionEnum.SYSTEM_ERROR.desc);
            }
           PrintWriter pw = null;
            try {
                pw = response.getWriter();
                pw.write(JSON.toJSONString(result));
                pw.flush();
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (pw != null)
                    pw.close();
            }
           return null;     
        }else{
          /*如果不是Ajax的重从定向到错误界面*/
           return new ModelAndView("error/500");
        }
   }
}
