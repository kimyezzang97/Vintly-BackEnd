package kr.vintly.member;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.vintly.Entity.RefreshEntity;
import kr.vintly.common.exception.member.JoinConflictException;
import kr.vintly.common.model.Message;
import kr.vintly.common.exception.StatusEnum;
import kr.vintly.jwt.JWTUtil;
import kr.vintly.member.model.req.ReqJoinDTO;
import kr.vintly.refresh.RefreshRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;

@RestController
@RequestMapping("/members")
public class MemberController {

    private MemberService memberService;
    private JWTUtil jwtUtil;
    private RefreshRepository refreshRepository;

    @Autowired
    public MemberController(MemberService memberService, JWTUtil jwtUtil, RefreshRepository refreshRepository){
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    // ID 중복 확인
    @GetMapping("/id/{id}")
    //@ResponseStatus(HttpStatus.OK) HTTP STATUS 예시
    public ResponseEntity<?> getChkId(@PathVariable("id") String id){
        //HttpHeaders headers=new HttpHeaders(); headers.add("Custom-Header","bar");
        //return new ResponseEntity<>(memberService.getChkId(id), headers, HttpStatus.OK); // 헤더까지 수정 가능
        return ResponseEntity.ok(Message.builder()
                .status(StatusEnum.OK)
                .message("")
                .data(memberService.getChkId(id)).build());
    }

    // email 중복 체크
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getChkEmail(@PathVariable("email") String email){
        return ResponseEntity.ok(Message.builder()
                .status(StatusEnum.OK)
                .message("")
                .data(memberService.getChkEmail(email))
                .build());
    }


    // nickname 중복 체크
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<?> getChkNickname(@PathVariable("nickname") String nickname){
        return ResponseEntity.ok(Message.builder()
                .status(StatusEnum.OK)
                .message("")
                .data(memberService.getChkNickname(nickname))
                .build());
    }

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<?> createMember(@Valid @RequestBody ReqJoinDTO reqJoinDTO) throws MessagingException, IOException {
        memberService.createMember(reqJoinDTO);

        return ResponseEntity.ok(Message.builder()
                .status(StatusEnum.OK)
                .message(reqJoinDTO.getMemberId() + " ID로 회원가입을 성공하였습니다.")
                .build());
    }

    // 회원가입 인증
    @GetMapping("/enable")
    public String enableMember(@RequestParam String id, @RequestParam String emailCode)  {
        memberService.enableMember(id, emailCode);
        return "이메일 인증에 성공하였습니다. 로그인 후 사용 해주세요.";
    }

    // ID 찾기
    @GetMapping("/find/id")
    public ResponseEntity<?> findId(@RequestParam String name, @RequestParam Date birth){
        return ResponseEntity.ok(Message.builder()
                .status(StatusEnum.OK)
                .data(memberService.findId(name, birth))
                .message("")
                .build());
    }

    // 토큰 재발급?
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response){
        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {

            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {

            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefreshToken(refresh);
        if (!isExist) {

            //response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshRepository.deleteByRefreshToken(refresh);
        addRefreshEntity(username, newRefresh, 86400000L);

        //response
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = RefreshEntity.builder()
                .memberId(username)
                .refreshToken(refresh)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refreshEntity);
    }
}
