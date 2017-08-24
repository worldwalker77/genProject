package ${groupId}.common.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static final String defaultPattern = "yyyy-MM-dd HH:mm:ss";

	public static final String zhCNPattern = "yyyy年MM月dd日 HH点mm分ss秒";

	public static final String zhCNPatternDate = "yyyy年MM月dd日";
	/**
	 * 格式化时间
	 * @Title: format    
	 * @param date
	 * @param pattern
	 * @return   
	 * @return String   
	 * @throws
	 */
	public static String format(Date date, String pattern){
		DateFormat df = new SimpleDateFormat();
		return df.format(date);
	}
	
	public static String format(Timestamp timestamp, String pattern){
		return format(new Date(timestamp.getTime()), pattern);
	}
	
	public static String formatDefaultPattern(Date date){
		DateFormat df = new SimpleDateFormat(defaultPattern);
		return df.format(date);
	}
	
	public static String formatDefaultPattern(Timestamp timestamp){
		DateFormat df = new SimpleDateFormat(defaultPattern);
		return df.format(new Date(timestamp.getTime()));
	}
	
}
