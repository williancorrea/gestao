package br.com.gestao.utils.jpa;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;

import java.io.IOException;

@JsonComponent
public class PageJsonSerializer extends JsonSerializer<Page<?>> {

    @Override
    public void serialize(Page<?> page, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        gen.writeStartObject();

        gen.writeNumberField("totalPaginas", page.getTotalPages());
        gen.writeNumberField("totalItens", page.getTotalElements());
        gen.writeNumberField("paginaAtual", page.getNumber());
        gen.writeNumberField("itensPorPagina", page.getSize());

//        gen.writeObjectField("sort", page.getSort());
//        gen.writeObjectField("pageable", page.getPageable());
        gen.writeObjectField("conteudo", page.getContent());

        gen.writeEndObject();
    }

}
