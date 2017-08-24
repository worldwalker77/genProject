package ${groupId}.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ${groupId}.common.exception.ExceptionEnum;
import ${groupId}.common.utils.RarUtil;
import ${groupId}.dao.VersionDao;
import ${groupId}.domain.Result;
import ${groupId}.domain.VersionModel;

@Service
public class GameServiceImpl implements GameService{
	
	private static final String FILE_PERSISTENCE_PAHT = "/home/data/clientversion/";//"D:/test/";
	
//	private static final String UNRAR_PATH = "/home/tomcat-file/apache-tomcat-7.0.73/ROOT/clientversion/";//"D:/test/";
	
	private static final String FILE_PERSISTENCE_PAHT1 = "C:/Users/jinfeng.liu/Desktop/rar1/";//"D:/test/";
	
	private static final String UNRAR_PATH1 = "C:/Users/jinfeng.liu/Desktop/rar2/";//"D:/test/";
	
	private static final String UPDATE_RUL = "http://file.wyqp.worldwalker.cn/clientversion/VERSION";//"D:/test/";
	
	private static final String CODE_URL = "http://file.wyqp.worldwalker.cn/clientversion/VERSION/game_code_VERSION.zip";//"D:/test/";
	@Autowired
	private VersionDao versionDao;

	@Override
	public Result uploadClientFile(MultipartFile multipartFile, String newFeature, String clientVersion, String unrarPath) {
		Result result = new Result();
		String fileName = multipartFile.getOriginalFilename();
        try {
        	
        	/**删除持久化目录下文件*/
        	RarUtil.deleteDir(new File(FILE_PERSISTENCE_PAHT));
        	/**重新创建目录*/
        	RarUtil.mkDir(FILE_PERSISTENCE_PAHT);
        	/**将文件存储到持久化目录下**/
			SaveFileToVirtualDir(multipartFile.getInputStream(), FILE_PERSISTENCE_PAHT, fileName);
			
			/**将持久化目录中的文件解压到file-manager的classespath 下的clientversion目录中*/
			String sourceRarPath = FILE_PERSISTENCE_PAHT + fileName;
			File sourceRar = new File(sourceRarPath);
			
			/**删除解压目录*/
        	RarUtil.deleteDir(new File(unrarPath));
        	/**重新创建解压目录*/
        	RarUtil.mkDir(unrarPath);
        	
			File destDir = new File(unrarPath);
			RarUtil.unrar(sourceRar, destDir);
			
			/**将下载链接持久化到数据库中*/
			String version = fileName.substring(0, fileName.length() - 4);
			String updateUrl = UPDATE_RUL.replace("VERSION", version);
			String codeUrl = CODE_URL.replace("VERSION", version);
			VersionModel versionModel = new VersionModel();
			versionModel.setCodeUrl(codeUrl);
			versionModel.setUpdateUrl(updateUrl);
			versionModel.setNewFeature(newFeature);
			versionModel.setClientVersion(clientVersion);
			versionDao.updateVersion(versionModel);
			
		} catch (Exception e) {
			result.setCode(ExceptionEnum.SYSTEM_ERROR.code);
			result.setDesc(ExceptionEnum.SYSTEM_ERROR.desc);
			e.printStackTrace();
		}
		return result;
	}
	
	public void SaveFileToVirtualDir(InputStream stream,String path,String fileName) throws Exception{ 
		  
		  FileOutputStream fs=new FileOutputStream( path + fileName);
		  byte[] buffer =new byte[1024*1024];
		  int bytesum = 0;
		  int byteread = 0;
		  while ((byteread=stream.read(buffer))!=-1){
		     bytesum+=byteread;
		     fs.write(buffer,0,byteread);
		     fs.flush();
		  }
		  fs.close();
		  stream.close();     
	}
	
	public static void main(String[] args) {
		String fileName = "1111.rar";
		String version = fileName.substring(0, fileName.length() - 4);
		System.out.println(version);
	}

	@Override
	public Result getVersionList(VersionModel versionModel) {
		List<VersionModel> list = versionDao.selectVersionList(versionModel);
		Long total = versionDao.selectVersionListCount(versionModel);
		Result result = new Result();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", total);
		result.setData(map);
		return result;
	}

	@Override
	public VersionModel getVersion() {
		return versionDao.selectVersion(new VersionModel());
	}
	
}
