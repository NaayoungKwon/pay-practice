package practice.common.util;

import java.util.Random;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class RandomGenerator {

  private static final Random random = new Random();

  public int nextInt(int lowerBound, int upperBound) {
    return lowerBound + random.nextInt(upperBound - lowerBound);
  }

  public String getUuid(){
    String uuid = UUID.randomUUID().toString().replaceAll("[^0-9]", "");

    String shortenedUUID = uuid.substring(0, 16);

    return String.format("%s-%s-%s-%s",
        shortenedUUID.substring(0, 4), shortenedUUID.substring(4, 8),
        shortenedUUID.substring(8, 12), shortenedUUID.substring(12, 16));
  }

}
