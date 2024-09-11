package shop.biday.oauth2.OauthDto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import shop.biday.model.domain.UserModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final UserModel userModel;

    public CustomOAuth2User(UserModel userModel) {


        this.userModel =userModel;
    }

    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userModel.getRoleAsString()));
        return authorities;
    }
    @Override
    public String getName() {

        return userModel.getName();
    }

    public String getUsername() {

        return userModel.getUsername();
    }

    public String getEmail() {

        return userModel.getEmail();
    }
}
