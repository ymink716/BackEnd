package kr.or.watermelon.show.proxy;

import kr.or.watermelon.show.dto.UserIdDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

//@FeignClient(name = "service-member", url="${service-member.ribbon.listOfServers}") // 유레카 없이 동작 없으면 자동으로 유레카에서 서비스명을 찾음
@FeignClient(name = "service-member")
public interface UserServiceProxy {
    @GetMapping(value = "/auth/userId")
    public UserIdDto getUserId(@RequestHeader("X-AUTH-TOKEN") String xAuthToken);
}
