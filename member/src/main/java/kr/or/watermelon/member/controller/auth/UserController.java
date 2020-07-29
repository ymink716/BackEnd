package kr.or.watermelon.member.controller.auth;

import kr.or.watermelon.member.advice.exception.CUserNotFoundException;
import kr.or.watermelon.member.entity.User;
import kr.or.watermelon.member.model.response.CommonResult;
import kr.or.watermelon.member.model.response.ListResult;
import kr.or.watermelon.member.model.response.SingleResult;
import kr.or.watermelon.member.repo.UserJpaRepo;
import kr.or.watermelon.member.service.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"2. User"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth")
public class UserController {

    private final UserJpaRepo userJpaRepo;
    private final ResponseService responseService; // 결과를 처리할 Service

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 단건 조회", notes = "인증받은 사용자의 아이디(email)로 회원을 조회한다")
    @GetMapping(value = "/user")
    public SingleResult<String> findUser() {
        // SecurityContext에서 인증받은 회원의 정보를 얻어온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userJpaRepo.findByUid(email).orElseThrow(CUserNotFoundException::new);
        // 결과데이터가 단일건인경우 getSingleResult를 이용해서 결과를 출력한다.
        return responseService.getSingleResult(user.toString());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 수정", notes = "인증받은 사용자의 회원 정보 중 하나의 정보를 수정한다 (수정된 정보 하나씩 반영)")
    @PutMapping(value = "/user")
    public SingleResult<String> modify(@ApiParam(value = "회원 이름", required = true) @RequestParam String name,
                                     @ApiParam(value = "모바일 번호", required = true) @RequestParam String phoneNo,
                                     @ApiParam(value = "생년월일", required = true) @RequestParam String dateOfBirth,
                                     @ApiParam(value = "성별", required = true) @RequestParam String gender) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userJpaRepo.findByUid(email).orElseThrow(CUserNotFoundException::new);

        if (!user.getName().equals(name)) {
            System.out.println(name);
            user.setName(name);
        } else if (!user.getPhoneNo().equals(phoneNo)) {
            System.out.println(phoneNo);
            user.setPhoneNo(phoneNo);
        } else if (!user.getDateOfBirth().equals(dateOfBirth)) {
            System.out.println(dateOfBirth);
            user.setDateOfBirth(dateOfBirth);
        } else if (!user.getGender().equals(gender)) {
            System.out.println(gender);
            user.setGender(gender);
        }

        User modifiedUser = userJpaRepo.save(user);
        return responseService.getSingleResult(modifiedUser.toString());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 삭제 (회원 탈퇴)", notes = "인증받은 사용자의 아이디(email)로 회원정보를 삭제한다")
    @DeleteMapping(value = "/user")
    public CommonResult delete(
            @ApiParam(value = "회원번호", required = true) @PathVariable long id) {

        // SecurityContext에서 인증받은 회원의 정보를 얻어온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userJpaRepo.findByUid(email).orElseThrow(CUserNotFoundException::new);

        userJpaRepo.deleteById(user.getId());
        // 성공 결과 정보만 필요한경우 getSuccessResult()를 이용하여 결과를 출력한다.
        return responseService.getSuccessResult();
    }
}