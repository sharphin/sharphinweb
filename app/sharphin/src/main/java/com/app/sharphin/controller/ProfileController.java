package com.app.sharphin.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
	public String editProfile(Model model,@PathVariable String user_id) {
		UserDto user = service.getUserDto(user_id);
		model.addAttribute("user", user);
		model.addAttribute("inon_path", user.icon_path());
		return "edit_profile";
	}
	@PostMapping("/{user_id}/edit/save")
	public String updateProfile(@ModelAttribute UserDto userinfo,@RequestParam("old_user_id") String user_id,Model model) {
		LocalDateTime nowtime = LocalDateTime.now();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		UserDto dto = new UserDto(userinfo.user_id(), 
		                        userinfo.user_name(),
							    userinfo.email(),
								encoder.encode(userinfo.password()),
								userinfo.icon_path(), 
								userinfo.authority(), 
								false, 
								nowtime, 
								nowtime);
		UserSighInDto userDetails = new UserSighInDto(dto);
		SecurityContext auth = SecurityContextHolder.getContext();
		auth.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,userDetails.getUser_id(),userDetails.getAuthorities()));
		service.userUpdate(dto,user_id);
		return viewProfile(model,userinfo.user_id());
	}
	@PostMapping("/{user_id}/edit/iconsave")
	@ResponseBody
	public int updateIcon(@RequestBody MultipartFile file,
	                      @PathVariable String user_id,Model model) {
		int result = 0;
		LocalDate today = LocalDate.now();
		String old_path = service.getUserDto(user_id).icon_path();
		Path p = Paths.get("C:\\images\\icon\\"+old_path);
        try {
            String filename = user_id+"_icon_"+today.format(DateTimeFormatter.ofPattern("yyyyMMdd"))+".jpg";
            String filePath = "C:\\images\\icon\\" + filename;
			result = service.iconUpDate(user_id,filename);
            byte[] content = file.getBytes();
			if (!old_path.equals(filename)) Files.delete(p);
			model.addAttribute("icon_path", filename);
			if (Files.exists(p)) Files.delete(p);
			Files.write(Paths.get(filePath), content);
        } catch (IOException e) {
			return -1;
        }
		return result;
	}
	
	@RequestMapping("/geticon")
	@ResponseBody
	public HttpEntity<byte[]> getImg(@RequestParam("name") String fileName){
		File fileImg = new File("C:\\images\\icon\\"+ fileName);
		
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
