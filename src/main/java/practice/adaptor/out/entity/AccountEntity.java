package practice.adaptor.out.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity(name = "account")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  UserEntity user;

  String type;
  BigDecimal balance;

  LocalDateTime updatedAt;
  LocalDateTime createdAt;
}
