package shop.biday.oauth2.UserDetailsService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import shop.biday.oauth2.OauthDto.CustomOAuth2User;
import shop.biday.oauth2.OauthDto.NaverResponse;
import shop.biday.oauth2.OauthDto.OAuth2Response;
import shop.biday.model.domain.UserModel;
import shop.biday.model.entity.Role;
import shop.biday.model.entity.UserEntity;
import shop.biday.model.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class OAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("oAuth2User: " + oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else {
            // 지원하지 않는 소셜의 경우 처리
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 : " + registrationId);
        }

        try {
            String birthYearString = oAuth2Response.getBirthyear();

            if (birthYearString == null || birthYearString.isEmpty()) {
                throw new OAuth2AuthenticationException("응답에서 생년이 누락되었습니다.");
            }

            int birthYear = Integer.parseInt(birthYearString);

            int currentYear = java.time.Year.now().getValue();
            int age = currentYear - birthYear;

            if (age < 19) {
                throw new OAuth2AuthenticationException("사용자는 19세 미만입니다.");
            }

            String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
            UserEntity existData = userRepository.findByEmail(oAuth2Response.getEmail());

            String role = "ROLE_USER";

            if (existData == null) {

                UserEntity userEntity = new UserEntity();
                userEntity.setUsername(username);
                userEntity.setName(oAuth2Response.getName());
                userEntity.setEmail(oAuth2Response.getEmail());
                userEntity.setPassword(bCryptPasswordEncoder.encode(oAuth2Response.getEmail())); // 비밀번호 암호화
                userEntity.setPhone(oAuth2Response.getMobile());
                userEntity.setRole(UserEntity.Role.valueOf(role));

                userRepository.save(userEntity);

                UserModel userModel = new UserModel();
                userModel.setUsername(username);
                userModel.setEmail(oAuth2Response.getEmail());
                userModel.setName(oAuth2Response.getName());
                userModel.setRole(Role.valueOf("ROLE_USER"));

                return new CustomOAuth2User(userModel);
            }
            else {

                existData.setUsername(username);
                existData.setPhone(oAuth2Response.getMobile());

                userRepository.save(existData);

                UserModel userModel = new UserModel();
                userModel.setUsername(username);
                userModel.setEmail(oAuth2Response.getEmail());
                userModel.setName(oAuth2Response.getName());
                userModel.setRole(Role.valueOf("ROLE_USER"));

                return new CustomOAuth2User(userModel);
            }

        } catch (NumberFormatException e) {
            throw new OAuth2AuthenticationException("잘못된 생년 형식입니다: " + e.getMessage());
        } catch (Exception e) {
            throw new OAuth2AuthenticationException("OAuth2 응답 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
