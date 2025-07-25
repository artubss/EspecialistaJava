package com.banco.domain;
package com.banco.domain;
package com.banco.domain;

import java.math.BigDecimal;

public interface Tributavel {
    BigDecimal calcularTributo();
}
import java.math.BigDecimal;

public interface Tributavel {
    BigDecimal calcularTributo();
}
import java.math.BigDecimal;

public interface Tributavel {

    BigDecimal calcularImposto();

    default boolean isento() {
        return calcularImposto().compareTo(BigDecimal.ZERO) == 0;
    }
}
