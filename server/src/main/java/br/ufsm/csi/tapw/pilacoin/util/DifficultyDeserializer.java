package br.ufsm.csi.tapw.pilacoin.util;

import br.ufsm.csi.tapw.pilacoin.model.Difficulty;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;

public class DifficultyDeserializer extends JsonDeserializer<Difficulty> {

    @Override
    public Difficulty deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.readValueAsTree();

        String dificuldade = node.get("dificuldade").asText();
        Date inicio = new Date(node.get("inicio").asLong());
        Date validadeFinal = new Date(node.get("validadeFinal").asLong());

        BigInteger diff = new BigInteger(dificuldade, 16).abs();

        return Difficulty.builder()
            .dificuldade(diff)
            .inicio(inicio)
            .validadeFinal(validadeFinal)
            .build();
    }
}

