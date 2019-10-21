package cn.kingkiller.community;

import cn.kingkiller.community.dto.AcesstokenDTO;
import cn.kingkiller.community.dto.GithubUser;
import cn.kingkiller.community.provide.GithubProvide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvide githubProvide;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.url}")
    private String redirectUrl;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state){
        AcesstokenDTO acesstokenDTO = new AcesstokenDTO();
        acesstokenDTO.setClient_id(clientId);
        acesstokenDTO.setClient_secret(clientSecret);
        acesstokenDTO.setCode(code);
        acesstokenDTO.setRedirect_url(redirectUrl);
        acesstokenDTO.setState(state);
        String acessToken = githubProvide.getAcessToken(acesstokenDTO);
        GithubUser user = githubProvide.githubUser(acessToken);
        System.out.println(user.getName());
        return "index";
    }
}
