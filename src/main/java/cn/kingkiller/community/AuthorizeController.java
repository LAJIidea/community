package cn.kingkiller.community;

import cn.kingkiller.community.dto.AcesstokenDTO;
import cn.kingkiller.community.dto.GithubUser;
import cn.kingkiller.community.mapper.UserMapper;
import cn.kingkiller.community.model.User;
import cn.kingkiller.community.provide.GithubProvide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

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

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response){
        AcesstokenDTO acesstokenDTO = new AcesstokenDTO();
        acesstokenDTO.setClient_id(clientId);
        acesstokenDTO.setClient_secret(clientSecret);
        acesstokenDTO.setCode(code);
        acesstokenDTO.setRedirect_url(redirectUrl);
        acesstokenDTO.setState(state);
        String acessToken = githubProvide.getAcessToken(acesstokenDTO);
        GithubUser githubUser = githubProvide.githubUser(acessToken);
        if(githubUser !=null && githubUser.getId()!=null){
            //登录成功，写cookie和session
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatar_url());
            userMapper.insert(user);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";

        }else{
            //登录失败，重新登录
            return "redirect:/";
        }
    }
}
