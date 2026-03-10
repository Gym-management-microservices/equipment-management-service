package micro.gym.equipmentmanagementservice.configuration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        final Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
        
        if (realmAccess == null || realmAccess.get("roles") == null) {
            return List.of();
        }
        
        return ((List<String>) realmAccess.get("roles")).stream()
            .map(roleName -> {
                String role = roleName.toUpperCase().startsWith("ROLE_") 
                    ? roleName.toUpperCase() 
                    : "ROLE_" + roleName.toUpperCase();
                return new SimpleGrantedAuthority(role);
            })
            .collect(Collectors.toList());
    }
}