package shop.biday.oauth2.UserDetailsService;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import shop.biday.oauth2.OauthDto.CustomOAuth2User;
import shop.biday.oauth2.OauthDto.NaverResponse;
import shop.biday.oauth2.OauthDto.OAuth2Response;
import shop.biday.model.domain.UserModel;
import shop.biday.model.entity.enums.Role;
import shop.biday.model.entity.UserEntity;
import shop.biday.model.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserDetailsService extends DefaultOAuth2UserService {

    private static final int MINIMUM_AGE = 19;
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ALLOWED_SOCIAL_PROVIDERS = "naver";
//    private static final List<String> ALLOWED_SOCIAL_PROVIDERS = Arrays.asList("naver", "kakao");


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals(ALLOWED_SOCIAL_PROVIDERS)) {
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

            if (age < MINIMUM_AGE) {
                throw new OAuth2AuthenticationException("사용자는 " + MINIMUM_AGE + "세 미만입니다.");
            }

            String oauthName = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
            UserEntity existData = userRepository.findByEmail(oAuth2Response.getEmail());

            if (existData == null) {
                UserEntity userEntity = new UserEntity();
                userEntity.setOauthUser(oauthName);
                userEntity.setName(oAuth2Response.getName());
                userEntity.setEmail(oAuth2Response.getEmail());
                userEntity.setPassword(passwordEncoder.encode(oAuth2Response.getEmail())); // 비밀번호 암호화
                userEntity.setPhone(oAuth2Response.getMobile());
                userEntity.setRole(UserEntity.Role.valueOf(ROLE_USER));
                userEntity.setStatus(true);
                userEntity.setTotalRating(2.0);

                Long id =userRepository.save(userEntity).getId();

                UserModel userModel = new UserModel();
                userModel.setId(id);
                userModel.setOauthName(oauthName);
                userModel.setEmail(oAuth2Response.getEmail());
                userModel.setName(oAuth2Response.getName());
                userModel.setRole(Role.valueOf(ROLE_USER));

                return new CustomOAuth2User(userModel);
            } else {
                existData.setOauthUser(oauthName);
                existData.setEmail(oAuth2Response.getEmail());
                existData.setPhone(oAuth2Response.getMobile());

                userRepository.save(existData);

                UserModel userModel = new UserModel();
                userModel.setId(existData.getId());
                userModel.setOauthName(oauthName);
                userModel.setEmail(oAuth2Response.getEmail());
                userModel.setName(oAuth2Response.getName());
                userModel.setRole(Role.valueOf(ROLE_USER));

                return new CustomOAuth2User(userModel);
            }

        } catch (NumberFormatException e) {
            log.error("error message : {}", e.getMessage());
            throw new OAuth2AuthenticationException("잘못된 생년 형식입니다: " + e.getMessage());
        } catch (Exception e) {
            log.error("error message : {}", e.getMessage());
            throw new OAuth2AuthenticationException("OAuth2 응답 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
