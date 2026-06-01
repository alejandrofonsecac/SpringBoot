package util;

import academy.devdojo.springboot.request.AnimePostRequestBody;

public class AnimePostRequestBodyCreator {
    public static AnimePostRequestBody createAnimePostRequestBody(){
        return AnimePostRequestBody.builder()
                .name(AnimeCreator.createAnimeToBeSaved().getName())
                .build();
    }
}
