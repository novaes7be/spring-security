package br.com.Novaes.spring_essentials_training.service;

import br.com.Novaes.spring_essentials_training.config.TokenProvider;
import br.com.Novaes.spring_essentials_training.database.model.AlunosEntity;
import br.com.Novaes.spring_essentials_training.database.model.RolesEntity;
import br.com.Novaes.spring_essentials_training.database.repository.IAlunosRepository;
import br.com.Novaes.spring_essentials_training.database.repository.RolesRepository;
import br.com.Novaes.spring_essentials_training.dto.AlunoDto;
import br.com.Novaes.spring_essentials_training.dto.LoginRequestDto;
import br.com.Novaes.spring_essentials_training.dto.RegisterRequestDto;
import br.com.Novaes.spring_essentials_training.dto.TokenResponseDto;
import br.com.Novaes.spring_essentials_training.enums.RoleTypeEnum;
import br.com.Novaes.spring_essentials_training.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleStatus;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final IAlunosRepository alunosRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    @Value("${jwt.expiration}")
    private long expirationTime;

    public void register(RegisterRequestDto dto) throws BadRequestException {
        AlunosEntity aluno = alunosRepository.findByEmail(dto.getEmail())
                .orElse(null);

        if (aluno != null) {
            throw  new BadRequestException("aluno ja cadastrado com este nome");
        }

        RolesEntity role = rolesRepository.findByNome(RoleTypeEnum.ROLE_ALUNO.name())
                .orElseGet(() -> rolesRepository.save(RolesEntity.builder()
                                .nome(RoleTypeEnum.ROLE_ALUNO.name())
                        .build()));

        alunosRepository.save(AlunosEntity.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .roles(Set.of(role))
                .senha(passwordEncoder.encode(dto.getSenha()))
                .build());
        passwordEncoder.matches(dto.getSenha(), "9879658746325465768787098869586475364");
    }
    public TokenResponseDto login(LoginRequestDto dto) throws Exception {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha()));
            String token = tokenProvider.gerarToken(authentication);
            //authentication provider -> UserDetailsService( carregar usuario apartir do email )
            // -> comparar senha criptografada com a que foi passada passwordEncoder.matches() -> autenticado


            return new TokenResponseDto(token, expirationTime);

        } catch (BadCredentialsException e) {
            throw new BadRequestException("Credenciais invalidas");
        } catch (Exception e){
            throw e;
        }
    }
}
