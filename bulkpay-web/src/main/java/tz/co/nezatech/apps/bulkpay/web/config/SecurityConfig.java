package tz.co.nezatech.apps.bulkpay.web.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select username,password, enabled from tbl_user where username=?")
				.authoritiesByUsernameQuery(
						"select * from \n" + "(select u.username, p.name as authority from tbl_user u \n"
								+ "left join tbl_role r on u.role_id=r.id\n"
								+ "left join tbl_role_permission rp on r.id=rp.role_id\n"
								+ "left join tbl_permission p on rp.permission_id=p.id\n" + "UNION\n"
								+ "select u.username, CONCAT('ROLE_',r.name) as authority from tbl_user u \n"
								+ "left join tbl_role r on u.role_id=r.id) as authorities\n" + "where username=?")
				.passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/img/**", "/fonts/**").permitAll()
				.antMatchers("/", "/home", "/users/pwd/**").permitAll().anyRequest().authenticated().and().formLogin()
				.loginPage("/login").successForwardUrl("/home").failureUrl("/login?error=true").permitAll().and()
				.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout=true").permitAll();

		http.exceptionHandling().accessDeniedPage("/403");
		// http.csrf().disable();

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.debug(true);

	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

}
