package br.ufsm.csi.tapw.pilacoin.service;

import br.ufsm.csi.tapw.blueprints.Observable;
import br.ufsm.csi.tapw.blueprints.Observer;
import br.ufsm.csi.tapw.pilacoin.model.Difficulty;
import br.ufsm.csi.tapw.pilacoin.util.JacksonUtil;
import br.ufsm.csi.tapw.pilacoin.util.JournalUtil;
import lombok.Data;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class DifficultyService implements Observable<Difficulty> {
    private final List<Observer<Difficulty>> observers = new ArrayList<>();

    private Difficulty difficulty;

    public DifficultyService(

            BlockDiscoveryService blockDiscoveryService,
            BlockValidationService blockValidationService,
            PilaCoinMinecraftService pilaCoinMiningService,
            PilaCoinValidationService validationService) {
        this.subscribe(pilaCoinMiningService);
        this.subscribe(validationService);
        this.subscribe(blockDiscoveryService);
        this.subscribe(blockValidationService);
    }

    @RabbitListener(queues = "${queue.dificuldade}")
    public void onReceiveDifficulty(@Payload String difficultyStr) {
        Difficulty difficulty = JacksonUtil.convert(difficultyStr, Difficulty.class);

        if (this.difficulty == null || !difficulty.getDificuldade().equals(this.difficulty.getDificuldade())) {

            this.updateDifficulty(difficulty);
        }
    }

    private void updateDifficulty(Difficulty diff) {
        JournalUtil.logRoundBox("DIFICULDADE\n---\n" + diff.getDificuldade());

        this.difficulty = diff;

        if (this.observers.isEmpty())
            return;

        for (var observer : observers) {
            observer.update(this.difficulty);
        }
    }

    @Override
    public void subscribe(Observer<Difficulty> observer) {
        this.observers.add(observer);
    }

    @Override
    public void unsubscribe(Observer<Difficulty> observer) {
        this.observers.remove(observer);
    }
}