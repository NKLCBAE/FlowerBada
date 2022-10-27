package app.bada.flower.api.controller;


import app.bada.flower.api.dto.ResponseDto;
import app.bada.flower.api.dto.auth.OAuthRes;
import app.bada.flower.api.dto.type.SocialLoginType;
import app.bada.flower.api.dto.user.SignInResDto;
import app.bada.flower.api.dto.user.UserInfo;
import app.bada.flower.api.entity.User;
import app.bada.flower.api.service.UserService;
import app.bada.flower.api.service.auth.OAuthService;
import app.bada.flower.api.service.jwt.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Api(value = "유저 API", tags = {"유저"})
@CrossOrigin("*")
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final OAuthService oAuthService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping("/signin")
    @ApiOperation(value="카카오 소셜 로그인", notes="로그인 요청을 보냄")
    public ResponseEntity<ResponseDto> signIn() throws IOException {
        Map<String, String> map = oAuthService.request(SocialLoginType.KAKAO);
        return new ResponseEntity<>(new ResponseDto(map), HttpStatus.OK);
    }

    @PostMapping("/signin/callback")
    public ResponseEntity callback (@RequestBody Map<String, String> data) throws Exception {
        String code = data.get("code");
        System.out.println(">> 소셜 로그인 API 서버로부터 받은 code :"+ code);
        OAuthRes oAuthRes=oAuthService.oAuthLogin(SocialLoginType.KAKAO, code);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("jwt", oAuthRes.getJwtToken());
        resultMap.put("user", oAuthRes.getUser().toUserInfo());
        resultMap.put("register", oAuthRes.getRegistered());
        System.out.println(resultMap);
        return new ResponseEntity<>(new ResponseDto(resultMap), HttpStatus.OK);
    }

    @PostMapping("/signout")
    public void signOut (@RequestHeader(value = "X-AUTH-TOKEN") String jwt) throws Exception {
        userService.logout(jwt.split(" ")[1]);
    }
}
