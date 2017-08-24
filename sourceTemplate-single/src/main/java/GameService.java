package ${groupId}.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import ${groupId}.domain.Result;
import ${groupId}.domain.VersionModel;

public interface GameService {
	
	public Result uploadClientFile(MultipartFile multipartFile, String newFeature, String clientVersion, String urarPath);
	
	public Result getVersionList(VersionModel versionModel);
	
	public VersionModel getVersion();
}
