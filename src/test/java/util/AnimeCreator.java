package util;

import academy.devdojo.springboot.dominio.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaved(){
        return Anime.builder()
                .name("Mushoku Tensei")
                .build();
    }

    public static Anime createValidAnime(){
        return Anime.builder()
                .name("Mushoku Tensei")
                .build();
    }

    public static Anime  createValidUpdatedAnime(){
        return Anime.builder()
                .name("Mushoku Tensei")
                .build();
    }
}
