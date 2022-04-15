package CapstoneProject.Capstoneproject1.domain.rank.controller;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.rank.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class RankController {

    private final RankService rankService;

    @ResponseBody
    @GetMapping("/rank/group")
    public ResponseDto getRankByGroup() {
        return rankService.getRankByGroup();
    }


}
