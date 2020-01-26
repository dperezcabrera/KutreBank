package com.github.dperezcabrera.bank.architecture.security;

import com.github.dperezcabrera.bank.architecture.auth.dtos.Features;
import com.github.dperezcabrera.bank.architecture.auth.services.FeatureService;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SimpleAuthenticationProviderTest {
    
    @Test
    public void testCheckInsecureCredentials() {
        FeatureService featureService = mock(FeatureService.class);
        String username = "sdafs";
        String password = "asdf'";
        SimpleAuthenticationProvider instance = new SimpleAuthenticationProvider(null, null, null, featureService);
        
        instance.checkInsecureCredentials(username, password);
        
        verify(featureService).useFeature(Features.SQL_INJECTION);
    }    
}
