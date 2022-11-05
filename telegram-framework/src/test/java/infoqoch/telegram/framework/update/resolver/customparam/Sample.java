package infoqoch.telegram.framework.update.resolver.customparam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sample {
    private Long updateId;
    private LocalDateTime regDt;
}
