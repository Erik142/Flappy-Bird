package dev.wahlberger.flappybird.sprite;

import java.io.IOException;
import java.net.URISyntaxException;

import dev.wahlberger.flappybird.sprite.PipeSprite.PipePosition;

public class PipePairSprite {
   private PipeSprite bottomPipe = null;
   private PipeSprite topPipe = null;
   
   public PipePairSprite() throws URISyntaxException, IOException {
       bottomPipe = new PipeSprite(PipePosition.Bottom);
       topPipe = new PipeSprite(PipePosition.Top);
   }
}
