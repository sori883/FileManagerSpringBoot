package com.example.demo.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.auth.SimpleLoginUser;
import com.example.demo.entity.Directory;
import com.example.demo.entity.File;
import com.example.demo.entity.User;
import com.example.demo.service.AccountService;
import com.example.demo.service.ContentsService;
import com.example.demo.service.DirService;
import com.example.demo.service.FileService;
import com.example.demo.service.StorageService;

import lombok.extern.slf4j.Slf4j;
@Controller
@RequestMapping(value = "members")
@Slf4j
public class ContentsController {
	private final AccountService accountService;
	private final DirService dirService;
	private final FileService fileService;
	private final ContentsService contentsService;
	private final StorageService storageService;
	
	
	public ContentsController(AccountService accountService, DirService dirService, FileService fileService,
							  ContentsService contentsService, StorageService storageService) {
		this.accountService = accountService;
		this.dirService = dirService;
		this.fileService = fileService;
		this.contentsService = contentsService;
		this.storageService = storageService;
	}
	
	/************************************************************************************
	 *  画面遷移
	 ************************************************************************************/
	
	@RequestMapping(value = "/", method=RequestMethod.GET)
	public String any(Principal principal) {
		Authentication authentication = (Authentication) principal;
		SimpleLoginUser loginUser = (SimpleLoginUser) authentication.getPrincipal();
		log.info("#any id:{}, name:{}", loginUser.getUser().getId(), loginUser.getUser().getName());
		contentsService.doService();
		
		return "members/index";
	}

	@RequestMapping(value = "user", method=RequestMethod.GET)
	// @AuthenticationPrincipalで認証しているユーザ情報をSimpleLoginUserに渡す
	public String user(@AuthenticationPrincipal SimpleLoginUser loginUser) {
		log.info("#user id:{}, name:{}", loginUser.getUser().getId(), loginUser.getUser().getName());
		log.info(ServletUriComponentsBuilder.fromCurrentContextPath().toString());
        
		return "members/user/index";
	}

	@RequestMapping(value = "admin", method=RequestMethod.GET)
	public String admin(@AuthenticationPrincipal(expression = "user") User user) {
		log.info("#admin id:{}, name:{}", user.getId(), user.getName());
		return "members/admin/index";
	}
	
	/************************************************************************************
	 *  User API
	 ************************************************************************************/

