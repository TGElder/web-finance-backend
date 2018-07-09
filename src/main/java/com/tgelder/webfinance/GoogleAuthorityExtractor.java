package com.tgelder.webfinance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class GoogleAuthorityExtractor implements AuthoritiesExtractor {

  @Override
  public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
    String id = (String) map.get("id");
    log.info(id);
    // Check if we've already registered this uer
    if (id.equals("###")) {
      log.info("GRANTING ADMIN RIGHTS");
      return AuthorityUtils.createAuthorityList("ROLE_ADMIN");
    } else {
      return AuthorityUtils.createAuthorityList();
    }
  }
}