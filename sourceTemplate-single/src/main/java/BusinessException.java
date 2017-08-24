package ${groupId}.common.exception;


/**
 * 异常父类，所有业务异常继承该类
 *
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 6511369321028049305L;
    protected Integer          code;
    protected String          desc;
    
    public BusinessException(String desc) {
        this.desc = desc;
    }

    public BusinessException(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public BusinessException(ExceptionEnum exceptionEnum) {
        if(exceptionEnum!=null){
            this.code = exceptionEnum.code;
            this.desc = exceptionEnum.desc;
        }
    }
    

}
