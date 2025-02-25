package practice.common.util;

import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class RandomGenerator {

  private static final Random random = new Random();

  public int nextInt(int lowerBound, int upperBound) {
    return lowerBound + random.nextInt(upperBound - lowerBound);
  }

}
