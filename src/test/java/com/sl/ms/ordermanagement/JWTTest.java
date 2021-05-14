package com.sl.ms.ordermanagement;

import com.sl.ms.ordermanagement.config.AuthenticationRequest;
import com.sl.ms.ordermanagement.config.JwtUtil;
import com.sl.ms.ordermanagement.items.ItemsRepository;
import com.sl.ms.ordermanagement.orders.OrdersRepository;
import com.sl.ms.ordermanagement.service.CustomUserDetailsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HelloWorldController.class)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class JWTTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @MockBean
    private OrdersRepository ordersRepository;

    @MockBean
    private ItemsRepository itemsRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;


    @Test
    public void createTokenTest() throws Exception {

        AuthenticationRequest req = new AuthenticationRequest();
        req.setUsername("foo");
        req.setPassword("foo");
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())))
                .thenReturn(authentication);
        this.mockMvc.perform(post("/authenticate")).andExpect(status().isBadRequest());
    }


}
