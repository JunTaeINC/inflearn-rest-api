package hello.study.restapi.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MemberServiceTest {


	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@Test
	void findByUsername() {
		String username = "jjj@naver.com";
		String password = "asdf";

		Member member = Member.builder()
			.email(username)
			.password(password)
			.roles(Set.of(MemberRole.ADMIN, MemberRole.USER))
			.build();

		memberRepository.save(member);

		UserDetailsService userDetailsService = memberService;
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		assertThat(userDetails.getPassword()).isEqualTo(password);
	}

	@Test
	void findByUsername_Fail() {
		String username = "random@naver.com";

		Exception exception = assertThrows(UsernameNotFoundException.class, () -> memberService.loadUserByUsername(username));

		assertTrue(exception.getMessage().contains(username));
	}
}