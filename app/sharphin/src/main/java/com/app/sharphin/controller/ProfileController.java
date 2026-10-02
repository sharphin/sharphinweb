package com.app.sharphin.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.app.sharphin.dto.user.UserDto;
import com.app.sharphin.dto.user.UserSighInDto;
import com.app.sharphin.service.FollowUserService;
import com.app.sharphin.service.UserService;

@Controller
public class ProfileController {
    @Autowired
    UserService service;
	@Autowired
    FollowUserService fservice;
	private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
	@Value("${sharphin.icon-dir}")
	String iconDir;
	private static final Pattern ICON_FILE_NAME = Pattern.compile("[A-Za-z0-9_-]+\\.(jpg|png)");

	private static void requireSelf(UserSighInDto loginUser, String user_id) {
		if (!loginUser.getUser_id().equals(user_id)) throw new AccessDeniedException("not allowed to edit user: " + user_id);
	}
	@GetMapping("/{user_id}")
	public String viewProfile(Model model,@PathVariable String user_id) {
        UserDto user = service.getUserDto(user_id);
		int follow = fservice.findFollowCount(user_id);
		int follower = fservice.findFollowerCount(user_id);
		model.addAttribute("follow", follow);
		model.addAttribute("follower", follower);	
        model.addAttribute("user", user);
		return "profile";
	}
	@GetMapping("/{user_id}/edit")
	public String editProfile(Model model,@PathVariable String user_id,@AuthenticationPrincipal UserSighInDto loginUser) {
		requireSelf(loginUser, user_id);
		UserDto user = service.getUserDto(user_id);
		model.addAttribute("user", user);
		model.addAttribute("inon_path", user.icon_path());
		return "edit_profile";
	}
	@PostMapping("/{user_id}/edit/save")
	public String updateProfile(@ModelAttribute UserDto userinfo,@AuthenticationPrincipal UserSighInDto loginUser,Model model,
	                            HttpServletRequest request,HttpServletResponse response) {
		// 更新対象は常にログインユーザー本人。権限・状態・アイコンはフォームの値を使わず DB の値を引き継ぐ
		String user_id = loginUser.getUser_id();
		UserDto current = service.getUserDto(user_id);
		if (!user_id.equals(userinfo.user_id()) && service.existUser(userinfo.user_id())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "user_id already exists");
		}
		LocalDateTime nowtime = LocalDateTime.now();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		UserDto dto = new UserDto(userinfo.user_id(), 
		                        userinfo.user_name(),
							    userinfo.email(),
								encoder.encode(userinfo.password()),
								current.icon_path(), 
								current.authority(), 
								current.disable(), 
								nowtime, 
								nowtime);
		UserSighInDto userDetails = new UserSighInDto(dto);
		service.userUpdate(dto,user_id);
		// user_id 変更後もセッションのログイン情報が新しい ID を指すよう、セッションへ保存し直す
		SecurityContext auth = SecurityContextHolder.createEmptyContext();
		auth.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities()));
		SecurityContextHolder.setContext(auth);
		securityContextRepository.saveContext(auth, request, response);
		return viewProfile(model,userinfo.user_id());
	}
	@PostMapping("/{user_id}/edit/iconsave")
	@ResponseBody
	public int updateIcon(@RequestBody MultipartFile file,
	                      @PathVariable String user_id,@AuthenticationPrincipal UserSighInDto loginUser,Model model) {
		requireSelf(loginUser, user_id);
		int result = 0;
		LocalDate today = LocalDate.now();
		String old_path = service.getUserDto(user_id).icon_path();
        try {
            String filename = user_id+"_icon_"+today.format(DateTimeFormatter.ofPattern("yyyyMMdd"))+".jpg";
            Path filePath = Paths.get(iconDir, filename);
			result = service.iconUpDate(user_id,filename);
            byte[] content = file.getBytes();
			if (old_path != null) Files.deleteIfExists(Paths.get(iconDir, old_path));
			model.addAttribute("icon_path", filename);
			Files.createDirectories(filePath.getParent());
			Files.write(filePath, content);
        } catch (IOException e) {
			return -1;
        }
		return result;
	}
	
	@RequestMapping("/geticon")
	@ResponseBody
	public HttpEntity<byte[]> getImg(@RequestParam("name") String fileName){
		// ../ などで保存先ディレクトリの外を読まれないよう、ファイル名の形式を限定する
		if (fileName == null || !ICON_FILE_NAME.matcher(fileName).matches()) return null;
		File fileImg = Paths.get(iconDir, fileName).toFile();
		
		byte[] byteImg = null;
		HttpHeaders headers = null;
		try {
			byteImg = Files.readAllBytes(fileImg.toPath());
			headers = new HttpHeaders();
			
			headers.setContentType(MediaType.IMAGE_PNG);
			headers.setContentLength(byteImg.length);
		} catch(IOException e) {
			return null;
		}
		return new HttpEntity<byte[]>(byteImg,headers);
	}
}
