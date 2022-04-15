package CapstoneProject.Capstoneproject1.domain.rank.controller;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.rank.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletRequest;

@Controller
@RequiredArgsConstructor
public class RankController {

    private final RankService rankService;

    @ResponseBody
    @GetMapping("/rank/group")
    public ResponseDto getRankByGroup() { // 동아리 랭킹 조회
        return rankService.getRankByGroup();
    }

    @ResponseBody
    @GetMapping("/rank/group/user")
    public ResponseDto getRankByUserInGroup(ServletRequest request){ // 동아리내 개인 랭킹 조회
        return rankService.getRankByUserInGroup(request);
    }

    @ResponseBody
    @GetMapping("/rank/user")
    public ResponseDto getRankByUser(){ // 전체 개인 랭킹 조회
        return rankService.getRankByUser();
    }


}
