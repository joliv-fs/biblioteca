package com.exemplo.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Libera a API para ser chamada pelo app que roda no navegador (Expo web).
 *
 * @Configuration → o Spring lê esta classe quando a API sobe.
 *                WebMvcConfigurer → interface que permite ajustar a
 *                configuração web do Spring.
 *                Aqui, só ajustamos o CORS.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                // Vale para TODOS os endereços da API (/autores, /livros, /generos...).
                .addMapping("/**")

                // Quem pode chamar: qualquer página em localhost, em qualquer porta.
                // O Expo web abre na 8081, mas, se ela estiver ocupada, usa a 8082, 8083...
                // O "*" na porta cobre todos esses casos, e só vale para a própria máquina.
                .allowedOriginPatterns("http://localhost:*")

                // Quais métodos HTTP o app pode usar.
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}