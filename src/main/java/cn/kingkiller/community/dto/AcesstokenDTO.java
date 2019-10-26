package cn.kingkiller.community.dto;

import lombok.Data;

@Data
public class AcesstokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_url;
    private String state;
}
