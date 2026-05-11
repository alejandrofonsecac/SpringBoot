package academy.devdojo.springboot.mapper;

import academy.devdojo.springboot.dominio.Anime;
import academy.devdojo.springboot.request.AnimePostRequestBody;
import academy.devdojo.springboot.request.AnimePutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

//Anotações:
//{1} --- Diz ao MapStruct para gerar automaticamente a implementação do mapper e registrar como bean do Spring.

@Mapper(componentModel =  "spring") //{1}
public abstract class AnimeMapper {
    public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);
    public abstract Anime toAnime(AnimePostRequestBody animePostRequestBody);
    public abstract Anime toAnime(AnimePutRequestBody animePutRequestBody);

}