	@RequestMapping(value="createDir", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void createDir(@RequestBody CreateDirForm createDirForm, 
						  @AuthenticationPrincipal(expression = "user") User user) {
		
		LocalDateTime d = LocalDateTime.now();
		String dirPath = "";
		
		// Pathが0の場合は、ホームディレクトリなので、nullを代入する
		dirPath = createDirForm.getPath().equals("0") ? null : createDirForm.getPath();
		
		dirService.create(createDirForm.getName(), dirPath, user.getId(), createDirForm.getAuthDir(), d);
	}
	
	@RequestMapping(value="delDir/{id}", method=RequestMethod.GET)
	@ResponseBody
	public void delDir(@PathVariable("id") Long id) {
		dirService.delete(id);
	}
	
	@RequestMapping(value="editDir/{path}", method=RequestMethod.POST)
	@ResponseBody
	public void editDir(@RequestBody EditDirForm editDirForm, @PathVariable("path") String path) {
		if (path.equals("0")) {
			path = null;
		}
		
		dirService.update(editDirForm.getId(), editDirForm.getName(), path, editDirForm.getAuthDir());
	}
	
	@RequestMapping(value="delFile/{id}", method=RequestMethod.GET)
	@ResponseBody
	public void delFile(@PathVariable("id") Long id) {
		fileService.delete(id);
	}
	
	@RequestMapping(value="editFile/{path}", method=RequestMethod.POST)
	@ResponseBody
	public void editFile(@RequestBody EditFileForm editFileForm, @PathVariable("path") String path) {
		String selectPath = "";
		Optional<Directory> directory = null;
		
		if (path.contains("-")) {
			int idIndex = path.lastIndexOf("-") == 1 ? 1 : (path.lastIndexOf("-") -1);
			selectPath = path.substring(path.length() - (idIndex));
			directory = dirService.findById(Long.parseLong(selectPath));
			fileService.update(editFileForm.getId(), editFileForm.getName(), directory.get());
			
		} else {
			if (path.equals("0")) {
				fileService.update(editFileForm.getId(), editFileForm.getName(), null);
			} else {
				selectPath = path;
				directory = dirService.findById(Long.parseLong(selectPath));
				fileService.update(editFileForm.getId(), editFileForm.getName(), directory.get());
			}
		}
	}
	
	@RequestMapping(value="loadlHomeDir", method=RequestMethod.GET)
	@ResponseBody
	public List<Directory> loadlHomeDir(@AuthenticationPrincipal(expression = "user") User user) {
		return dirService.findHomeDir(user);
	}
	
	@RequestMapping(value="loadlHomeFile", method=RequestMethod.GET)
	@ResponseBody
	public List<File> loadlHomeFile() {
		return fileService.findHomeFile();
	}
	
	@RequestMapping(value = "loadChildDir/{path}", method=RequestMethod.GET)
	@ResponseBody
	public List<Directory> loadChildDir(@PathVariable("path") String path,
										@AuthenticationPrincipal(expression = "user") User user) {
		// 0の場合はnull(ルートディレクトリのフォルダ)にする
		if (path.equals("0")) {
			path = null;
		}
		
		return dirService.findChildDir(user, path);
	}
	
	@RequestMapping(value = "loadChildFile/{id}", method=RequestMethod.GET)
	@ResponseBody
	public List<File> loadChildFile(@PathVariable("id") String id) {
		
		Optional<Directory> directory = null;
		String currentPath = ""; // directory検索用
		
		if (id.contains("-")) {
			// TODO: 4階層目から検索が上手くいかない(2-4-12-13が12-13になる。2-4-12は12になる)
			int idIndex = id.lastIndexOf("-") == 1 ? 1 : (id.lastIndexOf("-") -1);
			currentPath = id.substring(id.length() - idIndex);
			directory = dirService.findById(Long.parseLong(currentPath));
		} else {
			currentPath = id.equals("0") ? null : id;
			// TODO: 基本動作でcurrentPathがnullになることはない(Homeディレクトリじゃないから)
			directory = dirService.findById(Long.parseLong(currentPath));
			
		}
		
		return fileService.findChildFile(directory.get());
	}
	
	@RequestMapping(value = "upload/{id}", method=RequestMethod.POST)
	@ResponseBody
	public List<File> upload(@RequestParam("files") MultipartFile[] files, @PathVariable("id") String id,
									 @AuthenticationPrincipal(expression = "user") User user) {
		LocalDateTime d = LocalDateTime.now();
		
		// URLパラメータのidから親ディレクトリになるフォルダを取得する
		String currentDir = "";
		
		// 0はnull(ルートディレクトリのフォルダ):0じゃない場合は末端(1-2-5←末端は5)のidを取得
		if (id.contains("-")) {
			int idIndex = id.lastIndexOf("-") == 1 ? 1 : (id.lastIndexOf("-") -1);
			currentDir = id.substring(id.length() - idIndex);
		} else {
			currentDir = id.equals("0") ? null : id;
		}
			
		
		Optional<Directory> fetchDirectory = null;
		
		// currentDirがnullじゃない場合は親のディレクトリを取得する
		if (!(currentDir == null)) {
			fetchDirectory = dirService.findById(Long.parseLong(currentDir));
		}
		
		// fetchDirectoryがnullじゃない場合はDirectoryを取得:nullならnullを代入
		Directory directory = null;
		directory = fetchDirectory != null ? fetchDirectory.get() : null; 
		
		for (MultipartFile file : files) {
			// DB登録部分
			fileService.create(file.getOriginalFilename(), directory, file.getSize(), d, d);
			uploadFile(file);
		}
		
		// TODO:returnしてるけど使ってない
		return fileService.findHomeFile();
		
	}
	
	@RequestMapping(value = "/download/{filename:.+}", method=RequestMethod.GET)
    public Resource downloadFile(@PathVariable String filename) {
		// filenameのリソースを取得する
        Resource resource = storageService.loadAsResource(filename);        
        return resource;
    }
	
	/************************************************************************************
	 *  Administrator API
	 ************************************************************************************/
	
	@RequestMapping(value="loadlAllUser", method=RequestMethod.GET)
	@ResponseBody
	public List<User> loadAllUser() {
		return accountService.findAll(Boolean.TRUE);
	}
	
	@RequestMapping(value="addRole/{id}", method=RequestMethod.GET)
	@ResponseBody
	public void addRole(@PathVariable("id") Long id) {
		accountService.addRole(id);
	}
	
	@RequestMapping(value="delRole/{id}", method=RequestMethod.GET)
	@ResponseBody
	public void delRole(@PathVariable("id") Long id) {
		accountService.delRole(id);
	}
	
	@RequestMapping(value="delUser/{id}", method=RequestMethod.GET)
	@ResponseBody
	public void delUser(@PathVariable("id") Long id) {
		accountService.delete(id);
	}
	
	/************************************************************************************
	 *  private
	 ************************************************************************************/
	
	/**
	 * ファイルをアップロードするメソッド
	 * @param file アップロードするファイル（１つだけ）
	 * @return FileResponse
	 */
    private FileResponse uploadFile(MultipartFile file) {
        String name = storageService.store(file);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(name)
                .toUriString();

        return new FileResponse(name, uri, file.getContentType(), file.getSize());
    }
    
}
