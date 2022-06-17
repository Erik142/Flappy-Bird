package dev.wahlberger.flappybird.model.sprite;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import dev.wahlberger.flappybird.model.AudioHandler;
import dev.wahlberger.flappybird.sprite.PipePairSprite;

public class PipesModel extends AbstractSpriteModel<PipesModel> implements Iterable<PipePairSprite> {
   private final int NUM_OF_PIPES = 7;
   private final int GAP_BETWEEN_PIPES = 275;

   private final List<PipePairSprite> PIPES;

   private PipePairSprite nextPipe = null;
   private PipePairSprite lastPipe = null;

   private final int FLOOR_POSITION;
   private final int WINDOW_WIDTH;

   private final BirdModel BIRD_MODEL;

   public PipesModel(BirdModel birdModel, int floorPosition, int windowWidth) {
      super();

      this.BIRD_MODEL = birdModel;

      this.PIPES = new ArrayList<>();

      this.FLOOR_POSITION = floorPosition;
      this.WINDOW_WIDTH = windowWidth;
   }

   public void addPipe(PipePairSprite pipe) {
      this.PIPES.add(pipe);
   }
   
   public void generate(int amount) throws IOException, URISyntaxException {
      this.PIPES.clear();

      for (int i = 0; i < amount; i++) {
         PipePairSprite sprite = new PipePairSprite(FLOOR_POSITION, WINDOW_WIDTH + 300 + i*GAP_BETWEEN_PIPES);
         this.PIPES.add(sprite);
      }

     lastPipe = this.PIPES.get(amount - 1);
     nextPipe = this.PIPES.get(0);
   }

   public void generate() throws IOException, URISyntaxException {
      generate(NUM_OF_PIPES);
   }
   
   public PipePairSprite getLast() {
      return this.lastPipe;
   }
   
   public PipePairSprite getNext() {
      return this.nextPipe;
   }

   public Collection<PipePairSprite> getPipes() {
      return PIPES;
   }
   
   public void update() {
      for (int i = 0; i < this.PIPES.size(); i++) {
         PipePairSprite sprite = PIPES.get(i);
         sprite.movePipes();

         if (sprite.equals(nextPipe) && (sprite.getXPosition() + sprite.getTopPipe().getImage().getWidth()) < BIRD_MODEL.getPosition().x) {
             nextPipe = PIPES.get((i + 1) % NUM_OF_PIPES);
             
             /*
             try {
                 scoreBoard.increaseScore();
                 playSound("point.wav");
             } catch (URISyntaxException | IOException e) {
                 e.printStackTrace();
             }
             */
         }

         if (sprite.isOutsideOfScreen()) {
            sprite.setXPosition(lastPipe.getXPosition() + GAP_BETWEEN_PIPES);
            sprite.generatePositions();
            lastPipe = sprite;
         }
     }
   }

   @Override
   public Iterator<PipePairSprite> iterator() {
      return PIPES.iterator();
   }

   public void reset() {
      for (PipePairSprite pipePair : PIPES) {
         pipePair.reset();
      }

      nextPipe = PIPES.get(0);
      lastPipe = PIPES.get(PIPES.size() - 1);
   }
}
